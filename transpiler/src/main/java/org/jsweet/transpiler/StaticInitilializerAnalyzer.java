/* 
 * JSweet transpiler - http://www.jsweet.org
 * Copyright (C) 2015 CINCHEO SAS <renaud.pawlak@cincheo.fr>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.jsweet.transpiler;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.apache.log4j.Logger;
import org.jsweet.JSweetConfig;
import org.jsweet.transpiler.util.DirectedGraph;
import org.jsweet.transpiler.util.ReferenceGrabber;
import org.jsweet.transpiler.util.Util;

import standalone.com.sun.source.tree.BlockTree;
import standalone.com.sun.source.tree.ClassTree;
import standalone.com.sun.source.tree.CompilationUnitTree;
import standalone.com.sun.source.tree.ImportTree;
import standalone.com.sun.source.tree.PackageTree;
import standalone.com.sun.source.tree.Tree;
import standalone.com.sun.source.tree.VariableTree;
import standalone.com.sun.source.util.TreePathScanner;
import standalone.com.sun.source.util.Trees;

/**
 * This AST scanner creates a class dependency graph for each package, based on
 * static field initializers.
 * 
 * @author Renaud Pawlak
 * @author Louis Grignon
 */
public class StaticInitilializerAnalyzer extends TreePathScanner<Void, Trees> {

	private JSweetContext context;
	private CompilationUnitTree currentCompilationUnit;
	private int pass = 1;
	private static final Logger logger = Logger.getLogger(StaticInitilializerAnalyzer.class);
	/**
	 * A map containing the static initializers dependencies for each package when
	 * using modules (empty otherwise).
	 */
	public Map<PackageTree, DirectedGraph<CompilationUnitTree>> staticInitializersDependencies = new HashMap<>();

	/**
	 * A map containing the static initializers dependencies when not using modules
	 * (empty otherwise).
	 */
	public DirectedGraph<CompilationUnitTree> globalStaticInitializersDependencies = new DirectedGraph<>();

	/**
	 * Maps the types to the compilation units in which they are declared.
	 */
	public Map<TypeElement, CompilationUnitTree> typesToCompilationUnits = new HashMap<>();

	/**
	 * Creates the analyzer.
	 */
	public StaticInitilializerAnalyzer(JSweetContext context) {
		this.context = context;
		this.context.referenceAnalyzer = this;
	}

	private DirectedGraph<CompilationUnitTree> getGraph() {
	    return globalStaticInitializersDependencies;
	}
	
	public boolean isDependent(CompilationUnitTree cuSource, TypeElement target) {
	    CompilationUnitTree cuTarget = typesToCompilationUnits.get(target);
        if (cuSource != null && cuTarget != null) {
            return globalStaticInitializersDependencies.hasEdge(cuTarget, cuSource);
        }
        return false;
    }

	Set<TypeMirror> currentTopLevelImportedTypes = new HashSet<>();

	@Override
	public Void visitCompilationUnit(CompilationUnitTree compilationUnit, Trees trees) {

		currentCompilationUnit = compilationUnit;
		if (pass == 1) {
			getGraph().add(compilationUnit);
		} else {
			if (context.util.getPackageFullNameForCompilationUnit(compilationUnit)
					.startsWith(JSweetConfig.LIBS_PACKAGE + ".")) {

				// skip definitions
				return null;
			}
			currentTopLevelImportedTypes.clear();
			for (ImportTree importTree : compilationUnit.getImports()) {

				TypeMirror importedType = getImportedType(compilationUnit, importTree);
				if (importedType != null) {
					currentTopLevelImportedTypes.add(importedType);
				}
				// TypeElement type = Util.getImportedType(i);
				// if (type != null) {
				// CompilationUnitTree target = typesToCompilationUnits.get(type);
				// if (target != null && getGraph().contains(target)) {
				// logger.debug("adding import dependency: " +
				// currentTopLevel.getSourceFile() + " -> " +
				// target.getSourceFile());
				// getGraph().addEdge(target, currentTopLevel);
				// }
				// }

			}
		}

		super.visitCompilationUnit(compilationUnit, trees);
		currentCompilationUnit = null;

		return null;
	}

	private TypeMirror getImportedType(CompilationUnitTree compilationUnit, ImportTree importTree) {
		Tree importedIdentifier = importTree.getQualifiedIdentifier();
		TypeMirror importedType = Util.getType(importedIdentifier);
		return importedType;
	}

	@Override
	public Void visitClass(ClassTree classTree, Trees trees) {
		if (pass == 1) {
			typesToCompilationUnits.put((TypeElement) Util.getElement(classTree), currentCompilationUnit);
		} else {
			if (classTree.getExtendsClause() != null) {
				CompilationUnitTree target = typesToCompilationUnits.get(Util.getTypeElement(classTree.getExtendsClause()));
				if (target != null && getGraph().contains(target)) {
					logger.debug("adding inheritance dependency: " + currentCompilationUnit.getSourceFile() + " -> "
							+ target.getSourceFile());
					getGraph().addEdge(target, currentCompilationUnit);
				}
			}

			for (Tree member : classTree.getMembers()) {
				if (member instanceof VariableTree) {
					VariableTree field = (VariableTree) member;
					if (field.getModifiers().getFlags().contains(Modifier.STATIC) && field.getInitializer() != null
							&& !context.hasAnnotationType(Util.getElement(field), JSweetConfig.ANNOTATION_STRING_TYPE,
									JSweetConfig.ANNOTATION_ERASED)) {
						acceptReferences(field.getInitializer());
					}
				} else if (member instanceof BlockTree) {
					BlockTree initializer = (BlockTree) member;
					if (initializer.isStatic()) {
						acceptReferences(initializer);
					}
				}
			}
		}
		return super.visitClass(classTree, trees);
	}

	private void acceptReferences(Tree tree) {
		ReferenceGrabber refGrabber = new ReferenceGrabber();
		refGrabber.scan(tree, context.trees);
		for (TypeMirror referencedType : refGrabber.referencedTypes) {
			TypeElement referencedTypeElement = (TypeElement) context.types.asElement(referencedType);
			PackageElement referencedPackageElement = context.util.getParentElement(referencedTypeElement,
					PackageElement.class);
			PackageElement currentPackageElement = (PackageElement) Util.getElement(currentCompilationUnit.getPackage());

			if (!context.useModules || (Objects.equals(currentPackageElement, referencedPackageElement))) {
				CompilationUnitTree target = typesToCompilationUnits.get(referencedTypeElement);
				if (target != null && !currentCompilationUnit.equals(target) && getGraph().contains(target)) {
					logger.debug("adding static initializer dependency: " + currentCompilationUnit.getSourceFile()
							+ " -> " + target.getSourceFile());
					getGraph().addEdge(target, currentCompilationUnit);
				}
			}
		}
	}

	/**
	 * Processes all the given compilation units.
	 */
	public void process(Collection<CompilationUnitTree> compilationUnits) {
		scan(compilationUnits, context.trees);
		pass++;
		scan(compilationUnits, context.trees);
	}

}

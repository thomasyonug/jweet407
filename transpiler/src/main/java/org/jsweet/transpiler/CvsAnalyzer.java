package org.jsweet.transpiler;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;

import org.jsweet.transpiler.util.Util;

import standalone.com.sun.source.tree.*;
import standalone.com.sun.source.util.TreeScanner;
import standalone.com.sun.source.util.Trees;
import standalone.com.sun.tools.javac.tree.JCTree;
import standalone.com.sun.tools.javac.tree.JCTree.JCVariableDecl;
//import standalone.com.sun.tools.javac.util.Name;
import javax.lang.model.element.Name;
import java.util.*;

public class CvsAnalyzer extends TreeScanner<Void, Trees> {
    private Stack<List<Name>> clzScope;
    private List<Name> currentOutScope;
    private HashMap<ClassTree, List<Name>> cvsScope;
    public List<Name> getCvs(ClassTree trees) {
        return cvsScope.get(trees);
    }

    private void enterScope() {
        clzScope.add(new LinkedList<>());
    }
    private void exitScope() {
        clzScope.pop();
    }
    private void addVar(JCVariableDecl var) {
        clzScope.lastElement().add(var.getName());
    }
    private void addVar(Name name) {
        clzScope.lastElement().add(name);
    }
    private boolean exist(Name name) {
        if (isKeyword(name)) {
            return true;
        }
        if (currentOutScope.contains(name)) {
            return true;
        }
        for (var names : clzScope) {
            for (var n : names) {
                if (n.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCvsClz(ClassTree tree) {
        for (var names : cvsScope.values()) {
            for (var name : names) {
                if (name.equals(tree.getSimpleName())) {
                    return true;
                }
            }

        }
        return false;
    }
    private boolean isKeyword(Name name) {
//        var nameStr = name.toString();
//        if ()
        return false;
    }
    /**
     * Create a cross variables analyzer on local variables only (default).
     */
    public CvsAnalyzer() {
        clzScope = new Stack<>();
        cvsScope = new HashMap<>();
    }

    @Override
    public Void visitCompilationUnit(CompilationUnitTree compilationUnit, Trees trees) {
        enterScope();

        JSweetContext.currentCompilationUnit.set(compilationUnit);
        return super.visitCompilationUnit(compilationUnit, trees);
    }

    @Override
    public Void visitClass(ClassTree node, Trees trees) {
        enterScope();
        currentOutScope = new LinkedList<>();
        if (!Util.is_parallel(node)) {
            return super.visitClass(node, trees);
        }
        cvsScope.put(node, currentOutScope);
        var members = node.getMembers();
        for (var member : members) {
            if (member instanceof JCVariableDecl) {
                addVar((JCVariableDecl) member);
            }
        }
        var ret = super.visitClass(node, trees);
        exitScope();
        return ret;
    }

    @Override
    public Void visitMethod(MethodTree node, Trees trees) {
        enterScope();
        var ret = super.visitMethod(node, trees);
        exitScope();
        return ret;
    }

    @Override
    public Void visitVariable(VariableTree node, Trees trees) {
        addVar(node.getName());
        return super.visitVariable(node, trees);
    }

    @Override
    public Void visitIdentifier(IdentifierTree node, Trees trees) {
        if (!exist(node.getName())) {
            currentOutScope.add(node.getName());
        }
        return super.visitIdentifier(node, trees);
    }


}

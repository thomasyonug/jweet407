package org.jsweet.transpiler;

import org.jsweet.transpiler.util.Util;

import standalone.com.sun.source.tree.*;
import standalone.com.sun.source.util.Trees;
import standalone.com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import standalone.com.sun.source.util.TreePathScanner;

import java.util.*;

/**
 * CvsAnalyzer用于分析Java源码中的交叉变量引用。
 * 该类继承了TreeScanner<Void, Trees>，实现了对树形结构的扫描和分析。
 */
public class CvsAnalyzer extends TreePathScanner<Void, Trees> {
    private Stack<Set<String>> clzScope; // 存储类的作用域
    private Set<String> currentOutScope; // 当前类之外的作用域
    private HashMap<ClassTree, Set<String>> cvsScope; // 存储类与其交叉变量的映射
    private HashMap<ClassTree, Set<String>> volatileCvsScope; // 存储类与其volatile变量的映射
    private static final Set<String> JAVA_KEYWORDS = new HashSet<>();

    // 初始化Java关键字集合
    static {
        // 这里只列举了一部分Java关键字
        JAVA_KEYWORDS.add("Thread");
        JAVA_KEYWORDS.add("super");
        JAVA_KEYWORDS.add("Override");
        JAVA_KEYWORDS.add("System");
        // ... 其他关键字
    }

    /**
     * 获取给定类的交叉变量集合。
     *
     * @param trees 给定的类
     * @return 类的交叉变量集合
     */
    public Set<String> getCvs(ClassTree trees) {
        return cvsScope.get(trees);
    }

    public Set<String> getVolatileCvs(ClassTree trees) {
        return volatileCvsScope.get(trees);
    }

    private void enterScope() {
        clzScope.add(new HashSet<>());
    }

    private void exitScope() {
        clzScope.pop();
    }

    /**
     * 将变量添加到当前作用域。
     *
     * @param var JCVariableDecl对象
     */
    private void addVar(JCVariableDecl var) {
        clzScope.lastElement().add(var.getName().toString());
    }

    /**
     * 将变量名称添加到当前作用域。
     *
     * @param name 变量名称
     */
    private void addVar(Name name) {

        clzScope.lastElement().add(name.toString());
    }

    private void addVar(String name) {

        clzScope.lastElement().add(name);
    }

    /**
     * 检查给定变量名称是否存在于当前作用域。
     *
     * @param name 待检查的变量名称
     * @return 变量是否存在
     */
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

    /**
     * 检查给定类是否包含交叉变量。
     *
     * @param tree 给定的类
     * @return 类是否包含交叉变量
     */
    public boolean isCvsClz(ClassTree tree) {
        for (var names : cvsScope.values()) {
            for (var name : names) {
                if (name.startsWith(tree.getSimpleName().toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查给定变量名称是否是Java关键字。
     *
     * @param name 待检查的变量名称
     * @return 是否是关键字
     */
    private boolean isKeyword(Name name) {
        var nameStr = name.toString();
        if (JAVA_KEYWORDS.contains(nameStr)) {
            return true;
        }
        return false;
    }

    /**
     * 创建一个默认的交叉变量分析器，仅分析局部变量。
     */
    public CvsAnalyzer() {
        clzScope = new Stack<>();
        cvsScope = new HashMap<>();
        volatileCvsScope = new HashMap<>();
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
        currentOutScope = new HashSet<>();
        // 判断是否继承Thread类
        if (!Util.is_parallel(node)) {
            // return super.visitClass(node, trees);
            exitScope();
            var members = node.getMembers();
            for (var member : members) {
                if (member instanceof JCVariableDecl) {
                    JCVariableDecl variableDecl = (JCVariableDecl) member;
                    if (variableDecl.getModifiers().getFlags().contains(Modifier.VOLATILE)) {
                        // 如果是静态变量，则记录到当前类的作用域
                        Set<String> volatileCvs = volatileCvsScope.getOrDefault(node, new HashSet<String>());
                        volatileCvs.add(variableDecl.getName().toString());
                        volatileCvsScope.put(node, volatileCvs);
                    }
                }
            }
            return null;
        }
        var members = node.getMembers();
        for (var member : members) {
            if (member instanceof JCVariableDecl) {
                JCVariableDecl variableDecl = (JCVariableDecl) member;
                if (variableDecl.getModifiers().getFlags().contains(Modifier.STATIC)) {
                    // 如果是静态变量，则记录到当前类的作用域
                    addVar(node.getSimpleName().toString()+"."+variableDecl.getName().toString());
                    currentOutScope.add(node.getSimpleName().toString()+"."+variableDecl.getName().toString());
                }
                if (variableDecl.getModifiers().getFlags().contains(Modifier.VOLATILE)) {
                    // 如果是静态变量，则记录到当前类的作用域
                    Set<String> volatileCvs = volatileCvsScope.getOrDefault(node, new HashSet<String>());
                    volatileCvs.add(variableDecl.getName().toString());
                    volatileCvsScope.put(node, volatileCvs);
                }
            }
        }
        var ret = super.visitClass(node, trees);
        cvsScope.put(node, currentOutScope);
        exitScope();
        return ret;
    }

    @Override
    public Void visitMethod(MethodTree node, Trees trees) {
        // enterScope();
        var ret = super.visitMethod(node, trees);
        // exitScope();
        return ret;
    }

    @Override
    public Void visitVariable(VariableTree node, Trees trees) {
        if (node.getModifiers().getFlags().contains(Modifier.STATIC)) {
            addVar(node.getName());
        }
        return super.visitVariable(node, trees);
    }

    // @Override
    // public Void visitIdentifier(IdentifierTree node, Trees trees) {
    //     if (!exist(node.getName())) {
    //         currentOutScope.add(node.getName());
    //     }
    //     return super.visitIdentifier(node, trees);
    // }

    @Override
    public Void visitIdentifier(IdentifierTree node, Trees trees) {
        // Name identifierName = node.getName();

        // 获取 IdentifierTree 对应的符号
        Element element = trees.getElement(getCurrentPath());
        if (element != null && element.getKind() == ElementKind.FIELD) {
            VariableElement variableElement = (VariableElement) element;

            // 判断是否为静态变量
            if (variableElement.getModifiers().contains(Modifier.STATIC)) {
                // 获取类的符号
                Element classElement = variableElement.getEnclosingElement();

                // 记录引用的静态变量
                addVar(classElement.getSimpleName());
                currentOutScope.add(classElement.getSimpleName().toString());
            }
        }

        return super.visitIdentifier(node, trees);
    }
    
    @Override
    public Void visitMemberSelect(MemberSelectTree node, Trees trees) {
        //TODO: 目前System.out中的out也会被输出
        // 处理类似 Desk.food_flag 这样的 MemberSelectTree
        ExpressionTree expression = node.getExpression();
        if (JAVA_KEYWORDS.contains(expression.toString())) {
            return null;
        }
        // String name = node.getIdentifier().toString();
        // 判断是否为静态变量
        if (expression.getKind() == Tree.Kind.IDENTIFIER) {
            Element element = trees.getElement(getCurrentPath());
            if (element != null && element.getKind() == ElementKind.FIELD) {
                VariableElement variableElement = (VariableElement) element;
                if (variableElement.getModifiers().contains(Modifier.STATIC)) {
                    // 记录引用的静态变量
                    addVar(node.toString());
                    currentOutScope.add(node.toString());
                }
            }
        }

        return super.visitMemberSelect(node, trees);
    }


}

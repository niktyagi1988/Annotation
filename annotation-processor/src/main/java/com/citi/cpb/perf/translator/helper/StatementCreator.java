package com.citi.cpb.perf.translator.helper;

import java.lang.invoke.MethodHandles;

import com.citi.cpb.perf.util.Utility;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeMaker;

public class StatementCreator {

	
	public static JCMethodInvocation getThreadId(final TreeMaker maker,final JavacElements elements) {
		JCMethodInvocation thread = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
	            maker.Select(maker.Ident(elements.getName("Thread")),elements.getName("currentThread")),
	            com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
		
		JCMethodInvocation id = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
	            maker.Select(thread,elements.getName("getId")),
	            com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
		
		return id;
	}
	
	
	public static JCMethodInvocation getSystemTime(final TreeMaker maker,final JavacElements elements) {
		JCMethodInvocation systemTime = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
	            maker.Select(maker.Ident(elements.getName("System")),elements.getName("currentTimeMillis")),
	            com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
		
		return systemTime;
	}
	
	
	public static JCMethodInvocation getClassName(final TreeMaker maker,final JavacElements elements) {
		
		JCMethodInvocation lookUp = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(maker.QualIdent(Utility.getClassSymbol(elements, MethodHandles.class)),elements.getName("lookup")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
		
		JCMethodInvocation lookUpClass = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
	            maker.Select(lookUp,elements.getName("lookupClass")),
	            com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
		
		JCMethodInvocation className = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
	            maker.Select(lookUpClass,elements.getName("getCanonicalName")),
	            com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
		
		return className;
	}
	
	
	public static JCLiteral getMethodName(final TreeMaker maker,final JCMethodDecl methodDecl) {
		return maker.Literal(methodDecl.name+"("+methodDecl.params+")");
	}
	
	
}

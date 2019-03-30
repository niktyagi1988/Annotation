package com.citi.cpb.perf.translator;

import java.util.List;

import com.citi.cpb.perf.util.Utility;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.ListBuffer;

public class StartTimerTranslator {

	private final TreeMaker maker;
    private final JavacElements elements;
    
    public StartTimerTranslator(TreeMaker maker, JavacElements elements) {
		super();
		this.maker = maker;
		this.elements = elements;
	}

	public JCBlock createBody(JCBlock oldBody) {
        ListBuffer<JCTree.JCStatement> list = new ListBuffer<>();
        List<JCTree.JCStatement> statements = oldBody.getStatements();
        int counter = 0;
        int start = 0;
        boolean flag = false;
        
        for (JCStatement statement : statements) {
        	if (counter == start) {
                list.add(startTimer());
            }
        	
        	if(statement instanceof JCReturn) {
        		flag = true;
        		list.add(stopTimer());
        	}
        	
        	list.add(oldBody.stats.get(counter));
        	counter++;
		}
        
        if(!flag)
        	list.add(stopTimer());
        
        return maker.Block(1, list.toList());
    }

	private JCStatement startTimer() {
		
		JCMethodInvocation thread = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(maker.Ident(elements.getName("Thread")),elements.getName("currentThread")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
    	
    	JCMethodInvocation id = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(thread,elements.getName("getId")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
    	
    	JCMethodInvocation systemTime = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(maker.Ident(elements.getName("System")),elements.getName("currentTimeMillis")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
    	
    	JCMethodInvocation current = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
				maker.Select(maker.Ident(elements.getName("Thread")),elements.getName("currentThread")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
    	
    	JCMethodInvocation stack = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(current, elements.getName("getStackTrace")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
    	
    	JCMethodInvocation utilityTime = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(maker.QualIdent(getClassSymbol(Utility.class)),elements.getName("startTimer")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>of(id,stack,systemTime));
     
       return maker.Exec(utilityTime);
    }

    private JCStatement stopTimer() {
    	
    	JCMethodInvocation thread = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(maker.Ident(elements.getName("Thread")),elements.getName("currentThread")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
    	
    	JCMethodInvocation id = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(thread,elements.getName("getId")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
    	
    	JCMethodInvocation systemTime = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(maker.Ident(elements.getName("System")),elements.getName("currentTimeMillis")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
    	
    	JCMethodInvocation current = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
				maker.Select(maker.Ident(elements.getName("Thread")),elements.getName("currentThread")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
    	
    	JCMethodInvocation stack = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(current, elements.getName("getStackTrace")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>nil());
    	
    	JCMethodInvocation utilityTime = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(maker.QualIdent(getClassSymbol(Utility.class)),elements.getName("endTimer")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>of(id,stack,systemTime));
     
       return maker.Exec(utilityTime);
    }

    private Symbol.ClassSymbol getClassSymbol(Class<?> clazz) {
        return elements.getTypeElement(clazz.getName());
    }

    @SuppressWarnings("unused")
	private Symbol findMember(Class<?> clazz, String name) {
    	
    	List<Symbol> symbolList = getClassSymbol(clazz).getEnclosedElements();
    		for (Symbol symbol : symbolList) {
				if(symbol.getSimpleName().toString().equals(name))
				{
					return symbol;
				}
			}
    	return null;
    }
	
}

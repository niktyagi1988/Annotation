package com.citi.cpb.perf.translator;

import java.util.List;

import com.citi.cpb.perf.translator.helper.StatementCreator;
import com.citi.cpb.perf.util.PerfManager;
import com.citi.cpb.perf.util.Utility;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
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

	public JCBlock createBody(JCMethodDecl methodDecl) {
		
		JCBlock oldBody = methodDecl.body;
		ListBuffer<JCTree.JCStatement> list = new ListBuffer<>();
        List<JCTree.JCStatement> statements = oldBody.getStatements();
        int counter = 0;
        int start = 0;
        boolean flag = false;
        
        for (JCStatement statement : statements) {
        	if (counter == start) {
                list.add(startTimer(methodDecl));
            }
        	
        	if(statement instanceof JCReturn) {
        		flag = true;
        		list.add(stopTimer(methodDecl));
        	}
        	
        	list.add(oldBody.stats.get(counter));
        	counter++;
		}
        
        if(!flag)
        	list.add(stopTimer(methodDecl));
        
        return maker.Block(1, list.toList());
    }

	private JCStatement startTimer(JCMethodDecl methodDecl) {
		
    	JCMethodInvocation utilityTime = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(maker.QualIdent(Utility.getClassSymbol(elements,PerfManager.class)),elements.getName("startTimer")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>of(StatementCreator.getThreadId(maker, elements),
                													  StatementCreator.getClassName(maker, elements),
                													  StatementCreator.getMethodName(maker, methodDecl),
                													  StatementCreator.getSystemTime(maker, elements)));
     
       return maker.Exec(utilityTime);
    }

    private JCStatement stopTimer(JCMethodDecl methodDecl) {
    	
    	JCMethodInvocation utilityTime = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(maker.QualIdent(Utility.getClassSymbol(elements,PerfManager.class)),elements.getName("endTimer")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>of(StatementCreator.getThreadId(maker, elements),
                													  StatementCreator.getClassName(maker, elements),
                													  StatementCreator.getMethodName(maker, methodDecl),
                													  StatementCreator.getSystemTime(maker, elements)));
     
       return maker.Exec(utilityTime);
    }

	
}

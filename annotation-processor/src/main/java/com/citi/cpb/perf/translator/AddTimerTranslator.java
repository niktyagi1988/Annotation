package com.citi.cpb.perf.translator;

import java.util.List;

import com.citi.cpb.perf.translator.util.StatementCreator;
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

public class AddTimerTranslator {

	private final TreeMaker maker;
    private final JavacElements elements;
    
    public AddTimerTranslator(TreeMaker maker, JavacElements elements) {
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
                list.add(addTimer(methodDecl));
            }
        	
        	if(statement instanceof JCReturn) {
        		flag = true;
        		list.add(addTimer(methodDecl));
        	}
        	
        	list.add(oldBody.stats.get(counter));
        	counter++;
		}
        
        if(!flag)
        	list.add(addTimer(methodDecl));
        
        return maker.Block(1, list.toList());
    }

	private JCStatement addTimer(JCMethodDecl methodDecl) {
		JCMethodInvocation utilityTime = maker.Apply(
				com.sun.tools.javac.util.List.<JCTree.JCExpression>nil(),
                maker.Select(maker.QualIdent(Utility.getClassSymbol(elements,Utility.class)),elements.getName("addTimer")),
                com.sun.tools.javac.util.List.<JCTree.JCExpression>of(StatementCreator.getThreadId(maker, elements),
                													  StatementCreator.getClassName(maker, elements),
                													  StatementCreator.getMethodName(maker, methodDecl),
                													  StatementCreator.getSystemTime(maker, elements)));
     
       return maker.Exec(utilityTime);
    }
    
	
}

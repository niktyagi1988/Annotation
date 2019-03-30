package com.citi.cpb.perf.translator;

import java.util.List;

import com.citi.cpb.perf.annotation.AddTimer;
import com.citi.cpb.perf.annotation.StartTimer;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;

public class TimerTranslator extends TreeTranslator {

    private final TreeMaker maker;
    private final JavacElements elements;

    public TimerTranslator(Context context) {
        this.maker = TreeMaker.instance(context);
        this.elements = JavacElements.instance(context);
    }

    @Override
    public void visitMethodDef(JCMethodDecl methodDecl) {
        super.visitMethodDef(methodDecl);
        
        boolean addTimerAnnotation = false;
        boolean startTimerAnnotation = false;
        List<JCAnnotation> annList =  methodDecl.mods.annotations;
        
        	if(annList != null) {
        		for (JCAnnotation jcAnnotation : annList) {
        			JCTree tr = jcAnnotation.getAnnotationType();
        				if(tr != null) {
        					Type type = tr.type;
        						if(type != null) {
        							if(type.toString().equals(AddTimer.class.getName())) {
                        				addTimerAnnotation = true;
                        			}else if(type.toString().equals(StartTimer.class.getName())) {
                        				startTimerAnnotation = true;
                        			}
        						}
        				}
        		}
                
                if (addTimerAnnotation) {
                    result = createAddMethodDeclaration(methodDecl);
                }else if (startTimerAnnotation) {
                    result = createStartMethodDeclaration(methodDecl);
                }
        	}
    }

    private JCMethodDecl createAddMethodDeclaration(JCMethodDecl methodDecl) {
    	AddTimerTranslator obj = new AddTimerTranslator(maker,elements);
        JCBlock body = obj.createBody(methodDecl.body);
        return maker.MethodDef(methodDecl.mods, methodDecl.name,
                               methodDecl.restype, methodDecl.typarams,
                               methodDecl.params, methodDecl.thrown,
                               body, methodDecl.defaultValue);
    }
    
    private JCMethodDecl createStartMethodDeclaration(JCMethodDecl methodDecl) {
    	StartTimerTranslator obj = new StartTimerTranslator(maker,elements);
        JCBlock body = obj.createBody(methodDecl.body);
        
        JCMethodDecl meth = maker.MethodDef(methodDecl.mods, methodDecl.name,
                methodDecl.restype, methodDecl.typarams,
                methodDecl.params, methodDecl.thrown,
                body, methodDecl.defaultValue);
        
        return meth;
    }

    
}

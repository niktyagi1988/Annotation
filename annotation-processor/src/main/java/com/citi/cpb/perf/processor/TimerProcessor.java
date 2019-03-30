package com.citi.cpb.perf.processor;

import com.citi.cpb.perf.translator.TimerTranslator;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes(value = {"com.citi.cpb.perf.annotation.AddTimer",
								   "com.citi.cpb.perf.annotation.StartTimer"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TimerProcessor extends AbstractProcessor {

    private Trees trees;
    private TreeTranslator visitor;

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        trees = Trees.instance(processingEnvironment);
        visitor = new TimerTranslator(((JavacProcessingEnvironment) processingEnvironment).getContext());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (!roundEnvironment.processingOver()) {
            Set<? extends Element> elements = roundEnvironment.getRootElements();
            
            for (Element element : elements) {
            	JCTree tree = (JCTree) trees.getTree(element);
            	tree.accept(visitor);
			}
        }
        return true;
    }
}
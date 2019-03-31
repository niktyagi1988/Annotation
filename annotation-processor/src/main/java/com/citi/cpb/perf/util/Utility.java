package com.citi.cpb.perf.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.citi.cpb.perf.vo.PerfStats;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.model.JavacElements;

public class Utility {

	public static void analysisPerformanceAsync(final ConcurrentHashMap<Long,ArrayList<PerfStats>> map, final Long threadId) {
		ArrayList<PerfStats> list = map.get(threadId);
		Map<String,Long> pointerMap = new LinkedHashMap<String, Long>();
		Map<String,String> dashMap = new LinkedHashMap<String, String>();
		List<String> respList = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		String className = "";
		String oldClassName = "";
		
		for (PerfStats perfStats : list) {
			
			className = perfStats.getClassName()+"."+perfStats.getMethodName();
			
			if(pointerMap.containsKey(className)) {
				respList.set(respList.indexOf(className),dashMap.get(className).toString()+">> "+className+" - "+new Float(perfStats.getTime()-pointerMap.get(className))/1000+" seconds");
				builder = new StringBuilder();
			}else {
				respList.add(className);
				pointerMap.put(className, perfStats.getTime());
			}
			
			if(!oldClassName.equals(className)) {
				builder.append("------");
				dashMap.put(className, builder.toString());
			}
			oldClassName = className;
		}
		for (int i = 0; i <= respList.size()-1; i++) {
			System.out.println(respList.get(i));
		}
	}
	
	public static Symbol.ClassSymbol getClassSymbol(JavacElements elements,Class<?> clazz) {
        return elements.getTypeElement(clazz.getName());
    }
    
}

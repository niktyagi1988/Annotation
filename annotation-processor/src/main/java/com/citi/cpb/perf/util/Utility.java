package com.citi.cpb.perf.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.citi.cpb.perf.vo.PerfStats;
import com.citi.cpb.perf.vo.Stat;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.model.JavacElements;

public class Utility {

	public static void analysisPerformanceAsync(final ConcurrentHashMap<Long,ArrayList<PerfStats>> map, final Long threadId) {
		final Map<String,Stat> statMap = new LinkedHashMap<String, Stat>();
		
		new Thread(new Runnable() {
			
			public void run() {
				Stack<String> stack = new Stack<String>();
				Map<String,Long> pointerMap = new LinkedHashMap<String, Long>();
				String className = "";
				
				for (PerfStats perfStats : map.get(threadId)) {
					className = perfStats.getClassName()+"."+perfStats.getMethodName();

					if(pointerMap.containsKey(className)) {
						statMap.get(className).setAfterTime(perfStats.getTime());
						stack.pop();
					}else {
						statMap.put(className, new Stat(className,perfStats.getTime(), null, null));
						if(stack.size() != 0 )
							statMap.get(className).setParentMethodName(stack.get(stack.size()-1));
						stack.push(className);
						pointerMap.put(className, perfStats.getTime());
					}
				}
				Set<Entry<String,Stat>> set =  statMap.entrySet();
				StringBuffer buffer = new StringBuffer();
				
				for (Entry<String, Stat> entry : set) {
					Stat stat = entry.getValue();
					String parentName = stat.getParentMethodName();
					buffer = new StringBuffer("-----");
					
					if(parentName == null) {
						System.out.println(buffer.toString()+">"+stat.getMethodName() + " " + new Float(stat.getAfterTime() - stat.getBeforeTime())/1000 + "seconds");
					}else {
						buffer.append("-----");
						parentName = statMap.get(parentName).getParentMethodName();
						if(parentName != null) {
							buffer.append("-----");
							parentName = statMap.get(parentName).getParentMethodName();
							if(parentName != null) {
								buffer.append("-----");
							}
						}
						System.out.println(buffer.toString()+">"+stat.getMethodName() + " " + new Float(stat.getAfterTime() - stat.getBeforeTime())/1000 + "seconds");
					}
				}
			}
		}).start();
	}
	
	public static Symbol.ClassSymbol getClassSymbol(JavacElements elements,Class<?> clazz) {
        return elements.getTypeElement(clazz.getName());
    }
    
}

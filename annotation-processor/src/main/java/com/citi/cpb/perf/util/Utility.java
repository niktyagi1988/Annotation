package com.citi.cpb.perf.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import com.citi.cpb.perf.vo.PerfStats;
import com.citi.cpb.perf.vo.PrintStat;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.model.JavacElements;

public class Utility {

	public static void analysisPerformanceAsync(final ConcurrentHashMap<Long,ArrayList<PerfStats>> map, final Long threadId) {
		final Map<String,PrintStat> statMap = new LinkedHashMap<String, PrintStat>();
		
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
						statMap.put(className, new PrintStat(className,perfStats.getTime(), null, null));
						if(stack.size() != 0 )
							statMap.get(className).setParentMethodName(stack.get(stack.size()-1));
						stack.push(className);
						pointerMap.put(className, perfStats.getTime());
					}
				}
				Set<Entry<String,PrintStat>> set =  statMap.entrySet();
				System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<   Performance Start  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				for (Entry<String, PrintStat> entry : set) {
					PrintStat stat = entry.getValue();
					System.out.println(getDash(statMap, stat.getMethodName())+"> "+stat.getMethodName() + " " + new Float(stat.getAfterTime() - stat.getBeforeTime())/1000 + "seconds");
				}
				System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<   Performance End  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			}
		}).start();
	}
	
	public static StringBuffer getDash(Map<String,PrintStat> statMap,String className) {
		StringBuffer buffer = new StringBuffer("----------");
		String parentName = statMap.get(className).getParentMethodName();
		
		if(parentName == null)
			return buffer;
		if(parentName != null) {
			buffer.append(getDash(statMap, parentName));
		}
		return buffer;
	}
	
	public static Symbol.ClassSymbol getClassSymbol(JavacElements elements,Class<?> clazz) {
        return elements.getTypeElement(clazz.getName());
    }
    
}

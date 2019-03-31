package com.citi.cpb.perf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.citi.cpb.perf.vo.PerfStats;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.model.JavacElements;

public class Utility {

	private static ConcurrentHashMap<Long,ArrayList<PerfStats>> map   = new ConcurrentHashMap<Long, ArrayList<PerfStats>>();
	
	public static void startTimer(Long threadId,String className,String methodName, Long time) {
		try {
			ArrayList<PerfStats> list = new ArrayList<PerfStats>();
			list.add(new PerfStats(className, methodName, time));
			map.put(threadId, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addTimer(Long threadId,String className,String methodName, Long time) {
		try {
			map.get(threadId).add(new PerfStats(className, methodName, time));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void endTimer(Long threadId,String className,String methodName, Long time) {
		try {
			map.get(threadId).add(new PerfStats(className, methodName, time));
			analysisPerformanceAsync(map, threadId);
			map.remove(threadId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void analysisPerformanceAsync(final ConcurrentHashMap<Long,ArrayList<PerfStats>> map, final Long threadId) {
		
		ArrayList<PerfStats> list = map.get(threadId);
		Map<String,Long> pointerMap = new HashMap<>();
		
		for (PerfStats perfStats : list) {
			if(pointerMap.containsKey(perfStats.getClassName().concat(perfStats.getMethodName()))) {
				System.out.println("----->> "+perfStats.getClassName()+"."+perfStats.getMethodName()+" - "+new Float(perfStats.getTime()-pointerMap.get(perfStats.getClassName().concat(perfStats.getMethodName())))/1000+" seconds");
			}else {
				pointerMap.put(perfStats.getClassName().concat(perfStats.getMethodName()), perfStats.getTime());
			}
		}
	}
	
	public static Symbol.ClassSymbol getClassSymbol(JavacElements elements,Class<?> clazz) {
        return elements.getTypeElement(clazz.getName());
    }
    
}

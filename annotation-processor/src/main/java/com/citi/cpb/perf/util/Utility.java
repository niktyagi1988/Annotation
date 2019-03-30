package com.citi.cpb.perf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.citi.cpb.perf.vo.PerfStats;

public class Utility {

	private static ConcurrentHashMap<Long,ArrayList<PerfStats>> map   = new ConcurrentHashMap<Long, ArrayList<PerfStats>>();
	
	public static void startTimer(Long threadId,StackTraceElement[] element,Long time) {
		ArrayList<PerfStats> list = new ArrayList<PerfStats>();
		list.add(new PerfStats(element[1].getClassName(), element[1].getMethodName(),element[1].getLineNumber(), time));
		map.put(threadId, list);
	}
	
	public static void addTimer(Long threadId,StackTraceElement[] element,Long time) {
		map.get(threadId).add(new PerfStats(element[1].getClassName(), element[1].getMethodName(),element[1].getLineNumber(), time));
	}
	
	public static void endTimer(Long threadId,StackTraceElement[] element,Long time) {
		map.get(threadId).add(new PerfStats(element[1].getClassName(), element[1].getMethodName(),element[1].getLineNumber(), time));
		analysisPerformanceAsync(map, threadId);
		map.remove(threadId);
	}
	
	public static void analysisPerformanceAsync(final ConcurrentHashMap<Long,ArrayList<PerfStats>> map, final Long threadId) {
		
		ArrayList<PerfStats> list = map.get(threadId);
		Map<String,Long> pointerMap = new HashMap<>();
		
		System.out.println(map);
		
		for (PerfStats perfStats : list) {
			if(pointerMap.containsKey(perfStats.getClassName().concat(perfStats.getMethodName()))) {
				System.out.println("----->> "+perfStats.getClassName()+"."+perfStats.getMethodName()+"() - "+new Float(perfStats.getTime()-pointerMap.get(perfStats.getClassName().concat(perfStats.getMethodName())))/1000+" seconds");
			}else {
				pointerMap.put(perfStats.getClassName().concat(perfStats.getMethodName()), perfStats.getTime());
			}
		}
				
			
	}
	
}

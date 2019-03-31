package com.citi.cpb.perf.util;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.citi.cpb.perf.vo.PerfStats;

public class PerfManager {

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
			Utility.analysisPerformanceAsync(map, threadId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

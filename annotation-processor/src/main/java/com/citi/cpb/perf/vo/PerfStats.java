package com.citi.cpb.perf.vo;

public class PerfStats {

	private String className;
	private String methodName;
	private Long time;
	private int lineNo;
	
	public PerfStats(String className, String methodName, int lineNo, Long time) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.time = time;
		this.lineNo = lineNo;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	@Override
	public String toString() {
		return "PerfStats [className=" + className + ", methodName=" + methodName + ", time=" + time + ", lineNo="
				+ lineNo + "]";
	}
}

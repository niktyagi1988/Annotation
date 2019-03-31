package com.citi.cpb.perf.vo;

public class PrintStat {

	private String methodName;
	private Long beforeTime;
	private Long afterTime;
	private String parentMethodName;
	
	public PrintStat(String methodName, Long beforeTime, Long afterTime, String parentMethodName) {
		super();
		this.methodName = methodName;
		this.beforeTime = beforeTime;
		this.afterTime = afterTime;
		this.parentMethodName = parentMethodName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Long getBeforeTime() {
		return beforeTime;
	}
	public void setBeforeTime(Long beforeTime) {
		this.beforeTime = beforeTime;
	}
	public Long getAfterTime() {
		return afterTime;
	}
	public void setAfterTime(Long afterTime) {
		this.afterTime = afterTime;
	}
	public String getParentMethodName() {
		return parentMethodName;
	}
	public void setParentMethodName(String parentMethodName) {
		this.parentMethodName = parentMethodName;
	}
	@Override
	public String toString() {
		return "Stat [methodName=" + methodName + ", beforeTime=" + beforeTime + ", afterTime=" + afterTime
				+ ", parentMethodName=" + parentMethodName + "]";
	}
	
	
	
}

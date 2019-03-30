package com;

import com.citi.cpb.perf.annotation.AddTimer;
import com.citi.cpb.perf.annotation.StartTimer;

public class Main {

	public void print(int a,int b,int c)
    {
		System.out.println("A : "+a+" , B : "+b+" , C : "+c  );
    }
	
	@AddTimer
    public int addThreeNumber(int a,int b,int c) throws InterruptedException
    {
    	System.out.println("Starting calculation for 3 parameter");
    	int result = 0;
    	
    	for (int i = 0; i < 100; i++) {
			result = result + a + b;
			Thread.sleep(50);
		}
    	
    	print(a, b, c);
    	return result;
    }
	
	@AddTimer
    public int addTwoNumber(int a,int b) throws InterruptedException
    {
    	System.out.println("Starting calculation for 2 parameter");
    	int result = 0;
    	
    	for (int i = 0; i < 100; i++) {
			result = result + a + b;
			Thread.sleep(50);
		}
    	int res  = addThreeNumber(a, b, result);
    	return res;
    }
	
    @StartTimer
    public static void main(String[] args) throws InterruptedException {
      Main main = new Main();
      main.addTwoNumber(1, 2);
      
    }
   
}

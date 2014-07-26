package com.blastedstudios.ledge.ai.bt;

public class TimeKeeper {
	private long mark, last = System.currentTimeMillis();
	
	public long dt(){
		long current = System.currentTimeMillis();
		long dtLast = current - last;
		last = current;
		return dtLast;
	}
	
	public void mark(){
		mark = System.currentTimeMillis();
	}
	
	public long markDt(){
		return System.currentTimeMillis() - mark;
	}
	
	public boolean isMarked(){
		return mark != 0;
	}
}

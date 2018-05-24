package com.lib.tool;
public class ObjectRunnable implements Runnable{
	public Object obj;
	public Object obj2;
	public ObjectRunnable(Object obj) {
		super();
		this.obj = obj;
	}
	public ObjectRunnable(Object obj, Object obj2) {
		super();
		this.obj = obj;
		this.obj2 = obj2;
	}

	public void run() {
	}
}
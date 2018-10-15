package com.jerry.glory;

class father {
	public void print() {
		System.out.println("Father");
	}
	public int a;
	father() {
		a = 10;
	}
}

class son extends father {
	@Override
	public void print() {
		System.out.println("Son");
	}
}
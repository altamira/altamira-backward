package test.pack;

import javax.persistence.Entity;

@Entity
public class MyCase {

	private int a;
	
	private int b;

	public MyCase() {
		
	}
	
	public MyCase(int a, int b) {
		super();
		this.a = a;
		this.b = b;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}
	
	public int sum() {
		return a + b;
	}
}

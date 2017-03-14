package graduwork;

public class TestB extends TestA {

	public void print() {
		setA(2);
		System.out.println("From B: a = " + getA());
	}
}

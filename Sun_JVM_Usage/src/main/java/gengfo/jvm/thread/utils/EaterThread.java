package gengfo.jvm.thread.utils;



public class EaterThread extends Thread {

	private String name;
	private Tool leftHand;
	private Tool rightHand;

	public EaterThread(String name, Tool leftHand, Tool rightHand) {

		super("Gengfo-" + name + "-Thread");

		this.name = name;
		this.leftHand = leftHand;
		this.rightHand = rightHand;

	}

	public void run() {
		while (true) {
			try {
				System.out.println(name + " In spleeping ... ");
				Thread.sleep(1);
				System.out.println(name + " Woke up");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			eat();
		}
	}

	public void eat() {

		synchronized (leftHand) {
			System.out.println(name + " takes up " + leftHand + " (left).");

			synchronized (rightHand) {
				System.out.println(name + " takes up " + rightHand
						+ " (right).");

				System.out.println(name + " is eating now, yam yam!");
				System.out.println(name + " puts down " + rightHand
						+ " (right).");
			}

			System.out.println(name + " puts down " + leftHand + " (left).");

			System.out.println();
		}
	}

}

package gengfo.jvm.thread;

import gengfo.jvm.thread.utils.EaterThread;
import gengfo.jvm.thread.utils.Tool;


public class HelloWorldThread {

	public static void main(String args[]) {

		System.out.println("Testing EaterThread, hit Ctrl+c to exit.");

		Tool spoon = new Tool("Spoon");
		Tool fork = new Tool("Fork");

		new EaterThread("Alice",fork ,spoon ).start();
		new EaterThread("Bob", fork, spoon).start();

	}

}

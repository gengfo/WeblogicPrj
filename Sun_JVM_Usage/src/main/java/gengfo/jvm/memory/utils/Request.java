package gengfo.jvm.memory.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Request {
	private final String name;
	private final int number;
	private static final Random random = new Random();
	//private static MemBlock mb;
	private static List<MemBlock> list = new ArrayList<MemBlock>();

	public Request(String name, int number) {
		this.name = name;
		this.number = number;
	}

	public void execute() {
		System.out.println(Thread.currentThread().getName() + " executes "
				+ this);
		try {
			
			list.add(new MemBlock());
			
			Thread.sleep(random.nextInt(10));
		} catch (InterruptedException e) {
		}
	}

	public String toString() {
		return "[ Request from " + name + " No." + number + " ]";
	}
}

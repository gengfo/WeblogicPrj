package gengfo.worker;

import java.util.Random;

public class Request {
	private final String name;
	private final int number;

	public String getName() {
		return name;
	}

	public int getNumber() {
		return number;
	}

	private static final Random random = new Random();

	public Request(String name, int number) {
		this.name = name;
		this.number = number;
	}

	public void execute() {
		System.out.println(Thread.currentThread().getName() + " executes "
				+ this);

		int rNum = random.nextInt(1000);

		try {
			// int number = request.getNumber();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < rNum; i++) {
				sb.append(String.valueOf(i)+"-");
			}
			System.out.println(rNum);

			Thread.sleep(rNum);
		} catch (InterruptedException e) {
		}
	}

	public String toString() {
		return "[ Request from " + name + " No." + number + " ]";
	}
}

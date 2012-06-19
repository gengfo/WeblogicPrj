package gengfo.worker;

import java.util.Random;

public class Request {
	private final String name;
	private final int number;
	private static final Random random = new Random();
	private MemBlock mb;

	public MemBlock getMb() {
		return mb;
	}

	public void setMb(MemBlock mb) {
		this.mb = mb;
	}

	public Request(String name, int number) {
		this.name = name;
		this.number = number;
	}

	public void execute() {
		System.out.println(Thread.currentThread().getName() + " executes "
				+ this);
		try {
			int num = random.nextInt(1000);
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < 1024; j++) {
				sb.append(num);
			}
			String content = sb.toString();
			System.out.print(content);

			this.setMb(new MemBlock(sb.toString()));
			Thread.sleep(random.nextInt(1000));
		} catch (InterruptedException e) {
		}
	}

	public String toString() {
		return "[ Request from " + name + " No." + number + " ]";
	}
}

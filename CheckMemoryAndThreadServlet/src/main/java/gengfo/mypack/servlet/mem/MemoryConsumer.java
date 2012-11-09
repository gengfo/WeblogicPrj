package gengfo.mypack.servlet.mem;

public class MemoryConsumer {

	public final String[] memBlock;

	public MemoryConsumer() {

		this.memBlock = genBigMemory();

	}

	public String[] genBigMemory() {

		String[] array = new String[1024 * 1024];
		for (int i = 0; i < 1024 * 10; i++) {
			for (int j = 'a'; j <= 'z'; j++) {
				array[i] += (char) j;
			}
		}

		return array;
	}
}

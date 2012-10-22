package gengfo.jvm.memory.utils;

public class MemBlock {

	public final String[] memBlock;

	public MemBlock() {

		this.memBlock = genBigMemory();

	}

	public String[] genBigMemory() {

		String[] array = new String[1024];
		for (int i = 0; i < 1024; i++) {
			for (int j = 'a'; j <= 'z'; j++) {
				array[i] += (char) j;
			}
		}

		return array;
	}

}

package gengfo.worker;

public class MemBlock {

	private String bigStr;

	public MemBlock(String aString) {
		this.bigStr = aString;
	}

	public String getBigStr() {
		return bigStr;
	}

	public void setBigStr(String bigStr) {
		this.bigStr = bigStr;
	}

}

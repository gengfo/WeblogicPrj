package gengfo.jrockit;

import gengfo.worker.Channel;
import gengfo.worker.ClientThread;

public class HelloWorld {
	
	public static void main(String args[]){
		
		System.out.println("HelloWord start ...");
		
		Channel channel = new Channel(5);

		channel.startWorkers();
		
		new ClientThread("Alice", channel).start();
		new ClientThread("Bobby", channel).start();
		new ClientThread("Chris", channel).start();
		
		System.out.println("HelloWord end ...");
		
	}

}

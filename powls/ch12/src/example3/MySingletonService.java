package professional.weblogic.ch12.example3;

import weblogic.cluster.singleton.SingletonService;

public class MySingletonService implements SingletonService
{
    protected int counter;

    public MySingletonService() 
    {
	counter = 0; 
    }

    public synchronized int incrementCounter()
    {
	return ++counter;
    }

    public void activate()
    {
	System.out.println("MySingletonService is activating");
    }

    public void deactivate()
    {
	System.out.println("MySingletonService is deactivating");
    }
}
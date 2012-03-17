package professional.weblogic.ch12.example1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

public class WebLogicPerformanceMonitor
{
    private MBeanServerConnection connection;
    private JMXConnector connector;
    private ObjectName service;
    HashMap<String, HashMap<String, Number>> tracker = null;

    public WebLogicPerformanceMonitor(String protocol, String hostname,
				      int port, String username,
				      String password)
	throws MalformedURLException, IOException
    {
	JMXServiceURL serviceUrl =
	    new JMXServiceURL(protocol, hostname, port, "/jndi/" + 
			      "weblogic.management.mbeanservers.domainruntime");
	HashMap<String, String> props = new HashMap<String, String>();
	props.put(Context.SECURITY_PRINCIPAL, username);
	props.put(Context.SECURITY_CREDENTIALS, password);
	props.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
		  "weblogic.management.remote");
	connector = JMXConnectorFactory.connect(serviceUrl, props);
	connection = connector.getMBeanServerConnection();

	try {
	    service = 
		new ObjectName("com.bea:Name=DomainRuntimeService," +
			       "Type=weblogic.management.mbeanservers." +
			       "domainruntime.DomainRuntimeServiceMBean");
	}
	catch (MalformedObjectNameException e) {
	    throw new AssertionError(e.getMessage());
	}
	tracker = new HashMap<String, HashMap<String, Number>>();
    }

    public void printPerfStats()
	throws MBeanException, AttributeNotFoundException,
	       InstanceNotFoundException, ReflectionException, IOException
    {
	ObjectName[] serverRuntimes =
	    (ObjectName[])connection.getAttribute(service, "ServerRuntimes");
	
	int length = serverRuntimes.length;
	for (int i = 0; i < length; i++) {
	    String serverName = 
		(String)connection.getAttribute(serverRuntimes[i], "Name");

	    ObjectName threadPoolRuntime = 
		(ObjectName)connection.getAttribute(serverRuntimes[i],
						    "ThreadPoolRuntime");
	    Date now = new Date();
	    long completedReqCount =
		(Long)connection.getAttribute(threadPoolRuntime,
					      "CompletedRequestCount");
	    int executeThreadCount =
		(Integer)connection.getAttribute(threadPoolRuntime,
						 "ExecuteThreadTotalCount");
	    int pendingUserReqCount =
		(Integer)connection.getAttribute(threadPoolRuntime,
						 "PendingUserRequestCount");
	    int queueLength =
		(Integer)connection.getAttribute(threadPoolRuntime,
						 "QueueLength");
	    double throughput =
		(Double)connection.getAttribute(threadPoolRuntime,
						"Throughput");

	    System.out.printf("%s Stats at Time: %s\n" +
			      "\t%d Total Completed Requests\n" +
			      "\t%d Execute Threads\n" +
			      "\t%d Pending Requests (%d User, %d System)\n" +
			      "\t%f Total Throughput\n", serverName,
			      now.toString(),
			      completedReqCount, executeThreadCount,
			      queueLength, pendingUserReqCount,
			      (queueLength - pendingUserReqCount), throughput);

	    long newTime = now.getTime();

	    HashMap<String, Number> oldStats = tracker.get(serverName);
	    if (oldStats == null) { // first time through
		oldStats = new HashMap<String, Number>();
	    }
	    else {
		long oldTime = (Long)oldStats.get("Time");
		long oldCompletedReqCount =
		    (Long)oldStats.get("CompletedRequestCount");
		int oldExecuteThreadCount =
		    (Integer)oldStats.get("ExecuteThreadTotalCount");
		int oldPendingUserReqCount =
		    (Integer)oldStats.get("PendingUserRequestCount");
		int oldQueueLength = (Integer)oldStats.get("QueueLength");
		double oldThroughput = (Double)oldStats.get("Throughput");

		int threadIncr = executeThreadCount - oldExecuteThreadCount;
		String threadIncrString = null;
		if (threadIncr >= 0) {
		    threadIncrString = "created";
		}
		else {
		    threadIncrString = "destroyed";
		    threadIncr = Math.abs(threadIncr);
		}

		int queueLenIncr = queueLength - oldQueueLength;
		String queueLenIncrString = null;
		if (queueLenIncr >= 0) {
		    queueLenIncrString = "increased";
		}
		else {
		    queueLenIncrString = "decreased";
		    queueLenIncr = Math.abs(queueLenIncr);
		}

		System.out.printf("\n\tIn the last %d milliseconds:\n" +
				  "\t\t%d requests were processed\n" +
				  "\t\t%d Execute Threads %s\n" +
				  "\t\tPending Requests %s by %d requests\n\n",
				  newTime - oldTime,
				  completedReqCount -
				  oldCompletedReqCount,
				  threadIncr, threadIncrString,
				  queueLenIncrString, queueLenIncr);
	    }
	    oldStats.put("Time", newTime);
	    oldStats.put("CompletedRequestCount", completedReqCount);
	    oldStats.put("ExecuteThreadTotalCount", executeThreadCount);
	    oldStats.put("PendingUserRequestCount", pendingUserReqCount);
	    oldStats.put("QueueLength", queueLength);
	    oldStats.put("Throughput", throughput);
	    tracker.put(serverName, oldStats);
	}
    }

    private static void usage()
    {
	System.err.println("Usage: java professional.weblogic.ch12." + 
			   "WebLogicPerformanceMonitor [<property_file> | " +
			   "<interval> <protocol> <hostname> <port> " +
			   "<username> <password>]");	
    }

    public static void main(String[] args) throws Exception
    {
	String delayString = "5"; // seconds
	String protocol = "t3";
	String hostname = "192.168.1.40";
	String portString = "7001";
	String username = "weblogic";
	String password = "weblogic1";

	switch(args.length) {
	case 6:
	    password = args[5];
	    username = args[4];
	    portString = args[3];
	    hostname = args[2];
	    protocol = args[1];
	    delayString = args[0];
	    break;
	case 1:
	    FileInputStream file = null;
	    try {
		file = new FileInputStream(args[0]);
	    }
	    catch (FileNotFoundException fnfe) {
		System.err.println("Unable to find property file: " + args[0]);
		fnfe.printStackTrace();
		System.exit(1);
	    }

	    Properties props = new Properties();
	    try {
		props.load(file);
	    }
	    catch (IOException ioe) {
		System.err.println("Unable to read property file: + args[0]");
		ioe.printStackTrace();
		System.exit(1);
	    }
	    finally {
		try { file.close(); } catch (IOException ignore) { }
	    }

	    if (props.containsKey("interval_seconds"))
		delayString = props.getProperty("interval_seconds");
	    if (props.containsKey("protocol"))
		protocol = props.getProperty("protocol");
	    if (props.containsKey("hostname"))
		hostname = props.getProperty("hostname");
	    if (props.containsKey("port"))
		portString = props.getProperty("port");
	    if (props.containsKey("username"))
		username = props.getProperty("username");
	    if (props.containsKey("password"))
		password = props.getProperty("password");
	    break;
	    
	case 0:
	    break;

	default:

	    System.exit(1);
	}

	int delay = Integer.parseInt(delayString) * 1000; //milliseconds
	int port = Integer.parseInt(portString);

	WebLogicPerformanceMonitor wpm =
	    new WebLogicPerformanceMonitor(protocol, hostname, port,
					   username, password);
	while (true) {
	    try {
		wpm.printPerfStats();
		Thread.sleep(delay);
	    }
	    catch (InterruptedException ie) {}
	}
    }
}

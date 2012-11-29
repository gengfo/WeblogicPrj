combine memory and thread test together


1. build test app

2. deploy to weblogic server in test-domain
	if server says jvm issue, then set to startWeblogic.cmd
	-Dcom.sun.management.jmxremote.port=16103  -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmxremote.ssl=false
		
3. start jrmc to connect to remote host
	C:\oracle\Middleware\jrockit_160_22_D1.1.1-3\bin\jrmc
	
3. trigger by servelet ThradTest
	http://192.168.72.1:7001/MyServer/ThreadTest
	
	start console by jrmc
	run time
	dead lock
	
		
	
		
3. trigger by servelet MemTest
	http://192.168.72.1:7001/MyServer/MemTest
	
4. start jrmc to show mem growing
	add to type Graph
	show largest object
	list largest arrays
	add to instance graph
	double clck to find the MemoryConsumer class
	
	
	
5. dump mem to C:\>jrcmd 9176 hprofdump filename=mydump.hprof segment_threshold=2G segment_size=1G
	find in C:\oracle\Middleware\user_projects\domains\test_domain
	
6. visual vm to check the objects in mem
	
7. mat to check mem leak
	mem leak and show retained set
	
8. trigger thread test

9. jrmc console to check dead lock threads


mypack.service.MemoryConsumer

print_memusage
heap_diagnostics

start arp usage
--------------------
jrmc eclipse plugin
	http://download.oracle.com/technology/products/missioncontrol/updatesites/base/4.1.0/eclipse/update-site-instructions/index.html
	eclipse update site: http://download.oracle.com/technology/products/missioncontrol/updatesites/base/4.1.0/eclipse
	

illustration for ARP codes

com.oocl.ir4.arp.openservice.invoice.impl.DefaultInvoiceOpenService
com.oocl.ir4.arp.service.invoice.impl.DefaultInvoiceService

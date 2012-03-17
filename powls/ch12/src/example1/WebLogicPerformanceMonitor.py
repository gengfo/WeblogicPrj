import sys
import os

from java.io import *
from java.lang import *
from java.util import *

tracker = java.util.HashMap();	
try:
	delaySecs = System.getProperty("interval_seconds")
	protocol = System.getProperty("protocol")
	hostname = System.getProperty("hostname")
	port = System.getProperty("port")
	username = System.getProperty("username")
	password = System.getProperty("password")
except (Exception, Error, NameError):
	print "ERROR: Caught exception defining variables. Verify all inputs are defined. Exiting!"
	dumpStack()
	exit()

serverUrl = str(protocol) + "://" + str(hostname) + ":" + str(port)
print "Connecting to server " + serverUrl
connect(username, password, serverUrl)

while 1:
	domainConfig()
	serverNames = cmo.getServers()
	domainRuntime()
	now = java.util.Date()
	for server in serverNames:
		cd("/ServerRuntimes/" + str(server.getName()) +
		   "/ThreadPoolRuntime/ThreadPoolRuntime")
		compReqCount = cmo.getCompletedRequestCount()
		exeThrCount = cmo.getExecuteThreadTotalCount()
		pendUserReqCount = cmo.getPendingUserRequestCount()
		qLen = cmo.getQueueLength()
		tput = cmo.getThroughput()
		print server.getName() + " Stats at Time: " + now.toString() + "\n\t" + compReqCount + " Total Completed Requests\n" + "\t" + exeThrCount + " Execute Threads\n\t" + qLen + " Pending Requests (" + pendUserReqCount + " User, " + (qLen - pendUserReqCount) + " System)\n\t" + tput + " Total Throughput\n"

	java.lang.Thread.sleep(delaySecs*1000)




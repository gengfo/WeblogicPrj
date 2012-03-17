connect(sys.argv[1],sys.argv[2],sys.argv[3])

serverRuntime()
cd("SingleSignOnServicesRuntime/myserver")

cmo.publish(sys.argv[4])

print 'MetaData Published to '+sys.argv[4]

disconnect()
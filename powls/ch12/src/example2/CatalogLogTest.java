package professional.weblogic.ch12.example2;

import com.bigrez.logging.i18n.BigRezCatalogLogger;

public class CatalogLogTest
{
    private static void usage()
    {
	System.err.println("Usage: java [-Dweblogic.log.FileName=<log_file_name}>] professional.weblogic.ch12.example2.CatalogLogTest [<log_file_name>]\n");
	System.err.println("\tYou must specify the log file name either as a Java system property or a command-line argument.");
    }

    public static void main(String[] args) throws Exception
    {
	int len = args.length;
	String filename = System.getProperty("weblogic.log.FileName");

	if (len != 0)
	    filename = args[0];

	if (filename != null && filename.length() != 0) {
	    System.setProperty("weblogic.log.FileName", filename);
	    //	    usage();
	    //return;
	}
	BigRezCatalogLogger.logComplete("this is my test message");
    }
}

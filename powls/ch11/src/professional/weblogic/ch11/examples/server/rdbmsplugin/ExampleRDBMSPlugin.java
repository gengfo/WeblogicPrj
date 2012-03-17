package professional.weblogic.ch11.examples.server.rdbmsplugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import weblogic.management.security.ProviderMBean;
import weblogic.security.providers.authentication.CustomDBMSAuthenticatorPlugin;
import weblogic.security.providers.authentication.CustomDBMSAuthenticatorMBean;

public class ExampleRDBMSPlugin implements CustomDBMSAuthenticatorPlugin {

	@Override
	public void initialize(ProviderMBean mbean) {
		// TODO Auto-generated method stub
		System.out.println("Initializing Example RDBMS Plugin");
		
		CustomDBMSAuthenticatorMBean myMBean =
			(CustomDBMSAuthenticatorMBean)mbean;
		
		myMBean.getPluginProperties().list(System.out);
		
	}

	@Override
	public String lookupPassword(Connection conn, String user)
			throws SQLException {
		// TODO Auto-generated method stub

		
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt
					.executeQuery("select password from CH11_RDBMS_PLUGIN");

			while (rs.next()) {

				String password = rs.getString("password");

				System.out.println("The password for user "+user+" is *********");
				return password;

			}
			
			System.out.println("Couldn't find password for user "+user);
			return null;
		
	}

	@Override
	public String[] lookupUserGroups(Connection conn, String user)
			throws SQLException {
		
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt
					.executeQuery("select groups from CH11_RDBMS_PLUGIN");

			while (rs.next()) {

				String groups = rs.getString("groups");
				
				String [] theGroups = groups.split(":");
				
				System.out.println("The Groups for user "+user+" are "+Arrays.asList(theGroups));
				
				return theGroups;

			}
			
			System.out.println("Couldn't find groups for user "+user);
			
			return new String[]{};
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		System.out.println("Shutting Down Example RDBMS Plugin");

	}

	@Override
	public boolean userExists(Connection conn, String user) throws SQLException {
		// TODO Auto-generated method stub
		
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt
					.executeQuery("select groups from CH11_RDBMS_PLUGIN");

			while (rs.next()) {

				System.out.println("The user "+user+" exists");
				return true;

			}
			
			System.out.println("Couldn't find for user "+user);
			
			return false;
		
	}

}

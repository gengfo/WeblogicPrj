package gengfo.mypack.servlet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * This example demonstrates obtaining a database connection using a
 * <b>DataSource</b> object.
 *
 * @author Copyright (c) 2000,2010, Oracle and/or its affiliates.  All Rights Reserved.
 */
public class SimpleSql {

  public static void main(String argv[])
      throws Exception {

    java.sql.Connection conn = null;
    java.sql.Statement stmt = null;
    ResultSet rs = null;

    try {

      // ============== Get a database connection ==================

      Context ctx = null;

      // Put connection properties in to a hashtable.
      Hashtable<String,String> ht = new Hashtable<String,String>();
      ht.put(Context.INITIAL_CONTEXT_FACTORY,
          "weblogic.jndi.WLInitialContextFactory");
      ht.put(Context.PROVIDER_URL,
          "t3://localhost:7001");

      // Get a context for the JNDI lookup
      ctx = new InitialContext(ht);
      // Look up the data source
      javax.sql.DataSource ds
          = (javax.sql.DataSource) ctx.lookup("examples-dataSource-demoXAPool");
      //Get a database connection from the data source
      conn = ds.getConnection();
      conn.setAutoCommit(true);

      System.out.println("Making connection...\n");

      // ============== Execute SQL statements ======================
      stmt = conn.createStatement();

      try {
        stmt.execute("drop table empdemo");
        System.out.println("Table empdemo dropped.");
      } catch (SQLException e) {
        System.out.println("Table empdemo doesn't need to be dropped.");
      }

      stmt.execute("create table empdemo (empid int, name varchar(30), dept int)");
      System.out.println("Table empdemo created.");

      int numrows = stmt.executeUpdate("insert into empdemo values (0, 'John Smith', 12)");
      System.out.println("Number of rows inserted = " + numrows);

      stmt.execute("select * from empdemo");

      rs = stmt.getResultSet();
      System.out.println("Querying data...");
      while (rs.next()) {
        System.out.println("  ID: " + rs.getString("empid") +
            "\n  Name: " + rs.getString("name") +
            "\n  Dept: " + rs.getString("dept"));
      }

      numrows = stmt.executeUpdate("delete from empdemo where empid = 0");
      System.out.println("Number of rows deleted = " + numrows);
    } catch (Exception e) {
      System.out.println("Exception was thrown: " + e.getMessage());
    } finally {
	  // ========== Close JDBC objects, including the connection =======	
      try {
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
      } catch (SQLException e) {
        throw e;
      } finally {
        try {
          if (conn != null) {
            conn.close();
            conn = null;
          }
        } catch (SQLException e) {
          throw e;
        }
      }
    }
  }
}

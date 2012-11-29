package gengfo.mypack.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This example demonstrates using a DataSource to get a connection to a
 * database and execute a simple sql query using the connection in a servlet.
 * The example uses the PointBase database management system, provided in an
 * evaluation version with WebLogic Server.
 * 
 * @author Copyright (c) 1996-98 by WebLogic, Inc. All Rights Reserved.
 * @author Copyright (c) 1999,2010, Oracle and/or its affiliates. All Rights
 *         Reserved.
 */

public class SimpleSqlServlet extends HttpServlet {

	public void service(HttpServletRequest req, HttpServletResponse res)
			throws IOException {

		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		// out.println(ExampleUtils.returnHtmlHeader("Simple SQL - DataSource"));
		out.println("Attempting connection....");

		java.sql.Connection conn = null;
		java.sql.Statement stmt = null;
		ResultSet rs = null;

		try {
			// ============== Get a database connection ==================

			// Get a context for the JNDI look up
			Context ctx = new InitialContext();

			// Look up a data source
			oracle.jdbc.xa.client.OracleXADataSource ds = (oracle.jdbc.xa.client.OracleXADataSource) ctx
					.lookup("ipsDev");

			// Create a connection object
			conn = ds.getConnection();
			conn.setAutoCommit(true);

			out.println("<p>Connection successful...<p>Executing SQL...<p>");

			// ============== Execute SQL statements ==================
			stmt = conn.createStatement();

			// try {
			// //stmt.execute("drop table empdemo");
			// out.println("Table empdemo dropped...<p>");
			// }
			// //catch (SQLException e) {
			// out.println("<p>Table empdemo does not need to be dropped...<p>");
			// }

			// stmt.execute("create table empdemo (empid int, name varchar(30), dept int)");
			// out.println("<p>Table empdemo created...");

			// int numrows = stmt
			// .executeUpdate("insert into empdemo values (0,'John Smith', 12)");
			// out.println("<p>Number of rows inserted = " + numrows);

			stmt.execute("select * from ARP_INV_NOTE");

			rs = stmt.getResultSet();
			out.println("<hr>Querying data ...<br>");
			// while (rs.next()) {
			// out.println("<br><b>ID:  </b> " + rs.getString("empid")
			// + "<br><b>Name:</b> " + rs.getString("name")
			// + "<br><b>Dept:</b> " + rs.getString("dept") + "<hr>");
			// }

			ResultSetMetaData rsmd = rs.getMetaData();

			out.println("Querying table meta data...<p>");
			out.println("Number of Columns: " + rsmd.getColumnCount() + "<b>");

			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				out.println("<p>Column Name: " + rsmd.getColumnName(i));
				out.println("<br>Nullable: " + rsmd.isNullable(i));
				out.println("<br>Precision: " + rsmd.getPrecision(i));
				out.println("<br>Scale: " + rsmd.getScale(i));
				out.println("<br>Size: " + rsmd.getColumnDisplaySize(i));
				out.println("<br>Column Type: " + rsmd.getColumnType(i));
				out.println("<br>Column Type Name: "
						+ rsmd.getColumnTypeName(i));
				out.println("<br><hr>");
			}
		} catch (Exception ex) {
			out.println("Connection error.");
			ex.printStackTrace(new PrintWriter(out));
		} finally {
			// ============ Close JDBC objects, including the connection =======
			try {
				cleanup(rs, stmt, conn);

				out.println("</b><h3>Example finished...</h3></body></html>");
				// out.println(ExampleUtils.returnHtmlFooter());
				return;
			} catch (SQLException sqle) {
				out.println("Exception during close()" + sqle.getMessage());
			}
		}
	}

	private void cleanup(ResultSet pResultSet, Statement pStmt, Connection pConn)
			throws SQLException {
		try {
			if (pResultSet != null) {
				pResultSet.close();
				pResultSet = null;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (pStmt != null) {
					pStmt.close();
					pStmt = null;
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				try {
					if (pConn != null) {
						pConn.close();
						pConn = null;
					}
				} catch (SQLException e) {
					throw e;
				}
			}
		}
	}
}

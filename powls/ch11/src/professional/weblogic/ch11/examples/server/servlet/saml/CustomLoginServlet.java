package professional.weblogic.ch11.examples.server.servlet.saml;

import java.io.IOException;


import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weblogic.servlet.security.ServletAuthentication;

/**
 * Servlet implementation class for Servlet: Login
 * 
 */
public class CustomLoginServlet extends javax.servlet.http.HttpServlet  {
	static final long serialVersionUID = 1L;

	

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		final String user = request.getParameter("user");
		final String password = request.getParameter("password");
		String returnURL = request.getParameter("returnURL");

		
		
		try {
			int rc = ServletAuthentication.login(new CallbackHandler() {

				@Override
				public void handle(Callback[] callbacks) throws IOException,
						UnsupportedCallbackException {
					// TODO Auto-generated method stub
					for (int i = 0; i < callbacks.length; i++) {

						Callback c = callbacks[i];

						if (c instanceof NameCallback) {

							NameCallback nc = (NameCallback) c;
							nc.setName(user);
						} else if (c instanceof PasswordCallback) {

							PasswordCallback pc = (PasswordCallback) c;
							pc.setPassword(password.toCharArray());

						} else {
							System.out.println("Unknown callback " + c);
						}

					}

				}

			},request);
			
			response.sendRedirect(returnURL);
			
			
		} catch (LoginException le) {

			le.printStackTrace();
			response.sendRedirect("index.jsp?returnURL="+returnURL);
		}
	}
}
package professional.weblogic.ch02.example1;

import weblogic.servlet.jsp.JspBase;
import java.util.List;

import javax.servlet.http.*;

public abstract class MyJspBase extends JspBase
{

	protected static org.apache.log4j.Category LOG = org.apache.log4j.Category.getInstance("JSP");

	protected int atoi(String pValue) {
		try {
			return new Integer(pValue).intValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	protected float atof(String pValue) {
		try {
			return new Float(pValue).floatValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	protected String getr(HttpServletRequest pRequest) {
		String result = pRequest.getQueryString();
		if (result==null) result = "";
		LOG.debug("getr() returning " + result);
		return result;
	}

    protected String getr(HttpServletRequest pRequest, String pParamName) {
        String[] results = pRequest.getParameterValues(pParamName);
        String result = "";
        if (results == null || results.length == 0) {
            // leave result as empty string..
        } else if (results.length == 1) {
            result = results[0];
        } else {
            for (int ii=0; ii<results.length; ii++) {
                result += results[ii];
                if (ii<results.length) result += " ";  // space delimit multiple values
            }
        }
        LOG.debug("getr("+pParamName+") returning " + result);
        return result; 
    }

	//Returns the object in the session that is associated with the supplied key
	protected String gets(HttpSession pSession, String pKey) {       
		String result = (String)pSession.getAttribute(pKey);
		if (result==null) result = "";
		LOG.debug("gets("+pKey+") returning " + result);
		return result;
	}

	//Places an object(value) into the session associating it with the supplied key
	protected void sets(HttpSession pSession, String pKey, String pValue) {
		LOG.debug("sets("+pKey+") setting " + pValue);
		pSession.setAttribute(pKey,pValue);
		return;
	}

    //Sends a redirect response to the client with the new location
    protected void redirect(HttpServletResponse pResponse, String pLocation)
    throws java.io.IOException
    {
        if (pLocation.indexOf("/") == -1) {
            pLocation = "/" + pLocation; // Fix for WLS 6.0
        }
        LOG.info(">>> redirect to "+pLocation);
        pResponse.sendRedirect(pLocation);
        pResponse.flushBuffer();
        return;
    }

	protected String makeSelectList(String pName, List<String> pKeys, List<String> pValues, String pSelectedKey) {

		StringBuffer sb = new StringBuffer(100);

		sb.append("<SELECT NAME='" + pName + "'");
		sb.append(">\n");

		for (int jj=0; jj<pKeys.size(); jj++) {
			String key = (String)(pKeys.get(jj));
			String value = (String)(pValues.get(jj));
			sb.append("<OPTION VALUE='"+key+"'"+(key.equals(pSelectedKey)?" SELECTED":"")+">"+value+"</OPTION>\n");
		}
		sb.append("</SELECT>");

		return sb.toString();
	}

	protected String makeErrorDisplay(List<String> pErrors) {

		if (pErrors.size()>0) {
			StringBuffer sb = new StringBuffer(100);
			sb.append("<span class=\"error-header1\">Validation Errors<br></span><span class=\"error-header2\">You must correct the following error(s) before proceeding:</span><ul>\n");
			for (int jj=0; jj<pErrors.size(); jj++) {
				sb.append("<li><span class=\"error-text\">" + pErrors.get(jj) + "</span></li>\n");
			}
			sb.append("</ul>");
			return sb.toString();
		} else {
			return "";
		}
	}

    protected String getSubmitToken(HttpSession pSession) {

        String token = pSession.getId().substring(0,40) + "-" + System.currentTimeMillis();
        pSession.setAttribute("submittoken",token);
        return token;
    }

    protected boolean checkSubmitToken(HttpSession pSession, String pToken) {

        String savedToken = (String) pSession.getAttribute("submittoken");
        if (pToken.equals("") || savedToken==null || savedToken.equals("")) return false;
        return (pToken.equals(savedToken)); // return true if this is a valid token
    }

	protected void clearSubmitToken(HttpSession pSession) {

		pSession.removeAttribute("submittoken");
		return;
	}

}

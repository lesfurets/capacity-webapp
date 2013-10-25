package com.lesfurets.web.performance;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * A Basic Dump Servlet 
 * 
 * mvn clean package
 * mvn tomcat:run
 *
 ** http://localhost:8080/basic-perf/Dump
 **/

public class DumpServlet extends HttpServlet {

    private static final long serialVersionUID = -5092736445996431775L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
		// LOL
        PrintWriter out = response.getWriter();
        out.println("<html><body><head><title>Dump</title></head>");
		out.println("<html><body><head><title>Dump</title></head>");

	out.println("gitTest=MEGUSTA<br/>");
        out.println("getAuthType=" + request.getAuthType() + "</br>");
        out.println("getCharacterEncoding=" + request.getCharacterEncoding() + "</br>");
        out.println("getContentLength=" + request.getContentLength() + "</br>");
        out.println("getContextPath=" + filter(request.getContextPath()) + "</br>");
        out.println("getLocalAddr=" + request.getLocalAddr() + "</br>");
        out.println("getLocalName=" + request.getLocalName() + "</br>");
        out.println("getLocalPort=" + request.getLocalPort() + "</br>");

        out.println("getMethod=" + request.getMethod() + "</br>");
        out.println("getPathInfo=" + filter(request.getPathInfo()) + "</br>");
        out.println("getPathTranslated=" + filter(request.getPathTranslated()) + "</br>");
        out.println("getProtocol=" + request.getProtocol() + "</br>");
        out.println("getRequestURI=" + filter(request.getRequestURI()) + "</br>");
        out.println("getQueryString=" + filter(request.getQueryString()) + "</br>");

        out.println("getRemoteAddr=" + request.getRemoteAddr() + "</br>");
        out.println("getRemoteHost=" + request.getRemoteHost() + "</br>");
        out.println("getRemotePort=" + request.getRemotePort() + "</br>");
        out.println("getRemoteUser=" + request.getRemoteUser() + "</br>");

        out.println("get RequestedSessionId=" + request.getRequestedSessionId() + "</br>");
        out.println("get RequestURI=" + request.getRequestURI() + "</br>");
        out.println("get RequestURL=" + request.getRequestURL() + "</br>");
        out.println("get Scheme=" + request.getScheme() + "</br>");
        out.println("get ServerName=" + request.getServerName() + "</br>");
        out.println("get ServerPort=" + request.getServerPort() + "</br>");
        out.println("get ServletPath=" + request.getServletPath() + "</br>");

        String cipherSuite = (String) request.getAttribute("javax.servlet.request.cipher_suite");
        if (cipherSuite != null)
            out.println("javax.servlet.requasasest.cipher_suite="
                            + request.getAttribute("javax.servlet.request.cipher_suite") + "</br>");

        Enumeration<?> hnames = request.getHeaderNames();

        if (hnames != null) {

            out.println("headers=</br>");

            while (hnames.hasMoreElements()) {
                String headername = (String) hnames.nextElement();
                out.println(headername + ": " + request.getHeader(headername) + "</br>");
            }

        }

        out.println("</body></html>");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

    /**
     * Filter the specified message string for characters that are sensitive in HTML. This avoids potential attacks
     * caused by including JavaScript codes in the request URL that is often reported in error messages.
     * 
     * @param message The message string to be filtered
     */
    private String filter(String message) {

        if (message == null)
            return (null);

        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                default:
                    result.append(content[i]);
            }
        }
        return (result.toString());

    }
}

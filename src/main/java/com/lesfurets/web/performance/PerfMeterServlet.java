package com.lesfurets.web.performance;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * A Basic Performance Servlet
 * 
 * features :
 * 
 * * configurable wait-time (in nano-seconds) with waittime parameter (-1 to disable) * configurable response-size (in
 * char) with responsesize (-1 to disable) * configurable response content (string) with response
 * 
 * examples :
 * 
 * mvn clean package
 * 
 * mvn tomcat:run mvn jetty:run
 * 
 * * http://localhost:8080/basic-perf/PerfMeter?waittime=100&responsesize=100000 *
 * http://localhost:8080/basic-perf/PerfMeter?waittime=100&responsesize=-1 *
 * http://localhost:8080/basic-perf/PerfMeter?waittime=100&responsesize=8000&response=ShowMustGoOn
 * 
 * 
 * @author herve le furet
 */
public class PerfMeterServlet extends HttpServlet {

    private long defaultWaitTime = 1000000L;
    private int defaultResponseSize = -1;
    private String defaultResponse = "OK";

    /**
     * Initialize this servlet.
     */
    @Override
    public void init() throws ServletException {

        String lWaitTimeS = getServletConfig().getInitParameter("wait time"); // WaitTime in nano seconds
        String lResponseSizeS = getServletConfig().getInitParameter("response size"); // ResponseSize in bytes

        try {
            if (lWaitTimeS != null)
                defaultWaitTime = Long.parseLong(lWaitTimeS);

            if (lResponseSizeS != null)
                defaultResponseSize = Integer.parseInt(lResponseSizeS);
        } catch (NumberFormatException nfe) {
            StringWriter sw = new StringWriter();
            nfe.printStackTrace(new PrintWriter(sw));
            System.err.println(sw.toString());
        }

        defaultResponse = getServletConfig().getInitParameter("response"); // Response pattern
    }

    /**
     * Process a GET request for the specified resource.
     * 
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * 
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet-specified error occurs
     */
    @Override
    public void doGet(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException, ServletException {

        pResponse.setContentType("text/plain");

        PrintWriter lWriter = pResponse.getWriter();

        String lWaitTimeS = pRequest.getParameter("waittime"); // WaitTime in nano seconds
        String lResponseSizeS = pRequest.getParameter("responsesize"); // ResponseSize in bytes
        String lResponseS = pRequest.getParameter("response"); // ResponseSize in bytes

        long lWaitTime = defaultWaitTime;
        int lResponseSize = defaultResponseSize;
        String lResponse = defaultResponse;
        int lResponseLenght = defaultResponse.length();

        try {
            if (lWaitTimeS != null)
                lWaitTime = Long.parseLong(lWaitTimeS);

            if (lResponseSizeS != null)
                lResponseSize = Integer.parseInt(lResponseSizeS);
        } catch (NumberFormatException nfe) {
            StringWriter sw = new StringWriter();
            nfe.printStackTrace(new PrintWriter(sw));
            System.err.println(sw.toString());
        }

        if (lResponseS != null)
            lResponse = lResponseS;

        if (lResponse != null)
            lResponseLenght = lResponse.length();

        if (lWaitTime != -1) {

            try {
                Thread.sleep(lWaitTime / 1000L, (int) (lWaitTime % 1000L));
            } catch (InterruptedException ie) {
                StringWriter sw = new StringWriter();
                ie.printStackTrace(new PrintWriter(sw));
                System.err.println(sw.toString());
            }
        }

        if (lResponseLenght >= 0) {
            while (lResponseSize >= 0) {

                if (lResponseLenght > lResponseSize)
                    lWriter.write(lResponse, 0, lResponseSize);
                else
                    lWriter.write(lResponse);

                lResponseSize -= lResponseLenght;
            }
        }

    }

}

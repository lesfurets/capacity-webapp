/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package com.lesfurets.web;

import java.io.File;

import junit.framework.Assert;

import org.apache.catalina.LifecycleState;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.exporter.zip.ZipExporterImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.lesfurets.web.performance.PerfMeterServlet;


public class TomcatRunnerTest {
    /** The tomcat instance. */
    private Tomcat mTomcat;
    /** The temporary directory in which Tomcat and the app are deployed. */
    private String mWorkingDir = System.getProperty("java.io.tmpdir");

    @Before
    public void setup() throws Throwable {
        mTomcat = new Tomcat();
        mTomcat.setPort(9090);
        mTomcat.setBaseDir(mWorkingDir);
        mTomcat.getHost().setAppBase(mWorkingDir);
        mTomcat.getHost().setAutoDeploy(true);
        mTomcat.getHost().setDeployOnStartup(true);
        String contextPath = "/" + getApplicationId();
        
        File webApp = new File(mWorkingDir, getApplicationId());
        File oldWebApp = new File(webApp.getAbsolutePath());
        FileUtils.deleteDirectory(oldWebApp);
        
        final File webappDir = new File(mWorkingDir + "/" + getApplicationId() + ".war");
        System.out.println("Webapp dir : " + webappDir);
        final Archive<?> archive = createWebArchive();
        new ZipExporterImpl(archive).exportTo(webappDir, true);
//        new ExplodedExporterImpl(archive).exportExploded(new File(mWorkingDir), getApplicationId());

        mTomcat.addWebapp(mTomcat.getHost(), contextPath, webApp.getAbsolutePath());

        mTomcat.start();

    }
    
    private static final String WEBAPP_SRC = "src/main/webapp";


    private Archive<?> createWebArchive() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "perfwebapp.war");
        archive.setWebXML(new File(WEBAPP_SRC, "WEB-INF/web.xml"));
        archive.addClasses(PerfMeterServlet.class)
            .addAsWebResource(new File(WEBAPP_SRC, "index.html"));
//        archive.addPackage(java.lang.Package.getPackage("com.lesfurets.web.performance"));
        return archive;
    }

    private String getApplicationId() {
        return "perf-webapp";
    }

    @Test
    public void testTomcat() {
        WebDriver driver = new HtmlUnitDriver();
        driver.get(getAppBaseURL() + "/PerfMeter?&responsesize=2");
        Assert.assertEquals("OK", driver.getPageSource());
    }
    
    /**
     * @return the URL the app is running on
     */
    protected String getAppBaseURL() {
        return "http://localhost:" + getTomcatPort() + "/" + getApplicationId();
    }


    protected int getTomcatPort() {
        return mTomcat.getConnector().getLocalPort();
    }

    @After
    public final void teardown() throws Throwable {
        if (mTomcat.getServer() != null && mTomcat.getServer().getState() != LifecycleState.DESTROYED) {
            if (mTomcat.getServer().getState() != LifecycleState.STOPPED) {
                mTomcat.stop();
            }
            mTomcat.destroy();
        }
    }

}

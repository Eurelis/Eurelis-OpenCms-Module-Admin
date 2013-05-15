/**
 * This file is part of the Eurelis OpenCms Admin Module.
 * 
 * Copyright (c) 2013 Eurelis (http://www.eurelis.com)
 *
 * This module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this module. 
 * If not, see <http://www.gnu.org/licenses/>
 */
package com.eurelis.opencms.admin.systeminformation;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsStringUtil;
import org.opencms.widgets.CmsDisplayWidget;
import org.opencms.widgets.CmsInputWidget;
import org.opencms.widgets.CmsSelectWidgetOption;
import org.opencms.workplace.CmsWidgetDialog;
import org.opencms.workplace.CmsWidgetDialogParameter;

import com.eurelis.opencms.admin.CmsAdminSettings;

/**
 * The system infos overview dialog.<p>
 * 
 */
public class CmsSystemInformationOverviewDialog extends CmsWidgetDialog {
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsSystemInformationOverviewDialog.class);

    /** localized messages Keys prefix. */
    public static final String KEY_PREFIX = "sysinfo.stats";

    /** Defines which pages are valid for this dialog. */
    public static final String[] PAGES = {"page1"};

    /** System infos OS. */
    private String m_operatingSystem;
    
    /** System infos . */
    private String m_javaVersion;
    
    /** System infos . */
    private String m_jvmUptime;
    
    /** System infos . */
    private String m_jvmStarttime;
    
    /** System infos . */
    private String m_opencmsVersion;
    
    /** System infos . */
    private String m_opencmsRuntime;
    
    /** System infos . */
    private String m_opencmsStartupTime;
    
    /** The admin settings object that is edited on this dialog. */
    protected CmsAdminSettings m_adminSettings;


    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsSystemInformationOverviewDialog(CmsJspActionElement jsp) {

        super(jsp);

    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsSystemInformationOverviewDialog(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * Commits the edited interval to the db.<p>
     */
    public void actionCommit() {
        
    	LOG.debug("Admin settings actionCommit...");
        List errors = new ArrayList();
        setDialogObject(m_adminSettings);

        int interval = m_adminSettings.getInterval();
        LOG.debug("Admin settings actionCommit : m_adminSettings.getInterval() = " + interval);

        //memorisation system du parametre...
        CmsAdminSettings.setSettingsIntervalValue(getCms(), interval, getSession());

        // set the list of errors to display when saving failed
        setCommitErrors(errors);
        
    }

    /**
     * Returns the OS.<p>
     *
     * @return the OS
     */
    public String getOperatingSystem() {

        return m_operatingSystem;
    }

    /**
     * Returns the java version.<p>
     *
     * @return the java version
     */
    public String getJavaVersion() {

        return m_javaVersion;
    }

    /**
     * Returns the JVM uptime.<p>
     *
     * @return the JVM uptime
     */
    public String getJvmUptime() {

        return m_jvmUptime;
    }

    /**
     * Returns the JVM start time.<p>
     *
     * @return the JVM start time
     */
    public String getJvmStarttime() {

        return m_jvmStarttime;
    }
    
    /**
     * Returns the OpenCms version.<p>
     *
     * @return the OpenCms version
     */
    public String getOpenCmsVersion() {

        return m_opencmsVersion;
    }
    
    /**
     * Returns the OpenCms runtime.<p>
     *
     * @return the OpenCms runtime
     */
    public String getOpenCmsRuntime() {

        return m_opencmsRuntime;
    }
    
    /**
     * Returns the OpenCms startup time.<p>
     *
     * @return the OpenCms startup time
     */
    public String getOpenCmsStartupTime() {

        return m_opencmsStartupTime;
    }


    /**
     * Sets the OS.<p>
     *
     * @param operatingSystem the OS to set
     */
    public void setOperatingSystem(String operatingSystem) {

        m_operatingSystem = operatingSystem;
    }

    /**
     * Sets the java version.<p>
     *
     * @param javaVersion the java version to set
     */
    public void setJavaVersion(String javaVersion) {

        m_javaVersion = javaVersion;
    }

    /**
     * Sets the jvm uptime.<p>
     *
     * @param jvmUptime the jvm uptime to set
     */
    public void setJvmUptime(String jvmUptime) {

        m_jvmUptime = jvmUptime;
    }

    /**
     * Sets the jvm StartTime.<p>
     *
     * @param jvmStartTime the jvm StartTime to set
     */
    public void setJvmStarttime(String jvmStarttime) {

        m_jvmStarttime = jvmStarttime;
    }
    
    /**
     * Sets the OpenCms version.<p>
     *
     * @param opencmsVersion the OpenCms version to set
     */
    public void setOpenCmsVersion(String opencmsVersion) {

        m_opencmsVersion = opencmsVersion;
    }
    
    /**
     * Sets the OpenCms runtime.<p>
     *
     * @param opencmsRuntime the OpenCms runtime to set
     */
    public void setOpenCmsRuntime(String opencmsRuntime) {

        m_opencmsRuntime = opencmsRuntime;
    }
    
    /**
     * Sets the OpenCms startup time.<p>
     *
     * @param opencmsStartupTime the OpenCms startup time to set
     */
    public void setOpenCmsStartupTime(String opencmsStartupTime) {

        m_opencmsStartupTime = opencmsStartupTime;
    }
   

    /**
     * Creates the dialog HTML for all defined widgets of the named dialog (page).<p>
     * 
     * This overwrites the method from the super class to create a layout variation for the widgets.<p>
     * 
     * @param dialog the dialog (page) to get the HTML for
     * @return the dialog HTML for all defined widgets of the named dialog (page)
     */
    protected String createDialogHtml(String dialog) {

        StringBuffer result = new StringBuffer(1024);

        // create widget table
        result.append(createWidgetTableStart());

        // show error header once if there were validation errors
        result.append(createWidgetErrorHeader());

        if (dialog.equals(PAGES[0])) {
            // create the widgets for the first dialog page
            result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_BLOCK_1)));
            result.append(createWidgetTableStart());
            result.append(createDialogRowsHtml(0, 6));
            result.append(createWidgetTableEnd());
            result.append(dialogBlockEnd());
            
            // create the widgets for the settings page
            result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_SETTINGS_NAME_0)));
            result.append(createWidgetTableStart());
            result.append(createDialogRowsHtml(7, 7));
            result.append(createWidgetTableEnd());
            result.append(dialogBlockEnd());
        }

        // close widget table
        result.append(createWidgetTableEnd());

        return result.toString();
    }

    /**
     * @see org.opencms.workplace.CmsWidgetDialog#defaultActionHtmlEnd()
     */
    protected String defaultActionHtmlEnd() {

        return "";
    }

    /**
     * Creates the list of widgets for this dialog.<p>
     */
    protected void defineWidgets() {

        // initialize the cache object to use for the dialog
        initInfosObject();

        setKeyPrefix(KEY_PREFIX);

        // widgets to display
        addWidget(new CmsWidgetDialogParameter(this, "operatingSystem", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "javaVersion", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "jvmUptime", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "jvmStarttime", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "openCmsVersion", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "openCmsRuntime", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "openCmsStartupTime", PAGES[0], new CmsDisplayWidget()));
           
        //addWidget(new CmsWidgetDialogParameter(m_adminSettings, "interval", PAGES[0], new CmsSelectWidget(getIntervals())));
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "interval", PAGES[0], new CmsInputWidget()));
        
    }

    /**
     * @see org.opencms.workplace.CmsWidgetDialog#getPageArray()
     */
    protected String[] getPageArray() {

        return PAGES;
    }

    /**
     * Initializes the infos object.<p>
     */
    protected void initInfosObject() {
    	
    	com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
    	java.lang.management.OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    	java.lang.management.ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    	java.lang.management.RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean(); 
    	java.lang.management.ClassLoadingMXBean classesBean = ManagementFactory.getClassLoadingMXBean(); 
    	
    	//JVM uptime
    	Date date = new Date(runtimeBean.getUptime());
    	java.text.SimpleDateFormat simpleFormatH = new java.text.SimpleDateFormat("HH");
    	java.text.SimpleDateFormat simpleFormatM = new java.text.SimpleDateFormat("mm");
    	java.text.SimpleDateFormat simpleFormatS = new java.text.SimpleDateFormat("ss");
    	String jvmuptimestring = simpleFormatH.format(date) + "h " + simpleFormatM.format(date) + "min " + simpleFormatS.format(date) + "s ";
    	
    	//JVM start time
    	date = new Date(runtimeBean.getStartTime());
    	String jvmstarttimestring = simpleFormatH.format(date) + "h " + simpleFormatM.format(date) + "min " + simpleFormatS.format(date) + "s ";
    	
    	//OpenCms runtime
    	date = new Date(OpenCms.getSystemInfo().getRuntime());
    	String opencmsruntimestring = simpleFormatH.format(date) + "h " + simpleFormatM.format(date) + "min " + simpleFormatS.format(date) + "s ";
    	
    	//OpenCms startup time
    	date = new Date(OpenCms.getSystemInfo().getStartupTime());
    	String opencmsstartuptimestring = simpleFormatH.format(date) + "h " + simpleFormatM.format(date) + "min " + simpleFormatS.format(date) + "s ";
    	
    	setOperatingSystem(""+osBean.getName());
    	setJavaVersion(""+runtimeBean.getVmVersion());
    	setJvmUptime(""+jvmuptimestring);
    	setJvmStarttime(""+jvmstarttimestring);
    	setOpenCmsVersion(OpenCms.getSystemInfo().getVersionNumber());
    	setOpenCmsRuntime(""+opencmsruntimestring);
    	setOpenCmsStartupTime(""+opencmsstartuptimestring);

    	
    	Object o;
        if (CmsStringUtil.isEmpty(getParamAction())) {
            o = new CmsAdminSettings(getSession());
        } else {
            // this is not the initial call, get the job object from session
            o = getDialogObject();
        }
        if (!(o instanceof CmsAdminSettings)) {
            // create a new history settings handler object
            m_adminSettings = new CmsAdminSettings(getSession());
        } else {
            // reuse html import handler object stored in session
            m_adminSettings = (CmsAdminSettings)o;
        }
        
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initMessages()
     */
    protected void initMessages() {

        // add specific dialog resource bundle
        addMessages(Messages.get().getBundleName());
        // add default resource bundles
        super.initMessages();
    }

    /**
     * Overridden to set the online help path for this dialog.<p>
     * 
     * @see org.opencms.workplace.CmsWorkplace#initWorkplaceMembers(org.opencms.jsp.CmsJspActionElement)
     */
    protected void initWorkplaceMembers(CmsJspActionElement jsp) {

        super.initWorkplaceMembers(jsp);
        setOnlineHelpUriCustom("/eurelis_system_information/");
    }
    
    
    
    /**
     * Returns a list with the possible interval to choose from.<p>
     * 
     * @return a list with the possible interval to choose from
     */
    private List getIntervals() {

        ArrayList ret = new ArrayList();

        //recuperation du parametre memorise en system
        int defaultInterval = CmsAdminSettings.getSettingsIntervalValue(getCms(), getSession());
        
        ret.add(new CmsSelectWidgetOption(
            String.valueOf(5000),
            (defaultInterval==5000),
            key(Messages.GUI_ADMIN_SETTINGS_INTERVALS_5000))); 

        ret.add(new CmsSelectWidgetOption(
            String.valueOf(10000),
            (defaultInterval==10000),
            key(Messages.GUI_ADMIN_SETTINGS_INTERVALS_10000)));

        return ret;
    }
}

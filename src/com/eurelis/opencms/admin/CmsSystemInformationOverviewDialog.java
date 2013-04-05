package com.eurelis.opencms.admin;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsProperty;
import org.opencms.file.CmsResource;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsException;
import org.opencms.main.CmsIllegalArgumentException;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsStringUtil;
import org.opencms.widgets.CmsDisplayWidget;
import org.opencms.widgets.CmsSelectWidget;
import org.opencms.widgets.CmsSelectWidgetOption;
import org.opencms.workplace.CmsWidgetDialog;
import org.opencms.workplace.CmsWidgetDialogParameter;

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

        boolean enabled = m_adminSettings.getInterval() > 0;
        int interval = m_adminSettings.getInterval();
        LOG.debug("Admin settings actionCommit : m_adminSettings.getInterval() = " + interval);

        //memorisation system du parametre...
        CmsAdminSettings.setSettingsIntervalValue(getCms(), interval);

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
            result.append(createDialogRowsHtml(0, 3));
            result.append(createWidgetTableEnd());
            result.append(dialogBlockEnd());
            
            // create the widgets for the settings page
            result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_SETTINGS_NAME_0)));
            result.append(createWidgetTableStart());
            result.append(createDialogRowsHtml(4, 4));
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
           
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "interval", PAGES[0], new CmsSelectWidget(getIntervals())));
        
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
    	
    	setOperatingSystem(""+osBean.getName());
    	setJavaVersion(""+runtimeBean.getVmVersion());
    	setJvmUptime(""+jvmuptimestring);
    	setJvmStarttime(""+jvmstarttimestring);

    	
    	Object o;
        if (CmsStringUtil.isEmpty(getParamAction())) {
            o = new CmsAdminSettings();
        } else {
            // this is not the initial call, get the job object from session
            o = getDialogObject();
        }
        if (!(o instanceof CmsAdminSettings)) {
            // create a new history settings handler object
            m_adminSettings = new CmsAdminSettings();
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
        int defaultInterval = CmsAdminSettings.getSettingsIntervalValue(getCms());
        LOG.debug("Admin settings getIntervals(), defaultInterval = " + defaultInterval);
        LOG.debug("Admin settings getIntervals(), defaultInterval =? 5000 " + (defaultInterval==5000));
        LOG.debug("Admin settings getIntervals(), defaultInterval =? 10000 " + (defaultInterval==10000));
        
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

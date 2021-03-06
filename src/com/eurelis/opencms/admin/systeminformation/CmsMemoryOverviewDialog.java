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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsLog;
import org.opencms.util.CmsStringUtil;
import org.opencms.widgets.CmsCheckboxWidget;
import org.opencms.widgets.CmsDisplayWidget;
import org.opencms.workplace.CmsWidgetDialog;
import org.opencms.workplace.CmsWidgetDialogParameter;

import com.eurelis.opencms.admin.CmsAdminSettings;

/**
 * The system infos overview dialog.<p>
 * 
 */
public class CmsMemoryOverviewDialog extends CmsWidgetDialog {
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsMemoryOverviewDialog.class);

    /** localized messages Keys prefix. */
    public static final String KEY_PREFIX = "memory.stats";

    /** Defines which pages are valid for this dialog. */
    public static final String[] PAGES = {"page0"};

    /** System infos . */
    private String m_memPermMax;
    /** System infos . */
    private String m_memPermTotal;
    /** System infos . */
    private String m_memPermUsed;
    /** System infos . */
    private String m_memOldMax;
    /** System infos . */
    private String m_memOldTotal;
    /** System infos . */
    private String m_memOldUsed;
    /** System infos . */
    private String m_memEdenMax;
    /** System infos . */
    private String m_memEdenTotal;
    /** System infos . */
    private String m_memEdenUsed;
    /** System infos . */
    private String m_memSurvivorMax;
    /** System infos . */
    private String m_memSurvivorTotal;
    /** System infos . */
    private String m_memSurvivorUsed;
    
    /** The admin settings object that is edited on this dialog. */
    protected CmsAdminSettings m_adminSettings;
    
    private int frequencyInMillis = CmsAdminSettings.getSettingsIntervalValue(getCms(), getSession());
    private String jsonPath = getJsp().link("/system/workplace/admin/eurelis_system_information/json/getSystemInfo.json");


    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsMemoryOverviewDialog(CmsJspActionElement jsp) {

        super(jsp);

    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsMemoryOverviewDialog(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * Commits the edited group to the db.<p>
     */
    public void actionCommit() {

    	LOG.debug("Admin settings actionCommit...");
        List errors = new ArrayList();
        setDialogObject(m_adminSettings);

        boolean enabled = m_adminSettings.getInterval() > 0;
        int interval = m_adminSettings.getInterval();
        LOG.debug("Admin settings actionCommit : m_adminSettings.getInterval() = " + interval);
        
        boolean displayMemPerm = m_adminSettings.getDisplayMemPerm();
        LOG.debug("Admin settings actionCommit : m_adminSettings.getDisplayMemPerm() = " + displayMemPerm);
        
        boolean displayMemOld = m_adminSettings.getDisplayMemOld();
        LOG.debug("Admin settings actionCommit : m_adminSettings.getDisplayMemOld() = " + displayMemOld);
        
        boolean displayMemEden = m_adminSettings.getDisplayMemEden();
        LOG.debug("Admin settings actionCommit : m_adminSettings.getDisplayMemEden() = " + displayMemEden);
        
        boolean displayMemSurvivor = m_adminSettings.getDisplayMemSurvivor();
        LOG.debug("Admin settings actionCommit : m_adminSettings.getDisplayMemSurvivor() = " + displayMemSurvivor);

        //memorisation system du parametre...
        CmsAdminSettings.setSettingsIntervalValue(getCms(), interval, getSession());
        CmsAdminSettings.setSettingsDisplayMemPermValue(getCms(), displayMemPerm, getSession());
        CmsAdminSettings.setSettingsDisplayMemOldValue(getCms(), displayMemOld, getSession());
        CmsAdminSettings.setSettingsDisplayMemEdenValue(getCms(), displayMemEden, getSession());
        CmsAdminSettings.setSettingsDisplayMemSurvivorValue(getCms(), displayMemSurvivor, getSession());
        //CmsAdminSettings.publishSettingsFile(getCms());

        // set the list of errors to display when saving failed
        setCommitErrors(errors);
    }

    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getMemPermMax() {

        return m_memPermMax;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getMemPermTotal() {

        return m_memPermTotal;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getMemPermUsed() {

        return m_memPermUsed;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getMemOldMax() {

        return m_memOldMax;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getMemOldTotal() {

        return m_memOldTotal;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getMemOldUsed() {

        return m_memOldUsed;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getMemEdenMax() {

        return m_memEdenMax;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getMemEdenTotal() {

        return m_memEdenTotal;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getMemEdenUsed() {

        return m_memEdenUsed;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getMemSurvivorMax() {

        return m_memSurvivorMax;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getMemSurvivorTotal() {

        return m_memSurvivorTotal;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getMemSurvivorUsed() {

        return m_memSurvivorUsed;
    }

    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setMemPermMax(String arg) {

        m_memPermMax = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setMemPermTotal(String arg) {

        m_memPermTotal = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setMemPermUsed(String arg) {

        m_memPermUsed = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setMemOldMax(String arg) {

        m_memOldMax = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setMemOldTotal(String arg) {

        m_memOldTotal = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setMemOldUsed(String arg) {

        m_memOldUsed = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setMemEdenMax(String arg) {

        m_memEdenMax = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setMemEdenTotal(String arg) {

        m_memEdenTotal = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setMemEdenUsed(String arg) {

        m_memEdenUsed = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setMemSurvivorMax(String arg) {

        m_memSurvivorMax = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setMemSurvivorTotal(String arg) {

        m_memSurvivorTotal = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setMemSurvivorUsed(String arg) {

        m_memSurvivorUsed = arg;
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
        	
        	//settings
        	result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_BLOCK_SETTINGS)));
            result.append(createWidgetTableStart());
        	result.append(createDialogRowsHtml(0, 0));
        	
        	int lineNumber = 1;
        	
        	String listOfMemories = "";
        	for(java.lang.management.MemoryPoolMXBean item : ManagementFactory.getMemoryPoolMXBeans())  {
                String name = item.getName();
                listOfMemories = listOfMemories + name + ",";
                if(name.toLowerCase().contains("perm")){
                	result.append(createDialogRowsHtml(lineNumber, lineNumber));
                	lineNumber = lineNumber + 1;
                }else if(name.toLowerCase().contains("old")){
                	result.append(createDialogRowsHtml(lineNumber, lineNumber));
                	lineNumber = lineNumber + 1;
                }else if(name.toLowerCase().contains("eden")){
                	result.append(createDialogRowsHtml(lineNumber, lineNumber));
                	lineNumber = lineNumber + 1;
                }else if(name.toLowerCase().contains("survivor")){
                	result.append(createDialogRowsHtml(lineNumber, lineNumber));
                	lineNumber = lineNumber + 1;
                }
        	}
        	LOG.debug("createDialogHtml() :: listOfMemories = "+listOfMemories);
        	
        	result.append(createWidgetTableEnd());
            result.append(dialogBlockEnd());
        	
            // create the widgets for the first dialog page
            for(java.lang.management.MemoryPoolMXBean item : ManagementFactory.getMemoryPoolMXBeans())  {
                String name = item.getName();
                
                if(name.toLowerCase().contains("perm")){
                	if(m_adminSettings.getDisplayMemPerm()){
                		result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_BLOCK_)+name.toUpperCase()));
                        result.append(createWidgetTableStart());
                        result.append(createDialogRowsHtml(lineNumber, lineNumber+2));
                        result.append(createWidgetTableEnd());
                        result.append(dialogBlockEnd());
                        lineNumber = lineNumber + 3;
                	}
                }else if(name.toLowerCase().contains("old")){
                	if(m_adminSettings.getDisplayMemOld()){
                		result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_BLOCK_)+name.toUpperCase()));
                        result.append(createWidgetTableStart());
                        result.append(createDialogRowsHtml(lineNumber, lineNumber+2));
                        result.append(createWidgetTableEnd());
                        result.append(dialogBlockEnd());
                        lineNumber = lineNumber + 3;
                	}
                }else if(name.toLowerCase().contains("eden")){
                	if(m_adminSettings.getDisplayMemEden()){
                		result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_BLOCK_)+name.toUpperCase()));
                        result.append(createWidgetTableStart());
                        result.append(createDialogRowsHtml(lineNumber, lineNumber+2));
                        result.append(createWidgetTableEnd());
                        result.append(dialogBlockEnd());
                        lineNumber = lineNumber + 3;
                	}
                }else if(name.toLowerCase().contains("survivor")){
                	LOG.debug("createDialogHtml() :: m_adminSettings.getDisplayMemSurvivor() = "+m_adminSettings.getDisplayMemSurvivor());
                	if(m_adminSettings.getDisplayMemSurvivor()){
                	//if(CmsAdminSettings.getSettingsDisplayMemSurvivorValue(getCms(), getSession())){
                		result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_BLOCK_)+name.toUpperCase()));
                        result.append(createWidgetTableStart());
                        result.append(createDialogRowsHtml(lineNumber, lineNumber+2));
                        result.append(createWidgetTableEnd());
                        result.append(dialogBlockEnd());
                        lineNumber = lineNumber + 3;
                	}
                }
                
            }
        }

        // close widget table
        result.append(createWidgetTableEnd());
        
        result.append("<script type='text/javascript' src='" + getJsp().link("/system/workplace/resources/jquery/packed/jquery.js") + "'></script>\n");
        result.append("<script type='text/javascript' src='" + getJsp().link("/system/workplace/resources/highcharts/highstock-1.3.0.js") + "'></script>\n");
        result.append("<script type='text/javascript' src='" + getJsp().link("/system/workplace/resources/highcharts/exporting-3.0.0.js") + "'></script>\n");
        result.append("<script type='text/javascript'>\n");
        result.append("$(function() {\n");
        result.append("  $('form#EDITOR').after('");
        result.append("<div class=\"customScripts\">");
        if(m_adminSettings.getDisplayMemPerm()){
        	result.append("<div id=\"perm\" style=\"height: 300px; width: 50%; float: left;\">Loading Mem Perm graph...</div>");
        }
        if(m_adminSettings.getDisplayMemOld()){
        	result.append("<div id=\"old\" style=\"height: 300px; width: 50%; float: left;\">Loading Mem Old graph...</div>");
        }
        if(m_adminSettings.getDisplayMemEden()){
        	result.append("<div id=\"eden\" style=\"height: 300px; width: 50%; float: left;\">Loading Mem Eden graph...</div>");
        }
        if(m_adminSettings.getDisplayMemSurvivor()){
        	result.append("<div id=\"survivor\" style=\"height: 300px; width: 50%; float: left;\">Loading Mem Survivor graph...</div>");
        }
        result.append("</div>");
        result.append("  ');\n");
        result.append("  Highcharts.setOptions({\n");
        result.append("    global : { useUTC : true }\n");
        result.append("  }); \n");
        result.append(getUpdateInfoFunction(
				m_adminSettings.getDisplayMemPerm(),
				m_adminSettings.getDisplayMemOld(),
				m_adminSettings.getDisplayMemEden(),
				m_adminSettings.getDisplayMemSurvivor()
				));
        if(m_adminSettings.getDisplayMemPerm()){
        	result.append(getHighChartPerm());
        }
        if(m_adminSettings.getDisplayMemOld()){
        	result.append(getHighChartOld());
        }
        if(m_adminSettings.getDisplayMemEden()){
        	result.append(getHighChartEden());
        }
        if(m_adminSettings.getDisplayMemSurvivor()){
        	result.append(getHighChartSurvivor());
        }
        result.append("});\n");
        result.append("</script>\n");

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
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "interval", PAGES[0], new CmsDisplayWidget()));
        
        for(java.lang.management.MemoryPoolMXBean item : ManagementFactory.getMemoryPoolMXBeans())  {
            String name = item.getName();
            if(name.toLowerCase().contains("perm")){
            	addWidget(new CmsWidgetDialogParameter(m_adminSettings, "displayMemPerm", PAGES[0], new CmsCheckboxWidget()));
            }else if(name.toLowerCase().contains("old")){
            	addWidget(new CmsWidgetDialogParameter(m_adminSettings, "displayMemOld", PAGES[0], new CmsCheckboxWidget()));
            }else if(name.toLowerCase().contains("eden")){
            	addWidget(new CmsWidgetDialogParameter(m_adminSettings, "displayMemEden", PAGES[0], new CmsCheckboxWidget()));
            }else if(name.toLowerCase().contains("survivor")){
            	addWidget(new CmsWidgetDialogParameter(m_adminSettings, "displayMemSurvivor", PAGES[0], new CmsCheckboxWidget()));
            }
    	}

        // widgets to display
        int lineNumber = 5;
        int countItem = 0;
        for(java.lang.management.MemoryPoolMXBean item : ManagementFactory.getMemoryPoolMXBeans())  {
            String name = item.getName();
            
            if(name.toLowerCase().contains("perm")){
            	if(m_adminSettings.getDisplayMemPerm()){
            		addWidget(new CmsWidgetDialogParameter(this, "memPermMax", PAGES[0], new CmsDisplayWidget()));
            		addWidget(new CmsWidgetDialogParameter(this, "memPermTotal", PAGES[0], new CmsDisplayWidget()));
            		addWidget(new CmsWidgetDialogParameter(this, "memPermUsed", PAGES[0], new CmsDisplayWidget()));
	                lineNumber = lineNumber + 3;
	                countItem++;
            	}
            }else if(name.toLowerCase().contains("old")){
            	if(m_adminSettings.getDisplayMemOld()){
            		addWidget(new CmsWidgetDialogParameter(this, "memOldMax", PAGES[0], new CmsDisplayWidget()));
            		addWidget(new CmsWidgetDialogParameter(this, "memOldTotal", PAGES[0], new CmsDisplayWidget()));
            		addWidget(new CmsWidgetDialogParameter(this, "memOldUsed", PAGES[0], new CmsDisplayWidget()));
	                lineNumber = lineNumber + 3;
	                countItem++;
            	}
            }else if(name.toLowerCase().contains("eden")){
            	if(m_adminSettings.getDisplayMemEden()){
            		addWidget(new CmsWidgetDialogParameter(this, "memEdenMax", PAGES[0], new CmsDisplayWidget()));
            		addWidget(new CmsWidgetDialogParameter(this, "memEdenTotal", PAGES[0], new CmsDisplayWidget()));
            		addWidget(new CmsWidgetDialogParameter(this, "memEdenUsed", PAGES[0], new CmsDisplayWidget()));
	                lineNumber = lineNumber + 3;
	                countItem++;
            	}
            }else if(name.toLowerCase().contains("survivor")){
            	if(m_adminSettings.getDisplayMemSurvivor()){
            	//if(CmsAdminSettings.getSettingsDisplayMemSurvivorValue(getCms(), getSession())){
            		addWidget(new CmsWidgetDialogParameter(this, "memSurvivorMax", PAGES[0], new CmsDisplayWidget()));
            		addWidget(new CmsWidgetDialogParameter(this, "memSurvivorTotal", PAGES[0], new CmsDisplayWidget()));
            		addWidget(new CmsWidgetDialogParameter(this, "memSurvivorUsed", PAGES[0], new CmsDisplayWidget()));
	                lineNumber = lineNumber + 3;
	                countItem++;
            	}
            }else{
            	//LOG.debug("MemoryPoolMXBean name = " + name.toLowerCase());
            }
            
            
        }
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
    	
    	for(java.lang.management.MemoryPoolMXBean item : ManagementFactory.getMemoryPoolMXBeans())  {
        	java.lang.management.MemoryUsage mu = item.getUsage();
            String name = item.getName();
	      
            if(name.toLowerCase().contains("perm")){
            	setMemPermMax(""+mu.getMax());
    	    	setMemPermTotal(""+mu.getCommitted());
    	    	setMemPermUsed(""+mu.getUsed());
            }else if(name.toLowerCase().contains("old")){
            	setMemOldMax(""+mu.getMax());
    	    	setMemOldTotal(""+mu.getCommitted());
    	    	setMemOldUsed(""+mu.getUsed());
            }else if(name.toLowerCase().contains("eden")){
            	setMemEdenMax(""+mu.getMax());
    	    	setMemEdenTotal(""+mu.getCommitted());
    	    	setMemEdenUsed(""+mu.getUsed());
            }else if(name.toLowerCase().contains("survivor")){
            	setMemSurvivorMax(""+mu.getMax());
    	    	setMemSurvivorTotal(""+mu.getCommitted());
    	    	setMemSurvivorUsed(""+mu.getUsed());
            }else{
            	//LOG.debug("MemoryPoolMXBean name = " + name.toLowerCase());
            }
	    }
    	
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
    	
        setParamCloseLink(getJsp().link("/system/workplace/views/admin/admin-main.jsp?path=/eurelis_system_information/memory.jsp"));
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
    
    
    
    
    
    protected StringBuffer getUpdateInfoFunction(boolean displayPerm, boolean displayOld, boolean displayEden, boolean displaySurvivor){
    	
    	StringBuffer result = new StringBuffer(1024);
    	result.append("  function updateInfo() {\n");
        result.append("    $.getJSON('"+jsonPath+"', function(data) {\n");
        result.append("      var time = (new Date()).getTime();\n");
        result.append("      var $system = data.system;\n");
        //graphs
        if(displayPerm){
        	result.append("      /*if(window.chartPerm)*/ window.chartPerm.series[0].addPoint([time, $system.memory.perm.max], true, true, true);\n");
            result.append("      /*if(window.chartPerm)*/ window.chartPerm.series[1].addPoint([time, $system.memory.perm.committed], true, true, true);\n");
            result.append("      /*if(window.chartPerm)*/ window.chartPerm.series[2].addPoint([time, $system.memory.perm.used], true, true, true);\n");
        }
        if(displayOld){
        	result.append("      /*if(window.chartOld)*/ window.chartOld.series[0].addPoint([time, $system.memory.old.max], true, true, true);\n");
            result.append("      /*if(window.chartOld)*/ window.chartOld.series[1].addPoint([time, $system.memory.old.committed], true, true, true);\n");
            result.append("      /*if(window.chartOld)*/ window.chartOld.series[2].addPoint([time, $system.memory.old.used], true, true, true);\n");
        }
        if(displayEden){
        	result.append("      /*if(window.chartEden)*/ window.chartEden.series[0].addPoint([time, $system.memory.eden.max], true, true, true);\n");
            result.append("      /*if(window.chartEden)*/ window.chartEden.series[1].addPoint([time, $system.memory.eden.committed], true, true, true);\n");
            result.append("      /*if(window.chartEden)*/ window.chartEden.series[2].addPoint([time, $system.memory.eden.used], true, true, true);\n");
        }
        if(displaySurvivor){
        	result.append("      /*if(window.chartSurvivor)*/ window.chartSurvivor.series[0].addPoint([time, $system.memory.survivor.max], true, true, true);\n");
            result.append("      /*if(window.chartSurvivor)*/ window.chartSurvivor.series[1].addPoint([time, $system.memory.survivor.committed], true, true, true);\n");
            result.append("      /*if(window.chartSurvivor)*/ window.chartSurvivor.series[2].addPoint([time, $system.memory.survivor.used], true, true, true);\n");
        }	  
        //valeurs
        if(displayPerm){
        	result.append("      var $memPermMaxTag = $('[id^=\"memPermMax\"]');  \n");
        	result.append("      var $memPermTotalTag = $('[id^=\"memPermTotal\"]');  \n");
        	result.append("      var $memPermUsedTag = $('[id^=\"memPermUsed\"]');  \n");
        	result.append("      $memPermMaxTag.val($system.memory.perm.max);$memPermMaxTag.prev().html($system.memory.perm.max); \n");
        	result.append("      $memPermTotalTag.val($system.memory.perm.committed);$memPermTotalTag.prev().html($system.memory.perm.committed); \n");
        	result.append("      $memPermUsedTag.val($system.memory.perm.used);$memPermUsedTag.prev().html($system.memory.perm.used); \n");
        }
        if(displayOld){
        	result.append("      var $memOldMaxTag = $('[id^=\"memOldMax\"]');  \n");
        	result.append("      var $memOldTotalTag = $('[id^=\"memOldTotal\"]');  \n");
        	result.append("      var $memOldUsedTag = $('[id^=\"memOldUsed\"]');  \n");
        	result.append("      $memOldMaxTag.val($system.memory.old.max);$memOldMaxTag.prev().html($system.memory.old.max); \n");
        	result.append("      $memOldTotalTag.val($system.memory.old.committed);$memOldTotalTag.prev().html($system.memory.old.committed); \n");
        	result.append("      $memOldUsedTag.val($system.memory.old.used);$memOldUsedTag.prev().html($system.memory.old.used); \n");
        }
        if(displayEden){
        	result.append("      var $memEdenMaxTag = $('[id^=\"memEdenMax\"]');  \n");
        	result.append("      var $memEdenTotalTag = $('[id^=\"memEdenTotal\"]');  \n");
        	result.append("      var $memEdenUsedTag = $('[id^=\"memEdenUsed\"]');  \n");
        	result.append("      $memEdenMaxTag.val($system.memory.eden.max);$memEdenMaxTag.prev().html($system.memory.eden.max); \n");
        	result.append("      $memEdenTotalTag.val($system.memory.eden.committed);$memEdenTotalTag.prev().html($system.memory.eden.committed); \n");
        	result.append("      $memEdenUsedTag.val($system.memory.eden.used);$memEdenUsedTag.prev().html($system.memory.eden.used); \n");
        }
        if(displaySurvivor){
        	result.append("      var $memSurvivorMaxTag = $('[id^=\"memSurvivorMax\"]');  \n");
        	result.append("      var $memSurvivorTotalTag = $('[id^=\"memSurvivorTotal\"]');  \n");
        	result.append("      var $memSurvivorUsedTag = $('[id^=\"memSurvivorUsed\"]');  \n");
        	result.append("      $memSurvivorMaxTag.val($system.memory.survivor.max);$memSurvivorMaxTag.prev().html($system.memory.survivor.max); \n");
        	result.append("      $memSurvivorTotalTag.val($system.memory.survivor.committed);$memSurvivorTotalTag.prev().html($system.memory.survivor.committed); \n");
        	result.append("      $memSurvivorUsedTag.val($system.memory.survivor.used);$memSurvivorUsedTag.prev().html($system.memory.survivor.used); \n");
        }
        result.append("    });\n");
        result.append("  }\n");
    	return result;
    	
	}
    	
    
    protected StringBuffer getHighChartPerm(){
    	
    	StringBuffer result = new StringBuffer(1024);
    	result.append("  window.chartPerm = new Highcharts.StockChart({\n");
    	result.append("    chart : {\n");
    	result.append("      renderTo : 'perm',\n");
    	result.append("      events : {\n");
    	result.append("        load : function() {\n");
    	result.append("          setInterval(updateInfo, "+frequencyInMillis+");\n");
    	result.append("        }\n");
    	result.append("      }\n");
    	result.append("    },\n");
    	result.append("    credits: false,\n");
    	result.append("    legend: {\n");
    	result.append("      layout: 'vertical',\n");
    	result.append("      enabled: true,\n");
    	result.append("      align: 'right',\n");
    	result.append("      verticalAlign: 'top',\n");
    	result.append("      x: -10,\n");
    	result.append("      y: 100,\n");
    	result.append("      borderWidth: 1\n");
    	result.append("    }, \n");
    	result.append("    tooltip: { \n");
    	result.append("      style: { padding: '10px' }, \n");
    	result.append("      valueDecimals : 2,\n");
    	result.append("      formatter:function() {\n");
    	result.append("        var s = '<b>Time: ' + Highcharts.dateFormat('%Y/%m/%d %H:%M:%S', this.x) + '</b><br/>';\n");
    	result.append("        $.each(this.points, function(i, point) {\n");
    	result.append("          s += '<span style=\"color:'+this.series.color+';font-weight:bold\">'+this.series.name+'</span>:<b>'+Math.round(this.point.y/1024/1024)+' Mb</b><br/>';\n");
    	result.append("        });\n");
    	result.append("        return s;\n");
    	result.append("      }\n");
    	result.append("    },\n");
    	result.append("    rangeSelector: { enabled: false },\n");
    	result.append("    exporting: { enabled: false },\n");
    	result.append("    navigator: { enabled: false },\n");
    	result.append("    title : {\n");
    	result.append("      text : 'Perm Gen'\n");
    	result.append("    },\n");
    	result.append("    series : [ \n");
    	result.append("      {type : 'area', name : 'Max', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}, \n");
    	result.append("      {type : 'area', name : 'Total', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}, \n");
    	result.append("      {type : 'area', name : 'Used', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()} \n");
    	result.append("    ] \n");   
    	result.append("  });\n");
    	return result;
    	
	}

    protected StringBuffer getHighChartOld(){
	
		StringBuffer result = new StringBuffer(1024);
		result.append("  window.chartOld = new Highcharts.StockChart({\n");
		result.append("    chart : {");
		result.append("      renderTo : 'old',");
		result.append("      events : {");
		result.append("        load : function() {");
		result.append("          setInterval(updateInfo, "+frequencyInMillis+");");
		result.append("        }");
		result.append("      }");
		result.append("    },");
		result.append("    credits: false,");
		result.append("    legend: {");
		result.append("      layout: 'vertical',");
		result.append("      enabled: true,");
		result.append("      align: 'right',");
		result.append("      verticalAlign: 'top',");
		result.append("      x: -10,");
		result.append("      y: 100,");
		result.append("      borderWidth: 1");
		result.append("    }, ");
		result.append("    tooltip: { ");
		result.append("      style: { padding: '10px' }, ");
		result.append("      valueDecimals : 2,");
		result.append("      formatter:function() {");
		result.append("        var s = '<b>Time: ' + Highcharts.dateFormat('%Y/%m/%d %H:%M:%S', this.x) + '</b><br/>';");
		result.append("        $.each(this.points, function(i, point) {");
		result.append("          s += '<span style=\"color:'+this.series.color+';font-weight:bold\">'+this.series.name+'</span>:<b>'+Math.round(this.point.y/1024/1024)+' Mb</b><br/>';");
		result.append("        });");
		result.append("        return s;");
		result.append("      },");
		result.append("    },");
		result.append("    rangeSelector: { enabled: false },");
		result.append("    exporting: { enabled: false },");
		result.append("    navigator: { enabled: false },");
		result.append("    title : {");
		result.append("      text : 'Old Gen'");
		result.append("    },");
		result.append("    series : [");
		result.append("      {type : 'area', name : 'Max', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},  ");
		result.append("      {type : 'area', name : 'Total', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
		result.append("      {type : 'area', name : 'Used', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}");
		result.append("    ]       "); 
		result.append("  });\n");
		return result;
	
    }
    
    protected StringBuffer getHighChartEden(){
    	
    	StringBuffer result = new StringBuffer(1024);
    	result.append("  window.chartEden = new Highcharts.StockChart({\n");
    	result.append("    chart : {");
    	result.append("      renderTo : 'eden',");
    	result.append("      events : {");
    	result.append("        load : function() {");
    	result.append("          setInterval(updateInfo, "+frequencyInMillis+");");
    	result.append("        }");
    	result.append("      }");
    	result.append("    },");
    	result.append("    credits: false,");
    	result.append("    legend: {");
    	result.append("      layout: 'vertical',");
    	result.append("      enabled: true,");
    	result.append("      align: 'right',");
    	result.append("      verticalAlign: 'top',");
    	result.append("      x: -10,");
    	result.append("      y: 100,");
    	result.append("      borderWidth: 1");
    	result.append("    }, ");
    	result.append("    tooltip: { ");
    	result.append("      style: { padding: '10px' }, ");
    	result.append("      valueDecimals : 2,");
    	result.append("      formatter:function() {");
    	result.append("        var s = '<b>Time: ' + Highcharts.dateFormat('%Y/%m/%d %H:%M:%S', this.x) + '</b><br/>';");
    	result.append("        $.each(this.points, function(i, point) {");
    	result.append("          s += '<span style=\"color:'+this.series.color+';font-weight:bold\">'+this.series.name+'</span>:<b>'+Math.round(this.point.y/1024/1024)+' Mb</b><br/>';");
    	result.append("        });");
    	result.append("        return s;");
    	result.append("      },");
    	result.append("    },");
    	result.append("    rangeSelector: { enabled: false },");
    	result.append("    exporting: { enabled: false },");
    	result.append("    navigator: { enabled: false },");
    	result.append("    title : {");
    	result.append("      text : 'Eden Space'");
    	result.append("    },");
    	result.append("    series : [");
    	result.append("      {type : 'area', name : 'Max', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}, "); 
    	result.append("      {type : 'area', name : 'Total', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
    	result.append("      {type : 'area', name : 'Used', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}");
    	result.append("    ]  ");
    	result.append("  });\n");
    	return result;
    	
	}
    
    
    protected StringBuffer getHighChartSurvivor(){
    	
    	StringBuffer result = new StringBuffer(1024);
    	result.append("  window.chartSurvivor = new Highcharts.StockChart({\n");
    	result.append("    chart : {");
    	result.append("      renderTo : 'survivor',");
    	result.append("      events : {");
    	result.append("      load : function() {");
    	result.append("          setInterval(updateInfo, "+frequencyInMillis+");");
    	result.append("        }");
    	result.append("      }");
    	result.append("    },");
    	result.append("    credits: false,");
    	result.append("    legend: {");
    	result.append("      layout: 'vertical',");
    	result.append("      enabled: true,");
    	result.append("      align: 'right',");
    	result.append("      verticalAlign: 'top',");
    	result.append("      x: -10,");
    	result.append("      y: 100,");
    	result.append("      borderWidth: 1");
    	result.append("    }, ");
    	result.append("    tooltip: { ");
    	result.append("      style: { padding: '10px' }, ");
    	result.append("      valueDecimals : 2,");
    	result.append("      formatter:function() {");
    	result.append("        var s = '<b>Time: ' + Highcharts.dateFormat('%Y/%m/%d %H:%M:%S', this.x) + '</b><br/>';");
    	result.append("        $.each(this.points, function(i, point) {");
    	result.append("          s += '<span style=\"color:'+this.series.color+';font-weight:bold\">'+this.series.name+'</span>:<b>'+Math.round(this.point.y/1024/1024)+' Mb</b><br/>';");
    	result.append("        });");
    	result.append("        return s;");
    	result.append("      },");
    	result.append("    },");
    	result.append("    rangeSelector: { enabled: false },");
    	result.append("    exporting: { enabled: false },");
    	result.append("    navigator: { enabled: false },");
    	result.append("    title : {");
    	result.append("      text : 'Survivor'");
    	result.append("    },");
    	result.append("    series : [");
    	result.append("      {type : 'area', name : 'Current usage', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}, "); 
    	result.append("      {type : 'line', name : 'Active connections', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
    	result.append("      {type : 'line', name : 'Idle connections', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}");
    	result.append("    ]  ");
    	result.append("  });\n");
    	return result;
    	
    }
    
}

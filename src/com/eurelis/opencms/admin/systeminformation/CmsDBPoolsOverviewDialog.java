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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.db.CmsDbException;
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
public class CmsDBPoolsOverviewDialog extends CmsWidgetDialog {
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsDBPoolsOverviewDialog.class);

    /** localized messages Keys prefix. */
    public static final String KEY_PREFIX = "dbpools.stats";

    /** Defines which pages are valid for this dialog. */
    public static final String[] PAGES = {"page1"};

    /** System infos . */
    private String m_poolName1;
    /** System infos . */
    private String m_poolUrl1;
    /** System infos . */
    private String m_poolStrategy1;
    /** System infos . */
    private String m_poolMaxPoolSize1;
    /** System infos . */
    private String m_activeConnections1;
    /** System infos . */
    private String m_idleConnections1;
    /** System infos . */
    private String m_currentUsage1;
    
    /** System infos . */
    private String m_poolName2;
    /** System infos . */
    private String m_poolUrl2;
    /** System infos . */
    private String m_poolStrategy2;
    /** System infos . */
    private String m_poolMaxPoolSize2;
    /** System infos . */
    private String m_activeConnections2;
    /** System infos . */
    private String m_idleConnections2;
    /** System infos . */
    private String m_currentUsage2;
    
    /** System infos . */
    private String m_poolName3;
    /** System infos . */
    private String m_poolUrl3;
    /** System infos . */
    private String m_poolStrategy3;
    /** System infos . */
    private String m_poolMaxPoolSize3;
    /** System infos . */
    private String m_activeConnections3;
    /** System infos . */
    private String m_idleConnections3;
    /** System infos . */
    private String m_currentUsage3;
    
    /** System infos . */
    private String m_poolName4;
    /** System infos . */
    private String m_poolUrl4;
    /** System infos . */
    private String m_poolStrategy4;
    /** System infos . */
    private String m_poolMaxPoolSize4;
    /** System infos . */
    private String m_activeConnections4;
    /** System infos . */
    private String m_idleConnections4;
    /** System infos . */
    private String m_currentUsage4;
    
    /** System infos . */
    private String m_poolName5;
    /** System infos . */
    private String m_poolUrl5;
    /** System infos . */
    private String m_poolStrategy5;
    /** System infos . */
    private String m_poolMaxPoolSize5;
    /** System infos . */
    private String m_activeConnections5;
    /** System infos . */
    private String m_idleConnections5;
    /** System infos . */
    private String m_currentUsage5;
    
    
    
    private Map m_configParameter = null;
    private List m_poolsName = null;
    
    /** The admin settings object that is edited on this dialog. */
    protected CmsAdminSettings m_adminSettings;
    
    private int frequencyInMillis = CmsAdminSettings.getSettingsIntervalValue(getCms(), getSession());
    private String jsonPath = getJsp().link("/system/workplace/admin/eurelis_system_information/json/getSystemInfo.json");


    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsDBPoolsOverviewDialog(CmsJspActionElement jsp) {

        super(jsp);

    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsDBPoolsOverviewDialog(PageContext context, HttpServletRequest req, HttpServletResponse res) {

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
        
        boolean displayPool1 = m_adminSettings.getDisplayPool1();
        LOG.debug("Admin settings actionCommit : m_adminSettings.getDisplayPool1() = " + displayPool1);
        
        boolean displayPool2 = m_adminSettings.getDisplayPool2();
        LOG.debug("Admin settings actionCommit : m_adminSettings.getDisplayPool2() = " + displayPool2);
        
        boolean displayPool3 = m_adminSettings.getDisplayPool3();
        LOG.debug("Admin settings actionCommit : m_adminSettings.getDisplayPool3() = " + displayPool3);
        
        boolean displayPool4 = m_adminSettings.getDisplayPool4();
        LOG.debug("Admin settings actionCommit : m_adminSettings.getDisplayPool4() = " + displayPool4);
        
        boolean displayPool5 = m_adminSettings.getDisplayPool5();
        LOG.debug("Admin settings actionCommit : m_adminSettings.getDisplayPool5() = " + displayPool5);

        //memorisation system du parametre...
        CmsAdminSettings.setSettingsIntervalValue(getCms(), interval, getSession());
        CmsAdminSettings.setSettingsDisplayPool1Value(getCms(), displayPool1, getSession());
        CmsAdminSettings.setSettingsDisplayPool2Value(getCms(), displayPool2, getSession());
        CmsAdminSettings.setSettingsDisplayPool3Value(getCms(), displayPool3, getSession());
        CmsAdminSettings.setSettingsDisplayPool4Value(getCms(), displayPool4, getSession());
        CmsAdminSettings.setSettingsDisplayPool5Value(getCms(), displayPool5, getSession());
        //CmsAdminSettings.publishSettingsFile(getCms());

        // set the list of errors to display when saving failed
        setCommitErrors(errors);
    }

    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolName1() {

        return m_poolName1;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolUrl1() {

        return m_poolUrl1;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolStrategy1() {

        return m_poolStrategy1;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolMaxPoolSize1() {

        return m_poolMaxPoolSize1;
    }
    
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getActiveConnections1() {

        return m_activeConnections1;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getIdleConnections1() {

        return m_idleConnections1;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getCurrentUsage1() {

        return m_currentUsage1;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolName2() {

        return m_poolName2;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolUrl2() {

        return m_poolUrl2;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolStrategy2() {

        return m_poolStrategy2;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolMaxPoolSize2() {

        return m_poolMaxPoolSize2;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getActiveConnections2() {

        return m_activeConnections2;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getIdleConnections2() {

        return m_idleConnections2;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getCurrentUsage2() {

        return m_currentUsage2;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolName3() {

        return m_poolName3;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolUrl3() {

        return m_poolUrl3;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolStrategy3() {

        return m_poolStrategy3;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolMaxPoolSize3() {

        return m_poolMaxPoolSize3;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getActiveConnections3() {

        return m_activeConnections3;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getIdleConnections3() {

        return m_idleConnections3;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getCurrentUsage3() {

        return m_currentUsage3;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolName4() {

        return m_poolName4;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolUrl4() {

        return m_poolUrl4;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolStrategy4() {

        return m_poolStrategy4;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolMaxPoolSize4() {

        return m_poolMaxPoolSize4;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getActiveConnections4() {

        return m_activeConnections4;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getIdleConnections4() {

        return m_idleConnections4;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getCurrentUsage4() {

        return m_currentUsage4;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolName5() {

        return m_poolName5;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolUrl5() {

        return m_poolUrl5;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolStrategy5() {

        return m_poolStrategy5;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getPoolMaxPoolSize5() {

        return m_poolMaxPoolSize5;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getActiveConnections5() {

        return m_activeConnections5;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getIdleConnections5() {

        return m_idleConnections5;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getCurrentUsage5() {

        return m_currentUsage5;
    }

    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolName1(String arg) {

        m_poolName1 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolUrl1(String arg) {

        m_poolUrl1 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolStrategy1(String arg) {

        m_poolStrategy1 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolMaxPoolSize1(String arg) {

        m_poolMaxPoolSize1 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setActiveConnections1(String arg) {

        m_activeConnections1 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setIdleConnections1(String arg) {

        m_idleConnections1 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setCurrentUsage1(String arg) {

        m_currentUsage1 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolName2(String arg) {

        m_poolName2 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolUrl2(String arg) {

        m_poolUrl2 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolStrategy2(String arg) {

        m_poolStrategy2 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolMaxPoolSize2(String arg) {

        m_poolMaxPoolSize2 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setActiveConnections2(String arg) {

        m_activeConnections2 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setIdleConnections2(String arg) {

        m_idleConnections2 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setCurrentUsage2(String arg) {

        m_currentUsage2 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolName3(String arg) {

        m_poolName3 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolUrl3(String arg) {

        m_poolUrl3 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolStrategy3(String arg) {

        m_poolStrategy3 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolMaxPoolSize3(String arg) {

        m_poolMaxPoolSize3 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setActiveConnections3(String arg) {

        m_activeConnections3 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setIdleConnections3(String arg) {

        m_idleConnections3 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setCurrentUsage3(String arg) {

        m_currentUsage3 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolName4(String arg) {

        m_poolName4 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolUrl4(String arg) {

        m_poolUrl4 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolStrategy4(String arg) {

        m_poolStrategy4 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolMaxPoolSize4(String arg) {

        m_poolMaxPoolSize4 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setActiveConnections4(String arg) {

        m_activeConnections4 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setIdleConnections4(String arg) {

        m_idleConnections4 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setCurrentUsage4(String arg) {

        m_currentUsage4 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolName5(String arg) {

        m_poolName5 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolUrl5(String arg) {

        m_poolUrl5 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolStrategy5(String arg) {

        m_poolStrategy5 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setPoolMaxPoolSize5(String arg) {

        m_poolMaxPoolSize5 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setActiveConnections5(String arg) {

        m_activeConnections5 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setIdleConnections5(String arg) {

        m_idleConnections5 = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setCurrentUsage5(String arg) {

        m_currentUsage5 = arg;
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

        if(m_poolsName==null || m_configParameter==null){
        	initPools();
        }
        
        LOG.debug("createDialogHtml... pools count = " + m_poolsName.size());
        if(m_poolsName.size() > 5){
        	LOG.error("There are more DBPools configured than available in this tool. Only 5 out of " + m_poolsName.size() + " will be displayed.");
        }
        
        if (dialog.equals(PAGES[0])) {
        	
        	if(m_poolsName!=null && m_configParameter!=null){
        		
	        	//settings
	        	result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_DBPOOLS_ADMIN_TOOL_BLOCK_SETTINGS)));
	            result.append(createWidgetTableStart());
	            if(m_poolsName.size() > 5){
	            	result.append(createDialogRowsHtml(0, 5));
	            }else{
	            	result.append(createDialogRowsHtml(0, m_poolsName.size()));
	            }
	        	result.append(createWidgetTableEnd());
	            result.append(dialogBlockEnd());
	        	
	            // create the widgets for the first dialog page
	            int lineNumber = m_poolsName.size() + 1;
	            int poolNumber = 1;
	            Iterator it = m_poolsName.iterator();
	            while(it.hasNext()){
	            	String poolName = (String)it.next();
	                    
	            	if(poolNumber<=5){
	            		if(poolNumber==1 && m_adminSettings.getDisplayPool1()){
	            			result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_DBPOOLS_ADMIN_TOOL_BLOCK_)+poolName));
			                result.append(createWidgetTableStart());
			                result.append(createDialogRowsHtml(lineNumber, lineNumber+5));
			                result.append(createWidgetTableEnd());
			                result.append(dialogBlockEnd());
			                    
			                lineNumber = lineNumber + 6;
	            		}else if(poolNumber==2 && m_adminSettings.getDisplayPool2()){
	            			result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_DBPOOLS_ADMIN_TOOL_BLOCK_)+poolName));
			                result.append(createWidgetTableStart());
			                result.append(createDialogRowsHtml(lineNumber, lineNumber+5));
			                result.append(createWidgetTableEnd());
			                result.append(dialogBlockEnd());
			                    
			                lineNumber = lineNumber + 6;
	            		}else if(poolNumber==3 && m_adminSettings.getDisplayPool3()){
	            			result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_DBPOOLS_ADMIN_TOOL_BLOCK_)+poolName));
			                result.append(createWidgetTableStart());
			                result.append(createDialogRowsHtml(lineNumber, lineNumber+5));
			                result.append(createWidgetTableEnd());
			                result.append(dialogBlockEnd());
			                    
			                lineNumber = lineNumber + 6;
	            		}else if(poolNumber==4 && m_adminSettings.getDisplayPool4()){
	            			result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_DBPOOLS_ADMIN_TOOL_BLOCK_)+poolName));
			                result.append(createWidgetTableStart());
			                result.append(createDialogRowsHtml(lineNumber, lineNumber+5));
			                result.append(createWidgetTableEnd());
			                result.append(dialogBlockEnd());
			                    
			                lineNumber = lineNumber + 6;
	            		}else if(poolNumber==5 && m_adminSettings.getDisplayPool5()){
	            			result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_DBPOOLS_ADMIN_TOOL_BLOCK_)+poolName));
			                result.append(createWidgetTableStart());
			                result.append(createDialogRowsHtml(lineNumber, lineNumber+5));
			                result.append(createWidgetTableEnd());
			                result.append(dialogBlockEnd());
			                    
			                lineNumber = lineNumber + 6;
	            		}
	            	}
	                
	                poolNumber++;
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
        int poolNumber = 1;
        if(m_poolsName!=null && m_configParameter!=null){
	        Iterator it = m_poolsName.iterator();
	        while(it.hasNext()){
	            String poolName = (String)it.next();
	            if(poolNumber<=5){
			        if(poolNumber==1 && m_adminSettings.getDisplayPool1()){
			        	result.append("<div id=\""+poolName+"\" style=\"height: 300px; width: 50%; float: left;\">Loading pool 1 graph...</div>");
			        }else if(poolNumber==2 && m_adminSettings.getDisplayPool2()){
			        	result.append("<div id=\""+poolName+"\" style=\"height: 300px; width: 50%; float: left;\">Loading pool 2 graph...</div>");
			        }else if(poolNumber==3 && m_adminSettings.getDisplayPool3()){
			        	result.append("<div id=\""+poolName+"\" style=\"height: 300px; width: 50%; float: left;\">Loading pool 3 graph...</div>");
			        }else if(poolNumber==4 && m_adminSettings.getDisplayPool4()){
			        	result.append("<div id=\""+poolName+"\" style=\"height: 300px; width: 50%; float: left;\">Loading pool 4 graph...</div>");
			        }else if(poolNumber==5 && m_adminSettings.getDisplayPool5()){
			        	result.append("<div id=\""+poolName+"\" style=\"height: 300px; width: 50%; float: left;\">Loading pool 5 graph...</div>");
			        }
	            }
	            poolNumber++;
	        }
        }
        result.append("</div>");
        result.append("  ');\n");
        result.append("  Highcharts.setOptions({\n");
        result.append("    global : { useUTC : true }\n");
        result.append("  }); \n");
        result.append(getUpdateInfoFunction(
				m_adminSettings.getDisplayPool1(),
				m_adminSettings.getDisplayPool2(),
				m_adminSettings.getDisplayPool3(),
				m_adminSettings.getDisplayPool4(),
				m_adminSettings.getDisplayPool5()
				));
        poolNumber = 1;
        if(m_poolsName!=null && m_configParameter!=null){
	        Iterator it = m_poolsName.iterator();
	        while(it.hasNext()){
	            String poolName = (String)it.next();
	            if(poolNumber<=5){
			        if(poolNumber==1 && m_adminSettings.getDisplayPool1()){
			        	result.append(getHighChartPool(poolName));
			        }else if(poolNumber==2 && m_adminSettings.getDisplayPool2()){
			        	result.append(getHighChartPool(poolName));
			        }else if(poolNumber==3 && m_adminSettings.getDisplayPool3()){
			        	result.append(getHighChartPool(poolName));
			        }else if(poolNumber==4 && m_adminSettings.getDisplayPool4()){
			        	result.append(getHighChartPool(poolName));
			        }else if(poolNumber==5 && m_adminSettings.getDisplayPool5()){
			        	result.append(getHighChartPool(poolName));
			        }
	            }
	            poolNumber++;
	        }
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
        
        org.opencms.db.CmsSqlManager sqlM = org.opencms.main.OpenCms.getSqlManager() ;

        if(m_poolsName==null || m_configParameter==null){
        	initPools();
        }
        if(sqlM==null){
        	LOG.error("CmsSqlManager null");
        }
        
        // widgets to display
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "interval", PAGES[0], new CmsDisplayWidget()));
        int lineNumber = 1;
        if(m_poolsName.size() >= 1){
        	addWidget(new CmsWidgetDialogParameter(m_adminSettings, "displayPool1", PAGES[0], new CmsCheckboxWidget()));
        	lineNumber = 2;
        }
        if(m_poolsName.size() >= 2){
        	addWidget(new CmsWidgetDialogParameter(m_adminSettings, "displayPool2", PAGES[0], new CmsCheckboxWidget()));
        	lineNumber = 3;
        }
        if(m_poolsName.size() >= 3){
        	addWidget(new CmsWidgetDialogParameter(m_adminSettings, "displayPool3", PAGES[0], new CmsCheckboxWidget()));
        	lineNumber = 4;
        }
        if(m_poolsName.size() >= 4){
        	addWidget(new CmsWidgetDialogParameter(m_adminSettings, "displayPool4", PAGES[0], new CmsCheckboxWidget()));
        	lineNumber = 5;
        }
        if(m_poolsName.size() >= 5){
        	addWidget(new CmsWidgetDialogParameter(m_adminSettings, "displayPool5", PAGES[0], new CmsCheckboxWidget()));
        	lineNumber = 6;
        }
        
        
        
        if(m_poolsName!=null && m_configParameter!=null && sqlM!=null){
        	int poolNumber = 1;
            Iterator it = m_poolsName.iterator();
            while(it.hasNext()){
                String poolName = (String)it.next();
                if(poolNumber<=5){
			        if(poolNumber==1 && m_adminSettings.getDisplayPool1()){
			        	
			        	/*String poolUrl = getPoolUrl(poolName);
			        	int activeConnections = getActiveConnections(poolUrl);
                  	  	int idleConnections = getIdleConnections(poolUrl);
                  	  	String poolStrategyProperty = getStrategy(poolName);
                  	  	String maxActivesConfiguratedString = getMaxActive(poolName);
                  	  	float pourcentage = (activeConnections * 100f)/(1f* (new Integer(maxActivesConfiguratedString)));*/
			        	
			        	addWidget(new CmsWidgetDialogParameter(this, "poolUrl1", PAGES[0], new CmsDisplayWidget()));
			        	addWidget(new CmsWidgetDialogParameter(this, "poolStrategy1", PAGES[0], new CmsDisplayWidget()));
			        	addWidget(new CmsWidgetDialogParameter(this, "poolMaxPoolSize1", PAGES[0], new CmsDisplayWidget()));
                  	  	
                  	  	addWidget(new CmsWidgetDialogParameter(this, "activeConnections1", PAGES[0], new CmsDisplayWidget()));
                  	  	addWidget(new CmsWidgetDialogParameter(this, "idleConnections1", PAGES[0], new CmsDisplayWidget()));
                  	  	addWidget(new CmsWidgetDialogParameter(this, "currentUsage1", PAGES[0], new CmsDisplayWidget()));
                  	  	//addWidget(new CmsWidgetDialogParameter(this, "strategy1", PAGES[0], new CmsDisplayWidget()));
                  	  	//addWidget(new CmsWidgetDialogParameter(this, "maxActive1", PAGES[0], new CmsDisplayWidget()));
                  	  	lineNumber = lineNumber + 3;
			        	
			        }else if(poolNumber==2 && m_adminSettings.getDisplayPool2()){
			        	
			        	/*String poolUrl = getPoolUrl(poolName);
			        	int activeConnections = getActiveConnections(poolUrl);
                  	  	int idleConnections = getIdleConnections(poolUrl);
                  	  	String poolStrategyProperty = getStrategy(poolName);
                  	  	String maxActivesConfiguratedString = getMaxActive(poolName);
                  	  	float pourcentage = (activeConnections * 100f)/(1f* (new Integer(maxActivesConfiguratedString)));*/
			        	
			        	addWidget(new CmsWidgetDialogParameter(this, "poolUrl2", PAGES[0], new CmsDisplayWidget()));
			        	addWidget(new CmsWidgetDialogParameter(this, "poolStrategy2", PAGES[0], new CmsDisplayWidget()));
			        	addWidget(new CmsWidgetDialogParameter(this, "poolMaxPoolSize2", PAGES[0], new CmsDisplayWidget()));
                  	  	
                  	  	addWidget(new CmsWidgetDialogParameter(this, "activeConnections2", PAGES[0], new CmsDisplayWidget()));
                  	  	addWidget(new CmsWidgetDialogParameter(this, "idleConnections2", PAGES[0], new CmsDisplayWidget()));
                  	  	addWidget(new CmsWidgetDialogParameter(this, "currentUsage2", PAGES[0], new CmsDisplayWidget()));
                  	  	//addWidget(new CmsWidgetDialogParameter(this, "strategy2", PAGES[0], new CmsDisplayWidget()));
                  	  	//addWidget(new CmsWidgetDialogParameter(this, "maxActive2", PAGES[0], new CmsDisplayWidget()));
                  	  	lineNumber = lineNumber + 3;
			        	
			        }else if(poolNumber==3 && m_adminSettings.getDisplayPool3()){
			        	
			        	/*String poolUrl = getPoolUrl(poolName);
			        	int activeConnections = getActiveConnections(poolUrl);
                  	  	int idleConnections = getIdleConnections(poolUrl);
                  	  	String poolStrategyProperty = getStrategy(poolName);
                  	  	String maxActivesConfiguratedString = getMaxActive(poolName);
                  	  	float pourcentage = (activeConnections * 100f)/(1f* (new Integer(maxActivesConfiguratedString)));*/
			        	
			        	addWidget(new CmsWidgetDialogParameter(this, "poolUrl3", PAGES[0], new CmsDisplayWidget()));
			        	addWidget(new CmsWidgetDialogParameter(this, "poolStrategy3", PAGES[0], new CmsDisplayWidget()));
			        	addWidget(new CmsWidgetDialogParameter(this, "poolMaxPoolSize3", PAGES[0], new CmsDisplayWidget()));
                  	  	
                  	  	addWidget(new CmsWidgetDialogParameter(this, "activeConnections3", PAGES[0], new CmsDisplayWidget()));
                  	  	addWidget(new CmsWidgetDialogParameter(this, "idleConnections3", PAGES[0], new CmsDisplayWidget()));
                  	  	addWidget(new CmsWidgetDialogParameter(this, "currentUsage3", PAGES[0], new CmsDisplayWidget()));
                  	  	//addWidget(new CmsWidgetDialogParameter(this, "strategy3", PAGES[0], new CmsDisplayWidget()));
                  	  	//addWidget(new CmsWidgetDialogParameter(this, "maxActive3", PAGES[0], new CmsDisplayWidget()));
                  	  	lineNumber = lineNumber + 3;
			        	
			        }else if(poolNumber==4 && m_adminSettings.getDisplayPool4()){
			        	
			        	/*String poolUrl = getPoolUrl(poolName);
			        	int activeConnections = getActiveConnections(poolUrl);
                  	  	int idleConnections = getIdleConnections(poolUrl);
                  	  	String poolStrategyProperty = getStrategy(poolName);
                  	  	String maxActivesConfiguratedString = getMaxActive(poolName);
                  	  	float pourcentage = (activeConnections * 100f)/(1f* (new Integer(maxActivesConfiguratedString)));*/
                  	  	
			        	addWidget(new CmsWidgetDialogParameter(this, "poolUrl4", PAGES[0], new CmsDisplayWidget()));
			        	addWidget(new CmsWidgetDialogParameter(this, "poolStrategy4", PAGES[0], new CmsDisplayWidget()));
			        	addWidget(new CmsWidgetDialogParameter(this, "poolMaxPoolSize4", PAGES[0], new CmsDisplayWidget()));
			        	
                  	  	addWidget(new CmsWidgetDialogParameter(this, "activeConnections4", PAGES[0], new CmsDisplayWidget()));
                  	  	addWidget(new CmsWidgetDialogParameter(this, "idleConnections4", PAGES[0], new CmsDisplayWidget()));
                  	  	addWidget(new CmsWidgetDialogParameter(this, "currentUsage4", PAGES[0], new CmsDisplayWidget()));
                  	  	//addWidget(new CmsWidgetDialogParameter(this, "strategy4", PAGES[0], new CmsDisplayWidget()));
                  	  	//addWidget(new CmsWidgetDialogParameter(this, "maxActive4", PAGES[0], new CmsDisplayWidget()));
                  	  	lineNumber = lineNumber + 3;
			        	
			        }else if(poolNumber==5 && m_adminSettings.getDisplayPool5()){
			        	
			        	/*String poolUrl = getPoolUrl(poolName);
			        	int activeConnections = getActiveConnections(poolUrl);
                  	  	int idleConnections = getIdleConnections(poolUrl);
                  	  	String poolStrategyProperty = getStrategy(poolName);
                  	  	String maxActivesConfiguratedString = getMaxActive(poolName);
                  	  	float pourcentage = (activeConnections * 100f)/(1f* (new Integer(maxActivesConfiguratedString)));*/
			        	
			        	addWidget(new CmsWidgetDialogParameter(this, "poolUrl5", PAGES[0], new CmsDisplayWidget()));
			        	addWidget(new CmsWidgetDialogParameter(this, "poolStrategy5", PAGES[0], new CmsDisplayWidget()));
			        	addWidget(new CmsWidgetDialogParameter(this, "poolMaxPoolSize5", PAGES[0], new CmsDisplayWidget()));
                  	  	
                  	  	addWidget(new CmsWidgetDialogParameter(this, "activeConnections5", PAGES[0], new CmsDisplayWidget()));
                  	  	addWidget(new CmsWidgetDialogParameter(this, "idleConnections5", PAGES[0], new CmsDisplayWidget()));
                  	  	addWidget(new CmsWidgetDialogParameter(this, "currentUsage5", PAGES[0], new CmsDisplayWidget()));
                  	  	//addWidget(new CmsWidgetDialogParameter(this, "strategy5", PAGES[0], new CmsDisplayWidget()));
                  	  	//addWidget(new CmsWidgetDialogParameter(this, "maxActive5", PAGES[0], new CmsDisplayWidget()));
                  	  	lineNumber = lineNumber + 3;
			        	
			        }
                }                
                poolNumber++;
                
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
    	
    	
    	initPools();
    	
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
        
        setParamCloseLink(getJsp().link("/system/workplace/views/admin/admin-main.jsp?path=/eurelis_system_information/database_pools.jsp"));
    	
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
    
    
    
    
    
    protected StringBuffer getUpdateInfoFunction(boolean display1, boolean display2, boolean display3, boolean display4, boolean display5){
    	
    	StringBuffer result = new StringBuffer(1024);
    	result.append("  function updateInfo() {\n");
        result.append("    $.getJSON('"+jsonPath+"', function(data) {\n");
        result.append("      var time = (new Date()).getTime();\n");
        result.append("      var $system = data.system;\n");
        
        int poolNumber = 1;
        Iterator poolIterator =  m_poolsName.iterator();
        while(poolIterator.hasNext()){
        	String poolName = (String)poolIterator.next();
        	if(poolName != null){
        		if(poolNumber<=5){
        			//graphs
        			if(poolNumber==1 && display1){
        				result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[0].addPoint([time, $system.dbpools."+poolName+".activeConnections], true, true, true);\n");
        	        	result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[1].addPoint([time, $system.dbpools."+poolName+".idleConnections], true, true, true);\n");
        	        	result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[2].addPoint([time, $system.dbpools."+poolName+".pourcentage], true, true, true);\n");
			        }else if(poolNumber==2 && display2){
        				result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[0].addPoint([time, $system.dbpools."+poolName+".activeConnections], true, true, true);\n");
        	        	result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[1].addPoint([time, $system.dbpools."+poolName+".idleConnections], true, true, true);\n");
        	        	result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[2].addPoint([time, $system.dbpools."+poolName+".pourcentage], true, true, true);\n");
			        }else if(poolNumber==3 && display3){
        				result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[0].addPoint([time, $system.dbpools."+poolName+".activeConnections], true, true, true);\n");
        	        	result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[1].addPoint([time, $system.dbpools."+poolName+".idleConnections], true, true, true);\n");
        	        	result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[2].addPoint([time, $system.dbpools."+poolName+".pourcentage], true, true, true);\n");
			        }else if(poolNumber==4 && display4){
        				result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[0].addPoint([time, $system.dbpools."+poolName+".activeConnections], true, true, true);\n");
        	        	result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[1].addPoint([time, $system.dbpools."+poolName+".idleConnections], true, true, true);\n");
        	        	result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[2].addPoint([time, $system.dbpools."+poolName+".pourcentage], true, true, true);\n");
			        }else if(poolNumber==5 && display5){
        				result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[0].addPoint([time, $system.dbpools."+poolName+".activeConnections], true, true, true);\n");
        	        	result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[1].addPoint([time, $system.dbpools."+poolName+".idleConnections], true, true, true);\n");
        	        	result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[2].addPoint([time, $system.dbpools."+poolName+".pourcentage], true, true, true);\n");
			        }
        			//valeurs
        	        if(poolNumber==1 && display1){
        	        	result.append("      var $activeConnections1Tag = $('[id^=\"activeConnections1\"]'); \n");
        	        	result.append("      var $idleConnections1Tag = $('[id^=\"idleConnections1\"]'); \n");
        	        	result.append("      var $currentUsage1Tag = $('[id^=\"currentUsage1\"]'); \n");
        	        	result.append("      $activeConnections1Tag.val($system.dbpools."+poolName+".activeConnections);$activeConnections1Tag.prev().html($system.dbpools."+poolName+".activeConnections); \n");
        	        	result.append("      $idleConnections1Tag.val($system.dbpools."+poolName+".idleConnections);$idleConnections1Tag.prev().html($system.dbpools."+poolName+".idleConnections); \n");
        	        	result.append("      $currentUsage1Tag.val($system.dbpools."+poolName+".pourcentage + '%');$currentUsage1Tag.prev().html($system.dbpools."+poolName+".pourcentage + '%'); \n");
        	        }
        	        if(poolNumber==2 && display2){
        	        	result.append("      var $activeConnections2Tag = $('[id^=\"activeConnections2\"]'); \n");
        	        	result.append("      var $idleConnections2Tag = $('[id^=\"idleConnections2\"]'); \n");
        	        	result.append("      var $currentUsage2Tag = $('[id^=\"currentUsage2\"]'); \n");
        	        	result.append("      $activeConnections2Tag.val($system.dbpools."+poolName+".activeConnections);$activeConnections2Tag.prev().html($system.dbpools."+poolName+".activeConnections); \n");
        	        	result.append("      $idleConnections2Tag.val($system.dbpools."+poolName+".idleConnections);$idleConnections2Tag.prev().html($system.dbpools."+poolName+".idleConnections); \n");
        	        	result.append("      $currentUsage2Tag.val($system.dbpools."+poolName+".pourcentage + '%');$currentUsage2Tag.prev().html($system.dbpools."+poolName+".pourcentage + '%'); \n");
        	        }
        	        if(poolNumber==3 && display3){
        	        	result.append("      var $activeConnections3Tag = $('[id^=\"activeConnections3\"]'); \n");
        	        	result.append("      var $idleConnections3Tag = $('[id^=\"idleConnections3\"]'); \n");
        	        	result.append("      var $currentUsage3Tag = $('[id^=\"currentUsage3\"]'); \n");
        	        	result.append("      $activeConnections3Tag.val($system.dbpools."+poolName+".activeConnections);$activeConnections3Tag.prev().html($system.dbpools."+poolName+".activeConnections); \n");
        	        	result.append("      $idleConnections3Tag.val($system.dbpools."+poolName+".idleConnections);$idleConnections3Tag.prev().html($system.dbpools."+poolName+".idleConnections); \n");
        	        	result.append("      $currentUsage3Tag.val($system.dbpools."+poolName+".pourcentage + '%');$currentUsage3Tag.prev().html($system.dbpools."+poolName+".pourcentage + '%'); \n");
        	        }
        	        if(poolNumber==4 && display4){
        	        	result.append("      var $activeConnections4Tag = $('[id^=\"activeConnections4\"]'); \n");
        	        	result.append("      var $idleConnections4Tag = $('[id^=\"idleConnections4\"]'); \n");
        	        	result.append("      var $currentUsage4Tag = $('[id^=\"currentUsage4\"]'); \n");
        	        	result.append("      $activeConnections4Tag.val($system.dbpools."+poolName+".activeConnections);$activeConnections4Tag.prev().html($system.dbpools."+poolName+".activeConnections); \n");
        	        	result.append("      $idleConnections4Tag.val($system.dbpools."+poolName+".idleConnections);$idleConnections4Tag.prev().html($system.dbpools."+poolName+".idleConnections); \n");
        	        	result.append("      $currentUsage4Tag.val($system.dbpools."+poolName+".pourcentage + '%');$currentUsage4Tag.prev().html($system.dbpools."+poolName+".pourcentage + '%'); \n");
        	        }
        	        if(poolNumber==5 && display5){
        	        	result.append("      var $activeConnections5Tag = $('[id^=\"activeConnections5\"]'); \n");
        	        	result.append("      var $idleConnections5Tag = $('[id^=\"idleConnections5\"]'); \n");
        	        	result.append("      var $currentUsage5Tag = $('[id^=\"currentUsage5\"]'); \n");
        	        	result.append("      $activeConnections5Tag.val($system.dbpools."+poolName+".activeConnections);$activeConnections5Tag.prev().html($system.dbpools."+poolName+".activeConnections); \n");
        	        	result.append("      $idleConnections5Tag.val($system.dbpools."+poolName+".idleConnections);$idleConnections5Tag.prev().html($system.dbpools."+poolName+".idleConnections); \n");
        	        	result.append("      $currentUsage5Tag.val($system.dbpools."+poolName+".pourcentage + '%');$currentUsage5Tag.prev().html($system.dbpools."+poolName+".pourcentage + '%'); \n");
        	        }
        		}
        		poolNumber++;
        	}
        }   	  

        result.append("    });\n");
        result.append("  }\n");
    	return result;
    	
	}
    	
    
    protected StringBuffer getHighChartPool(String poolName){
    	
    	StringBuffer result = new StringBuffer(1024);
    	
    	if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(poolName)){
    		String name = poolName.replaceAll(" ", "").toLowerCase();
        	
        	result.append("  window.chart"+name+" = new Highcharts.StockChart({");
        	result.append("    chart : {");
        	result.append("      renderTo : '"+name+"',");
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
        	result.append("    rangeSelector: { \n");
	        result.append("      buttons: [\n");
	        result.append("        { count: 1, type: 'minute', text: '1m' },\n");
	        result.append("        { count: 5, type: 'minute', text: '5m' }, \n");
	        result.append("        { count: 30, type: 'minute', text: '\u00bdh' }, \n");
	        result.append("        { count: 1, type: 'hour', text: '1h' },\n"); 
	        result.append("        { count: 2, type: 'hour', text: '2h' }\n");
	        result.append("      ],\n");
	        result.append("      inputEnabled: false,\n");
	        result.append("      selected: 0\n");
	        result.append("     }, \n");
        	result.append("    exporting: { enabled: false },");
        	//result.append("    navigator: { enabled: false },");
        	result.append("    title : {");
        	result.append("      text : '"+poolName+"'");
        	result.append("    },");
        	result.append("    series : [ ");
        	result.append("      {type : 'area', name : 'Max', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}, ");
        	result.append("      {type : 'area', name : 'Total', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
        	result.append("      {type : 'area', name : 'Used', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}");
        	result.append("    ] ");   
        	result.append("  });");
    	}
    	
    	return result;
    	
	}

    
    
    
    
    protected void initPools(){
    	
    	String myconfigPath = org.opencms.main.OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf(org.opencms.main.CmsSystemInfo.FOLDER_CONFIG_DEFAULT);
        LOG.debug("initPools... myconfig path = " + myconfigPath) ;
        
        org.opencms.configuration.CmsConfigurationManager myconfig = null;
        myconfig = new org.opencms.configuration.CmsConfigurationManager(myconfigPath);
        
        org.opencms.configuration.CmsParameterConfiguration propertyConfiguration = null;
        String path = null;
        try {
        	path = org.opencms.main.OpenCms.getSystemInfo().getConfigurationFileRfsPath();
        	LOG.debug("initPools... configurationFileRfsPath = " + path) ;
            propertyConfiguration = new org.opencms.configuration.CmsParameterConfiguration(path);
            myconfig.setConfiguration(propertyConfiguration); 
        } catch (Exception e) {
          	LOG.error(e.getMessage() + " (" + path + ")",e);
        }
        Map configParameter = myconfig.getConfiguration();
        this.m_configParameter = configParameter;

        List poolsName = new ArrayList();
        int poolNumber = 1;
        if(configParameter!=null){
        	String poolsNameProperty = (String) configParameter.get("db.pools");
            String[] poolNames = poolsNameProperty.split(",");
            for(int i =0; i< poolNames.length; i++){
              if(poolNames[i] != null){
                poolsName.add(poolNames[i]);
                LOG.debug("initPools... Pool name = " + poolNames[i]) ;
                if(poolNumber<=5){
                	String poolName = poolNames[i];
		        	String poolUrl = getPoolUrl(poolName);
		        	LOG.debug("initPools... Pool url = " + poolUrl) ;
		        	int activeConnections = getActiveConnections(poolUrl);
		        	LOG.debug("initPools... Pool activeConnections = " + activeConnections) ;
              	  	int idleConnections = getIdleConnections(poolUrl);
              	  	LOG.debug("initPools... Pool idleConnections = " + idleConnections) ;
              	  	String poolStrategyProperty = getStrategy(poolName);
              	  	LOG.debug("initPools... Pool poolStrategyProperty = " + poolStrategyProperty) ;
              	  	String maxActivesConfiguratedString = getMaxActive(poolName);
              	  	LOG.debug("initPools... Pool maxActivesConfiguratedString = " + maxActivesConfiguratedString) ;
              	  	float pourcentage = Float.NaN;
              	  	if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(maxActivesConfiguratedString)){
              	  		pourcentage = (activeConnections * 100f)/(1f* (Integer.valueOf(maxActivesConfiguratedString)));
              	  	}
			        if(poolNumber==1){
			        	setPoolUrl1(""+poolUrl);
			        	setPoolStrategy1(""+poolStrategyProperty);
			        	setPoolMaxPoolSize1(""+maxActivesConfiguratedString);
			        	setActiveConnections1(""+activeConnections);
			        	setIdleConnections1(""+idleConnections);
			        	setCurrentUsage1(""+pourcentage+"%");
			        }else if(poolNumber==2){
			        	setPoolUrl2(""+poolUrl);
			        	setPoolStrategy2(""+poolStrategyProperty);
			        	setPoolMaxPoolSize2(""+maxActivesConfiguratedString);
			        	setActiveConnections2(""+activeConnections);
			        	setIdleConnections2(""+idleConnections);
			        	setCurrentUsage2(""+pourcentage+"%");
			        }else if(poolNumber==3){
			        	setPoolUrl3(""+poolUrl);
			        	setPoolStrategy3(""+poolStrategyProperty);
			        	setPoolMaxPoolSize3(""+maxActivesConfiguratedString);
			        	setActiveConnections3(""+activeConnections);
			        	setIdleConnections3(""+idleConnections);
			        	setCurrentUsage3(""+pourcentage+"%");
			        }else if(poolNumber==4){
			        	setPoolUrl4(""+poolUrl);
			        	setPoolStrategy4(""+poolStrategyProperty);
			        	setPoolMaxPoolSize4(""+maxActivesConfiguratedString);
			        	setActiveConnections4(""+activeConnections);
			        	setIdleConnections4(""+idleConnections);
			        	setCurrentUsage4(""+pourcentage+"%");
			        }else if(poolNumber==5){
			        	setPoolUrl5(""+poolUrl);
			        	setPoolStrategy5(""+poolStrategyProperty);
			        	setPoolMaxPoolSize5(""+maxActivesConfiguratedString);
			        	setActiveConnections5(""+activeConnections);
			        	setIdleConnections5(""+idleConnections);
			        	setCurrentUsage5(""+pourcentage+"%");
			        }
                }
                poolNumber++;
                
              }
            }
        }
    	
        
    	this.m_poolsName = poolsName;
    }
    
    
    protected String getPoolUrl(String poolName){
    	
    	String result = null;
    	org.opencms.db.CmsSqlManager sqlM = org.opencms.main.OpenCms.getSqlManager() ;
    	if(sqlM==null){
    		LOG.error("initPools - getPoolUrl : OpenCms.getSqlManager() null or empty") ;
    		return null;
    	}
    	if(CmsStringUtil.isEmptyOrWhitespaceOnly(poolName)){
	  		LOG.error("initPools - getPoolUrl : poolName null or empty") ;
	  		return null;
	  	}
    	if(sqlM.getDbPoolUrls()!=null){
        	Iterator poolsURLIterator = sqlM.getDbPoolUrls().iterator();
            while(poolsURLIterator.hasNext()){
            	String poolURL = (String) poolsURLIterator.next();
            	if(poolURL != null && poolURL.endsWith(poolName)){
            		return poolURL;
            	}
            }
    	}
    	return result ;
    	
    }
    	
    protected int getActiveConnections(String poolUrl){
    	
    	org.opencms.db.CmsSqlManager sqlM = org.opencms.main.OpenCms.getSqlManager() ;
    	if(sqlM==null){
    		LOG.error("initPools - getActiveConnections : OpenCms.getSqlManager() null or empty") ;
    		return 0;
    	}
  	  	try {
  	  		if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(poolUrl)){
  	  			return sqlM.getActiveConnections(poolUrl);
  	  		}else{
  	  			LOG.error("initPools - getActiveConnections : poolUrl null or empty") ;
  	  		}
  	  	}catch (CmsDbException e1) {
  	  		e1.printStackTrace();
  	  		LOG.error(e1);
  	  	}
  	  	return 0;
    	
    }
    
    protected int getIdleConnections(String poolUrl){
    	
    	org.opencms.db.CmsSqlManager sqlM = org.opencms.main.OpenCms.getSqlManager() ;
    	if(sqlM==null){
    		LOG.error("initPools - getIdleConnections : OpenCms.getSqlManager() null or empty") ;
    		return 0;
    	}
  	  	try {
  	  		if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(poolUrl)){
	  			return sqlM.getIdleConnections(poolUrl);
	  		}else{
	  			LOG.error("initPools - getIdleConnections : poolUrl null or empty") ;
	  		}
  	  	}catch (CmsDbException e1) {
  	  		e1.printStackTrace();
  	  		LOG.error(e1);
  	  	}
  	  	return 0;
    	
    }
    
    protected String getStrategy(String poolName){
    	
    	if(m_configParameter==null) return "";
    	String poolStrategyProperty = (String) m_configParameter.get("db.pool."+poolName+".whenExhaustedAction");
	  	if(poolStrategyProperty==null) poolStrategyProperty = "";
  	  	return poolStrategyProperty;
    	
    }
    
    protected String getMaxActive(String poolName){
    	
    	if(m_configParameter==null) return "";
    	String maxActivesConfiguratedString = (String) m_configParameter.get("db.pool."+poolName+".maxActive");
	  	if(maxActivesConfiguratedString==null) maxActivesConfiguratedString = "";
  	  	return maxActivesConfiguratedString;
    	
    }
    
}

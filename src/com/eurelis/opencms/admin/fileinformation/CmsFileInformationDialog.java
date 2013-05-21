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

package com.eurelis.opencms.admin.fileinformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsLog;
import org.opencms.util.CmsDateUtil;
import org.opencms.util.CmsStringUtil;
import org.opencms.widgets.CmsCalendarWidget;
import org.opencms.widgets.CmsDisplayWidget;
import org.opencms.widgets.CmsInputWidget;
import org.opencms.widgets.CmsVfsFileWidget;
import org.opencms.workplace.CmsWidgetDialog;
import org.opencms.workplace.CmsWidgetDialogParameter;

import com.eurelis.opencms.admin.CmsAdminSettings;


/**
 * Dialog in the administration view, to edit the resources to check the internal links for.<p>
 * 
 * @since 6.5.3 
 */
public class CmsFileInformationDialog extends CmsWidgetDialog {
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsFileInformationDialog.class);
    
    /** The dialog type. */
    public static final String DIALOG_TYPE = "filesadvsearch";


    /** localized messages Keys prefix. */
    public static final String KEY_PREFIX = "fileinformation";

    /** Defines which pages are valid for this dialog. */
    public static final String[] PAGES = {"page1"};

    /** Auxiliary Property for the VFS resources. */
    private String m_folder;
    /** Auxiliary Property for the VFS resources. */
    private int m_minLength;
    /** Auxiliary Property for the VFS resources. */
    private int m_maxLength;
    /** Auxiliary Property for the VFS resources. */
    private long m_createdBefore;
    /** Auxiliary Property for the VFS resources. */
    private long m_createdAfter;
    
    /** Auxiliary Property for the VFS resources. */
    private String m_forcedfolder;
    
    
    
    /** The admin settings object that is edited on this dialog. */
    protected CmsAdminSettings m_adminSettings;
    
    
    

    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsFileInformationDialog(CmsJspActionElement jsp) {

        super(jsp);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsFileInformationDialog(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
    }
    
    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     * @param folder the forced folder used only via resource contextual menu action. Must be relative to /.
     */
    public CmsFileInformationDialog(PageContext context, HttpServletRequest req, HttpServletResponse res, String folder) {
    	
        this(new CmsJspActionElement(context, req, res));
        setForcedFolder(folder);
        
        /*CmsJspActionElement jsp = new CmsJspActionElement(context, req, res);
        if (jsp != null) {
            m_jsp = jsp;
            m_cms = m_jsp.getCmsObject();
            m_session = m_jsp.getRequest().getSession();

            // check role
            try {
                checkRole();
            } catch (CmsRoleViolationException e) {
                throw new CmsIllegalStateException(e.getMessageContainer(), e);
            }

            // get / create the workplace settings 
            m_settings = (CmsWorkplaceSettings)m_session.getAttribute(CmsWorkplaceManager.SESSION_WORKPLACE_SETTINGS);

            if (m_settings == null) {
                // create the settings object
                m_settings = new CmsWorkplaceSettings();
                m_settings = initWorkplaceSettings(m_cms, m_settings, false);

                storeSettings(m_session, m_settings);
            }

            // initialize messages            
            CmsMessages messages = OpenCms.getWorkplaceManager().getMessages(getLocale());
            // generate a new multi messages object and add the messages from the workplace
            m_messages = new CmsMultiMessages(getLocale());
            m_messages.addMessages(messages);
            initMessages();

            // check request for changes in the workplace settings
            initWorkplaceRequestValues(m_settings, m_jsp.getRequest());

            // set cms context accordingly
            initWorkplaceCmsContext(m_settings, m_cms);

            // timewarp reset logic
            initTimeWarp(m_settings.getUserSettings(), m_session);
        }*/
        
     	// check request for changes in the workplace settings (apelle defineWidgets()
        initWorkplaceRequestValues(getSettings(), getJsp().getRequest());
        
        // set cms context accordingly
        //initWorkplaceCmsContext(getSettings(), getCms());

        // timewarp reset logic
        //initTimeWarp(getSettings().getUserSettings(), getSession());
        
    }
    
    
    
    

    /**
     * Commits the edited project to the db.<p>
     */
    public void actionCommit() {
        
        LOG.debug("actionCommit...");
        List errors = new ArrayList();
        setDialogObject(m_adminSettings);

        String folder = m_adminSettings.getFilesFolder();
        LOG.debug("actionCommit : m_adminSettings.getFilesFolder() = " + folder);
        int minLength = m_adminSettings.getFilesMinLength();
        LOG.debug("actionCommit : m_adminSettings.getFilesMinLength() = " + minLength);
        int maxLength = m_adminSettings.getFilesMaxLength();
        LOG.debug("actionCommit : m_adminSettings.getFilesMaxLength() = " + maxLength);
        long createdBefore = m_adminSettings.getFilesCreatedBefore();
        LOG.debug("actionCommit : m_adminSettings.getFilesCreatedBefore() = " + createdBefore + " = " + CmsDateUtil.getDateTimeShort(createdBefore));
        long createdAfter = m_adminSettings.getFilesCreatedAfter();
        LOG.debug("actionCommit : m_adminSettings.getFilesCreatedAfter() = " + createdAfter + " = " + CmsDateUtil.getDateTimeShort(createdAfter));

        //memorisation system du parametre...
        CmsAdminSettings.setSettingsFilesFolderValue(getCms(), folder, getSession());
        CmsAdminSettings.setSettingsFilesMinLengthValue(getCms(), minLength, getSession());
        CmsAdminSettings.setSettingsFilesMaxLengthValue(getCms(), maxLength, getSession());
        CmsAdminSettings.setSettingsFilesCreatedBeforeValue(getCms(), createdBefore, getSession());
        CmsAdminSettings.setSettingsFilesCreatedAfterValue(getCms(), createdAfter, getSession());
        
        
        // forward to the list
        try {
        	if(getForcedFolder()!=null){
        		Map mapParam = new HashMap();
        		mapParam.put("forcedfolder", getForcedFolder());
        		
        		// uri="views/admin/admin-main.jsp?root=explorer&amp;path=%2Fhistory"
        		mapParam.put("root", "explorer");
        		mapParam.put("path", "/eurelis/file_information");
        		getToolManager().jspForwardPage(this, "/system/workplace/views/admin/admin-main.jsp", mapParam);
        	}else{
        		getToolManager().jspForwardTool(this, "/eurelis_file_information/list", null);
        	}
		} catch (IOException e) {
			e.printStackTrace();
			errors.add(e);
		} catch (ServletException e) {
			e.printStackTrace();
			errors.add(e);
		}
		
		// set the list of errors to display when saving failed
        setCommitErrors(errors);
    }

    /**
     * Returns the folder of VFS resources.<p>
     *
     * @return the folder of VFS resources
     */
    public String getFolder() {

        if(CmsStringUtil.isEmptyOrWhitespaceOnly(m_folder) || !getCms().existsResource(m_folder)){
        	m_folder = "/";
        	LOG.warn("m_folder null, empty, or not exists => /");
        }
        return m_folder;
    }
    
    /**
     * Returns the forced folder of VFS resources.<p>
     * null if undefined.
     *
     * @return the forced folder of VFS resources
     */
    public String getForcedFolder() {

        if(CmsStringUtil.isEmptyOrWhitespaceOnly(m_forcedfolder)){
        	m_forcedfolder = null;
        	//LOG.warn("m_forcedfolder null, empty => null");
        	return null;
        }
        
        String currentSiteRoot = getCms().getRequestContext().getSiteRoot();
        getCms().getRequestContext().setSiteRoot("/");
        if(!getCms().existsResource(m_forcedfolder)){
        	m_forcedfolder = null;
        	//LOG.warn("m_forcedfolder not exists => null");
        	getCms().getRequestContext().setSiteRoot(currentSiteRoot);
        	return null;
    	}
        getCms().getRequestContext().setSiteRoot(currentSiteRoot);
        
        return m_forcedfolder;
    }

    /**
     * Sets the resources folder.<p>
     * 
     * @param value the resources to set
     */
    public void setFolder(String value) {

        if (value == null) {
            m_folder = "/";
            return;
        }else{
        	LOG.debug("setFolder() " + value);
        	m_folder = value;
        }
    }
    
    /**
     * Sets the resources folder.<p>
     * 
     * @param value the resources to set
     */
    public void setForcedFolder(String value) {

        if (value == null) {
            m_forcedfolder = null;
            return;
        }else{
        	//LOG.debug("setForcedFolder() " + value);
        	m_forcedfolder = value;
        }
    }
    
    /**
     * Returns the min length of VFS resources.<p>
     *
     * @return the min length of VFS resources
     */
    public int getMinLength() {

        if(m_minLength<0){
        	m_minLength = 0;
        	LOG.warn("m_minLength < 0 => 0");
        }
        return m_minLength;
    }

    /**
     * Sets the resources min length.<p>
     * 
     * @param value the resources to set
     */
    public void setMinLength(int value) {

        if (value < 0) {
        	m_minLength = 0;
            return;
        }else{
        	m_minLength = value;
        }
    }
    
    /**
     * Returns the max length of VFS resources.<p>
     *
     * @return the max length of VFS resources
     */
    public int getMaxLength() {

        if(m_maxLength<0){
        	m_maxLength = -1;
        }
        return m_maxLength;
    }

    /**
     * Sets the resources max length.<p>
     * 
     * @param value the resources to set
     */
    public void setMaxLength(int value) {

        if (value < 0) {
        	m_maxLength = -1;
            return;
        }else{
        	m_maxLength = value;
        }
    }
    
    /**
     * Returns the created before of VFS resources.<p>
     *
     * @return the created before of VFS resources
     */
    public long getCreatedBefore() {

        return m_createdBefore;
    }

    /**
     * Sets the resources CreatedBefore.<p>
     * 
     * @param value the resources to set
     */
    public void setCreatedBefore(long value) {

    	m_createdBefore = value;
    }
    
    /**
     * Returns the created after of VFS resources.<p>
     *
     * @return the created after of VFS resources
     */
    public long getCreatedAfter() {

        return m_createdAfter;
    }

    /**
     * Sets the resources Created after.<p>
     * 
     * @param value the resources to set
     */
    public void setCreatedAfter(long value) {

    	m_createdAfter = value;
    }
    
    
    
    protected String getDialogTitle() {

        return key(Messages.GUI_FILEINFORMATION_DIALOG_TITLE);
    }
    
    

    /**
     * @see org.opencms.workplace.CmsWidgetDialog#createDialogHtml(java.lang.String)
     */
    protected String createDialogHtml(String dialog) {

        StringBuffer result = new StringBuffer(1024);

        result.append(createWidgetTableStart());
        // show error header once if there were validation errors
        result.append(createWidgetErrorHeader());

        if (dialog.equals(PAGES[0])) {
            // create the widgets for the first dialog page
            result.append(dialogBlockStart(key(Messages.GUI_FILEINFORMATION_EDITOR_LABEL_BLOCK_0)));
            result.append(createWidgetTableStart());
            result.append(createDialogRowsHtml(0, 4));
            result.append(createWidgetTableEnd());
            result.append(dialogBlockEnd());
        }

        result.append(createWidgetTableEnd());
        return result.toString();
    }

    /**
     * Creates the list of widgets for this dialog.<p>
     */
    protected void defineWidgets() {

        // initialize the project object to use for the dialog
        initSessionObject();

        setKeyPrefix(KEY_PREFIX);

        // widgets to display
        LOG.debug("defineWidgets() getForcedFolder() = "+getForcedFolder());
    	if(getForcedFolder()!=null){
    		//LOG.debug("defineWidgets() getForcedFolder() != null => CmsDisplayWidget");
        	addWidget(new CmsWidgetDialogParameter(this, "forcedFolder", PAGES[0], new CmsDisplayWidget()));
        }else{
        	//LOG.debug("defineWidgets() getForcedFolder() == null => CmsVfsFileWidget");
        	addWidget(new CmsWidgetDialogParameter(m_adminSettings, "filesFolder", PAGES[0], new CmsVfsFileWidget()));
        }
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "filesMinLengthInString", PAGES[0], new CmsInputWidget()));
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "filesMaxLengthInString", PAGES[0], new CmsInputWidget()));
        
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "filesCreatedAfter", PAGES[0], new CmsCalendarWidget()));
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "filesCreatedBefore", PAGES[0], new CmsCalendarWidget()));
        
    }
    
    public void displayDialog() throws JspException, IOException, ServletException {

    	//LOG.debug("displayDialog()");
    	displayDialog(false);
    }

    /**
     * @see org.opencms.workplace.CmsWidgetDialog#getPageArray()
     */
    protected String[] getPageArray() {

        return PAGES;
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
     * Initializes the session object to work with depending on the dialog state and request parameters.<p>
     */
    protected void initSessionObject() {
        
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
        
        setForcedFolder(getCms().getRequestContext().addSiteRoot(getParamResource()));
        
        //setParamCloseLink(getJsp().link("/system/workplace/views/admin/admin-main.jsp?path=/eurelis_file_information/"));
        
        if(getForcedFolder()!=null){
        	setParamCloseLink(getJsp().link("/system/workplace/views/admin/admin-main.jsp?root=explorer"));
        }else{
        	setParamCloseLink(getJsp().link("/system/workplace/views/admin/admin-main.jsp?path=/"));
        }
        
        
    }
}
/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.eurelis.opencms.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.opencms.workplace.CmsDialog;
import org.opencms.workplace.CmsWidgetDialog;
import org.opencms.workplace.CmsWidgetDialogParameter;
import org.opencms.workplace.CmsWorkplaceSettings;

/**
 * Dialog in the administration view, to edit the resources to check the internal links for.<p>
 * 
 * @since 6.5.3 
 */
public class CmsFileInformationDialog extends CmsWidgetDialog {
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsFileInformationDialog.class);


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
			getToolManager().jspForwardTool(this, "/eurelis_file_information/list", null);
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
     * Sets the resources folder.<p>
     * 
     * @param value the resources to set
     */
    public void setFolder(String value) {

        if (value == null) {
            m_folder = "/";
            return;
        }else{
        	m_folder = value;
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
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "filesFolder", PAGES[0], new CmsVfsFileWidget()));
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "filesMinLengthInString", PAGES[0], new CmsInputWidget()));
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "filesMaxLengthInString", PAGES[0], new CmsInputWidget()));
        
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "filesCreatedAfter", PAGES[0], new CmsCalendarWidget()));
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "filesCreatedBefore", PAGES[0], new CmsCalendarWidget()));
        
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
        
        setParamCloseLink(getJsp().link("/system/workplace/views/admin/admin-main.jsp?path=/eurelis_file_information/"));
        
    }
}
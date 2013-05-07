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

package com.eurelis.opencms.admin.xmltransformation.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsFile;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsLog;
import org.opencms.report.I_CmsReportThread;
import org.opencms.workplace.explorer.CmsExplorer;
import org.opencms.workplace.list.A_CmsListReport;

import com.eurelis.opencms.admin.xmltransformation.CmsXmlTransformation;
import com.eurelis.opencms.admin.xmltransformation.ui.threads.CmsXmlTransformationThread;

public class CmsXmlTransformationProcessReport extends A_CmsListReport {
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsXmlTransformationProcessReport.class);
	
	
	/** Request parameter name for the class name to get the dialog object from. */
    public static final String PARAM_CLASSNAME = "classname";

    /** Request parameter for the class name to get the dialog object from. */
    private String m_paramClassname;

    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsXmlTransformationProcessReport(CmsJspActionElement jsp) {

        super(jsp);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsXmlTransformationProcessReport(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
        
    }
    
    private static final String MOCK_PROCESS_ACTION_REPORT = VFS_PATH_SYSTEM + "modules/com.eurelis.opencms.admin/elements/mock_process.jsp";
    private static final String REAL_PROCESS_ACTION_REPORT = VFS_PATH_SYSTEM + "system/workplace/views/workplace.jsp";
    
    @Override
    public void displayReport() throws JspException {

    	if (this.getAction()   != ACTION_REPORT_END) {
    		super.displayReport();
    	}
    	else {
    		Map params = new HashMap();
    		
    		try {
    			CmsXmlTransformation cmsXmlTransformation = (CmsXmlTransformation)this.getJsp().getRequest().getSession().getAttribute(CmsXmlTransformation.SESSION_KEY);
    			
    			if (cmsXmlTransformation.isMockProcess()) {
    				getToolManager().jspForwardPage(this, MOCK_PROCESS_ACTION_REPORT, params);
    			}
    			else {
    				
    				params.put(PARAM_RESOURCE, "");
    				getJsp().include(FILE_EXPLORER_FILELIST, null, params);
    			}
    			
			} catch (IOException e) {
				LOG.error("displayReport " + e.getMessage());
			} catch (ServletException e) {
				LOG.error("displayReport " + e.getMessage());
			}
    	}
    	
    	
    }
    
    
    /**
     * Returns the request parameter value for the class name to get the dialog object from.<p>
     * 
     * @return the request parameter value for the class name to get the dialog object from
     */
    public String getParamClassname() {

        return m_paramClassname;
    }

    /** 
     * @see org.opencms.workplace.list.A_CmsListReport#initializeThread()
     */
    public I_CmsReportThread initializeThread() {
    	
    	CmsXmlTransformation xmlTransformation = (CmsXmlTransformation) getJsp().getRequest().getSession().getAttribute(CmsXmlTransformation.SESSION_KEY);
    	
    	CmsXmlTransformationThread cxtt = new CmsXmlTransformationThread(getCms(), xmlTransformation);


        return cxtt;
    }

    /** 
     * Sets the request parameter value for the class name to get the dialog object from.<p>
     * 
     * @param className the request parameter value for the class name to get the dialog object from
     */
    public void setParamClassname(String className) {

        m_paramClassname = className;
    }
    
    
    
    
    

}

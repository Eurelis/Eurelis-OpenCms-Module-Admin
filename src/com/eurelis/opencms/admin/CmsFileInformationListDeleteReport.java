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

package com.eurelis.opencms.admin;

import org.opencms.jsp.CmsJspActionElement;
import org.opencms.report.I_CmsReportThread;
import org.opencms.workplace.list.A_CmsListReport;
import org.opencms.workplace.threads.CmsModuleDeleteThread;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * Provides a report for deleting resources.<p> 
 * 
 * @since 6.0.0 
 */
public class CmsFileInformationListDeleteReport extends A_CmsListReport {

    /** Resourcename. */
    private String m_paramResource;

    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsFileInformationListDeleteReport(CmsJspActionElement jsp) {

        super(jsp);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsFileInformationListDeleteReport(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * Gets the resource parameter.<p>
     * 
     * @return the resource parameter
     */
    public String getParamResource() {

        return m_paramResource;
    }

    /** 
     * 
     * @see org.opencms.workplace.list.A_CmsListReport#initializeThread()
     */
    public I_CmsReportThread initializeThread() {

        List resources = extractResourceNames();
        CmsFileInformationDeleteThread deleteResourceThread = new CmsFileInformationDeleteThread(getCms(), resources);
        return deleteResourceThread;
    }

    /** 
     * Sets the resource parameter.<p>
     * @param paramResource the resource parameter
     */
    public void setParamResource(String paramResource) {

        m_paramResource = paramResource;
    }

    /**
     * Extracts all resources to delete form the resource parameter.<p>
     * @return list of resource names
     */
    private List extractResourceNames() {

        List resources = new ArrayList();

        StringTokenizer tok = new StringTokenizer(getParamResource(), ",");
        while (tok.hasMoreTokens()) {
            String resource = tok.nextToken();
            resources.add(resource);
        }

        return resources;
    }

}

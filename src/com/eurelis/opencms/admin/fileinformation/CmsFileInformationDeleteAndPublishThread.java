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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.file.CmsVfsResourceNotFoundException;
import org.opencms.lock.CmsLock;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.module.CmsModuleManager;
import org.opencms.report.A_CmsReportThread;
import org.opencms.report.I_CmsReport;


/**
 * Deletes a resource.<p>
 * 
 * @since 6.0.0 
 */
public class CmsFileInformationDeleteAndPublishThread extends A_CmsReportThread {

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsFileInformationDeleteAndPublishThread.class);

    /** A list of resource name to delete. */
    private List<String> m_resourceNames;

    /**
     * Creates the resource delete thread.<p>
     * 
     * @param cms the current cms context
     * @param resourceNames the name of the resource
     * @param replaceMode the replace mode
     */
    public CmsFileInformationDeleteAndPublishThread(CmsObject cms, List<String> resourceNames) {

        super(cms, Messages.get().getBundle().key(Messages.GUI_DELETE_FILEINFORMATION_THREAD_NAME_1, resourceNames));
        m_resourceNames = resourceNames;
        initHtmlReport(cms.getRequestContext().getLocale());
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.get().getBundle().key(Messages.LOG_DELETE_THREAD_CONSTRUCTED_0));
        }
    }

    /**
     * @see org.opencms.report.A_CmsReportThread#getReportUpdate()
     */
    @Override
    public String getReportUpdate() {

        return getReport().getReportUpdate();
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {

        I_CmsReport report = getReport();
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.get().getBundle().key(Messages.LOG_DELETE_THREAD_STARTED_0));
            }
            m_resourceNames = CmsModuleManager.topologicalSort(m_resourceNames, null);
            Collections.reverse(m_resourceNames);

            String currentSiteRoot = getCms().getRequestContext().getSiteRoot();
            getCms().getRequestContext().setSiteRoot("/");
            
            report.println(Messages.get().container(Messages.RPT_DELETE_FILEINFORMATION_BEGIN_0), I_CmsReport.FORMAT_HEADLINE);
            
            Iterator<String> j = m_resourceNames.iterator();
            while (j.hasNext()) {
                String resourceName = j.next();

                resourceName = resourceName.replace('\\', '/');

                // now delete the module
                //OpenCms.getModuleManager().deleteModule(getCms(), moduleName, m_replaceMode, report);
                
                // now delete the resource
                CmsResource resource = null;
                try {
                    resource = getCms().readResource(resourceName, CmsResourceFilter.ALL);
                } catch (CmsVfsResourceNotFoundException e) {
                    // ignore
                }
                CmsLock lock = getCms().getLock(resourceName);
                if (lock.isUnlocked()) {
                    // lock the resource
                	getCms().lockResource(resourceName);
                } else if (lock.isLockableBy(getCms().getRequestContext().getCurrentUser())) {
                    // steal the resource
                	getCms().changeLock(resourceName);
                }
                if (!resource.getState().isDeleted()) {
                    // delete the resource
                	getCms().deleteResource(resourceName, CmsResource.DELETE_PRESERVE_SIBLINGS);
                }
                report.print(Messages.get().container(Messages.RPT_DELETE_0), I_CmsReport.FORMAT_NOTE);
                report.println(org.opencms.report.Messages.get().container(
                    org.opencms.report.Messages.RPT_ARGUMENT_1,
                    resourceName));
                if (!resource.getState().isNew()) {
                    // unlock the resource (so it gets deleted with next publish)
                	getCms().unlockResource(resourceName);
                }
                
            }
            
            report.println(Messages.get().container(Messages.RPT_DELETE_FILEINFORMATION_END_0), I_CmsReport.FORMAT_HEADLINE);
            
            report.println(Messages.get().container(Messages.RPT_PUBLISH_FILEINFORMATION_BEGIN_0), I_CmsReport.FORMAT_HEADLINE);
            
            j = m_resourceNames.iterator();
            while (j.hasNext()) {
                String resourceName = j.next();

                resourceName = resourceName.replace('\\', '/');
                
                if(getCms().existsResource(resourceName)){
                	// now delete the resource
                    CmsResource resource = null;
                    try {
                        resource = getCms().readResource(resourceName, CmsResourceFilter.ALL);
                    } catch (CmsVfsResourceNotFoundException e) {
                        // ignore
                    }
                    CmsLock lock = getCms().getLock(resourceName);
                    if (lock.isUnlocked()) {
                        // lock the resource
                    	getCms().lockResource(resourceName);
                    } else if (lock.isLockableBy(getCms().getRequestContext().getCurrentUser())) {
                        // steal the resource
                    	getCms().changeLock(resourceName);
                    }
                    // publish the resource
                    OpenCms.getPublishManager().publishResource(getCms(), resourceName);
                    
                    report.print(Messages.get().container(Messages.RPT_PUBLISH_0), I_CmsReport.FORMAT_NOTE);
                    report.println(org.opencms.report.Messages.get().container(
                        org.opencms.report.Messages.RPT_ARGUMENT_1,
                        resourceName));
                }
                
            }
            
            report.println(Messages.get().container(Messages.RPT_DELETE_FILEINFORMATION_END_0), I_CmsReport.FORMAT_HEADLINE);
            
            getCms().getRequestContext().setSiteRoot(currentSiteRoot);

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.get().getBundle().key(Messages.LOG_DELETE_THREAD_FINISHED_0));
            }
        } catch (Throwable e) {
            report.println(e);
            LOG.error(Messages.get().getBundle().key(Messages.LOG_FILEINFORMATION_DELETE_FAILED_1, m_resourceNames), e);
        }
    }
}
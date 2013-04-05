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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsProperty;
import org.opencms.file.CmsResource;
import org.opencms.main.CmsException;
import org.opencms.main.CmsIllegalArgumentException;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.CmsWorkplaceAction;


/**
 * Bean to store the entries made by the user in the Eurelis Admin Settings form in the
 * administration view.<p>
 * 
 */
public class CmsAdminSettings {
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsAdminSettings.class);

    /** Interval between graphs refresh. */
    private int m_interval;
    private String m_intervalString;

    public static final String SETTINGS_FILE_PATH = "/system/shared/eurelis-admin.txt";
    public static final String KEY_INTERVAL = "interval";
    public static final int DEFAULT_VALUE = 5000;
    
    /**
     * Default constructor initializing values.<p>
     */
    public CmsAdminSettings() {

    	CmsWorkplaceAction workplaceAction = CmsWorkplaceAction.getInstance();
    	CmsObject adminObject;
		try {
			adminObject = workplaceAction.getCmsAdminObject();
			m_interval = getSettingsIntervalValue(adminObject);
	        m_intervalString = String.valueOf(m_interval);
		} catch (CmsException e) {
			e.printStackTrace();
			LOG.error(e);
			m_interval = DEFAULT_VALUE;
	        m_intervalString = String.valueOf(m_interval);
		}
        
    }
    

    /**
     * Returns the interval between graphs refresh.<p>
     *
     * @return the interval between graphs refresh
     */
    public int getInterval() {

        return m_interval;
    }
    public String getIntervalString() {

        return m_intervalString;
    }


    /**
     * Sets the interval between graphs refresh.<p>
     *
     * @param interval the interval between graphs refresh
     */
    public void setInterval(int interval) {

        m_interval = interval;
        m_intervalString = String.valueOf(interval);
    }
    public void setIntervalString(String interval) {

        m_interval = Integer.valueOf(interval).intValue();
        m_intervalString = interval;
    }
    
    
    
    /**
     * Read the file of settings (create it with default value if doesn't exists).
     * @param obj
     * @return
     * @throws Exception 
     */
    public static CmsFile getSettingsFile(CmsObject obj) {
    	
    	CmsFile file = null;
        if(!obj.existsResource(SETTINGS_FILE_PATH)){
        	try {
        		String content = KEY_INTERVAL + "=" + DEFAULT_VALUE;
				List<CmsProperty> properties = new ArrayList<CmsProperty>();
				CmsResource rsc = obj.createResource(SETTINGS_FILE_PATH, 1, content.getBytes(), properties);
				file = new CmsFile(rsc);
				OpenCms.getPublishManager().publishResource(obj, SETTINGS_FILE_PATH);
				LOG.info("Admin settings : memorisation => " + content);
			} catch (CmsIllegalArgumentException e) {
				e.printStackTrace();
				LOG.error(e);
			} catch (CmsException e) {
				e.printStackTrace();
				LOG.error(e);
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error(e);
			}finally{
				try {
					obj.unlockResource(file);
				} catch (CmsException e) {
					//
				}
			}
        }else{
        	try {
        		file = obj.readFile(SETTINGS_FILE_PATH);
			} catch (CmsException e) {
				e.printStackTrace();
				LOG.error(e);
			}
        }
        return file;
        
    }
    
    /**
     * Read the setting value
     * @param obj
     * @return
     */
    public static int getSettingsIntervalValue(CmsObject obj){
    	
    	int defaultInterval = DEFAULT_VALUE;
        CmsFile file = getSettingsFile(obj);
        if(file!=null){
        	byte[] bytes = file.getContents();
        	String content = new String(bytes);
        	if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(content)){
        		Map map = CmsStringUtil.splitAsMap(content, ";", "=");
        		if(map.containsKey(CmsAdminSettings.KEY_INTERVAL)) {
        			String valueInString = (String)map.get(KEY_INTERVAL);
        			defaultInterval = CmsStringUtil.getIntValueRounded(valueInString, defaultInterval, Messages.GUI_ADMIN_SETTINGS_INTERVALS_ERROR_INTEGER);
        			LOG.debug("Admin settings : get interval => " + defaultInterval);
        		}
        	}else{
        		LOG.error("Admin settings : get interval failed, settings file empty");
        	}
        }else{
        	LOG.error("Admin settings : get interval failed, settings file null");
        }
    	return defaultInterval;
        
    }
    
    
    /**
     * Write a new value in the settings file.
     * @param obj
     * @param interval
     */
    public static void setSettingsIntervalValue(CmsObject obj, int interval){
    	
    	CmsFile file = getSettingsFile(obj);
        if(file!=null){
        	try {
        		String content = KEY_INTERVAL + "=" + interval;
        		try {
        			obj.lockResource(file);
            		file.setContents(content.getBytes());
        			obj.writeFile(file);
    				OpenCms.getPublishManager().publishResource(obj, SETTINGS_FILE_PATH);
    				LOG.info("Admin settings : memorisation => " + content);
    			} catch (CmsException e) {
    				e.printStackTrace();
    				LOG.error(e);
    			} catch (Exception e) {
    				e.printStackTrace();
    				LOG.error(e);
    			}finally{
    				try {
    					obj.unlockResource(file);
    				} catch (CmsException e) {
    					//
    				}
    			}
			} catch (CmsIllegalArgumentException e) {
				e.printStackTrace();
				LOG.error(e);
			}
        }else{
        	LOG.error("Admin settings : set interval failed, settings file null");
        }
        
    }
    
    
}

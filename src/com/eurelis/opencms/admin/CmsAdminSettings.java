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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsProject;
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
    
    /** Display CPU graph. */
    private boolean m_displayCPU;
    /** Display Heap graph. */
    private boolean m_displayHeap;
    /** Display Classes graph. */
    private boolean m_displayClasses;
    /** Display Threads graph. */
    private boolean m_displayThreads;
    
    /** Display Perm graph. */
    private boolean m_displayMemPerm;
    /** Display Old graph. */
    private boolean m_displayMemOld;
    /** Display Eden graph. */
    private boolean m_displayMemEden;
    /** Display Survivor graph. */
    private boolean m_displayMemSurvivor;
    
    /** Display Pool1 graph. */
    private boolean m_displayPool1;
    /** Display Pool2 graph. */
    private boolean m_displayPool2;
    /** Display Pool3 graph. */
    private boolean m_displayPool3;
    /** Display Pool4 graph. */
    private boolean m_displayPool4;
    /** Display Pool5 graph. */
    private boolean m_displayPool5;
    
    
    /** Search file folder */
    private String m_filesFolder;
    /** Search file min length */
    private int m_filesMinLength;
    /** Search file max length */
    private int m_filesMaxLength;
    /** Search file created before */
    private long m_filesCreatedBefore;
    /** Search file created after */
    private long m_filesCreatedAfter;
    

    public static final String SETTINGS_FILE_PATH = "/system/shared/eurelis-admin.txt";
    
    public static final String KEY_INTERVAL = "interval";
    public static final String KEY_DISPLAY_CPU = "displaycpu";
    public static final String KEY_DISPLAY_HEAP = "displayheap";
    public static final String KEY_DISPLAY_CLASSES = "displayclasses";
    public static final String KEY_DISPLAY_THREADS = "displaythreads";
    public static final String KEY_DISPLAY_MEMPERM = "displaymemperm";
    public static final String KEY_DISPLAY_MEMOLD = "displaymemold";
    public static final String KEY_DISPLAY_MEMEDEN = "displaymemeden";
    public static final String KEY_DISPLAY_MEMSURVIVOR = "displaymemsurvivor";
    public static final String KEY_DISPLAY_POOL1 = "displaypool1";
    public static final String KEY_DISPLAY_POOL2 = "displaypool2";
    public static final String KEY_DISPLAY_POOL3 = "displaypool3";
    public static final String KEY_DISPLAY_POOL4 = "displaypool4";
    public static final String KEY_DISPLAY_POOL5 = "displaypool5";
    public static final String KEY_FILES_FOLDER = "filesfolder";
    public static final String KEY_FILES_MINLENGTH = "filesminlength";
    public static final String KEY_FILES_MAXLENGTH = "filesmaxlength";
    public static final String KEY_FILES_CREATEDBEFORE = "filescreatedbefore";
    public static final String KEY_FILES_CREATEDAFTER = "filescreatedafter";
    
    public static final int DEFAULT_VALUE_INTERVAL = 5000;
    public static final boolean DEFAULT_VALUE_DISPLAY_CPU = true;
    public static final boolean DEFAULT_VALUE_DISPLAY_HEAP = true;
    public static final boolean DEFAULT_VALUE_DISPLAY_CLASSES = false;
    public static final boolean DEFAULT_VALUE_DISPLAY_THREADS = false;
    public static final boolean DEFAULT_VALUE_DISPLAY_MEMPERM = true;
    public static final boolean DEFAULT_VALUE_DISPLAY_MEMOLD = true;
    public static final boolean DEFAULT_VALUE_DISPLAY_MEMEDEN = false;
    public static final boolean DEFAULT_VALUE_DISPLAY_MEMSURVIVOR = false;
    public static final boolean DEFAULT_VALUE_DISPLAY_POOL1 = true;
    public static final boolean DEFAULT_VALUE_DISPLAY_POOL2 = true;
    public static final boolean DEFAULT_VALUE_DISPLAY_POOL3 = true;
    public static final boolean DEFAULT_VALUE_DISPLAY_POOL4 = true;
    public static final boolean DEFAULT_VALUE_DISPLAY_POOL5 = true;
    public static final String DEFAULT_VALUE_FILES_FOLDER = "/";
    public static final int DEFAULT_VALUE_FILES_MINLENGTH = 0;
    public static final int DEFAULT_VALUE_FILES_MAXLENGTH = -1;
    public static final long DEFAULT_VALUE_FILES_CREATEDBEFORE = 0 /*Long.MAX_VALUE*/;
    public static final long DEFAULT_VALUE_FILES_CREATEDAFTER = 0 /*Long.MIN_VALUE*/;
    
    /**
     * Default constructor initializing values.<p>
     */
    public CmsAdminSettings() {

    	CmsWorkplaceAction workplaceAction = CmsWorkplaceAction.getInstance();
    	CmsObject adminObject;
		try {
			LOG.debug("INIT CmsAdminSettings : with null session");
			adminObject = workplaceAction.getCmsAdminObject();
			m_interval = getSettingsIntervalValue(adminObject, null);
			m_displayCPU = getSettingsDisplayCPUValue(adminObject, null);
			m_displayHeap = getSettingsDisplayHeapValue(adminObject, null);
			m_displayClasses  = getSettingsDisplayClassesValue(adminObject, null);
			m_displayThreads = getSettingsDisplayThreadsValue(adminObject, null);
			m_displayMemPerm = getSettingsDisplayMemPermValue(adminObject, null);
			m_displayMemOld = getSettingsDisplayMemOldValue(adminObject, null);
			m_displayMemEden  = getSettingsDisplayMemEdenValue(adminObject, null);
			m_displayMemSurvivor  = getSettingsDisplayMemSurvivorValue(adminObject, null);
			m_displayPool1 = getSettingsDisplayPool1Value(adminObject, null);
			m_displayPool2 = getSettingsDisplayPool2Value(adminObject, null);
			m_displayPool3 = getSettingsDisplayPool3Value(adminObject, null);
			m_displayPool4 = getSettingsDisplayPool4Value(adminObject, null);
			m_displayPool5 = getSettingsDisplayPool5Value(adminObject, null);
			m_filesFolder = getSettingsFilesFolderValue(adminObject, null);
			m_filesMinLength = getSettingsFilesMinLengthValue(adminObject, null);
			m_filesMaxLength = getSettingsFilesMaxLengthValue(adminObject, null);
			m_filesCreatedBefore = getSettingsFilesCreatedBeforeValue(adminObject, null);
			m_filesCreatedAfter = getSettingsFilesCreatedAfterValue(adminObject, null);
		} catch (CmsException e) {
			e.printStackTrace();
			LOG.error(e);
			m_interval = DEFAULT_VALUE_INTERVAL;
			m_displayCPU = DEFAULT_VALUE_DISPLAY_CPU;
			m_displayHeap = DEFAULT_VALUE_DISPLAY_HEAP;
			m_displayClasses = DEFAULT_VALUE_DISPLAY_CLASSES;
			m_displayThreads = DEFAULT_VALUE_DISPLAY_THREADS;
			m_displayMemPerm = DEFAULT_VALUE_DISPLAY_MEMPERM;
			m_displayMemOld = DEFAULT_VALUE_DISPLAY_MEMOLD;
			m_displayMemEden = DEFAULT_VALUE_DISPLAY_MEMEDEN;
			m_displayMemSurvivor = DEFAULT_VALUE_DISPLAY_MEMSURVIVOR;
			m_displayPool1 = DEFAULT_VALUE_DISPLAY_POOL1;
			m_displayPool2 = DEFAULT_VALUE_DISPLAY_POOL2;
			m_displayPool3 = DEFAULT_VALUE_DISPLAY_POOL3;
			m_displayPool4 = DEFAULT_VALUE_DISPLAY_POOL4;
			m_displayPool5 = DEFAULT_VALUE_DISPLAY_POOL5;
			m_filesFolder = DEFAULT_VALUE_FILES_FOLDER;
			m_filesMinLength = DEFAULT_VALUE_FILES_MINLENGTH;
			m_filesMaxLength = DEFAULT_VALUE_FILES_MAXLENGTH;
			m_filesCreatedBefore = DEFAULT_VALUE_FILES_CREATEDBEFORE;
			m_filesCreatedAfter = DEFAULT_VALUE_FILES_CREATEDAFTER;
		}
        
    }
    
    /**
     * Default constructor initializing values.<p>
     */
    public CmsAdminSettings(HttpSession session) {

    	CmsWorkplaceAction workplaceAction = CmsWorkplaceAction.getInstance();
    	CmsObject adminObject;
		try {
			LOG.debug("INIT CmsAdminSettings : with a session");
			adminObject = workplaceAction.getCmsAdminObject();
			m_interval = getSettingsIntervalValue(adminObject, session);
			m_displayCPU = getSettingsDisplayCPUValue(adminObject, session);
			m_displayHeap = getSettingsDisplayHeapValue(adminObject, session);
			m_displayClasses  = getSettingsDisplayClassesValue(adminObject, session);
			m_displayThreads = getSettingsDisplayThreadsValue(adminObject, session);
			m_displayMemPerm = getSettingsDisplayMemPermValue(adminObject, session);
			m_displayMemOld = getSettingsDisplayMemOldValue(adminObject, session);
			m_displayMemEden  = getSettingsDisplayMemEdenValue(adminObject, session);
			m_displayMemSurvivor  = getSettingsDisplayMemSurvivorValue(adminObject, session);
			m_displayPool1 = getSettingsDisplayPool1Value(adminObject, session);
			m_displayPool2 = getSettingsDisplayPool2Value(adminObject, session);
			m_displayPool3 = getSettingsDisplayPool3Value(adminObject, session);
			m_displayPool4 = getSettingsDisplayPool4Value(adminObject, session);
			m_displayPool5 = getSettingsDisplayPool5Value(adminObject, session);
			m_filesFolder = getSettingsFilesFolderValue(adminObject, session);
			m_filesMinLength = getSettingsFilesMinLengthValue(adminObject, session);
			m_filesMaxLength = getSettingsFilesMaxLengthValue(adminObject, session);
			m_filesCreatedBefore = getSettingsFilesCreatedBeforeValue(adminObject, session);
			m_filesCreatedAfter = getSettingsFilesCreatedAfterValue(adminObject, session);
		} catch (CmsException e) {
			e.printStackTrace();
			LOG.error(e);
			m_interval = DEFAULT_VALUE_INTERVAL;
			m_displayCPU = DEFAULT_VALUE_DISPLAY_CPU;
			m_displayHeap = DEFAULT_VALUE_DISPLAY_HEAP;
			m_displayClasses = DEFAULT_VALUE_DISPLAY_CLASSES;
			m_displayThreads = DEFAULT_VALUE_DISPLAY_THREADS;
			m_displayMemPerm = DEFAULT_VALUE_DISPLAY_MEMPERM;
			m_displayMemOld = DEFAULT_VALUE_DISPLAY_MEMOLD;
			m_displayMemEden = DEFAULT_VALUE_DISPLAY_MEMEDEN;
			m_displayMemSurvivor = DEFAULT_VALUE_DISPLAY_MEMSURVIVOR;
			m_displayPool1 = DEFAULT_VALUE_DISPLAY_POOL1;
			m_displayPool2 = DEFAULT_VALUE_DISPLAY_POOL2;
			m_displayPool3 = DEFAULT_VALUE_DISPLAY_POOL3;
			m_displayPool4 = DEFAULT_VALUE_DISPLAY_POOL4;
			m_displayPool5 = DEFAULT_VALUE_DISPLAY_POOL5;
			m_filesFolder = DEFAULT_VALUE_FILES_FOLDER;
			m_filesMinLength = DEFAULT_VALUE_FILES_MINLENGTH;
			m_filesMaxLength = DEFAULT_VALUE_FILES_MAXLENGTH;
			m_filesCreatedBefore = DEFAULT_VALUE_FILES_CREATEDBEFORE;
			m_filesCreatedAfter = DEFAULT_VALUE_FILES_CREATEDAFTER;
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
    
    /**
     * Returns the visibility of CPU graph, true if displayed.<p>
     *
     * @return the visibility of CPU graph
     */
    public boolean getDisplayCPU() {

        return m_displayCPU;
    }

    /**
     * Returns the visibility of heap graph, true if displayed.<p>
     *
     * @return the visibility of heap graph
     */
    public boolean getDisplayHeap() {

        return m_displayHeap;
    }
    
    /**
     * Returns the visibility of Classes graph, true if displayed.<p>
     *
     * @return the visibility of Classes graph
     */
    public boolean getDisplayClasses() {

        return m_displayClasses;
    }
    
    /**
     * Returns the visibility of Threads graph, true if displayed.<p>
     *
     * @return the visibility of Threads graph
     */
    public boolean getDisplayThreads() {

        return m_displayThreads;
    }
    
    /**
     * Returns the visibility of Mem Perm graph, true if displayed.<p>
     *
     * @return the visibility of Mem Perm graph
     */
    public boolean getDisplayMemPerm() {

        return m_displayMemPerm;
    }
    
    /**
     * Returns the visibility of Mem Old graph, true if displayed.<p>
     *
     * @return the visibility of Mem Old graph
     */
    public boolean getDisplayMemOld() {

        return m_displayMemOld;
    }
    
    /**
     * Returns the visibility of Mem Eden graph, true if displayed.<p>
     *
     * @return the visibility of Mem Eden graph
     */
    public boolean getDisplayMemEden() {

        return m_displayMemEden;
    }
    
    /**
     * Returns the visibility of Mem Survivor graph, true if displayed.<p>
     *
     * @return the visibility of Mem Survivor graph
     */
    public boolean getDisplayMemSurvivor() {

        return m_displayMemSurvivor;
    }
    
    /**
     * Returns the visibility of Pool1 graph, true if displayed.<p>
     *
     * @return the visibility of Pool1 graph
     */
    public boolean getDisplayPool1() {

        return m_displayPool1;
    }
    
    /**
     * Returns the visibility of Pool2 graph, true if displayed.<p>
     *
     * @return the visibility of Pool2 graph
     */
    public boolean getDisplayPool2() {

        return m_displayPool2;
    }
    
    /**
     * Returns the visibility of Pool3 graph, true if displayed.<p>
     *
     * @return the visibility of Pool3 graph
     */
    public boolean getDisplayPool3() {

        return m_displayPool3;
    }
    
    /**
     * Returns the visibility of Pool4 graph, true if displayed.<p>
     *
     * @return the visibility of Pool4 graph
     */
    public boolean getDisplayPool4() {

        return m_displayPool4;
    }
    
    /**
     * Returns the visibility of Pool5 graph, true if displayed.<p>
     *
     * @return the visibility of Pool5 graph
     */
    public boolean getDisplayPool5() {

        return m_displayPool5;
    }
    
    public String getFilesFolder() {

        if(CmsStringUtil.isEmptyOrWhitespaceOnly(m_filesFolder)){
        	m_filesFolder = "/";
        	LOG.warn("m_filesFolder null, empty, or not exists => /");
        }
        return m_filesFolder;
    }
    
    public int getFilesMinLength() {

        if(m_filesMinLength<0){
        	m_filesMinLength = 0;
        	LOG.warn("m_filesMinLength < 0 => 0");
        }
        return m_filesMinLength;
    }
    
    public int getFilesMaxLength() {

        if(m_filesMaxLength<=0){
        	m_filesMaxLength = -1;
        }
        return m_filesMaxLength;
    }
    
    public String getFilesMinLengthInString() {

        if(m_filesMinLength<0){
        	m_filesMinLength = 0;
        	LOG.warn("m_filesMinLength < 0 => 0");
        }
        if(m_filesMinLength == 0){
        	return "";
        }else{
        	return m_filesMinLength+"";
        }
    }
    
    public String getFilesMaxLengthInString() {

        if(m_filesMaxLength<=0){
        	m_filesMaxLength = -1;
        }
        if(m_filesMaxLength == -1){
        	return "";
        }else{
        	return m_filesMaxLength+"";
        }
    }
    
    public long getFilesCreatedBefore() {

        return m_filesCreatedBefore;
    }
    
    public long getFilesCreatedAfter() {

        return m_filesCreatedAfter;
    }
    
    /**
     * Sets the interval between graphs refresh.<p>
     *
     * @param interval the interval between graphs refresh
     */
    public void setInterval(int interval) {

        m_interval = interval;
    }
    
    /**
     * Sets the visibility of CPU graph.<p>
     *
     * @param interval the visibility of CPU graph
     */
    public void setDisplayCPU(boolean isDisplayed) {

    	m_displayCPU = isDisplayed;
    }
    
    /**
     * Sets the visibility of Heap graph.<p>
     *
     * @param interval the visibility of Heap graph
     */
    public void setDisplayHeap(boolean isDisplayed) {

    	m_displayHeap = isDisplayed;
    }
    
    /**
     * Sets the visibility of Classes graph.<p>
     *
     * @param interval the visibility of Classes graph
     */
    public void setDisplayClasses(boolean isDisplayed) {

    	m_displayClasses = isDisplayed;
    }
    
    /**
     * Sets the visibility of Threads graph.<p>
     *
     * @param interval the visibility of Threads graph
     */
    public void setDisplayThreads(boolean isDisplayed) {

    	m_displayThreads = isDisplayed;
    }
    
    /**
     * Sets the visibility of Mem Perm graph.<p>
     *
     * @param interval the visibility of Mem Perm graph
     */
    public void setDisplayMemPerm(boolean isDisplayed) {

    	m_displayMemPerm = isDisplayed;
    }
    
    /**
     * Sets the visibility of Mem Old graph.<p>
     *
     * @param interval the visibility of Mem Old graph
     */
    public void setDisplayMemOld(boolean isDisplayed) {

    	m_displayMemOld = isDisplayed;
    }
    
    /**
     * Sets the visibility of Mem Eden graph.<p>
     *
     * @param interval the visibility of Mem Eden graph
     */
    public void setDisplayMemEden(boolean isDisplayed) {

    	m_displayMemEden = isDisplayed;
    }
    
    /**
     * Sets the visibility of Mem Survivor graph.<p>
     *
     * @param interval the visibility of Mem Survivor graph
     */
    public void setDisplayMemSurvivor(boolean isDisplayed) {

    	m_displayMemSurvivor = isDisplayed;
    }
    
    /**
     * Sets the visibility of Pool1 graph.<p>
     *
     * @param interval the visibility of Pool1 graph
     */
    public void setDisplayPool1(boolean isDisplayed) {

    	m_displayPool1 = isDisplayed;
    }
    
    /**
     * Sets the visibility of Pool2 graph.<p>
     *
     * @param interval the visibility of Pool2 graph
     */
    public void setDisplayPool2(boolean isDisplayed) {

    	m_displayPool2 = isDisplayed;
    }
    
    /**
     * Sets the visibility of Pool3 graph.<p>
     *
     * @param interval the visibility of Pool3 graph
     */
    public void setDisplayPool3(boolean isDisplayed) {

    	m_displayPool3 = isDisplayed;
    }
    
    /**
     * Sets the visibility of Pool4 graph.<p>
     *
     * @param interval the visibility of Pool4 graph
     */
    public void setDisplayPool4(boolean isDisplayed) {

    	m_displayPool4 = isDisplayed;
    }
    
    /**
     * Sets the visibility of Pool5 graph.<p>
     *
     * @param interval the visibility of Pool5 graph
     */
    public void setDisplayPool5(boolean isDisplayed) {

    	m_displayPool5 = isDisplayed;
    }
    
    public void setFilesFolder(String value) {

        if (value == null) {
            m_filesFolder = "/";
            return;
        }else{
        	m_filesFolder = value;
        }
    }
    
    public void setFilesMinLength(int value) {

        if (value <= 0) {
        	m_filesMinLength = 0;
            return;
        }else{
        	m_filesMinLength = value;
        }
    }
    
    public void setFilesMaxLength(int value) {

        if (value <= 0) {
        	m_filesMaxLength = -1;
            return;
        }else{
        	m_filesMaxLength = value;
        }
    }
    
    public void setFilesMinLengthInString(String value) {

    	int valueinint = CmsStringUtil.getIntValueRounded(value, 0, null);
        if (valueinint <= 0) {
        	m_filesMinLength = 0;
            return;
        }else{
        	m_filesMinLength = valueinint;
        }
    }
    
    public void setFilesMaxLengthInString(String value) {

    	int valueinint = CmsStringUtil.getIntValueRounded(value, -1, null);
        if (valueinint <= 0) {
        	m_filesMaxLength = -1;
            return;
        }else{
        	m_filesMaxLength = valueinint;
        }
    }
    
    public void setFilesCreatedBefore(long value) {

    	m_filesCreatedBefore = value;
    }
    
    
    public void setFilesCreatedAfter(long value) {

    	m_filesCreatedAfter = value;
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
        	
    		String content = KEY_INTERVAL + "=" + DEFAULT_VALUE_INTERVAL;
			List<CmsProperty> properties = new ArrayList<CmsProperty>();
			LOG.debug("Admin settings : creating settings file... => content = " + content);
			
			String currentSiteRoot = obj.getRequestContext().getSiteRoot();
			obj.getRequestContext().setSiteRoot("/");
			CmsProject currentProject = obj.getRequestContext().getCurrentProject();
			CmsProject offlineProject = currentProject;
			try {
				offlineProject = obj.readProject("Offline");
			} catch (CmsException e1) {
				e1.printStackTrace();
				LOG.error("getSettingsFile() " + e1,e1);
			}
			obj.getRequestContext().setCurrentProject(offlineProject);
			LOG.debug("Admin settings : creating settings file... => switch on site root /");
			CmsResource rsc = null;
			try {
				if(obj.existsResource(SETTINGS_FILE_PATH)){
					rsc = obj.readResource(SETTINGS_FILE_PATH);
					LOG.debug("Admin settings : reading settings file... => resource read " + SETTINGS_FILE_PATH);
				}else{
					rsc = obj.createResource(SETTINGS_FILE_PATH, 1, content.getBytes(), properties);
					LOG.debug("Admin settings : creating settings file... => resource created " + SETTINGS_FILE_PATH);
					obj.writeResource(rsc);
					LOG.debug("Admin settings : creating settings file... => resource written " + SETTINGS_FILE_PATH);
				}
			} catch (CmsIllegalArgumentException e) {
				e.printStackTrace();
				LOG.error("getSettingsFile() " + e,e);
			} catch (CmsException e) {
				e.printStackTrace();
				LOG.error("getSettingsFile() " + e,e);
			}
			if(rsc != null){
				try {
					OpenCms.getPublishManager().publishResource(obj, SETTINGS_FILE_PATH);
					LOG.debug("Admin settings : creating settings file... => resource published " + SETTINGS_FILE_PATH);
					LOG.info("Admin settings : memorisation => " + content);
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error("getSettingsFile() " + e,e);
				}
			}
			obj.getRequestContext().setSiteRoot(currentSiteRoot);
			obj.getRequestContext().setCurrentProject(currentProject);
			LOG.debug("Admin settings : creating settings file... => switch on site root " + currentSiteRoot);
			
			
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
     * Read the setting value.
     * Get it from the session, or if doesn't exists from the file, of if not found from the default value
     * @param obj
     * @param session
     * @return
     */
    public static int getSettingsIntervalValue(CmsObject obj, HttpSession session){
    	
    	int defaultValue = DEFAULT_VALUE_INTERVAL;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_INTERVAL))){
        	defaultValue = Integer.valueOf((String)session.getAttribute(KEY_INTERVAL)).intValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_INTERVAL);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = CmsStringUtil.getIntValueRounded(fileValue, defaultValue, Messages.GUI_ADMIN_SETTINGS_INTERVALS_ERROR_INTEGER);
            }
        }
    	return defaultValue;
        
    }
    
    /**
	 * Read the setting value.
	 * Get it from the session, or if doesn't exists from the file, of if not found from the default value
	 * @param obj
	 * @param session
	 */
	public static boolean getSettingsDisplayCPUValue(CmsObject obj, HttpSession session){
		
		boolean defaultValue = DEFAULT_VALUE_DISPLAY_CPU;
	    if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_DISPLAY_CPU))){
	    	defaultValue = Boolean.valueOf((String)session.getAttribute(KEY_DISPLAY_CPU)).booleanValue();
	    }else{
	    	String fileValue = readValueInSettingsFile(obj, KEY_DISPLAY_CPU);
	        if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
	        	defaultValue = Boolean.valueOf(fileValue).booleanValue();
	        }
	    }
		return defaultValue;
	    
	}

	
    
    /**
     * Read the setting value.
     * Get it from the session, or if doesn't exists from the file, of if not found from the default value
     * @param obj
     * @param session
     */
    public static boolean getSettingsDisplayHeapValue(CmsObject obj, HttpSession session){
    	
    	boolean defaultValue = DEFAULT_VALUE_DISPLAY_HEAP;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_DISPLAY_HEAP))){
        	defaultValue = Boolean.valueOf((String)session.getAttribute(KEY_DISPLAY_HEAP)).booleanValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_DISPLAY_HEAP);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Boolean.valueOf(fileValue).booleanValue();
            }
        }
    	return defaultValue;
        
    }
    
    /**
     * Read the setting value.
     * Get it from the session, or if doesn't exists from the file, of if not found from the default value
     * @param obj
     * @param session
     */
    public static boolean getSettingsDisplayClassesValue(CmsObject obj, HttpSession session){
    	
    	boolean defaultValue = DEFAULT_VALUE_DISPLAY_CLASSES;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_DISPLAY_CLASSES))){
        	defaultValue = Boolean.valueOf((String)session.getAttribute(KEY_DISPLAY_CLASSES)).booleanValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_DISPLAY_CLASSES);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Boolean.valueOf(fileValue).booleanValue();
            }
        }
    	return defaultValue;
        
    }
    
    /**
     * Read the setting value.
     * Get it from the session, or if doesn't exists from the file, of if not found from the default value
     * @param obj
     * @param session
     */
    public static boolean getSettingsDisplayThreadsValue(CmsObject obj, HttpSession session){
    	
    	boolean defaultValue = DEFAULT_VALUE_DISPLAY_THREADS;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_DISPLAY_THREADS))){
        	defaultValue = Boolean.valueOf((String)session.getAttribute(KEY_DISPLAY_THREADS)).booleanValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_DISPLAY_THREADS);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Boolean.valueOf(fileValue).booleanValue();
            }
        }
    	return defaultValue;
        
    }
    
    /**
     * Read the setting value.
     * Get it from the session, or if doesn't exists from the file, of if not found from the default value
     * @param obj
     * @param session
     */
    public static boolean getSettingsDisplayMemPermValue(CmsObject obj, HttpSession session){
    	
    	boolean defaultValue = DEFAULT_VALUE_DISPLAY_MEMPERM;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_DISPLAY_MEMPERM))){
        	defaultValue = Boolean.valueOf((String)session.getAttribute(KEY_DISPLAY_MEMPERM)).booleanValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_DISPLAY_MEMPERM);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Boolean.valueOf(fileValue).booleanValue();
            }
        }
    	return defaultValue;
        
    }
    
    /**
     * Read the setting value.
     * Get it from the session, or if doesn't exists from the file, of if not found from the default value
     * @param obj
     * @param session
     */
    public static boolean getSettingsDisplayMemOldValue(CmsObject obj, HttpSession session){
    	
    	boolean defaultValue = DEFAULT_VALUE_DISPLAY_MEMOLD;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_DISPLAY_MEMOLD))){
        	defaultValue = Boolean.valueOf((String)session.getAttribute(KEY_DISPLAY_MEMOLD)).booleanValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_DISPLAY_MEMOLD);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Boolean.valueOf(fileValue).booleanValue();
            }
        }
    	return defaultValue;
        
    }
    
    /**
     * Read the setting value.
     * Get it from the session, or if doesn't exists from the file, of if not found from the default value
     * @param obj
     * @param session
     */
    public static boolean getSettingsDisplayMemEdenValue(CmsObject obj, HttpSession session){
    	
    	boolean defaultValue = DEFAULT_VALUE_DISPLAY_MEMEDEN;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_DISPLAY_MEMEDEN))){
        	defaultValue = Boolean.valueOf((String)session.getAttribute(KEY_DISPLAY_MEMEDEN)).booleanValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_DISPLAY_MEMEDEN);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Boolean.valueOf(fileValue).booleanValue();
            }
        }
    	return defaultValue;
        
    }
    
    /**
     * Read the setting value.
     * Get it from the session, or if doesn't exists from the file, of if not found from the default value
     * @param obj
     * @param session
     */
    public static boolean getSettingsDisplayMemSurvivorValue(CmsObject obj, HttpSession session){
    	
    	boolean defaultValue = DEFAULT_VALUE_DISPLAY_MEMSURVIVOR;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_DISPLAY_MEMSURVIVOR))){
        	defaultValue = Boolean.valueOf((String)session.getAttribute(KEY_DISPLAY_MEMSURVIVOR)).booleanValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_DISPLAY_MEMSURVIVOR);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Boolean.valueOf(fileValue).booleanValue();
            }
        }
    	return defaultValue;
        
    }
    
    /**
     * Read the setting value.
     * Get it from the session, or if doesn't exists from the file, of if not found from the default value
     * @param obj
     * @param session
     */
    public static boolean getSettingsDisplayPool1Value(CmsObject obj, HttpSession session){
    	
    	boolean defaultValue = DEFAULT_VALUE_DISPLAY_POOL1;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_DISPLAY_POOL1))){
        	defaultValue = Boolean.valueOf((String)session.getAttribute(KEY_DISPLAY_POOL1)).booleanValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_DISPLAY_POOL1);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Boolean.valueOf(fileValue).booleanValue();
            }
        }
    	return defaultValue;
        
    }
    
    /**
     * Read the setting value.
     * Get it from the session, or if doesn't exists from the file, of if not found from the default value
     * @param obj
     * @param session
     */
    public static boolean getSettingsDisplayPool2Value(CmsObject obj, HttpSession session){
    	
    	boolean defaultValue = DEFAULT_VALUE_DISPLAY_POOL2;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_DISPLAY_POOL2))){
        	defaultValue = Boolean.valueOf((String)session.getAttribute(KEY_DISPLAY_POOL2)).booleanValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_DISPLAY_POOL2);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Boolean.valueOf(fileValue).booleanValue();
            }
        }
    	return defaultValue;
        
    }
    
    /**
     * Read the setting value.
     * Get it from the session, or if doesn't exists from the file, of if not found from the default value
     * @param obj
     * @param session
     */
    public static boolean getSettingsDisplayPool3Value(CmsObject obj, HttpSession session){
    	
    	boolean defaultValue = DEFAULT_VALUE_DISPLAY_POOL3;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_DISPLAY_POOL3))){
        	defaultValue = Boolean.valueOf((String)session.getAttribute(KEY_DISPLAY_POOL3)).booleanValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_DISPLAY_POOL3);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Boolean.valueOf(fileValue).booleanValue();
            }
        }
    	return defaultValue;
        
    }
    
    /**
     * Read the setting value.
     * Get it from the session, or if doesn't exists from the file, of if not found from the default value
     * @param obj
     * @param session
     */
    public static boolean getSettingsDisplayPool4Value(CmsObject obj, HttpSession session){
    	
    	boolean defaultValue = DEFAULT_VALUE_DISPLAY_POOL4;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_DISPLAY_POOL4))){
        	defaultValue = Boolean.valueOf((String)session.getAttribute(KEY_DISPLAY_POOL4)).booleanValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_DISPLAY_POOL4);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Boolean.valueOf(fileValue).booleanValue();
            }
        }
    	return defaultValue;
        
    }
    
    /**
     * Read the setting value.
     * Get it from the session, or if doesn't exists from the file, of if not found from the default value
     * @param obj
     * @param session
     */
    public static boolean getSettingsDisplayPool5Value(CmsObject obj, HttpSession session){
    	
    	boolean defaultValue = DEFAULT_VALUE_DISPLAY_POOL5;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_DISPLAY_POOL5))){
        	defaultValue = Boolean.valueOf((String)session.getAttribute(KEY_DISPLAY_POOL5)).booleanValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_DISPLAY_POOL5);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Boolean.valueOf(fileValue).booleanValue();
            }
        }
    	return defaultValue;
        
    }
    
    public static String getSettingsFilesFolderValue(CmsObject obj, HttpSession session){
    	
    	String defaultValue = DEFAULT_VALUE_FILES_FOLDER;
        if(session!=null && CmsStringUtil.isNotEmptyOrWhitespaceOnly((String)session.getAttribute(KEY_FILES_FOLDER))){
        	defaultValue = (String)session.getAttribute(KEY_FILES_FOLDER);
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_FILES_FOLDER);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = fileValue;
            }
        }
    	return defaultValue;
        
    }
    
    public static int getSettingsFilesMinLengthValue(CmsObject obj, HttpSession session){
    	
    	int defaultValue = DEFAULT_VALUE_FILES_MINLENGTH;
    	if(session!=null && (Integer)session.getAttribute(KEY_FILES_MINLENGTH)!=null){
        	defaultValue = ((Integer)session.getAttribute(KEY_FILES_MINLENGTH)).intValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_FILES_MINLENGTH);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Integer.parseInt(fileValue);
            }
        }
    	return defaultValue;
        
    }
    
    /**
     * NB : -1 = pas de filtre
     * @param obj
     * @param session
     * @return
     */
    public static int getSettingsFilesMaxLengthValue(CmsObject obj, HttpSession session){
    	
    	int defaultValue = DEFAULT_VALUE_FILES_MAXLENGTH;
    	if(session!=null && (Integer)session.getAttribute(KEY_FILES_MAXLENGTH)!=null){
        	defaultValue = ((Integer)session.getAttribute(KEY_FILES_MAXLENGTH)).intValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_FILES_MAXLENGTH);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Integer.parseInt(fileValue);
            }
        }
    	return defaultValue;
        
    }
    
    public static long getSettingsFilesCreatedBeforeValue(CmsObject obj, HttpSession session){
    	
    	long defaultValue = DEFAULT_VALUE_FILES_CREATEDBEFORE;
    	if(session!=null && (Long)session.getAttribute(KEY_FILES_CREATEDBEFORE)!=null){
        	defaultValue = ((Long)session.getAttribute(KEY_FILES_CREATEDBEFORE)).longValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_FILES_CREATEDBEFORE);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Long.parseLong(fileValue);
            }
        }
    	return defaultValue;
        
    }
    
    public static long getSettingsFilesCreatedAfterValue(CmsObject obj, HttpSession session){
    	
    	long defaultValue = DEFAULT_VALUE_FILES_CREATEDAFTER;
    	if(session!=null && (Long)session.getAttribute(KEY_FILES_CREATEDAFTER)!=null){
        	defaultValue = ((Long)session.getAttribute(KEY_FILES_CREATEDAFTER)).longValue();
        }else{
        	String fileValue = readValueInSettingsFile(obj, KEY_FILES_CREATEDAFTER);
            if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(fileValue)){
            	defaultValue = Long.parseLong(fileValue);
            }
        }
    	return defaultValue;
        
    }
    
    
    /**
     * Register the new setting value.
     * @param obj
     * @param interval
     * @param publish
     * @param session
     */
    public static void setSettingsIntervalValue(CmsObject obj, int interval, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_INTERVAL, "" + interval);
    		LOG.debug("setSettingsIntervalValue... Set " + KEY_INTERVAL + " to " + interval + " in session OK");
    	}
    	//writeNewValueInSettingsFile(obj, KEY_INTERVAL, isDisplayed);
        
    }
    
    /**
     * Register the new setting value.
     * @param obj
     * @param isDisplayed
     * @param publish
     */
    public static void setSettingsDisplayCPUValue(CmsObject obj, boolean isDisplayed, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_DISPLAY_CPU, "" + isDisplayed);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_DISPLAY_CPU, isDisplayed);
        
    }
    
    /**
     * Register the new setting value.
     * @param obj
     * @param isDisplayed
     * @param publish
     */
    public static void setSettingsDisplayHeapValue(CmsObject obj, boolean isDisplayed, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_DISPLAY_HEAP, "" + isDisplayed);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_DISPLAY_HEAP, isDisplayed);
        
    }
    
    /**
     * Register the new setting value.
     * @param obj
     * @param isDisplayed
     * @param publish
     */
    public static void setSettingsDisplayClassesValue(CmsObject obj, boolean isDisplayed, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_DISPLAY_CLASSES, "" + isDisplayed);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_DISPLAY_CLASSES, isDisplayed);
        
    }
    
    /**
     * Register the new setting value.
     * @param obj
     * @param isDisplayed
     * @param publish
     */
    public static void setSettingsDisplayThreadsValue(CmsObject obj, boolean isDisplayed, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_DISPLAY_THREADS, "" + isDisplayed);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_DISPLAY_THREADS, isDisplayed);
        
    }
    
    /**
     * Register the new setting value.
     * @param obj
     * @param isDisplayed
     * @param publish
     */
    public static void setSettingsDisplayMemPermValue(CmsObject obj, boolean isDisplayed, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_DISPLAY_MEMPERM, "" + isDisplayed);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_DISPLAY_MEMPERM, isDisplayed);
        
    }
    
    /**
     * Register the new setting value.
     * @param obj
     * @param isDisplayed
     * @param publish
     */
    public static void setSettingsDisplayMemOldValue(CmsObject obj, boolean isDisplayed, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_DISPLAY_MEMOLD, "" + isDisplayed);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_DISPLAY_MEMOLD, isDisplayed);
        
    }
    
    /**
     * Register the new setting value.
     * @param obj
     * @param isDisplayed
     * @param publish
     */
    public static void setSettingsDisplayMemEdenValue(CmsObject obj, boolean isDisplayed, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_DISPLAY_MEMEDEN, "" + isDisplayed);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_DISPLAY_MEMEDEN, isDisplayed);
        
    }
    
    /**
     * Register the new setting value.
     * @param obj
     * @param isDisplayed
     * @param publish
     */
    public static void setSettingsDisplayMemSurvivorValue(CmsObject obj, boolean isDisplayed, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_DISPLAY_MEMSURVIVOR, "" + isDisplayed);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_DISPLAY_MEMSURVIVOR, isDisplayed);
        
    }
    
    /**
     * Register the new setting value.
     * @param obj
     * @param isDisplayed
     * @param publish
     */
    public static void setSettingsDisplayPool1Value(CmsObject obj, boolean isDisplayed, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_DISPLAY_POOL1, "" + isDisplayed);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_DISPLAY_POOL1, isDisplayed);
        
    }
    
    /**
     * Register the new setting value.
     * @param obj
     * @param isDisplayed
     * @param publish
     */
    public static void setSettingsDisplayPool2Value(CmsObject obj, boolean isDisplayed, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_DISPLAY_POOL2, "" + isDisplayed);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_DISPLAY_POOL2, isDisplayed);
        
    }
    
    /**
     * Register the new setting value.
     * @param obj
     * @param isDisplayed
     * @param publish
     */
    public static void setSettingsDisplayPool3Value(CmsObject obj, boolean isDisplayed, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_DISPLAY_POOL3, "" + isDisplayed);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_DISPLAY_POOL3, isDisplayed);
        
    }
    
    /**
     * Register the new setting value.
     * @param obj
     * @param isDisplayed
     * @param publish
     */
    public static void setSettingsDisplayPool4Value(CmsObject obj, boolean isDisplayed, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_DISPLAY_POOL4, "" + isDisplayed);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_DISPLAY_POOL4, isDisplayed);
        
    }
    
    /**
     * Register the new setting value.
     * @param obj
     * @param isDisplayed
     * @param publish
     */
    public static void setSettingsDisplayPool5Value(CmsObject obj, boolean isDisplayed, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_DISPLAY_POOL5, "" + isDisplayed);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_DISPLAY_POOL5, isDisplayed);
        
    }
    
    public static void setSettingsFilesFolderValue(CmsObject obj, String folder, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_FILES_FOLDER, folder);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_FILES_FOLDER, folder);
        
    }
    
    public static void setSettingsFilesMinLengthValue(CmsObject obj, int minLength, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_FILES_MINLENGTH, minLength);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_FILES_MINLENGTH, minLength);
        
    }
    
    /**
     * 
     * @param obj
     * @param maxLength -1 if no filter
     * @param session
     */
    public static void setSettingsFilesMaxLengthValue(CmsObject obj, int maxLength, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_FILES_MAXLENGTH, maxLength);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_FILES_MAXLENGTH, maxLength);
        
    }
    
    public static void setSettingsFilesCreatedBeforeValue(CmsObject obj, long createdBefore, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_FILES_CREATEDBEFORE, createdBefore);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_FILES_CREATEDBEFORE, createdBefore);
        
    }
    
    public static void setSettingsFilesCreatedAfterValue(CmsObject obj, long createdAfter, HttpSession session){
    	
    	if(session!=null){
    		session.setAttribute(KEY_FILES_CREATEDAFTER, createdAfter);
    	}
    	//writeNewValueInSettingsFile(obj, KEY_FILES_CREATEDAFTER, createdAfter);
        
    }
    
    
    protected static String readValueInSettingsFile(CmsObject obj, String key){
    	
    	String result = null;
    	if(CmsStringUtil.isEmptyOrWhitespaceOnly(key)){
    		LOG.error("Admin settings : readValueInSettingsFile... key null or empty");
    	}else{
	    	CmsFile file = getSettingsFile(obj);
	        if(file!=null){
	        	byte[] bytes = file.getContents();
	        	String content = new String(bytes);
	        	if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(content)){
	        		Map map = CmsStringUtil.splitAsMap(content, ";", "=");
	        		if(map.containsKey(key)) {
	        			result = (String)map.get(key);
	        		}
	        	}else{
	        		//LOG.warn("Admin settings : get " + key + " failed, settings file empty");
	        	}
	        }else{
	        	//LOG.warn("Admin settings : get " + key + " failed, settings file null");
	        }
    	}
        return result;
    	
    }
    
    /**
     * Write the value in the settings file.
     * Doesn't publish the file.
     * @param obj cmsObject
     * @param key the key of the value. Ex: CmsAdminSettings.KEY_INTERVAL
     * @param value the value. Ex: CmsAdminSettings.DEFAULT_VALUE_INTERVAL
     */
    protected static void writeNewValueInSettingsFile(CmsObject obj, String key, Object value){
    	
    	if(CmsStringUtil.isEmptyOrWhitespaceOnly(key)){
    		LOG.error("Admin settings : writeNewValueInSettingsFile... key null or empty");
    	}else{
    		CmsFile file = getSettingsFile(obj);
            if(file!=null){
            	try {
            		//recuperer la map existante
            		byte[] bytes = file.getContents();
                	String content = new String(bytes);
                	if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(content)){
                		Map map = CmsStringUtil.splitAsMap(content, ";", "=");
                		//modifier ou ajouter la valeur
                		map.put(key, value);
                		//reecrit la map en string
                		String newContentInString = CmsStringUtil.mapAsString(map, ";", "=");
                		//reecrire le contenu
                		try {
                			obj.lockResource(file);
                    		file.setContents(newContentInString.getBytes());
                			obj.writeFile(file);
                			LOG.info("Admin settings : memorisation => " + newContentInString);
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
                	}
    			} catch (CmsIllegalArgumentException e) {
    				e.printStackTrace();
    				LOG.error(e);
    			}
            }else{
            	LOG.error("Admin settings : set " + key + " failed, settings file null");
            }
    	}
    	
    }
    
    
    public static void publishSettingsFile(CmsObject obj){
    	
    	CmsFile file = getSettingsFile(obj);
        if(file!=null){
        	try {
            	obj.lockResource(file);
            	OpenCms.getPublishManager().publishResource(obj, SETTINGS_FILE_PATH);
            	LOG.debug("Admin settings : published");
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
        	LOG.error("Admin settings : publish setting file failed, settings file null");
        }
    	
    }
    
    
    
    
    
}

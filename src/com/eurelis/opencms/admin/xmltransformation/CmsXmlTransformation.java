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

package com.eurelis.opencms.admin.xmltransformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eurelis.tools.xml.transformation.Journal;
import com.eurelis.tools.xml.transformation.model.XMLTransformation;

public class CmsXmlTransformation {
	
	public static final String SESSION_KEY = "__XMLTransformation__";

	private List<String> resourceList = new ArrayList<String>();
	private Map<String, Boolean> ignoreValidationMap = new HashMap<String, Boolean>();
	private boolean ignoreContentAlreadyValid = false;
	private List<Journal> journalList = null;
	
	private boolean mockProcess = true;
	
	
	public boolean hasInitialFileValidationFailed() {
		if (xmlTransformationProcessingErrorList != null && !xmlTransformationProcessingErrorList.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	private String vfsFile;

	public String getVfsFile() {
		return vfsFile;
	}

	public void setVfsFile(String vfsFile) {
		this.vfsFile = vfsFile;
	}
	
	private XMLTransformation xmlTransformation;
	
	public XMLTransformation getXmlTransformation() {
		return this.xmlTransformation;
	}
	
	public void setXmlTransformation(XMLTransformation xmlTransformation) {
		this.xmlTransformation = xmlTransformation;
	}
	
	private List<String> xmlTransformationProcessingErrorList;
	
	public List<String> getXmlTransformationProcessingErrors() {
		return this.xmlTransformationProcessingErrorList;
	}
	
	public void setXmlTransformationProcessingErrors(List<String> xmlTransformationProcessingErrorList) {
		this.xmlTransformationProcessingErrorList = xmlTransformationProcessingErrorList;
	}
	
	private int contentType;
	
	public int getContentType() {
		return contentType;
	}
	
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}
	
	
	public void addResource(String location) {
		this.resourceList.add(location);
		this.ignoreValidationMap.put(location, Boolean.TRUE);
	}
	
	public List<String> getResourceList() {
		return this.resourceList;
	}
	
	public void setIgnoreValidation(String forResource, boolean ignoreValidation) {
		this.ignoreValidationMap.put(forResource, ignoreValidation);
	}
	
	public boolean ignoreValidation(String forResource) {
		return this.ignoreValidationMap.get(forResource);
	}

	public boolean getIgnoreContentAlreadyValid() {
		return ignoreContentAlreadyValid;
	}

	public void setIgnoreContentAlreadyValid(boolean ignoreContentAlreadyValid) {
		this.ignoreContentAlreadyValid = ignoreContentAlreadyValid;
	}

	public List<Journal> getJournalList() {
		return journalList;
	}

	public void setJournalList(List<Journal> journalList) {
		this.journalList = journalList;
	}

	public boolean isMockProcess() {
		return mockProcess;
	}

	public void setMockProcess(boolean mockProcess) {
		this.mockProcess = mockProcess;
	}
	
	
	
	
}

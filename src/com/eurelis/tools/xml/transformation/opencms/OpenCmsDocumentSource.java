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


package com.eurelis.tools.xml.transformation.opencms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;

import com.eurelis.tools.xml.transformation.DocumentSource;

public class OpenCmsDocumentSource implements DocumentSource {

	private Log LOGGER = CmsLog.getLog(OpenCmsDocumentSource.class);
	
	private String documentPath = null;
	private boolean ignoreInitialValidation = false;
	
	private CmsObject cmsObject = null;
	private CmsFile cmsFile = null;
	
	private Document initialDocument = null;
	
	
	public OpenCmsDocumentSource(String documentPath, CmsObject cmsObject, boolean ignoreInitialValidation) throws CmsException, DocumentException, IOException {
		this.cmsObject = cmsObject;
		cmsFile = this.cmsObject.readFile(documentPath);
		cmsObject.lockResource(cmsFile);
		
		
		this.documentPath = documentPath;
		
		byte[] documentBytes = cmsFile.getContents();
		
		SAXReader saxReader = new SAXReader();
		InputStream is = new ByteArrayInputStream(documentBytes);
		this.initialDocument = saxReader.read(is);
		this.ignoreInitialValidation = ignoreInitialValidation;
		
		is.close();
		
		cmsObject.unlockResource(cmsFile);
	
	}
	
	
	
	@Override
	public Document document() {
		return initialDocument;
	}

	@Override
	public boolean ignoreInitialValidation() {
		return ignoreInitialValidation;
	}

	@Override
	public String documentIdentifier() {
		return documentPath;
	}

	@Override
	public void setNewDocument(Document newDoc) {
		
		String xmlString = newDoc.asXML();
		
		byte byteArray[] = xmlString.getBytes();
		
		cmsFile.setContents(byteArray);
		
		try {
			cmsObject.lockResource(cmsFile);
			cmsObject.writeFile(cmsFile);
			cmsObject.unlockResource(cmsFile);
		} catch (CmsException e) {
			LOGGER.error("setNewDocument " + e.getMessage());
		}
		
	}

}

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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsLog;
import org.opencms.xml.CmsXmlEntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.eurelis.tools.xml.transformation.DocumentFeeder;
import com.eurelis.tools.xml.transformation.DocumentSource;

public class OpenCmsDocumentFeeder implements DocumentFeeder {

	private Log LOGGER = CmsLog.getLog(OpenCmsDocumentFeeder.class);
	
	private List<OpenCmsDocumentSource> documentSourceList = new ArrayList<OpenCmsDocumentSource>();
	
	private Iterator<OpenCmsDocumentSource> documentSourceIterator = null;
	
	private Set<String> alreadyResolvedEntitySet = new HashSet<String>();
	
	
	private CmsObject cmsObject = null;
	private CmsXmlEntityResolver cmsXmlEntityResolver = null;
	
	
	public OpenCmsDocumentFeeder(CmsObject cmsObject) {
		this.cmsObject = cmsObject;
		this.cmsXmlEntityResolver = new CmsXmlEntityResolver(cmsObject);
	}
	
	
	public boolean addSourceDocument(String location, boolean ignoreInitialValidation) {
		boolean success = false;
		
		try {
			OpenCmsDocumentSource ds = new OpenCmsDocumentSource(location, this.cmsObject, ignoreInitialValidation);
			this.documentSourceList.add(ds);
			success = true;
		} catch(Exception e) {
			LOGGER.error("addDocumentSource " + location + " " + e.getMessage());
		}
		return success;
	}
	
	
	
	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		
		InputSource constructedSource = null;
		
		InputSource originalSource = this.cmsXmlEntityResolver.resolveEntity(publicId, systemId);
		
		byte[] bytes = org.opencms.util.CmsFileUtil.readFully(originalSource.getByteStream());
		String originalSourceString = org.opencms.i18n.CmsEncoder.createString(bytes, "UTF-8");
		
		
		
		for (String entity : alreadyResolvedEntitySet) {
			originalSourceString = originalSourceString.replaceAll("<xsd:include(?:/(?!>)|[^/])*schemaLocation=\"" + entity + "\"(?:/(?!>)|[^/])*/>", "");
		}
		
		alreadyResolvedEntitySet.add(systemId);

		constructedSource = new InputSource(new StringReader(originalSourceString));
		
		return constructedSource;
	}

	@Override
	public boolean hasNext() {
		if (documentSourceIterator == null) {
			documentSourceIterator = documentSourceList.iterator();
		}
		
		return documentSourceIterator.hasNext();
	}

	@Override
	public DocumentSource nextDocumentSource() {
		
		return documentSourceIterator.next();
	}

	@Override
	public void resetEntityResolver() {
		alreadyResolvedEntitySet = new HashSet<String>();

	}

}

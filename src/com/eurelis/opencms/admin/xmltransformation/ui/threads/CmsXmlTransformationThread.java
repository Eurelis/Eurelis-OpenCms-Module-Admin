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


package com.eurelis.opencms.admin.xmltransformation.ui.threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.i18n.CmsMessageContainer;
import org.opencms.i18n.I_CmsMessageBundle;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.report.A_CmsReportThread;
import org.opencms.report.I_CmsReport;

import com.eurelis.opencms.admin.xmltransformation.CmsXmlTransformation;
import com.eurelis.tools.xml.transformation.DocumentSource;
import com.eurelis.tools.xml.transformation.Journal;
import com.eurelis.tools.xml.transformation.JournalEntry;
import com.eurelis.tools.xml.transformation.model.XMLTransformation;
import com.eurelis.tools.xml.transformation.opencms.OpenCmsDocumentFeeder;
import com.eurelis.tools.xml.transformation.processors.XMLTransformationProcessor;

public class CmsXmlTransformationThread extends A_CmsReportThread {

	public static final String BATCHPROCESSOR_WARNING_ALREADY_VALID_DOCUMENT_0 = "BATCHPROCESSOR_WARNING_ALREADY_VALID_DOCUMENT_0";
	public static final String BATCHPROCESSOR_INFO_INITIALCHECK_OK_0 = "BATCHPROCESSOR_INFO_INITIALCHECK_OK_0";
	public static final String BATCHPROCESSOR_INFO_TRANSFORMATION_DONE_0 = "BATCHPROCESSOR_INFO_TRANSFORMATION_DONE_0";
	public static final String BATCHPROCESSOR_ERROR_DOCUMENT_NOTVALID_AFTER_TRANSFORMATION_0 = "BATCHPROCESSOR_ERROR_DOCUMENT_NOTVALID_AFTER_TRANSFORMATION_0";
	public static final String BATCHPROCESSOR_INFO_DOCUMENT_VALID_AFTER_TRANSFORMATION_0 = "BATCHPROCESSOR_INFO_DOCUMENT_VALID_AFTER_TRANSFORMATION_0";
	public static final String BATCHPROCESSOR_INFO_DOCUMENT_SAVED_0 = "BATCHPROCESSOR_INFO_DOCUMENT_SAVED_0";
	
	
	private CmsXmlTransformation cmsXmlTransformation = null;
	private static final Log LOG = CmsLog.getLog(CmsXmlTransformationThread.class);
	
	private CmsObject m_cms;
	
	public CmsXmlTransformationThread(CmsObject cms, CmsXmlTransformation xmlTransformation) {
		super(cms, "THREAD_NAME");
		initHtmlReport(cms.getRequestContext().getLocale());
		m_cms = cms;
		m_cms.getRequestContext().setUpdateSessionEnabled(false);
		this.cmsXmlTransformation = xmlTransformation;
	}
	
	
	@Override
	public String getReportUpdate() {
		return getReport().getReportUpdate();
	}
	
	
	@Override
	public void run() {
		
		XMLTransformation xmlTransformation = cmsXmlTransformation.getXmlTransformation();
		
		boolean testMode = cmsXmlTransformation.isMockProcess();
		boolean ignoreAllContentAlreadyValid = cmsXmlTransformation.getIgnoreContentAlreadyValid();
		
		I_CmsMessageBundle messages = Messages.get();
		
		CmsResource startingResource;
		try {
			startingResource = this.getCms().readResource(this.cmsXmlTransformation.getVfsFile());
		
		
			List<String> resourceList = new ArrayList<String>();
			
			
			if (testMode) {
				this.getReport().println(Messages.get().container(Messages.BATCHPROCESSOR_SEARCHING_DOCUMENTS_0), I_CmsReport.FORMAT_HEADLINE);
				
				correspondingResources(resourceList, startingResource);
			} else {
				resourceList = cmsXmlTransformation.getResourceList();
			}
			
			OpenCmsDocumentFeeder documentFeeder = new OpenCmsDocumentFeeder(getCms());
			
			//LOGGER.error("resourceList " + resourceList);
			
			
			if (testMode) {
			for (String location : resourceList) {
				boolean success = documentFeeder.addSourceDocument(location, false);
			
			}
			} else {
				for (String location : resourceList) {
					boolean ignoreValidation = !cmsXmlTransformation.ignoreValidation(location);
					System.out.printf("%s %d %n", location, ignoreValidation?1:0);
					documentFeeder.addSourceDocument(location, !cmsXmlTransformation.ignoreValidation(location));
				}
			}
			
			
			
			
			List<Journal> journalList = new ArrayList<Journal>();
			
			while(documentFeeder.hasNext()) {
				
				DocumentSource docSource = documentFeeder.nextDocumentSource();
				String documentID = docSource.documentIdentifier();
				Journal journal = new Journal(documentID);
				
				getReport().println(messages.container(Messages.BATCHPROCESSOR_PROCESSING_DOCUMENT_0, documentID), I_CmsReport.FORMAT_HEADLINE);
				
				
				
				boolean ignoreInitialCheck = docSource.ignoreInitialValidation();
				
				Document dom4jDocument = docSource.document();
				
				XMLTransformationProcessor xmlTransformationProcessor = new XMLTransformationProcessor(journal, xmlTransformation);
				
				boolean initialCheck = true;
				
				
				if (!ignoreInitialCheck) {
					documentFeeder.resetEntityResolver();
					initialCheck = !xmlTransformationProcessor.validateDocument(dom4jDocument, documentFeeder, true);
				
					if (!initialCheck) {
						journal.setInitialValidationHasFailed(true);
						journal.warning(xmlTransformationProcessor, BATCHPROCESSOR_WARNING_ALREADY_VALID_DOCUMENT_0);
					}
					else {
						journal.info(xmlTransformationProcessor, BATCHPROCESSOR_INFO_INITIALCHECK_OK_0);
					}
				}
				
				if (initialCheck || testMode) {
				
					Document resultDoc = xmlTransformationProcessor.processTransformation(dom4jDocument);
					journal.info(xmlTransformationProcessor, BATCHPROCESSOR_INFO_TRANSFORMATION_DONE_0);
					
					
					boolean secondCheck = false;
					
					documentFeeder.resetEntityResolver();
					secondCheck = xmlTransformationProcessor.validateDocument(resultDoc, documentFeeder, false);
					
					if (!secondCheck) {
						journal.error(xmlTransformationProcessor, BATCHPROCESSOR_ERROR_DOCUMENT_NOTVALID_AFTER_TRANSFORMATION_0);
						System.out.println(resultDoc);
					}
					
					else {
						journal.info(xmlTransformationProcessor, BATCHPROCESSOR_INFO_DOCUMENT_VALID_AFTER_TRANSFORMATION_0);
						
						
						if (!testMode) {
							docSource.setNewDocument(resultDoc);
							
							journal.info(xmlTransformationProcessor, BATCHPROCESSOR_INFO_DOCUMENT_SAVED_0);
						}
						
						
					}
				
				}
			
				if (ignoreAllContentAlreadyValid && !initialCheck) {
					
				}
				else {
					journalList.add(journal);
					for (JournalEntry journalEntry : journal.getAllEntries()) {
						String key = journalEntry.getKey();
						Object[] args = journalEntry.getArgs();
					
						CmsMessageContainer cmc = messages.container(key, args);
					
					
					
						int format = I_CmsReport.FORMAT_NOTE;
						if (journalEntry.getKind() == JournalEntry.EntryKind.WARNING) {
							format = I_CmsReport.FORMAT_WARNING;
						}
						else if (journalEntry.getKind() == JournalEntry.EntryKind.ERROR) {
							format = I_CmsReport.FORMAT_ERROR;
						}	
					
						getReport().print(cmc, format);
						getReport().println();
						
					}
					
					cmsXmlTransformation.addResource(journal.getDocumentId());
				
				}
				getReport().println();
			}
			
			this.getReport().println(Messages.get().container(Messages.BATCHPROCESSOR_PROCESSING_ENDED_0), I_CmsReport.FORMAT_HEADLINE);
			
			cmsXmlTransformation.setJournalList(journalList);
		
		} catch (CmsException e) {
			LOG.error("run " + e.getMessage());
		}
		
		
		
	}

	
	protected void correspondingResources(List<String> resourceList, CmsResource cmsResource) {
		CmsObject cmsObject = this.getCms();
		
		String relativePath = cmsObject.getSitePath(cmsResource);
		
		if (cmsResource.isFile() && cmsResource.getTypeId() == cmsXmlTransformation.getContentType()) {			
			resourceList.add(relativePath);
		}
		else if (cmsResource.isFolder()) {
			try {
				List<CmsResource> cmsResourceList = cmsObject.getResourcesInFolder(relativePath, CmsResourceFilter.ALL);
				
				for (CmsResource resource : cmsResourceList) {
					correspondingResources(resourceList, resource);
				}
				
				
			} catch (CmsException e) {
				LOG.error("correspondingResources " + e.getMessage());
			}
			
			
		}
		
	}
	
	
}

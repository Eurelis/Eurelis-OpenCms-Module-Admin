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


package com.eurelis.tools.xml.transformation;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;

import com.eurelis.tools.xml.transformation.model.XMLTransformation;
import com.eurelis.tools.xml.transformation.processors.XMLTransformationProcessor;


/**
 * The Class BatchProcessor.
 */
public class BatchProcessor {

	public static final String BATCHPROCESSOR_WARNING_ALREADY_VALID_DOCUMENT_0 = "BATCHPROCESSOR_WARNING_ALREADY_VALID_DOCUMENT_0";
	public static final String BATCHPROCESSOR_INFO_INITIALCHECK_OK_0 = "BATCHPROCESSOR_INFO_INITIALCHECK_OK_0";
	public static final String BATCHPROCESSOR_INFO_TRANSFORMATION_DONE_0 = "BATCHPROCESSOR_INFO_TRANSFORMATION_DONE_0";
	public static final String BATCHPROCESSOR_ERROR_DOCUMENT_NOTVALID_AFTER_TRANSFORMATION_0 = "BATCHPROCESSOR_ERROR_DOCUMENT_NOTVALID_AFTER_TRANSFORMATION_0";
	public static final String BATCHPROCESSOR_INFO_DOCUMENT_VALID_AFTER_TRANSFORMATION_0 = "BATCHPROCESSOR_INFO_DOCUMENT_VALID_AFTER_TRANSFORMATION_0";
	public static final String BATCHPROCESSOR_INFO_DOCUMENT_SAVED_0 = "BATCHPROCESSOR_INFO_DOCUMENT_SAVED_0";



	/** The document feeder that will provide the xml documents to process */
	private DocumentFeeder documentFeeder;

	/** The description of the transformations we want to apply to xml documents */
	private XMLTransformation transformation;



	/**
	 * Instantiates a new batch processor.
	 *
	 * @param feeder the document feeder
	 * @param transformation the description of the transformations we want to apply
	 */
	public BatchProcessor(DocumentFeeder feeder, XMLTransformation transformation) {
		this.documentFeeder = feeder;
		this.transformation = transformation;
	}


	/**
	 * The run method is used to process the transformation on every xml document provided by the feeder
	 *
	 * @param testMode if true xml documents won't be saved after the processing
	 * @return a list of journal objects describing what appened during each processing
	 */
	public List<Journal> run(boolean testMode) {
		List<Journal> journalList = new ArrayList<Journal>();

		while(documentFeeder.hasNext()) {
			DocumentSource docSource = documentFeeder.nextDocumentSource();
			String documentID = docSource.documentIdentifier();
			Journal journal = new Journal(documentID);
			journalList.add(journal);

			boolean ignoreInitialCheck = docSource.ignoreInitialValidation();

			Document dom4jDocument = docSource.document();

			XMLTransformationProcessor xmlTransformationProcessor = new XMLTransformationProcessor(journal, transformation);

			boolean initialCheck = true;

			if (!ignoreInitialCheck) {
				documentFeeder.resetEntityResolver();
				initialCheck = !xmlTransformationProcessor.validateDocument(dom4jDocument, documentFeeder, true);

				if (!initialCheck) {
					journal.setInitialValidationHasFailed(true);
					journal.error(xmlTransformationProcessor, BATCHPROCESSOR_WARNING_ALREADY_VALID_DOCUMENT_0);
				}
				else {
					journal.info(xmlTransformationProcessor, BATCHPROCESSOR_INFO_INITIALCHECK_OK_0);
				}
			}



			Document resultDoc = xmlTransformationProcessor.processTransformation(dom4jDocument);
			journal.info(xmlTransformationProcessor, BATCHPROCESSOR_INFO_TRANSFORMATION_DONE_0);


			boolean secondCheck = false;

			documentFeeder.resetEntityResolver();
			secondCheck = xmlTransformationProcessor.validateDocument(resultDoc, documentFeeder, false);

			journal.setHasSecondValidationSucceeded(secondCheck);
			
			
			if (!secondCheck) {
				journal.error(xmlTransformationProcessor, BATCHPROCESSOR_ERROR_DOCUMENT_NOTVALID_AFTER_TRANSFORMATION_0);

			}

			else {
				journal.info(xmlTransformationProcessor, BATCHPROCESSOR_INFO_DOCUMENT_VALID_AFTER_TRANSFORMATION_0);


				if (!testMode) {
					docSource.setNewDocument(resultDoc);

					journal.info(xmlTransformationProcessor, BATCHPROCESSOR_INFO_DOCUMENT_SAVED_0);
				}

			}

		}


		return journalList;
	}



}
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

package com.eurelis.tools.xml.transformation.local;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.InputSource;

import com.eurelis.tools.xml.transformation.DocumentSource;

/**
 * The Class LocalDocumentSource.
 */
public class LocalDocumentSource implements DocumentSource {

	/** The File object where is located the xml file represented by this docuent source. */
	private File documentFile = null;
	
	/** The initial dom4j document. */
	private Document initialDocument = null;
	
	/** The ignore initial validation flag. */
	private boolean ignoreInitialValidation = false;
	
	/**
	 * Instantiates a new local document source.
	 *
	 * @param documentFile the document file
	 * @param ignoreInitialValidation the ignore initial validation
	 * @throws Exception any exception that might occurs : there is no readable file, or the file isn't an XML document
	 */
	public LocalDocumentSource(File documentFile, boolean ignoreInitialValidation) throws Exception {
		this.ignoreInitialValidation = ignoreInitialValidation;
		this.documentFile = documentFile;
		this.initialDocument = readDocument(documentFile);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.eurelis.tools.xml.transformation.DocumentSource#document()
	 */
	public Document document() {
		return initialDocument;
	}

	/* (non-Javadoc)
	 * @see com.eurelis.tools.xml.transformation.DocumentSource#ignoreInitialValidation()
	 */
	public boolean ignoreInitialValidation() {
		return ignoreInitialValidation;
	}

	/* (non-Javadoc)
	 * @see com.eurelis.tools.xml.transformation.DocumentSource#documentIdentifier()
	 */
	public String documentIdentifier() {
		return documentFile.getAbsolutePath();
	}

	/* (non-Javadoc)
	 * @see com.eurelis.tools.xml.transformation.DocumentSource#setNewDocument(org.dom4j.Document)
	 */
	public void setNewDocument(Document newDoc) {
		// TODO supprimer le changement de nom apres coup
		
		File folder = documentFile.getParentFile();
		File newFile = new File(folder, documentFile.getName() + ".new");
		
		
		writeDocument(newFile, newDoc);
	}
	
	
	/**
	 * Read a dom4j document from a File object.
	 *
	 * @param file the File object indicating the document position
	 * @return the read document
	 * @throws Exception any exception that might occurs while trying to convert the file to a dom4j document
	 */
	public static Document readDocument(File file) throws Exception {
		Document document = null;
		SAXReader saxReader = new SAXReader();
		
		InputStream ist = new FileInputStream(file);
		Charset charset = Charset.forName("UTF-8");
		Reader reader = new BufferedReader(new InputStreamReader(ist, charset));
		InputSource source = new InputSource(reader);
		
		document = saxReader.read(source);
		
		return document;
	}
	
	
	/**
	 * Write a dom4j document to the location specified by a File object
	 *
	 * @param file the location where to write the document
	 * @param document the dom4j document to write
	 */
	public static void writeDocument(File file, Document document) {
		XMLWriter xmlWriter = null;

		OutputStream os = null;
		OutputStreamWriter osw = null;
		
		try {
			
			os = new FileOutputStream(file);
			osw = new OutputStreamWriter(os, "UTF-8");
			
			xmlWriter = new XMLWriter(osw, new OutputFormat("  ", true, "UTF-8"));
			xmlWriter.write(document);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (xmlWriter != null) {
				try {
					xmlWriter.flush();
					xmlWriter.close();
					
					osw.close();
					os.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
}

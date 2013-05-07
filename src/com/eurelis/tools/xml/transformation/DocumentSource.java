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

import org.dom4j.Document;

/**
 * The Interface DocumentSource.
 */
public interface DocumentSource {

	/**
	 * Returns the dom4j xml document associated with this source
	 *
	 * @return the dom4j xml document
	 */
	public Document document();

	/**
	 * Ignore initial validation.
	 *
	 * @return true if the first validation pass should be ignored for this document
	 */
	public boolean ignoreInitialValidation();

	/**
	 * Returns a documentIdentifier to be used for the journal
	 *
	 * @return a unique identifier for this document
	 */
	public String documentIdentifier();

	/**
	 * Sets the new document. Method to be used to save the result of the transformation
	 *
	 * @param newDoc the processed value of the document
	 */
	public void setNewDocument(Document newDoc);
}
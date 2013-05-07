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


/**
 * The Interface DocumentFeeder.
 */
public interface DocumentFeeder extends org.xml.sax.EntityResolver {

	/**
	 * Returns true if the feeder has more document sources.
	 *
	 * @return true if the feeder has more document sources
	 */
	public boolean hasNext();

	/**
	 * Returns the next document sources for this feeder.
	 *
	 * @return the next document sources for this feeder.
	 */
	public DocumentSource nextDocumentSource();
	
	/**
	 * Reset entity resolver. Method called after each document validation in ordre to reset schema validation mechanism
	 */
	public void resetEntityResolver();
	
}
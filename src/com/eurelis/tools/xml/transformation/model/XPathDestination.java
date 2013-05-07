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

package com.eurelis.tools.xml.transformation.model;

import org.dom4j.Document;
import org.dom4j.Node;

import com.eurelis.tools.xml.transformation.processors.DestinationProcessor;

// TODO: Auto-generated Javadoc
/**
 * Class to describe how to look for destination node by selecting an existing node using xpath.
 */
public class XPathDestination extends Destination {

	/* (non-Javadoc)
	 * @see com.eurelis.tools.xml.transformation.model.Destination#beProcessed(org.dom4j.Document, org.dom4j.Node, com.eurelis.tools.xml.transformation.processors.DestinationProcessor)
	 */
	public Node beProcessed(Document target, Node node, DestinationProcessor processor) {
		return processor.processXPathDestination(target, node, destination, position);
	}

	/**
	 * Instantiates a new XPath destination.
	 *
	 * @param dst the destination
	 */
	public XPathDestination(String dst, Position position) {
		super(dst, position);
	}
}
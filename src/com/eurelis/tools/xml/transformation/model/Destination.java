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

/**
 * Abstract class to describe how to look for destination node.
 */
public abstract class Destination {
	
	public static enum Position {
		FIRST, BEFORE, AFTER, LAST
	}
	
	
	protected Position position;
	
	
	/** The string representation of the destination, might be a XPath or a SXPath */
	protected String destination;

	/**
	 * Instantiates a new destination.
	 *
	 * @param dst the destination path
	 */
	public Destination(String dst, Position pos) {
		this.destination = dst;
		this.position = pos;
	}

	/**
	 * Be processed, callback method used by the processor to determine if the destination string should be processed has a XPath or a SXPath
	 *
	 * @param target the target document
	 * @param node the node we are processing the destination from
	 * @param processor the destination processor implementation
	 * @return the node found after having processed the destination
	 */
	public abstract Node beProcessed(Document target, Node node, DestinationProcessor processor);


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "destination " + destination;
	}

	
}
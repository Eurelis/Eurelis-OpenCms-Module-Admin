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

package com.eurelis.tools.xml.transformation.nodeselection;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.XPath;

import com.eurelis.tools.xml.transformation.model.Destination.Position;

/**
 * This Class is used to represent a decomposed XPATH with :
 * - it's first element used to start selecting nodes
 * - a list of XPath elements used to select the next level of nodes
 * - a last element : an attribute, nothing or the name of a node.
 */
public class XPATHDecomposition {
	
	/** The XPath to select the first element. */
	private XPath firstElement;
	
	/** The different XPath used to select the next node starting from the nextElement. */
	private List<XPath> elements;
	
	/** The last element : the node or attribute to be selected at the end. */
	private String lastElement;
	
	
	private Position position;
	
	
	/**
	 * Instantiates a new xPATH decomposition.
	 */
	public XPATHDecomposition() {
		this.elements = new ArrayList<XPath>();
	}
	

	/**
	 * Gets the XPath to select the first element.
	 *
	 * @return the first element XPath selector
	 */
	public XPath getFirstElement() {
		return this.firstElement;
	}
	
	/**
	 * Sets the first element XPath selector
	 *
	 * @param firstElement the new first element XPath selector
	 */
	public void setFirstElement(XPath firstElement) {
		this.firstElement = firstElement;
	}

	/**
	 * Gets the elements.
	 *
	 * @return the elements
	 */
	public List<XPath> getElements() {
		return this.elements;
	}
	
	/**
	 * Adds a XPath to select a node level
	 *
	 * @param xpathElement the xpath element
	 */
	public void addXPathElement(XPath xpathElement) {
		this.elements.add(xpathElement);
	}

	/**
	 * Gets the last element.
	 *
	 * @return the last element
	 */
	public String getLastElement() {
		return this.lastElement;
	}
	
	/**
	 * Sets the last element.
	 *
	 * @param lastElement the new last element
	 */
	public void setLastElement(String lastElement) {
		this.lastElement = lastElement;
	}
	
	public String toString() {
		return this.getFirstElement() + " " + this.elements + " " + this.lastElement;
	}


	public Position getPosition() {
		return position;
	}


	public void setPosition(Position position) {
		this.position = position;
	}
}
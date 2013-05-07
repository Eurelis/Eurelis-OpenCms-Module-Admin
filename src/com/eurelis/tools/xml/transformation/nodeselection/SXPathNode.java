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

import org.dom4j.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class SXPathNode.
 */
public class SXPathNode {
	
	/** The name of this SXPathNode. */
	private String name;
	
	/** The attributes. */
	private List<SXPathNodeAttribute> attributes;

	/**
	 * Instantiates a new SXPathNode.
	 *
	 * @param name the node name for this SXPathNode
	 */
	public SXPathNode(String name) {
		this.name = name;
		this.attributes = new ArrayList<SXPathNodeAttribute>();
	}

	/**
	 * Method to compare this SXPathNode against a dom4j element
	 *
	 * @param xmlNode the Element to test against
	 * @return true, if this SXPathNode instance and the Element node have the same name and if the SXPathAttributes associated to this SXPathNode have equivalents in the tested Element 
	 */
	public boolean equals(Element xmlNode) {
		boolean isEquivalent = true;
		
		if (this.name.equals(xmlNode.getName())) {
		
			for (SXPathNodeAttribute attribute : attributes) {
				String eValue = xmlNode.attributeValue(attribute.getName());
				if (eValue == null || !eValue.equals(attribute.getValue())) {
					isEquivalent = false;
				}
			}
			
		}
		else {
			isEquivalent = false;
		}
		
		return isEquivalent;
	}

	/**
	 * Adds a SXPathNodeAttribute.
	 *
	 * @param name the name of the SXPathNodeAttribute
	 * @param value the value of the SXPathNodeAttribute
	 */
	public void addAttribute(String name, String value) {
		this.attributes.add(new SXPathNodeAttribute(name, value));
	}

	/**
	 * Gets the name of the node represented by this SXPathNode.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	public List<SXPathNodeAttribute> getAttributes() {
		return attributes;
	}
	
	
	public String toString() {
		return this.name + " " + this.attributes;
	}
	
}
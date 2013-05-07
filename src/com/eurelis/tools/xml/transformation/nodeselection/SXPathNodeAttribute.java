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

import org.dom4j.Attribute;

/**
 * The Class SXPathNodeAttribute.
 */
public class SXPathNodeAttribute {
	
	/** The name of the attribute. */
	private String name;
	
	/** The value of the attribute. */
	private String value;

	/**
	 * Instantiates a new SXPathNodeAttribute.
	 *
	 * @param name the name of the attribute
	 * @param value the value of the attribute
	 */
	public SXPathNodeAttribute(String name, String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Method to compare this SXPathNodeAttribute against a dom4j attribute
	 *
	 * @param attribute the attribute to test against
	 * @return true, if the attribute has the same name and value than this SXPathNodeAttribute instance
	 */
	public boolean equals(Attribute attribute) {
		boolean isEquivalent = false;
		
		if (attribute.getName().equals(name)) {
			if (attribute.getValue().equals(value)) {
				isEquivalent = true;
			}	
		}
		
		return isEquivalent;
	}

	/**
	 * Gets the name of this SXPathNodeAttribute
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the value of this SXPathNodeAttribute
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	public String toString() {
		return this.name + "=" + this.value;
	}
}
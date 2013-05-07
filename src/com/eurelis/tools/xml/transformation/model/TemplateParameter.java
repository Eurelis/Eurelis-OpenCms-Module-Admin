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

import org.dom4j.XPath;

// TODO: Auto-generated Javadoc
/**
 * Class used to describe parameters used by the template transformation mechanism.
 */
public class TemplateParameter {
	
	/** The name of the parameter. */
	private String name;
	
	/** The xpath used to select the value for this parameter. */
	private XPath xpath;

	/**
	 * Instantiates a new template parameter.
	 *
	 * @param name the name
	 * @param xpath the xpath
	 */
	public TemplateParameter(String name, XPath xpath) {
		this.name = name;
		this.xpath = xpath;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the xpath.
	 *
	 * @return the xpath where to find the value
	 */
	public XPath getXpath() {
		return xpath;
	}
	
}
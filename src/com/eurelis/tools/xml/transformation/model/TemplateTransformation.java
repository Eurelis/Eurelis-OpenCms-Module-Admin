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

import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * Class used to describe a template transformation.
 */
public class TemplateTransformation {
	
	/** The template. */
	private String template;

	/** The parameters. */
	public List<TemplateParameter> parameters = new ArrayList<TemplateParameter>();
	
	
	/**
	 * Instantiates a new template transformation.
	 *
	 * @param template the template
	 * @throws DocumentException the document exception
	 */
	public TemplateTransformation(String template) throws DocumentException {
		this.template = template;
		
		DocumentHelper.parseText(template);
	}
	
	/**
	 * Instantiates a new template transformation.
	 */
	public TemplateTransformation() {
		
	}
	
	/**
	 * Adds a template parameter.
	 *
	 * @param parameter the parameter
	 */
	public void addParameter(TemplateParameter parameter) {
		parameters.add(parameter);
	}
	
	/**
	 * Gets the template as a string.
	 *
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}
	
	/**
	 * Sets the template.
	 *
	 * @param template the new template
	 * @throws DocumentException a document exception is thrown if the template can't be parsed to a valid xml dom4j document
	 */
	public void setTemplate(String template) throws DocumentException {
		this.template = template;
		
		DocumentHelper.parseText(template);
	}
}
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

package com.eurelis.tools.xml.transformation.model.builder;

import org.dom4j.XPath;

import com.eurelis.tools.xml.transformation.model.Destination.Position;
import com.eurelis.tools.xml.transformation.model.SXPathDestination;
import com.eurelis.tools.xml.transformation.model.TemplateTransformation;
import com.eurelis.tools.xml.transformation.model.UnitaryTransformation;
import com.eurelis.tools.xml.transformation.model.XPathDestination;

/**
 * Class used to instantiate UnitaryTransformation objects.
 */
public class UnitaryTransformationBuilder {
	
	/** The unitary transformation. */
	private UnitaryTransformation unitaryTransformation;
	
	/** The template transformation builder. */
	private TemplateTransformationBuilder templateTransformationBuilder;
	
	
	/**
	 * Creates the instance.
	 *
	 * @return the unitary transformation builder
	 */
	public static UnitaryTransformationBuilder createInstance() {
		return new UnitaryTransformationBuilder();
	}
	
	/**
	 * Instantiates a new unitary transformation builder.
	 */
	private UnitaryTransformationBuilder() {
		this.unitaryTransformation = new UnitaryTransformation();
	}
	
	/**
	 * Sets the XPath to select source(s) node(s).
	 *
	 * @param source the xpath
	 * @return this unitary transformation builder instance
	 */
	public UnitaryTransformationBuilder setSource(XPath source) {
		unitaryTransformation.setSource(source);
		return this;
	}

	/**
	 * Sets a template transformation.
	 *
	 * @param templateTransformation the template transformation
	 * @return this unitary transformation builder instance
	 */
	public UnitaryTransformationBuilder setTemplateTransformation(TemplateTransformation templateTransformation) {
		this.unitaryTransformation.setTemplateTransformation(templateTransformation);
		return this;
	}

	/**
	 * Gets a template transformation builder.
	 *
	 * @return a new or the current template transformation builder
	 */
	public TemplateTransformationBuilder getTemplateTransformationBuilder() {
		if (templateTransformationBuilder == null) {
			templateTransformationBuilder = TemplateTransformationBuilder.createInstance();
			this.unitaryTransformation.setTemplateTransformation(templateTransformationBuilder.build());
		}
		
		return this.templateTransformationBuilder;
	}

	/**
	 * Sets the destination as a SXPath.
	 *
	 * @param destination the SXPath destination string representation
	 * @return this unitary transformation builder instance
	 */
	public UnitaryTransformationBuilder setSXPathDestination(String destination, Position position) {
		this.unitaryTransformation.setDestination(new SXPathDestination(destination, position));
		return this;
		
	}

	/**
	 * Sets the destination as a XPath
	 *
	 * @param destination the XPath destination string representation
	 * @return this unitary transformation builder instance
	 */
	public UnitaryTransformationBuilder setXPathDestination(String destination, Position position) {
		this.unitaryTransformation.setDestination(new XPathDestination(destination, position));
		return this;
	}
	
	
	/**
	 * Builds the unitary transformation object.
	 *
	 * @return a unitary transformation
	 */
	public UnitaryTransformation build() {
		return this.unitaryTransformation;
	}
}
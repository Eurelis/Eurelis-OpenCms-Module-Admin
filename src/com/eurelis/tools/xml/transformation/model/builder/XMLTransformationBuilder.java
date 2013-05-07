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

import com.eurelis.tools.xml.transformation.model.XMLTransformation;

// TODO: Auto-generated Javadoc
/**
 * Class used to instantiate ContentTransformation objects.
 */
public class XMLTransformationBuilder {
	
	/** The xml transformation. */
	private XMLTransformation xmlTransformation;

	/**
	 * Instantiates a new XML transformation builder.
	 */
	private XMLTransformationBuilder() {
		this.xmlTransformation = new XMLTransformation();
	}
	
	/**
	 * Creates and returns a new XMLTransformationBuilder instance
	 *
	 * @return a new XML transformation builder
	 */
	public static XMLTransformationBuilder newInstance() {
		return new XMLTransformationBuilder();
	}

	
	/**
	 * Creates and returns a new unitary transformation builder to add a unitary transformation to the XMLTransformation being build
	 *
	 * @return an unitary transformation builder
	 */
	public UnitaryTransformationBuilder newUnitaryTransformationBuilder() {
		UnitaryTransformationBuilder utb = UnitaryTransformationBuilder.createInstance();
		xmlTransformation.addUnitaryTransformation(utb.build());
		return utb;
	}

	/**
	 * Builds the XMLTransformation
	 *
	 * @return the XML transformation
	 */
	public XMLTransformation build() {
		return xmlTransformation;
	}
}
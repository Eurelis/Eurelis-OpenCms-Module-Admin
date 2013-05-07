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


/**
 * This class describe unitary transformations to apply to xml documents.
 */
public class XMLTransformation {
	
	
	/** The unitary transformations. */
	private List<UnitaryTransformation> transformations = new ArrayList<UnitaryTransformation>();

	
	/**
	 * Instantiates a new XML transformation.
	 */
	public XMLTransformation() {

	}
	
	/**
	 * Adds a unitary transformation.
	 *
	 * @param unitaryTransformation the unitary transformation
	 */
	public void addUnitaryTransformation(UnitaryTransformation unitaryTransformation) {
		transformations.add(unitaryTransformation);
	}
	
	

	/**
	 * Gets the transformations.
	 *
	 * @return the transformations
	 */
	public List<UnitaryTransformation> getTransformations() {
		return transformations;
	}
}
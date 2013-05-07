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

/**
 * Class used to describe an unitary transformation, that is to say it describes how to:
 * - delete
 * - move
 * - create
 * - transform
 * 
 * nodes.
 */
public class UnitaryTransformation {
	
	/** Xpath to select source(s) node(s) for this unitary transformation. */
	private XPath source;
	
	/** An optional template transformation. If not used the transformation is either a deletion, a move or a create one */
	private TemplateTransformation templateTransformation;
	
	/** An optional destination. If not used the transformation is either a deletion or a template one*/
	private Destination destination;

	
	/**
	 * Instantiates a new unitary transformation.
	 */
	public UnitaryTransformation() {
		
	}
	
	
	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public XPath getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 *
	 * @param source the new source
	 */
	public void setSource(XPath source) {
		this.source = source;
	}

	/**
	 * Gets the destination.
	 *
	 * @return the destination
	 */
	public Destination getDestination() {
		return destination;
	}

	/**
	 * Sets the destination.
	 *
	 * @param dst the new destination
	 */
	public void setDestination(Destination dst) {
		this.destination = dst;
	}

	/**
	 * Gets the template transformation.
	 *
	 * @return the template transformation
	 */
	public TemplateTransformation getTemplateTransformation() {
		return templateTransformation;
	}

	/**
	 * Sets the template transformation.
	 *
	 * @param templateTransformation the new template transformation
	 */
	public void setTemplateTransformation(TemplateTransformation templateTransformation) {
		this.templateTransformation = templateTransformation;
	}
}
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

import java.util.List;
import java.util.ArrayList;

import com.eurelis.tools.xml.transformation.model.Destination.Position;

/**
 * The Class SXPath.
 */
public class SXPath {
	
	/** The SXPathNodes : the successive indications to tell how to select or create a node from a current one. */
	private List<SXPathNode> nodes;
	
	/** The last element : empty, a node name or an attribute */
	private String lastElement = null;
	
	private Position position = null;
	
	
	/**
	 * Instantiates a new SXPath.
	 */
	public SXPath() {
		nodes = new ArrayList<SXPathNode>();
	}
	
	

	/**
	 * Adds the parent path node.
	 *
	 * @param node the node
	 */
	public void addParentPathNode(SXPathNode node) {
		nodes.add(node);
	}

	/**
	 * Adds a child SXPathNode.
	 *
	 * @param node the SXPathNode to add
	 */
	public void addChildPathNode(SXPathNode node) {
		nodes.add(0, node);
	}

	
	/**
	 * Gets the SXPathNodes.
	 *
	 * @return the SXPathNodes
	 */
	public List<SXPathNode> getNodes() {
		return this.nodes;
	}




	/**
	 * Gets the last element.
	 *
	 * @return the last element
	 */
	public String getLastElement() {
		return lastElement;
	}

	/**
	 * Sets the last element.
	 *
	 * @param lastElement the last element
	 */
	public void setLastElement(String lastElement) {
		this.lastElement = lastElement;
	}
	
	
	public String toString() {
		return this.nodes + " " + this.lastElement;
	}



	public Position getPosition() {
		return position;
	}



	public void setPosition(Position position) {
		this.position = position;
	}
	
}
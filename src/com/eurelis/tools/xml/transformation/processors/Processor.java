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

package com.eurelis.tools.xml.transformation.processors;

import com.eurelis.tools.xml.transformation.Journal;

/**
 * An abstract class to describe a processor
 */
public abstract class Processor {
	protected Journal journal;
	private Processor parentProcessor;

	public Processor(Journal journal, Processor parent) {
		this.journal = journal;
		this.parentProcessor = parent;
	}

	public Processor getParent() {
		return parentProcessor;
	}
	
	public abstract String getName();
}
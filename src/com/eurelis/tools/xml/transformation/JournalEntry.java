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


package com.eurelis.tools.xml.transformation;

import com.eurelis.tools.xml.transformation.processors.Processor;


/**
 * The Class JournalEntry.
 */
public class JournalEntry {
	
	
	/** The level of error of this entry. */
	private EntryKind kind;
	
	/** The message. */
	private String message;
	
	/** The processor instance that sent this message. */
	private Processor writer;
	
	

	
	private String key = null;
	private Object[] args = null;
	
	
	/**
	 * Instantiates a new journal entry.
	 *
	 * @param kind the error level of this entry
	 * @param message the message
	 * @param writer the processor instance that sent this message
	 * @param sender the sender
	 */
	public JournalEntry(EntryKind kind, String key, Object[] args, Processor writer) {
		this.kind = kind;
		this.key = key;
		this.args = args;
		this.writer = writer;
	}

	
	public String getKey() {
		return this.key;
	}
	
	public Object[] getArgs() {
		return this.args;
	}
	
	/**
	 * Returns a description of the entry.
	 *
	 * @return the string
	 */
	public String description() {
		String levelKind = "";
		if (kind == EntryKind.INFO) {
			levelKind = "INFO";
		}
		else if (kind == EntryKind.WARNING) {
			levelKind = "WARNING";
		}
		else if (kind == EntryKind.ERROR) {
			levelKind = "ERROR";
		}
		
		return String.format("%s - %s : %s", levelKind, this.writer.getName(), this.message);
	}
	
	
	/**
	 * Enumeration used to describe the level of a journal entry.
	 */
	public enum EntryKind {
		/** The info level. */
		INFO, 
		/** The warning level. */
		WARNING, 
		/** The error level. */
		ERROR;
	}
	

	/**
	 * Gets the error level.
	 *
	 * @return the level
	 */
	public EntryKind getKind() {
		return kind;
	}
}
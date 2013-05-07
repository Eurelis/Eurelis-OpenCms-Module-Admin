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

import java.util.ArrayList;
import java.util.List;

import com.eurelis.tools.xml.transformation.processors.Processor;


/**
 * The Class Journal.
 */
public class Journal {
	
	/** The unique identifier for the document whose transformation is logged in this journal. */
	private String documentId;
	
	/** The journal entries. */
	private List<JournalEntry> entries = new ArrayList<JournalEntry>();
	
	
	/** Has the initial validation failed ? */
	private boolean hasInitialValidationFailed = false;
	
	
	private boolean hasTransformationSucceed = true;
	
	
	
	/**
	 * Instantiates a new journal.
	 *
	 * @param documentId the document id
	 */
	public Journal(String documentId) {
		this.documentId = documentId;
	}
	
	
	/**
	 * Info.
	 *
	 * @param writer the processor instance at the origin of the message
	 * @param message the info message
	 */
	public void info(Processor writer, String key) {
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.INFO, key, new Object[0], writer);
		
		this.entries.add(newJournalEntry);
	}
	
	public void info(Processor writer, String key, Object arg1) {
		Object []args = new Object[1];
		args[0] = arg1;
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.INFO, key, args, writer);
		
		this.entries.add(newJournalEntry);
	}
	
	public void info(Processor writer, String key, Object arg1, Object arg2) {
		Object []args = new Object[2];
		args[0] = arg1;
		args[1] = arg2;
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.INFO, key, args, writer);
		
		this.entries.add(newJournalEntry);
	}
	
	public void info(Processor writer, String key, Object arg1, Object arg2, Object arg3) {
		Object []args = new Object[3];
		args[0] = arg1;
		args[1] = arg2;
		args[2] = arg3;
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.INFO, key, args, writer);
	}
	
	public void info(Processor writer, String key, Object[] args) {
		
	}
	

	/**
	 * Warning.
	 *
	 * @param writer the processor instance at the origin of the message
	 * @param message the warning message
	 */
	public void warning(Processor writer, String key) {
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.WARNING, key, new Object[0], writer);
		
		this.entries.add(newJournalEntry);
	}
	
	public void warning(Processor writer, String key, Object arg1) {
		Object []args = new Object[1];
		args[0] = arg1;
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.WARNING, key, args, writer);
		
		this.entries.add(newJournalEntry);
	}
	
	public void warning(Processor writer, String key, Object arg1, Object arg2) {
		Object []args = new Object[2];
		args[0] = arg1;
		args[1] = arg2;
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.WARNING, key, args, writer);
		
		this.entries.add(newJournalEntry);
	}
	
	public void warning(Processor writer, String key, Object arg1, Object arg2, Object arg3) {
		Object []args = new Object[3];
		args[0] = arg1;
		args[1] = arg2;
		args[2] = arg3;
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.WARNING, key, args, writer);
		
		this.entries.add(newJournalEntry);
	}
	
	public void warning(Processor writer, String key, Object args[]) {
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.WARNING, key, args, writer);
		
		this.entries.add(newJournalEntry);
	}
	
	/**
	 * Error.
	 *
	 * @param writer the processor instance at the origin of the message
	 * @param message the error message
	 */
	public void error(Processor writer, String key) {
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.ERROR, key, new Object[0], writer);
		
		this.entries.add(newJournalEntry);
		
		this.hasTransformationSucceed = false;
	}
	
	public void error(Processor writer, String key, Object arg1) {
		Object []args = new Object[1];
		args[0] = arg1;
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.ERROR, key, args, writer);
		
		this.entries.add(newJournalEntry);
		
		this.hasTransformationSucceed = false;
	}
	
	public void error(Processor writer, String key, Object arg1, Object arg2) {
		Object []args = new Object[2];
		args[0] = arg1;
		args[1] = arg2;
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.ERROR, key, args, writer);
		
		this.entries.add(newJournalEntry);
		
		this.hasTransformationSucceed = false;
	}
	
	public void error(Processor writer, String key, Object arg1, Object arg2, Object arg3) {
		Object []args = new Object[3];
		args[0] = arg1;
		args[1] = arg2;
		args[2] = arg3;
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.ERROR, key, args, writer);
		
		this.entries.add(newJournalEntry);
		
		this.hasTransformationSucceed = false;
	}
	
	public void error(Processor writer, String key, Object args[]) {
		JournalEntry newJournalEntry = new JournalEntry(JournalEntry.EntryKind.ERROR, key, args, writer);
		
		this.entries.add(newJournalEntry);
		
		this.hasTransformationSucceed = false;
	}
	
	
	
	
	/**
	 * Gets the all entries.
	 *
	 * @return the all entries
	 */
	public List<JournalEntry> getAllEntries() {
		return this.entries;
	}
	
	
	/**
	 * Gets the entries.
	 *
	 * @param minimumLevel the minimum level
	 * @return the entries
	 */
	public List<JournalEntry> getEntries(JournalEntry.EntryKind minimumLevel) {
		if (minimumLevel == null || minimumLevel == JournalEntry.EntryKind.INFO) {
			return this.getAllEntries();
		}
		else {
			List<JournalEntry> filteredList = new ArrayList<JournalEntry>();
			
			if (minimumLevel == JournalEntry.EntryKind.WARNING) {
				
				for (JournalEntry je : this.entries) {
					if (je.getKind() != JournalEntry.EntryKind.INFO) {
						// if the kind isn't INFO, then it is either WARNING or ERROR
						filteredList.add(je);
					}
				}
				
			}
			else {
				// ERROR
				for (JournalEntry je : this.entries) {
					if (je.getKind() == JournalEntry.EntryKind.ERROR) {
						filteredList.add(je);
					}
				}
			}
			
			return filteredList;
		}
		
	}
	
	/**
	 * Return the identifier of the document
	 * @return document id
	 */
	public String getDocumentId() {
		return documentId;
	}
	
	/**
	 * Setter for hasInitialValidationFailed
	 * @param failure true if the document was already valid, false otherwise
	 */
	public void setInitialValidationHasFailed(boolean failure) {
		this.hasInitialValidationFailed = failure;
	}
	
	/**
	 * Getter for hasInitialValidationFailed
	 * @return true if the document was already valid, false otherwise
	 */
	public boolean hasInitialValidationFailed() {
		return this.hasInitialValidationFailed;
	}
	
	public void setHasSecondValidationSucceeded(boolean secondValidation) {
		this.hasTransformationSucceed &= secondValidation;
	}
	
	
	public boolean hasTransformationSucceed() {
		return this.hasTransformationSucceed;
	}
	
	
}
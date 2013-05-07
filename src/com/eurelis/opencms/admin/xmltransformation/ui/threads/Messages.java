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


package com.eurelis.opencms.admin.xmltransformation.ui.threads;

import org.opencms.i18n.A_CmsMessageBundle;
import org.opencms.i18n.I_CmsMessageBundle;


public class Messages extends A_CmsMessageBundle {
	
	public static final String BATCHPROCESSOR_PROCESSING_DOCUMENT_0 = "BATCHPROCESSOR_PROCESSING_DOCUMENT_0";
	
	public static final String BATCHPROCESSOR_SEARCHING_DOCUMENTS_0 = "BATCHPROCESSOR_SEARCHING_DOCUMENTS_0";
	
	public static final String BATCHPROCESSOR_PROCESSING_ENDED_0 = "BATCHPROCESSOR_PROCESSING_ENDED_0";
	
	private static final String BUNDLE_NAME = "com.eurelis.opencms.admin.xmltransformation.ui.threads.messages";
	
	private static final I_CmsMessageBundle INSTANCE = new Messages();
	
	private Messages() {

	}

	@Override
	public String getBundleName() {
		return BUNDLE_NAME;
	}
	
	public static I_CmsMessageBundle get() {
		return INSTANCE;
	}
	
	
}

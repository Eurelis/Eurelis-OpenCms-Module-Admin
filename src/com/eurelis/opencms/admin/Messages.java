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
package com.eurelis.opencms.admin;

import org.opencms.i18n.A_CmsMessageBundle;
import org.opencms.i18n.I_CmsMessageBundle;


public final class Messages extends A_CmsMessageBundle {
	
	/** Message constant for key in the resource bundle. */
    public static final String GUI_ADMIN_SETTINGS_INTERVALS_ERROR_INTEGER = "GUI_ADMIN_SETTINGS_INTERVALS_ERROR_INTEGER";
	
	
	
	
	

	/** Message constant for key in the resource bundle. */
    public static final String GUI_EURELIS_ADMIN_TOOL_GROUP_0 = "GUI_EURELIS_ADMIN_TOOL_GROUP_0";
    /** Message constant for key in the resource bundle. */
    public static final String GUI_SYSTEMINFORMATION_ADMIN_TOOL_NAME_0 = "GUI_SYSTEMINFORMATION_ADMIN_TOOL_NAME_0";
    /** Message constant for key in the resource bundle. */
    public static final String GUI_SYSTEMINFORMATION_ADMIN_TOOL_HELP_0 = "GUI_SYSTEMINFORMATION_ADMIN_TOOL_HELP_0";
    /** Message constant for key in the resource bundle. */
    public static final String GUI_SYSTEMINFORMATION_ADMIN_TOOL_GROUP_0 = "GUI_SYSTEMINFORMATION_ADMIN_TOOL_GROUP_0";
    
    
    
    
    
    
    

    /** Name of the used resource bundle. */
    private static final String BUNDLE_NAME = "com.eurelis.opencms.admin.messages";

    /** Static instance member. */
    private static final I_CmsMessageBundle INSTANCE = new Messages();

    /**
     * Hides the public constructor for this utility class.<p>
     */
    private Messages() {

        // hide the constructor
    }

    /**
     * Returns an instance of this localized message accessor.<p>
     * 
     * @return an instance of this localized message accessor
     */
    public static I_CmsMessageBundle get() {

        return INSTANCE;
    }

    /**
     * Returns the bundle name for this OpenCms package.<p>
     * 
     * @return the bundle name for this OpenCms package
     */
    public String getBundleName() {

        return BUNDLE_NAME;
    }

}
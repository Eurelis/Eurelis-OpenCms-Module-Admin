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
package com.eurelis.opencms.admin.systeminformation;

import org.opencms.i18n.A_CmsMessageBundle;
import org.opencms.i18n.I_CmsMessageBundle;


public final class Messages extends A_CmsMessageBundle {

	
	/** Message constant for key in the resource bundle. */
    public static final String GUI_ADMIN_SETTINGS_INTERVALS_5000 = "GUI_ADMIN_SETTINGS_INTERVALS_5000";
    /** Message constant for key in the resource bundle. */
    public static final String GUI_ADMIN_SETTINGS_INTERVALS_10000 = "GUI_ADMIN_SETTINGS_INTERVALS_10000";
    
	
	/** Message constant for key in the resource bundle. */
    public static final String GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_BLOCK_1 = "GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_BLOCK_1";
    /** Message constant for key in the resource bundle. */
    public static final String GUI_SYSTEMINFORMATION_OVERVIEW_SETTINGS_NAME_0 = "GUI_SYSTEMINFORMATION_OVERVIEW_SETTINGS_NAME_0";

    
    /** Message constant for key in the resource bundle. */
    public static final String GUI_SYSTEMINFORMATION_CPU_ADMIN_TOOL_BLOCK_SETTINGS = "GUI_SYSTEMINFORMATION_CPU_ADMIN_TOOL_BLOCK_SETTINGS";
    /** Message constant for key in the resource bundle. */
    public static final String GUI_SYSTEMINFORMATION_CPU_ADMIN_TOOL_BLOCK_1 = "GUI_SYSTEMINFORMATION_CPU_ADMIN_TOOL_BLOCK_1";
    /** Message constant for key in the resource bundle. */
    public static final String GUI_SYSTEMINFORMATION_CPU_ADMIN_TOOL_BLOCK_2 = "GUI_SYSTEMINFORMATION_CPU_ADMIN_TOOL_BLOCK_2";
    /** Message constant for key in the resource bundle. */
    public static final String GUI_SYSTEMINFORMATION_CPU_ADMIN_TOOL_BLOCK_3 = "GUI_SYSTEMINFORMATION_CPU_ADMIN_TOOL_BLOCK_3";
    
    
    /** Message constant for key in the resource bundle. */
    public static final String GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_BLOCK_SETTINGS = "GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_BLOCK_SETTINGS";
    /** Message constant for key in the resource bundle. */
    public static final String GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_BLOCK_ = "GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_BLOCK_";
    
    
    /** Message constant for key in the resource bundle. */
    public static final String GUI_SYSTEMINFORMATION_DBPOOLS_ADMIN_TOOL_BLOCK_SETTINGS = "GUI_SYSTEMINFORMATION_DBPOOLS_ADMIN_TOOL_BLOCK_SETTINGS";
    /** Message constant for key in the resource bundle. */
    public static final String GUI_SYSTEMINFORMATION_DBPOOLS_ADMIN_TOOL_BLOCK_ = "GUI_SYSTEMINFORMATION_DBPOOLS_ADMIN_TOOL_BLOCK_";
    
    

    /** Name of the used resource bundle. */
    private static final String BUNDLE_NAME = "com.eurelis.opencms.admin.systeminformation.messages";

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
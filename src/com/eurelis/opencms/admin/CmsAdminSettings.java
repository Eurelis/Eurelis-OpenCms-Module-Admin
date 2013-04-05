/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.eurelis.opencms.admin;


/**
 * Bean to store the entries made by the user in the Eurelis Admin Settings form in the
 * administration view.<p>
 * 
 */
public class CmsAdminSettings {

    /** Interval between graphs refresh. */
    private int m_interval;

    
    /**
     * Default constructor initializing values.<p>
     */
    public CmsAdminSettings() {

        //m_interval = OpenCms.getSystemInfo().getHistoryVersions();
        m_interval = 5000;
        
    }
    

    /**
     * Returns the interval between graphs refresh.<p>
     *
     * @return the interval between graphs refresh
     */
    public int getInterval() {

        return m_interval;
    }


    /**
     * Sets the interval between graphs refresh.<p>
     *
     * @param interval the interval between graphs refresh
     */
    public void setInterval(int interval) {

        m_interval = interval;
    }
}

<% 			
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
 %>
<%@page language="java" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@page import="com.eurelis.opencms.admin.systeminformation.CmsCPUThreadsClassesOverviewDialog" %>
					
					
<%
//initialize the widget dialog
CmsCPUThreadsClassesOverviewDialog wpWidget = new CmsCPUThreadsClassesOverviewDialog(pageContext, request, response);
// perform the widget actions   
wpWidget.displayDialog(true);
if (wpWidget.isForwarded()) {
	return;
}
//write the content of widget dialog
wpWidget.writeDialog();
%>
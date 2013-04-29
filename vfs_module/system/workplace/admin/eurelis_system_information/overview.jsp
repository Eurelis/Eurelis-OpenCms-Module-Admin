<%@page import="
					java.util.*,
					org.opencms.file.*,
					org.opencms.db.*,
					org.opencms.jsp.CmsJspActionElement,
					org.opencms.main.OpenCms,
					org.opencms.workplace.CmsDialog,
					com.eurelis.opencms.admin.Messages,
					com.eurelis.opencms.admin.CmsSystemInformationOverviewDialog,
					java.lang.management.ManagementFactory" %>
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
<%
//initialize the widget dialog
CmsSystemInformationOverviewDialog wpWidget = new CmsSystemInformationOverviewDialog(pageContext, request, response);
// perform the widget actions   
wpWidget.displayDialog(true);
if (wpWidget.isForwarded()) {
	return;
}
//write the content of widget dialog
wpWidget.writeDialog();
%>





<%-- 







<%!
protected String writeRow(String label, String value){
	StringBuffer result = new StringBuffer(2048);
	result.append("<tr>");
	result.append("<td class='xmlLabel'>" + label + "</td>");
	result.append("<td><span style='display:block; height: 1px; width: 16px;'></span></td>");
	result.append("<td class='xmlTd'><span class='xmlInput textInput' style='border: 0px solid black;'>" + value + "</span></td>");
	result.append("<td><span style='display:block; height: 1px; width: 5px;'></span></td>");
	result.append("<td></td>");
	result.append("</tr>");
	return result.toString() ;
}
%>

<% CmsJspActionElement actionElement = new CmsJspActionElement(pageContext, request, response); %>
<% CmsDialog wpDialog = new CmsDialog(pageContext, request, response); %>


<% com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean(); %>
<% java.lang.management.OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean(); %>
<% java.lang.management.ThreadMXBean threadBean = ManagementFactory.getThreadMXBean(); %>
<% java.lang.management.RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean(); %>
<% java.lang.management.ClassLoadingMXBean classesBean = ManagementFactory.getClassLoadingMXBean(); %>

<%

StringBuffer result = new StringBuffer(2048);

result.append(wpDialog.htmlStart(null));
result.append(wpDialog.bodyStart("dialog", null));
result.append(wpDialog.dialogStart());
result.append(wpDialog.dialogContentStart(wpDialog.getParamTitle()));

result.append(wpDialog.dialogBlockStart(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_1)));
result.append("<table cellspacing='0' cellpadding='0' class='xmlTable'>\n");
result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_OS), ""+osBean.getName()));
result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_JAVAVERSION), ""+runtimeBean.getVmVersion()));

Date date = new Date(runtimeBean.getUptime());
java.text.SimpleDateFormat simpleFormatH = new java.text.SimpleDateFormat("HH");
java.text.SimpleDateFormat simpleFormatM = new java.text.SimpleDateFormat("mm");
java.text.SimpleDateFormat simpleFormatS = new java.text.SimpleDateFormat("ss");
String jvmuptimestring = simpleFormatH.format(date) + "h " + simpleFormatM.format(date) + "min " + simpleFormatS.format(date) + "s ";

result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_JVMUPTIME), ""+jvmuptimestring ));

date = new Date(runtimeBean.getStartTime());
String jvmstarttimestring = simpleFormatH.format(date) + "h " + simpleFormatM.format(date) + "min " + simpleFormatS.format(date) + "s ";

result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_JVMSTARTTIME), ""+jvmstarttimestring));
result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_CPUNUMBER), ""+osBean.getAvailableProcessors()));
result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_CPUUSAGE), ""+100*osBean.getSystemLoadAverage()+"%"));
result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_COUNTLOADEDCLASSES), ""+classesBean.getLoadedClassCount()));
result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_COUNTUNLOADEDCLASSES), ""+classesBean.getUnloadedClassCount()));
result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_COUNTTOTALLOADEDCLASSES), ""+classesBean.getTotalLoadedClassCount()));
result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_COUNTTHREADS), ""+threadBean.getThreadCount()));
result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_COUNTTHREADSSTARTED), ""+threadBean.getTotalStartedThreadCount()));
result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_THREADSPEAK), ""+threadBean.getPeakThreadCount()));
result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_THREADSDAEMON), ""+threadBean.getDaemonThreadCount()));
result.append("</table>\n");
result.append(wpDialog.dialogBlockEnd());

result.append(wpDialog.dialogSpacer());

result.append(wpDialog.dialogBlockStart(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_2)));
result.append("<table cellspacing='0' cellpadding='0' class='xmlTable'>\n");
//result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_USAGE), ""));
//result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_MAXIMAL), ""));
//result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_AVERAGE), ""));
//result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_CURRENT), ""));
for(java.lang.management.MemoryPoolMXBean item : ManagementFactory.getMemoryPoolMXBeans())  {
    java.lang.management.MemoryUsage mu = item.getUsage();
    String name = item.getName();
    String idname = name;
    if(name.contains("Perm")) { idname = "perm"; } 
    else if(name.contains("Eden")) { idname = "eden"; } 
    else if(name.contains("Survivor")) { idname = "survivor"; } 
    else if(name.contains("Old")) { idname = "old"; } 
    else { continue; } 
    
    result.append(writeRow("<b>" + name + "</b>", ""));
    if(idname.equals("heap")){
    	result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_HEAPFREE), ""+Runtime.getRuntime().freeMemory()));
    	result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_HEAPTOTAL), ""+Runtime.getRuntime().totalMemory()));
    	result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_HEAPMAX), ""+Runtime.getRuntime().maxMemory()));
    }else if(idname.equals("physical")){
    	result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_PHYSICALFREE), ""+sunOsBean.getFreePhysicalMemorySize()));
    	result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_PHYSICALTOTAL), ""+sunOsBean.getTotalPhysicalMemorySize()));
    }else if(idname.equals("swap")){
    	result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_SWAPFREE), ""+sunOsBean.getFreeSwapSpaceSize()));
    	result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_SWAPTOTAL), ""+sunOsBean.getTotalSwapSpaceSize()));
    }else{
    	result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_USED), ""+mu.getUsed()));
    	result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_COMMITED), ""+mu.getCommitted()));
    	result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_LABEL_MAX), ""+mu.getMax()));
    }
}
result.append("</table>\n");
result.append(wpDialog.dialogBlockEnd());


result.append(wpDialog.dialogSpacer());

result.append(wpDialog.dialogBlockStart(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_OVERVIEW_ADMIN_TOOL_LABEL_3)));
result.append("<table cellspacing='0' cellpadding='0' class='xmlTable'>\n");

boolean anError = false;
String error = "";
Map configParameter = null;
org.opencms.configuration.CmsConfigurationManager myconfig = null;
myconfig = new org.opencms.configuration.CmsConfigurationManager(org.opencms.main.OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf(org.opencms.main.CmsSystemInfo.FOLDER_CONFIG_DEFAULT));
org.opencms.configuration.CmsParameterConfiguration propertyConfiguration = null;
String path = null;
try {
		path = org.opencms.main.OpenCms.getSystemInfo().getConfigurationFileRfsPath();
		result.append(writeRow("opencms.properties path", ""+path));
    propertyConfiguration = new org.opencms.configuration.CmsParameterConfiguration(path);
    myconfig.setConfiguration(propertyConfiguration); 
} catch (Exception e) {
  	anError = true;
		error += e.getMessage().toString() + " (" + path + ")";     
}
configParameter = myconfig.getConfiguration();

List poolsName = new ArrayList();
String poolsNameProperty = (String) configParameter.get("db.pools");
String[] poolNames = poolsNameProperty.split(",");
for(int i =0; i< poolNames.length; i++){
  if(poolNames[i] != null){
    poolsName.add(poolNames[i]);
  }
}

org.opencms.db.CmsSqlManager sqlM = org.opencms.main.OpenCms.getSqlManager() ;
String urlDefault = sqlM.getDefaultDbPoolName();
result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_DBPOOL_ADMIN_TOOL_LABEL_URLDEFAULT), ""+urlDefault));

Iterator poolIterator =  poolsName.iterator();
while(poolIterator.hasNext()){
  String poolName = (String)poolIterator.next();
  if(poolName != null){
	  	Iterator poolsURLIterator = sqlM.getDbPoolUrls().iterator();
      while(poolsURLIterator.hasNext()){
        String poolURL = (String) poolsURLIterator.next();
        if(poolURL != null && poolURL.endsWith(poolName)){
        		String url = poolURL;
        	  int activeConnections = sqlM.getActiveConnections(url);
        	  int idleConnections = sqlM.getIdleConnections(url);
        	  
        	  String poolStrategyProperty = (String) configParameter.get("db.pool."+poolName+".whenExhaustedAction");
        	  String maxActivesConfiguratedString = (String)configParameter.get("db.pool."+poolName+".maxActive");
        	  
        	  float pourcentage = (activeConnections * 100f)/(1f* (new Integer(maxActivesConfiguratedString)));
        	  
        	  java.sql.Connection conn = null;
        	  long timeBeforeRequest = 0;
        	  long timeAfterRequest = 0;
        		try{
        			conn = sqlM.getConnection(poolName);
        		
        			/*Get list of tables*/
        			String showTableQuery = "select * from CMS_PROJECTS;";
        			java.sql.PreparedStatement stmt = null;
        			stmt = conn.prepareStatement(showTableQuery);
        			java.sql.ResultSet resultset = null;
        			try{
        				 timeBeforeRequest = System.currentTimeMillis();
        				 resultset = stmt.executeQuery(); 
        				 timeAfterRequest = System.currentTimeMillis();
        			}catch(java.sql.SQLException e){ 
        				 anError = true;
        				 error += e.getMessage().toString();
        			}
        		}catch(Exception e){
        			anError = true;
        			error += e.getMessage().toString();     
        		}finally{
        			//conn.close();
        		}
        		
        		result.append(writeRow("<b>" + Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_DBPOOL_ADMIN_TOOL_LABEL_URL) + "</b>", "<b>"+url+"</b>"));
       		  if(poolStrategyProperty != null){
       		  	result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_DBPOOL_ADMIN_TOOL_LABEL_STRATEGY), ""+poolStrategyProperty));
       		  }
       		  if(maxActivesConfiguratedString != null){
       			  try{ 
       		  		result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_DBPOOL_ADMIN_TOOL_LABEL_MAXSIZE), ""+Integer.parseInt(maxActivesConfiguratedString)));
       			  }catch(NumberFormatException e){              
       		  	}
       		  }
       		  result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_DBPOOL_ADMIN_TOOL_LABEL_ACTIVECONNECTIONS), ""+activeConnections));
       		  result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_DBPOOL_ADMIN_TOOL_LABEL_IDLECONNECTIONS), ""+idleConnections));
       		  result.append(writeRow(Messages.get().getBundle().key(Messages.GUI_SYSTEMINFORMATION_DBPOOL_ADMIN_TOOL_LABEL_CURRENTUSAGE), ""+pourcentage+"%"));

       			if(anError){
       				result.append(writeRow("ERROR", ""+error));
       			}
        	  
        }
      }
  }
}

result.append("</table>\n");
result.append(wpDialog.dialogBlockEnd());


result.append(wpDialog.dialogContentEnd());
result.append(wpDialog.dialogEnd());
result.append(wpDialog.bodyEnd());
result.append(wpDialog.htmlEnd());

out.print(result.toString());

%>
--%>
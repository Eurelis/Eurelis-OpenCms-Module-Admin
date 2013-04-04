<%@page taglibs="c,cms,fn,fmt" language="java" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@page import="
					java.util.*,
					org.opencms.file.*,
					org.opencms.db.*,
					org.opencms.jsp.CmsJspActionElement,
					org.opencms.main.OpenCms,
					org.opencms.workplace.CmsDialog,
					com.eurelis.opencms.admin.Messages,
					com.eurelis.opencms.admin.CmsDBPoolsOverviewDialog,
					java.lang.management.ManagementFactory" %>
					
				
<%
//initialize the widget dialog
CmsDBPoolsOverviewDialog wpWidget = new CmsDBPoolsOverviewDialog(pageContext, request, response);
// perform the widget actions   
wpWidget.displayDialog(true);
if (wpWidget.isForwarded()) {
	return;
}
//write the content of widget dialog
wpWidget.writeDialog();
%>						
					
<%-- 
<% CmsJspActionElement actionElement = new CmsJspActionElement(pageContext, request, response); %>
<% CmsDialog wpDialog = new CmsDialog(pageContext, request, response); %>
<% int frequencyInMillis = 5000; %>
<% StringBuffer result = new StringBuffer(2048); %>

<c:set var="jquery"><cms:link>/system/workplace/resources/jquery/packed/jquery.js</cms:link></c:set>
<% String jquery = (String)pageContext.getAttribute("jquery"); %>

<%
boolean anError = false;
String error = "";
Map configParameter = null;
org.opencms.configuration.CmsConfigurationManager myconfig = null;
myconfig = new org.opencms.configuration.CmsConfigurationManager(org.opencms.main.OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf(org.opencms.main.CmsSystemInfo.FOLDER_CONFIG_DEFAULT));
org.opencms.configuration.CmsParameterConfiguration propertyConfiguration = null;
String path = null;
try {
		path = org.opencms.main.OpenCms.getSystemInfo().getConfigurationFileRfsPath();
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
%>


<%



result.append(wpDialog.htmlStart(null));
result.append(wpDialog.bodyStart("dialog", null));
result.append(wpDialog.dialogStart());
result.append(wpDialog.dialogContentStart(wpDialog.getParamTitle()));

// scripts
result.append("<script type='text/javascript' src='" + jquery + "'></script>");
result.append("<script type='text/javascript' src='http://code.highcharts.com/stock/highstock.js'></script>");
result.append("<script type='text/javascript' src='http://code.highcharts.com/stock/modules/exporting.js'></script>");
result.append("<script type='text/javascript'>");
result.append("$(function() {\n");
result.append("  Highcharts.setOptions({\n");
result.append("    global : { useUTC : true }\n");
result.append("  }); \n");

result.append("  function updateInfo() {\n");
result.append("    $.getJSON('json/getSystemInfo.json', function(data) {\n");
result.append("      var time = (new Date()).getTime();\n");
result.append("      var $system = data.system;\n");
result.append("      var heapMax = $system.memory.heap.max;\n");
result.append("      var heapTotal = $system.memory.heap.total;\n");
result.append("      var heapUsed = heapTotal - $system.memory.heap.free;\n");
result.append("      var totalSwapMemory = $system.memory.swap.total;\n");
result.append("      var usedSwapMemory = totalSwapMemory - $system.memory.swap.free;\n");
result.append("      window.chartHeap.series[0].addPoint([time, heapMax], true, true, true);\n");
result.append("      window.chartHeap.series[1].addPoint([time, heapTotal], true, true, true);\n");
result.append("      window.chartHeap.series[2].addPoint([time, heapUsed], true, true, true);\n");
result.append("      window.chartCpu.series[0].addPoint([time, $system.cpu.usage], true, true, true);\n");
result.append("      window.chartThreads.series[0].addPoint([time, $system.threads.counts.total], true, true, true);\n");
result.append("      window.chartThreads.series[1].addPoint([time, $system.threads.counts.daemon], true, true, true);\n");
result.append("      window.chartPerm.series[0].addPoint([time, $system.memory.perm.max], true, true, true);\n");
result.append("      window.chartPerm.series[1].addPoint([time, $system.memory.perm.committed], true, true, true);\n");
result.append("      window.chartPerm.series[2].addPoint([time, $system.memory.perm.used], true, true, true);\n");
result.append("      window.chartOld.series[0].addPoint([time, $system.memory.old.max], true, true, true);\n");
result.append("      window.chartOld.series[1].addPoint([time, $system.memory.old.committed], true, true, true);\n");
result.append("      window.chartOld.series[2].addPoint([time, $system.memory.old.used], true, true, true);\n");
result.append("      window.chartEden.series[0].addPoint([time, $system.memory.eden.max], true, true, true);\n");
result.append("      window.chartEden.series[1].addPoint([time, $system.memory.eden.committed], true, true, true);\n");
result.append("      window.chartEden.series[2].addPoint([time, $system.memory.eden.used], true, true, true);\n");
result.append("      window.chartSurvivor.series[0].addPoint([time, $system.memory.survivor.max], true, true, true);\n");
result.append("      window.chartSurvivor.series[1].addPoint([time, $system.memory.survivor.committed], true, true, true);\n");
result.append("      window.chartSurvivor.series[2].addPoint([time, $system.memory.survivor.used], true, true, true);\n");


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
        	  
        	  result.append("      window.chart"+poolName+".series[0].addPoint([time, "+activeConnections+"], true, true, true);\n");
        	  result.append("      window.chart"+poolName+".series[1].addPoint([time, "+idleConnections+"], true, true, true);\n");
        	  result.append("      window.chart"+poolName+".series[2].addPoint([time, "+pourcentage+"], true, true, true);\n");
        }
      }
  }
} 


result.append("    });\n");
result.append("  }\n");


poolIterator =  poolsName.iterator();
while(poolIterator.hasNext()){
  String poolName = (String)poolIterator.next();
  if(poolName != null){
	  
	  result.append("  window.chart"+poolName+" = new Highcharts.StockChart({\n");
	  result.append("    chart : {\n");
	  result.append("      renderTo : '" + poolName + "',\n");
	  result.append("      events : {\n");
	  result.append("      load : function() {\n");
	  result.append("          setInterval(updateInfo, "+frequencyInMillis+");\n");
	  result.append("        }\n");
	  result.append("      }\n");
	  result.append("    },\n");
	  result.append("    credits: false,\n");
	  result.append("    legend: {\n");
	  result.append("      layout: 'vertical',\n");
	  result.append("      enabled: true,\n");
	  result.append("      align: 'right',\n");
	  result.append("      verticalAlign: 'top',\n");
	  result.append("      x: -10,\n");
	  result.append("      y: 100,\n");
	  result.append("      borderWidth: 1\n");
	  result.append("    }, \n");
	  result.append("    tooltip: { \n");
	  result.append("      style: { padding: '10px' }, \n");
	  result.append("      valueDecimals : 2,\n");
	  result.append("      formatter:function() {\n");
	  result.append("        var s = '<b>Time: ' + Highcharts.dateFormat('%Y/%m/%d %H:%M:%S', this.x) + '</b><br/>';\n");
	  result.append("        $.each(this.points, function(i, point) {\n");
	  result.append("          s += '<span style=\"color:'+this.series.color+';font-weight:bold\">'+this.series.name+'</span>:<b>'+Math.round(this.point.y/1024/1024)+' Mb</b><br/>';\n");
	  result.append("        });\n");
	  result.append("        return s;\n");
	  result.append("      },\n");
	  result.append("    },\n");
	  result.append("    rangeSelector: { enabled: false },\n");
	  result.append("    exporting: { enabled: false },\n");
	  result.append("    navigator: { enabled: false },\n");
	  result.append("    title : {\n");
	  result.append("      text : '" + poolName + "'\n");
	  result.append("    },\n");
	  result.append("    series : [\n");
	  result.append("      {type : 'area', name : 'Current usage', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}, \n"); 
	  result.append("      {type : 'line', name : 'Active connections', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},\n");
	  result.append("      {type : 'line', name : 'Idle connections', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}\n");
	  result.append("    ]  \n");
	  result.append("  });\n");
	  
  }
}

result.append("});\n");
result.append("</script>");

poolIterator =  poolsName.iterator();
while(poolIterator.hasNext()){
  String poolName = (String)poolIterator.next();
  if(poolName != null){
	  result.append("<div id=\""+poolName+"\" style=\"height: 300px; width: 500px; float: left;\"></div>");
  }
}

result.append(wpDialog.dialogContentEnd());
result.append(wpDialog.dialogEnd());
result.append(wpDialog.bodyEnd());
result.append(wpDialog.htmlEnd());

out.print(result.toString());

%>

--%>		
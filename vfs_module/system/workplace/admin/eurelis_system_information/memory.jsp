<%@page taglibs="c,cms,fn,fmt" language="java" trimDirectiveWhitespaces="true" pageEncoding="UTF-8" %>
<%@page import="
					java.util.*,
					org.opencms.file.*,
					org.opencms.db.*,
					org.opencms.jsp.CmsJspActionElement,
					org.opencms.main.OpenCms,
					org.opencms.workplace.CmsDialog,
					com.eurelis.opencms.admin.Messages,
					com.eurelis.opencms.admin.CmsMemoryOverviewDialog,
					java.lang.management.ManagementFactory" %>
				
<%
//initialize the widget dialog
CmsMemoryOverviewDialog wpWidget = new CmsMemoryOverviewDialog(pageContext, request, response);
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
result.append("$(function() {");
result.append("  Highcharts.setOptions({");
result.append("    global : { useUTC : true }");
result.append("  }); ");

result.append("  function updateInfo() {");
result.append("    $.getJSON('json/getSystemInfo.json', function(data) {");
result.append("      var time = (new Date()).getTime();");
result.append("      var $system = data.system;");
result.append("      var heapMax = $system.memory.heap.max;");
result.append("      var heapTotal = $system.memory.heap.total;");
result.append("      var heapUsed = heapTotal - $system.memory.heap.free;");
result.append("      var totalSwapMemory = $system.memory.swap.total;");
result.append("      var usedSwapMemory = totalSwapMemory - $system.memory.swap.free;");
result.append("      window.chartHeap.series[0].addPoint([time, heapMax], true, true, true);");
result.append("      window.chartHeap.series[1].addPoint([time, heapTotal], true, true, true);");
result.append("      window.chartHeap.series[2].addPoint([time, heapUsed], true, true, true);");
result.append("      window.chartCpu.series[0].addPoint([time, $system.cpu.usage], true, true, true);");
result.append("      window.chartThreads.series[0].addPoint([time, $system.threads.counts.total], true, true, true);");
result.append("      window.chartThreads.series[1].addPoint([time, $system.threads.counts.daemon], true, true, true);");
result.append("      window.chartPerm.series[0].addPoint([time, $system.memory.perm.max], true, true, true);");
result.append("      window.chartPerm.series[1].addPoint([time, $system.memory.perm.committed], true, true, true);");
result.append("      window.chartPerm.series[2].addPoint([time, $system.memory.perm.used], true, true, true);");
result.append("      window.chartOld.series[0].addPoint([time, $system.memory.old.max], true, true, true);");
result.append("      window.chartOld.series[1].addPoint([time, $system.memory.old.committed], true, true, true);");
result.append("      window.chartOld.series[2].addPoint([time, $system.memory.old.used], true, true, true);");
result.append("      window.chartEden.series[0].addPoint([time, $system.memory.eden.max], true, true, true);");
result.append("      window.chartEden.series[1].addPoint([time, $system.memory.eden.committed], true, true, true);");
result.append("      window.chartEden.series[2].addPoint([time, $system.memory.eden.used], true, true, true);");
result.append("      window.chartSurvivor.series[0].addPoint([time, $system.memory.survivor.max], true, true, true);");
result.append("      window.chartSurvivor.series[1].addPoint([time, $system.memory.survivor.committed], true, true, true);");
result.append("      window.chartSurvivor.series[2].addPoint([time, $system.memory.survivor.used], true, true, true);");

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
        	  
        	  result.append("      window.chart"+poolName+".series[0].addPoint([time, "+activeConnections+"], true, true, true);");
        	  result.append("      window.chart"+poolName+".series[1].addPoint([time, "+idleConnections+"], true, true, true);");
        	  result.append("      window.chart"+poolName+".series[2].addPoint([time, "+pourcentage+"], true, true, true);");
        }
      }
  }
} 


result.append("    });");
result.append("  }");

result.append("  window.chartPerm = new Highcharts.StockChart({");
result.append("    chart : {");
result.append("      renderTo : 'perm',");
result.append("      events : {");
result.append("        load : function() {");
result.append("          setInterval(updateInfo, "+frequencyInMillis+");");
result.append("        }");
result.append("      }");
result.append("    },");
result.append("    credits: false,");
result.append("    legend: {");
result.append("      layout: 'vertical',");
result.append("      enabled: true,");
result.append("      align: 'right',");
result.append("      verticalAlign: 'top',");
result.append("      x: -10,");
result.append("      y: 100,");
result.append("      borderWidth: 1");
result.append("    }, ");
result.append("    tooltip: { ");
result.append("      style: { padding: '10px' }, ");
result.append("      valueDecimals : 2,");
result.append("      formatter:function() {");
result.append("        var s = '<b>Time: ' + Highcharts.dateFormat('%Y/%m/%d %H:%M:%S', this.x) + '</b><br/>';");
result.append("        $.each(this.points, function(i, point) {");
result.append("          s += '<span style=\"color:'+this.series.color+';font-weight:bold\">'+this.series.name+'</span>:<b>'+Math.round(this.point.y/1024/1024)+' Mb</b><br/>';");
result.append("        });");
result.append("        return s;");
result.append("      },");
result.append("    },");
result.append("    rangeSelector: { enabled: false },");
result.append("    exporting: { enabled: false },");
result.append("    navigator: { enabled: false },");
result.append("    title : {");
result.append("      text : 'Perm Gen'");
result.append("    },");
result.append("    series : [ ");
result.append("      {type : 'area', name : 'Max', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}, ");
result.append("      {type : 'area', name : 'Total', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
result.append("      {type : 'area', name : 'Used', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}");
result.append("    ] ");   
result.append("  });");

result.append("  window.chartOld = new Highcharts.StockChart({");
result.append("    chart : {");
result.append("      renderTo : 'old',");
result.append("      events : {");
result.append("        load : function() {");
result.append("          setInterval(updateInfo, "+frequencyInMillis+");");
result.append("        }");
result.append("      }");
result.append("    },");
result.append("    credits: false,");
result.append("    legend: {");
result.append("      layout: 'vertical',");
result.append("      enabled: true,");
result.append("      align: 'right',");
result.append("      verticalAlign: 'top',");
result.append("      x: -10,");
result.append("      y: 100,");
result.append("      borderWidth: 1");
result.append("    }, ");
result.append("    tooltip: { ");
result.append("      style: { padding: '10px' }, ");
result.append("      valueDecimals : 2,");
result.append("      formatter:function() {");
result.append("        var s = '<b>Time: ' + Highcharts.dateFormat('%Y/%m/%d %H:%M:%S', this.x) + '</b><br/>';");
result.append("        $.each(this.points, function(i, point) {");
result.append("          s += '<span style=\"color:'+this.series.color+';font-weight:bold\">'+this.series.name+'</span>:<b>'+Math.round(this.point.y/1024/1024)+' Mb</b><br/>';");
result.append("        });");
result.append("        return s;");
result.append("      },");
result.append("    },");
result.append("    rangeSelector: { enabled: false },");
result.append("    exporting: { enabled: false },");
result.append("    navigator: { enabled: false },");
result.append("    title : {");
result.append("      text : 'Old Gen'");
result.append("    },");
result.append("    series : [");
result.append("      {type : 'area', name : 'Max', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},  ");
result.append("      {type : 'area', name : 'Total', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
result.append("      {type : 'area', name : 'Used', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}");
result.append("    ]       "); 
result.append("  });");

result.append("  window.chartEden = new Highcharts.StockChart({");
result.append("    chart : {");
result.append("      renderTo : 'eden',");
result.append("      events : {");
result.append("        load : function() {");
result.append("          setInterval(updateInfo, "+frequencyInMillis+");");
result.append("        }");
result.append("      }");
result.append("    },");
result.append("    credits: false,");
result.append("    legend: {");
result.append("      layout: 'vertical',");
result.append("      enabled: true,");
result.append("      align: 'right',");
result.append("      verticalAlign: 'top',");
result.append("      x: -10,");
result.append("      y: 100,");
result.append("      borderWidth: 1");
result.append("    }, ");
result.append("    tooltip: { ");
result.append("      style: { padding: '10px' }, ");
result.append("      valueDecimals : 2,");
result.append("      formatter:function() {");
result.append("        var s = '<b>Time: ' + Highcharts.dateFormat('%Y/%m/%d %H:%M:%S', this.x) + '</b><br/>';");
result.append("        $.each(this.points, function(i, point) {");
result.append("          s += '<span style=\"color:'+this.series.color+';font-weight:bold\">'+this.series.name+'</span>:<b>'+Math.round(this.point.y/1024/1024)+' Mb</b><br/>';");
result.append("        });");
result.append("        return s;");
result.append("      },");
result.append("    },");
result.append("    rangeSelector: { enabled: false },");
result.append("    exporting: { enabled: false },");
result.append("    navigator: { enabled: false },");
result.append("    title : {");
result.append("      text : 'Eden Space'");
result.append("    },");
result.append("    series : [");
result.append("      {type : 'area', name : 'Max', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}, "); 
result.append("      {type : 'area', name : 'Total', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
result.append("      {type : 'area', name : 'Used', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}");
result.append("    ]  ");
result.append("  });");

result.append("  window.chartSurvivor = new Highcharts.StockChart({");
result.append("    chart : {");
result.append("      renderTo : 'survivor',");
result.append("      events : {");
result.append("      load : function() {");
result.append("          setInterval(updateInfo, "+frequencyInMillis+");");
result.append("        }");
result.append("      }");
result.append("    },");
result.append("    credits: false,");
result.append("    legend: {");
result.append("      layout: 'vertical',");
result.append("      enabled: true,");
result.append("      align: 'right',");
result.append("      verticalAlign: 'top',");
result.append("      x: -10,");
result.append("      y: 100,");
result.append("      borderWidth: 1");
result.append("    }, ");
result.append("    tooltip: { ");
result.append("      style: { padding: '10px' }, ");
result.append("      valueDecimals : 2,");
result.append("      formatter:function() {");
result.append("        var s = '<b>Time: ' + Highcharts.dateFormat('%Y/%m/%d %H:%M:%S', this.x) + '</b><br/>';");
result.append("        $.each(this.points, function(i, point) {");
result.append("          s += '<span style=\"color:'+this.series.color+';font-weight:bold\">'+this.series.name+'</span>:<b>'+Math.round(this.point.y/1024/1024)+' Mb</b><br/>';");
result.append("        });");
result.append("        return s;");
result.append("      },");
result.append("    },");
result.append("    rangeSelector: { enabled: false },");
result.append("    exporting: { enabled: false },");
result.append("    navigator: { enabled: false },");
result.append("    title : {");
result.append("      text : 'Survivor'");
result.append("    },");
result.append("    series : [");
result.append("      {type : 'area', name : 'Current usage', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}, "); 
result.append("      {type : 'line', name : 'Active connections', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
result.append("      {type : 'line', name : 'Idle connections', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}");
result.append("    ]  ");
result.append("  });");

result.append("});");
result.append("</script>");

result.append("<div id=\"perm\" style=\"height: 300px; width: 500px; float: left;\"></div>");
result.append("<div id=\"old\" style=\"height: 300px; width: 500px; float: left;\"></div>");
result.append("<div id=\"eden\" style=\"height: 300px; width: 500px; float: left;\"></div>");
result.append("<div id=\"survivor\" style=\"height: 300px; width: 500px; float: left;\"></div>");

result.append(wpDialog.dialogContentEnd());
result.append(wpDialog.dialogEnd());
result.append(wpDialog.bodyEnd());
result.append(wpDialog.htmlEnd());

out.print(result.toString());

%>
 --%>
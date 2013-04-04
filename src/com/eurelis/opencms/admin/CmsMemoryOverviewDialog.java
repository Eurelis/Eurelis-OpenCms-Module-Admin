package com.eurelis.opencms.admin;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsLog;
import org.opencms.util.CmsStringUtil;
import org.opencms.widgets.CmsDisplayWidget;
import org.opencms.workplace.CmsWidgetDialog;
import org.opencms.workplace.CmsWidgetDialogParameter;

/**
 * The system infos overview dialog.<p>
 * 
 */
public class CmsMemoryOverviewDialog extends CmsWidgetDialog {
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsMemoryOverviewDialog.class);

    /** localized messages Keys prefix. */
    public static final String KEY_PREFIX = "memory.stats";

    /** Defines which pages are valid for this dialog. */
    public static final String[] PAGES = {"page1"};

    
    
    
    private int frequencyInMillis = 5000;
    private String jsonPath = getJsp().link("/system/workplace/admin/eurelis_system_information/json/getSystemInfo.json");


    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsMemoryOverviewDialog(CmsJspActionElement jsp) {

        super(jsp);

    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsMemoryOverviewDialog(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * Commits the edited group to the db.<p>
     */
    public void actionCommit() {

        // no saving needed
        setCommitErrors(new ArrayList());
    }

    
    

    /**
     * Creates the dialog HTML for all defined widgets of the named dialog (page).<p>
     * 
     * This overwrites the method from the super class to create a layout variation for the widgets.<p>
     * 
     * @param dialog the dialog (page) to get the HTML for
     * @return the dialog HTML for all defined widgets of the named dialog (page)
     */
    protected String createDialogHtml(String dialog) {

        StringBuffer result = new StringBuffer(1024);

        // create widget table
        result.append(createWidgetTableStart());

        // show error header once if there were validation errors
        result.append(createWidgetErrorHeader());

        if (dialog.equals(PAGES[0])) {
            // create the widgets for the first dialog page
            int count = 0;
            for(java.lang.management.MemoryPoolMXBean item : ManagementFactory.getMemoryPoolMXBeans())  {
                java.lang.management.MemoryUsage mu = item.getUsage();
                String name = item.getName();
                
                result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_MEMORY_ADMIN_TOOL_BLOCK_)+name.toUpperCase()));
                result.append(createWidgetTableStart());
                result.append(createDialogRowsHtml(count, count+2));
                result.append(createWidgetTableEnd());
                result.append(dialogBlockEnd());
                
                count = count + 3;
            }
        }

        // close widget table
        result.append(createWidgetTableEnd());
        
        
        // add highstock scripts
        result.append("<script type='text/javascript' src='" + getJsp().link("/system/workplace/resources/jquery/packed/jquery.js") + "'></script>\n");
        result.append("<script type='text/javascript' src='http://code.highcharts.com/stock/highstock.js'></script>\n");
        result.append("<script type='text/javascript' src='http://code.highcharts.com/stock/modules/exporting.js'></script>\n");
        
        // add graphs building scripts
        result.append("<script type='text/javascript'>\n");
        result.append("$(function() {\n");
        result.append("  Highcharts.setOptions({\n");
        result.append("    global : { useUTC : true }\n");
        result.append("  });\n");
        result.append(getUpdateInfoFunction());
        
        result.append(getHighChartPerm());
        result.append(getHighChartOld());
        result.append(getHighChartEden());
        result.append(getHighChartSurvivor());

        result.append("});\n");
        result.append("</script>\n");
        
        result.append("<div id=\"perm\" style=\"height: 300px; width: 25%; float: left;\"></div>\n");
        result.append("<div id=\"old\" style=\"height: 300px; width: 25%; float: left;\"></div>\n");
        result.append("<div id=\"eden\" style=\"height: 300px; width: 25%; float: left;\"></div>\n");
        result.append("<div id=\"survivor\" style=\"height: 300px; width: 25%; float: left;\"></div>\n");

        return result.toString();
    }

    /**
     * @see org.opencms.workplace.CmsWidgetDialog#defaultActionHtmlEnd()
     */
    protected String defaultActionHtmlEnd() {

        return "";
    }

    /**
     * Creates the list of widgets for this dialog.<p>
     */
    protected void defineWidgets() {

        // initialize the cache object to use for the dialog
        initInfosObject();

        setKeyPrefix(KEY_PREFIX);

        // widgets to display
        int count = 0;
        int countItem = 0;
        for(java.lang.management.MemoryPoolMXBean item : ManagementFactory.getMemoryPoolMXBeans())  {
            java.lang.management.MemoryUsage mu = item.getUsage();
            String name = item.getName();
            
            LOG.debug("addWidget... Used = " + mu.getUsed() + " Commited = " + mu.getCommitted() + " Max = " + mu.getMax());
            addWidget(new CmsWidgetDialogParameter(
            			""+mu.getUsed(), ""+mu.getUsed(), 
            			"used" + countItem , 
            			new CmsDisplayWidget(), "",
            			1, 1, count));
            addWidget(new CmsWidgetDialogParameter(
        			""+mu.getCommitted(), ""+mu.getCommitted(), 
        			"commited" + countItem , 
        			new CmsDisplayWidget(), "",
        			1, 1, count + 1));
            addWidget(new CmsWidgetDialogParameter(
        			""+mu.getMax(), ""+mu.getMax(), 
        			"max" + countItem , 
        			new CmsDisplayWidget(), "",
        			1, 1, count + 2));
            
            /*if(idname.equals("heap")){
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
            	
            }*/
            
            count = count + 3;
            countItem++;
        }
    }

    /**
     * @see org.opencms.workplace.CmsWidgetDialog#getPageArray()
     */
    protected String[] getPageArray() {

        return PAGES;
    }

    /**
     * Initializes the infos object.<p>
     */
    protected void initInfosObject() {
    	
    	com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
    	java.lang.management.OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    	java.lang.management.ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    	java.lang.management.RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean(); 
    	java.lang.management.ClassLoadingMXBean classesBean = ManagementFactory.getClassLoadingMXBean(); 
    	
    	
    	
    	
    	
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initMessages()
     */
    protected void initMessages() {

        // add specific dialog resource bundle
        addMessages(Messages.get().getBundleName());
        // add default resource bundles
        super.initMessages();
    }

    /**
     * Overridden to set the online help path for this dialog.<p>
     * 
     * @see org.opencms.workplace.CmsWorkplace#initWorkplaceMembers(org.opencms.jsp.CmsJspActionElement)
     */
    protected void initWorkplaceMembers(CmsJspActionElement jsp) {

        super.initWorkplaceMembers(jsp);
        setOnlineHelpUriCustom("/eurelis_system_information/");
    }
    
    
    
    
    
    protected StringBuffer getUpdateInfoFunction(){
    	
    	StringBuffer result = new StringBuffer(1024);
    	result.append("  function updateInfo() {\n");
        result.append("    $.getJSON('"+jsonPath+"', function(data) {\n");
        result.append("      console.log('updateInfo!!');\n");
        result.append("      var time = (new Date()).getTime();\n");
        result.append("      var $system = data.system;\n");
        result.append("      var heapMax = $system.memory.heap.max;\n");
        result.append("      var heapTotal = $system.memory.heap.total;\n");
        result.append("      var heapUsed = heapTotal - $system.memory.heap.free;\n");
        result.append("      var totalSwapMemory = $system.memory.swap.total;\n");
        result.append("      var usedSwapMemory = totalSwapMemory - $system.memory.swap.free;\n");
        
        /*result.append("      if(window.chartHeap) window.chartHeap.series[0].addPoint([time, heapMax], true, true, true); \n");
        result.append("      if(window.chartHeap) window.chartHeap.series[1].addPoint([time, heapTotal], true, true, true); \n");
        result.append("      if(window.chartHeap) window.chartHeap.series[2].addPoint([time, heapUsed], true, true, true); \n");
        result.append("      if(window.chartCpu) window.chartCpu.series[0].addPoint([time, $system.cpu.usage], true, true, true); \n");
        result.append("      if(window.chartThreads) window.chartThreads.series[0].addPoint([time, $system.threads.counts.total], true, true, true);\n");
        result.append("      if(window.chartThreads) window.chartThreads.series[1].addPoint([time, $system.threads.counts.daemon], true, true, true);\n");
        result.append("      if(window.chartClasses) window.chartClasses.series[0].addPoint([time, $system.classes.loaded], true, true, true);\n");
        result.append("      if(window.chartClasses) window.chartClasses.series[1].addPoint([time, $system.classes.unloaded], true, true, true);\n");
        result.append("      if(window.chartClasses) window.chartClasses.series[2].addPoint([time, $system.classes.totalloaded], true, true, true);\n");*/
        result.append("      /*if(window.chartPerm)*/ window.chartPerm.series[0].addPoint([time, $system.memory.perm.max], true, true, true);\n");
        result.append("      /*if(window.chartPerm)*/ window.chartPerm.series[1].addPoint([time, $system.memory.perm.committed], true, true, true);\n");
        result.append("      /*if(window.chartPerm)*/ window.chartPerm.series[2].addPoint([time, $system.memory.perm.used], true, true, true);\n");
        result.append("      /*if(window.chartOld)*/ window.chartOld.series[0].addPoint([time, $system.memory.old.max], true, true, true);\n");
        result.append("      /*if(window.chartOld)*/ window.chartOld.series[1].addPoint([time, $system.memory.old.committed], true, true, true);\n");
        result.append("      /*if(window.chartOld)*/ window.chartOld.series[2].addPoint([time, $system.memory.old.used], true, true, true);\n");
        result.append("      /*if(window.chartEden)*/ window.chartEden.series[0].addPoint([time, $system.memory.eden.max], true, true, true);\n");
        result.append("      /*if(window.chartEden)*/ window.chartEden.series[1].addPoint([time, $system.memory.eden.committed], true, true, true);\n");
        result.append("      /*if(window.chartEden)*/ window.chartEden.series[2].addPoint([time, $system.memory.eden.used], true, true, true);\n");
        result.append("      /*if(window.chartSurvivor)*/ window.chartSurvivor.series[0].addPoint([time, $system.memory.survivor.max], true, true, true);\n");
        result.append("      /*if(window.chartSurvivor)*/ window.chartSurvivor.series[1].addPoint([time, $system.memory.survivor.committed], true, true, true);\n");
        result.append("      /*if(window.chartSurvivor)*/ window.chartSurvivor.series[2].addPoint([time, $system.memory.survivor.used], true, true, true);\n");
        /*
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
        } */  	  

        result.append("    });\n");
        result.append("  }\n");
    	return result;
    	
	}
    	
    
    protected StringBuffer getHighChartPerm(){
    	
    	StringBuffer result = new StringBuffer(1024);
    	result.append("  window.chartPerm = new Highcharts.StockChart({\n");
    	result.append("    chart : {\n");
    	result.append("      renderTo : 'perm',\n");
    	result.append("      events : {\n");
    	result.append("        load : function() {\n");
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
    	result.append("      }\n");
    	result.append("    },\n");
    	result.append("    rangeSelector: { enabled: false },\n");
    	result.append("    exporting: { enabled: false },\n");
    	result.append("    navigator: { enabled: false },\n");
    	result.append("    title : {\n");
    	result.append("      text : 'Perm Gen'\n");
    	result.append("    },\n");
    	result.append("    series : [ \n");
    	result.append("      {type : 'area', name : 'Max', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}, \n");
    	result.append("      {type : 'area', name : 'Total', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}, \n");
    	result.append("      {type : 'area', name : 'Used', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()} \n");
    	result.append("    ] \n");   
    	result.append("  });\n");
    	return result;
    	
	}

    protected StringBuffer getHighChartOld(){
	
		StringBuffer result = new StringBuffer(1024);
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
		return result;
	
    }
    
    protected StringBuffer getHighChartEden(){
    	
    	StringBuffer result = new StringBuffer(1024);
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
    	return result;
    	
	}
    
    
    protected StringBuffer getHighChartSurvivor(){
    	
    	StringBuffer result = new StringBuffer(1024);
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
    	return result;
    	
    }
    
}

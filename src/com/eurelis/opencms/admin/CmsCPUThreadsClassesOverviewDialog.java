package com.eurelis.opencms.admin;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class CmsCPUThreadsClassesOverviewDialog extends CmsWidgetDialog {
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsCPUThreadsClassesOverviewDialog.class);

    /** localized messages Keys prefix. */
    public static final String KEY_PREFIX = "cputhreadsclasses.stats";

    /** Defines which pages are valid for this dialog. */
    public static final String[] PAGES = {"page1"};

    /** System infos . */
    private String m_cpuCount;
    
    /** System infos . */
    private String m_cpuUsage;
    
    /** System infos . */
    private String m_loadedClassesCount;
    
    /** System infos . */
    private String m_unloadedClassesCount;
    
    /** System infos . */
    private String m_totalLoadedClassesCount;
    
    /** System infos . */
    private String m_threadsCount;
    
    /** System infos . */
    private String m_threadsStartedCount;
    
    /** System infos . */
    private String m_threadsPeakCount;
    
    /** System infos . */
    private String m_threadsDaemonCount;
    
    /** The admin settings object that is edited on this dialog. */
    protected CmsAdminSettings m_adminSettings;
    
    
    private int frequencyInMillis = CmsAdminSettings.getSettingsIntervalValue(getCms());
    private String jsonPath = getJsp().link("/system/workplace/admin/eurelis_system_information/json/getSystemInfo.json");


    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsCPUThreadsClassesOverviewDialog(CmsJspActionElement jsp) {

        super(jsp);

    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsCPUThreadsClassesOverviewDialog(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * Commits the edited group to the db.<p>
     */
    public void actionCommit() {

    	LOG.debug("Admin settings actionCommit...");
        List errors = new ArrayList();
        setDialogObject(m_adminSettings);

        boolean enabled = m_adminSettings.getInterval() > 0;
        int interval = m_adminSettings.getInterval();
        LOG.debug("Admin settings actionCommit : m_adminSettings.getInterval() = " + interval);

        //memorisation system du parametre...
        CmsAdminSettings.setSettingsIntervalValue(getCms(), interval);

        // set the list of errors to display when saving failed
        setCommitErrors(errors);
    }

    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getCpuCount() {

        return m_cpuCount;
    }

    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getCpuUsage() {

        return m_cpuUsage;
    }

    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getLoadedClassesCount() {

        return m_loadedClassesCount;
    }

    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getUnloadedClassesCount() {

        return m_unloadedClassesCount;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getTotalLoadedClassesCount() {

        return m_totalLoadedClassesCount;
    }

    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getThreadsCount() {

        return m_threadsCount;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getThreadsStartedCount() {

        return m_threadsStartedCount;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getThreadsPeakCount() {

        return m_threadsPeakCount;
    }
    
    /**
     * Returns the .<p>
     *
     * @return the 
     */
    public String getThreadsDaemonCount() {

        return m_threadsDaemonCount;
    }

    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setCpuCount(String arg) {

        m_cpuCount = arg;
    }

    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setCpuUsage(String arg) {

        m_cpuUsage = arg;
    }

    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setLoadedClassesCount(String arg) {

        m_loadedClassesCount = arg;
    }

    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setUnloadedClassesCount(String arg) {

        m_unloadedClassesCount = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setTotalLoadedClassesCount(String arg) {

        m_totalLoadedClassesCount = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setThreadsCount(String arg) {

        m_threadsCount = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setThreadsStartedCount(String arg) {

        m_threadsStartedCount = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setThreadsPeakCount(String arg) {

        m_threadsPeakCount = arg;
    }
    
    /**
     * Sets the .<p>
     *
     * @param arg the  to set
     */
    public void setThreadsDaemonCount(String arg) {

        m_threadsDaemonCount = arg;
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
        	result.append(createDialogRowsHtml(0, 0));
        	
            // create the widgets for the first dialog page
            result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_CPU_ADMIN_TOOL_BLOCK_1)));
            result.append(createWidgetTableStart());
            result.append(createDialogRowsHtml(1, 2));
            result.append(createWidgetTableEnd());
            result.append(dialogBlockEnd());
            
            result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_CPU_ADMIN_TOOL_BLOCK_2)));
            result.append(createWidgetTableStart());
            result.append(createDialogRowsHtml(3, 5));
            result.append(createWidgetTableEnd());
            result.append(dialogBlockEnd());
            
            result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_CPU_ADMIN_TOOL_BLOCK_3)));
            result.append(createWidgetTableStart());
            result.append(createDialogRowsHtml(6, 9));
            result.append(createWidgetTableEnd());
            result.append(dialogBlockEnd());
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
        result.append("  console.log('chargement de la page');\n");
        result.append("  Highcharts.setOptions({\n");
        result.append("    global : { useUTC : true }\n");
        result.append("  }); \n");
        result.append("  console.log('options highcharts settees');\n");
        result.append(getUpdateInfoFunction());
        result.append(getHighChartCPU());
        result.append(getHighChartHeap());
        result.append(getHighChartThreads());
        result.append(getHighChartClasses());
        result.append("  console.log('graphs highcharts inities');\n");
        result.append("});\n");
        result.append("</script>\n");
        
        // add graphs divs
        result.append("<div id=\"cpu\" style=\"height: 300px; width: 50%; float: left;\"></div>\n");
        result.append("<div id=\"heap\" style=\"height: 300px; width: 50%; float: left;\"></div>\n");
        result.append("<div id=\"classes\" style=\"height: 300px; width: 50%; float: left;\"></div>\n");
        result.append("<div id=\"threads\" style=\"height: 300px; width: 50%; float: left;\"></div>\n");
        
        

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
        addWidget(new CmsWidgetDialogParameter(m_adminSettings, "interval", PAGES[0], new CmsDisplayWidget()));
        
        addWidget(new CmsWidgetDialogParameter(this, "cpuCount", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "cpuUsage", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "loadedClassesCount", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "unloadedClassesCount", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "totalLoadedClassesCount", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "threadsCount", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "threadsStartedCount", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "threadsPeakCount", PAGES[0], new CmsDisplayWidget()));
        addWidget(new CmsWidgetDialogParameter(this, "threadsDaemonCount", PAGES[0], new CmsDisplayWidget()));
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
    	
    	
    	
    	setCpuCount(""+osBean.getAvailableProcessors());
    	setCpuUsage(""+100*osBean.getSystemLoadAverage()+"%");
    	
    	setLoadedClassesCount(""+classesBean.getLoadedClassCount());
    	setUnloadedClassesCount(""+classesBean.getUnloadedClassCount());
    	setTotalLoadedClassesCount(""+classesBean.getTotalLoadedClassCount());

    	setThreadsCount(""+threadBean.getThreadCount());
    	setThreadsStartedCount(""+threadBean.getTotalStartedThreadCount());
    	setThreadsPeakCount(""+threadBean.getPeakThreadCount());
    	setThreadsDaemonCount(""+threadBean.getDaemonThreadCount());
    	
    	Object o;
        if (CmsStringUtil.isEmpty(getParamAction())) {
            o = new CmsAdminSettings();
        } else {
            // this is not the initial call, get the job object from session
            o = getDialogObject();
        }
        if (!(o instanceof CmsAdminSettings)) {
            // create a new history settings handler object
            m_adminSettings = new CmsAdminSettings();
        } else {
            // reuse html import handler object stored in session
            m_adminSettings = (CmsAdminSettings)o;
        }
    	
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
        result.append("      console.log(data);\n");
        result.append("      console.log('updateInfo!!');\n");
        result.append("      var time = (new Date()).getTime();\n");
        result.append("      var $system = data.system;\n");
        result.append("      var heapMax = $system.memory.heap.max;\n");
        result.append("      var heapTotal = $system.memory.heap.total;\n");
        result.append("      var heapUsed = heapTotal - $system.memory.heap.free;\n");
        result.append("      var totalSwapMemory = $system.memory.swap.total;\n");
        result.append("      var usedSwapMemory = totalSwapMemory - $system.memory.swap.free;\n");
        
        result.append("      /*if(window.chartHeap)*/ window.chartHeap.series[0].addPoint([time, heapMax], true, true, true); \n");
        result.append("      /*if(window.chartHeap)*/ window.chartHeap.series[1].addPoint([time, heapTotal], true, true, true); \n");
        result.append("      /*if(window.chartHeap)*/ window.chartHeap.series[2].addPoint([time, heapUsed], true, true, true); \n");
        result.append("      /*if(window.chartCpu)*/ window.chartCpu.series[0].addPoint([time, $system.cpu.usage], true, true, true); \n");
        result.append("      /*if(window.chartThreads)*/ window.chartThreads.series[0].addPoint([time, $system.threads.counts.total], true, true, true);\n");
        result.append("      /*if(window.chartThreads)*/ window.chartThreads.series[1].addPoint([time, $system.threads.counts.daemon], true, true, true);\n");
        result.append("      /*if(window.chartClasses)*/ window.chartClasses.series[0].addPoint([time, $system.classes.loaded], true, true, true);\n");
        result.append("      /*if(window.chartClasses)*/ window.chartClasses.series[1].addPoint([time, $system.classes.unloaded], true, true, true);\n");
        result.append("      /*if(window.chartClasses)*/ window.chartClasses.series[2].addPoint([time, $system.classes.totalloaded], true, true, true);\n");
        /*result.append("      if(window.chartPerm) window.chartPerm.series[0].addPoint([time, $system.memory.perm.max], true, true, true);\n");
        result.append("      if(window.chartPerm) window.chartPerm.series[1].addPoint([time, $system.memory.perm.committed], true, true, true);\n");
        result.append("      if(window.chartPerm) window.chartPerm.series[2].addPoint([time, $system.memory.perm.used], true, true, true);\n");
        result.append("      if(window.chartOld) window.chartOld.series[0].addPoint([time, $system.memory.old.max], true, true, true);\n");
        result.append("      if(window.chartOld) window.chartOld.series[1].addPoint([time, $system.memory.old.committed], true, true, true);\n");
        result.append("      if(window.chartOld) window.chartOld.series[2].addPoint([time, $system.memory.old.used], true, true, true);\n");
        result.append("      if(window.chartEden) window.chartEden.series[0].addPoint([time, $system.memory.eden.max], true, true, true);\n");
        result.append("      if(window.chartEden) window.chartEden.series[1].addPoint([time, $system.memory.eden.committed], true, true, true);\n");
        result.append("      if(window.chartEden) window.chartEden.series[2].addPoint([time, $system.memory.eden.used], true, true, true);\n");
        result.append("      if(window.chartSurvivor) window.chartSurvivor.series[0].addPoint([time, $system.memory.survivor.max], true, true, true);\n");
        result.append("      if(window.chartSurvivor) window.chartSurvivor.series[1].addPoint([time, $system.memory.survivor.committed], true, true, true);\n");
        result.append("      if(window.chartSurvivor) window.chartSurvivor.series[2].addPoint([time, $system.memory.survivor.used], true, true, true);\n");*/
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
    	
    
    protected StringBuffer getHighChartCPU(){
    	
    	StringBuffer result = new StringBuffer(1024);
    	result.append("  window.chartCpu = new Highcharts.StockChart({\n");
        result.append("    chart : {\n");
        result.append("      renderTo : 'cpu',\n");
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
        result.append("          s += '<span style=\"color:'+this.series.color+';font-weight:bold\">'+this.series.name+'</span>:<b>'+Math.round(this.point.y)+' %</b><br/>';\n");
        result.append("        });\n");
        result.append("        return s;\n");
        result.append("      },\n");
        result.append("    },\n");
        result.append("    rangeSelector: {\n");
        result.append("      buttons: [\n");
        result.append("        { count: 1, type: 'minute', text: '1m' },\n");
        result.append("        { count: 5, type: 'minute', text: '5m' }, \n");
        result.append("        { count: 30, type: 'minute', text: '½h' }, \n");
        result.append("        { count: 1, type: 'hour', text: '1h' },\n"); 
        result.append("        { count: 2, type: 'hour', text: '2h' }\n");
        result.append("      ],\n");
        result.append("      inputEnabled: false,\n");
        result.append("      selected: 0\n");
        result.append("    },\n");
        result.append("    exporting: { enabled: true },\n");
        result.append("    title : {\n");
        result.append("      text : 'CPU Usage'\n");
        result.append("    },\n");
        result.append("    series : [ \n");
        result.append("      {type : 'spline', name : 'CPU Usage', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}\n");
        result.append("    ] \n");   
        result.append("  });\n");
        
    	return result;
    	
    }
    
    protected StringBuffer getHighChartHeap(){
    	
    	StringBuffer result = new StringBuffer(1024);
    	result.append("  window.chartHeap = new Highcharts.StockChart({");
        result.append("    chart : {");
        result.append("      renderTo : 'heap',");
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
        result.append("    rangeSelector: {");
        result.append("      buttons: [");
        result.append("        { count: 1, type: 'minute', text: '1m' },");
        result.append("        { count: 5, type: 'minute', text: '5m' }, ");
        result.append("        { count: 30, type: 'minute', text: '½h' }, ");
        result.append("        { count: 1, type: 'hour', text: '1h' }, ");
        result.append("        { count: 2, type: 'hour', text: '2h' }");
        result.append("      ],");
        result.append("      inputEnabled: false,");
        result.append("      selected: 0");
        result.append("    },");
        result.append("    exporting: { enabled: true },");
        result.append("    title : {");
        result.append("      text : 'Heap'");
        result.append("    },");
        result.append("    series : [ ");
        result.append("      {type : 'area', name : 'Max Memory', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
        result.append("      {type : 'area', name : 'Size Memory', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
        result.append("      {type : 'area', name : 'Used Memory', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}");
        result.append("    ] ");       
        result.append("  });");
    	return result;
    	
    }
    
    protected StringBuffer getHighChartThreads(){
    	
    	StringBuffer result = new StringBuffer(1024);
    	result.append("  window.chartThreads = new Highcharts.StockChart({");
        result.append("    chart : {");
        result.append("      renderTo : 'threads',");
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
        result.append("          s += '<span style=\"color:'+this.series.color+';font-weight:bold\">'+this.series.name+'</span>:<b>'+Math.round(this.point.y)+'</b><br/>';");
        result.append("        });");
        result.append("        return s;");
        result.append("      },");
        result.append("    },");
        result.append("    rangeSelector: {");
        result.append("      buttons: [");
        result.append("        { count: 1, type: 'minute', text: '1m' },");
        result.append("        { count: 5, type: 'minute', text: '5m' }, ");
        result.append("        { count: 30, type: 'minute', text: '½h' }, ");
        result.append("        { count: 1, type: 'hour', text: '1h' }, ");
        result.append("        { count: 2, type: 'hour', text: '2h' }");
        result.append("      ],");
        result.append("      inputEnabled: false,");
        result.append("      selected: 0");
        result.append("    },");
        result.append("    exporting: { enabled: true },");
        result.append("    title : {");
        result.append("      text : 'Threads'");
        result.append("    },");
        result.append("    series : [ ");
        result.append("      {type : 'line', name : 'Live threads', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
        result.append("      {type : 'line', name : 'Daemon threads', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}");
        result.append("    ]  ");      
        result.append("  });");
    	return result;
    	
    }
    
    protected StringBuffer getHighChartClasses(){
    	
    	StringBuffer result = new StringBuffer(1024);
    	result.append("  window.chartClasses = new Highcharts.StockChart({");
        result.append("    chart : {");
        result.append("      renderTo : 'classes',");
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
        result.append("          s += '<span style=\"color:'+this.series.color+';font-weight:bold\">'+this.series.name+'</span>:<b>'+Math.round(this.point.y)+'</b><br/>';");
        result.append("        });");
        result.append("        return s;");
        result.append("      },");
        result.append("    },");
        result.append("    rangeSelector: {");
        result.append("      buttons: [");
        result.append("        { count: 1, type: 'minute', text: '1m' },");
        result.append("        { count: 5, type: 'minute', text: '5m' }, ");
        result.append("        { count: 30, type: 'minute', text: '½h' }, ");
        result.append("        { count: 1, type: 'hour', text: '1h' }, ");
        result.append("        { count: 2, type: 'hour', text: '2h' }");
        result.append("      ],");
        result.append("      inputEnabled: false,");
        result.append("      selected: 0");
        result.append("    },");
        result.append("    exporting: { enabled: true },");
        result.append("    title : {");
        result.append("      text : 'Classes'");
        result.append("    },");
        result.append("    series : [ ");
        result.append("      {type : 'line', name : 'Loaded classes', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
        result.append("      {type : 'line', name : 'Unloaded classes', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
        result.append("      {type : 'line', name : 'Total loaded classes', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}");
        result.append("    ]  ");      
        result.append("  });");
    	return result;
    	
    }
    
}

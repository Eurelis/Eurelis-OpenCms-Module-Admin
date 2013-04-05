package com.eurelis.opencms.admin;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.db.CmsDbException;
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
public class CmsDBPoolsOverviewDialog extends CmsWidgetDialog {
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsDBPoolsOverviewDialog.class);

    /** localized messages Keys prefix. */
    public static final String KEY_PREFIX = "dbpools.stats";

    /** Defines which pages are valid for this dialog. */
    public static final String[] PAGES = {"page1"};

    private Map m_configParameter = null;
    private List m_poolsName = null;
    
    private int frequencyInMillis = CmsAdminSettings.getSettingsIntervalValue(getCms());
    private String jsonPath = getJsp().link("/system/workplace/admin/eurelis_system_information/json/getSystemInfo.json");


    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsDBPoolsOverviewDialog(CmsJspActionElement jsp) {

        super(jsp);

    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsDBPoolsOverviewDialog(PageContext context, HttpServletRequest req, HttpServletResponse res) {

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

        if(m_poolsName==null || m_configParameter==null){
        	initPools();
        }
        
        if (dialog.equals(PAGES[0])) {
            // create the widgets for the first dialog page
        	if(m_poolsName!=null && m_configParameter!=null){
        		LOG.debug("createDialogHtml... " + m_poolsName.size() + " pool(s)");
                int count = 0;
                Iterator it = m_poolsName.iterator();
                while(it.hasNext()){
                    String poolName = (String)it.next();
                    
                    result.append(dialogBlockStart(key(Messages.GUI_SYSTEMINFORMATION_DBPOOLS_ADMIN_TOOL_BLOCK_)+poolName));
                    result.append(createWidgetTableStart());
                    result.append(createDialogRowsHtml(count, count+4));
                    result.append(createWidgetTableEnd());
                    result.append(dialogBlockEnd());
                    
                    count = count + 5;
                }
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
        
        if(m_poolsName!=null && m_configParameter!=null){
	        Iterator it = m_poolsName.iterator();
	        while(it.hasNext()){
	            String poolName = (String)it.next();
	            result.append(getHighChartPool(poolName));
	        }
        }

        result.append("});\n");
        result.append("</script>\n");
        
        
        if(m_poolsName!=null && m_configParameter!=null){
	        Iterator it = m_poolsName.iterator();
	        while(it.hasNext()){
	            String poolName = (String)it.next();
	            result.append("<div id=\""+poolName+"\" style=\"height: 300px; width: 25%; float: left;\"></div>\n");
	        }
        }

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
        
        org.opencms.db.CmsSqlManager sqlM = org.opencms.main.OpenCms.getSqlManager() ;

        // widgets to display
        if(m_poolsName==null || m_configParameter==null){
        	initPools();
        }
        
        if(sqlM==null){
        	LOG.error("CmsSqlManager null");
        }
        
        if(m_poolsName!=null && m_configParameter!=null && sqlM!=null){
        	int count = 0;
            int countItem = 0;
            Iterator it = m_poolsName.iterator();
            while(it.hasNext()){
                String poolName = (String)it.next();
                LOG.debug("defineWidgets... poolName = " + poolName);
                
                if(sqlM.getDbPoolUrls()!=null){
                	Iterator poolsURLIterator = sqlM.getDbPoolUrls().iterator();
                    while(poolsURLIterator.hasNext()){
                    	String poolURL = (String) poolsURLIterator.next();
                    	if(poolURL != null && poolURL.endsWith(poolName)){
                    		LOG.debug("defineWidgets... poolURL = " + poolURL);
                      	  	String url = poolURL;
                      	  	int activeConnections = 0;
                      	  	int idleConnections = 0;
                      	  	try {
                      	  		activeConnections = sqlM.getActiveConnections(url);
                      	  		LOG.debug("defineWidgets... activeConnections = " + activeConnections);
                      	  		
                      	  		idleConnections = sqlM.getIdleConnections(url);
                      	  		LOG.debug("defineWidgets... idleConnections = " + idleConnections);
                      	  		
                      	  		String poolStrategyProperty = (String) m_configParameter.get("db.pool."+poolName+".whenExhaustedAction");
                      	  		if(poolStrategyProperty==null) poolStrategyProperty = "";
                      	  		LOG.debug("defineWidgets... poolStrategyProperty = " + poolStrategyProperty);
                      	  		
                      	  		String maxActivesConfiguratedString = (String) m_configParameter.get("db.pool."+poolName+".maxActive");
                      	  		if(maxActivesConfiguratedString==null) maxActivesConfiguratedString = "";
                      	  		LOG.debug("defineWidgets... maxActivesConfiguratedString = " + maxActivesConfiguratedString);
                        	  
                      	  		float pourcentage = (activeConnections * 100f)/(1f* (new Integer(maxActivesConfiguratedString)));
                        	  
                      	  		LOG.debug("defineWidgets... pourcentage=" + pourcentage + "%");
                      	  		
                      	  		/*java.sql.Connection conn = null;
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
                        				 LOG.error(e.getMessage(),e);
                        			}
                        		}catch(Exception e){
                        			LOG.error(e.getMessage(),e);
                        		}finally{
                        			//conn.close();
                        		}*/
                        		
                        		
                        		addWidget(new CmsWidgetDialogParameter(
                          			""+poolStrategyProperty, ""+poolStrategyProperty, 
                          			"strategy" + countItem , 
                          			new CmsDisplayWidget(), "",
                          			1, 1, count));
                        		addWidget(new CmsWidgetDialogParameter(
                          			""+Integer.parseInt(maxActivesConfiguratedString), ""+Integer.parseInt(maxActivesConfiguratedString), 
                          			"maxsize" + countItem , 
                          			new CmsDisplayWidget(), "",
                          			1, 1, count + 1));
                        		addWidget(new CmsWidgetDialogParameter(
                          			""+activeConnections, ""+activeConnections, 
                          			"activeconnections" + countItem , 
                          			new CmsDisplayWidget(), "",
                          			1, 1, count + 2));
                        		addWidget(new CmsWidgetDialogParameter(
                          			""+idleConnections, ""+idleConnections, 
                          			"idleconnections" + countItem , 
                          			new CmsDisplayWidget(), "",
                          			1, 1, count + 3));
                        		addWidget(new CmsWidgetDialogParameter(
                          			""+pourcentage+"%", ""+pourcentage+"%", 
                          			"currentusage" + countItem , 
                          			new CmsDisplayWidget(), "",
                          			1, 1, count + 4));
                        		
                        	  
                      	  	} catch (CmsDbException e1) {
                      	  		e1.printStackTrace();
                      	  		LOG.error(e1);
                      	  	}
                      }
                    }
                    
                    count = count + 5;
                    countItem++;
                }
                
            }
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
    	
    	
    	initPools();
    	
    	
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
        result.append("      if(window.chartClasses) window.chartClasses.series[2].addPoint([time, $system.classes.totalloaded], true, true, true);\n");
        result.append("      if(window.chartPerm) window.chartPerm.series[0].addPoint([time, $system.memory.perm.max], true, true, true);\n");
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
        
        Iterator poolIterator =  m_poolsName.iterator();
        while(poolIterator.hasNext()){
          String poolName = (String)poolIterator.next();
          if(poolName != null){
        	  result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[0].addPoint([time, $system.dbpools."+poolName+".activeConnections], true, true, true);\n");
        	  result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[1].addPoint([time, $system.dbpools."+poolName+".idleConnections], true, true, true);\n");
        	  result.append("      /*if(window.chart"+poolName+")*/ window.chart"+poolName+".series[2].addPoint([time, $system.dbpools."+poolName+".pourcentage], true, true, true);\n");
          }
        }   	  

        result.append("    });\n");
        result.append("  }\n");
    	return result;
    	
	}
    	
    
    protected StringBuffer getHighChartPool(String poolName){
    	
    	StringBuffer result = new StringBuffer(1024);
    	
    	if(CmsStringUtil.isNotEmptyOrWhitespaceOnly(poolName)){
    		String name = poolName.replaceAll(" ", "").toLowerCase();
        	
        	result.append("  window.chart"+name+" = new Highcharts.StockChart({");
        	result.append("    chart : {");
        	result.append("      renderTo : '"+name+"',");
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
        	result.append("      text : '"+poolName+"'");
        	result.append("    },");
        	result.append("    series : [ ");
        	result.append("      {type : 'area', name : 'Max', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}, ");
        	result.append("      {type : 'area', name : 'Total', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()},");
        	result.append("      {type : 'area', name : 'Used', data: (function(){var data=[],time=(new Date()).getTime(),i;for(i=-999;i<=0;i++){data.push([time+i*1000,0]);};return data;})()}");
        	result.append("    ] ");   
        	result.append("  });");
    	}
    	
    	return result;
    	
	}

    
    
    
    
    protected void initPools(){
    	
    	String myconfigPath = org.opencms.main.OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf(org.opencms.main.CmsSystemInfo.FOLDER_CONFIG_DEFAULT);
        LOG.debug("initPools... myconfig path = " + myconfigPath) ;
        
        org.opencms.configuration.CmsConfigurationManager myconfig = null;
        myconfig = new org.opencms.configuration.CmsConfigurationManager(myconfigPath);
        
        org.opencms.configuration.CmsParameterConfiguration propertyConfiguration = null;
        String path = null;
        try {
        	path = org.opencms.main.OpenCms.getSystemInfo().getConfigurationFileRfsPath();
        	LOG.debug("initPools... configurationFileRfsPath = " + path) ;
            propertyConfiguration = new org.opencms.configuration.CmsParameterConfiguration(path);
            myconfig.setConfiguration(propertyConfiguration); 
        } catch (Exception e) {
          	LOG.error(e.getMessage() + " (" + path + ")",e);
        }
        Map configParameter = myconfig.getConfiguration();

        List poolsName = new ArrayList();
        if(configParameter!=null){
        	String poolsNameProperty = (String) configParameter.get("db.pools");
            String[] poolNames = poolsNameProperty.split(",");
            for(int i =0; i< poolNames.length; i++){
              if(poolNames[i] != null){
                poolsName.add(poolNames[i]);
                LOG.debug("initPools... Pool name = " + poolNames[i]) ;
              }
            }
        }
    	
        this.m_configParameter = configParameter;
    	this.m_poolsName = poolsName;
    }
    
    
    
}

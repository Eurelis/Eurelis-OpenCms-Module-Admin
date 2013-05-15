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

package com.eurelis.opencms.admin.fileinformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsProperty;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.lock.CmsLock;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsDateUtil;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.CmsWorkplace;
import org.opencms.workplace.list.A_CmsListDialog;
import org.opencms.workplace.list.CmsListColumnAlignEnum;
import org.opencms.workplace.list.CmsListColumnDefinition;
import org.opencms.workplace.list.CmsListDirectAction;
import org.opencms.workplace.list.CmsListItem;
import org.opencms.workplace.list.CmsListItemActionIconComparator;
import org.opencms.workplace.list.CmsListItemDefaultComparator;
import org.opencms.workplace.list.CmsListItemNumericalComparator;
import org.opencms.workplace.list.CmsListMetadata;
import org.opencms.workplace.list.CmsListMultiAction;
import org.opencms.workplace.list.CmsListOrderEnum;
import org.opencms.workplace.list.CmsListSearchAction;
import org.opencms.workplace.tools.CmsToolDialog;

import com.eurelis.opencms.admin.CmsAdminSettings;




/**
 * Internal link validation Dialog.<p>
 * 
 * @since 6.5.3 
 */
public class CmsFileInformationList extends A_CmsListDialog {

    
    /** list id constant. */
    public static final String LIST_ID = "lfi";

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsFileInformationList.class);
    
    /** List column delete. */
    public static final String LIST_COLUMN_DELETE = "cd";
    
    /** List column delete. */
    public static final String LIST_COLUMN_DELETEANDPUBLISH = "cdp";

    /** list column id constant. */
    private static final String LIST_COLUMN_TYPEICON = "ci";
    
    /** list column id constant. */
    private static final String LIST_COLUMN_LOCKICON = "cli";
    
    /** list column id constant. */
    private static final String LIST_COLUMN_PROJECTICON = "cpi";
    
    /** list column id constant. */
    private static final String LIST_COLUMN_STATE = "cst";
    
	/** List column . */
    public static final String LIST_COLUMN_PATH = "cp";
    
    /** List column . */
    public static final String LIST_COLUMN_TITLE = "ct";
    
    /** List column . */
    public static final String LIST_COLUMN_TYPE = "cty";
    
    /** List column . */
    public static final String LIST_COLUMN_SIZE = "csi";
    
    /** List column . */
    public static final String LIST_COLUMN_ROOT = "cr";
    
    /** list column id constant. */
    private static final String LIST_COLUMN_CREATIONDATE = "ccd";
    
    
    /** List action delete. */
    public static final String LIST_ACTION_DELETE = "ad";
    
    /** List action multi delete. */
    public static final String LIST_MACTION_DELETE = "md";
    
    /** List action delete and publish. */
    public static final String LIST_ACTION_DELETEANDPUBLISH = "adp";
    
    /** List action multi delete and publish. */
    public static final String LIST_MACTION_DELETEANDPUBLISH = "mdp";
    
    /** Path to the file info reports. */
    public static final String PATH_REPORTS = "/system/workplace/admin/eurelis_file_information/reports/";
    
    /** Resource parameter. */
    public static final String PARAM_RESOURCE = "resource";
    
    /** Resource parameter. */
    public static final String PARAM_FORCEDFOLDER = "forcedfolder";
    
   
    /**
     * Public constructor.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsFileInformationList(CmsJspActionElement jsp) {

        super(
                jsp,
                LIST_ID,
                Messages.get().container(Messages.GUI_FILEINFORMATION_LIST_NAME_0),
                LIST_COLUMN_PATH,
                CmsListOrderEnum.ORDER_ASCENDING,
                null);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsFileInformationList(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListMultiActions()
     */
    public void executeListMultiActions() throws IOException, ServletException {

    	if (getParamListAction().equals(LIST_MACTION_DELETE)) {
            String rscList = "";
            // execute the delete multiaction
            Iterator itItems = getSelectedItems().iterator();
            StringBuffer resources = new StringBuffer(32);
            while (itItems.hasNext()) {
                CmsListItem listItem = (CmsListItem)itItems.next();
                resources.append(listItem.getId());
                resources.append(",");
            }
            rscList = new String(resources);
            rscList = rscList.substring(0, rscList.length() - 1);
            Map params = new HashMap();
            params.put(PARAM_RESOURCE, rscList);
            params.put(PARAM_ACTION, DIALOG_INITIAL);
            params.put(PARAM_STYLE, CmsToolDialog.STYLE_NEW);
            getToolManager().jspForwardPage(this, PATH_REPORTS + "delete.jsp", params);
        }else if (getParamListAction().equals(LIST_MACTION_DELETEANDPUBLISH)) {
            String rscList = "";
            // execute the delete multiaction
            Iterator itItems = getSelectedItems().iterator();
            StringBuffer resources = new StringBuffer(32);
            while (itItems.hasNext()) {
                CmsListItem listItem = (CmsListItem)itItems.next();
                resources.append(listItem.getId());
                resources.append(",");
            }
            rscList = new String(resources);
            rscList = rscList.substring(0, rscList.length() - 1);
            Map params = new HashMap();
            params.put(PARAM_RESOURCE, rscList);
            params.put(PARAM_ACTION, DIALOG_INITIAL);
            params.put(PARAM_STYLE, CmsToolDialog.STYLE_NEW);
            getToolManager().jspForwardPage(this, PATH_REPORTS + "deleteandpublish.jsp", params);
        }
        listSave();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListSingleActions()
     */
    public void executeListSingleActions() throws IOException, ServletException {

    	String resource = getSelectedItem().getId();
        Map params = new HashMap();
        params.put(PARAM_RESOURCE, resource);
        if (getParamListAction().equals(LIST_ACTION_DELETE)) {
            // forward to the delete resource screen   
            params.put(PARAM_ACTION, DIALOG_INITIAL);
            params.put(PARAM_STYLE, CmsToolDialog.STYLE_NEW);
            getToolManager().jspForwardPage(this, PATH_REPORTS + "delete.jsp", params);
        } else if (getParamListAction().equals(LIST_ACTION_DELETEANDPUBLISH)) {
            // forward to the delete resource screen   
            params.put(PARAM_ACTION, DIALOG_INITIAL);
            params.put(PARAM_STYLE, CmsToolDialog.STYLE_NEW);
            getToolManager().jspForwardPage(this, PATH_REPORTS + "deleteandpublish.jsp", params);
        }
        listSave();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#fillDetails(java.lang.String)
     */
    protected void fillDetails(String detailId) {

        
    }
    
    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#getListItems()
     */
    protected List getListItems() {

        List ret = new ArrayList();
        
        //LOG.debug("getListItems... ");

        CmsAdminSettings settings = new CmsAdminSettings(getSession());
        
        String folder = settings.getSettingsFilesFolderValue(getCms(), getSession());
        if(CmsStringUtil.isEmptyOrWhitespaceOnly(folder)){
        	return ret;
        }
        if(!getCms().existsResource(folder)){
        	return ret;
        }
        LOG.debug("getListItems... : folder=" + folder + " ");
        
        String forcedfolder = null;
        if(getParameterMap()!=null && getParameterMap().containsKey(PARAM_FORCEDFOLDER)){
        	forcedfolder = getParameterMap().get(PARAM_FORCEDFOLDER)[0];
        }
        LOG.debug("getListItems... : forcedfolder=" + forcedfolder + " ");
        
        int minLength = settings.getSettingsFilesMinLengthValue(getCms(), getSession());
        int maxLength = settings.getSettingsFilesMaxLengthValue(getCms(), getSession());
        LOG.debug("getListItems... : minLength=" + minLength + " maxLength=" + maxLength);
        
        long createdBefore = settings.getSettingsFilesCreatedBeforeValue(getCms(), getSession());
        long createdAfter = settings.getSettingsFilesCreatedAfterValue(getCms(), getSession());
        LOG.debug("getListItems... : createdBefore=" + createdBefore + " (" + CmsDateUtil.getDateTimeShort(createdBefore) + ")" + " createdAfter=" + createdAfter + " (" + CmsDateUtil.getDateTimeShort(createdAfter) + ")");
        
        //recuperation des fichiers
        List<CmsResource> allRsc = new ArrayList<CmsResource>();
        try {
        	if(CmsStringUtil.isEmptyOrWhitespaceOnly(forcedfolder)){
        		allRsc = getCms().readResources(folder, CmsResourceFilter.ALL.addRequireFile(), true);
        		LOG.debug("getListItems... " + allRsc.size() + " files in " + folder);
        	}else{
        		String currentSiteRoot1 = getCms().getRequestContext().getSiteRoot();
            	getCms().getRequestContext().setSiteRoot("/");
        		allRsc = getCms().readResources(forcedfolder, CmsResourceFilter.ALL.addRequireFile(), true);
        		getCms().getRequestContext().setSiteRoot(currentSiteRoot1);
        		LOG.debug("getListItems... " + allRsc.size() + " files in " + forcedfolder);
        	}
        	if(allRsc==null){
        		LOG.warn("getListItems... allRsc null");
        		return ret;
        	}
        	
        	
        	//on passe sur siteroot "/"
        	String currentSiteRoot = getCms().getRequestContext().getSiteRoot();
        	getCms().getRequestContext().setSiteRoot("/");
        	
        	Iterator<CmsResource> i = allRsc.iterator();
            while (i.hasNext()) {
            	CmsResource resource = (CmsResource)i.next();
                CmsListItem item = getList().newItem(resource.getRootPath());
                
                // type
      		  	String type = OpenCms.getResourceManager().getResourceType(resource.getTypeId()).getTypeName();
                // type icon
      		  	String iconpath = OpenCms.getSystemInfo().getContextPath() + "/resources/" + CmsWorkplace.RES_PATH_FILETYPES + OpenCms.getWorkplaceManager().getExplorerTypeSetting(type).getIcon();
      		  	
      			// lock
      		  	CmsLock lock = getCms().getLock(resource);
    		  	String lockiconpath = null;
    		  	if(!lock.isUnlocked()){
    		  		lockiconpath = OpenCms.getSystemInfo().getContextPath() + "/resources/explorer/lock_other.gif";
    		  		if(lock.isDirectlyOwnedBy(getCms().getRequestContext().getCurrentUser())){
    		  			lockiconpath = OpenCms.getSystemInfo().getContextPath() + "/resources/explorer/lock_user.gif";
    		  		}
    		  		if(lock.isOwnedBy(getCms().getRequestContext().getCurrentUser()) && lock.isShared()){
    		  			lockiconpath = OpenCms.getSystemInfo().getContextPath() + "/resources/explorer/lock_shared.gif";
    		  		}
    		  	}
      		  	
      		  	// state
                String state = "";
      		  	if(resource.getState().isChanged()) 	state = getJsp().label("checkresources.label.state.changed");
      		  	if(resource.getState().isDeleted()) 	state = getJsp().label("checkresources.label.state.deleted");
      		  	if(resource.getState().isNew()) 		state = getJsp().label("checkresources.label.state.new");
      		  	if(resource.getState().isUnchanged()) 	state = getJsp().label("checkresources.label.state.unchanged");
      		  	String spancolor = "";
      		  	if(resource.getState().isChanged()) 	spancolor = "color:#B40000;";
      		  	if(resource.getState().isDeleted()) 	spancolor = "";
      		  	if(resource.getState().isNew()) 		spancolor = "color:#00A;";
      		  	if(resource.getState().isUnchanged()) 	spancolor = "";
      		  	String spanitalic = "";
      		  	if(!resource.isReleasedAndNotExpired(System.currentTimeMillis())) 	spanitalic = "font-style:italic;";
      		  	String spanlined = "";
      		  	if(resource.getState().isDeleted()) 	spanlined = "text-decoration: line-through;";
      		  	
      		  	//project
      		  	String projecticonpath = null;
    		  	if(resource.getState().isChanged()) 	projecticonpath = OpenCms.getSystemInfo().getContextPath() + "/resources/explorer/project_this.png";
    		  	if(resource.getState().isDeleted()) 	projecticonpath = OpenCms.getSystemInfo().getContextPath() + "/resources/explorer/project_this.png";
    		  	if(resource.getState().isNew()) 		projecticonpath = OpenCms.getSystemInfo().getContextPath() + "/resources/explorer/project_this.png";
    		  	if(resource.getState().isUnchanged()) 	projecticonpath = null;
                
      		  	// rootpath
                String rootPath = resource.getRootPath();
                
                // title property
                CmsProperty property = getCms().readPropertyObject(resource.getRootPath(), "Title", false);
                String title = (property!=null && property.getValue()!=null) ?  property.getValue() : "";
                
                // size
                String size = resource.getLength() + "";
                
                // root
                String siteroot = OpenCms.getSiteManager().getSiteForRootPath(resource.getRootPath()).getSiteRoot();
                CmsProperty siterootproperty = getCms().readPropertyObject(siteroot, "Title", false);
                String siteroottitle = siterootproperty!=null ? siterootproperty.getValue() : "";
                
      		  	item.set(LIST_COLUMN_TYPEICON, "<img src=\""+iconpath+"\" border=\"0\" width=\"16\" height=\"16\" alt=\"\"/>");
    		  	item.set(LIST_COLUMN_LOCKICON, lockiconpath!=null ? "<img src=\""+lockiconpath+"\" border=\"0\" width=\"16\" height=\"16\" alt=\"\"/>" : "");
    		  	item.set(LIST_COLUMN_PROJECTICON, projecticonpath!=null ? "<img src=\""+projecticonpath+"\" border=\"0\" width=\"16\" height=\"16\" alt=\"\"/>" : "");
      		  	item.set(LIST_COLUMN_STATE, "<span style=\""+spancolor+spanitalic+spanlined+"\">" + state + "</span>");
      		  	item.set(LIST_COLUMN_PATH, "<span style=\""+spancolor+spanitalic+spanlined+"\">" + rootPath + "</span>");
      		  	item.set(LIST_COLUMN_TITLE, "<span style=\""+spancolor+spanitalic+spanlined+"\">" + title + "</span>");
      		  	item.set(LIST_COLUMN_TYPE, "<span style=\""+spancolor+spanitalic+spanlined+"\">" + type + "</span>");
                item.set(LIST_COLUMN_SIZE, "<span style=\""+spancolor+spanitalic+spanlined+"\">" + size + "</span>");
                item.set(LIST_COLUMN_ROOT, "<span style=\""+spancolor+spanitalic+spanlined+"\">" + siteroottitle + "</span>");
                item.set(LIST_COLUMN_CREATIONDATE, "<span style=\""+spancolor+spanitalic+spanlined+"\">" + CmsDateUtil.getDateTimeShort(resource.getDateCreated()) + "</span>");
                
                boolean displayed = true;
                if(resource.getLength() < minLength){
                	displayed = false;
                }
                if(maxLength != -1){
                	LOG.debug("getListItems... filter on max length " + maxLength);
            		if(resource.getLength() > maxLength){
            			displayed = false;
            		}
            	}
                if(createdBefore != Long.MIN_VALUE && createdBefore != Long.MAX_VALUE && createdBefore != 0){
                	LOG.debug("getListItems... filter on creation date < " + CmsDateUtil.getDateTimeShort(createdBefore));
            		if(resource.getDateCreated() > createdBefore){
            			displayed = false;
            		}
            	}
                if(createdBefore != Long.MIN_VALUE && createdAfter != Long.MAX_VALUE && createdAfter != 0){
                	LOG.debug("getListItems... filter on creation date > " + CmsDateUtil.getDateTimeShort(createdBefore));
            		if(resource.getDateCreated() < createdAfter){
            			displayed = false;
            		}
            	}
                
                if(displayed){
                	ret.add(item);
                }
                
            }
            
            getCms().getRequestContext().setSiteRoot(currentSiteRoot);
        	
		} catch (CmsException e) {
			e.printStackTrace();
			LOG.error(e,e);
		}
        
		LOG.debug("getListItems... " + ret.size() + " files found");
        return ret;
    }
    

    /**
     * @see org.opencms.workplace.CmsWorkplace#initMessages()
     */
    protected void initMessages() {

        // add specific dialog resource bundle
        addMessages(Messages.get().getBundleName());
        super.initMessages();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setIndependentActions(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setIndependentActions(CmsListMetadata metadata) {

    	// makes the list searchable
        CmsListSearchAction searchAction = new CmsListSearchAction(metadata.getColumnDefinition(LIST_COLUMN_PATH));
        searchAction.addColumn(metadata.getColumnDefinition(LIST_COLUMN_TITLE));
        searchAction.addColumn(metadata.getColumnDefinition(LIST_COLUMN_TYPE));
        searchAction.addColumn(metadata.getColumnDefinition(LIST_COLUMN_SIZE));
        searchAction.addColumn(metadata.getColumnDefinition(LIST_COLUMN_ROOT));
        metadata.setSearchAction(searchAction);
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setMultiActions(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setMultiActions(CmsListMetadata metadata) {

    	// add the delete resource multi action
        CmsListMultiAction deleteResources = new CmsListMultiAction(LIST_MACTION_DELETE);
        deleteResources.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_ACTION_MDELETE_NAME_0));
        deleteResources.setConfirmationMessage(Messages.get().container(Messages.GUI_FILEINFORMATION_ACTION_MDELETE_CONF_0));
        deleteResources.setIconPath(ICON_MULTI_DELETE);
        deleteResources.setEnabled(true);
        deleteResources.setHelpText(Messages.get().container(Messages.GUI_FILEINFORMATION_ACTION_MDELETE_HELP_0));
        metadata.addMultiAction(deleteResources); 
        
        // add the delete and publish resource multi action
        CmsListMultiAction deleteandpublishResources = new CmsListMultiAction(LIST_MACTION_DELETEANDPUBLISH);
        deleteandpublishResources.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_ACTION_MDELETEANDPUBLISH_NAME_0));
        deleteandpublishResources.setConfirmationMessage(Messages.get().container(Messages.GUI_FILEINFORMATION_ACTION_MDELETEANDPUBLISH_CONF_0));
        deleteandpublishResources.setIconPath(ICON_MULTI_DELETE);
        deleteandpublishResources.setEnabled(true);
        deleteandpublishResources.setHelpText(Messages.get().container(Messages.GUI_FILEINFORMATION_ACTION_MDELETEANDPUBLISH_HELP_0));
        metadata.addMultiAction(deleteandpublishResources); 
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setColumns(org.opencms.workplace.list.CmsListMetadata)
     */
	protected void setColumns(CmsListMetadata metadata) {
		
		// add column for delete action
        CmsListColumnDefinition delCol = new CmsListColumnDefinition(LIST_COLUMN_DELETE);
        delCol.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_COLS_DELETE_0));
        delCol.setWidth("20");
        delCol.setSorteable(false);
        delCol.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        // direct action: delete resource
        CmsListDirectAction delResource = new CmsListDirectAction(LIST_ACTION_DELETE);
        delResource.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_ACTION_DELETE_NAME_0));
        delResource.setConfirmationMessage(Messages.get().container(Messages.GUI_FILEINFORMATION_ACTION_DELETE_CONF_0));
        delResource.setIconPath(ICON_DELETE);
        delResource.setEnabled(true);
        delResource.setHelpText(Messages.get().container(Messages.GUI_FILEINFORMATION_ACTION_DELETE_HELP_0));
        delCol.addDirectAction(delResource);
        metadata.addColumn(delCol);
        
        // add column for delete and publish action
        CmsListColumnDefinition delPubCol = new CmsListColumnDefinition(LIST_COLUMN_DELETEANDPUBLISH);
        delPubCol.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_COLS_DELETEANDPUBLISH_0));
        delPubCol.setWidth("20");
        delPubCol.setSorteable(false);
        delPubCol.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        // direct action: delete resource
        delResource = new CmsListDirectAction(LIST_ACTION_DELETEANDPUBLISH);
        delResource.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_ACTION_DELETEANDPUBLISH_NAME_0));
        delResource.setConfirmationMessage(Messages.get().container(Messages.GUI_FILEINFORMATION_ACTION_DELETEANDPUBLISH_CONF_0));
        delResource.setIconPath(ICON_DELETE);
        delResource.setEnabled(true);
        delResource.setHelpText(Messages.get().container(Messages.GUI_FILEINFORMATION_ACTION_DELETEANDPUBLISH_HELP_0));
        delPubCol.addDirectAction(delResource);
        metadata.addColumn(delPubCol);
		
		// add column icon
		CmsListColumnDefinition col = new CmsListColumnDefinition(LIST_COLUMN_TYPEICON);
		col.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_COLS_ICON_0));
		col.setWidth("20");
		col.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
		col.setPrintable(false);
        col.setSorteable(true);
        col.setListItemComparator(new CmsListItemActionIconComparator());
        col.setVisible(true);
        metadata.addColumn(col);
        
        // add column lock icon
        col = new CmsListColumnDefinition(LIST_COLUMN_LOCKICON);
        col.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_COLS_LOCKICON_0));
        col.setWidth("20");
        col.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        col.setPrintable(false);
        col.setSorteable(true);
        col.setListItemComparator(new CmsListItemActionIconComparator());
        col.setVisible(true);
        metadata.addColumn(col);
        
        // add column project icon
        col = new CmsListColumnDefinition(LIST_COLUMN_PROJECTICON);
        col.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_COLS_PROJECTICON_0));
        col.setWidth("20");
        col.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        col.setPrintable(false);
        col.setSorteable(true);
        col.setListItemComparator(new CmsListItemActionIconComparator());
        col.setVisible(true);
        metadata.addColumn(col);
        
        // add column state icon
        col = new CmsListColumnDefinition(LIST_COLUMN_STATE);
        col.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_COLS_STATE_0));
        col.setWidth("10%");
        col.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        col.setSorteable(true);
        col.setPrintable(false);
        col.setSorteable(true);
        col.setListItemComparator(new CmsListItemActionIconComparator());
        col.setVisible(false);
        metadata.addColumn(col);
		
		// add column for rootpath
        col = new CmsListColumnDefinition(LIST_COLUMN_PATH);
        col.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_LIST_COLS_ROOTPATH_0));
        col.setWidth("35%");
        col.setAlign(CmsListColumnAlignEnum.ALIGN_LEFT);
        col.setPrintable(true);
        col.setSorteable(true);
        col.setListItemComparator(new CmsListItemDefaultComparator());
        col.setVisible(true);
        metadata.addColumn(col);
        
        // add column for title
        col = new CmsListColumnDefinition(LIST_COLUMN_TITLE);
        col.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_LIST_COLS_TITLE_0));
        col.setWidth("30%");
        col.setAlign(CmsListColumnAlignEnum.ALIGN_LEFT);
        col.setPrintable(true);
        col.setSorteable(true);
        col.setListItemComparator(new CmsListItemDefaultComparator());
        col.setVisible(true);
        metadata.addColumn(col);
        
        // add column for type
        col = new CmsListColumnDefinition(LIST_COLUMN_TYPE);
        col.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_LIST_COLS_TYPE_0));
        col.setWidth("5%");
        col.setAlign(CmsListColumnAlignEnum.ALIGN_LEFT);
        col.setPrintable(true);
        col.setSorteable(true);
        col.setListItemComparator(new CmsListItemDefaultComparator());
        col.setVisible(true);
        metadata.addColumn(col);
        
        // add column for size
        col = new CmsListColumnDefinition(LIST_COLUMN_SIZE);
        col.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_LIST_COLS_SIZE_0));
        col.setWidth("5%");
        col.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        col.setPrintable(true);
        col.setSorteable(true);
        col.setListItemComparator(new CmsListItemNumericalComparator());
        col.setVisible(true);
        metadata.addColumn(col);
        
      	// add column for creation display
        col = new CmsListColumnDefinition(LIST_COLUMN_CREATIONDATE);
        col.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_COLS_CREATIONDATE_0));
        col.setWidth("5%");
        col.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        col.setPrintable(true);
        col.setSorteable(true);
        col.setListItemComparator(new CmsListItemDefaultComparator());
        col.setVisible(true);
        metadata.addColumn(col);
        
        // add column for root
        col = new CmsListColumnDefinition(LIST_COLUMN_ROOT);
        col.setName(Messages.get().container(Messages.GUI_FILEINFORMATION_LIST_COLS_ROOT_0));
        col.setWidth("10%");
        col.setAlign(CmsListColumnAlignEnum.ALIGN_LEFT);
        col.setPrintable(true);
        col.setSorteable(true);
        col.setListItemComparator(new CmsListItemDefaultComparator());
        col.setVisible(true);
        metadata.addColumn(col);
		
	}
    
    
    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#validateParamaters()
     */
    protected void validateParamaters() throws Exception {

        /*if (CmsStringUtil.isEmptyOrWhitespaceOnly(getParamFolder())) {
            // just throw a dummy exception here since getModule does not produce an exception when a 
            // module is not found
            throw new Exception();
        }*/
    }

}
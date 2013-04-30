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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.opencms.workplace.list.CmsListItem;
import org.opencms.workplace.list.CmsListItemActionIconComparator;
import org.opencms.workplace.list.CmsListItemDefaultComparator;
import org.opencms.workplace.list.CmsListItemNumericalComparator;
import org.opencms.workplace.list.CmsListMetadata;
import org.opencms.workplace.list.CmsListOrderEnum;
import org.opencms.workplace.list.CmsListSearchAction;




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

    /** list column id constant. */
    private static final String LIST_COLUMN_TYPEICON = "ci";
    
    /** list column id constant. */
    private static final String LIST_COLUMN_LOCKICON = "cli";
    
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
    public void executeListMultiActions() {

        
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListSingleActions()
     */
    public void executeListSingleActions() {

        
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
        int minLength = settings.getSettingsFilesMinLengthValue(getCms(), getSession());
        int maxLength = settings.getSettingsFilesMaxLengthValue(getCms(), getSession());
        LOG.debug("getListItems... : folder=" + folder + " minLength=" + minLength + " maxLength=" + maxLength);
        
        long createdBefore = settings.getSettingsFilesCreatedBeforeValue(getCms(), getSession());
        long createdAfter = settings.getSettingsFilesCreatedAfterValue(getCms(), getSession());
        LOG.debug("getListItems... : createdBefore=" + createdBefore + " createdAfter=" + createdAfter);
        
        //recuperation des fichiers
        List<CmsResource> allRsc = new ArrayList<CmsResource>();
        try {
        	allRsc = getCms().readResources(folder, CmsResourceFilter.DEFAULT_FILES, true);
        	if(allRsc==null){
        		LOG.warn("getListItems... allRsc null");
        		return ret;
        	}
        	LOG.debug("getListItems... " + allRsc.size() + " files");
        	
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
      		  	CmsLock lock = this.getCms().getLock(resource);
    		  	String lockiconpath = null;
    		  	if(!lock.isUnlocked()){
    		  		lockiconpath = OpenCms.getSystemInfo().getContextPath() + "/resources/explorer/lock_other.gif";
    		  		if(lock.isDirectlyOwnedBy(this.getCms().getRequestContext().getCurrentUser())){
    		  			lockiconpath = OpenCms.getSystemInfo().getContextPath() + "/resources/explorer/lock_user.gif";
    		  		}
    		  		if(lock.isOwnedBy(this.getCms().getRequestContext().getCurrentUser()) && lock.isShared()){
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
                
      		  	// rootpath
                String rootPath = resource.getRootPath();
                
                // title property
                CmsProperty property = getCms().readPropertyObject(resource.getRootPath(), "Title", false);
                String title = property!=null ?  property.getValue() : "";
                
                // size
                String size = resource.getLength() + "";
                
                // root
                String siteroot = OpenCms.getSiteManager().getSiteForRootPath(resource.getRootPath()).getSiteRoot();
                CmsProperty siterootproperty = getCms().readPropertyObject(siteroot, "Title", false);
                String siteroottitle = siterootproperty!=null ? siterootproperty.getValue() : "";
                
      		  	item.set(LIST_COLUMN_TYPEICON, "<img src=\""+iconpath+"\" border=\"0\" width=\"16\" height=\"16\" alt=\"\"/>");
    		  	item.set(LIST_COLUMN_LOCKICON, lockiconpath!=null ? "<img src=\""+lockiconpath+"\" border=\"0\" width=\"16\" height=\"16\" alt=\"\"/>" : "");
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
            		if(resource.getLength() > maxLength){
            			displayed = false;
            		}
            	}
                if(createdBefore != Long.MIN_VALUE && createdBefore != 0){
            		if(resource.getDateCreated() > createdBefore){
            			displayed = false;
            		}
            	}
                if(createdAfter != Long.MAX_VALUE && createdAfter != 0){
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
			LOG.error(e);
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

        // no LMA        
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setColumns(org.opencms.workplace.list.CmsListMetadata)
     */
	protected void setColumns(CmsListMetadata metadata) {
		
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
/*
 * Copyright (c) Eurelis. All rights reserved. CONFIDENTIAL - Use is subject to license terms.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are not permitted without prior written permission of Eurelis.
 */

package com.eurelis.opencms.admin.xmltransformation.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.i18n.CmsMessageContainer;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.main.CmsRuntimeException;
import org.opencms.workplace.list.A_CmsListDialog;
import org.opencms.workplace.list.CmsHtmlList;
import org.opencms.workplace.list.CmsListColumnAlignEnum;
import org.opencms.workplace.list.CmsListColumnDefinition;
import org.opencms.workplace.list.CmsListItem;
import org.opencms.workplace.list.CmsListMetadata;
import org.opencms.workplace.list.CmsListMultiAction;
import org.opencms.workplace.list.CmsListOrderEnum;
import org.opencms.workplace.list.CmsListState;

import com.eurelis.opencms.admin.xmltransformation.CmsXmlTransformation;
import com.eurelis.tools.xml.transformation.Journal;

public class CmsXmlMockProcessDialog extends A_CmsListDialog {

	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsXmlMockProcessDialog.class);
	
	public static final String LIST_ID = "li";
	
	public static final String LIST_COLUMN_CHECKBOX = "cc";
	
	public static final String LIST_COLUMN_PATH = "cp";
	
	public static final String LIST_COLUMN_VALID_BEFORE_PROCESSING = "cvb";
	
	public static final String LIST_COLUMN_VALID_AFTER_PROCESSING = "cva";
	
	
	
	private List<Journal> sortedJournalList = null;
	private CmsXmlTransformation cmsXmlTransformation = null;
	
	
	public static CmsXmlMockProcessDialog newInstance(PageContext context, HttpServletRequest req, HttpServletResponse res) {
		
		String currentParamSessionSortCol = (String) context.getAttribute("sort_column", PageContext.SESSION_SCOPE);
		CmsListOrderEnum currentSessionSortOrder = (CmsListOrderEnum) context.getAttribute("sort_order", PageContext.SESSION_SCOPE);
		
		String requestParamSessionSortCol =  req.getParameter(PARAM_SORT_COL);
		String action = req.getParameter(PARAM_ACTION);
		
		CmsListOrderEnum listOrder;
				
		boolean refreshList = false;
		
		if (currentParamSessionSortCol == null || requestParamSessionSortCol == null) {
			listOrder = CmsListOrderEnum.ORDER_ASCENDING;
			requestParamSessionSortCol = LIST_COLUMN_PATH;
		}
		else {
			if (action.equals(LIST_SORT)) {

				if (requestParamSessionSortCol.equals(currentParamSessionSortCol)) {
					refreshList = true;
					if (currentSessionSortOrder.equals(CmsListOrderEnum.ORDER_ASCENDING)) {
						listOrder = CmsListOrderEnum.ORDER_DESCENDING;
					}
					else {
						listOrder = CmsListOrderEnum.ORDER_ASCENDING;
					}

				}
				else {
					listOrder = CmsListOrderEnum.ORDER_ASCENDING;
				}

			}
			else {
				listOrder = currentSessionSortOrder;
			}

		}
		
		context.setAttribute("sort_order", listOrder, PageContext.SESSION_SCOPE);
		context.setAttribute("sort_column", requestParamSessionSortCol, PageContext.SESSION_SCOPE);
				
		CmsXmlMockProcessDialog returnObject = new CmsXmlMockProcessDialog(new CmsJspActionElement(context, req, res), LIST_ID, Messages.get().container(Messages.GUI_MOCK_PROCESS_LIST_NAME_0), requestParamSessionSortCol, listOrder, null);
		
		if (refreshList) {
			returnObject.refreshList();
		}
		
		return returnObject;
		
	}
	
	public CmsXmlMockProcessDialog(PageContext context, HttpServletRequest req, HttpServletResponse res) {
		this(new CmsJspActionElement(context, req, res), LIST_ID, Messages.get().container(Messages.GUI_MOCK_PROCESS_LIST_NAME_0), LIST_COLUMN_PATH, CmsListOrderEnum.ORDER_ASCENDING, null);
	}
	
	
	 
	
	protected CmsXmlMockProcessDialog(CmsJspActionElement jsp, String listId,
			CmsMessageContainer listName, String sortedColId,
			CmsListOrderEnum sortOrder, String searchableColId) {
		super(jsp, listId, listName, sortedColId, sortOrder, searchableColId);
				
		//this.setParamAction("/system/modules/com.eurelis.opencms.admin/elements/mock_process.jsp");
		
		this.cmsXmlTransformation = (CmsXmlTransformation)jsp.getRequest().getSession().getAttribute(CmsXmlTransformation.SESSION_KEY);
		this.cmsXmlTransformation.setMockProcess(false);
		sortedJournalList = cmsXmlTransformation.getJournalList();
		
		
		//this.getList().setSortedColumn(sortedColId);
		
		if (sortedColId.equals(LIST_COLUMN_PATH)) {
			if (sortOrder == CmsListOrderEnum.ORDER_ASCENDING) {
				Collections.sort(sortedJournalList, new Comparator<Journal>() {

					public int compare(Journal arg0, Journal arg1) {
						return arg0.getDocumentId().compareTo(arg1.getDocumentId());
					}
					
				});
				
			}
			else {				
				Collections.sort(sortedJournalList, new Comparator<Journal>() {

					public int compare(Journal arg1, Journal arg0) {
						return arg0.getDocumentId().compareTo(arg1.getDocumentId());
					}
					
				});
			}
			
		}
		else if (sortedColId.equals(LIST_COLUMN_VALID_BEFORE_PROCESSING)) {
			if (sortOrder == CmsListOrderEnum.ORDER_ASCENDING) {
				Collections.sort(sortedJournalList, new Comparator<Journal>() {

					public int compare(Journal arg0, Journal arg1) {
						Boolean b0 = arg0.hasInitialValidationFailed();
						Boolean b1 = arg1.hasInitialValidationFailed();
						
						return b0.compareTo(b1);
					}
					
				});
			}
			else {
				Collections.sort(sortedJournalList, new Comparator<Journal>() {

					public int compare(Journal arg1, Journal arg0) {
						Boolean b0 = arg0.hasInitialValidationFailed();
						Boolean b1 = arg1.hasInitialValidationFailed();
						
						return b0.compareTo(b1);
					}
					
				});
			}
		}
		else if (sortedColId.equals(LIST_COLUMN_VALID_AFTER_PROCESSING)) {
			if (sortOrder == CmsListOrderEnum.ORDER_ASCENDING) {
				Collections.sort(sortedJournalList, new Comparator<Journal>() {

					public int compare(Journal arg0, Journal arg1) {
						Boolean b0 = arg0.hasTransformationSucceed();
						Boolean b1 = arg1.hasTransformationSucceed();
						
						return b0.compareTo(b1);
					}
					
				});
			}
			else {
				Collections.sort(sortedJournalList, new Comparator<Journal>() {

					public int compare(Journal arg1, Journal arg0) {
						Boolean b0 = arg0.hasTransformationSucceed();
						Boolean b1 = arg1.hasTransformationSucceed();
						
						return b0.compareTo(b1);
					}
					
				});
			}
		}
		else if (sortedColId.equals(LIST_COLUMN_CHECKBOX)) {
			if (sortOrder == CmsListOrderEnum.ORDER_ASCENDING) {
				Collections.sort(sortedJournalList, new Comparator<Journal>() {

					public int compare(Journal arg0, Journal arg1) {
						Boolean b0 = cmsXmlTransformation.ignoreValidation(arg0.getDocumentId());
						Boolean b1 = cmsXmlTransformation.ignoreValidation(arg1.getDocumentId());
						
						return b0.compareTo(b1);
					}
					
				});
			}
			else {
				Collections.sort(sortedJournalList, new Comparator<Journal>() {

					public int compare(Journal arg1, Journal arg0) {
						Boolean b0 = cmsXmlTransformation.ignoreValidation(arg0.getDocumentId());
						Boolean b1 = cmsXmlTransformation.ignoreValidation(arg1.getDocumentId());
						
						return b0.compareTo(b1);
					}
					
				});
			}
		}
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void executeListMultiActions() throws IOException, ServletException, CmsRuntimeException {
		
		List<CmsListItem> listItem = this.getSelectedItems();
		
		if (getParamListAction().equalsIgnoreCase(TOGGLE_IGNORE_INITIAL_VALIDATION_ACTION)) {
			for (CmsListItem item : listItem) {
				String resourceId = (String)item.get(LIST_COLUMN_PATH);
				
				boolean currentValue = cmsXmlTransformation.ignoreValidation(resourceId);
				cmsXmlTransformation.setIgnoreValidation(resourceId, !currentValue);
				
			}
			
			//this.refreshList();
			
		}
		
	}

	
	public void executeListSingleActions() throws IOException, ServletException, CmsRuntimeException {
		
	}

	@Override
	protected void fillDetails(String detailId) {
		// TODO Auto-generated method stub
		
	}
	
	protected List<Journal> getJournalList() {
		
		this.getParamPage();
		this.getParamSortCol();
		
		
		return null;
	}
	

	@Override
	protected List<CmsListItem> getListItems() throws CmsException {
		// TODO Auto-generated method stub
		
		List<CmsListItem> listItemList = new ArrayList<CmsListItem>();
		
		
		
		
		
		
		if (this.sortedJournalList != null) {
			
			CmsListState cmsListState = this.getListState();

			
			CmsHtmlList htmlList = this.getList();
			int first = (cmsListState.getPage() - 1) * htmlList.getMaxItemsPerPage();
			int last = Math.min(sortedJournalList.size(), first + htmlList.getMaxItemsPerPage());
			
			List<Journal> workJournalList = sortedJournalList.subList(first, last);
			
			
			this.getList().setTotalSize(sortedJournalList.size());
			this.getList().setSize(sortedJournalList.size());
			
			for (Journal journal : workJournalList) {
				CmsListItem item = getList().newItem(journal.getDocumentId());
				String documentId = journal.getDocumentId();
				
				item.set(LIST_COLUMN_CHECKBOX, cmsXmlTransformation.ignoreValidation(documentId));
				item.set(LIST_COLUMN_PATH, journal.getDocumentId());
				item.set(LIST_COLUMN_VALID_AFTER_PROCESSING, journal.hasTransformationSucceed());
				item.set(LIST_COLUMN_VALID_BEFORE_PROCESSING, !journal.hasInitialValidationFailed());
				
				listItemList.add(item);
			}
			
		}
		
		
		
		return listItemList;
	}

	@Override
	protected void setColumns(CmsListMetadata metadata) {
		metadata.setSelfManaged(true);
		
		CmsListColumnDefinition checkboxColumn = new CmsListColumnDefinition(LIST_COLUMN_CHECKBOX);
		
		checkboxColumn.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
		checkboxColumn.setSorteable(true);
		CmsMessageContainer checkboxName = Messages.get().container(Messages.GUI_MOCK_PROCESS_CHECKBOX_COLUMN_NAME_0);
		checkboxColumn.setName(checkboxName);
		checkboxColumn.setHelpText(Messages.get().container(Messages.GUI_MOCK_PROCESS_CHECKBOX_COLUMN_HELP_0));
		metadata.addColumn(checkboxColumn);
		
		CmsListColumnDefinition pathColumn = new CmsListColumnDefinition(LIST_COLUMN_PATH);
		pathColumn.setAlign(CmsListColumnAlignEnum.ALIGN_LEFT);
		pathColumn.setSorteable(true);
		CmsMessageContainer pathcolumnName = Messages.get().container(Messages.GUI_MOCK_PROCESS_PATH_COLUMN_NAME_0);
		pathColumn.setName(pathcolumnName);
		metadata.addColumn(pathColumn);
		
		CmsListColumnDefinition validBeforeColumn = new CmsListColumnDefinition(LIST_COLUMN_VALID_BEFORE_PROCESSING);
		validBeforeColumn.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
		CmsMessageContainer validBeforeColumnName = Messages.get().container(Messages.GUI_MOCK_PROCESS_VALID_BEFORE_COLUMN_NAME_0);
		validBeforeColumn.setName(validBeforeColumnName);
		validBeforeColumn.setHelpText(Messages.get().container(Messages.GUI_MOCK_PROCESS_VALID_BEFORE_COLUMN_HELP_0));
		metadata.addColumn(validBeforeColumn);
		
		CmsListColumnDefinition validAfterColumn = new CmsListColumnDefinition(LIST_COLUMN_VALID_AFTER_PROCESSING);
		validAfterColumn.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
		CmsMessageContainer validAfterColumnName = Messages.get().container(Messages.GUI_MOCK_PROCESS_VALID_AFTER_COLUMN_NAME_0);
		validAfterColumn.setName(validAfterColumnName);
		validAfterColumn.setHelpText(Messages.get().container(Messages.GUI_MOCK_PROCESS_VALID_AFTER_COLUMN_HELP_0));
		metadata.addColumn(validAfterColumn);
		
	}

	@Override
	protected void setIndependentActions(CmsListMetadata metadata) {
		// TODO Auto-generated method stub
		
	}
	
	public static final String TOGGLE_IGNORE_INITIAL_VALIDATION_ACTION = "tiiv";

	@Override
	protected void setMultiActions(CmsListMetadata metadata) {
		// TODO Auto-generated method stub
		
		CmsListMultiAction toggleIgnoreInitialValidationAction = new CmsListMultiAction(TOGGLE_IGNORE_INITIAL_VALIDATION_ACTION);
		toggleIgnoreInitialValidationAction.setName(Messages.get().container(Messages.GUI_TOGGLE_IGNORE_INITIAL_VALIDATION_ACTION_0));
		toggleIgnoreInitialValidationAction.setHelpText(Messages.get().container(Messages.GUI_TOGGLE_IGNORE_INITIAL_VALIDATION_ACTION_HELP_0));
		toggleIgnoreInitialValidationAction.setVisible(true);
		metadata.addMultiAction(toggleIgnoreInitialValidationAction);
	
	}
	
	
	public String defaultActionHtml() {

        if ((getList() != null) && getList().getAllContent().isEmpty()) {
            // TODO: check the need for this
            refreshList();
        }
        StringBuffer result = new StringBuffer(2048);
        result.append(defaultActionHtmlStart());
        result.append(customHtmlStart());
        result.append(defaultActionHtmlContent());
        
        
        result.append("<form name=\"EDITOR\" id=\"EDITOR\" method=\"post\" action=\"/opencms/opencms/system/modules/com.eurelis.opencms.admin/elements/process.jsp\" class=\"nomargin\" onsubmit=\"return submitAction('ok', null, 'EDITOR');\">");

        result.append("<!-- button row start -->");
        result.append("<div class=\"dialogbuttons\" unselectable=\"on\">");
        result.append("<input name=\"ok\" value=\"Ok\" type=\"submit\" class=\"dialogbutton\">");
        result.append("<input name=\"cancel\" type=\"button\" value=\"Cancel\" onclick=\"submitAction('cancel', form);\" class=\"dialogbutton\">");
        result.append("</div>");
        result.append("<!-- button row end -->");
        result.append("<input type=\"hidden\" name=\"elementindex\" value=\"0\">");
        result.append(String.format("<input type=\"hidden\" name=\"resource\" value=\"%s\">", this.getJsp().getRequest().getParameter(PARAM_RESOURCE)));
        result.append("<input type=\"hidden\" name=\"page\" value=\"page0\">");
        result.append("<input type=\"hidden\" name=\"action\" value=\"save\">");
        result.append("<input type=\"hidden\" name=\"elementname\" value=\"undefined\">");

        result.append("<input type=\"hidden\" name=\"framename\" value=\"\">");
        result.append("</form>");
        
        
        
        
        result.append(customHtmlEnd());
        result.append(defaultActionHtmlEnd());
        return result.toString();
    }
	

}

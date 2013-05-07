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

package com.eurelis.opencms.admin.xmltransformation.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.InvalidXPathException;
import org.dom4j.XPath;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.file.types.I_CmsResourceType;
import org.opencms.i18n.CmsEncoder;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsStringUtil;
import org.opencms.widgets.CmsCheckboxWidget;
import org.opencms.widgets.CmsVfsFileWidget;
import org.opencms.workplace.CmsDialog;
import org.opencms.workplace.CmsWidgetDialog;
import org.opencms.workplace.CmsWidgetDialogParameter;
import org.opencms.workplace.CmsWorkplaceSettings;
import org.opencms.workplace.tools.CmsToolDialog;
import org.opencms.xml.content.CmsXmlContent;
import org.opencms.xml.content.CmsXmlContentFactory;
import org.opencms.xml.types.I_CmsXmlContentValue;

import com.eurelis.opencms.admin.xmltransformation.CmsXmlTransformation;
import com.eurelis.tools.xml.transformation.BatchProcessor;
import com.eurelis.tools.xml.transformation.model.Destination.Position;
import com.eurelis.tools.xml.transformation.model.builder.TemplateTransformationBuilder;
import com.eurelis.tools.xml.transformation.model.builder.UnitaryTransformationBuilder;
import com.eurelis.tools.xml.transformation.model.builder.XMLTransformationBuilder;
import com.eurelis.tools.xml.transformation.nodeselection.XPATHParser;

public class CmsXmlTransformationDialog extends CmsWidgetDialog {

	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsXmlTransformationDialog.class);
	
	protected static final String PROCESS_ACTION_REPORT = VFS_PATH_SYSTEM + "modules/com.eurelis.opencms.admin/elements/process.jsp";
	
	
	public CmsXmlTransformationDialog(CmsJspActionElement jsp) {
		super(jsp);
	}
	
	
	public CmsXmlTransformationDialog(PageContext context, HttpServletRequest req, HttpServletResponse res) {
        this(new CmsJspActionElement(context, req, res));
    }

	
	
	
	public List<String> processRequest(CmsXmlTransformation cmsXmlTransformation) {
		CmsJspActionElement jsp = this.getJsp();
		CmsObject cmsObject = this.getCms();
		
		String uri = CmsEncoder.decode(jsp.getRequest().getParameter(CmsDialog.PARAM_RESOURCE));
				
		
		boolean contentTypeValid = false;
		
		List<String> errorList = new ArrayList<String>();
		XMLTransformationBuilder xmlTransformationBuilder = XMLTransformationBuilder.newInstance();
		
		
		try {
			CmsFile cmsFile = cmsObject.readFile(uri);
			
			CmsXmlContent xmlTransformationContent = CmsXmlContentFactory.unmarshal(cmsObject, cmsFile);
			List<Locale> localeList = xmlTransformationContent.getLocales();
			
			String contentType = null;
			
			if (localeList.size() == 1) {
				Locale locale = localeList.get(0);
				
				I_CmsXmlContentValue fileTypeContentValue = xmlTransformationContent.getValue("ContentType", locale);
				contentType = fileTypeContentValue.getStringValue(cmsObject);
				
				I_CmsResourceType resourceType = OpenCms.getResourceManager().getResourceType(contentType);
				
				
				if (resourceType != null) {
					contentTypeValid = true;
					cmsXmlTransformation.setContentType(resourceType.getTypeId());
				}
				
				if (!contentTypeValid) {
					errorList.add(String.format(Messages.getLbl(Messages.GUI_XMLTRANSFORMATION_BADCONTENTTYPE_FORMAT_0, getLocale()), contentType));
					cmsXmlTransformation.setContentType(-1);
				}
				
				
				
				
				List<I_CmsXmlContentValue> unitaryTransformationList = xmlTransformationContent.getValues("UnitaryTransformation", locale);
				
				int unitaryTransformationIndex = 0;
				for (I_CmsXmlContentValue unitaryTransformation : unitaryTransformationList) {
					unitaryTransformationIndex++; // indice a 1
					
					UnitaryTransformationBuilder unitaryTransformationBuilder = xmlTransformationBuilder.newUnitaryTransformationBuilder();
					
					
					
					boolean validSource = false;
					
					String sourcePath = String.format("UnitaryTransformation[%d]/Source", unitaryTransformationIndex);
					String destinationXPath = String.format("UnitaryTransformation[%d]/Destination/XPath", unitaryTransformationIndex);
					String destinationSXPath = String.format("UnitaryTransformation[%d]/Destination/SXPath", unitaryTransformationIndex);
					String destinationPosition = String.format("UnitaryTransformation[%d]/Destination/Position", unitaryTransformationIndex);
					String templatePath = String.format("UnitaryTransformation[%d]/Template", unitaryTransformationIndex);
					
					String sourceContentString = null;
					
					I_CmsXmlContentValue sourceContentValue = xmlTransformationContent.getValue(sourcePath, locale);
					
					if (sourceContentValue != null) {
						sourceContentString = sourceContentValue.getStringValue(cmsObject);
						
						if (sourceContentString != null && !sourceContentString.isEmpty()) {
							try {
								XPath sourceXPath = DocumentFactory.getInstance().createXPath(sourceContentString);
								
								unitaryTransformationBuilder.setSource(sourceXPath);
								
								validSource = true;
								
							} catch(InvalidXPathException e) {
								
							}
							
							
						}
						
					}
					
					if (!validSource) {
						errorList.add(String.format(Messages.getLbl(Messages.GUI_UNITARYTRANSFORMATION_BADSOURCE_FORMAT_0, getLocale()), sourceContentString, sourcePath));
					}
					
					
					// destination
					I_CmsXmlContentValue xPathContentValue = xmlTransformationContent.getValue(destinationXPath, locale);
					I_CmsXmlContentValue sxPathContentValue = xmlTransformationContent.getValue(destinationSXPath, locale);
					I_CmsXmlContentValue positionContentValue = xmlTransformationContent.getValue(destinationPosition, locale);
					
					if (xPathContentValue != null && sxPathContentValue != null) {
						String destinationXPathString = xPathContentValue.getStringValue(cmsObject);
						String sxPathString = sxPathContentValue.getStringValue(cmsObject);
						
						String position = positionContentValue.getStringValue(cmsObject);
						
						Position pos = Position.LAST;
						
						if (position != null) {
							if ("FIRST".equalsIgnoreCase(position)) {
								pos = Position.FIRST;
							}
							else if ("LAST".equalsIgnoreCase(position)) {
								pos = Position.LAST;
							}
							else if ("BEFORE".equalsIgnoreCase(position)) {
								pos = Position.BEFORE;
							}
							else if ("AFTER".equalsIgnoreCase(position)) {
								pos = Position.AFTER;
							}
						}
						
						
						
						String xPathTestString = null;
						
						if (destinationXPathString.charAt(destinationXPathString.length() - 1) == '/') {
							xPathTestString = destinationXPathString.substring(0, destinationXPathString.length() - 1);
						}
						else {
							xPathTestString = destinationXPathString;
						}
						
						boolean isSimplified = Boolean.valueOf(sxPathString);
						boolean isValidPosition = true;
						
						if (isSimplified) {
						if (pos == Position.BEFORE || pos == Position.AFTER) {
							isValidPosition = false;
						}
						}
						
						if (!isValidPosition) {
							errorList.add(String.format(Messages.getLbl(Messages.GUI_UNITARYTRANSFORMATION_BADSXPATH_POSITION_FORMAT_0, getLocale()), position, destinationPosition));
						}
						
						boolean validXPath = false;
						
						try {
							DocumentFactory.getInstance().createXPath(xPathTestString);
							validXPath = true;
						}
						catch (InvalidXPathException e) {
							
						}
						
						if (validXPath && isSimplified) {
							
							try {
								XPATHParser.getInstance().parseFailOverPath(destinationXPathString, pos);
							} 
							catch (java.lang.IllegalArgumentException e) {
								validXPath = false;
							}
							
						}
						
						if (!validXPath) {
							errorList.add(String.format(Messages.getLbl(Messages.GUI_UNITARYTRANSFORMATION_BADDESTINATION_FORMAT_0, getLocale()), isSimplified?1:0, destinationXPathString, destinationXPath));
							
						}
						else {
							if (isSimplified) {
								unitaryTransformationBuilder.setSXPathDestination(destinationXPathString, pos);
							}
							else {
								unitaryTransformationBuilder.setXPathDestination(destinationXPathString, pos);
							}
							
						}
						
					}
					
					
					// template
					I_CmsXmlContentValue templateContentValue = xmlTransformationContent.getValue(templatePath, locale);
					if (templateContentValue != null) {
						Element templateElement = templateContentValue.getElement();
						
						boolean isValidTemplateString = false;
						String templateString = templateElement.selectSingleNode("Template").getText().trim();
						
						TemplateTransformationBuilder templateTransformationBuilder = unitaryTransformationBuilder.getTemplateTransformationBuilder();
						
						try {
							templateTransformationBuilder.setTemplate(templateString);
							isValidTemplateString = true;
						} catch(DocumentException e) {
							
						}
						
						if (!isValidTemplateString) {
							errorList.add(String.format(Messages.getLbl(Messages.GUI_TEMPLATETRANSFORMATION_BADTEMPLATE_FORMAT_0, getLocale()), templateString, templatePath));									
						}
						
						
						int templateParameterIndex = 0;
						@SuppressWarnings("unchecked")
						List<Element> templateParameterList = templateElement.selectNodes("Parameters");
						
						for (Element templateParameterElement : templateParameterList) {
							templateParameterIndex++;
							String templateParameterPath = String.format("UnitaryTransformation[%d]/Template/Parameters[%d]/", unitaryTransformationIndex, templateParameterIndex);
							
							String templateParameterName = templateParameterElement.elementTextTrim("ParameterName");
							String templateParameterXPathValue = templateParameterElement.elementTextTrim("ParameterXPathValue");
							
							boolean templateParameterIsValid = false;
							
							if (!templateParameterName.isEmpty() && !templateParameterXPathValue.isEmpty()) {
								try {
									templateTransformationBuilder.addParameter(templateParameterName, DocumentFactory.getInstance().createXPath(templateParameterXPathValue));
									templateParameterIsValid = true;
								}
								catch (InvalidXPathException e) {

								}
							
							}
							
							if (!templateParameterIsValid) {
								errorList.add(String.format(Messages.getLbl(Messages.GUI_TEMPLATETRANSFORMATION_BADPARAMETER_FORMAT_0, getLocale()), templateParameterName, templateParameterXPathValue, templateParameterPath));
							}
							
						}
						
					}
					
				}
				
			}
			
			
		} catch (CmsException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("processRequest " + e);
			}
			LOG.error("processRequest " + e.getMessage());
			e.printStackTrace();
		}
		
		
		if (errorList.isEmpty()) {
			cmsXmlTransformation.setXmlTransformation(xmlTransformationBuilder.build());
			cmsXmlTransformation.setXmlTransformationProcessingErrors(null);
		}
		else {
			cmsXmlTransformation.setXmlTransformation(null);
			cmsXmlTransformation.setXmlTransformationProcessingErrors(errorList);
		}
		
		return errorList;
		
	}
	
	
	
	@Override
	protected void initMessages() {
		addMessages(Messages.get().getBundleName());
		super.initMessages();
	}
	
	
	@Override
	public void actionCommit() throws IOException, ServletException {
		
		initModule();
		

		HttpSession session = this.getJsp().getRequest().getSession();
		session.setAttribute(CmsXmlTransformation.SESSION_KEY, cmsXmlTransformation);
		
		Map params = new HashMap();
		params.put(CmsXmlTransformationProcessReport.PARAM_CLASSNAME, this.getClass().getName());
		params.put(PARAM_STYLE, CmsToolDialog.STYLE_NEW);
		params.put(PARAM_CLOSELINK, "system/workplace/views/workplace.jsp");
		params.put(PARAM_ACTION, "/system/modules/com.eurelis.opencms.admin/elements/mock_process.jsp");
		
		getToolManager().jspForwardPage(this, PROCESS_ACTION_REPORT, params);
		
		
	}

	private CmsXmlTransformation cmsXmlTransformation = null;
	
	private BatchProcessor bc = null;
	
	@Override
	protected void defineWidgets() {
		initModule();
		
		
		String vfsFile = this.cmsXmlTransformation.getVfsFile();

		
		processRequest(cmsXmlTransformation);
		List<String> xmlTransformationProcessingError = this.cmsXmlTransformation.getXmlTransformationProcessingErrors();
		

		
		if (xmlTransformationProcessingError != null && !xmlTransformationProcessingError.isEmpty()) {

		}
		else {
			addWidget(new CmsWidgetDialogParameter(cmsXmlTransformation, "vfsFile", PAGES[0], new CmsVfsFileWidget()));
			addWidget(new CmsWidgetDialogParameter(cmsXmlTransformation, "ignoreContentAlreadyValid", PAGES[0], new CmsCheckboxWidget()));
		}
		
		
	}
	
	
	protected void correspondingResources(List<String> resourceList, CmsResource cmsResource) {
		CmsObject cmsObject = this.getCms();
		
		String relativePath = cmsObject.getSitePath(cmsResource);
		
		if (cmsResource.isFile() && cmsResource.getTypeId() == cmsXmlTransformation.getContentType()) {			
			resourceList.add(relativePath);
		}
		else if (cmsResource.isFolder()) {
			try {
				List<CmsResource> cmsResourceList = cmsObject.getResourcesInFolder(relativePath, CmsResourceFilter.ALL);
				
				for (CmsResource resource : cmsResourceList) {
					correspondingResources(resourceList, resource);
				}
				
				
			} catch (CmsException e) {
				LOG.error("correspondingResources " + e);
			}
			
			
		}
		
	}
	
	
	protected String createDialogHtml(String dialog) {
		
		initModule();
		
		
        StringBuffer result = new StringBuffer(1024);

        
        // create table
        result.append(createWidgetTableStart());

        // show error header once if there were validation errors
        result.append(createWidgetErrorHeader());

        if (dialog.equals(PAGES[0])) {
        	result.append(createWidgetBlockStart(Messages.getLbl(Messages.GUI_CHOOSE_FILE_TO_APPLY_TRANSFORMATION_0, getLocale())));
            List<String> xmlTransformationProcessingError = this.cmsXmlTransformation.getXmlTransformationProcessingErrors();
            
    		if (xmlTransformationProcessingError != null && !xmlTransformationProcessingError.isEmpty()) {
    			
    			for (String error : xmlTransformationProcessingError) {
    				result.append("<p>" + error + "</p>");
    			}
    			
    		}
    		else {
    			result.append(createDialogRowsHtml(0, 1));
    		}
            
            result.append(createWidgetBlockEnd());
        }
        
       
        
        // close table
        result.append(createWidgetTableEnd());

        
        
        return result.toString();
    }
	
	
	protected void initModule() {

		Object o;
		if (CmsStringUtil.isEmpty(getParamAction())) {
			o = new CmsXmlTransformation();
		}
		else {
			o = getDialogObject();
		}
		
		if (!(o instanceof CmsXmlTransformation)) {
			this.cmsXmlTransformation = new CmsXmlTransformation();
			this.cmsXmlTransformation.setMockProcess(true);
		}
		else {
			this.cmsXmlTransformation = (CmsXmlTransformation)o;

		}
		
		//LOGGER.error("initModule " + this.cmsXmlTransformation);
		
	}
	
	private static final String PAGES[] = { "page0" };
	
	@Override
	protected String[] getPageArray() {
		return PAGES;
	}
	
	
	@Override
	protected void initWorkplaceRequestValues(CmsWorkplaceSettings settings, HttpServletRequest request) {
		super.initWorkplaceRequestValues(settings, request);
				
		setDialogObject(this.cmsXmlTransformation);
	}
	
	
	@Override
	public String dialogButtonsCustom() {
		if (cmsXmlTransformation != null && cmsXmlTransformation.hasInitialFileValidationFailed()) {
			return dialogButtons(new int[] {BUTTON_CANCEL}, new String[1]);
		}
		else {
			return super.dialogButtonsCustom();
		}
		
	}
	

}

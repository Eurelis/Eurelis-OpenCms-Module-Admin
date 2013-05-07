<%@ page pageEncoding="UTF-8" %><%@ page import="com.eurelis.opencms.admin.xmltransformation.ui.*" %><%	
	
	CmsXmlTransformationProcessReport wp = new CmsXmlTransformationProcessReport(pageContext, request, response);
	wp.displayReport();
%>
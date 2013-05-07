<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="com.eurelis.opencms.admin.xmltransformation.ui.*" %>

<%
	CmsXmlTransformationDialog dialog = new CmsXmlTransformationDialog(pageContext, request, response);
	dialog.displayDialog();
%>


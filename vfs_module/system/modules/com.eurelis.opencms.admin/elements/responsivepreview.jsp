<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.opencms.staticexport.CmsLinkManager,org.opencms.main.OpenCms,org.opencms.jsp.CmsJspActionElement,org.opencms.file.CmsObject,org.opencms.i18n.CmsEncoder,org.opencms.workplace.CmsDialog,org.opencms.workplace.CmsWorkplace,java.util.*,com.eurelis.opencms.admin.responsivepreview.*" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%	
	
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
 
	CmsJspActionElement jsp = new CmsJspActionElement(pageContext, request, response);
 	
 
    String actionParam = request.getParameter("action");
	
	if (actionParam != null && actionParam.equals("submit")) {
		jsp.include(CmsWorkplace.FILE_EXPLORER_FILELIST, null, new HashMap());	
	
	}
	else {
 		CmsObject cmsObject = jsp.getCmsObject();
		
 		String resourceName =  CmsEncoder.decode(jsp.getRequest().getParameter(CmsDialog.PARAM_RESOURCE));
	
 		String onlineLink = OpenCms.getLinkManager().getOnlineLink(cmsObject, resourceName);
 	
		DeviceListReader dlr = new DeviceListReader(jsp);
		dlr.processRequest(request);
	
%>

<!doctype html>
<html>
  <head>
    <link href="<cms:link>/system/workplace/commons/style/workplace.css</cms:link>" type="text/css" rel="stylesheet"></link>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script>
      var url = getURLParameter('url');
      var orientation = getURLParameter('orientation');
      var device = getURLParameter('device'); 
      var ios = {
        'iphone' : {
          'portrait' : {
            'width' : 378,
            'height': 744,
            'next-class' : 'landscape',
            'img': '<cms:link>/system/modules/com.eurelis.opencms.admin/resources/rotate-ccw.png</cms:link>',
          },
          'landscape' : {
            'width' : 744,
            'height': 378,
            'next-class' : 'portrait',
            'img': '<cms:link>/system/modules/com.eurelis.opencms.admin/resources/rotate-cw.png</cms:link>',
          }
        },
        'ipad' : {
          'portrait' : {
            'width' : 986,
            'height': 1268,
            'next-class' : 'landscape',
            'img': '<cms:link>/system/modules/com.eurelis.opencms.admin/resources/rotate-ccw.png</cms:link>',
          },
          'landscape' : {
            'width' : 1268,
            'height': 986,
            'next-class' : 'portrait',
            'img': '<cms:link>/system/modules/com.eurelis.opencms.admin/resources/rotate-cw.png</cms:link>',
          }
        }
      };
      function getURLParameter(name) {
        return decodeURI((RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1]);
      }
      function openWindow(w,h,url) {
        window.open(url,"_blank","width="+Math.min(screen.width-16,w)+",height="+Math.min(screen.height-64,h));
      }
      function submit() {
        var params = {};
        $.each($('#form').serializeArray(), function(i, field) {
            params[field.name] = field.value;
        });
        var device = params['device'];
        var orientation = params['orientation'];
        openWindow(ios[device][orientation]['width'], ios[device][orientation]['height'], location.pathname+'?url='+params['url']+'&device='+device+'&orientation='+orientation);
        return true;
      }
      function rotate() {
        var next = ios[device][orientation]['next-class'];
        openWindow(ios[device][next]['width'], ios[device][next]['height'], location.pathname+'?url='+url+'&device='+device+'&orientation='+next);
        window.close();
      }
      $(document).ready(function(){
        if(url != 'null') {
          if(!orientation) orientation = "portrait";
          if(!device) device = "iphone";
          $('#rotate').click(rotate);
          $('iframe').attr('src', url+'?device='+device);
               
          $('#ios').attr('class', device+' '+orientation);
          $('#rotate img').attr('src', ios[device][orientation]['img']);
          $('#ios').show();
          $('#iphone_alert').hide();
          if(navigator.userAgent.indexOf('Safari') == -1 || navigator.userAgent.indexOf('Chrome') != -1) { $('#iphone_alert').show(); }
          //window.resizeTo(ios[device][orientation]['width']+16,ios[device][orientation]['height']+64);
        }
        else {
          $('#form').submit(submit);
          $('#help').show();
        }
      });
    </script>
    <style type="text/css">
      body {
        font-family: Verdana, Arial;
        margin: 0;
      }
      #ios, #iphone_alert, #help { 
        display:none; 
      }
      #orientationlandscape img {
        padding: 0 20px;
        -webkit-transform: rotate(90deg);
           -moz-transform: rotate(90deg);
            -ms-transform: rotate(90deg);
             -o-transform: rotate(90deg);
                transform: rotate(90deg);
      }
      #help table tr td{
        padding: 10px 0;
      }
      label img {
        padding: 0 10px;
        vertical-align:middle;
      }
      
      <c:forEach var="device" items="${deviceVOList}">
      div#ios.${device.id}.portrait {
          width:${device.imageWidth}px;
          height:${device.imageHeight}px;
          background: url(<cms:link>${device.imageSrc}</cms:link>) no-repeat;
      }
      div#ios.${device.id}.landscape {
          width:${device.imageHeight}px;
          height:${device.imageWidth}px;
      }
      div#ios.${device.id}.landscape:before {
        content: "";
        position: absolute;
        z-index: -1;
        width: 200%;
        height: 200%;
        top: -${device.imageHeight}px;
        left: -${device.imageWidth}px;
        background: url(<cms:link>${device.imageSrc}</cms:link>) no-repeat;
        -webkit-transform: rotate(-90deg);
        -moz-transform: rotate(-90deg);
        -ms-transform: rotate(-90deg);
        -o-transform: rotate(-90deg);
        transform: rotate(-90deg);
      }
      
      .${device.id}.landscape iframe {
        width: ${device.screenHeight}px;
        height: ${device.screenWidth}px;
        padding: ${device.imageWidth - device.screenWidth - device.screenLeft}px ${device.imageHeight - device.screenHeight - device.screenTop}px ${device.screenLeft}px ${device.screenTop}px;
      }
      .${device.id}.portrait iframe {
        width: ${device.screenWidth}px;
        height: ${device.screenHeight}px;
        padding: ${device.screenTop}px ${device.imageWidth - device.screenWidth - device.screenLeft}px ${device.imageHeight - device.screenHeight - device.screenTop}px ${device.screenLeft}px;
      }
      
      </c:forEach>
      
      
      div#ios.landscape, div#ios.portrait {
         position: relative;
         overflow: hidden;
      }
      iframe {
        position: absolute;
        border:none;
      }
      
      a {
        color: #CCCCCC;
        position: absolute;
        top: 30px;
        left: 30px;
        z-index:1;
      }
      
      
      div#ios.iphone.portrait #iphone_alert {
        top: 125px;
        left: 30px;
      }
      
      div#ios.iphone.landscape #iphone_alert {
        top: -40px;
        left: 215px;
      }
      
      div#ios.ipad.portrait #iphone_alert {
        top: 300px;
        left: 330px;
      }
      
      div#ios.ipad.landscape #iphone_alert {
        top: 240px;
        left: 480px;
      }
      
      /* Alert */
      #iphone_alert {
        width : 320px;
        height : 480px;
        position: absolute;
        display: block;
        z-index:3000;
        color: #fff;
      }

      #iphone_alert .alert {
        font: normal 17px/23px "Helvetica Neue", Arial;
        background: #162344;
        background: rgba(22,35,68,0.9);
        color: #fff;
        text-shadow: hsla(0,0%,0%,0.8) 0 -1px 0;
        margin: 0 auto;
        padding: 0 0 8px 0;
        border: 2px #dfe1e6 solid;
        position: absolute;
        width: 90%;
        left: 4.5%;
        top:30%;
        -webkit-border-radius: 9px;
           -moz-border-radius: 9px;
                border-radius: 9px;
        -webkit-box-shadow: hsla(0,0%,0%,0.7) 0 1px 2px;
           -moz-box-shadow: hsla(0,0%,0%,0.7) 0 1px 2px;
                box-shadow: hsla(0,0%,0%,0.7) 0 1px 2px;
      }
      #iphone_alert .alert::after {
        content: '';
        background: -webkit-linear-gradient(top, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.3));
        background:    -moz-linear-gradient(top, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.3));
        background:     -ms-linear-gradient(top, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.3));
        background:      -o-linear-gradient(top, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.3));
        background:         linear-gradient(top, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0.3));
        -ms-filter: progid:DXImageTransform.Microsoft.gradient(startColorStr='#697287', EndColorStr='#343f5c', GradientType=0);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#697287', endColorstr='#343f5c', GradientType=0);
        height: 15%;
        width: 100%;
        -webkit-border-radius: 7px 7px 50% 50%  / 7px 7px 4px 4px;
           -moz-border-radius: 7px 7px 50% 50%  / 7px 7px 4px 4px;
                border-radius: 7px 7px 50% 50%  / 7px 7px 4px 4px;
        position: absolute;
        left: 0;
        top:0;
      }

      #iphone_alert .text {
        position: relative;
        z-index: 110;
        text-align: center;
        width: 90%;
        left: 5%;
        margin: 13px 0 15px 0;
      }
      #iphone_alert .text b {
        margin-bottom: 5px;
        display: block;
      }
      #iphone_alert .button {
        font-weight: bold;
        font-size: 15px;
        height: 30px;
        line-height: 30px;
        text-align: center;
        width: 35%;
        cursor: pointer;
        border: 1px #131e3b solid;
        display: block;
        text-decoration: none;
        color: #fff;
        cursor: pointer;
        background: -webkit-linear-gradient(top, rgba(116, 124, 143, 0.8) 0%, rgba(52, 63, 92, 0.8) 50%, rgba(22, 35, 68, 0.8) 50%, rgba(35, 47, 78, 0.8) 100%);
        background:    -moz-linear-gradient(top, rgba(116, 124, 143, 0.8) 0%, rgba(52, 63, 92, 0.8) 50%, rgba(22, 35, 68, 0.8) 50%, rgba(35, 47, 78, 0.8) 100%);
        background:     -ms-linear-gradient(top, rgba(116, 124, 143, 0.8) 0%, rgba(52, 63, 92, 0.8) 50%, rgba(22, 35, 68, 0.8) 50%, rgba(35, 47, 78, 0.8) 100%);
        background:      -o-linear-gradient(top, rgba(116, 124, 143, 0.8) 0%, rgba(52, 63, 92, 0.8) 50%, rgba(22, 35, 68, 0.8) 50%, rgba(35, 47, 78, 0.8) 100%);
        background:         linear-gradient(top, rgba(116, 124, 143, 0.8) 0%, rgba(52, 63, 92, 0.8) 50%, rgba(22, 35, 68, 0.8) 50%, rgba(35, 47, 78, 0.8) 100%);
        -ms-filter: progid:DXImageTransform.Microsoft.gradient(startColorStr='#747c8f', EndColorStr='#232f4e', GradientType=0);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#747c8f', endColorstr='#232f4e', GradientType=0);
        -webkit-border-radius:5px;
           -moz-border-radius:5px;
                border-radius:5px;
        -webkit-box-shadow: #454f69 0 1px 0;
           -moz-box-shadow: #454f69 0 1px 0;
                box-shadow: #454f69 0 1px 0;
      }

      #iphone_alert .button.wide {
        width: 235px;
        margin-left: 10px;
      }
      #iphone_alert .button.f_left {
        float: left;
        margin: 0 0 0 10px;
      }
      #iphone_alert .button.f_right {
        float: right;
        margin: 0 10px 0 0;
        top: 102px;
        left: 145px;
      }

      #iphone_alert .button.light {
        background: -webkit-linear-gradient(top, rgba(174, 178, 190, 0.6) 0%, rgba(106, 116, 139, 0.6) 50%, rgba(80, 90, 117, 0.6) 50%, rgba(95, 105, 129, 0.6) 100%);
        background:    -moz-linear-gradient(top, rgba(174, 178, 190, 0.6) 0%, rgba(106, 116, 139, 0.6) 50%, rgba(80, 90, 117, 0.6) 50%, rgba(95, 105, 129, 0.6) 100%);
        background:     -ms-linear-gradient(top, rgba(174, 178, 190, 0.6) 0%, rgba(106, 116, 139, 0.6) 50%, rgba(80, 90, 117, 0.6) 50%, rgba(95, 105, 129, 0.6) 100%);
        background:      -o-linear-gradient(top, rgba(174, 178, 190, 0.6) 0%, rgba(106, 116, 139, 0.6) 50%, rgba(80, 90, 117, 0.6) 50%, rgba(95, 105, 129, 0.6) 100%);
        background:         linear-gradient(top, rgba(174, 178, 190, 0.6) 0%, rgba(106, 116, 139, 0.6) 50%, rgba(80, 90, 117, 0.6) 50%, rgba(95, 105, 129, 0.6) 100%);
        -ms-filter: progid:DXImageTransform.Microsoft.gradient(startColorStr='#aeb2be', EndColorStr='#5f6981', GradientType=0);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#aeb2be', endColorstr='#5f6981', GradientType=0);
        -webkit-box-shadow: #454f69 0 1px 0, inset #dadde4 0 1px 0;
           -moz-box-shadow: #454f69 0 1px 0, inset #dadde4 0 1px 0;
                box-shadow: #454f69 0 1px 0, inset #dadde4 0 1px 0;
      }

      #iphone_alert .button:active {
        background: -webkit-linear-gradient(top, rgba(90, 95, 102, 0.7) 0%, rgba(41, 47, 57, 0.7) 50%, rgba(23, 31, 40, 0.7) 50%, rgba(36, 44, 53, 0.7) 100%);
        background:    -moz-linear-gradient(top, rgba(90, 95, 102, 0.7) 0%, rgba(41, 47, 57, 0.7) 50%, rgba(23, 31, 40, 0.7) 50%, rgba(36, 44, 53, 0.7) 100%);
        background:     -ms-linear-gradient(top, rgba(90, 95, 102, 0.7) 0%, rgba(41, 47, 57, 0.7) 50%, rgba(23, 31, 40, 0.7) 50%, rgba(36, 44, 53, 0.7) 100%);
        background:      -o-linear-gradient(top, rgba(90, 95, 102, 0.7) 0%, rgba(41, 47, 57, 0.7) 50%, rgba(23, 31, 40, 0.7) 50%, rgba(36, 44, 53, 0.7) 100%);
        background:         linear-gradient(top, rgba(90, 95, 102, 0.7) 0%, rgba(41, 47, 57, 0.7) 50%, rgba(23, 31, 40, 0.7) 50%, rgba(36, 44, 53, 0.7) 100%);
        -ms-filter: progid:DXImageTransform.Microsoft.gradient(startColorStr='#5a5f66', EndColorStr='#242c35', GradientType=0);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#5a5f66', endColorstr='#242c35', GradientType=0);
        -webkit-box-shadow: #454f69 0 1px 0;
           -moz-box-shadow: #454f69 0 1px 0;
                box-shadow: #454f69 0 1px 0;
      }
      .button {
        float: left; display: block; padding: 2px 15px 4px; color: #c6c6c6; font: bold 13px/22px "Helvetica Neue", Arial, Helvetica, Geneva, sans-serif; text-decoration: none; text-shadow: rgba(0,0,0,0.7) 0 1px 1px; cursor: pointer; margin: 0 5px;
        background: -webkit-linear-gradient(top, rgba(255,255,255,0.1), rgba(0,0,0,0.1));
        background:    -moz-linear-gradient(top, rgba(255,255,255,0.1), rgba(0,0,0,0.1));
        background:     -ms-linear-gradient(top, rgba(255,255,255,0.1), rgba(0,0,0,0.1));
        background:      -o-linear-gradient(top, rgba(255,255,255,0.1), rgba(0,0,0,0.1));
        background:         linear-gradient(top, rgba(255,255,255,0.1), rgba(0,0,0,0.1));
        -webkit-border-radius:20px;
           -moz-border-radius:20px;
                border-radius:20px; 
        -webkit-box-shadow: rgba(0,0,0,0.4) 0 1px 0, inset rgba(255,255,255,0.2) 0 1px 0;
           -moz-box-shadow: rgba(0,0,0,0.4) 0 1px 0, inset rgba(255,255,255,0.2) 0 1px 0;
                box-shadow: rgba(0,0,0,0.4) 0 1px 0, inset rgba(255,255,255,0.2) 0 1px 0;
      }

      .button:hover {
        background: -webkit-linear-gradient(top, rgba(255,255,255,0.2), rgba(0,0,0,0.1));
        background:    -moz-linear-gradient(top, rgba(255,255,255,0.2), rgba(0,0,0,0.1));
        background:     -ms-linear-gradient(top, rgba(255,255,255,0.2), rgba(0,0,0,0.1));
        background:      -o-linear-gradient(top, rgba(255,255,255,0.2), rgba(0,0,0,0.1));
        background:         linear-gradient(top, rgba(255,255,255,0.2), rgba(0,0,0,0.1));
      }

      .button:active {
        background: -webkit-linear-gradient(bottom, rgba(255,255,255,0.1), rgba(0,0,0,0.1));
        background:    -moz-linear-gradient(bottom, rgba(255,255,255,0.1), rgba(0,0,0,0.1));
        background:     -ms-linear-gradient(bottom, rgba(255,255,255,0.1), rgba(0,0,0,0.1));
        background:      -o-linear-gradient(bottom, rgba(255,255,255,0.1), rgba(0,0,0,0.1));
        background:         linear-gradient(bottom, rgba(255,255,255,0.1), rgba(0,0,0,0.1));
      }
    </style>
  </head>
  <body>
  <c:if test="${empty param['url'] && empty param['device'] && empty param['orientation']}"> 
  	<table class="dialog" cellspacing="0" cellpadding="0">
		<tbody><tr><td>
			<table class="dialogbox" cellpadding="0" cellspacing="0">
				<tbody><tr><td>
					<div class="dialoghead" unselectable="on">Responsive Preview</div><div class="dialogcontent" unselectable="on">
					<!-- dialogcontent start -->
						<table class="xmlTable" cellpadding="0" cellspacing="0">
							<tbody><tr><td colspan="5">
	</c:if>
	
    							<div id="help">
      								<form name="form" id="form" action="">
        								<table style="width:100%;">
          									<tr>
            									<td>URL de la page:</td>
            									<td><input type="text" name="url" id="url" style="width: 99%" class="xmlInput" value="<%=onlineLink%>" readonly/></td>
          									</tr>
          									<tr>
            									<td>Terminal mobile:</td>
            									<td>
              										<label id="deviceiphone"><input type="radio" name="device" value="iphone" checked="checked"/><img src="<cms:link>/system/modules/com.eurelis.opencms.admin/resources/iphone.png</cms:link>" width="30"/>iPhone</label>  
              										<label id="deviceipad"><input type="radio" name="device" value="ipad"/><img src="<cms:link>/system/modules/com.eurelis.opencms.admin/resources/ipad.png</cms:link>" width="80"/>iPad</label>  
            									</td>
          									</tr>
          									<tr>
            									<td>Orientation:</td>
            									<td>
              										<label id="orientationportrait"><input type="radio" name="orientation" value="portrait" checked="checked"/><img src="<cms:link>/system/modules/com.eurelis.opencms.admin/resources/iphone.png</cms:link>" width="30"/>Portrait</label>  
              										<label id="orientationlandscape"><input type="radio" name="orientation" value="landscape"/><img src="<cms:link>/system/modules/com.eurelis.opencms.admin/resources/iphone.png</cms:link>" width="30"/>Paysage</label>  
            									</td>
          									</tr>
          									<tr>
            									<td colspan="2">
													<div class="dialogbuttons" unselectable="on">
														<input class="dialogbutton" type="submit" value="Ok" name="ok">
														<input class="dialogbutton" type="button" onclick="this.form.submit();" value="Cancel" name="cancel">
														<input type="hidden" name="action" value="submit">
													</div>
												
												</td>
          									</tr>
        								</table>
      								</form>
    							</div>
    							<div id="ios">
      								<a id="rotate" href="javascript:void(0)"><img/></a>
      								<iframe></iframe>		  
      								<div id="iphone_alert" class="iphone_alert">
        								<div class="alert">
          									<div class="text"><b>Compatibilité navigateur</b>Veuillez utiliser Safari pour une compatibilité maximale.</div>
          									<div class="button f_left" onclick="$('#iphone_alert').hide()">Annuler</div>
          									<a class="button f_right light" onclick="$('#iphone_alert').hide()">OK</a>
          									<br class="clear" />
		    							</div>
      								</div>
    							</div>
								
<c:if test="${empty param['url'] && empty param['device'] && empty param['orientation']}"> 								
							</td></tr></tbody>
						</table>
					</div>
				</td></tr></tbody>
			</table>
		</td></tr></tbody>
	</table>
</c:if>
  </body>
</html>
<%
	}
%>
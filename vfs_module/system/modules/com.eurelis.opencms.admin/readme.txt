<div id="accordion">
	<h3>Presentation</h3>
	<div class="content">
	
	</div>
	<h3>Historique</h3>
	<div class="content">
	
	</div>
	<h3>Installation / Configuration</h3>
	<div class="content">
		<h4>Ajout de l'action contextuelle Files advanced search sur les Folder</h4>
		<p>Dans /web-inf/config/opencms.workplace.xml, ajouter </p>
		<code>&lt;separator />
			&lt;entry key="GUI_EXPLORER_CONTEXT_EURELISFILEINFORMATION_0" uri="commons/eurelis/file_information.jsp" rule="standard" /></code>
		
	</div>
	<h3>Personnalisations</h3>
	<div class="content">
	
	</div>
</div>
<link href="../../resources/jquery/css/ui-ocms/jquery.ui.css" rel="stylesheet" type="text/css"></link>
<style type="text/css">
.ui-accordion-header{padding-left: 30px;}
.content{height:auto !important;}

</style>
<script type="text/javascript" src="../../resources/jquery/packed/jquery.js"></script>
<script type="text/javascript" src="../../resources/jquery/packed/jquery.ui.js"></script>
<script type="text/javascript">
$(function() {
    $( "#accordion" ).accordion();
});
</script>
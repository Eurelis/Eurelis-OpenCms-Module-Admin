<div id="accordion">
	<h3>Presentation</h3>
	<div class="content">
		<h4>Outils de la vue Administration</h4>
		<ul>
			<li>System information : Versions Java, OS, Graphs et valeurs de CPU, memoires, bases de donnees...</li>
			<li>File information : Recherche et suppression de fichiers avec filtres racine, dates, et taille de fichiers</li>
		</ul>
		<h4>Actions contextuelles</h4>
		<ul>
			<li>File information : Recherche et suppression de fichiers avec filtres racine, dates, et taille de fichiers</li>
			<li>Responsive preview : Affichage d'un page en format iPad iPhone dans une nouvelle fenetre</li>
		</ul>
		<h4>Autres</h4>
		<ul>
			<li>Transformation xmlcontent : Suppression / Deplacement des champs d'un xmlcontent</li>
		</ul>
	</div>
	<h3>Historique</h3>
	<div class="content">
	
	</div>
	<h3>Installation / Configuration</h3>
	<div class="content">
		<h4>Ajout de l'action contextuelle "Files advanced search" sur le type -folder-</h4>
		<p>Dans /web-inf/config/opencms.workplace.xml, ajouter </p>
		<code>&lt;separator />
			&lt;entry key="GUI_EXPLORER_CONTEXT_EURELISFILEINFORMATION_0" uri="commons/eurelis/file_information.jsp" rule="standard" /></code>
		<h4>Ajout de l'action contextuelle "Responsive preview" sur le type -containerpage-</h4>
		<p>Est normalement ajoute par une surcharge </p>
		<code>&lt;separator />
			&lt;entry key="GUI_EXPLORER_CONTEXT_RESPONSIVE_PREVIEW_0" uri="/system/modules/com.eurelis.opencms.admin/elements/responsivepreview.jsp" rule="standard" /></code>
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
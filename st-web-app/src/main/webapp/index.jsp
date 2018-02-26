<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="itfn" uri="http://www.systematictesting.com/framework-core-functions/"%>
<!DOCTYPE html>
<html lang="en" ng-app="mainApp" ng-controller="siteMainCtrl">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>QDOS | Systematic Testing Ltd.</title>
	
		<link href="//fonts.googleapis.com/css?family=Open+Sans:400,700" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" href="libs/bootstrap/ui-bootstrap-custom-0.13.0-csp.css" media="screen">
		<link rel="stylesheet" type="text/css" href="libs/bootstrap/css/bootstrap.min.css" media="screen">
		<link rel="stylesheet" type="text/css" href="libs/bootstrap/css/bootstrap-switch.min.css" media="screen">
		<link rel="stylesheet" type="text/css" href="libs/bootstrap/css/bootstrap-theme.min.css" media="screen">
		<link rel="stylesheet" type="text/css" href="libs/angular-plugins/ng-table/ng-table.min.css" media="screen">
		<link rel="stylesheet" type="text/css" href="libs/font-awesome/css/font-awesome.min.css" media="screen">
		<link rel="stylesheet" type="text/css" href="libs/c3/c3.min.css" media="screen">
		<link rel="stylesheet" type="text/css" href="libs/angular-plugins/meditor/meditor.min.css" media="screen">
		<link rel="stylesheet" type="text/css" href="libs/angular-plugins/ng-tags-input/ng-tags-input.min.css" media="screen">
		<!-- <link rel="stylesheet" type="text/css" href="libs/angular-plugins/ng-tags-input/ng-tags-input.bootstrap.min.css" media="screen"> -->
		
		<link rel="stylesheet" type="text/css" href="css/main.css" media="screen">
		<!-- Dynamically Modules CSS must be included -->
		<link rel="stylesheet" type="text/css" href="css/module/sidebar/sidebar.css" media="screen">
		<link rel="stylesheet" type="text/css" href="css/module/sitemgr/sitemgr.css" media="screen">
		<link rel="stylesheet" type="text/css" href="css/module/navbar/navbar.css" media="screen">
		<link rel="stylesheet" type="text/css" href="css/module/qdos/qdos.css" media="screen">
		<link rel="stylesheet" type="text/css" href="css/module/testcatalog/testcatalog.css" media="screen">
	</head>
	<body>
		<%-- <c:if test="${email ne null}">
			<script>
				//ga(‘set’, ‘&uid’, {{${email}}});
				//ga(‘set’, ‘&automationCoverage’, {{${itfn:isRequestedFeatureAllowed(feature.automationCoverage, featureList)}}});
				//ga(‘set’, ‘&moreVersionHistory’, {{${itfn:isRequestedFeatureAllowed(feature.moreVersionHistory, featureList)}}});
				//ga(‘set’, ‘&siteManager’, {{${itfn:isRequestedFeatureAllowed(feature.siteManager, featureList)}}});
			</script>
		</c:if> --%>


	
		<div ng-class="isSessionValid?'':'displayNone'">
			<div id="wrapper" ng-class="sidebarRootLevelClass?'toggled':''">
				<div ng-include="'templates/views/modulesMenu.html'"></div>
				<div id="page-content-wrapper">
					<div class="container-fluid">
						<header class="row">
							<div ng-include="'templates/module/navbar/view/navbar.html'"></div>
						</header>
						<div class="row">
							<div class="col-md-12" ng-view>
							</div>
						</div>
						<footer class="row"></footer>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="libs/jquery/jquery-2.1.1.min.js"></script>
		<!-- <script type="text/javascript" src="libs/angular/angular.min.js"></script>
		<script type="text/javascript" src="libs/angular/angular-sanitize.min.js"></script>
		<script type="text/javascript" src="libs/angular/angular-aria.min.js"></script>
		<script type="text/javascript" src="libs/angular/angular-route.min.js"></script>
		<script type="text/javascript" src="libs/angular/angular-resource.min.js"></script>
		<script type="text/javascript" src="libs/angular/angular-loader.min.js"></script>
		<script type="text/javascript" src="libs/angular/angular-cookies.min.js"></script> WORKING-->
		
		<script type="text/javascript" src="libs/angular-1.3.20/angular.min.js"></script>
		<script type="text/javascript" src="libs/angular-1.3.20/angular-sanitize.min.js"></script>
		<script type="text/javascript" src="libs/angular-1.3.20/angular-aria.min.js"></script>
		<script type="text/javascript" src="libs/angular-1.3.20/angular-route.min.js"></script>
		<script type="text/javascript" src="libs/angular-1.3.20/angular-resource.min.js"></script>
		<script type="text/javascript" src="libs/angular-1.3.20/angular-loader.min.js"></script>
		<script type="text/javascript" src="libs/angular-1.3.20/angular-cookies.min.js"></script>
		<script type="text/javascript" src="libs/angular-1.3.20/angular-animate.min.js"></script>
		<script type="text/javascript" src="libs/angular-1.3.20/angular-scenario.js"></script>
		<script type="text/javascript" src="libs/angular-1.3.20/angular-touch.min.js"></script>
		
		<script type="text/javascript" src="libs/bootstrap/bootstrap.min.js"></script>
		<script type="text/javascript" src="libs/bootstrap/bootstrap-switch.min.js"></script>
		<!-- <script type="text/javascript" src="libs/bootstrap/ui-bootstrap-custom-0.13.0.min.js"></script> -->
		<!-- <script type="text/javascript" src="libs/bootstrap/ui-bootstrap-custom-tpls-0.13.0.js"></script> WORKING -->
		<script type="text/javascript" src="libs/bootstrap/ui-bootstrap-tpls-0.14.3.min.js"></script>
		
		<script type="text/javascript" src="libs/d3/d3.v3.min.js"></script>
		<script type="text/javascript" src="libs/c3/c3.min.js"></script>
		<script type="text/javascript" src="libs/angular-plugins/c3-angular/c3-angular.min.js"></script>
		<script type="text/javascript" src="libs/angular-plugins/ng-table/ng-table.min.js"></script>
		<script type="text/javascript" src="libs/angular-plugins/ng-storage/ngStorage.min.js"></script>
		<script type="text/javascript" src="libs/angular-plugins/angular-file-upload/angular-file-upload.min.js"></script>
		<script type="text/javascript" src="libs/angular-plugins/angular-ngAnimate/ngAnimate.js"></script>
		<script type="text/javascript" src="libs/angular-plugins/meditor/meditor.min.js"></script>
		<script type="text/javascript" src="libs/angular-plugins/ng-tags-input/ng-tags-input.min.js"></script>
		
		<script type="text/javascript" src="app/SUtils.js"></script>
		<script type="text/javascript" src="app/mainApp.js"></script>
		<script type="text/javascript" src="app/mainApp.controllers.js"></script>
		<script type="text/javascript" src="app/mainApp.filters.js"></script>
		<script type="text/javascript" src="app/mainApp.directives.js"></script>
	
		<!-- Dynamically Modules JS must be included -->
		<script type="text/javascript" src="app/module/navbar/navBar.js"></script>
		<script type="text/javascript" src="app/module/sidebar/sidebar.js"></script>
		<script type="text/javascript" src="app/module/sitemgr/sitemgr.js"></script>
		<script type="text/javascript" src="app/module/qdos/qdos.js"></script>
		<script type="text/javascript" src="app/module/testcatalog/testcatalog.js"></script>
		<script type="text/javascript" src="app/module/testcatalog/testcatalog.services.js"></script>
		<script type="text/javascript" src="app/module/testcatalog/testcatalog.controllers.js"></script>
	</body>
</html>
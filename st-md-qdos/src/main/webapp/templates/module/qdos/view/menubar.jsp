<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="itfn" uri="http://www.systematictesting.com/framework-core-functions/"%>

<nav  class="navbar navbar-default">
	<div class="container-fluid">
	  <!-- Brand and toggle get grouped for better mobile display -->
	  <div class="navbar-header">
	    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-2" aria-expanded="false">
	      <span class="sr-only">Toggle navigation</span>
	      <span class="icon-bar"></span>
	      <span class="icon-bar"></span>
	      <span class="icon-bar"></span>
	    </button>
	    <a class="navbar-brand header-icon desaturateIcons" href="#/qdos"><span class="qdos-header-icon"/><span class="module-header-label">&nbsp;Reports</span></a>
	  </div>

	  <!-- Collect the nav links, forms, and other content for toggling -->
	  <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-2">
	    <ul class="nav navbar-nav navbar-right">
	      <c:if test="${itfn:isRequestedFeatureAllowed(feature.automationCoverage, featureList)}">
	      	<li class="dropdown">
	      		<a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-scale" aria-hidden="true"></span>&nbsp;&nbsp;Automation Coverage&nbsp;<span class="caret"></span></a>
	      		<ul class="dropdown-menu">
	      			<li><a href="#/qdos/automationCoverage/view"><span class="glyphicon glyphicon-globe" aria-hidden="true"></span>&nbsp;&nbsp;Public Sites</a></li>
	      			<li><a href="#/qdos/dashboard-automationCoverage/view"><span class="glyphicon glyphicon-globe" aria-hidden="true"></span>&nbsp;&nbsp;My Sites</a></li>
	      		</ul>
	      	</li>
	      </c:if>
	      <li class="dropdown">
	      	<a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-modal-window" aria-hidden="true"></span>&nbsp;&nbsp;Test Reports&nbsp;<span class="caret"></span></a>
			<ul class="dropdown-menu">
				<li><a href="#/qdos"><span class="glyphicon glyphicon-globe" aria-hidden="true"></span>&nbsp;&nbsp;Public Sites</a></li>
	            <li><a href="#/qdos/dashboard/view"><span class="glyphicon glyphicon-globe" aria-hidden="true"></span>&nbsp;&nbsp;My Sites</a></li>
	        </ul>
	      </li>
	    </ul>
	  </div><!-- /.navbar-collapse -->
	</div><!-- /.container-fluid -->
</nav>
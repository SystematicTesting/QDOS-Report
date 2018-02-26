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
	    <a class="navbar-brand header-icon" href="#/sitemgr">
	    <span class="glyphicon glyphicon-cog" aria-hidden="true"></span> <span class="module-header-label"> &nbsp;Site Manager</span>
	    </a>
	  </div>

	  <!-- Collect the nav links, forms, and other content for toggling -->
	  <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-2">
	    <ul class="nav navbar-nav navbar-right">
	    	<li><a href="#/sitemgr/add"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>&nbsp;&nbsp;Add Site</a></li>
	      <!-- <li><a href="#"><span class="glyphicon glyphicon-modal-window" aria-hidden="true"></span>&nbsp;&nbsp;Functional</a></li>
	      <li><a href="#"><span class="glyphicon glyphicon-scale" aria-hidden="true"></span>&nbsp;&nbsp;Performance</a></li> -->
	      <!-- <li><a href="#/qdos/emailSubscription/view"><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>&nbsp;&nbsp;Email Subscription</a></li> -->
	    </ul>
	  </div><!-- /.navbar-collapse -->
	</div><!-- /.container-fluid -->
</nav>
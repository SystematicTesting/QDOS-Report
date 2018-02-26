<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="itfn" uri="http://www.systematictesting.com/framework-core-functions/"%>

<nav  class="navbar navbar-default">
	<div class="container-fluid">
	  <div class="navbar-header">
	    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-2" aria-expanded="false">
	      <span class="sr-only">Toggle navigation</span>
	      <span class="icon-bar"></span>
	      <span class="icon-bar"></span>
	      <span class="icon-bar"></span>
	    </button>
	    <a class="navbar-brand header-icon" href="#/testcatalog">
	    <span class="glyphicon glyphicon-globe" aria-hidden="true"></span> <span class="module-header-label"> &nbsp;Sites</span>
	    </a>
	  </div>

	  <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-2">
	    <ul class="nav navbar-nav navbar-right">
	      <li><a href="#/sitemgr/add"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>&nbsp;&nbsp;Add Site</a></li>
	      <li><a href="#/testcatalog/mysites/view"><span class="glyphicon glyphicon-modal-window" aria-hidden="true"></span>&nbsp;&nbsp;My Sites</a></li>
	    </ul>
	  </div>
	</div>
</nav>
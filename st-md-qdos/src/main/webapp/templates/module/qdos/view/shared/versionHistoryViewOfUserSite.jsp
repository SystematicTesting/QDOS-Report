<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="itfn" uri="http://www.systematictesting.com/framework-core-functions/"%>

<div id="version-history" class="row grey-background show-box">
	<div class="col-md-12">	
		<h4>Trend Analysis on Versions</h4>
	</div>
</div>
  	<div class="row">
    <div class="col-md-12 show-box">
  <br/>
  <c3chart bindto-id="versionHistoryGraph" chart-data="graphData.datapoints" chart-columns="graphData.datacolumns" chart-x="graphData.datax" show-labels="true" show-subchart="false" enable-zoom="true">
	  <chart-size chart-height="450"/>
	  <chart-axis>
		<chart-axis-x axis-position="outer-center" axis-type="category">
			  <chart-axis-x-tick tick-culling="4" tick-fit="true" tick-rotate="50"/>
		</chart-axis-x>
		<chart-axis-y axis-id="y" axis-position="outer-right" axis-label="Test Cases" padding-top="50" padding-bottom="1"/>
	  </chart-axis>
	  <chart-grid show-x="false" show-y="true"/>
  </c3chart>
  <div class="breadcrumb">
  	<h5>
  		<span>
	  		Number of versions on this page : 
			<a href="{{moduleAccessURL}}/{{email | encodeURI}}/{{siteName | encodeURI}}/10" class="highlight-box" ng-class="numberOfRecords==10?'active':''">&nbsp;10&nbsp;</a>
			<a href="{{moduleAccessURL}}/{{email | encodeURI}}/{{siteName | encodeURI}}/25" class="highlight-box" ng-class="numberOfRecords==25?'active':''">&nbsp;25&nbsp;</a>
			<a href="{{moduleAccessURL}}/{{email | encodeURI}}/{{siteName | encodeURI}}/50" class="highlight-box" ng-class="numberOfRecords==50?'active':''">&nbsp;50&nbsp;</a>
			<a href="{{moduleAccessURL}}/{{email | encodeURI}}/{{siteName | encodeURI}}/75" class="highlight-box" ng-class="numberOfRecords==75?'active':''">&nbsp;75&nbsp;</a>
			<a href="{{moduleAccessURL}}/{{email | encodeURI}}/{{siteName | encodeURI}}/100" class="highlight-box" ng-class="numberOfRecords==100?'active':''">&nbsp;100&nbsp;</a>
			<a href="{{moduleAccessURL}}/{{email | encodeURI}}/{{siteName | encodeURI}}/125" class="highlight-box" ng-class="numberOfRecords==125?'active':''">&nbsp;125&nbsp;</a>
			<a href="{{moduleAccessURL}}/{{email | encodeURI}}/{{siteName | encodeURI}}/150" class="highlight-box" ng-class="numberOfRecords==150?'active':''">&nbsp;150&nbsp;</a>
			<a href="{{moduleAccessURL}}/{{email | encodeURI}}/{{siteName | encodeURI}}/175" class="highlight-box" ng-class="numberOfRecords==175?'active':''">&nbsp;175&nbsp;</a>
			<a href="{{moduleAccessURL}}/{{email | encodeURI}}/{{siteName | encodeURI}}/200" class="highlight-box" ng-class="numberOfRecords==200?'active':''">&nbsp;200&nbsp;</a>
		</span>
		<span style="float:right;">
			Graph Type :
			<a href="{{moduleAccessURL}}/{{email | encodeURI}}/{{siteName | encodeURI}}/{{numberOfRecords}}/graphType/bar" class="highlight-box" ng-class="graphType=='bar'?'active':''">&nbsp;Bar&nbsp;</a>
			<a href="{{moduleAccessURL}}/{{email | encodeURI}}/{{siteName | encodeURI}}/{{numberOfRecords}}" class="highlight-box" ng-class="graphType=='line'?'active':''">&nbsp;Line&nbsp;</a>	
		</span>
	</h5>
  </div>
    </div>
  	</div>
<div class="row">
	<div class="col-sm-12">	
		<br/>
	</div>
</div>

<div id="version-details" class="row grey-background show-box">
	<div class="col-md-12">	
		<h4>Version Information</h4>
	</div>
</div>
<div class="row">
	<div class="col-md-12">	
		<div class="row show-box grey-background">
			<div class="col-sm-12">
				<input type="text" class="form-control" placeholder="Search Here" ng-model="searchQuery">
				<br>
			</div>
		</div>
		<div class="row bg-info show-dark-box">
			<div class="col-md-5">
				<div class="row">
					<div class="col-xs-3 show-dark-box">
						<h6><span class="">Report Version</span></h6>
					</div>
					<div class="col-xs-3 show-dark-box">
						<h6><span class="">Site Version</span></h6>
					</div>
					<div class="col-xs-3 show-dark-box">
						<h6><span class="">Operating System</span></h6>
					</div>
					<div class="col-xs-3 show-dark-box">
						<h6><span class="">Browser Name</span></h6>
					</div>
				</div>
			</div>
			<div class="col-md-3">
				<div class="row">
					<div class="col-xs-6 show-dark-box">
						<h6><span class="">Start Time</span></h6>
					</div>
					<div class="col-xs-6 show-dark-box">
						<h6><span class="">Execution Time</span></h6>
					</div>
				</div>
			</div>
			<div class="col-md-4">
				<h6><span class="">Summary of Test Results</span></h6>
			</div>
		</div>
		
		
		<div class="row show-box version-row-data highlight-box" ng-repeat="item in tableData | reverse | filter:searchQuery as results">
			<a href="{{moduleAccessURL}}/{{email | encodeURI}}/{{siteName | encodeURI}}/version/{{item.versionNumber}}">
				<div class="col-md-5">
					<div class="row">
						<div class="col-xs-3">
							<span class="">{{item.versionNumber}}</span>
						</div>
						<div class="col-xs-3">
							<span class="">{{item.catalogVersion}}</span>
						</div>
						<div class="col-xs-3">
							<span class="">{{item.operatingSystem}}</span>
						</div>
						<div class="col-xs-3">
							<span class="">{{item.browser}}</span>
						</div>
					</div>
				</div>
				<div class="col-md-3">
					<div class="row">
						<div class="col-xs-6">
							<span class="">{{item.startTime}}</span>
						</div>
						<div class="col-xs-6">
							<span class="">{{item.totalTime | secondsToDateTime}}</span>
						</div>
					</div>
				</div>
				<div class="col-md-4 progress version-progress">
					<div class="progress-bar passed-testcases" ng-show="item.totalTestCases" style="width: {{item.passPercentile | createPercentageBarValue}}" ng-bind-html="item.passPercentile | showPercentileText">
					</div>
					<div class="progress-bar failed-testcases" ng-show="item.totalTestCases" style="width: {{item.failPercentile | createPercentageBarValue}}" ng-bind-html="item.failPercentile | showPercentileText">
					</div>
					<div class="progress-bar aborted-testcases" ng-show="item.totalTestCases" style="width: {{item.abortPercentile | createPercentageBarValue}}" ng-bind-html="item.abortPercentile | showPercentileText">
					</div>
					<div class="progress-bar progress-bar-striped active progress-bar-success" ng-hide="item.totalTestCases" style="width: 100%">
					</div>
				</div>
			</a>
		</div>
		
	</div>
</div>

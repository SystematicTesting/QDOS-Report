<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="itfn" uri="http://www.systematictesting.com/framework-core-functions/"%>

	<div class="row grey-background show-box">
		<div class="col-md-12">	
			<h4>Test Case Details</h4>
		</div>
	  </div>
	  <div class="row">
	  	<div class="col-md-6">
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>Test Case ID</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5>{{testcase.testCaseId}}</h5>
	  			</div>
	  		</div>
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>Test Case Description</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5><div ng-bind-html="testcase.testCaseName"></div></h5>
	  			</div>
	  		</div>
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>Mode</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5>{{testcase.testCaseMode}}</h5>
	  			</div>
	  		</div>
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>Suite Name</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5>{{suiteName}}</h5>
	  			</div>
	  		</div>
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>Version</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5>{{serverData.versionNumber}}</h5>
	  			</div>
	  		</div>
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>Site Name</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5>{{serverData.siteName}}</h5>
	  			</div>
	  		</div>
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>Created By</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5>{{serverData.email}}</h5>
	  			</div>
	  		</div>
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>Element Key:Values</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<button type="button" class="btn btn-default" aria-label="Key value pairs" ng-click="showElementKeyValuePairs()">
					  <span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>&nbsp;&nbsp;Key:Values
					</button>
	  			</div>
	  		</div>
	  		<div class="row show-box" ng-hide="selectedDataSetId==''">
	  			<div class="col-xs-6">
	  				<h5>Select Data Set</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<div class="btn-group">
					  <button type="button" class="btn btn-default">{{selectedDataSetId}}</button>
					  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					    <span class="caret"></span>
					  </button>
					  
					  <ul class="dropdown-menu" aria-labelledby="dataSetOfTestCase">
					    <li ng-repeat="row in testcase.datasets.array" ng-click="changeSelectedDataSet(row)"><a href>{{row.id}}</a></li>
					  </ul>
					</div>
	  			</div>
	  		</div>
	  	</div>
	  	<div class="col-md-6">
	  		<div class="row show-box">
	  			<div class="col-md-12">
	  				<br/>
		  			<c3chart bindto-id="suiteDetails" chart-data="graphData.datapoints" chart-columns="graphData.datacolumns" chart-x="graphData.datax" show-labels="true">
					  	<chart-size chart-height="260"/>
					  	<chart-axis>
							<chart-axis-x axis-position="outer-center" axis-type="category">
							<chart-axis-x-tick tick-culling="4" tick-fit="true" tick-rotate="0"/>
						</chart-axis-x>
						<chart-axis-y axis-id="y" axis-position="outer-right" axis-label="Number of Steps" padding-top="50" padding-bottom="1"/>
					  	</chart-axis>
					  	<chart-grid show-x="false" show-y="true"/>
				  	</c3chart>
			  	</div>	
	  		</div>
	  		<div class="row show-box" ng-hide="selectedDataSetId==''">
	  			<div class="col-xs-6">
	  				<h5>Data Set Description</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5>{{testcase.datasets[selectedDataSetId]}}</h5>
	  			</div>
	  		</div>
	  	</div>
	  </div>
	  <br/>
	  <div class="row grey-background show-box">
		<div class="col-xs-12">	
			<h4>Test Steps</h4>
		</div>
	  </div>
	  <div class="row">
	  	<table ng-table="tableParams" show-filter="true" class="table table-bordered table-striped">
			<tr ng-repeat="row in $data">
				<td title="'Step ID'" filter="{stepId: 'text'}" sortable="'stepId'">{{row.stepId}}</td>
				<td title="'Step Description'" filter="{stepDescription: 'text'}" sortable="'stepDescription'"><div ng-bind-html="row.stepDescription"></div></td>
				<td title="'Action'" filter="{action: 'text'}" sortable="'action'">{{row.stepKeyword}}</td>
				<td title="'Element Type'" filter="{elementType: 'text'}" sortable="'elementType'" class="hide-on-mobile" header-class="'hide-on-mobile'">{{row.elementType}}</td>
				<td title="'Element Key'" filter="{elementKey: 'text'}" sortable="'elementKey'" class="hide-on-mobile" header-class="'hide-on-mobile'"><span tooltip="{{getValueOfKey(row.elementKey)}}">{{row.elementKey}}</span></td>
				<td title="'Element Value'" filter="{elementValue: 'text'}" sortable="'elementValue'" class="hide-on-mobile" header-class="'hide-on-mobile'">{{row.elementValue}}</td>
				<td title="'Proceed on FAIL'" filter="{proceedOnFail: 'text'}" sortable="'proceedOnFail'">{{row.proceedOnFail}}</td>
			</tr>
	    </table>
	  </div>
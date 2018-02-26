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
	  	<div class="col-md-4">
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
	  				<h5>Site Version</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5>{{serverData.catalogVersion}}</h5>
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
	  	</div>
	  	<div class="col-md-4">
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>Test Case ID</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5>{{testCaseData.testCaseId}}</h5>
	  			</div>
	  		</div>
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>Description</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5>{{testCaseData.testCaseName}}</h5>
	  			</div>
	  		</div>
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>Status</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5><span ng-class="testCaseData.status=='RUNNING'?'runningStatus':testCaseData.status=='ABORTED'?'abortedStatus':''">{{testCaseData.status}}</span></h5>
	  			</div>
	  		</div>
	  	</div>
	  	<div class="col-md-4">
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>Start Time</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5>{{testCaseData.startTime}}</h5>
	  			</div>
	  		</div>
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>End Time</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5><span ng-class="testCaseData.endTime=='RUNNING'?'runningStatus':testCaseData.endTime=='ABORTED'?'abortedStatus':''">{{testCaseData.endTime}}</span></h5>
	  			</div>
	  		</div>
	  		<div class="row show-box">
	  			<div class="col-xs-6">
	  				<h5>Total Time</h5>
	  			</div>
	  			<div class="col-xs-6">
	  				<h5>{{testCaseData.duration | secondsToDateTime}}</h5>
	  			</div>
	  		</div>
	  	</div>
	  </div>
	  <br/>
	  <div class="row grey-background show-box">
		<div class="col-md-12">	
			<h4>List of Test Steps</h4>
		</div>
	  </div>
	  <div class="row">
	  	<table ng-table="tableParams" show-filter="true" class="table table-bordered table-striped">
			<tr ng-repeat="row in $data">
				<td title="'Data Set ID'" filter="{dataSetId: 'text'}" sortable="'dataSetId'">{{row.dataSetId}}</td>
				<td title="'Step ID'" filter="{stepId: 'text'}" sortable="'stepId'">{{row.stepId}}</td>
				<td title="'Step Description'" filter="{stepDescription: 'text'}" sortable="'stepDescription'">{{row.stepDescription}}</td>
				<td title="'Seconds'" filter="{duration: 'text'}" sortable="'duration'" class="hide-on-mobile" header-class="'hide-on-mobile'">{{row.duration | secondsToDateTime}}</td>
				<td title="'Proceed on FAIL'" filter="{proceedOnFail: 'text'}" sortable="'proceedOnFail'" class="hide-on-mobile" header-class="'hide-on-mobile'">{{row.proceedOnFail}}</td>
				<td title="'System Message'" filter="{systemMessage: 'text'}" sortable="'systemMessage'" class="hide-on-mobile" header-class="'hide-on-mobile'">{{row.systemMessage}}</td>
				<td title="'Keyword'" filter="{stepKeyword: 'text'}" sortable="'stepKeyword'" class="hide-on-mobile" header-class="'hide-on-mobile'">{{row.stepKeyword}}</td>
				<td title="'Status'" filter="{stepStatus: 'text'}" sortable="'stepStatus'" class="{{row.stepStatusClass}}">{{row.stepStatus}}</td>
				<td title="'Screenshot'" class="hide-on-mobile align-text-center" header-class="'hide-on-mobile'">
					<div ng-show="row.stepScreenShot != 'DISABLED'" ng-click="showScreenshot(testCaseData.testCaseId, row.stepId, row.stepScreenShot)">
						<div ng-bind-html="row.stepScreenShot | addThumbnailImageElement"></div>
					</div>
					<span ng-show="row.stepScreenShot == 'DISABLED'" class="disabled">{{row.stepScreenShot}}</span>
				</td>
			</tr>
	    </table>
	  </div>
var qdos = angular.module('qdos',['gridshore.c3js.chart','ngCookies']);

qdos.value("DetailedSharedSitesUrl","/module/qdos/sreport/detailedSharedSites.rest?status=ACTIVE");
qdos.value("UserSharedSitesUrl","/module/qdos/sreport/userSharedSites.rest?status=ACTIVE");
qdos.value("AllMySitesUrl","/module/qdos/sreport/userAllSites.rest?status=ACTIVE");
qdos.value("VersionHistoryOfUserSiteUrl","/module/qdos/sreport/versionHistoryOfUserSite.rest");
qdos.value("VersionDetailsOfUserSingleSiteUrl","/module/qdos/sreport/versionDetailsOfSite.rest");
qdos.value("UpdateMySiteShareStatus","/userSite/saveMySitesStatus.rest");

qdos.service("qdosService", function (DetailedSharedSitesUrl, UserSharedSitesUrl, VersionHistoryOfUserSiteUrl, VersionDetailsOfUserSingleSiteUrl, AllMySitesUrl, $http) {
	var serviceObject = this;

	this.getDetailedSharedSites = function($scope){
		var responsePromise = $http.get(DetailedSharedSitesUrl);
		responsePromise.success(function(data, status, headers, config) {
						var result = [];
						if (data!=null && Object.prototype.toString.call(data) === '[object Array]'){
							var numberOfRowsRequired = Math.ceil(data.length/3);
							var dataIndex = 0;
							for (var row=0;row<numberOfRowsRequired;row++){
								result[row] = [];
								for (var rowArrayIndex=0;rowArrayIndex<3;rowArrayIndex++){
									if (data[dataIndex]!=null){
										result[row][rowArrayIndex]=data[dataIndex];
										dataIndex++;
									}
								}
							}
						} else {
							SUtils.warn(SUtils.appConfig.qdosLogPrefix + "getDetailedSharedSites() :: Expected Response of Array is not available on Response of URL : "+DetailedSharedSitesUrl);
						}
	                    $scope.detailedSharedSites = result;
	                    $scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.qdosLogPrefix + "getDetailedSharedSites() :: AJAX Request failed on URL : "+DetailedSharedSitesUrl);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
	};
	this.getUserSharedSites = function($scope, email){
		var responsePromise = $http.get(UserSharedSitesUrl+"&email="+email);
		responsePromise.success(function(data, status, headers, config) {
						var result = [];
						if (data!=null && Object.prototype.toString.call(data) === '[object Array]'){
							var numberOfRowsRequired = Math.ceil(data.length/3);
							var dataIndex = 0;
							for (var row=0;row<numberOfRowsRequired;row++){
								result[row] = [];
								for (var rowArrayIndex=0;rowArrayIndex<3;rowArrayIndex++){
									if (data[dataIndex]!=null){
										result[row][rowArrayIndex]=data[dataIndex];
										dataIndex++;
									}
								}
							}
						} else {
							SUtils.warn(SUtils.appConfig.qdosLogPrefix + "getUserSharedSites() :: Expected Response of Array is not available on Response of URL : "+UserSharedSitesUrl);
						}
	                    $scope.userSharedSites = result;
	                    $scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.qdosLogPrefix + "getUserSharedSites() :: AJAX Request failed on URL : "+UserSharedSitesUrl);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
	};

	this.getAllOfMySites = function($scope, email){
		var responsePromise = $http.get(AllMySitesUrl);
		responsePromise.success(function(data, status, headers, config) {
						var result = [];
						if (data!=null && Object.prototype.toString.call(data) === '[object Array]'){
							var numberOfRowsRequired = Math.ceil(data.length/3);
							var dataIndex = 0;
							for (var row=0;row<numberOfRowsRequired;row++){
								result[row] = [];
								for (var rowArrayIndex=0;rowArrayIndex<3;rowArrayIndex++){
									if (data[dataIndex]!=null){
										result[row][rowArrayIndex]=data[dataIndex];
										dataIndex++;
									}
								}
							}
						} else {
							SUtils.warn(SUtils.appConfig.qdosLogPrefix + "getAllOfMySites() :: Expected Response of Array is not available on Response of URL : "+AllMySitesUrl);
						}
	                    $scope.userSharedSites = result;
	                    $scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.qdosLogPrefix + "getAllOfMySites() :: AJAX Request failed on URL : "+AllMySitesUrl);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
	};

	this.getVersionHistoryOfUserSite = function($scope, email, siteName, numberOfRecords){
		var urlToFetchVersionHistoryOfUserSite = VersionHistoryOfUserSiteUrl + "?siteName="+siteName+"&numberOfRecords="+numberOfRecords+"&email="+email;
    	var responsePromise = $http.get(urlToFetchVersionHistoryOfUserSite);
		responsePromise.success(function(data, status, headers, config) {
						if(data!=null && Object.prototype.toString.call(data) === '[object Array]'){
							for(var index=0;index<data.length;index++){
								if ($scope.requestType === "AUTOMATION_COVERAGE"){
									$scope.graphData.datapoints.push({"x":data[index].versionNumber,
										"MANUAL":data[index].versionReport.manual,
										"AUTOMATED":(data[index].versionReport.failed+data[index].versionReport.passed+data[index].versionReport.aborted),
										"TOTAL":data[index].versionReport.total});
									var automatedPercentile = (((data[index].versionReport.passed+data[index].versionReport.failed+data[index].versionReport.aborted)*100)/data[index].versionReport.total).toFixed(2);
									var manualPercentile = 100 - automatedPercentile;
									$scope.tableData.push({"versionNumber":data[index].versionNumber,
															"catalogVersion":data[index].catalogVersion,
															"operatingSystem":data[index].operatingSystem,
															"browser":data[index].browser,
															"totalTestCases":data[index].versionReport.total,
															"automatedPercentile": automatedPercentile,
															"manualPercentile":manualPercentile});
								} else {
									$scope.graphData.datapoints.push({"x":data[index].versionNumber,
										"PASS":data[index].versionReport.passed,
										"FAIL":data[index].versionReport.failed,
										"ABORTED":data[index].versionReport.aborted,
										"TOTAL":data[index].versionReport.total-data[index].versionReport.manual});

									var totatAutomatedTestCases = data[index].versionReport.total-data[index].versionReport.manual;
									var passPercentile = ((data[index].versionReport.passed*100)/totatAutomatedTestCases).toFixed(2);
									var failPercentile = ((data[index].versionReport.failed*100)/totatAutomatedTestCases).toFixed(2);
									var abortPercentile = ((data[index].versionReport.aborted*100)/totatAutomatedTestCases).toFixed(2);
									var totalPercentile = passPercentile + failPercentile + abortPercentile;
									if (totalPercentile>100){
										var differenceInPercentile = totalPercentile - 100;
										if (failPercentile>differenceInPercentile){
											failPercentile = failPercentile - differenceInPercentile;
										} else if (abortPercentile>differenceInPercentile){
											abortPercentile = abortPercentile - differenceInPercentile;
										} else if (passPercentile>differenceInPercentile){
											passPercentile = passPercentile - differenceInPercentile;
										}
									}

									$scope.tableData.push({"versionNumber":data[index].versionNumber,
															"operatingSystem":data[index].operatingSystem,
															"catalogVersion":data[index].catalogVersion,
															"browser":data[index].browser,
															"startTime":data[index].startTime,
															"totalTime":data[index].totalTime,
															"totalTestCases":totatAutomatedTestCases,
															"passPercentile":passPercentile,
															"failPercentile":failPercentile,
															"abortPercentile":abortPercentile});
								}
								
							}
						} else {
							SUtils.warn(SUtils.appConfig.qdosLogPrefix + "getVersionHistoryOfUserSite() :: Response formate on URL : "+urlToFetchVersionHistoryOfUserSite+" is not correct. Its expecting an Array");
						}
						$scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.qdosLogPrefix + "getVersionHistoryOfUserSite() :: AJAX Request failed on URL : "+urlToFetchVersionHistoryOfUserSite);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
    };
    this.getVersionDetailsOfUserSingleSite = function($scope, email, siteName, version){
		var urlToFetchVersionDetailsOfSite = VersionDetailsOfUserSingleSiteUrl + "?siteName="+siteName+"&versionNumber="+version+"&email="+email;
    	var responsePromise = $http.get(urlToFetchVersionDetailsOfSite, {cache: true});
		responsePromise.success(function(data, status, headers, config) {
						if(data!=null){
							$scope.serverData = data;
						} else {
							SUtils.warn(SUtils.appConfig.qdosLogPrefix + "getVersionDetailsOfUserSingleSite() :: Data is null from AJAX call to URL : "+urlToFetchVersionDetailsOfSite);
						}
						$scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.qdosLogPrefix + "getVersionDetailsOfUserSingleSite() :: AJAX Request failed on URL : "+urlToFetchVersionDetailsOfSite);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
    };

    this.getTestSuiteReportObject = function(siteVersionObject, testSuiteName){
    	if(siteVersionObject!=null && siteVersionObject.testSuitesReport!=null && Object.prototype.toString.call(siteVersionObject.testSuitesReport) === '[object Array]' && siteVersionObject.testSuitesReport.length!=0){
			for(var suiteIndex=0;suiteIndex<siteVersionObject.testSuitesReport.length;suiteIndex++){
				if (siteVersionObject.testSuitesReport[suiteIndex].suiteName!=null && siteVersionObject.testSuitesReport[suiteIndex].suiteName === testSuiteName){
					return siteVersionObject.testSuitesReport[suiteIndex].report;
				}
			}
		} else {
			SUtils.warn(SUtils.appConfig.qdosLogPrefix + "getTestSuiteReportObject() :: testSuiteReport array is null. Please check expected format for requested URL : versionDetailsOfSite.json");
			return {};
		}
    };

    this.getFilteredTestCaseArray = function(testCaseArray, requestType){
    	var resultedTestCaseArray = new Array();
    	for(var index=0;index<testCaseArray.length;index++){
    		var testCase = testCaseArray[index];
    		if (requestType==="AUTOMATION_COVERAGE"){
    			if (testCase.status==="MANUAL"){
    				resultedTestCaseArray.push(testCase);
    			} else {
    				resultedTestCaseArray.push({
    					"testCaseId":testCase.testCaseId,
    					"testCaseName":testCase.testCaseName,
    					"statusClass":"automated",
    					"status":"AUTOMATED",
    					"startTime":testCase.startTime,
    					"endTime":testCase.endTime,
    					"duration":testCase.duration
    				});
    			}
    			
	    	} else if (requestType === "TEST_RESULTS" && testCase.status != "MANUAL"){
	    		resultedTestCaseArray.push(testCase);
	    	}
    	}
    	return resultedTestCaseArray;
    };

    this.getTestCaseObject = function(testCaseArray, testCaseId){
    	if(testCaseArray!=null && Object.prototype.toString.call(testCaseArray) === '[object Array]' && testCaseArray.length!=0){
			for(var testCaseIndex=0;testCaseIndex<testCaseArray.length;testCaseIndex++){
				if (testCaseArray[testCaseIndex].testCaseId!=null && testCaseArray[testCaseIndex].testCaseId === testCaseId){
					return testCaseArray[testCaseIndex];
				}
			}
		} else {
			SUtils.warn(SUtils.appConfig.qdosLogPrefix + "getTestCaseObject() :: testCaseArray array is null. Please check expected format for requested URL : versionDetailsOfSite.json");
			return {};
		}
    };
    
});

qdos.config(['$routeProvider', function($routeProvider){
	$routeProvider.
		when('/qdos/automationCoverage/view', {
			templateUrl: 'templates/module/qdos/view/qdos.automation.coverage.html',
			controller: 'qdosAutomationCoverageCtrl'
		}).
		when('/qdos/automationCoverage/user/:user', {
			templateUrl: 'templates/module/qdos/view/qdos.automation.coverage.user.html',
			controller: 'qdosAutomationCoverageUserSitesCtrl'
		}).
		when('/qdos/automationCoverage/user/:user/:siteName/:numberOfRecords', {
			templateUrl: 'templates/module/qdos/view/qdos.automation.coverage.user.site.html',
			controller: 'qdosAutomationCoverageUserSingleSiteCtrl'
		}).
		when('/qdos/automationCoverage/user/:user/:siteName/:numberOfRecords/graphType/:graphType', {
			templateUrl: 'templates/module/qdos/view/qdos.automation.coverage.user.site.html',
			controller: 'qdosAutomationCoverageUserSingleSiteCtrl'
		}).
		when('/qdos/automationCoverage/user/:user/:siteName/version/:version', {
			templateUrl: 'templates/module/qdos/view/qdos.automation.coverage.user.site.version.html',
			controller: 'qdosAutomationCoverageUserSingleSiteVersionCtrl'
		}).
		when('/qdos/automationCoverage/user/:user/:siteName/version/:version/:suiteName', {
			templateUrl: 'templates/module/qdos/view/qdos.automation.coverage.user.site.version.suitename.html',
			controller: 'qdosAutomationCoverageUserSingleSiteVersionSuiteNameCtrl'
		}).
		
		when('/qdos/dashboard/view', {
			templateUrl: 'templates/module/qdos/view/qdos.dashboard.html',
			controller: 'dashboardCtrl'
		}).
		when('/qdos/dashboard/user/:user/:siteName/:numberOfRecords', {
			templateUrl: 'templates/module/qdos/view/qdos.user.site.html',
			controller: 'qdosDashboardUserSingleSiteCtrl'
		}).
		when('/qdos/dashboard/user/:user/:siteName/:numberOfRecords/graphType/:graphType', {
			templateUrl: 'templates/module/qdos/view/qdos.user.site.html',
			controller: 'qdosDashboardUserSingleSiteCtrl'
		}).
		when('/qdos/dashboard/user/:user/:siteName/version/:version', {
			templateUrl: 'templates/module/qdos/view/qdos.user.site.version.html',
			controller: 'qdosDashboardUserSingleSiteVersionCtrl'
		}).
		when('/qdos/dashboard/user/:user/:siteName/version/:version/:suiteName', {
			templateUrl: 'templates/module/qdos/view/qdos.user.site.version.suitename.html',
			controller: 'qdosDashboardUserSingleSiteVersionSuiteNameCtrl'
		}).
		when('/qdos/dashboard/user/:user/:siteName/version/:version/:suiteName/:testCaseId', {
			templateUrl: 'templates/module/qdos/view/qdos.user.site.version.suitename.testcaseid.html',
			controller: 'qdosDashboardUserSingleSiteVersionSuiteNameTestCaseIdCtrl'
		}).
		
		when('/qdos/dashboard-automationCoverage/view', {
			templateUrl: 'templates/module/qdos/view/qdos.dashboard.automationCoverage.html',
			controller: 'qdosDashboardAutomationCoverageCtrl'
		}).
		when('/qdos/dashboard-automationCoverage/user/:user/:siteName/:numberOfRecords', {
			templateUrl: 'templates/module/qdos/view/qdos.automation.coverage.user.site.html',
			controller: 'qdosDashboardAutomationCoverageUserSingleSiteCtrl'
		}).
		when('/qdos/dashboard-automationCoverage/user/:user/:siteName/:numberOfRecords/graphType/:graphType', {
			templateUrl: 'templates/module/qdos/view/qdos.automation.coverage.user.site.html',
			controller: 'qdosDashboardAutomationCoverageUserSingleSiteCtrl'
		}).
		when('/qdos/dashboard-automationCoverage/user/:user/:siteName/version/:version', {
			templateUrl: 'templates/module/qdos/view/qdos.automation.coverage.user.site.version.html',
			controller: 'qdosDashboardAutomationCoverageUserSingleSiteVersionCtrl'
		}).
		when('/qdos/dashboard-automationCoverage/user/:user/:siteName/version/:version/:suiteName', {
			templateUrl: 'templates/module/qdos/view/qdos.automation.coverage.user.site.version.suitename.html',
			controller: 'qdosDashboardAutomationCoverageUserSingleSiteVersionSuiteNameCtrl'
		}).
		when('/qdos/:user', {
			templateUrl: 'templates/module/qdos/view/qdos.user.html',
			controller: 'qdosUserSitesCtrl'
		}).
		when('/qdos/:user/:siteName/:numberOfRecords', {
			templateUrl: 'templates/module/qdos/view/qdos.user.site.html',
			controller: 'qdosUserSingleSiteCtrl'
		}).
		when('/qdos/:user/:siteName/:numberOfRecords/graphType/:graphType', {
			templateUrl: 'templates/module/qdos/view/qdos.user.site.html',
			controller: 'qdosUserSingleSiteCtrl'
		}).
		when('/qdos/:user/:siteName/version/:version', {
			templateUrl: 'templates/module/qdos/view/qdos.user.site.version.html',
			controller: 'qdosUserSingleSiteVersionCtrl'
		}).
		when('/qdos/:user/:siteName/version/:version/:suiteName', {
			templateUrl: 'templates/module/qdos/view/qdos.user.site.version.suitename.html',
			controller: 'qdosUserSingleSiteVersionSuiteNameCtrl'
		}).
		when('/qdos/:user/:siteName/version/:version/:suiteName/:testCaseId', {
			templateUrl: 'templates/module/qdos/view/qdos.user.site.version.suitename.testcaseid.html',
			controller: 'qdosUserSingleSiteVersionSuiteNameTestCaseIdCtrl'
		}).
		when('/qdos', {
			templateUrl: 'templates/module/qdos/view/qdos.html',
			controller: 'qdosHomePageCtrl'
		}).
	    otherwise({
	        redirectTo: '/'
	    });
}]);


qdos.controller("qdosUserSingleSiteVersionSuiteNameTestCaseIdCtrl", function($scope,$routeParams,$sce,$resource,$modal,$cookies,NgTableParams,qdosService){
	$scope.isAjaxFinished = "false";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.suiteName = $routeParams.suiteName;
	$scope.testCaseId = $routeParams.testCaseId;
	$scope.testSuiteReportObject = {};
	$scope.testCaseData = {};
	qdosService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);
	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.testSuitesReport!=null ) {
			$scope.testSuiteReportObject = qdosService.getTestSuiteReportObject(serverData,$scope.suiteName);
		}
	}, true);
	$scope.$watch('testSuiteReportObject', function(testSuiteReportObject, oldtestSuiteReportObject) {
		if ($scope.testSuiteReportObject!=null && $scope.testSuiteReportObject.testCaseArray!=null && Object.prototype.toString.call($scope.testSuiteReportObject.testCaseArray) === '[object Array]'){
			$scope.testCaseData = qdosService.getTestCaseObject($scope.testSuiteReportObject.testCaseArray,$scope.testCaseId);
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: $scope.testCaseData.testStepsData
		    });
		}

	}, true);
	$scope.showScreenshot = function(testCaseId, stepId, stepScreenshot){
		var testStepData = {
			"stepScreenShot":stepScreenshot,
			"testCaseId":testCaseId,
			"stepId":stepId
		};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/qdos/view/qdos.user.site.version.suitename.testcaseid.stepscreenshot.html',
	      controller: 'qdosUserSingleSiteVersionSuiteNameTestCaseIdStepScreenshotCtrl',
	      keyboard:true,
	      size:"lg",
	      resolve: {
		    testStepData: function () {
		      return testStepData;
		    }
		  }
	    });
	};
	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites - Reports",
				"anchor":"#/qdos"
			},
			{
				"displayName":$routeParams.user,
				"anchor":"#/qdos/"+window.encodeURIComponent($routeParams.user)
			},
			{
				"displayName":$routeParams.siteName,
				"anchor":"#/qdos/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
			},
			{
				"displayName":$routeParams.version,
				"anchor":"#/qdos/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/version/"+window.encodeURIComponent($routeParams.version)
			},
			{
				"displayName":$routeParams.suiteName,
				"anchor":"#/qdos/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/version/"+window.encodeURIComponent($routeParams.version)+"/"+window.encodeURIComponent($routeParams.suiteName)
			}
		],
		"currentPageDisplayName":$routeParams.testCaseId
	};
});

qdos.controller("qdosUserSingleSiteVersionSuiteNameTestCaseIdStepScreenshotCtrl", function($scope,$modalInstance, testStepData){
	$scope.testStepData = testStepData;
	$scope.close = function () {
	    $modalInstance.dismiss('close');
	};
});


qdos.controller("qdosUserSingleSiteVersionSuiteNameCtrl", function($scope,$routeParams,$sce,$resource,$cookies,NgTableParams,qdosService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/qdos";
	$scope.requestType = "TEST_RESULTS";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.suiteName = $routeParams.suiteName;
	$scope.testSuiteReportObject = {};
	qdosService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);
	$scope.graphData = {};
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"PASS","type":"bar","name":"PASS","color":"#2CA02C"},
								  {"id":"FAIL","type":"bar","name":"FAIL","color":"#D62728"},
								  {"id":"ABORTED","type":"bar","name":"ABORTED","color":"#FF7F0E"}];
	$scope.graphData.datax={"id":"x"};

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.testSuitesReport!=null ) {
			$scope.testSuiteReportObject = qdosService.getTestSuiteReportObject(serverData,$scope.suiteName);
		}
	}, true);

	$scope.$watch('testSuiteReportObject', function(testSuiteReportObject, oldtestSuiteReportObject) {
		if ($scope.testSuiteReportObject!=null && $scope.testSuiteReportObject.testSuiteDetails!=null && $scope.testSuiteReportObject.testSuiteDetails.summary !=null){
			$scope.graphData.datapoints.push({"x":$scope.serverData.versionNumber,
				"PASS":$scope.testSuiteReportObject.testSuiteDetails.summary.passed,
				"FAIL":$scope.testSuiteReportObject.testSuiteDetails.summary.failed,
				"ABORTED":$scope.testSuiteReportObject.testSuiteDetails.summary.aborted});
		}
		if ($scope.testSuiteReportObject!=null && $scope.testSuiteReportObject.testCaseArray!=null && Object.prototype.toString.call($scope.testSuiteReportObject.testCaseArray) === '[object Array]'){
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: qdosService.getFilteredTestCaseArray($scope.testSuiteReportObject.testCaseArray, $scope.requestType)
		    });
		}

	}, true);	

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites - Reports",
				"anchor":$scope.moduleAccessURL
			},
			{
				"displayName":$routeParams.user,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)
			},
			{
				"displayName":$routeParams.siteName,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
			},
			{
				"displayName":$routeParams.version,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/version/"+window.encodeURIComponent($routeParams.version)
			}
		],
		"currentPageDisplayName":$routeParams.suiteName
	};
});

qdos.controller("qdosUserSingleSiteVersionCtrl", function($scope,$routeParams,$sce,$cookies,qdosService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/qdos";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.graphData = {};
	$scope.testSuitesArray = [];
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"PASS","type":"bar","name":"PASS","color":"#2CA02C"},
								  {"id":"FAIL","type":"bar","name":"FAIL","color":"#D62728"},
								  {"id":"ABORTED","type":"bar","name":"ABORTED","color":"#FF7F0E"}];
	$scope.graphData.datax={"id":"x"};
	
	qdosService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.versionReport !=null){
			$scope.graphData.datapoints.push({"x":serverData.versionNumber,
				"PASS":serverData.versionReport.passed,
				"FAIL":serverData.versionReport.failed,
				"ABORTED":serverData.versionReport.aborted});

			if (serverData.testSuitesReport!=null && Object.prototype.toString.call(serverData.testSuitesReport) === '[object Array]'){
				for (var index=0;index<serverData.testSuitesReport.length;index++){
					var testSuite = serverData.testSuitesReport[index];
					var totalAutomatedTestCases = testSuite.report.testSuiteDetails.summary.total - testSuite.report.testSuiteDetails.summary.manual;
					var passPercentile = ((testSuite.report.testSuiteDetails.summary.passed*100)/totalAutomatedTestCases).toFixed(2);
					var failPercentile = ((testSuite.report.testSuiteDetails.summary.failed*100)/totalAutomatedTestCases).toFixed(2);
					var abortPercentile = ((testSuite.report.testSuiteDetails.summary.aborted*100)/totalAutomatedTestCases).toFixed(2);
					var totalPercentile = passPercentile + failPercentile + abortPercentile;
					if (totalPercentile>100){
						var differenceInPercentile = totalPercentile - 100;
						if (failPercentile>differenceInPercentile){
							failPercentile = failPercentile - differenceInPercentile;
						} else if (abortPercentile>differenceInPercentile){
							abortPercentile = abortPercentile - differenceInPercentile;
						} else if (passPercentile>differenceInPercentile){
							passPercentile = passPercentile - differenceInPercentile;
						}
					}

					$scope.testSuitesArray.push({
						"suiteName":testSuite.suiteName,
						"startTime":testSuite.report.testSuiteDetails.startTime,
						"endTime":testSuite.report.testSuiteDetails.endTime,
						"totalTime":testSuite.report.testSuiteDetails.duration,
						"totalTestCases":totalAutomatedTestCases,
						"passPercentile":passPercentile,
						"failPercentile":failPercentile,
						"abortPercentile":abortPercentile
					});
				}
			}
		}
	}, true);

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites - Reports",
				"anchor":$scope.moduleAccessURL
			},
			{
				"displayName":$routeParams.user,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)
			},
			{
				"displayName":$routeParams.siteName,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
			}
		],
		"currentPageDisplayName":$routeParams.version
	};
});

qdos.controller("qdosUserSingleSiteCtrl", function($scope,$routeParams,$sce,$cookies,qdosService) {
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/qdos";
	$scope.requestType = "TEST_RESULTS";
	$scope.myEmail = $cookies.email;
	$scope.email = $routeParams.user;
	$scope.siteName = $routeParams.siteName;
	$scope.numberOfRecords = $routeParams.numberOfRecords;
	$scope.graphType = "line";
	if ($routeParams.graphType !=null && $routeParams.graphType === "bar"){
		$scope.graphType = "bar";
	}
	$scope.graphData = {};
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"PASS","type":$scope.graphType,"name":"PASS","color":"#2CA02C"},
								  {"id":"FAIL","type":$scope.graphType,"name":"FAIL","color":"#D62728"},
								  {"id":"ABORTED","type":$scope.graphType,"name":"ABORTED","color":"#FF7F0E"},
	 					          {"id":"TOTAL","type":$scope.graphType,"name":"TOTAL","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};
	$scope.tableData = [];
	qdosService.getVersionHistoryOfUserSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.numberOfRecords);
	
	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites - Reports",
				"anchor":$scope.moduleAccessURL
			},
			{
				"displayName":$routeParams.user,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)
			}
		],
		"currentPageDisplayName":$routeParams.siteName
	};
});

qdos.controller("qdosUserSitesCtrl", function($scope,$routeParams,$cookies,qdosService) {
	$scope.isAjaxFinished = "false";
	$scope.userSharedSites = {};
	qdosService.getUserSharedSites($scope,$routeParams.user);
	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites - Reports",
				"anchor":"#/qdos"
			}
		],
		"currentPageDisplayName":$routeParams.user
	};
	$scope.chartType="donut";
	$scope.myEmail = $cookies.email;
});

qdos.controller("qdosHomePageCtrl", function($scope,$cookies,qdosService) {
	$scope.isAjaxFinished = "false";
	$scope.detailedSharedSites = {};
	qdosService.getDetailedSharedSites($scope);
	$scope.chartType="donut";
	$scope.myEmail = $cookies.email;
});

qdos.controller("dashboardCtrl", function($scope,$cookies,$filter,qdosService) {
	$scope.isAjaxFinished = "false";
	$scope.userSharedSites = {};
	qdosService.getAllOfMySites($scope,$filter('encodeURI')($cookies.email));
	$scope.chartType="donut";
	$scope.myEmail = $cookies.email;
});

qdos.controller("qdosAutomationCoverageCtrl", function($scope,$cookies,qdosService) {
	$scope.isAjaxFinished = "false";
	$scope.detailedSharedSites = {};
	qdosService.getDetailedSharedSites($scope);
	$scope.chartType="donut";
	$scope.myEmail = $cookies.email;
});

qdos.controller("qdosAutomationCoverageUserSitesCtrl", function($scope,$routeParams,$cookies,qdosService) {
	$scope.isAjaxFinished = "false";
	$scope.userSharedSites = {};
	qdosService.getUserSharedSites($scope,$routeParams.user);
	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites - Automation Coverage",
				"anchor":"#/qdos/automationCoverage/view"
			}
		],
		"currentPageDisplayName":$routeParams.user
	};
	$scope.chartType="donut";
	$scope.myEmail = $cookies.email;
});

qdos.controller("qdosAutomationCoverageUserSingleSiteCtrl", function($scope,$routeParams,$sce,$cookies,qdosService) {
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/qdos/automationCoverage/user";
	$scope.requestType = "AUTOMATION_COVERAGE";
	$scope.myEmail = $cookies.email;
	$scope.email = $routeParams.user;
	$scope.siteName = $routeParams.siteName;
	$scope.numberOfRecords = $routeParams.numberOfRecords;
	$scope.graphType = "line";
	if ($routeParams.graphType !=null && $routeParams.graphType === "bar"){
		$scope.graphType = "bar";
	}
	$scope.graphData = {};
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"MANUAL","type":$scope.graphType,"name":"MANUAL","color":"#F1530F"},
								  {"id":"AUTOMATED","type":$scope.graphType,"name":"AUTOMATED","color":"#1F77B4"},
	 					          {"id":"TOTAL","type":$scope.graphType,"name":"TOTAL","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};
	$scope.tableData = [];
	qdosService.getVersionHistoryOfUserSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.numberOfRecords);
	
	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites - Automation Coverage",
				"anchor":"#/qdos/automationCoverage/view"
			},
			{
				"displayName":$routeParams.user,
				"anchor":"#/qdos/automationCoverage/user/"+window.encodeURIComponent($routeParams.user)
			}
		],
		"currentPageDisplayName":$routeParams.siteName
	};
});

qdos.controller("qdosAutomationCoverageUserSingleSiteVersionCtrl", function($scope,$routeParams,$sce,$cookies,qdosService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/qdos/automationCoverage/user";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.graphData = {};
	$scope.testSuitesArray = [];
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"MANUAL","type":"bar","name":"MANUAL","color":"#F1530F"},
								  {"id":"AUTOMATED","type":"bar","name":"AUTOMATED","color":"#1F77B4"},
	 					          {"id":"TOTAL","type":"bar","name":"TOTAL","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};
	
	qdosService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.versionReport !=null){
			$scope.graphData.datapoints.push({"x":serverData.versionNumber,
				"MANUAL":serverData.versionReport.manual,
				"AUTOMATED":serverData.versionReport.passed+serverData.versionReport.failed+serverData.versionReport.aborted,
				"TOTAL":serverData.versionReport.total});

			if (serverData.testSuitesReport!=null && Object.prototype.toString.call(serverData.testSuitesReport) === '[object Array]'){
				for (var index=0;index<serverData.testSuitesReport.length;index++){
					var testSuite = serverData.testSuitesReport[index];
					var totalAutomatedTestCases = testSuite.report.testSuiteDetails.summary.total;
					var automatedPercentile = (((testSuite.report.testSuiteDetails.summary.passed+testSuite.report.testSuiteDetails.summary.failed+testSuite.report.testSuiteDetails.summary.aborted)*100)/totalAutomatedTestCases).toFixed(2);
					var manualPercentile = ((testSuite.report.testSuiteDetails.summary.manual*100)/totalAutomatedTestCases).toFixed(2);
					var totalPercentile = automatedPercentile + manualPercentile;
					if (totalPercentile>100){
						var differenceInPercentile = totalPercentile - 100;
						if (manualPercentile>differenceInPercentile){
							manualPercentile = manualPercentile - differenceInPercentile;
						} else if (automatedPercentile>differenceInPercentile){
							automatedPercentile = automatedPercentile - differenceInPercentile;
						}
					}

					$scope.testSuitesArray.push({
						"suiteName":testSuite.suiteName,
						"endTime":testSuite.report.testSuiteDetails.endTime,
						"totalTestCases":totalAutomatedTestCases,
						"manualPercentile":manualPercentile,
						"automatedPercentile":automatedPercentile
					});
				}
			}
		}
	}, true);

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites - Automation Coverage",
				"anchor":"#/qdos/automationCoverage/view"
			},
			{
				"displayName":$routeParams.user,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)
			},
			{
				"displayName":$routeParams.siteName,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
			}
		],
		"currentPageDisplayName":$routeParams.version
	};
});


qdos.controller("qdosAutomationCoverageUserSingleSiteVersionSuiteNameCtrl", function($scope,$routeParams,$sce,$resource,$cookies,NgTableParams,qdosService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/qdos/automationCoverage/user";
	$scope.requestType = "AUTOMATION_COVERAGE";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.suiteName = $routeParams.suiteName;
	$scope.testSuiteReportObject = {};
	qdosService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);
	$scope.graphData = {};
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"MANUAL","type":"bar","name":"MANUAL","color":"#F1530F"},
								  {"id":"AUTOMATED","type":"bar","name":"AUTOMATED","color":"#1F77B4"},
	 					          {"id":"TOTAL","type":"bar","name":"TOTAL","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.testSuitesReport!=null ) {
			$scope.testSuiteReportObject = qdosService.getTestSuiteReportObject(serverData,$scope.suiteName);
		}
	}, true);

	$scope.$watch('testSuiteReportObject', function(testSuiteReportObject, oldtestSuiteReportObject) {
		if ($scope.testSuiteReportObject!=null && $scope.testSuiteReportObject.testSuiteDetails!=null && $scope.testSuiteReportObject.testSuiteDetails.summary !=null){
			$scope.graphData.datapoints.push({"x":$scope.serverData.versionNumber,
				"MANUAL":$scope.testSuiteReportObject.testSuiteDetails.summary.manual,
				"AUTOMATED":$scope.testSuiteReportObject.testSuiteDetails.summary.passed+$scope.testSuiteReportObject.testSuiteDetails.summary.failed+$scope.testSuiteReportObject.testSuiteDetails.summary.aborted,
				"TOTAL":$scope.testSuiteReportObject.testSuiteDetails.summary.total});
		}
		if ($scope.testSuiteReportObject!=null && $scope.testSuiteReportObject.testCaseArray!=null && Object.prototype.toString.call($scope.testSuiteReportObject.testCaseArray) === '[object Array]'){
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: qdosService.getFilteredTestCaseArray($scope.testSuiteReportObject.testCaseArray, $scope.requestType)
		    });
		}

	}, true);	

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites - Automation Coverage",
				"anchor":"#/qdos/automationCoverage/view"
			},
			{
				"displayName":$routeParams.user,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)
			},
			{
				"displayName":$routeParams.siteName,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
			},
			{
				"displayName":$routeParams.version,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/version/"+window.encodeURIComponent($routeParams.version)
			}
		],
		"currentPageDisplayName":$routeParams.suiteName
	};
});

qdos.controller("qdosDashboardUserSingleSiteCtrl", function($scope,$routeParams,$sce,$cookies,qdosService) {
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/qdos/dashboard/user";
	$scope.requestType = "TEST_RESULTS";
	$scope.myEmail = $cookies.email;
	$scope.email = $routeParams.user;
	$scope.siteName = $routeParams.siteName;
	$scope.numberOfRecords = $routeParams.numberOfRecords;
	$scope.graphType = "line";
	if ($routeParams.graphType !=null && $routeParams.graphType === "bar"){
		$scope.graphType = "bar";
	}
	$scope.graphData = {};
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"PASS","type":$scope.graphType,"name":"PASS","color":"#2CA02C"},
								  {"id":"FAIL","type":$scope.graphType,"name":"FAIL","color":"#D62728"},
								  {"id":"ABORTED","type":$scope.graphType,"name":"ABORTED","color":"#FF7F0E"},
	 					          {"id":"TOTAL","type":$scope.graphType,"name":"TOTAL","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};
	$scope.tableData = [];
	qdosService.getVersionHistoryOfUserSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.numberOfRecords);
	
	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"My Sites - Reports",
				"anchor":"#/qdos/dashboard/view"
			}
		],
		"currentPageDisplayName":$routeParams.siteName
	};
});

qdos.controller("qdosDashboardUserSingleSiteVersionCtrl", function($scope,$routeParams,$sce,$cookies,qdosService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/qdos/dashboard/user";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.graphData = {};
	$scope.testSuitesArray = [];
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"PASS","type":"bar","name":"PASS","color":"#2CA02C"},
								  {"id":"FAIL","type":"bar","name":"FAIL","color":"#D62728"},
								  {"id":"ABORTED","type":"bar","name":"ABORTED","color":"#FF7F0E"}];
	$scope.graphData.datax={"id":"x"};
	
	qdosService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.versionReport !=null){
			$scope.graphData.datapoints.push({"x":serverData.versionNumber,
				"PASS":serverData.versionReport.passed,
				"FAIL":serverData.versionReport.failed,
				"ABORTED":serverData.versionReport.aborted});

			if (serverData.testSuitesReport!=null && Object.prototype.toString.call(serverData.testSuitesReport) === '[object Array]'){
				for (var index=0;index<serverData.testSuitesReport.length;index++){
					var testSuite = serverData.testSuitesReport[index];
					var totalAutomatedTestCases = testSuite.report.testSuiteDetails.summary.total - testSuite.report.testSuiteDetails.summary.manual;
					var passPercentile = ((testSuite.report.testSuiteDetails.summary.passed*100)/totalAutomatedTestCases).toFixed(2);
					var failPercentile = ((testSuite.report.testSuiteDetails.summary.failed*100)/totalAutomatedTestCases).toFixed(2);
					var abortPercentile = ((testSuite.report.testSuiteDetails.summary.aborted*100)/totalAutomatedTestCases).toFixed(2);
					var totalPercentile = passPercentile + failPercentile + abortPercentile;
					if (totalPercentile>100){
						var differenceInPercentile = totalPercentile - 100;
						if (failPercentile>differenceInPercentile){
							failPercentile = failPercentile - differenceInPercentile;
						} else if (abortPercentile>differenceInPercentile){
							abortPercentile = abortPercentile - differenceInPercentile;
						} else if (passPercentile>differenceInPercentile){
							passPercentile = passPercentile - differenceInPercentile;
						}
					}

					$scope.testSuitesArray.push({
						"suiteName":testSuite.suiteName,
						"startTime":testSuite.report.testSuiteDetails.startTime,
						"endTime":testSuite.report.testSuiteDetails.endTime,
						"totalTime":testSuite.report.testSuiteDetails.duration,
						"totalTestCases":totalAutomatedTestCases,
						"passPercentile":passPercentile,
						"failPercentile":failPercentile,
						"abortPercentile":abortPercentile
					});
				}
			}
		}
	}, true);

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"My Sites - Reports",
				"anchor":"#/qdos/dashboard/view"
			},
			{
				"displayName":$routeParams.siteName,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
			}
		],
		"currentPageDisplayName":$routeParams.version
	};
});

qdos.controller("qdosDashboardUserSingleSiteVersionSuiteNameCtrl", function($scope,$routeParams,$sce,$resource,$cookies,NgTableParams,qdosService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/qdos/dashboard/user";
	$scope.requestType = "TEST_RESULTS";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.suiteName = $routeParams.suiteName;
	$scope.testSuiteReportObject = {};
	qdosService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);
	$scope.graphData = {};
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"PASS","type":"bar","name":"PASS","color":"#2CA02C"},
								  {"id":"FAIL","type":"bar","name":"FAIL","color":"#D62728"},
								  {"id":"ABORTED","type":"bar","name":"ABORTED","color":"#FF7F0E"}];
	$scope.graphData.datax={"id":"x"};

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.testSuitesReport!=null ) {
			$scope.testSuiteReportObject = qdosService.getTestSuiteReportObject(serverData,$scope.suiteName);
		}
	}, true);

	$scope.$watch('testSuiteReportObject', function(testSuiteReportObject, oldtestSuiteReportObject) {
		if ($scope.testSuiteReportObject!=null && $scope.testSuiteReportObject.testSuiteDetails!=null && $scope.testSuiteReportObject.testSuiteDetails.summary !=null){
			$scope.graphData.datapoints.push({"x":$scope.serverData.versionNumber,
				"PASS":$scope.testSuiteReportObject.testSuiteDetails.summary.passed,
				"FAIL":$scope.testSuiteReportObject.testSuiteDetails.summary.failed,
				"ABORTED":$scope.testSuiteReportObject.testSuiteDetails.summary.aborted});
		}
		if ($scope.testSuiteReportObject!=null && $scope.testSuiteReportObject.testCaseArray!=null && Object.prototype.toString.call($scope.testSuiteReportObject.testCaseArray) === '[object Array]'){
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: qdosService.getFilteredTestCaseArray($scope.testSuiteReportObject.testCaseArray, $scope.requestType)
		    });
		}

	}, true);	

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"My Sites - Reports",
				"anchor":"#/qdos/dashboard/view"
			},
			{
				"displayName":$routeParams.siteName,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
			},
			{
				"displayName":$routeParams.version,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/version/"+window.encodeURIComponent($routeParams.version)
			}
		],
		"currentPageDisplayName":$routeParams.suiteName
	};
});

qdos.controller("qdosDashboardUserSingleSiteVersionSuiteNameTestCaseIdCtrl", function($scope,$routeParams,$sce,$resource,$modal,$cookies,NgTableParams,qdosService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/qdos/dashboard/user";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.suiteName = $routeParams.suiteName;
	$scope.testCaseId = $routeParams.testCaseId;
	$scope.testSuiteReportObject = {};
	$scope.testCaseData = {};
	qdosService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);
	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.testSuitesReport!=null ) {
			$scope.testSuiteReportObject = qdosService.getTestSuiteReportObject(serverData,$scope.suiteName);
		}
	}, true);
	$scope.$watch('testSuiteReportObject', function(testSuiteReportObject, oldtestSuiteReportObject) {
		if ($scope.testSuiteReportObject!=null && $scope.testSuiteReportObject.testCaseArray!=null && Object.prototype.toString.call($scope.testSuiteReportObject.testCaseArray) === '[object Array]'){
			$scope.testCaseData = qdosService.getTestCaseObject($scope.testSuiteReportObject.testCaseArray,$scope.testCaseId);
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: $scope.testCaseData.testStepsData
		    });
		}

	}, true);
	$scope.showScreenshot = function(testCaseId, stepId, stepScreenshot, screenshotUrl){
		var testStepData = {
			"stepScreenShot":stepScreenshot,
			"testCaseId":testCaseId,
			"stepId":stepId,
			"screenshotUrl":screenshotUrl
		};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/qdos/view/qdos.user.site.version.suitename.testcaseid.stepscreenshot.html',
	      controller: 'qdosUserSingleSiteVersionSuiteNameTestCaseIdStepScreenshotCtrl',
	      keyboard:true,
	      size:"lg",
	      resolve: {
		    testStepData: function () {
		      return testStepData;
		    }
		  }
	    });
	};
	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"My Sites - Reports",
				"anchor":"#/qdos/dashboard/view"
			},
			{
				"displayName":$routeParams.siteName,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
			},
			{
				"displayName":$routeParams.version,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/version/"+window.encodeURIComponent($routeParams.version)
			},
			{
				"displayName":$routeParams.suiteName,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/version/"+window.encodeURIComponent($routeParams.version)+"/"+window.encodeURIComponent($routeParams.suiteName)
			}
		],
		"currentPageDisplayName":$routeParams.testCaseId
	};
});


qdos.controller("qdosDashboardAutomationCoverageCtrl", function($scope,$cookies,$filter,qdosService) {
	$scope.isAjaxFinished = "false";
	$scope.userSharedSites = {};
	qdosService.getAllOfMySites($scope,$filter('encodeURI')($cookies.email));
	$scope.chartType="donut";
	$scope.myEmail = $cookies.email;
});

qdos.controller("qdosDashboardAutomationCoverageUserSingleSiteCtrl", function($scope,$routeParams,$sce,$cookies,qdosService) {
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/qdos/dashboard-automationCoverage/user";
	$scope.requestType = "AUTOMATION_COVERAGE";
	$scope.myEmail = $cookies.email;
	$scope.email = $routeParams.user;
	$scope.siteName = $routeParams.siteName;
	$scope.numberOfRecords = $routeParams.numberOfRecords;
	$scope.graphType = "line";
	if ($routeParams.graphType !=null && $routeParams.graphType === "bar"){
		$scope.graphType = "bar";
	}
	$scope.graphData = {};
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"MANUAL","type":$scope.graphType,"name":"MANUAL","color":"#F1530F"},
								  {"id":"AUTOMATED","type":$scope.graphType,"name":"AUTOMATED","color":"#1F77B4"},
	 					          {"id":"TOTAL","type":$scope.graphType,"name":"TOTAL","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};
	$scope.tableData = [];
	qdosService.getVersionHistoryOfUserSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.numberOfRecords);
	
	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"My Sites - Automation Coverage",
				"anchor":"#/qdos/dashboard-automationCoverage/view"
			}
		],
		"currentPageDisplayName":$routeParams.siteName
	};
});


qdos.controller("qdosDashboardAutomationCoverageUserSingleSiteVersionCtrl", function($scope,$routeParams,$sce,$cookies,qdosService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/qdos/dashboard-automationCoverage/user";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.graphData = {};
	$scope.testSuitesArray = [];
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"MANUAL","type":"bar","name":"MANUAL","color":"#F1530F"},
								  {"id":"AUTOMATED","type":"bar","name":"AUTOMATED","color":"#1F77B4"},
	 					          {"id":"TOTAL","type":"bar","name":"TOTAL","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};
	
	qdosService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.versionReport !=null){
			$scope.graphData.datapoints.push({"x":serverData.versionNumber,
				"MANUAL":serverData.versionReport.manual,
				"AUTOMATED":serverData.versionReport.passed+serverData.versionReport.failed+serverData.versionReport.aborted,
				"TOTAL":serverData.versionReport.total});

			if (serverData.testSuitesReport!=null && Object.prototype.toString.call(serverData.testSuitesReport) === '[object Array]'){
				for (var index=0;index<serverData.testSuitesReport.length;index++){
					var testSuite = serverData.testSuitesReport[index];
					var totalAutomatedTestCases = testSuite.report.testSuiteDetails.summary.total;
					var automatedPercentile = (((testSuite.report.testSuiteDetails.summary.passed+testSuite.report.testSuiteDetails.summary.failed+testSuite.report.testSuiteDetails.summary.aborted)*100)/totalAutomatedTestCases).toFixed(2);
					var manualPercentile = ((testSuite.report.testSuiteDetails.summary.manual*100)/totalAutomatedTestCases).toFixed(2);
					var totalPercentile = automatedPercentile + manualPercentile;
					if (totalPercentile>100){
						var differenceInPercentile = totalPercentile - 100;
						if (manualPercentile>differenceInPercentile){
							manualPercentile = manualPercentile - differenceInPercentile;
						} else if (automatedPercentile>differenceInPercentile){
							automatedPercentile = automatedPercentile - differenceInPercentile;
						}
					}

					$scope.testSuitesArray.push({
						"suiteName":testSuite.suiteName,
						"endTime":testSuite.report.testSuiteDetails.endTime,
						"totalTestCases":totalAutomatedTestCases,
						"manualPercentile":manualPercentile,
						"automatedPercentile":automatedPercentile
					});
				}
			}
		}
	}, true);

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"My Sites - Automation Coverage",
				"anchor":"#/qdos/dashboard-automationCoverage/view"
			},
			{
				"displayName":$routeParams.siteName,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
			}
		],
		"currentPageDisplayName":$routeParams.version
	};
});

qdos.controller("qdosDashboardAutomationCoverageUserSingleSiteVersionSuiteNameCtrl", function($scope,$routeParams,$sce,$resource,$cookies,NgTableParams,qdosService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/qdos/dashboard-automationCoverage/user";
	$scope.requestType = "AUTOMATION_COVERAGE";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.suiteName = $routeParams.suiteName;
	$scope.testSuiteReportObject = {};
	qdosService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);
	$scope.graphData = {};
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"MANUAL","type":"bar","name":"MANUAL","color":"#F1530F"},
								  {"id":"AUTOMATED","type":"bar","name":"AUTOMATED","color":"#1F77B4"},
	 					          {"id":"TOTAL","type":"bar","name":"TOTAL","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.testSuitesReport!=null ) {
			$scope.testSuiteReportObject = qdosService.getTestSuiteReportObject(serverData,$scope.suiteName);
		}
	}, true);

	$scope.$watch('testSuiteReportObject', function(testSuiteReportObject, oldtestSuiteReportObject) {
		if ($scope.testSuiteReportObject!=null && $scope.testSuiteReportObject.testSuiteDetails!=null && $scope.testSuiteReportObject.testSuiteDetails.summary !=null){
			$scope.graphData.datapoints.push({"x":$scope.serverData.versionNumber,
				"MANUAL":$scope.testSuiteReportObject.testSuiteDetails.summary.manual,
				"AUTOMATED":$scope.testSuiteReportObject.testSuiteDetails.summary.passed+$scope.testSuiteReportObject.testSuiteDetails.summary.failed+$scope.testSuiteReportObject.testSuiteDetails.summary.aborted,
				"TOTAL":$scope.testSuiteReportObject.testSuiteDetails.summary.total});
		}
		if ($scope.testSuiteReportObject!=null && $scope.testSuiteReportObject.testCaseArray!=null && Object.prototype.toString.call($scope.testSuiteReportObject.testCaseArray) === '[object Array]'){
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: qdosService.getFilteredTestCaseArray($scope.testSuiteReportObject.testCaseArray, $scope.requestType)
		    });
		}

	}, true);	

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"My Sites - Automation Coverage",
				"anchor":"#/qdos/dashboard-automationCoverage/view"
			},
			{
				"displayName":$routeParams.siteName,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
			},
			{
				"displayName":$routeParams.version,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/version/"+window.encodeURIComponent($routeParams.version)
			}
		],
		"currentPageDisplayName":$routeParams.suiteName
	};
});
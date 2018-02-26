testcatalog.controller("testcatalogHomePageCtrl", function($scope,$cookies,catalogService) {
	$scope.isAjaxFinished = "false";
	$scope.detailedSharedCatalogs = [];
	catalogService.getDetailedSharedCatalogs($scope);
	$scope.myEmail = $cookies.email;
});

testcatalog.controller("mysitesCtrl", function($scope,$cookies,$filter,catalogService) {
	$scope.isAjaxFinished = "false";
	$scope.userSharedSites = [];
	catalogService.getAllOfMySites($scope,$filter('encodeURI')($cookies.email));
	$scope.myEmail = $cookies.email;
});

testcatalog.controller("testcatalogMysitesUserSingleSiteCtrl", function($scope,$routeParams,$sce,$cookies,catalogService) {
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/testcatalog/mysites/user";
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
	$scope.graphData.datacolumns=[{"id":"TOTAL_SUITES","type":$scope.graphType,"name":"Total Suites","color":"#FF7F0E"},
	 					          {"id":"TOTAL_TEST_CASES","type":$scope.graphType,"name":"Total Test Cases","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};
	$scope.tableData = [];
	catalogService.getVersionHistoryOfUserSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.numberOfRecords);
	
	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"My Sites",
				"anchor":"#/testcatalog/mysites/view"
			}
		],
		"currentPageDisplayName":$routeParams.siteName
	};
});

testcatalog.controller("testcatalogMysitesUserSingleSiteVersionCtrl", function($scope,$routeParams,$sce,$resource,$cookies,NgTableParams,catalogService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/testcatalog/mysites/user";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.suiteName = $routeParams.suiteName;
	catalogService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);
	$scope.graphData = {};
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"TOTAL_TEST_CASES","type":"bar","name":"Total Test Cases","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.totalTestCases !=null){
			$scope.graphData.datapoints.push({"x":serverData.versionNumber,"TOTAL_TEST_CASES":serverData.totalTestCases});
		}
		if (serverData!=null && serverData.testSuitesArray!=null && Object.prototype.toString.call(serverData.testSuitesArray) === '[object Array]'){
			var dataForTable = [];
			for(var index=0;index<serverData.testSuitesArray.length;index++){
				if (serverData.testSuitesArray[index].testSuite.suiteName == $scope.suiteName){
					dataForTable = serverData.testSuitesArray[index].testSuite.testCaseArray;
					break;
				}
			}
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: dataForTable
		    });
		}

	}, true);	

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"My Sites",
				"anchor":"#/testcatalog/mysites/view"
			},
			{
				"displayName":$routeParams.siteName,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
			}
		],
		"currentPageDisplayName":$routeParams.version
	};
});

testcatalog.controller("testcatalogMysitesUserSingleSiteVersionSuiteNameCtrl", function($scope,$routeParams,$sce,$resource,$cookies,NgTableParams,catalogService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/testcatalog/mysites/user";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.suiteName = $routeParams.suiteName;
	$scope.suiteDescription = "";
	catalogService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);
	$scope.graphData = {};
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"TOTAL_TEST_CASES","type":"bar","name":"Total Test Cases","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.totalTestCases !=null){
			$scope.graphData.datapoints.push({"x":serverData.versionNumber,"TOTAL_TEST_CASES":serverData.totalTestCases});
		}
		if (serverData!=null && serverData.testSuitesArray!=null && Object.prototype.toString.call(serverData.testSuitesArray) === '[object Array]'){
			var dataForTable = [];
			for(var index=0;index<serverData.testSuitesArray.length;index++){
				if (serverData.testSuitesArray[index].testSuite.suiteName == $scope.suiteName){
					$scope.suiteDescription = serverData.testSuitesArray[index].testSuite.suiteDescription;
					dataForTable = serverData.testSuitesArray[index].testSuite.testCaseArray;
					break;
				}
			}
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: dataForTable
		    });
		}

	}, true);	

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"My Sites",
				"anchor":"#/testcatalog/mysites/view"
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

testcatalog.controller("testcatalogMysitesUserSingleSiteVersionSuiteNameTestCaseIdCtrl", function($scope,$routeParams,$sce,$resource,$modal, $cookies,NgTableParams,catalogService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/testcatalog/mysites/user";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.suiteName = $routeParams.suiteName;
	$scope.testcase = {};
	$scope.testcase.datasets = {};
	$scope.selectedDataSetId = "";
	
	catalogService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);
	$scope.graphData = {};
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"TOTAL_TEST_STEPS","type":"bar","name":"Total Test Steps","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};
	
	$scope.changeSelectedDataSet = function(dataSetId){
		$scope.selectedDataSetId = dataSetId.id;
	};
	
	$scope.showElementKeyValuePairs = function(){
		var keyValuePairs;
		for(var index=0;index < $scope.serverData.testSuitesArray.length;index++){
			if ($scope.serverData.testSuitesArray[index].testSuite.suiteName == $scope.suiteName){
				keyValuePairs = $scope.serverData.testSuitesArray[index].testSuite.keyValuePairs;
			}
		}
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.version.suitename.keyvaluepairs.html',
	      controller: 'testcatalogUserSingleSiteVersionSuiteNameTestCaseIdKeyValuePairsCtrl',
	      keyboard:true,
	      size:"lg",
	      resolve: {
	    	  keyValuePairs: function () {
		      return keyValuePairs;
		    }
		  }
	    });
	};

	$scope.getValueOfKey = function(key){
		for(var index=0;index < $scope.serverData.testSuitesArray.length;index++){
			if ($scope.serverData.testSuitesArray[index].testSuite.suiteName == $scope.suiteName){
				if ($scope.serverData.testSuitesArray[index].testSuite.keyValuePairs[key]!=null){
					return $scope.serverData.testSuitesArray[index].testSuite.keyValuePairs[key];
				}
			}
		}
	};

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if (serverData!=null && serverData.testSuitesArray!=null && Object.prototype.toString.call(serverData.testSuitesArray) === '[object Array]'){
			var testCaseArrayFromServerData = [];
			for(var index=0;index<serverData.testSuitesArray.length;index++){
				if (serverData.testSuitesArray[index].testSuite.suiteName == $scope.suiteName){
					testCaseArrayFromServerData = serverData.testSuitesArray[index].testSuite.testCaseArray;
					for(var subIndex=0; subIndex<testCaseArrayFromServerData.length;subIndex++){
						if (testCaseArrayFromServerData[subIndex].testCaseId == $routeParams.testCaseId){
							$scope.testcase = testCaseArrayFromServerData[subIndex];
							break;
						}
					}
					break;
				}
			}
			$scope.graphData.datapoints.push({"x":$scope.testcase.testCaseId,"TOTAL_TEST_STEPS":$scope.testcase.testStepsData.length});

			$scope.testcase.datasets = {};
			$scope.testcase.datasets.array = new Array();
			
			for (var prop in $scope.testcase.dataSets){
				for (var datasetItemIndex in $scope.testcase.dataSets[prop]){
					if (datasetItemIndex==0){
						$scope.selectedDataSetId = $scope.testcase.dataSets[prop][datasetItemIndex].id;
					}
					var objData = new Object();
					objData.id = $scope.testcase.dataSets[prop][datasetItemIndex].id;
					objData.description = $scope.testcase.dataSets[prop][datasetItemIndex].description;
					$scope.testcase.datasets.array.push(objData);
				}
				break;
			}
			
		}

	}, false);
	
	$scope.$watch('selectedDataSetId', function(selectedDataSetId, oldselectedDataSetId) {
		if (typeof($scope.testcase.testStepsData)!="undefined"){
			
			var testCaseArray = new Array();
			for (var testStepIndex in $scope.testcase.testStepsData){
				var testStep = new Object();
				testStep.stepId = $scope.testcase.testStepsData[testStepIndex].stepId;
				testStep.stepDescription = $scope.testcase.testStepsData[testStepIndex].stepDescription;
				testStep.stepKeyword = $scope.testcase.testStepsData[testStepIndex].stepKeyword;
				testStep.proceedOnFail = $scope.testcase.testStepsData[testStepIndex].proceedOnFail;
				testStep.elementType = $scope.testcase.testStepsData[testStepIndex].elementType;
				testStep.elementKey = $scope.testcase.testStepsData[testStepIndex].elementKey;
				if ($scope.testcase.testStepsData[testStepIndex].elementValue!=""){
					for (var dataSetIndex in $scope.testcase.dataSets[$scope.testcase.testStepsData[testStepIndex].elementValue]){
						if (selectedDataSetId == $scope.testcase.dataSets[$scope.testcase.testStepsData[testStepIndex].elementValue][dataSetIndex].id){
							testStep.elementValue = $scope.testcase.dataSets[$scope.testcase.testStepsData[testStepIndex].elementValue][dataSetIndex].value;
							break;
						}
					}
				} else {
					testStep.elementValue = "N/A";
				}
				testCaseArray.push(testStep);
			}
			$scope.tableParams = new NgTableParams({
			      page: 1,
			      count: 10
			    }, {
			      filterDelay: 300,
			      dataset: testCaseArray
			    });
		}
		
	}, false);

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"My Sites",
				"anchor":"#/testcatalog/mysites/view"
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

testcatalog.controller("testcatalogUserSitesCtrl", function($scope,$routeParams,$cookies,catalogService) {
	$scope.isAjaxFinished = "false";
	$scope.userSharedCatalogs = [];
	catalogService.getUserSharedCatalogs($scope,$routeParams.user);
	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites",
				"anchor":"#/testcatalog"
			}
		],
		"currentPageDisplayName":$routeParams.user
	};
	$scope.myEmail = $cookies.email;
});

testcatalog.controller("testcatalogUserSingleSiteCtrl", function($scope,$routeParams,$sce,$cookies,catalogService) {
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/testcatalog";
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
	$scope.graphData.datacolumns=[{"id":"TOTAL_SUITES","type":$scope.graphType,"name":"Total Suites","color":"#FF7F0E"},
	 					          {"id":"TOTAL_TEST_CASES","type":$scope.graphType,"name":"Total Test Cases","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};
	$scope.tableData = [];
	catalogService.getVersionHistoryOfUserSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.numberOfRecords);
	
	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites",
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

testcatalog.controller("testcatalogUserSingleSiteVersionCtrl", function($scope,$routeParams,$sce,$cookies,catalogService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/testcatalog";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.graphData = {};
	$scope.testSuitesArray = [];
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"TOTAL_SUITES","type":"bar","name":"Total Suites","color":"#FF7F0E"},
								  {"id":"TOTAL_TEST_CASES","type":"bar","name":"Total Test Cases","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};
	
	catalogService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.totalTestCases !=null){
			
			$scope.graphData.datapoints.push({"x":serverData.versionNumber,
				"TOTAL_SUITES":serverData.totalTestSuites,
				"TOTAL_TEST_CASES":serverData.totalTestCases});
		}
	}, true);

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites",
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

testcatalog.controller("testcatalogUserSingleSiteVersionSuiteNameCtrl", function($scope,$routeParams,$sce,$resource,$cookies,NgTableParams,catalogService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/testcatalog";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.suiteName = $routeParams.suiteName;
	$scope.suiteDescription = "";
	catalogService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);
	$scope.graphData = {};
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"TOTAL_TEST_CASES","type":"bar","name":"Total Test Cases","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if(serverData!=null && serverData.totalTestCases !=null){
			$scope.graphData.datapoints.push({"x":serverData.versionNumber,"TOTAL_TEST_CASES":serverData.totalTestCases});
		}
		if (serverData!=null && serverData.testSuitesArray!=null && Object.prototype.toString.call(serverData.testSuitesArray) === '[object Array]'){
			var dataForTable = [];
			for(var index=0;index<serverData.testSuitesArray.length;index++){
				if (serverData.testSuitesArray[index].testSuite.suiteName == $scope.suiteName){
					$scope.suiteDescription = serverData.testSuitesArray[index].testSuite.suiteDescription;
					dataForTable = serverData.testSuitesArray[index].testSuite.testCaseArray;
					break;
				}
			}
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: dataForTable
		    });
		}

	}, true);	

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites",
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

testcatalog.controller("testcatalogUserSingleSiteVersionSuiteNameTestCaseIdCtrl", function($scope,$routeParams,$sce,$resource,$modal, $cookies,NgTableParams,catalogService){
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/testcatalog";
	$scope.myEmail = $cookies.email;
	$scope.serverData = {};
	$scope.suiteName = $routeParams.suiteName;
	$scope.testcase = {};
	$scope.testcase.datasets = {};
	$scope.selectedDataSetId = "";
	
	catalogService.getVersionDetailsOfUserSingleSite($scope,$routeParams.user,$routeParams.siteName,$routeParams.version);
	$scope.graphData = {};
	$scope.graphData.datapoints=[];
	$scope.graphData.datacolumns=[{"id":"TOTAL_TEST_STEPS","type":"bar","name":"Total Test Steps","color":"#000000"}];
	$scope.graphData.datax={"id":"x"};
	
	$scope.changeSelectedDataSet = function(dataSetId){
		$scope.selectedDataSetId = dataSetId.id;
	};
	
	$scope.showElementKeyValuePairs = function(){
		var keyValuePairs;
		for(var index=0;index < $scope.serverData.testSuitesArray.length;index++){
			if ($scope.serverData.testSuitesArray[index].testSuite.suiteName == $scope.suiteName){
				keyValuePairs = $scope.serverData.testSuitesArray[index].testSuite.keyValuePairs;
			}
		}
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.version.suitename.keyvaluepairs.html',
	      controller: 'testcatalogUserSingleSiteVersionSuiteNameTestCaseIdKeyValuePairsCtrl',
	      keyboard:true,
	      size:"lg",
	      resolve: {
	    	  keyValuePairs: function () {
		      return keyValuePairs;
		    }
		  }
	    });
	};

	$scope.getValueOfKey = function(key){
		for(var index=0;index < $scope.serverData.testSuitesArray.length;index++){
			if ($scope.serverData.testSuitesArray[index].testSuite.suiteName == $scope.suiteName){
				if ($scope.serverData.testSuitesArray[index].testSuite.keyValuePairs[key]!=null){
					return $scope.serverData.testSuitesArray[index].testSuite.keyValuePairs[key];
				}
			}
		}
	};

	$scope.$watch('serverData', function(serverData, oldserverData) {
		if (serverData!=null && serverData.testSuitesArray!=null && Object.prototype.toString.call(serverData.testSuitesArray) === '[object Array]'){
			var testCaseArrayFromServerData = [];
			for(var index=0;index<serverData.testSuitesArray.length;index++){
				if (serverData.testSuitesArray[index].testSuite.suiteName == $scope.suiteName){
					testCaseArrayFromServerData = serverData.testSuitesArray[index].testSuite.testCaseArray;
					for(var subIndex=0; subIndex<testCaseArrayFromServerData.length;subIndex++){
						if (testCaseArrayFromServerData[subIndex].testCaseId == $routeParams.testCaseId){
							$scope.testcase = testCaseArrayFromServerData[subIndex];
							break;
						}
					}
					break;
				}
			}
			$scope.graphData.datapoints.push({"x":$scope.testcase.testCaseId,"TOTAL_TEST_STEPS":$scope.testcase.testStepsData.length});
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: $scope.testcase.testStepsData
		    });
			$scope.testcase.datasets = {};
			$scope.testcase.datasets.array = new Array();
			for (var prop in $scope.testcase.dataSets){
				for (var datasetItemIndex in $scope.testcase.dataSets[prop]){
					if (datasetItemIndex==0){
						$scope.selectedDataSetId = $scope.testcase.dataSets[prop][datasetItemIndex].id;
					}
					var objData = new Object();
					objData.id = $scope.testcase.dataSets[prop][datasetItemIndex].id;
					objData.description = $scope.testcase.dataSets[prop][datasetItemIndex].description;
					$scope.testcase.datasets.array.push(objData);
				}
				break;
			}
		}

	}, false);
	
	$scope.$watch('selectedDataSetId', function(selectedDataSetId, oldselectedDataSetId) {
		if (typeof($scope.testcase.testStepsData)!="undefined"){
			
			var testCaseArray = new Array();
			for (var testStepIndex in $scope.testcase.testStepsData){
				var testStep = new Object();
				testStep.stepId = $scope.testcase.testStepsData[testStepIndex].stepId;
				testStep.stepDescription = $scope.testcase.testStepsData[testStepIndex].stepDescription;
				testStep.stepKeyword = $scope.testcase.testStepsData[testStepIndex].stepKeyword;
				testStep.proceedOnFail = $scope.testcase.testStepsData[testStepIndex].proceedOnFail;
				testStep.elementType = $scope.testcase.testStepsData[testStepIndex].elementType;
				testStep.elementKey = $scope.testcase.testStepsData[testStepIndex].elementKey;
				if ($scope.testcase.testStepsData[testStepIndex].elementValue!=""){
					for (var dataSetIndex in $scope.testcase.dataSets[$scope.testcase.testStepsData[testStepIndex].elementValue]){
						if (selectedDataSetId == $scope.testcase.dataSets[$scope.testcase.testStepsData[testStepIndex].elementValue][dataSetIndex].id){
							testStep.elementValue = $scope.testcase.dataSets[$scope.testcase.testStepsData[testStepIndex].elementValue][dataSetIndex].value;
							break;
						}
					}
				} else {
					testStep.elementValue = "N/A";
				}
				testCaseArray.push(testStep);
			}
			$scope.tableParams = new NgTableParams({
			      page: 1,
			      count: 10
			    }, {
			      filterDelay: 300,
			      dataset: testCaseArray
			    });
		}
		
	}, false);

	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"Public Sites",
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
			},
			{
				"displayName":$routeParams.suiteName,
				"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/version/"+window.encodeURIComponent($routeParams.version)+"/"+window.encodeURIComponent($routeParams.suiteName)
			}
		],
		"currentPageDisplayName":$routeParams.testCaseId
	};
});

testcatalog.controller("testcatalogUserSingleSiteVersionSuiteNameTestCaseIdKeyValuePairsCtrl", function($scope,$modalInstance, keyValuePairs, NgTableParams){
	$scope.keyValuePairs = keyValuePairs;
	var keyValueArray = new Array();
	for (var key in $scope.keyValuePairs) {
		var keyValuePair = new Object();
		keyValuePair["key"] = key;
		keyValuePair["value"] = $scope.keyValuePairs[key];
		keyValueArray.push(keyValuePair);
	}
	$scope.tableParams = new NgTableParams({
	      page: 1,
	      count: 10
	    }, {
	      filterDelay: 300,
	      dataset: keyValueArray
    });
	$scope.close = function () {
	    $modalInstance.dismiss('close');
	};
});

testcatalog.controller("testcatalogUserSingleSiteAddVersionCtrl", function($scope,$routeParams,$cookies,$modal,$sessionStorage,catalogService,NgTableParams,$window) {
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/testcatalog";
	$scope.testcatalog = {};
	$scope.testcatalog.addVersion = {};
	$scope.testcatalog.addVersion.buttons = {};
	$scope.testcatalog.addVersion.buttons.disabledAll = "false";
	$scope.testcatalog.addVersion.actions = {};
	$scope.downloadedFile = {};
	$scope.myEmail = $cookies.email;
	$scope.siteName = $routeParams.siteName;
	$scope.testSuitesLibrary = new Array();

	catalogService.getTestSuitesLibrary($scope);
	
	$scope.$watch('testSuitesLibrary', function(testSuitesLibrary, oldtestSuitesLibrary) {
		if ($scope.testSuitesLibrary!=null && Object.prototype.toString.call($scope.testSuitesLibrary) === '[object Array]'){
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: $scope.testSuitesLibrary
		    });
		}
	}, true);
	
	$scope.$watch('downloadedFile', function(downloadedFile, olddownloadedFile) {
		if (downloadedFile.fileName!=null){
			var downloadLink = document.createElement("a");
			document.body.appendChild(downloadLink);
			downloadLink.style = "display: none";
			var fName = $scope.downloadedFile.fileName;
			var file = new Blob([$scope.downloadedFile.data],{type: 'application/octet-stream'});
			var fileURL = (window.URL || window.webkitURL).createObjectURL(file);
			downloadLink.href = fileURL;
			downloadLink.download = fName;
			downloadLink.click();
			downloadLink.parentNode.removeChild(downloadLink);
			$scope.downloadedFile={};
		}
	}, true);
	
	$scope.s2ab = function (s) {
		var buf = new ArrayBuffer(s.length);
		var view = new Uint8Array(buf);
		for (var i=0; i!=s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
		return buf;
	};
	
	$scope.testcatalog.addVersion.actions.resetButtons = function(){
		$scope.testcatalog.addVersion.buttons.deleteSelectedSuites = {"label":"Deleted Selected Suites","hide":false};
		$scope.testcatalog.addVersion.buttons.commitVersion = {"label":"Commit Version","hide":false};
	};
	$scope.testcatalog.addVersion.actions.resetButtons();
	
	$scope.testcatalog.addVersion.actions.onSelectingCheckbox = function(item){
		for (var index=0;index<$scope.testSuitesLibrary.length;index++){
			if ($scope.testSuitesLibrary[index].id == item.id){
				$scope.testSuitesLibrary[index].isSelected=item.isSelected;
				break;
			}
		}
	};

	$scope.testcatalog.addVersion.actions.deleteTestSuites = function(){
		var itemsToDelete = new Array();
		for (var index=0;index<$scope.testSuitesLibrary.length;index++){
			if ($scope.testSuitesLibrary[index].isSelected){
				itemsToDelete.push($scope.testSuitesLibrary[index]);
			}
		}
		$scope.isAjaxFinished = "false";
		$scope.testcatalog.addVersion.buttons.deleteSelectedSuites = {"label":"Deleting...","hide":false};
		catalogService.deleteTestSuitesFromLibrary($scope,itemsToDelete, "multiSelect");
	};
	
	$scope.testcatalog.addVersion.actions.commitVersion = function() {
		var itemsToCommit = new Array();
		for (var index=0;index<$scope.testSuitesLibrary.length;index++){
			if ($scope.testSuitesLibrary[index].isSelected){
				itemsToCommit.push($scope.testSuitesLibrary[index]);
			}
		}
		$scope.isAjaxFinished = "false";
		$scope.testcatalog.addVersion.buttons.commitVersion = {"label":"Committing...","hide":false};
		catalogService.commitVersion($scope,itemsToCommit);
	};
	
	$scope.testcatalog.addVersion.actions.downloadExcelFile = function(item){
		catalogService.downloadTestSuite($scope, item);
	};
	
	$scope.testcatalog.addVersion.actions.deleteTestSuite = function(testSuiteDetails){
		var modelData = new Object();
		modelData.title = "Please confirm!";
		modelData.message = "Are you sure you want to delete this feature : "+testSuiteDetails.testSuite.suiteName + " ?";
		var itemsToDelete = new Array();
		itemsToDelete.push(testSuiteDetails);
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/views/confirmModalTemplate.html',
	      controller: 'confirmModelController',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  modelData: function () {
		      return modelData;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			catalogService.deleteTestSuitesFromLibrary($scope,itemsToDelete, "singleSelect");
		});
	}
		
	$scope.testcatalog.addVersion.actions.createTestSuite = function(){
		var createTestSuiteData = {
			"email" : $scope.myEmail,
			"siteName" : $scope.siteName,
			"activeAPIkey":$cookies.activeAPIkey,
			"suiteName":"",
			"suiteDescription": ""
		};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.createTestSuite.html',
	      controller: 'qdosUserSingleSiteAddVersionViaCreateTestSuiteCtrl',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  createTestSuiteData: function () {
		      return createTestSuiteData;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			$('.angular-meditor-toolbar').removeClass("angular-meditor-toolbar--show");
			catalogService.getTestSuitesLibrary($scope);
		});
		
	};
	
	$scope.testcatalog.addVersion.actions.copySuitesFromStoredVersions = function(){
		var requestData = {
			"email" : $scope.myEmail,
			"siteName" : $scope.siteName,
			"activeAPIkey":$cookies.activeAPIkey
		};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.copySuitesFromStoredVersions.html',
	      controller: 'qdosUserSingleSiteAddVersionViaCopySuitesFromPreviousVersionsCtrl',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  requestData: function () {
		      return requestData;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			catalogService.getTestSuitesLibrary($scope);
		});
	};
	
	$scope.testcatalog.addVersion.actions.localDevice = function(){
		var localDeviceData = {
			"email" : $scope.myEmail,
			"siteName" : $scope.siteName,
			"activeAPIkey":$cookies.activeAPIkey
		};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.localdevice.html',
	      controller: 'qdosUserSingleSiteAddVersionViaLocalDeviceCtrl',
	      keyboard:true,
	      size:"lg",
	      resolve: {
	    	  localDeviceData: function () {
		      return localDeviceData;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			catalogService.getTestSuitesLibrary($scope);
		});
	};
	
	$scope.breadcrumb = {
		"data":[
			{
				"displayName":"My Sites",
				"anchor":"#/testcatalog/mysites/view"
			},
			{
				"displayName":$scope.siteName,
				"anchor":$scope.moduleAccessURL+"/mysites/user"+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
			}
		],
		"currentPageDisplayName":"Add Version"
	};
});

testcatalog.controller("qdosUserSingleSiteAddVersionViaLocalDeviceCtrl", ['$scope','$modalInstance','localDeviceData','FileUploader','$timeout','$filter','$sessionStorage',function($scope,$modalInstance, localDeviceData,FileUploader,$timeout,$filter,$sessionStorage){
	$scope.close = function () {
	    //$modalInstance.dismiss('close');
	    $modalInstance.close();
	};
	
	$scope.localDeviceData = localDeviceData;
	
	$scope.uploader = new FileUploader({
        url: '/module/testcatalog/upload/saveTestSuite.rest'
    });
	
	$scope.uploader.errorMessage = new Array();
	$scope.uploader.displayErrorAs = "none";
	
	$scope.uploader.filters.push({
        name: 'customFilter',
        fn: function(item, options) {
        	if (this.queue.length < 20){
        		return true;
        	} else {
        		$scope.uploader.errorMessage.push("Maximum upload limit reached.");
        		$scope.uploader.displayErrorAs = "block";
        		return false;
        	}
            return ;
        }
    });
	
	$scope.uploader.filters.push({
        name: 'xlsFilter',
        fn: function(item, options) {
        	if (item.type == 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || item.type == 'application/vnd.ms-excel'){
        		return true;
        	} else {
        		$scope.uploader.errorMessage.push(item.name+" is not valid.");
        		$scope.uploader.displayErrorAs = "block";
        		return false;
        	}
        }
    });
	
	// CALLBACKS
	$scope.uploader.onWhenAddingFileFailed = function(item, filter, options) {
        $timeout(function () { $scope.uploader.errorMessage.length = 0; }, 10000);
        
    };
    $scope.uploader.onAfterAddingFile = function(fileItem) {
    	fileItem.formData = [	{"siteName":localDeviceData.siteName},
								{"fileName":fileItem.file.name},
								{"sourceType":"File Upload - Local Drive"}
    	                     ];
    };
    $scope.uploader.onAfterAddingAll = function(addedFileItems) {

    };
    $scope.uploader.onBeforeUploadItem = function(item) {

    };
    $scope.uploader.onProgressItem = function(fileItem, progress) {

    };
    $scope.uploader.onProgressAll = function(progress) {

    };
    $scope.uploader.onSuccessItem = function(fileItem, response, status, headers) {

    };
    $scope.uploader.onErrorItem = function(fileItem, response, status, headers) {

        $scope.uploader.errorMessage.push(fileItem.file.name + " :: " +response.message);
		$scope.uploader.displayErrorAs = "block";
        $timeout(function () { $scope.uploader.errorMessage.length = 0; }, 30000);
    };
    $scope.uploader.onCancelItem = function(fileItem, response, status, headers) {

    };
    $scope.uploader.onCompleteItem = function(fileItem, response, status, headers) {

    };
    $scope.uploader.onCompleteAll = function() {

    };


}]);

testcatalog.controller("qdosUserSingleSiteAddVersionViaCopySuitesFromPreviousVersionsCtrl", ['$scope','$modalInstance','requestData','catalogService','$timeout','$filter','$sessionStorage',function($scope,$modalInstance, requestData,catalogService,$timeout,$filter,$sessionStorage){
	$scope.close = function () {
	    //$modalInstance.dismiss('close');
	    $modalInstance.close();
	};
	$scope.isAjaxFinished = "false";
	$scope.requestData = requestData;
	$scope.jsonTestSuiteToSubmit = new Object();
	
	$scope.selectedSiteName = "-- Select Site Name --";
	$scope.selectedSiteOwner = "NULL";
	
	$scope.allPublicCatalogs = [];
	catalogService.getAllPublicCatalogs($scope);
	$scope.selectedCatalogVersionNumber = "-- Select Version --";
	$scope.catalogVersionsVsTestSuites = [];
	
	$scope.selectedTestSuiteName = "-- Select Test Suite Name --";
	$scope.selectedTestSuiteObject = null;
	$scope.listOfTestSuites = [];
	
	$scope.changeSelectedSiteName = function (row){
		$scope.isAjaxFinished = "false";
		$scope.catalogVersionsVsTestSuites = [];
		$scope.listOfTestSuites = [];
		$scope.selectedCatalogVersionNumber = "-- Select Version --";
		$scope.selectedTestSuiteName = "-- Select Test Suite Name --";
		$scope.selectedTestSuiteObject = null;
		$scope.selectedSiteName = row.siteName;
		$scope.selectedSiteOwner = row.email;
		catalogService.getCatalogVersionVsTestSuites($scope);
	};
	
	$scope.changeSelectedCatalogVersionNumber = function (row){
		$scope.selectedCatalogVersionNumber = row.version;
		$scope.selectedTestSuiteName = "-- Select Test Suite Name --";
		$scope.selectedTestSuiteObject = null;
		$scope.listOfTestSuites = row.testSuites;
	};
	
	$scope.changeSelectedSuiteName = function (row){
		$scope.selectedTestSuiteName = row.testSuite.suiteName;
		$scope.selectedTestSuiteObject = row;
		$scope.jsonTestSuiteToSubmit.fileName = "NULL";
		$scope.jsonTestSuiteToSubmit.fileLocation = "NULL";
		$scope.jsonTestSuiteToSubmit.email = $scope.requestData.email;
		$scope.jsonTestSuiteToSubmit.siteName = $scope.requestData.siteName;
		$scope.jsonTestSuiteToSubmit.sourceType = row.siteName + " - " + $scope.selectedCatalogVersionNumber;
		$scope.jsonTestSuiteToSubmit.sourceEmail = row.email;
		$scope.jsonTestSuiteToSubmit.testSuite = row.testSuite;
	};
	
	$scope.addTestSuiteFromStoredVersion = new Object();
	$scope.addTestSuiteFromStoredVersion.button = new Object();
	$scope.addTestSuiteFromStoredVersion.button.label = "Add Suite";
	$scope.addTestSuiteFromStoredVersion.button.disable = "false";
	
	$scope.submitSelectedTestSuite = function(){
		$scope.isAjaxFinished = "false";
		$scope.addTestSuiteFromStoredVersion.button.disable = "true";
		$scope.addTestSuiteFromStoredVersion.button.label = "Committing...";
		catalogService.copySingleTestSuiteFromStoredVersion($scope,$scope.jsonTestSuiteToSubmit);
	};
}]);

testcatalog.controller("qdosUserSingleSiteAddVersionViaCreateTestSuiteCtrl", ['$scope','$modalInstance','createTestSuiteData','catalogService','$timeout','$filter','$sessionStorage',function($scope,$modalInstance, createTestSuiteData,catalogService,$timeout,$filter,$sessionStorage){
	$scope.close = function () {
	    $modalInstance.close();
	};
	$scope.isAjaxFinished = "true";
	$scope.createTestSuiteData = createTestSuiteData;
	
	$scope.jsonTestSuiteToSubmit = new Object();
	$scope.jsonTestSuiteToSubmit.fileName = "NULL";
	$scope.jsonTestSuiteToSubmit.fileLocation = "NULL";
	$scope.jsonTestSuiteToSubmit.email = $scope.createTestSuiteData.email;
	$scope.jsonTestSuiteToSubmit.siteName = $scope.createTestSuiteData.siteName;
	$scope.jsonTestSuiteToSubmit.sourceType = "Website";
	$scope.jsonTestSuiteToSubmit.sourceEmail = $scope.createTestSuiteData.email;
	$scope.jsonTestSuiteToSubmit.testSuite = new Object();
	$scope.jsonTestSuiteToSubmit.testSuite.suiteName = $scope.createTestSuiteData.suiteName;
	$scope.jsonTestSuiteToSubmit.testSuite.suiteDescription = $scope.createTestSuiteData.suiteDescription!=null?$scope.createTestSuiteData.suiteDescription:"";
	
	$scope.singleTestSuite = new Object();
	$scope.singleTestSuite.button = new Object();
	$scope.singleTestSuite.button.label = "Submit";
	$scope.singleTestSuite.button.disable = "false";
	$scope.singleTestSuite.suiteNameFieldStatus = $scope.createTestSuiteData.suiteName!="";
	
	$scope.submitTestSuite = function(){
		if ($scope.jsonTestSuiteToSubmit!=null && $scope.jsonTestSuiteToSubmit.testSuite.suiteName!="" && $scope.jsonTestSuiteToSubmit.testSuite.suiteDescription!="") {
			console.log($filter("isStringFreeFromSpecialCharacters")($scope.jsonTestSuiteToSubmit.testSuite.suiteName) );
			console.log($scope.singleTestSuite.suiteNameFieldStatus);
			console.log($filter("isStringFreeFromSpecialCharacters")($scope.jsonTestSuiteToSubmit.testSuite.suiteName) || $scope.singleTestSuite.suiteNameFieldStatus);
			if ($filter("isStringFreeFromSpecialCharacters")($scope.jsonTestSuiteToSubmit.testSuite.suiteName) || $scope.singleTestSuite.suiteNameFieldStatus){
				$scope.isAjaxFinished = "false";
				$scope.singleTestSuite.button.error = "false";
				$scope.singleTestSuite.button.disable = "true";
				$scope.singleTestSuite.button.label = "Adding...";
				catalogService.createTestSuite($scope,$scope.jsonTestSuiteToSubmit);
				$('.angular-meditor-toolbar').removeClass("angular-meditor-toolbar--show");
			} else {
				$scope.singleTestSuite.button.error = "true";
				$scope.singleTestSuite.button.errorMessage = "Feature Name can't contain special characters (including spaces and underscore)";
			}
			
		} else {
			$scope.singleTestSuite.button.error = "true";
			$scope.singleTestSuite.button.errorMessage = "Please fill the mandatory parameters."; 
		}
	};
}]);

testcatalog.controller("testcatalogUserSingleSiteEditTestSuiteCtrl", function($scope,$routeParams,$cookies,$modal,$sessionStorage,catalogService,NgTableParams,$window) {
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/testcatalog";
	$scope.myEmail = $cookies.email;
	$scope.siteName = $routeParams.siteName;
	$scope.testSuiteName = $routeParams.testSuiteName;
	$scope.sourceType = $routeParams.sourceType;
	$scope.singleTestSuiteFromLibrary = new Object();
	$scope.actions = new Object();
	
	$scope.actions.addDataSchema = function(testCaseInfo){
		var createTestCaseDataSchema = {
				"email" : $scope.myEmail,
				"siteName" : $scope.siteName,
				"activeAPIkey":$cookies.activeAPIkey,
				"suiteName":$scope.testSuiteName,
				"testCaseInfo":testCaseInfo
			};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.createTestCaseDataSchema.html',
	      controller: 'qdosUserSingleSiteAddTestCaseDataSchemaCtrl',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  createTestCaseDataSchema: function () {
		      return createTestCaseDataSchema;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			catalogService.getSingleTestSuiteFromLibrary($scope);
		});
	};
	
	$scope.actions.deleteTestCase = function(testCaseInfo){
		var modelData = new Object();
		modelData.title = "Please confirm!";
		modelData.message = "Are you sure you want to delete this scenario with ID = "+testCaseInfo.testCaseId + " ?";
		var itemsToDelete = new Array();
		itemsToDelete.push(testCaseInfo);
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/views/confirmModalTemplate.html',
	      controller: 'confirmModelController',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  modelData: function () {
		      return modelData;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			catalogService.deleteTestCasesFromTestSuite($scope,itemsToDelete);
		});
	};
	
	$scope.actions.addTestCase = function(){
		var createTestCaseData = {
				"email" : $scope.myEmail,
				"siteName" : $scope.siteName,
				"activeAPIkey":$cookies.activeAPIkey,
				"suiteName":$scope.testSuiteName
			};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.createTestCase.html',
	      controller: 'qdosUserSingleSiteAddTestCaseCtrl',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  createTestCaseData: function () {
		      return createTestCaseData;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			$('.angular-meditor-toolbar').removeClass("angular-meditor-toolbar--show");
			catalogService.getSingleTestSuiteFromLibrary($scope);
		});
	};
	
	$scope.actions.editTestSuite = function(){
		var createTestSuiteData = {
				"email" : $scope.myEmail,
				"siteName" : $scope.siteName,
				"activeAPIkey":$cookies.activeAPIkey,
				"suiteName":$scope.testSuiteName,
				"suiteDescription": $scope.singleTestSuiteFromLibrary.testSuite.suiteDescription
			};
			var modalInstance = $modal.open({
		      animation: true,
		      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.createTestSuite.html',
		      controller: 'qdosUserSingleSiteAddVersionViaCreateTestSuiteCtrl',
		      keyboard:true,
		      size:"md",
		      resolve: {
		    	  createTestSuiteData: function () {
			      return createTestSuiteData;
			    }
			  }
		    });
			modalInstance.result.then(function () {
				$('.angular-meditor-toolbar').removeClass("angular-meditor-toolbar--show");
				catalogService.getSingleTestSuiteFromLibrary($scope);
			});
	};

	catalogService.getSingleTestSuiteFromLibrary($scope);
	
	$scope.$watch('singleTestSuiteFromLibrary', function(singleTestSuiteFromLibrary, oldsingleTestSuiteFromLibrary) {
		if ($scope.singleTestSuiteFromLibrary!=null 
				&& $scope.singleTestSuiteFromLibrary.testSuite!=null 
				&& $scope.singleTestSuiteFromLibrary.testSuite.testCaseArray!=null
				&& Object.prototype.toString.call($scope.singleTestSuiteFromLibrary.testSuite.testCaseArray) === '[object Array]'){
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: $scope.singleTestSuiteFromLibrary.testSuite.testCaseArray
		    });
		}
	}, true);
	
	$scope.breadcrumb = {
			"data":[
				{
					"displayName":"My Sites",
					"anchor":"#/testcatalog/mysites/view"
				},
				{
					"displayName":$scope.siteName,
					"anchor":$scope.moduleAccessURL+"/mysites/user"+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
				},
				{
					"displayName":"Add Version",
					"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/version/add"
				}
			],
			"currentPageDisplayName":$routeParams.testSuiteName+" - "+$routeParams.sourceType
		};
});

testcatalog.controller("qdosUserSingleSiteAddTestCaseCtrl", ['$scope','$modalInstance','createTestCaseData','catalogService','$timeout','$filter','$sessionStorage',function($scope,$modalInstance, createTestCaseData,catalogService,$timeout,$filter,$sessionStorage){
	$scope.close = function () {
	    $modalInstance.close();
	};
	$scope.isAjaxFinished = "true";
	$scope.createTestCaseData = createTestCaseData;
	
	$scope.jsonTestCaseToSubmit = new Object();
	$scope.jsonTestCaseToSubmit.testCaseName = createTestCaseData.testCaseName==null?"":createTestCaseData.testCaseName;
	$scope.jsonTestCaseToSubmit.testCaseId = createTestCaseData.testCaseId==null?"":createTestCaseData.testCaseId;
	$scope.jsonTestCaseToSubmit.testCaseIdFieldStatus = createTestCaseData.testCaseId!=null;
	$scope.jsonTestCaseToSubmit.testCaseMode = createTestCaseData.testCaseMode==null?"":createTestCaseData.testCaseMode;
	$scope.jsonTestCaseToSubmit.testCaseType = createTestCaseData.testCaseType==null?"":createTestCaseData.testCaseType;
	
	$scope.singleTestCase = new Object();
	$scope.singleTestCase.button = new Object();
	$scope.singleTestCase.button.label = "Submit";
	$scope.singleTestCase.button.disable = "false";
	
	
	$scope.submitTestCase = function(){
		if ($scope.jsonTestCaseToSubmit!=null && $scope.jsonTestCaseToSubmit.testCaseName!="" && $scope.jsonTestCaseToSubmit.testCaseId!="" && $scope.jsonTestCaseToSubmit.testCaseMode!="") {
			if ($filter("isStringFreeFromSpecialCharacters")($scope.jsonTestCaseToSubmit.testCaseId)){
				$scope.isAjaxFinished = "false";
				$scope.singleTestCase.button.error = "false";
				$scope.singleTestCase.button.disable = "true";
				$scope.singleTestCase.button.label = "Adding...";
				catalogService.createTestCase($scope,$scope.jsonTestCaseToSubmit);
				$('.angular-meditor-toolbar').removeClass("angular-meditor-toolbar--show");
			} else {
				$scope.singleTestCase.button.error = "true";
				$scope.singleTestCase.button.errorMessage = "Scenario ID can't contain special characters (including spaces and underscore).";
			}
			
		} else {
			$scope.singleTestCase.button.error = "true";
			$scope.singleTestCase.button.errorMessage = "Please fill the mandatory parameters."; 
		}
	};
}]);

testcatalog.controller("qdosUserSingleSiteAddTestCaseDataSchemaCtrl", ['$scope','$modalInstance','createTestCaseDataSchema','catalogService','$timeout','$filter','$sessionStorage',function($scope,$modalInstance, createTestCaseDataSchema,catalogService,$timeout,$filter,$sessionStorage){
	$scope.close = function () {
	    $modalInstance.close();
	};
	$scope.isAjaxFinished = "true";
	$scope.createTestCaseDataSchema = createTestCaseDataSchema;
	
	$scope.jsonTestCaseDataSchemaToSubmit = new Object();
	if (createTestCaseDataSchema.testCaseInfo.dataSets==null){
		$scope.jsonTestCaseDataSchemaToSubmit.fieldNames = null;
	} else {
		$scope.jsonTestCaseDataSchemaToSubmit.fieldNames = new Array();
		for(var fieldName in createTestCaseDataSchema.testCaseInfo.dataSets){
			$scope.jsonTestCaseDataSchemaToSubmit.fieldNames.push(fieldName);   
		}
	}
	
	
	$scope.singleTestCaseDataSchema = new Object();
	$scope.singleTestCaseDataSchema.button = new Object();
	$scope.singleTestCaseDataSchema.button.label = "Submit";
	$scope.singleTestCaseDataSchema.button.disable = "false";
	
	
	$scope.submitTestCaseDataSchema = function(){
		if ($scope.jsonTestCaseDataSchemaToSubmit!=null && $scope.jsonTestCaseDataSchemaToSubmit.fieldNames!=null) {
			$scope.isAjaxFinished = "false";
			$scope.singleTestCaseDataSchema.button.error = "false";
			$scope.singleTestCaseDataSchema.button.disable = "true";
			$scope.singleTestCaseDataSchema.button.label = "Adding...";
			catalogService.createTestCaseDataSchema($scope,$scope.jsonTestCaseDataSchemaToSubmit);
			
		} else {
			$scope.singleTestCaseDataSchema.button.error = "true";
			$scope.singleTestCaseDataSchema.button.errorMessage = "Please fill the mandatory parameters."; 
		}
	};
}]);


testcatalog.controller("testcatalogUserSingleSiteEditTestDataCtrl", function($scope,$routeParams,$cookies,$modal,$sessionStorage,catalogService,NgTableParams,$window) {
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/testcatalog";
	$scope.myEmail = $cookies.email;
	$scope.siteName = $routeParams.siteName;
	$scope.testSuiteName = $routeParams.testSuiteName;
	$scope.sourceType = $routeParams.sourceType;
	$scope.testCaseId = $routeParams.testCaseId
	$scope.singleTestSuiteFromLibrary = new Object();
	$scope.completeTestCase = new Object();
	$scope.actions = new Object();
	$scope.tableItems = new Array();
	$scope.dataSetItemToView = null;

	catalogService.getSingleTestSuiteFromLibrary($scope);
	
	$scope.$watch('singleTestSuiteFromLibrary', function(singleTestSuiteFromLibrary, oldsingleTestSuiteFromLibrary) {
		if ($scope.singleTestSuiteFromLibrary!=null 
				&& $scope.singleTestSuiteFromLibrary.testSuite!=null 
				&& $scope.singleTestSuiteFromLibrary.testSuite.testCaseArray!=null
				&& Object.prototype.toString.call($scope.singleTestSuiteFromLibrary.testSuite.testCaseArray) === '[object Array]'){
			
			for (var index=0;index<$scope.singleTestSuiteFromLibrary.testSuite.testCaseArray.length;index++){
				if ($scope.singleTestSuiteFromLibrary.testSuite.testCaseArray[index].testCaseId === $scope.testCaseId){
					$scope.completeTestCase = $scope.singleTestSuiteFromLibrary.testSuite.testCaseArray[index];
				}
			}
			$scope.tableItems.length=0;
			for(var fieldName in $scope.completeTestCase.dataSets){
				for(var arrayIndex=0;arrayIndex<$scope.completeTestCase.dataSets[fieldName].length;arrayIndex++){
					var rowItem = new Object();
					rowItem.id = $scope.completeTestCase.dataSets[fieldName][arrayIndex].id;
					rowItem.description = $scope.completeTestCase.dataSets[fieldName][arrayIndex].description;
					$scope.tableItems.push(rowItem);
				}
				break;
			}
			
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: $scope.tableItems
		    });
		}
	}, true);
	
	$scope.actions.viewDataSet = function(row){
		$scope.dataSetItemToView = new Object();
		$scope.dataSetItemToView.id = row.id;
		$scope.dataSetItemToView.description = row.description;
		$scope.dataSetItemToView.fields = new Array();
		for(var fieldName in $scope.completeTestCase.dataSets){
			for(var arrayIndex=0;arrayIndex<$scope.completeTestCase.dataSets[fieldName].length;arrayIndex++){
				var rowItem = new Object();
				if (row.id == $scope.completeTestCase.dataSets[fieldName][arrayIndex].id){
					rowItem.name = fieldName;
					rowItem.value = $scope.completeTestCase.dataSets[fieldName][arrayIndex].value;
					$scope.dataSetItemToView.fields.push(rowItem);
				}
				
			}
		}
	};
	
	$scope.actions.addDataSet = function(){
		var createDataSet = {
				"email" : $scope.myEmail,
				"siteName" : $scope.siteName,
				"activeAPIkey":$cookies.activeAPIkey,
				"suiteName":$scope.testSuiteName,
				"completeTestCase":$scope.completeTestCase
			};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.createDataSet.html',
	      controller: 'qdosUserSingleSiteAddDataSetCtrl',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  createDataSet: function () {
		      return createDataSet;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			$('.angular-meditor-toolbar').removeClass("angular-meditor-toolbar--show");
			catalogService.getSingleTestSuiteFromLibrary($scope);
		});
	};
	
	$scope.actions.deleteDataSet = function(row){
		var modelData = new Object();
		modelData.title = "Please confirm!";
		modelData.message = "Are you sure you want to delete this data set with ID = "+row.id + " ?";
		var dataSetToDelete = new Object();
		dataSetToDelete.id=row.id;
		dataSetToDelete.description=row.description;
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/views/confirmModalTemplate.html',
	      controller: 'confirmModelController',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  modelData: function () {
		      return modelData;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			catalogService.deleteDataSetFromTestCase($scope,dataSetToDelete);
		});
	};
	
	$scope.actions.editDataSet = function(row){
		var createDataSet = {
				"email" : $scope.myEmail,
				"siteName" : $scope.siteName,
				"activeAPIkey":$cookies.activeAPIkey,
				"suiteName":$scope.testSuiteName,
				"completeTestCase":$scope.completeTestCase,
				"dataSetId":row.id,
				"dataSetDescription":row.description
			};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.createDataSet.html',
	      controller: 'qdosUserSingleSiteAddDataSetCtrl',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  createDataSet: function () {
		      return createDataSet;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			$('.angular-meditor-toolbar').removeClass("angular-meditor-toolbar--show");
			catalogService.getSingleTestSuiteFromLibrary($scope);
		});
	};
	
	$scope.breadcrumb = {
			"data":[
				{
					"displayName":"My Sites",
					"anchor":"#/testcatalog/mysites/view"
				},
				{
					"displayName":$scope.siteName,
					"anchor":$scope.moduleAccessURL+"/mysites/user"+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
				},
				{
					"displayName":"Add Version",
					"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/version/add"
				},
				{
					"displayName":$routeParams.testSuiteName+" - "+$routeParams.sourceType,
					"anchor":$scope.moduleAccessURL+"/edit/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/suiteName/"+window.encodeURIComponent($routeParams.testSuiteName)+"/"+window.encodeURIComponent($routeParams.sourceType)
				}
			],
			"currentPageDisplayName":$routeParams.testCaseId+" - DATA SETS"
		};
});

testcatalog.controller("qdosUserSingleSiteAddDataSetCtrl", ['$scope','$modalInstance','createDataSet','catalogService','$timeout','$filter','$sessionStorage',function($scope,$modalInstance, createDataSet,catalogService,$timeout,$filter,$sessionStorage){
	$scope.close = function () {
	    $modalInstance.close();
	};
	$scope.isAjaxFinished = "true";
	$scope.createDataSet = createDataSet;
	
	$scope.jsonDataSetToSubmit = new Object();
	$scope.jsonDataSetToSubmit.fields = new Array();
	$scope.jsonDataSetToSubmit.dataSetId = createDataSet.dataSetId==null?"":createDataSet.dataSetId;
	$scope.jsonDataSetToSubmit.dataSetDescription = createDataSet.dataSetDescription==null?"":createDataSet.dataSetDescription;
	$scope.jsonDataSetToSubmit.dataSetIdFieldStatus = createDataSet.dataSetId!=null;
	

	for(var fieldName in createDataSet.completeTestCase.dataSets){
		var field = new Object();
		field.name = fieldName;
		
		if (createDataSet.dataSetId==null){
			field.value = "";
		} else {
			for(var arrayIndex=0;arrayIndex<createDataSet.completeTestCase.dataSets[fieldName].length;arrayIndex++){
				if (createDataSet.dataSetId == createDataSet.completeTestCase.dataSets[fieldName][arrayIndex].id){
					field.value = createDataSet.completeTestCase.dataSets[fieldName][arrayIndex].value;
				}
				
			}
		}
		$scope.jsonDataSetToSubmit.fields.push(field);
	}
	
	$scope.singleDataSet = new Object();
	$scope.singleDataSet.button = new Object();
	$scope.singleDataSet.button.label = "Submit";
	$scope.singleDataSet.button.disable = "false";
	
	
	$scope.submitDataSet = function(){
		if ($scope.jsonDataSetToSubmit!=null && $scope.jsonDataSetToSubmit.dataSetId!="" && $scope.jsonDataSetToSubmit.dataSetDescription!="" && $scope.jsonDataSetToSubmit.fields.length>0) {
			if ($filter("isStringFreeFromSpecialCharacters")($scope.jsonDataSetToSubmit.dataSetId)){
				var fieldNameVsDataSet = new Object();
				for (var index=0;index<$scope.jsonDataSetToSubmit.fields.length;index++){
					var dataSet = new Object();
					dataSet.id = $scope.jsonDataSetToSubmit.dataSetId;
					dataSet.description = $scope.jsonDataSetToSubmit.dataSetDescription;
					dataSet.value = $scope.jsonDataSetToSubmit.fields[index].value;
					fieldNameVsDataSet[$scope.jsonDataSetToSubmit.fields[index].name] = dataSet;
					
				}
				$scope.isAjaxFinished = "false";
				$scope.singleDataSet.button.error = "false";
				$scope.singleDataSet.button.disable = "true";
				$scope.singleDataSet.button.label = "Adding...";
				catalogService.creatDataSet($scope,fieldNameVsDataSet);
				$('.angular-meditor-toolbar').removeClass("angular-meditor-toolbar--show");
			} else {
				$scope.singleDataSet.button.error = "true";
				$scope.singleDataSet.button.errorMessage = "Data Set ID can't contain special characters (including spaces and underscore).";
			}
			
		} else {
			$scope.singleDataSet.button.error = "true";
			$scope.singleDataSet.button.errorMessage = "Please fill the mandatory parameters."; 
		}
	};
}]);

testcatalog.controller("testcatalogUserSingleSiteEditTestStepsCtrl", function($scope,$routeParams,$cookies,$modal,$sessionStorage,catalogService,NgTableParams,$window) {
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/testcatalog";
	$scope.myEmail = $cookies.email;
	$scope.siteName = $routeParams.siteName;
	$scope.testSuiteName = $routeParams.testSuiteName;
	$scope.sourceType = $routeParams.sourceType;
	$scope.testCaseId = $routeParams.testCaseId
	$scope.singleTestSuiteFromLibrary = new Object();
	$scope.completeTestCase = new Object();
	$scope.suiteProperties = new Array();
	$scope.actions = new Object();
	$scope.tableItems = new Array();

	catalogService.getSingleTestSuiteFromLibrary($scope);
	
	$scope.$watch('singleTestSuiteFromLibrary', function(singleTestSuiteFromLibrary, oldsingleTestSuiteFromLibrary) {
		if ($scope.singleTestSuiteFromLibrary!=null 
				&& $scope.singleTestSuiteFromLibrary.testSuite!=null 
				&& $scope.singleTestSuiteFromLibrary.testSuite.testCaseArray!=null
				&& Object.prototype.toString.call($scope.singleTestSuiteFromLibrary.testSuite.testCaseArray) === '[object Array]'){
			
			for (var index=0;index<$scope.singleTestSuiteFromLibrary.testSuite.testCaseArray.length;index++){
				if ($scope.singleTestSuiteFromLibrary.testSuite.testCaseArray[index].testCaseId === $scope.testCaseId){
					$scope.completeTestCase = $scope.singleTestSuiteFromLibrary.testSuite.testCaseArray[index];
				}
			}
			$scope.tableItems.length=0;
			$scope.tableItems = $scope.completeTestCase.testStepsData;
			$scope.suiteProperties.length=0;
			for (var prop in $scope.singleTestSuiteFromLibrary.testSuite.keyValuePairs){
				$scope.suiteProperties.push(prop);
			}
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: $scope.tableItems
		    });
		}
	}, true);
	
	$scope.actions.editTestCase = function(){
		var createTestCaseData = {
				"email" : $scope.myEmail,
				"siteName" : $scope.siteName,
				"activeAPIkey":$cookies.activeAPIkey,
				"suiteName":$scope.testSuiteName,
				"testCaseId":$scope.completeTestCase.testCaseId,
				"testCaseName":$scope.completeTestCase.testCaseName,
				"testCaseMode":$scope.completeTestCase.testCaseMode,
				"testCaseType":$scope.completeTestCase.testCaseType
			};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.createTestCase.html',
	      controller: 'qdosUserSingleSiteAddTestCaseCtrl',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  createTestCaseData: function () {
		      return createTestCaseData;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			$('.angular-meditor-toolbar').removeClass("angular-meditor-toolbar--show");
			catalogService.getSingleTestSuiteFromLibrary($scope);
		});
	};
	
	$scope.actions.deleteTestStep = function(row){
		var modelData = new Object();
		modelData.title = "Please confirm!";
		modelData.message = "Are you sure you want to delete this Test Step with ID = "+row.stepId + " ?";
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/views/confirmModalTemplate.html',
	      controller: 'confirmModelController',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  modelData: function () {
		      return modelData;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			catalogService.deleteTestStep($scope,row);
		});
	};
	
	$scope.actions.editTestStep = function(row){
		var createTestStepData = {
				"email" : $scope.myEmail,
				"siteName" : $scope.siteName,
				"activeAPIkey":$cookies.activeAPIkey,
				"suiteName":$scope.testSuiteName,
				"completeTestCase":$scope.completeTestCase,
				"suiteProperties":$scope.suiteProperties,
				"stepId":row.stepId,
				"stepDescription":row.stepDescription,
				"stepKeyword":row.stepKeyword,
				"proceedOnFail":row.proceedOnFail,
				"elementValue":row.elementValue,
				"elementType":row.elementType,
				"elementKey":row.elementKey
			};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.createTestStep.html',
	      controller: 'testCatalogUserSingleSiteAddTestStepCtrl',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  createTestStepData: function () {
		      return createTestStepData;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			$('.angular-meditor-toolbar').removeClass("angular-meditor-toolbar--show");
			catalogService.getSingleTestSuiteFromLibrary($scope);
		});
	};
	
	$scope.actions.addTestStep = function(){
		var createTestStepData = {
				"email" : $scope.myEmail,
				"siteName" : $scope.siteName,
				"activeAPIkey":$cookies.activeAPIkey,
				"suiteName":$scope.testSuiteName,
				"completeTestCase":$scope.completeTestCase,
				"suiteProperties":$scope.suiteProperties
			};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.createTestStep.html',
	      controller: 'testCatalogUserSingleSiteAddTestStepCtrl',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  createTestStepData: function () {
		      return createTestStepData;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			$('.angular-meditor-toolbar').removeClass("angular-meditor-toolbar--show");
			catalogService.getSingleTestSuiteFromLibrary($scope);
		});
	};
	
	$scope.breadcrumb = {
			"data":[
				{
					"displayName":"My Sites",
					"anchor":"#/testcatalog/mysites/view"
				},
				{
					"displayName":$scope.siteName,
					"anchor":$scope.moduleAccessURL+"/mysites/user"+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
				},
				{
					"displayName":"Add Version",
					"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/version/add"
				},
				{
					"displayName":$routeParams.testSuiteName+" - "+$routeParams.sourceType,
					"anchor":$scope.moduleAccessURL+"/edit/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/suiteName/"+window.encodeURIComponent($routeParams.testSuiteName)+"/"+window.encodeURIComponent($routeParams.sourceType)
				}
			],
			"currentPageDisplayName":$routeParams.testCaseId+" - Steps"
		};
});

testcatalog.controller("testCatalogUserSingleSiteAddTestStepCtrl", ['$scope','$modalInstance','createTestStepData','catalogService','$timeout','$filter','$sessionStorage',function($scope,$modalInstance, createTestStepData,catalogService,$timeout,$filter,$sessionStorage){
	$scope.close = function () {
	    $modalInstance.close();
	};
	$scope.isAjaxFinished = "true";
	$scope.createTestStepData = createTestStepData;
	
	$scope.jsonTestStepToSubmit = new Object();
	$scope.jsonTestStepToSubmit.stepId = createTestStepData.stepId==null?"":createTestStepData.stepId;
	$scope.jsonTestStepToSubmit.stepDescription = createTestStepData.stepDescription==null?"":createTestStepData.stepDescription;
	$scope.jsonTestStepToSubmit.stepKeyword = createTestStepData.stepKeyword==null?"":createTestStepData.stepKeyword;
	$scope.jsonTestStepToSubmit.proceedOnFail = createTestStepData.proceedOnFail==null?"":createTestStepData.proceedOnFail;
	$scope.jsonTestStepToSubmit.elementType = createTestStepData.elementType==null?"":createTestStepData.elementType;
	$scope.jsonTestStepToSubmit.elementKey = createTestStepData.elementKey==null?"":createTestStepData.elementKey;
	$scope.jsonTestStepToSubmit.elementValue = createTestStepData.elementValue==null?"":createTestStepData.elementValue;
	$scope.jsonTestStepToSubmit.stepIdFieldStatus = createTestStepData.stepId!=null;
	
	
	$scope.singleTestStep = new Object();
	$scope.singleTestStep.button = new Object();
	$scope.singleTestStep.button.label = "Submit";
	$scope.singleTestStep.button.disable = "false";
	
	$scope.singleTestStep.dataFields = new Array();
	for(var fieldName in createTestStepData.completeTestCase.dataSets){
		$scope.singleTestStep.dataFields.push(fieldName);
	}
	
	$scope.singleTestStep.suiteProperties = createTestStepData.suiteProperties;
	
	$scope.submitTestStep = function(){
		if ($scope.jsonTestStepToSubmit!=null && $scope.jsonTestStepToSubmit.stepId!="" && $scope.jsonTestStepToSubmit.stepDescription!="" 
				&& $scope.jsonTestStepToSubmit.stepKeyword!="" && $scope.jsonTestStepToSubmit.proceedOnFail!="") {
			if ($filter("isStringFreeFromSpecialCharacters")($scope.jsonTestStepToSubmit.stepId)){
				$scope.isAjaxFinished = "false";
				$scope.singleTestStep.button.error = "false";
				$scope.singleTestStep.button.disable = "true";
				$scope.singleTestStep.button.label = "Adding...";
				catalogService.createTestStep($scope,$scope.jsonTestStepToSubmit);
				$('.angular-meditor-toolbar').removeClass("angular-meditor-toolbar--show");
			} else {
				$scope.singleTestStep.button.error = "true";
				$scope.singleTestStep.button.errorMessage = "Step ID can't contain special characters (including spaces and underscore).";
			}
			
		} else {
			$scope.singleTestStep.button.error = "true";
			$scope.singleTestStep.button.errorMessage = "Please fill the mandatory parameters."; 
		}
	};
}]);


testcatalog.controller("testcatalogUserSingleSiteEditTestSuiteElementPropertiesCtrl", function($scope,$routeParams,$cookies,$modal,$sessionStorage,catalogService,NgTableParams,$window) {
	$scope.isAjaxFinished = "false";
	$scope.moduleAccessURL = "#/testcatalog";
	$scope.myEmail = $cookies.email;
	$scope.siteName = $routeParams.siteName;
	$scope.testSuiteName = $routeParams.testSuiteName;
	$scope.sourceType = $routeParams.sourceType;
	$scope.singleTestSuiteFromLibrary = new Object();
	$scope.actions = new Object();
	$scope.tableItems = new Array();
	
	
	$scope.actions.addProperty = function(){
		var createProperty = {
				"email" : $scope.myEmail,
				"siteName" : $scope.siteName,
				"activeAPIkey":$cookies.activeAPIkey,
				"suiteName":$scope.testSuiteName
			};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.createElementProperty.html',
	      controller: 'testCatalogUserSingleSiteAddElementPropertyCtrl',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  createProperty: function () {
		      return createProperty;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			catalogService.getSingleTestSuiteFromLibrary($scope);
		});
	};
	
	$scope.actions.deleteProperty = function(row){
		var modelData = new Object();
		modelData.title = "Please confirm!";
		modelData.message = "Are you sure you want to delete this Element Variable = "+row.name + " ?";
		var itemToDelete = new Object();
		itemToDelete[row.name] = row.value;
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/views/confirmModalTemplate.html',
	      controller: 'confirmModelController',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  modelData: function () {
		      return modelData;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			catalogService.deleteElementProperty($scope,itemToDelete);
		});
	};
	
	$scope.actions.editProperty = function(row){
		var createProperty = {
				"email" : $scope.myEmail,
				"siteName" : $scope.siteName,
				"activeAPIkey":$cookies.activeAPIkey,
				"suiteName":$scope.testSuiteName,
				"elementPropertyName":row.name,
				"elementPropertyValue":row.value
			};
		var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.createElementProperty.html',
	      controller: 'testCatalogUserSingleSiteAddElementPropertyCtrl',
	      keyboard:true,
	      size:"md",
	      resolve: {
	    	  createProperty: function () {
		      return createProperty;
		    }
		  }
	    });
		modalInstance.result.then(function () {
			catalogService.getSingleTestSuiteFromLibrary($scope);
		});
	};
	
	catalogService.getSingleTestSuiteFromLibrary($scope);
	
	$scope.$watch('singleTestSuiteFromLibrary', function(singleTestSuiteFromLibrary, oldsingleTestSuiteFromLibrary) {
		if ($scope.singleTestSuiteFromLibrary!=null 
				&& $scope.singleTestSuiteFromLibrary.testSuite!=null 
				&& $scope.singleTestSuiteFromLibrary.testSuite.keyValuePairs!=null){
			
			$scope.tableItems.length = 0;
			for(var key in $scope.singleTestSuiteFromLibrary.testSuite.keyValuePairs){
				var obj = new Object();
				obj.name = key;
				obj.value = $scope.singleTestSuiteFromLibrary.testSuite.keyValuePairs[key];
				$scope.tableItems.push(obj);
			}
			
			$scope.tableParams = new NgTableParams({
		      page: 1,
		      count: 10
		    }, {
		      filterDelay: 300,
		      dataset: $scope.tableItems
		    });
		}
	}, true);
	
	$scope.breadcrumb = {
			"data":[
				{
					"displayName":"My Sites",
					"anchor":"#/testcatalog/mysites/view"
				},
				{
					"displayName":$scope.siteName,
					"anchor":$scope.moduleAccessURL+"/mysites/user"+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/10"
				},
				{
					"displayName":"Add Version",
					"anchor":$scope.moduleAccessURL+"/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/version/add"
				},
				{
					"displayName":$routeParams.testSuiteName+" - "+$routeParams.sourceType,
					"anchor":$scope.moduleAccessURL+"/edit/"+window.encodeURIComponent($routeParams.user)+"/"+window.encodeURIComponent($routeParams.siteName)+"/suiteName/"+$routeParams.testSuiteName+"/"+$routeParams.sourceType
				}
			],
			"currentPageDisplayName":"Element Properties"
		};
});

testcatalog.controller("testCatalogUserSingleSiteAddElementPropertyCtrl", ['$scope','$modalInstance','createProperty','catalogService','$timeout','$filter','$sessionStorage',function($scope,$modalInstance, createProperty,catalogService,$timeout,$filter,$sessionStorage){
	$scope.close = function () {
	    $modalInstance.close();
	};
	$scope.isAjaxFinished = "true";
	$scope.createProperty = createProperty;
	
	$scope.jsonElementPropertyToSubmit = new Object();
	$scope.jsonElementPropertyToSubmit.elementPropertyName = createProperty.elementPropertyName==null?"":createProperty.elementPropertyName;
	$scope.jsonElementPropertyToSubmit.elementPropertyValue = createProperty.elementPropertyValue==null?"":createProperty.elementPropertyValue;
	
	$scope.singleElementProperty = new Object();
	$scope.singleElementProperty.elementPropertyNameStatus = createProperty.elementPropertyName!=null;
	$scope.singleElementProperty.button = new Object();
	$scope.singleElementProperty.button.label = "Submit";
	$scope.singleElementProperty.button.disable = "false";
	
	
	$scope.submitElementProperty = function(){
		if ($scope.jsonElementPropertyToSubmit!=null && $scope.jsonElementPropertyToSubmit.elementPropertyName!=null && $scope.jsonElementPropertyToSubmit.elementPropertyValue!=null) {
			$scope.isAjaxFinished = "false";
			$scope.singleElementProperty.button.error = "false";
			$scope.singleElementProperty.button.disable = "true";
			$scope.singleElementProperty.button.label = "Adding...";
			var propertyToSubmit = new Object();
			propertyToSubmit[$scope.jsonElementPropertyToSubmit.elementPropertyName]=$scope.jsonElementPropertyToSubmit.elementPropertyValue;
			catalogService.createElementProperty($scope,propertyToSubmit);
			
		} else {
			$scope.singleElementProperty.button.error = "true";
			$scope.singleElementProperty.button.errorMessage = "Please fill the mandatory parameters."; 
		}
	};
}]);

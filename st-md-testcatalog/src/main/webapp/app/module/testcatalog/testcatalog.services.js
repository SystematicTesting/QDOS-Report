testcatalog.value("DetailedSharedCatalogsUrl","/module/testcatalog/sreport/detailedSharedCatalogs.rest?status=ACTIVE");
testcatalog.value("AllVersionsOfCatalogUrl","/module/testcatalog/sreport/allVersionsOfCatalog.rest?siteName=");
testcatalog.value("UserSharedCatalogsUrl","/module/testcatalog/sreport/userSharedCatalogs.rest?status=ACTIVE");
testcatalog.value("AllMyCatalogsUrl","/module/testcatalog/sreport/userAllSites.rest?status=ACTIVE");
testcatalog.value("TestSuitesLibraryUrl","/module/testcatalog/sreport/testSuitesLibrary.rest?siteName=");
testcatalog.value("SingleTestSuiteFromLibraryUrl","/module/testcatalog/sreport/singleTestSuiteFromLibrary.rest");
testcatalog.value("DeleteTestSuitesLibraryUrl","/module/testcatalog/sreport/deleteTestSuitesLibrary.rest");
testcatalog.value("CommitVersionUrl","/module/testcatalog/sreport/commitCatalog.rest");
testcatalog.value("DownloadTestSuiteUrl","/module/testcatalog/sreport/downloadTestSuite.rest");
testcatalog.value("CatalogVersionHistoryOfUserSiteUrl","/module/testcatalog/sreport/versionHistoryOfUserSite.rest");
testcatalog.value("CatalogVersionDetailsOfUserSingleSiteUrl","/module/testcatalog/sreport/versionDetailsOfSite.rest");
testcatalog.value("CopySingleTestSuiteFromStoredVersionUrl","/module/testcatalog/upload/copyTestSuite.rest");
testcatalog.value("CreateSingleTestSuiteViaWebsite","/module/testcatalog/upload/createTestSuite.rest");
testcatalog.value("CreateSingleTestCaseViaWebsite","/module/testcatalog/upload/createTestCase.rest");
testcatalog.value("CreateTestStepURL","/module/testcatalog/upload/createTestStep.rest");
testcatalog.value("CreateSingleTestCaseDataSchema","/module/testcatalog/upload/createTestCaseDataSchema.rest");
testcatalog.value("CreateElementPropertyURL","/module/testcatalog/upload/createElementProperty.rest");
testcatalog.value("CreateSingleTestCaseDataSet","/module/testcatalog/upload/createTestCaseDataSet.rest");
testcatalog.value("DeleteTestCasesURL","/module/testcatalog/upload/deleteTestCases.rest");
testcatalog.value("DeleteDataSetURL","/module/testcatalog/upload/deleteTestCaseDataSet.rest");
testcatalog.value("DeleteTestStepURL","/module/testcatalog/upload/deleteTestStep.rest");
testcatalog.value("DeleteElementPropertyURL","/module/testcatalog/upload/deleteElementProperty.rest");

testcatalog.service("catalogService", function (DetailedSharedCatalogsUrl,AllVersionsOfCatalogUrl,UserSharedCatalogsUrl, AllMyCatalogsUrl,TestSuitesLibraryUrl, DeleteTestSuitesLibraryUrl, CommitVersionUrl, DownloadTestSuiteUrl, DeleteTestCasesURL, CatalogVersionHistoryOfUserSiteUrl, CatalogVersionDetailsOfUserSingleSiteUrl,CopySingleTestSuiteFromStoredVersionUrl, CreateSingleTestSuiteViaWebsite, CreateElementPropertyURL, CreateSingleTestCaseViaWebsite, DeleteElementPropertyURL, CreateTestStepURL, CreateSingleTestCaseDataSchema, CreateSingleTestCaseDataSet, DeleteDataSetURL, DeleteTestStepURL, SingleTestSuiteFromLibraryUrl, $http) {
	var serviceObject = this;
	
	this.getDetailedSharedCatalogs = function($scope){
		var responsePromise = $http.get(DetailedSharedCatalogsUrl);
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
							SUtils.warn(SUtils.appConfig.testcatalogLogPrefix + "getDetailedSharedCatalogs() :: Expected Response of Array is not available on Response of URL : "+DetailedSharedCatalogsUrl);
						}
	                    $scope.detailedSharedCatalogs = result;
	                    $scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "getDetailedSharedCatalogs() :: AJAX Request failed on URL : "+DetailedSharedCatalogsUrl);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
	};
	
	this.getAllPublicCatalogs = function($scope){
		var responsePromise = $http.get(DetailedSharedCatalogsUrl);
		responsePromise.success(function(data, status, headers, config) {
						var result = [];
						if (data!=null && Object.prototype.toString.call(data) === '[object Array]'){
							result = data;
						} else {
							SUtils.warn(SUtils.appConfig.testcatalogLogPrefix + "getAllPublicCatalogs() :: Expected Response of Array is not available on Response of URL : "+DetailedSharedCatalogsUrl);
						}
	                    $scope.allPublicCatalogs = result;
	                    $scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "getAllPublicCatalogs() :: AJAX Request failed on URL : "+DetailedSharedCatalogsUrl);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
	};
	
	this.getCatalogVersionVsTestSuites = function($scope){
		var responsePromise = $http.get(AllVersionsOfCatalogUrl+$scope.selectedSiteName+"&email="+$scope.selectedSiteOwner);
		responsePromise.success(function(data, status, headers, config) {
						var result = [];
						if (data!=null && Object.keys(data).length != 0){
							for (var item in data){
								var obj = new Object();
								obj.version = item;
								obj.testSuites = data[item];
								result.push(obj);
							}
						} else {
							SUtils.warn(SUtils.appConfig.testcatalogLogPrefix + "getCatalogVersionVsTestSuites() :: Expected Response of Array is not available on Response of URL : "+AllVersionsOfCatalogUrl);
						}
	                    $scope.catalogVersionsVsTestSuites = result;
	                    $scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "getCatalogVersionVsTestSuites() :: AJAX Request failed on URL : "+AllVersionsOfCatalogUrl);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
	};
	
	this.getUserSharedCatalogs = function($scope, email){
		var responsePromise = $http.get(UserSharedCatalogsUrl+"&email="+email);
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
							SUtils.warn(SUtils.appConfig.testcatalogLogPrefix + "getUserSharedCatalogs() :: Expected Response of Array is not available on Response of URL : "+UserSharedCatalogsUrl);
						}
	                    $scope.userSharedCatalogs = result;
	                    $scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "getUserSharedCatalogs() :: AJAX Request failed on URL : "+UserSharedCatalogsUrl);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
	};
	
	this.getAllOfMySites = function($scope, email){
		var responsePromise = $http.get(AllMyCatalogsUrl);
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
							SUtils.warn(SUtils.appConfig.qdosLogPrefix + "getAllOfMySites() :: Expected Response of Array is not available on Response of URL : "+AllMyCatalogsUrl);
						}
	                    $scope.userSharedSites = result;
	                    $scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.qdosLogPrefix + "getAllOfMySites() :: AJAX Request failed on URL : "+AllMyCatalogsUrl);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
	};
	
	this.getTestSuitesLibrary = function($scope){
		var responsePromise = $http.get(TestSuitesLibraryUrl+$scope.siteName);
		responsePromise.success(function(data, status, headers, config) {
						if (data!=null && Object.prototype.toString.call(data) === '[object Array]'){
							for(var index=0;index<data.length;index++){
								data[index].isSelected = true;
							}
							$scope.testSuitesLibrary = data;
						} else {
							$scope.testSuitesLibrary = [];
							SUtils.warn(SUtils.appConfig.testcatalogLogPrefix + "getTestSuitesLibrary() :: Expected Response of Array is not available on Response of URL : "+TestSuitesLibraryUrl);
						}
	                    $scope.isAjaxFinished = "true";
	                    $scope.testcatalog.addVersion.actions.resetButtons();
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "getTestSuitesLibrary() :: AJAX Request failed on URL : "+TestSuitesLibraryUrl);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "getTestSuitesLibrary() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "getTestSuitesLibrary() :: ERROR CODE : "+data.status);
	                    $scope.isAjaxFinished = "true";
	                    $scope.testcatalog.addVersion.actions.resetButtons();
	                    return [];
	                });
	};
	
	this.getSingleTestSuiteFromLibrary = function($scope){
		var responsePromise = $http.get(SingleTestSuiteFromLibraryUrl+"?siteName="+$scope.siteName+"&suiteName="+$scope.testSuiteName);
		responsePromise.success(function(data, status, headers, config) {
						if (data!=null){
							$scope.singleTestSuiteFromLibrary = data;
						} else {
							$scope.singleTestSuiteFromLibrary = new Object();
							SUtils.warn(SUtils.appConfig.testcatalogLogPrefix + "getSingleTestSuiteFromLibrary() :: Server responded with Null Object in Response of URL : "+SingleTestSuiteFromLibraryUrl);
						}
	                    $scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "getSingleTestSuiteFromLibrary() :: AJAX Request failed on URL : "+SingleTestSuiteFromLibraryUrl);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "getSingleTestSuiteFromLibrary() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "getSingleTestSuiteFromLibrary() :: ERROR CODE : "+data.status);
	                    $scope.isAjaxFinished = "true";
	                });
	};
	
	this.deleteTestSuitesFromLibrary = function($scope,itemsToDelete, calledBy){
		var payLoad = {"testSuitesLibrary":itemsToDelete};
		var responsePromise = $http({
	        url: DeleteTestSuitesLibraryUrl,
	        method: "POST",
	        data: payLoad,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
						serviceObject.getTestSuitesLibrary($scope);
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteTestSuitesFromLibrary() :: AJAX Request failed on URL : "+DeleteTestSuitesLibraryUrl);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteTestSuitesFromLibrary() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteTestSuitesFromLibrary() :: ERROR CODE : "+data.status);
	                    $scope.isAjaxFinished = "true";
	                    if (calledBy==="multiSelect"){
	                    	$scope.testcatalog.addVersion.actions.resetButtons();
	                    	$scope.testcatalog.addVersion.buttons.deleteSelectedSuites = {"label":"Deletion failed. Try again.","hide":false};
	                    }
	                });
	};
	
	this.commitVersion = function($scope,itemsToCommitted){
		var payLoad = {
				"testSuitesLibrary":itemsToCommitted,
				"siteName":$scope.siteName
				};
		var responsePromise = $http({
	        url: CommitVersionUrl,
	        method: "POST",
	        data: payLoad,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
						$scope.testcatalog.addVersion.buttons.disabledAll = "true";
						$scope.testcatalog.addVersion.buttons.deleteSelectedSuites = {"label":"...","hide":true};
						$scope.testcatalog.addVersion.buttons.commitVersion = {"label":"Version Committed Successfully.","hide":false};
						$scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "commitVersion() :: AJAX Request failed on URL : "+CommitVersionUrl);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "commitVersion() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "commitVersion() :: ERROR CODE : "+data.status);
	                    $scope.isAjaxFinished = "true";
	                    $scope.testcatalog.addVersion.actions.resetButtons();
	                    $scope.testcatalog.addVersion.buttons.commitVersion = {"label":"Commit failed. Try again.","hide":false};
	                });
	};
	
	this.copySingleTestSuiteFromStoredVersion = function($scope,payLoad){
		var responsePromise = $http({
	        url: CopySingleTestSuiteFromStoredVersionUrl,
	        method: "POST",
	        data: payLoad,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
						$scope.addTestSuiteFromStoredVersion.button.label = "Test Suite added successfully";
						$scope.isAjaxFinished = "true";
						$scope.addTestSuiteFromStoredVersion.button.disable = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "copySingleTestSuiteFromStoredVersion() :: AJAX Request failed on URL : "+CopySingleTestSuiteFromStoredVersionUrl);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "copySingleTestSuiteFromStoredVersion() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "copySingleTestSuiteFromStoredVersion() :: ERROR CODE : "+data.status);
	                    $scope.addTestSuiteFromStoredVersion.button.label = "Test Suite not added. Try again.";
						$scope.isAjaxFinished = "true";
						$scope.addTestSuiteFromStoredVersion.button.disable = "false";
	                });
	};
	
	this.createTestSuite = function($scope,payLoad){
		var responsePromise = $http({
	        url: CreateSingleTestSuiteViaWebsite,
	        method: "POST",
	        data: payLoad,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
						$scope.singleTestSuite.button.label = "Feature updated successfully";
						$scope.isAjaxFinished = "true";
						$scope.singleTestSuite.button.disable = "true";
						$scope.singleTestSuite.button.error = "false";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createTestSuite() :: AJAX Request failed on URL : "+CreateSingleTestSuiteViaWebsite);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createTestSuite() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createTestSuite() :: ERROR CODE : "+data.status);
	                    $scope.singleTestSuite.button.label = "Submit";
						$scope.isAjaxFinished = "true";
						$scope.singleTestSuite.button.error = "true";
						$scope.singleTestSuite.button.errorMessage = "Request Failed. Please try again after page refresh.";
						$scope.singleTestSuite.button.disable = "false";
	                });
	};
	
	this.createElementProperty = function($scope,payLoad){
		var responsePromise = $http({
	        url: CreateElementPropertyURL+"?suiteName="+$scope.createProperty.suiteName+"&siteName="+$scope.createProperty.siteName,
	        method: "POST",
	        data: payLoad,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
						$scope.singleElementProperty.button.label = "Element Property added successfully";
						$scope.isAjaxFinished = "true";
						$scope.singleElementProperty.button.disable = "true";
						$scope.singleElementProperty.button.error = "false";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createElementProperty() :: AJAX Request failed on URL : "+CreateElementPropertyURL);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createElementProperty() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createElementProperty() :: ERROR CODE : "+data.status);
	                    $scope.singleElementProperty.button.label = "Submit";
						$scope.isAjaxFinished = "true";
						$scope.singleElementProperty.button.error = "true";
						$scope.singleElementProperty.button.errorMessage = "Request Failed. Please try again after page refresh.";
						$scope.singleElementProperty.button.disable = "false";
	                });
	};
	
	this.deleteElementProperty = function($scope,payLoad){
		var responsePromise = $http({
	        url: DeleteElementPropertyURL+"?suiteName="+$scope.testSuiteName+"&siteName="+$scope.siteName,
	        method: "POST",
	        data: payLoad,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
			serviceObject.getSingleTestSuiteFromLibrary($scope);
        });
		responsePromise.error(function(data, status, headers, config) {
            SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteElementProperty() :: AJAX Request failed on URL : "+DeleteElementPropertyURL);
            SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteElementProperty() :: ERROR MESSAGE : "+data.message);
            SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteElementProperty() :: ERROR CODE : "+data.status);
            $scope.isAjaxFinished = "true";
        });
	};

	
	this.createTestCase = function($scope,payLoad){
		var responsePromise = $http({
	        url: CreateSingleTestCaseViaWebsite+"?suiteName="+$scope.createTestCaseData.suiteName+"&siteName="+$scope.createTestCaseData.siteName,
	        method: "POST",
	        data: payLoad,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
						$scope.singleTestCase.button.label = "Scenario added successfully";
						$scope.isAjaxFinished = "true";
						$scope.singleTestCase.button.disable = "true";
						$scope.singleTestCase.button.error = "false";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createTestCase() :: AJAX Request failed on URL : "+CreateSingleTestCaseViaWebsite);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createTestCase() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createTestCase() :: ERROR CODE : "+data.status);
	                    $scope.singleTestCase.button.label = "Submit";
						$scope.isAjaxFinished = "true";
						$scope.singleTestCase.button.error = "true";
						$scope.singleTestCase.button.errorMessage = "Request Failed. Please try again after page refresh.";
						$scope.singleTestCase.button.disable = "false";
	                });
	};
	
	this.createTestStep = function($scope,payLoad){
		var responsePromise = $http({
	        url: CreateTestStepURL+"?suiteName="+$scope.createTestStepData.suiteName
	        	+"&siteName="+$scope.createTestStepData.siteName+"&testCaseId="+$scope.createTestStepData.completeTestCase.testCaseId,
	        method: "POST",
	        data: payLoad,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
						$scope.singleTestStep.button.label = "Test Step added successfully";
						$scope.isAjaxFinished = "true";
						$scope.singleTestStep.button.disable = "true";
						$scope.singleTestStep.button.error = "false";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createTestStep() :: AJAX Request failed on URL : "+CreateTestStepURL);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createTestStep() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createTestStep() :: ERROR CODE : "+data.status);
	                    $scope.singleTestStep.button.label = "Submit";
						$scope.isAjaxFinished = "true";
						$scope.singleTestStep.button.error = "true";
						$scope.singleTestStep.button.errorMessage = "Request Failed. Please try again after page refresh.";
						$scope.singleTestStep.button.disable = "false";
	                });
	};
	
	this.createTestCaseDataSchema = function($scope,payLoad){
		var responsePromise = $http({
	        url: CreateSingleTestCaseDataSchema+"?suiteName="+$scope.createTestCaseDataSchema.suiteName
	        	+"&siteName="+$scope.createTestCaseDataSchema.siteName+"&testCaseId="+$scope.createTestCaseDataSchema.testCaseInfo.testCaseId,
	        method: "POST",
	        data: payLoad,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
						$scope.singleTestCaseDataSchema.button.label = "Data Fields added successfully";
						$scope.isAjaxFinished = "true";
						$scope.singleTestCaseDataSchema.button.disable = "true";
						$scope.singleTestCaseDataSchema.button.error = "false";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createTestCaseDataSchema() :: AJAX Request failed on URL : "+CreateSingleTestCaseDataSchema);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createTestCaseDataSchema() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "createTestCaseDataSchema() :: ERROR CODE : "+data.status);
	                    $scope.singleTestCaseDataSchema.button.label = "Submit";
						$scope.isAjaxFinished = "true";
						$scope.singleTestCaseDataSchema.button.error = "true";
						$scope.singleTestCaseDataSchema.button.errorMessage = "Request Failed. Please try again after page refresh.";
						$scope.singleTestCaseDataSchema.button.disable = "false";
	                });
	};
	
	this.creatDataSet = function($scope,payLoad){
		var responsePromise = $http({
	        url: CreateSingleTestCaseDataSet+"?suiteName="+$scope.createDataSet.suiteName
	        	+"&siteName="+$scope.createDataSet.siteName+"&testCaseId="+$scope.createDataSet.completeTestCase.testCaseId,
	        method: "POST",
	        data: payLoad,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
						$scope.singleDataSet.button.label = "Data Set added successfully";
						$scope.isAjaxFinished = "true";
						$scope.singleDataSet.button.disable = "true";
						$scope.singleDataSet.button.error = "false";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "creatDataSet() :: AJAX Request failed on URL : "+CreateSingleTestCaseDataSet);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "creatDataSet() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "creatDataSet() :: ERROR CODE : "+data.status);
	                    $scope.singleDataSet.button.label = "Submit";
						$scope.isAjaxFinished = "true";
						$scope.singleDataSet.button.error = "true";
						$scope.singleDataSet.button.errorMessage = "Request Failed. Please try again after page refresh.";
						$scope.singleDataSet.button.disable = "false";
	                });
	}; 
	
	this.deleteTestCasesFromTestSuite = function($scope,payLoad){
		var responsePromise = $http({
	        url: DeleteTestCasesURL+"?suiteName="+$scope.testSuiteName+"&siteName="+$scope.siteName,
	        method: "POST",
	        data: payLoad,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
			serviceObject.getSingleTestSuiteFromLibrary($scope);
        });
		responsePromise.error(function(data, status, headers, config) {
            SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteTestCases() :: AJAX Request failed on URL : "+DeleteTestCasesURL);
            SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteTestCases() :: ERROR MESSAGE : "+data.message);
            SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteTestCases() :: ERROR CODE : "+data.status);
            $scope.isAjaxFinished = "true";
        });
	};
	
	this.deleteTestStep = function($scope,payLoad){
		var responsePromise = $http({
	        url: DeleteTestStepURL+"?suiteName="+$scope.testSuiteName+"&siteName="+$scope.siteName+"&testCaseId="+$scope.testCaseId,
	        method: "POST",
	        data: payLoad,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
			serviceObject.getSingleTestSuiteFromLibrary($scope);
        });
		responsePromise.error(function(data, status, headers, config) {
            SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteTestStep() :: AJAX Request failed on URL : "+DeleteTestStepURL);
            SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteTestStep() :: ERROR MESSAGE : "+data.message);
            SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteTestStep() :: ERROR CODE : "+data.status);
            $scope.isAjaxFinished = "true";
        });
	};
	
	this.deleteDataSetFromTestCase = function($scope,payLoad){
		var responsePromise = $http({
	        url: DeleteDataSetURL+"?suiteName="+$scope.testSuiteName+"&siteName="+$scope.siteName+"&testCaseId="+$scope.testCaseId,
	        method: "POST",
	        data: payLoad,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
			serviceObject.getSingleTestSuiteFromLibrary($scope);
        });
		responsePromise.error(function(data, status, headers, config) {
            SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteDataSetFromTestCase() :: AJAX Request failed on URL : "+DeleteDataSetURL);
            SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteDataSetFromTestCase() :: ERROR MESSAGE : "+data.message);
            SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "deleteDataSetFromTestCase() :: ERROR CODE : "+data.status);
            $scope.isAjaxFinished = "true";
        });
	};
	
	
	this.downloadTestSuite = function($scope,testSuiteToDownload){
		
		var responsePromise = $http({
	        url: DownloadTestSuiteUrl,
	        method: "POST",
	        data: testSuiteToDownload,
	        responseType: "arraybuffer",
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8",
	        	"byPassHttpProviderConfig":true
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
						$scope.downloadedFile = {
								"fileName":testSuiteToDownload.fileName,
								"data":data
						};
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "downloadTestSuite() :: AJAX Request failed on URL : "+DownloadTestSuiteUrl);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "downloadTestSuite() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "downloadTestSuite() :: ERROR CODE : "+data.status);
	                });
	};
	
	this.getVersionHistoryOfUserSite = function($scope, email, siteName, numberOfRecords){
		var urlToFetchVersionHistoryOfUserSite = CatalogVersionHistoryOfUserSiteUrl + "?siteName="+siteName+"&numberOfRecords="+numberOfRecords+"&email="+email;
    	var responsePromise = $http.get(urlToFetchVersionHistoryOfUserSite);
		responsePromise.success(function(data, status, headers, config) {
						if(data!=null && Object.prototype.toString.call(data) === '[object Array]'){
							for(var index=0;index<data.length;index++){
								$scope.graphData.datapoints.push({"x":data[index].versionNumber,
									"TOTAL_SUITES":data[index].totalNumberOfSuites,
									"TOTAL_TEST_CASES":data[index].totalNumberOfTestCases});

								$scope.tableData.push({"versionNumber":data[index].versionNumber,
														"totalNumberOfTestCases":data[index].totalNumberOfTestCases,
														"totalNumberOfSuites":data[index].totalNumberOfSuites,
														"createTime":data[index].createTime});	
							}
						} else {
							SUtils.warn(SUtils.appConfig.testcatalogLogPrefix + "getVersionHistoryOfUserSite() :: Response formate on URL : "+urlToFetchVersionHistoryOfUserSite+" is not correct. Its expecting an Array");
						}
						$scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "getVersionHistoryOfUserSite() :: AJAX Request failed on URL : "+urlToFetchVersionHistoryOfUserSite);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
    };
    
    this.getVersionDetailsOfUserSingleSite = function($scope, email, siteName, version){
		var urlToFetchVersionDetailsOfSite = CatalogVersionDetailsOfUserSingleSiteUrl + "?siteName="+siteName+"&versionNumber="+version+"&email="+email;
    	var responsePromise = $http.get(urlToFetchVersionDetailsOfSite, {cache: true});
		responsePromise.success(function(data, status, headers, config) {
						if(data!=null){
							$scope.serverData = data;
						} else {
							SUtils.warn(SUtils.appConfig.testcatalogLogPrefix + "getVersionDetailsOfUserSingleSite() :: Data is null from AJAX call to URL : "+urlToFetchVersionDetailsOfSite);
						}
						$scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.testcatalogLogPrefix + "getVersionDetailsOfUserSingleSite() :: AJAX Request failed on URL : "+urlToFetchVersionDetailsOfSite);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
    };

});

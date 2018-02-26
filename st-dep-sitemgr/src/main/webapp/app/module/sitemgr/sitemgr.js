var sitemgr = angular.module('sitemgr',['gridshore.c3js.chart','ngCookies']);

sitemgr.value("SitemgrMyProfileUrl","/user/getUserDetails.rest");
sitemgr.value("UpdateMySiteShareStatus","/userSite/saveMySitesStatus.rest");
sitemgr.value("MyActiveSitesUrl","/userSite/mysites.rest?status=ACTIVE");
sitemgr.value("SharedSitesUrl","/userSite/sharedSites.rest?status=ACTIVE");
sitemgr.value("SiteAddUrl","/userSite/add.rest");

sitemgr.service("sitemgrService", function (SitemgrMyProfileUrl, MyActiveSitesUrl, SharedSitesUrl, $http) {
	var serviceObject = this;
	
	this.getMyProfileData = function($scope){
    	var responsePromise = $http.get(SitemgrMyProfileUrl);
		responsePromise.success(function(data, status, headers, config) {
						if (data!=null){
		                    $scope.myProfileData = data;
						} else {
							SUtils.warn(SUtils.appConfig.qdosLogPrefix + "getMyProfileData() :: AJAX Response on URL : "+SitemgrMyProfileUrl + " is undefined");
						}
						$scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.qdosLogPrefix + "getMyProfileData() :: AJAX Request failed on URL : "+SitemgrMyProfileUrl);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
    };
    
	this.getMyActiveSites = function($scope){
    	var responsePromise = $http.get(MyActiveSitesUrl);
		responsePromise.success(function(data, status, headers, config) {
						if (data!=null){
		                    $scope.myActiveSites = data;
		                    serviceObject.getMyProfileData($scope);
		                    for (var index=0;index<$scope.myActiveSites.length;index++){
		                    	var singleSite = $scope.myActiveSites[index];
		                    	if (singleSite.status==="ACTIVE"){
		                    		singleSite.isDeleted = false;
		                    	} else {
		                    		singleSite.isDeleted = true;
		                    	}

		                    	if (singleSite.shareStatus == "public"){
		                    		singleSite.isPublicallyVisible = true;
		                    		singleSite.isCompanyWideVisible = true;
		                    	} else if (singleSite.shareStatus == "company"){
		                    		singleSite.isPublicallyVisible = false;
		                    		singleSite.isCompanyWideVisible = true;
		                    	} else {
		                    		singleSite.isPublicallyVisible = false;
		                    		singleSite.isCompanyWideVisible = false;
		                    	}
		                    }
		                } else {
		                	SUtils.warn(SUtils.appConfig.sitemgrLogPrefix + "getMyActiveSites() :: AJAX Response on URL : "+MyActiveSitesUrl + " is undefined");
		                }
		                $scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.sitemgrLogPrefix + "getMyActiveSites() :: AJAX Request failed on URL : "+MyActiveSitesUrl);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });	
    };
	this.getSharedSites = function($scope){
    	var responsePromise = $http.get(SharedSitesUrl);
		responsePromise.success(function(data, status, headers, config) {
						if (data!=null){
		                    $scope.sharedSites = data;
		                } else {
		                	SUtils.warn(SUtils.appConfig.sitemgrLogPrefix + "getSharedSites() :: AJAX Response of URL : "+SharedSitesUrl+ " is undefined");
		                }
		                $scope.isAjaxFinished = "true";
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.sitemgrLogPrefix + "getSharedSites() :: AJAX Request failed on URL : "+SharedSitesUrl);
	                    $scope.isAjaxFinished = "true";
	                    return {};
	                });
    };
});

sitemgr.config(['$routeProvider', function($routeProvider){
	$routeProvider.
		when('/sitemgr/emailSubscription/view', {
			templateUrl: 'templates/module/sitemgr/view/email-subscriptions.html',
			controller: 'emailSubscriptionCtrl'
		}).
		when('/sitemgr/add', {
			templateUrl: 'templates/module/sitemgr/view/addSite.html',
			controller: 'addSiteCtrl'
		}).
		when('/sitemgr', {
			templateUrl: 'templates/module/sitemgr/view/sitemgr.html',
			controller: 'siteManagerCtrl'
		}).
		otherwise({
	        redirectTo: '/'
	    });
}]);

sitemgr.controller("siteManagerCtrl", function($scope,$cookies,$http,$log,sitemgrService,UpdateMySiteShareStatus) {
	$scope.isAjaxFinished = "false";
	$scope.myEmail = $cookies.email;
	$scope.sharedSites = {};
	$scope.myProfileData = {};
	$scope.myActiveSites = {};
	$scope.mySitesShareStatus = {
			submitStatus : "",
			submitMessage : "",
			submitButtonDisabled : null,
			submitButtonText : "Submit"
	};
	sitemgrService.getSharedSites($scope);
	sitemgrService.getMyActiveSites($scope);

	$scope.$watch('myProfileData', function(myProfileData) {
		if (myProfileData.email != null){
			for (var index=0;index<$scope.sharedSites.length;index++){
				var singleSharedSite = $scope.sharedSites[index];
				for (var subIndex=0;subIndex<$scope.myProfileData.sharedSubscribedSites.length;subIndex++){
					var siteVsEmailsObj = $scope.myProfileData.sharedSubscribedSites[subIndex];
					if (siteVsEmailsObj.siteName === singleSharedSite.siteName && siteVsEmailsObj.email === singleSharedSite.email){
						singleSharedSite.isSelected = true;
						break;
					}
				}
			}
	   }
   	});

   	$scope.$watch('myActiveSites', function(myActiveSites, oldActiveSites) {
		for (var index=0;index<$scope.myActiveSites.length;index++){
        	var singleSite = $scope.myActiveSites[index];
        	var oldSingleSite = oldActiveSites[index];
        	if (oldSingleSite!=null){
	        	if (singleSite.isCompanyWideVisible == false && oldSingleSite.isCompanyWideVisible == true && singleSite.isPublicallyVisible == true){
	        		singleSite.isPublicallyVisible = false;
	        	} else if (singleSite.isPublicallyVisible == true && oldSingleSite.isPublicallyVisible == false && singleSite.isCompanyWideVisible == false){
	        		singleSite.isCompanyWideVisible = true;
	        	}
        	}
        }
   	}, true);

	$scope.saveSharedSiteForDashboard = function(){
		console.log($scope.sharedSites);
	};

	$scope.mySitesShareStatus.saveMySitesPreferences = function(){
		$scope.mySitesShareStatus.submitButtonText = "Please wait...";
		$scope.mySitesShareStatus.submitButtonDisabled = "true";
		var postData = {};
		for(var index=0;index<$scope.myActiveSites.length;index++){
			postData['email-'+index] = $scope.myActiveSites[index].email;
			postData['siteName-'+index] = $scope.myActiveSites[index].siteName;
			if ($scope.myActiveSites[index].isCompanyWideVisible){
				postData['shareStatus-'+index] = "company";
			} 
			if ($scope.myActiveSites[index].isPublicallyVisible){
				postData['shareStatus-'+index] = "public";
			} 
			if (!$scope.myActiveSites[index].isPublicallyVisible && !$scope.myActiveSites[index].isCompanyWideVisible){
				postData['shareStatus-'+index] = "private";
			}
			if ($scope.myActiveSites[index].isDeleted){
				postData['status-'+index] = "DELETED";
			} else {
				postData['status-'+index] = "ACTIVE";
			}
		}
		
		var responsePromise = $http({
	        url: UpdateMySiteShareStatus,
	        method: "POST",
	        data: postData
	    });
		responsePromise.success(function(data, status, headers, config) {
			if (data!=null){
				$scope.mySitesShareStatus.submitStatus = "alert-success";
				$scope.mySitesShareStatus.submitMessage = "Sites updated successfully.";
				$scope.mySitesShareStatus.submitButtonText = "Done.";
				$scope.mySitesShareStatus.submitButtonDisabled = "true";
			} else {
				$scope.mySitesShareStatus.submitStatus = "alert-danger";
				$scope.mySitesShareStatus.submitMessage = "Update Failed! Please try again after reloading the page.";
				$scope.mySitesShareStatus.submitButtonText = "Submit";
				$scope.mySitesShareStatus.submitButtonDisabled = null;
				$log.warn(SUtils.appConfig.sitemgrLogPrefix + "sitemgr.controller.siteManagerCtrl() :: AJAX Response on URL : "+UpdateMySiteShareStatus + " is undefined");
			}
        });
		responsePromise.error(function(data, status, headers, config) {
			$scope.mySitesShareStatus.submitStatus = "alert-danger";
			if (data!=null && data.message!=null){
				$scope.mySitesShareStatus.submitMessage = data.message;
			} else {
				$scope.mySitesShareStatus.submitMessage = "Update Failed! Please try again later.";
			}
			$scope.mySitesShareStatus.submitButtonText = "Submit";
			$scope.mySitesShareStatus.submitButtonDisabled = null;
			$log.error(SUtils.appConfig.sitemgrLogPrefix + "sitemgr.controller.mySitesStatus() :: AJAX Request failed on URL : "+UpdateMySiteShareStatus); 
        });
	};
});

sitemgr.controller("addSiteCtrl", function($scope,$http,$filter,$log,SiteAddUrl) {
	$scope.addCatalog = {
			description:"",
			siteName:"",
			shareStatus:"",
			submitStatus : "",
			submitMessage : "",
			submitButtonDisabled : null,
			submitButtonText : "Submit",
			submit : function(){
				if (checkMandatoryFieldsPassed($scope.addCatalog)){
					$scope.addCatalog.submitButtonText = "Please wait...";
					$scope.addCatalog.submitButtonDisabled = "true";
					
					var responsePromise = $http({
				        url: SiteAddUrl,
				        method: "POST",
				        data: {
				        	'description' : $scope.addCatalog.description,
				        	'siteName':$scope.addCatalog.siteName,
				        	'shareStatus':$scope.addCatalog.shareStatus
				        }
				    });
					responsePromise.success(function(data, status, headers, config) {
						if (data!=null){
							$scope.addCatalog.submitStatus = "alert-success";
							$scope.addCatalog.submitMessage = "Site added successfully.";
							$scope.addCatalog.submitButtonText = "Done.";
							$scope.addCatalog.submitButtonDisabled = "true";
						} else {
							$scope.addCatalog.submitStatus = "alert-danger";
							$scope.addCatalog.submitMessage = "Update Failed! Please try again after reloading the page.";
							$scope.addCatalog.submitButtonText = "Submit";
							$scope.addCatalog.submitButtonDisabled = null;
							$log.warn(SUtils.appConfig.sitemgrLogPrefix + "sitemgr.controller.addSiteCtrl() :: AJAX Response on URL : "+SiteAddUrl + " is undefined");
						}
		            });
					responsePromise.error(function(data, status, headers, config) {
						$scope.addCatalog.submitStatus = "alert-danger";
						if (data!=null && data.message!=null){
							$scope.addCatalog.submitMessage = data.message;
						} else {
							$scope.addCatalog.submitMessage = "Update Failed! Please try again later.";
						}
						$scope.addCatalog.submitButtonText = "Submit";
						$scope.addCatalog.submitButtonDisabled = null;
						$log.error(SUtils.appConfig.sitemgrLogPrefix + "sitemgr.controller.addSiteCtrl() :: AJAX Request failed on URL : "+SiteAddUrl); 
		            });
					
				} else {
					//Mandatory parameters missing.
					$scope.addCatalog.submitStatus = "alert-danger";
					$scope.addCatalog.submitMessage = "Mandatory Parameters are missing.";
					$scope.addCatalog.submitButtonText = "Submit";
					$scope.addCatalog.submitButtonDisabled = null;
				}
			}
	};
	var checkMandatoryFieldsPassed = function(addCatalog){
		if (addCatalog.shareStatus!=null && addCatalog.shareStatus!='' &&
				addCatalog.siteName!=null && addCatalog.siteName!='' &&
				addCatalog.description!=null && addCatalog.description!=''){
			return true;
		}
	};
});

sitemgr.controller("emailSubscriptionCtrl", function($scope,$cookies,$http,sitemgrService) {
	$scope.isAjaxFinished = "false";
	$scope.myEmail = $cookies.email;
	$scope.myActiveSites = {};
	$scope.myProfileData = {};

	sitemgrService.getMyActiveSites($scope);

	$scope.$watch('myProfileData', function(myProfileData) {
		if (myProfileData.email != null){
			for (var index=0;index<$scope.myActiveSites.length;index++){
				var activeSiteObj = $scope.myActiveSites[index];
				for (var subIndex=0;subIndex<$scope.myProfileData.emailSubscriptions.length;subIndex++){
					var siteVsEmailsObj = $scope.myProfileData.emailSubscriptions[subIndex];
					if (siteVsEmailsObj[activeSiteObj.siteName] !=null){
						activeSiteObj.emails = siteVsEmailsObj[activeSiteObj.siteName];
						break;
					}
				}
			}
	   }
   	});

	$scope.saveSitePreferences = function(){
		console.log($scope.myActiveSites);
	};
});



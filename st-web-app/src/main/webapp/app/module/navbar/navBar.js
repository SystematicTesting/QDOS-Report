var navBar = angular.module('navBar',['ngRoute','ui.bootstrap','ngCookies']);

navBar.value("MyProfileUrl","/user/getUserDetails.rest");
navBar.value("MyProfileUpdateUrl","/user/updateMyProfile.rest");
navBar.value("PasswordUpdateUrl","/user/changePassword.rest");
navBar.value("LicenseUpdateUrl","/user/updateLicense.rest");
navBar.value("DownloadQDOSFilesUrl","/files/downloadQDOSFiles.rest?fileName=");

navBar.service("navBarService", function (MyProfileUrl, DownloadQDOSFilesUrl, $http) {
	var serviceObject = this;

    this.getMyProfileData = function($scope){
    	var responsePromise = $http.get(MyProfileUrl);
		responsePromise.success(function(data, status, headers, config) {
	                    $scope.myProfileData = data;
	                });
		responsePromise.error(function(data, status, headers, config) {
			SUtils.error(SUtils.appConfig.mainAppLogPrefix + "getMyProfileData() :: AJAX Request failed on URL : "+MyProfileUrl);
            return {};
        });
    };
    
    this.downloadQdosClientJar = function($scope){
    	var requestData = {
	    		"fileName" : "QDOS.jar"
	    	};
    	var responsePromise = $http({
	        url: DownloadQDOSFilesUrl+requestData.fileName,
	        method: "GET",
	        responseType: "arraybuffer",
	        data: requestData,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8"
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
						$scope.downloadedQdosClientJarFile = {
								"fileName":requestData.fileName,
								"data":data
						};
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.mainAppLogPrefix + "downloadQdosClientJar() :: AJAX Request failed on URL : "+DownloadQDOSFilesUrl+requestData.fileName);
	                    SUtils.error(SUtils.appConfig.mainAppLogPrefix + "downloadQdosClientJar() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.mainAppLogPrefix + "downloadQdosClientJar() :: ERROR CODE : "+data.status);
	                });
    };
    
    this.downloadSampleTestSuiteFile = function($scope){
    	var requestData = {
        		"fileName" : "SampleTestSuite.xlsx"
        	};
    	var responsePromise = $http({
	        url: DownloadQDOSFilesUrl+requestData.fileName,
	        method: "GET",
	        responseType: "arraybuffer",
	        data: requestData,
	        headers:{
	        	"Content-Type":"application/json;charset=utf-8"
	        }
	    });
		
		responsePromise.success(function(data, status, headers, config) {
						$scope.downloadedSampleTestSuiteFile = {
								"fileName":requestData.fileName,
								"data":data
						};
	                });
		responsePromise.error(function(data, status, headers, config) {
	                    SUtils.error(SUtils.appConfig.mainAppLogPrefix + "downloadSampleTestSuiteFile() :: AJAX Request failed on URL : "+DownloadQDOSFilesUrl+requestData.fileName);
	                    SUtils.error(SUtils.appConfig.mainAppLogPrefix + "downloadSampleTestSuiteFile() :: ERROR MESSAGE : "+data.message);
	                    SUtils.error(SUtils.appConfig.mainAppLogPrefix + "downloadSampleTestSuiteFile() :: ERROR CODE : "+data.status);
	                });
    };
    
});

navBar.config(['$routeProvider', function($routeProvider){
	$routeProvider.
		when('/ChangePassword', {
			templateUrl: 'templates/module/navbar/view/ChangePassword.html',
			controller: 'passwordMgrCtrl'
		}).
		when('/MyProfile', {
			templateUrl: 'templates/module/navbar/view/MyProfile.html',
			controller: 'myProfileCtrl'
		}).
		when('/UpdateLicense', {
			templateUrl: 'templates/module/navbar/view/UpdateLicense.html',
			controller: 'updateLicenseCtrl'
		}).
	    otherwise({
	        redirectTo: '/'
	    });
}]);


navBar.controller("navBarMainCtrl", ['$scope','$rootScope','$modal','$cookies','$cookieStore','$window', '$filter','navBarService',function($scope, $rootScope, $modal, $cookies, $cookieStore,$window, $filter,navBarService) {
	
	$scope.isEnterpriseLicense = $filter('isEnterpriseLicense')($cookies);
	$rootScope.sidebarRootLevelClass = 0;
    $scope.toggleSideBar = function(){
		$rootScope.sidebarRootLevelClass = !$rootScope.sidebarRootLevelClass;
	};
	$scope.logout = function(){
		angular.forEach($cookies, function (v, k) {
			$cookieStore.remove(k);
		});
		$window.location.reload();
	};

	$scope.showAPIKey = function(){
	    var modalInstance = $modal.open({
	      animation: true,
	      templateUrl: 'templates/module/navbar/view/APIKey.html',
	      controller: 'aPIKeyCtrl',
	      keyboard:true
	    });
	};
	
	$scope.downloadedQdosClientJarFile = {};
	$scope.downloadQDOSClient = function(){
		console.log("Download Button clicked.");
		navBarService.downloadQdosClientJar($scope);
	};
	
	$scope.$watch('downloadedQdosClientJarFile', function(downloadedQdosClientJarFile, olddownloadedQdosClientJarFile) {
		if (downloadedQdosClientJarFile.fileName!=null){
			var downloadLink = document.createElement("a");
			document.body.appendChild(downloadLink);
			downloadLink.style = "display: none";
			var fName = $scope.downloadedQdosClientJarFile.fileName;
			var file = new Blob([$scope.downloadedQdosClientJarFile.data],{type: 'application/octet-stream'});
			var fileURL = (window.URL || window.webkitURL).createObjectURL(file);
			downloadLink.href = fileURL;
			downloadLink.download = fName;
			downloadLink.click();
			downloadLink.parentNode.removeChild(downloadLink);
			$scope.downloadedQdosClientJarFile={};
		}
	}, true);
	
	$scope.downloadedSampleTestSuiteFile = {};
	$scope.downloadSampleTestSuite = function(){
		console.log("Download Sample Test Suite Clicked");
		navBarService.downloadSampleTestSuiteFile($scope);
	};
	
	$scope.$watch('downloadedSampleTestSuiteFile', function(downloadedSampleTestSuiteFile, olddownloadedSampleTestSuiteFile) {
		if (downloadedSampleTestSuiteFile.fileName!=null){
			var downloadLink = document.createElement("a");
			document.body.appendChild(downloadLink);
			downloadLink.style = "display: none";
			var fName = $scope.downloadedSampleTestSuiteFile.fileName;
			var file = new Blob([$scope.downloadedSampleTestSuiteFile.data],{type: 'application/octet-stream'});
			var fileURL = (window.URL || window.webkitURL).createObjectURL(file);
			downloadLink.href = fileURL;
			downloadLink.download = fName;
			downloadLink.click();
			downloadLink.parentNode.removeChild(downloadLink);
			$scope.downloadedSampleTestSuiteFile={};
		}
	}, true);

}]);

navBar.controller("passwordMgrCtrl", function($scope,$http,navBarService,PasswordUpdateUrl) {
	$scope.passwordMgr = {
			currentpassword:"",
			newpassword:"",
			confirmpassword:"",
			submitStatus : "",
			submitMessage : "",
			submitButtonDisabled : null,
			submitButtonText : "Submit",
			submit : function(){
				if (checkMandatoryFieldsPassed($scope.passwordMgr)){
					$scope.passwordMgr.submitButtonText = "Please wait...";
					$scope.passwordMgr.submitButtonDisabled = "true";
					if ($scope.passwordMgr.newpassword === $scope.passwordMgr.confirmpassword){
						var responsePromise = $http({
					        url: PasswordUpdateUrl,
					        method: "POST",
					        data: { 
					        	'oldPassword' : $scope.passwordMgr.currentpassword,
					        	'newPassword' : $scope.passwordMgr.newpassword
					        }
					    });
						responsePromise.success(function(data, status, headers, config) {
							if (data!=null){
								$scope.passwordMgr.submitStatus = "alert-success";
								$scope.passwordMgr.submitMessage = "Password updated successfully.";
								$scope.passwordMgr.submitButtonText = "Done.";
								$scope.passwordMgr.submitButtonDisabled = "true";
							} else {
								$scope.passwordMgr.submitStatus = "alert-danger";
								$scope.passwordMgr.submitMessage = "Update Failed! Please try again after reloading the page.";
								$scope.passwordMgr.submitButtonText = "Submit";
								$scope.passwordMgr.submitButtonDisabled = null;
								$log.warn(SUtils.appConfig.mainAppLogPrefix + "navBar.controller.passwordMgrCtrl() :: AJAX Response on URL : "+PasswordUpdateUrl + " is undefined");
							}
			            });
						responsePromise.error(function(data, status, headers, config) {
							$scope.passwordMgr.submitStatus = "alert-danger";
							if (data!=null && data.message!=null){
								$scope.passwordMgr.submitMessage = data.message;
							} else {
								$scope.passwordMgr.submitMessage = "Update Failed! Please try again later.";
							}
							$scope.passwordMgr.submitButtonText = "Submit";
							$scope.passwordMgr.submitButtonDisabled = null;
							$log.error(SUtils.appConfig.mainAppLogPrefix + "navBar.controller.passwordMgrCtrl() :: AJAX Request failed on URL : "+PasswordUpdateUrl); 
			            });
					} else {
						$scope.passwordMgr.submitStatus = "alert-danger";
						$scope.passwordMgr.submitMessage = "New Password does not match with New Password.";
						$scope.passwordMgr.submitButtonText = "Submit";
						$scope.passwordMgr.submitButtonDisabled = null;
					}
				} else {
					//Mandatory parameters missing.
					$scope.passwordMgr.submitStatus = "alert-danger";
					$scope.passwordMgr.submitMessage = "Mandatory Parameters are missing.";
					$scope.passwordMgr.submitButtonText = "Submit";
					$scope.passwordMgr.submitButtonDisabled = null;
				}
			}
	};
	var checkMandatoryFieldsPassed = function(passwordMgr){
		if (passwordMgr.currentpassword!=null && passwordMgr.currentpassword!='' &&
				passwordMgr.newpassword!=null && passwordMgr.newpassword!='' &&
				passwordMgr.confirmpassword!=null && passwordMgr.confirmpassword!=''){
			return true;
		}
	};
});

navBar.controller("aPIKeyCtrl", function($scope,$modalInstance, $http,navBarService,$cookies){
	$scope.apiKey = $cookies.defaultAPIkey;
	$scope.close = function () {
	    $modalInstance.dismiss('close');
	};
});

navBar.controller("myProfileCtrl", function($scope,$http,navBarService,MyProfileUpdateUrl) {
	$scope.myProfileData = {};
	$scope.myProfileForm = {
			submitStatus : "",
			submitMessage : "",
			submitButtonText : "Submit",
			submitButtonDisabled : null
	};
	navBarService.getMyProfileData($scope);
	$scope.myProfileForm.submit = function(){
		if (checkMandatoryFieldsPassed($scope.myProfileData)){
			$scope.myProfileForm.submitButtonText = "Please wait...";
			$scope.myProfileForm.submitButtonDisabled = "true";
			var responsePromise = $http({
		        url: MyProfileUpdateUrl,
		        method: "POST",
		        data: { 
		        	'firstname' : $scope.myProfileData.firstname,
		        	'lastname' : $scope.myProfileData.lastname,
		        	'address' : $scope.myProfileData.address,
		        	'city' : $scope.myProfileData.city,
		        	'country' : $scope.myProfileData.country,
		        	'companyname' : $scope.myProfileData.companyname,
		        	'postcode' : $scope.myProfileData.postcode
		        }
		    });
			responsePromise.success(function(data, status, headers, config) {
				if (data!=null){
					$scope.myProfileForm.submitStatus = "alert-success";
					$scope.myProfileForm.submitMessage = "User updated successfully.";
					$scope.myProfileForm.submitButtonText = "Done.";
					$scope.myProfileForm.submitButtonDisabled = "true";
				} else {
					$scope.myProfileForm.submitStatus = "alert-danger";
					$scope.myProfileForm.submitMessage = "Update Failed! Please try again after reloading the page.";
					$scope.myProfileForm.submitButtonText = "Submit";
					$scope.myProfileForm.submitButtonDisabled = null;
					$log.warn(SUtils.appConfig.mainAppLogPrefix + "navBar.controller.myProfileCtrl() :: AJAX Response on URL : "+MyProfileUpdateUrl + " is undefined");
				}
            });
			responsePromise.error(function(data, status, headers, config) {
				$scope.myProfileForm.submitStatus = "alert-danger";
				$scope.myProfileForm.submitMessage = "Update Failed! Please try again later.";
				$scope.myProfileForm.submitButtonText = "Submit";
				$scope.myProfileForm.submitButtonDisabled = null;
				$log.error(SUtils.appConfig.mainAppLogPrefix + "navBar.controller.myProfileCtrl() :: AJAX Request failed on URL : "+MyProfileUpdateUrl); 
            });
			
		} else {
			//Mandatory parameters missing.
			$scope.myProfileForm.submitStatus = "alert-danger";
			$scope.myProfileForm.submitMessage = "Mandatory Parameters are missing.";
			$scope.myProfileForm.submitButtonText = "Submit";
			$scope.myProfileForm.submitButtonDisabled = null;
		}
	};
	var checkMandatoryFieldsPassed = function(myProfileData){
		if (myProfileData.firstname!=null && myProfileData.firstname!='' &&
				myProfileData.lastname!=null && myProfileData.lastname!='' &&
				myProfileData.city!=null && myProfileData.city!='' &&
				myProfileData.country!=null && myProfileData.country!='' &&
				myProfileData.companyname!=null && myProfileData.companyname!=''){
			return true;
		}
	}
	
});


navBar.controller("updateLicenseCtrl", function($scope,$http,$filter,$log,navBarService,LicenseUpdateUrl) {
	$scope.updateLicense = {
			featureList:"",
			licenseKey:"",
			expiryDate:"",
			submitStatus : "",
			submitMessage : "",
			submitButtonDisabled : null,
			submitButtonText : "Submit",
			submit : function(){
				if (checkMandatoryFieldsPassed($scope.updateLicense)){
					$scope.updateLicense.submitButtonText = "Please wait...";
					$scope.updateLicense.submitButtonDisabled = "true";
					if ($filter('validateDate')($scope.updateLicense.expiryDate)){
						var responsePromise = $http({
					        url: LicenseUpdateUrl,
					        method: "POST",
					        data: { 
					        	'featureList' : $scope.updateLicense.featureList,
					        	'licenseKey' : $scope.updateLicense.licenseKey,
					        	'expiryDateString' : $scope.updateLicense.expiryDate
					        }
					    });
						responsePromise.success(function(data, status, headers, config) {
							if (data!=null){
								$scope.updateLicense.submitStatus = "alert-success";
								$scope.updateLicense.submitMessage = "License updated successfully.";
								$scope.updateLicense.submitButtonText = "Done.";
								$scope.updateLicense.submitButtonDisabled = "true";
							} else {
								$scope.updateLicense.submitStatus = "alert-danger";
								$scope.updateLicense.submitMessage = "Update Failed! Please try again after reloading the page.";
								$scope.updateLicense.submitButtonText = "Submit";
								$scope.updateLicense.submitButtonDisabled = null;
								$log.warn(SUtils.appConfig.mainAppLogPrefix + "navBar.controller.updateLicenseCtrl() :: AJAX Response on URL : "+LicenseUpdateUrl + " is undefined");
							}
			            });
						responsePromise.error(function(data, status, headers, config) {
							$scope.updateLicense.submitStatus = "alert-danger";
							if (data!=null && data.message!=null){
								$scope.updateLicense.submitMessage = data.message;
							} else {
								$scope.updateLicense.submitMessage = "Update Failed! Please try again later.";
							}
							$scope.updateLicense.submitButtonText = "Submit";
							$scope.updateLicense.submitButtonDisabled = null;
							$log.error(SUtils.appConfig.mainAppLogPrefix + "navBar.controller.updateLicenseCtrl() :: AJAX Request failed on URL : "+LicenseUpdateUrl); 
			            });
					} else {
						$scope.updateLicense.submitStatus = "alert-danger";
						$scope.updateLicense.submitMessage = "Expiry Date is not valid.";
						$scope.updateLicense.submitButtonText = "Submit";
						$scope.updateLicense.submitButtonDisabled = null;
					}
				} else {
					//Mandatory parameters missing.
					$scope.updateLicense.submitStatus = "alert-danger";
					$scope.updateLicense.submitMessage = "Mandatory Parameters are missing.";
					$scope.updateLicense.submitButtonText = "Submit";
					$scope.updateLicense.submitButtonDisabled = null;
				}
			}
	};
	var checkMandatoryFieldsPassed = function(updateLicense){
		if (updateLicense.featureList!=null && updateLicense.featureList!='' &&
				updateLicense.licenseKey!=null && updateLicense.licenseKey!='' &&
				updateLicense.expiryDate!=null && updateLicense.expiryDate!=''){
			return true;
		}
	};
});

mainApp.controller("userHomePageCtrl", ['$scope','$modal','$cookies','$cookieStore','$filter',function($scope,$modal,$cookies,$cookieStore,$filter) {

}]);

mainApp.controller("siteMainCtrl", ['$scope','$modal','$cookies','$cookieStore','$filter',function($scope,$modal,$cookies,$cookieStore,$filter) {

	//Session Detection Logic
	if ($cookies.activeAPIkey!=null && $cookies.defaultAPIkey!=null && $cookies.accountId!=null && $cookies.sessionId!=null){
		$scope.isSessionValid = true;
	} else {
		//Account activation logic
		var email = $filter('getRequestParameter')('email');
		var activeAPIkey = $filter('getRequestParameter')('activeAPIkey');
		if (email!=null && activeAPIkey!=null){
			$scope.isSessionValid = false;
			var accountActData = {
				email : $filter('decodeURI')(email),
				activeAPIkey : activeAPIkey
			};
			$modal.open({
		      animation: true,
		      templateUrl: 'templates/views/accountActivation.html',
		      controller: 'accountActivationCtrl',
		      keyboard:false,
		      backdrop : 'static',
		      size:'md',
		      resolve: {
			    accountActData: function () {
			      return accountActData;
			    }
			  }
		    });
		} else {
			$scope.isSessionValid = false;
			angular.forEach($cookies, function (v, k) {
				$cookieStore.remove(k);
			});
			$modal.open({
		      animation: true,
		      templateUrl: 'templates/views/login.html',
		      controller: 'loginCtrl',
		      keyboard:false,
		      backdrop : 'static',
		      size:'md'
		    });
		}
		
	}
}]);

mainApp.controller("accountActivationCtrl", ['$scope','$modalInstance','$modal','$cookies','$cookieStore','$window','$http','$filter','$log','AccountActivationURL','accountActData',function($scope,$modalInstance, $modal,$cookies,$cookieStore,$window,$http,$filter,$log,AccountActivationURL,accountActData) {
	$scope.accountAct = {
		actStatus:"alert-warning",
		actMessage : "Processing your request now...",
		loginButtonDisabled : "true",
		login : function(){
			$window.location.href="/";
		}
	};
	var responsePromise = $http({
        url: AccountActivationURL,
        method: "POST",
        data: accountActData
    });
	responsePromise.success(function(data, status, headers, config) {
		if (data!=null){
			$scope.accountAct.actStatus = "alert-success";
			$scope.accountAct.actMessage = "Account activated successfully. You can login now.";
			$scope.accountAct.loginButtonDisabled = null;
		} else {
			$scope.accountAct.actStatus = "alert-danger";
			$scope.accountAct.actMessage = "Account activation failed.";
			$scope.accountAct.loginButtonDisabled = "true";
			$log.warn(SUtils.appConfig.mainAppLogPrefix + "mainApp.controller.accountActivationCtrl() :: AJAX Response on URL : "+AccountActivationURL + " is undefined");
		}
    });
	responsePromise.error(function(data, status, headers, config) {
		$scope.accountAct.actStatus = "alert-danger";
		if (data!=null && data.message!=null){
			$scope.accountAct.actMessage = data.message;
		} else {
			$scope.accountAct.actMessage = "Account activation failed.";
		}
		$scope.accountAct.loginButtonDisabled = "true";
		$log.error(SUtils.appConfig.mainAppLogPrefix + "mainApp.controller.accountActivationCtrl() :: AJAX Request failed on URL : "+AccountActivationURL); 
    });
}]);

mainApp.controller("loginCtrl", ['$scope','$modalInstance','$modal','$cookies','$cookieStore','$window','$http','$filter','$log','LoginURL',function($scope,$modalInstance, $modal,$cookies,$cookieStore,$window,$http,$filter,$log,LoginURL) {
	$scope.loginForm = {
		email : null,
		password : null,
		loginButtonDisabled : null,
		loginButtonText : "Login",
		registerButtonText : "Register",
		loginStatus : "displayNone",
		loginMessage : "",
		login : function(){
			$scope.loginForm.loginButtonDisabled = "true";
			$scope.loginForm.loginButtonText = "Please wait...";
			var passwordWithoutSpace = $filter('nospace')($scope.loginForm.password);
			$log.debug($scope.loginForm);
			$log.debug(($scope.loginForm.email!=null && $scope.loginForm.email!='' &&$scope.loginForm.password!=null && passwordWithoutSpace!=''));
			
			if ($scope.loginForm.email!=null && $scope.loginForm.email!='' && $scope.loginForm.password!=null && passwordWithoutSpace!=''){
				if ($filter('validateEmail')($scope.loginForm.email)){
					var responsePromise = $http({
				        url: LoginURL,
				        method: "POST",
				        data: { 
				        	'email' : $scope.loginForm.email,
				        	'password' : $scope.loginForm.password
				        }
				    });
					responsePromise.success(function(data, status, headers, config) {
						if (data!=null){
							$scope.loginForm.loginStatus = "alert-success";
							$scope.loginForm.loginMessage = "Redirecting now...";
							$scope.loginForm.loginButtonText = "...";
							$scope.loginForm.loginButtonDisabled = "true";
		                    //$cookieStore.put("jsActiveApiKey", data.apiKey);
		                    $window.location.reload();
						} else {
							$scope.loginForm.loginStatus = "alert-danger";
							$scope.loginForm.loginMessage = "Login Failed! Please try again after reloading the page.";
							$scope.loginForm.loginButtonText = "Login";
							$scope.loginForm.loginButtonDisabled = null;
							$log.warn(SUtils.appConfig.mainAppLogPrefix + "mainApp.controller.loginCtrl() :: AJAX Response on URL : "+LoginURL + " is undefined");
						}
	                });
					responsePromise.error(function(data, status, headers, config) {
						$scope.loginForm.loginStatus = "alert-danger";
						if (data!=null && data.message!=null){
							$scope.loginForm.loginMessage = data.message;
						} else {
							$scope.loginForm.loginMessage = "Login Failed! Please try again.";
						}
						$scope.loginForm.loginButtonText = "Login";
						$scope.loginForm.loginButtonDisabled = null;
						$log.error(SUtils.appConfig.mainAppLogPrefix + "mainApp.controller.loginCtrl() :: AJAX Request failed on URL : "+LoginURL); 
	                });
				} else {
					//Email format is not valid.
					$scope.loginForm.loginStatus = "alert-warning";
					$scope.loginForm.loginMessage = "Email address is not valid.";
					$scope.loginForm.loginButtonText = "Login";
					$scope.loginForm.loginButtonDisabled = null;
				}
			} else {
				//Mandatory parameters missing.
				$scope.loginForm.loginStatus = "alert-danger";
				$scope.loginForm.loginMessage = "Mandatory Parameters are missing.";
				$scope.loginForm.loginButtonText = "Login";
				$scope.loginForm.loginButtonDisabled = null;
			}
		},
		register : function(){
			$modalInstance.dismiss('close');
			$modal.open({
		      animation: true,
		      templateUrl: 'templates/views/register.html',
		      controller: 'registerCtrl',
		      keyboard:false,
		      backdrop : 'static',
		      size:'md'
		    });
		},
		forgotPassword : function(){
			$modalInstance.dismiss('close');
			$modal.open({
		      animation: true,
		      templateUrl: 'templates/views/forgotPassword.html',
		      controller: 'forgotPasswordCtrl',
		      keyboard:false,
		      backdrop : 'static',
		      size:'md'
		    });
		}
	};
}]);

//Register Controller
mainApp.controller("registerCtrl", ['$scope','$modalInstance','$modal','$cookies','$cookieStore','$window','$http','$filter','$log','RegisterURL',function($scope,$modalInstance, $modal,$cookies,$cookieStore,$window,$http,$filter,$log,RegisterURL) {
	$scope.registerForm = {
			email : null,
			password : null,
			registerButtonDisabled : null,
			loginButtonDisabled : null,
			loginButtonText : "Login",
			registerButtonText : "Register",
			registerStatus : "displayNone",
			registerMessage : "",
			register : function(){
				$scope.registerForm.registerButtonDisabled = "true";
				$scope.registerForm.loginButtonDisabled = "true";
				$scope.registerForm.registerButtonText = "Please wait...";
				var passwordWithoutSpace = $filter('nospace')($scope.registerForm.password);
				$log.debug($scope.registerForm);
				if (checkMandatoryFieldsPassed($scope.registerForm)){
					if ($filter('validateEmail')($scope.registerForm.email)){
						if ($scope.registerForm.password === $scope.registerForm.confirmPassword){
							if ($scope.registerForm.licenseTypeEnterprise==true){
								if ($filter('validateDate')($scope.registerForm.expiryDate)){
									sendAjaxRequest();
								} else {
									$scope.registerForm.registerStatus = "alert-danger";
									$scope.registerForm.registerMessage = "Expiry Date is not a valid Date.";
									$scope.registerForm.registerButtonText = "Register";
									$scope.registerForm.registerButtonDisabled = null;
									$scope.registerForm.loginButtonDisabled = null;
								}
							} else if ($scope.registerForm.licenseTypeStandard == true) {
								sendAjaxRequest();
							} 
						} else {
							$scope.registerForm.registerStatus = "alert-danger";
							$scope.registerForm.registerMessage = "Passwords are not matching.";
							$scope.registerForm.registerButtonText = "Register";
							$scope.registerForm.registerButtonDisabled = null;
							$scope.registerForm.loginButtonDisabled = null;
						}
					} else {
						//Email format is not valid.
						$scope.registerForm.registerStatus = "alert-warning";
						$scope.registerForm.registerMessage = "Email address is not valid.";
						$scope.registerForm.registerButtonText = "Register";
						$scope.registerForm.registerButtonDisabled = null;
						$scope.registerForm.loginButtonDisabled = null;
					}
				} else {
					//Mandatory parameters missing.
					$scope.registerForm.registerStatus = "alert-danger";
					$scope.registerForm.registerMessage = "Mandatory Parameters are missing.";
					$scope.registerForm.registerButtonText = "Register";
					$scope.registerForm.registerButtonDisabled = null;
					$scope.registerForm.loginButtonDisabled = null;
				}
			},
			login : function(){
				$modalInstance.dismiss('close');
				$modal.open({
			      animation: true,
			      templateUrl: 'templates/views/login.html',
			      controller: 'loginCtrl',
			      keyboard:false,
			      backdrop : 'static',
			      size:'md'
			    });
			}
		};

	var sendAjaxRequest = function(){
		var responsePromise = $http({
	        url: RegisterURL,
	        method: "POST",
	        data: { 
	        	'email' : $scope.registerForm.email,
	        	'password' : $scope.registerForm.password,
	        	'firstname' : $scope.registerForm.firstname,
	        	'lastname' : $scope.registerForm.lastname,
	        	'address' : $scope.registerForm.address,
	        	'city' : $scope.registerForm.city,
	        	'country' : $scope.registerForm.country,
	        	'companyname' : $scope.registerForm.companyname,
	        	'postcode' : $scope.registerForm.postcode,
	        	'termsAndConditions' : $scope.registerForm.termsAndConditions,
	        	'licenseKey':$scope.registerForm.licenseTypeEnterprise==true?$scope.registerForm.licenseKey:'',
	        	'expiryDateString':$scope.registerForm.licenseTypeEnterprise==true?$scope.registerForm.expiryDate:'',
	        	'featureList':$scope.registerForm.licenseTypeEnterprise==true?$scope.registerForm.featureList:''
	        }
	    });
		responsePromise.success(function(data, status, headers, config) {
			if (data!=null){
				$scope.registerForm.registerStatus = "alert-success";
				$scope.registerForm.registerMessage = "User has been successfully registered. Please check your email to acitvate the account.";
				$scope.registerForm.registerButtonText = "...";
				$scope.registerForm.loginButtonText = "Login";
				$scope.registerForm.registerButtonDisabled = "true";
				$scope.registerForm.loginButtonDisabled = null;
			} else {
				$scope.registerForm.registerStatus = "alert-danger";
				$scope.registerForm.registerMessage = "Registration Failed! Please try again after reloading the page.";
				$scope.registerForm.registerButtonText = "Register";
				$scope.registerForm.registerButtonDisabled = null;
				$scope.registerForm.loginButtonDisabled = null;
				$log.warn(SUtils.appConfig.mainAppLogPrefix + "mainApp.controller.registerCtrl() :: AJAX Response on URL : "+RegisterURL + " is undefined");
			}
        });
		responsePromise.error(function(data, status, headers, config) {
			$scope.registerForm.registerStatus = "alert-danger";
			$scope.registerForm.registerMessage = "Registration Failed! Please try again.";
			if (data!=null && data.message!=null){
				$scope.registerForm.registerMessage = data.message;
			}
			
			$scope.registerForm.registerButtonText = "Register";
			$scope.registerForm.registerButtonDisabled = null;
			$scope.registerForm.loginButtonDisabled = null;
			$log.error(SUtils.appConfig.mainAppLogPrefix + "mainApp.controller.registerCtrl() :: AJAX Request failed on URL : "+RegisterURL);
            
        });
	};
	
	var checkMandatoryFieldsPassed = function(registerForm){
		if (registerForm.email!=null && registerForm.email!='' && 
				registerForm.password!=null && $filter('nospace')(registerForm.password)!='' &&
				registerForm.confirmPassword!=null && $filter('nospace')(registerForm.confirmPassword)!='' &&
				registerForm.firstname!=null && registerForm.firstname!='' &&
				registerForm.lastname!=null && registerForm.lastname!='' &&
				registerForm.city!=null && registerForm.city!='' &&
				registerForm.country!=null && registerForm.country!='' &&
				registerForm.companyname!=null && registerForm.companyname!='' &&
				registerForm.termsAndConditions!=null && registerForm.termsAndConditions==true){
			if (registerForm.licenseTypeStandard==true || registerForm.licenseTypeEnterprise == true){
				if (registerForm.licenseTypeEnterprise == true){
					if (registerForm.featureList!=null && registerForm.featureList != '' && 
						registerForm.licenseKey!=null && registerForm.licenseKey!='' &&
						registerForm.expiryDate!=null && registerForm.expiryDate!=''){
						return true;
					} else {
						return false;
					}
				} else if (registerForm.licenseTypeStandard==true) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
			
		} else {
			return false;
		}
	}

}]);


//Forgot Password Controller
mainApp.controller("forgotPasswordCtrl", ['$scope','$modalInstance','$modal','$cookies','$cookieStore','$window','$http','$filter','$log','ForgotPasswordURL',function($scope,$modalInstance, $modal,$cookies,$cookieStore,$window,$http,$filter,$log,ForgotPasswordURL) {
	$scope.forgotPasswordForm = {
			email : null,
			forgotPasswordButtonDisabled : null,
			loginButtonDisabled : null,
			loginButtonText : "Login",
			forgotPasswordButtonText : "Submit",
			forgotPasswordStatus : "displayNone",
			forgotPasswordMessage : "",
			submit : function(){
				$scope.forgotPasswordForm.forgotPasswordButtonDisabled = "true";
				$scope.forgotPasswordForm.loginButtonDisabled = "true";
				$scope.forgotPasswordForm.forgotPasswordButtonText = "Please wait...";
				$log.debug($scope.forgotPasswordForm);
				if (checkMandatoryFieldsPassed($scope.forgotPasswordForm)){
					if ($filter('validateEmail')($scope.forgotPasswordForm.email)){
						sendAjaxRequest();
					} else {
						//Email format is not valid.
						$scope.forgotPasswordForm.forgotPasswordStatus = "alert-warning";
						$scope.forgotPasswordForm.forgotPasswordMessage = "Email address is not valid.";
						$scope.forgotPasswordForm.forgotPasswordButtonText = "Submit";
						$scope.forgotPasswordForm.forgotPasswordButtonDisabled = null;
						$scope.forgotPasswordForm.loginButtonDisabled = null;
					}
				} else {
					//Mandatory parameters missing.
					$scope.forgotPasswordForm.forgotPasswordStatus = "alert-danger";
					$scope.forgotPasswordForm.forgotPasswordMessage = "Please enter the email address.";
					$scope.forgotPasswordForm.forgotPasswordButtonText = "Submit";
					$scope.forgotPasswordForm.forgotPasswordButtonDisabled = null;
					$scope.forgotPasswordForm.loginButtonDisabled = null;
				}
			},
			login : function(){
				$modalInstance.dismiss('close');
				$modal.open({
			      animation: true,
			      templateUrl: 'templates/views/login.html',
			      controller: 'loginCtrl',
			      keyboard:false,
			      backdrop : 'static',
			      size:'md'
			    });
			}
		};

	var sendAjaxRequest = function(){
		var responsePromise = $http({
	        url: ForgotPasswordURL,
	        method: "POST",
	        data: { 
	        	'email' : $scope.forgotPasswordForm.email
	        }
	    });
		responsePromise.success(function(data, status, headers, config) {
			if (data!=null){
				$scope.forgotPasswordForm.forgotPasswordStatus = "alert-success";
				$scope.forgotPasswordForm.forgotPasswordMessage = "Password has been successfully sent to your email address.";
				$scope.forgotPasswordForm.forgotPasswordButtonText = "...";
				$scope.forgotPasswordForm.loginButtonText = "Login";
				$scope.forgotPasswordForm.forgotPasswordButtonDisabled = "true";
				$scope.forgotPasswordForm.loginButtonDisabled = null;
			} else {
				$scope.forgotPasswordForm.forgotPasswordStatus = "alert-danger";
				$scope.forgotPasswordForm.forgotPasswordMessage = "Password Reset Failed! Please try again after reloading the page.";
				$scope.forgotPasswordForm.forgotPasswordButtonText = "Submit";
				$scope.forgotPasswordForm.forgotPasswordButtonDisabled = null;
				$scope.forgotPasswordForm.loginButtonDisabled = null;
				$log.warn(SUtils.appConfig.mainAppLogPrefix + "mainApp.controller.forgotPasswordCtrl() :: AJAX Response on URL : "+ForgotPasswordURL + " is undefined");
			}
        });
		responsePromise.error(function(data, status, headers, config) {
			$scope.forgotPasswordForm.forgotPasswordStatus = "alert-danger";
			$scope.forgotPasswordForm.forgotPasswordMessage = "Your password can't be restored. Please contact adminstrator.";
			if (data!=null && data.message!=null){
				$scope.forgotPasswordForm.forgotPasswordMessage = data.message;
			}
			
			$scope.forgotPasswordForm.forgotPasswordButtonText = "Submit";
			$scope.forgotPasswordForm.forgotPasswordButtonDisabled = null;
			$scope.forgotPasswordForm.loginButtonDisabled = null;
			$log.error(SUtils.appConfig.mainAppLogPrefix + "mainApp.controller.forgotPasswordCtrl() :: AJAX Request failed on URL : "+ForgotPasswordURL);
            
        });
	};
	
	var checkMandatoryFieldsPassed = function(forgotPasswordForm){
		if (forgotPasswordForm.email!=null && forgotPasswordForm.email!=''){
			return true;
		} else {
			return false;
		}
	}

}]);

mainApp.controller("confirmModelController", ['$scope','$modalInstance','modelData',function($scope,$modalInstance, modelData){
	$scope.close = function () {
	    $modalInstance.close();
	};
	$scope.title = modelData.title;
	$scope.message = modelData.message;
	$scope.ok = function(){
		$modalInstance.close();
	};
	$scope.cancel = function(){
		$modalInstance.dismiss('close');
	};
}]);
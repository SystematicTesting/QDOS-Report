var mainApp = angular.module('mainApp',['ui.bootstrap','navBar','sidebar','ngSanitize','ngTable','ngResource','ngRoute','ngCookies']);

//constants
mainApp.value("LoginURL","/user/login.rest");
mainApp.value("RegisterURL","/user/create.rest");
mainApp.value("AccountActivationURL","/user/activateAccount.rest");
mainApp.value("ForgotPasswordURL","/user/forgotPassword.rest");

mainApp.config(['$httpProvider', function ($httpProvider) {
	// Intercept POST requests, convert to standard form encoding
	$httpProvider.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
	$httpProvider.defaults.transformRequest.unshift(function (data, headersGetter) {
		var headers = headersGetter();
		if (headers.byPassHttpProviderConfig){
			return data;
		}
		var key, result = [];
		if (typeof data === "string"){
			return data;
		}
		for (key in data) {
			if (data.hasOwnProperty(key)) {
				result.push(encodeURIComponent(key) + "=" + encodeURIComponent(data[key]));
			}
		}
		return result.join("&");
	});
}]);

mainApp.config(['$routeProvider', function($routeProvider){
	$routeProvider.
		when('/', {
			templateUrl: 'templates/views/home.html',
			controller: 'userHomePageCtrl'
		});
}]);
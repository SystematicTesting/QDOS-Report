var testcatalog = angular.module('testcatalog',['gridshore.c3js.chart','ngCookies','angularFileUpload','ngAnimate','ngStorage','angular-meditor','ui.bootstrap','ngTagsInput']);

testcatalog.config(['$routeProvider', function($routeProvider){
	$routeProvider.
		when('/testcatalog/mysites/view', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.mysites.html',
			controller: 'mysitesCtrl'
		}).
		when('/testcatalog/mysites/user/:user/:siteName/:numberOfRecords', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.html',
			controller: 'testcatalogMysitesUserSingleSiteCtrl'
		}).
		when('/testcatalog/mysites/user/:user/:siteName/:numberOfRecords/graphType/:graphType', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.html',
			controller: 'testcatalogMysitesUserSingleSiteCtrl'
		}).
		when('/testcatalog/mysites/user/:user/:siteName/version/:version', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.version.html',
			controller: 'testcatalogMysitesUserSingleSiteVersionCtrl'
		}).
		when('/testcatalog/mysites/user/:user/:siteName/version/:version/:suiteName', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.version.suitename.html',
			controller: 'testcatalogMysitesUserSingleSiteVersionSuiteNameCtrl'
		}).
		when('/testcatalog/mysites/user/:user/:siteName/version/:version/:suiteName/:testCaseId', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.version.suitename.testcaseid.html',
			controller: 'testcatalogMysitesUserSingleSiteVersionSuiteNameTestCaseIdCtrl'
		}).
		when('/testcatalog/:user/:siteName/:numberOfRecords', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.html',
			controller: 'testcatalogUserSingleSiteCtrl'
		}).
		when('/testcatalog/:user/:siteName/:numberOfRecords/graphType/:graphType', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.html',
			controller: 'testcatalogUserSingleSiteCtrl'
		}).
		when('/testcatalog/:user/:siteName/version/add', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.add.version.html',
			controller: 'testcatalogUserSingleSiteAddVersionCtrl'
		}).
		when('/testcatalog/:user/:siteName/version/:version', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.version.html',
			controller: 'testcatalogUserSingleSiteVersionCtrl'
		}).
		when('/testcatalog/:user/:siteName/version/:version/:suiteName', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.version.suitename.html',
			controller: 'testcatalogUserSingleSiteVersionSuiteNameCtrl'
		}).
		when('/testcatalog/:user/:siteName/version/:version/:suiteName/:testCaseId', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.version.suitename.testcaseid.html',
			controller: 'testcatalogUserSingleSiteVersionSuiteNameTestCaseIdCtrl'
		}).
		when('/testcatalog/edit/:user/:siteName/suiteName/:testSuiteName/:sourceType', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.edit.testSuite.html',
			controller: 'testcatalogUserSingleSiteEditTestSuiteCtrl'
		}).
		when('/testcatalog/edit/:user/:siteName/suiteName/:testSuiteName/:sourceType/elementProperties', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.edit.testSuite.elementProperties.html',
			controller: 'testcatalogUserSingleSiteEditTestSuiteElementPropertiesCtrl'//TODO
		}).
		when('/testcatalog/edit/:user/:siteName/suiteName/:testSuiteName/:sourceType/data/:testCaseId', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.edit.testData.html',
			controller: 'testcatalogUserSingleSiteEditTestDataCtrl'
		}).
		when('/testcatalog/edit/:user/:siteName/suiteName/:testSuiteName/:sourceType/teststeps/:testCaseId', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.site.edit.testSteps.html',
			controller: 'testcatalogUserSingleSiteEditTestStepsCtrl'
		}).
		when('/testcatalog/:user', {
			templateUrl: 'templates/module/testcatalog/view/testcatalog.user.html',
			controller: 'testcatalogUserSitesCtrl'
		}).
		when('/testcatalog', {
			templateUrl: 'templates/module/testcatalog/view/catalog.html',
			controller: 'testcatalogHomePageCtrl'
		}).
	    otherwise({
	        redirectTo: '/'
	    });
}]);



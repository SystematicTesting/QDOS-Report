String.prototype.endsWith = function(str) {
	return (this.match(str+"$")==str);
};

String.prototype.startsWith = function(str) {
	return (this.match("^"+str)==str);
};

var SUtils = SUtils || {};

SUtils.appConfig = {
	logLevel : "debug",
	qdosLogPrefix : " :: QDOS :: ",
	sitemgrLogPrefix : " :: SITEMGR :: ",
	testcatalogLogPrefix : " :: TESTCATALOG :: ",
	mainAppLogPrefix : " :: mainApp :: "
};

SUtils.helpers = {
	log: function(str){
		if(window.console){
			console.log(str);
		}
	}
};

SUtils.warn = function(msg){
	msg = "WARN :: "+msg;
	SUtils.helpers.log(msg);
};

SUtils.debug = function(msg){
	if (SUtils.appConfig.logLevel === "debug"){
		SUtils.helpers.log(msg);
	}
};

SUtils.info = function(msg){
	if (SUtils.appConfig.logLevel === "info"){
		msg = "INFO :: "+msg;
		SUtils.helpers.log(msg);
	}
};

SUtils.error = function(msg){
	msg = "ERROR :: "+msg;
	SUtils.helpers.log(msg);
};

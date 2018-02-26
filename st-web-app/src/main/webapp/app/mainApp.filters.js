//All Filters
mainApp.filter('encodeURI', function() {
  return window.encodeURIComponent;
});

mainApp.filter('decodeURI', function() {
  return window.decodeURIComponent;
});

mainApp.filter('removeSpecialCharacter', function() {
  return function(input) {
    return "obj-"+input.replace(/[^\w]/gi, '');
  };
});

mainApp.filter('showPercentileText', function($sce) {
  return function(percentile) {
    if (percentile<3){
        return $sce.trustAsHtml("<span class=\"sr-only\"> "+percentile+"% </span>");
    } else {
        return percentile+"%";
    }
  };
});

mainApp.filter('showStringAsHTML', function($sce) {
  return function(htmlString) {
    return $sce.trustAsHtml(htmlString);
  };
});


mainApp.filter('addThumbnailImageElement', function($sce) {
  return function(URL) {
    if (URL.endsWith("DISABLED")){
        return "";
    } else if (URL.endsWith("APP_SERVER") || URL.startsWith("activeAPIkey")){
    	return "<img src='/servlet/module/qdos/GetScreenShot.file?"+URL+"'  class='highlight-box show-dark-box screenshot-thumbnail'/>";
    } else {
        return "<img src='"+URL+"'  class='highlight-box show-dark-box screenshot-thumbnail'/>";
    }
  };
});

mainApp.filter('addImageElement', function($sce) {
  return function(URL) {
    if (URL.endsWith("DISABLED")){
        return "";
    } else if (URL.endsWith("APP_SERVER") || URL.startsWith("activeAPIkey")){
    	return "<img src='/servlet/module/qdos/GetScreenShot.file?"+URL+"' width='100%'/>";
    } else {
        return "<img src='"+URL+"'  width='100%'/>";
    }
  };
});

mainApp.filter('isEnterpriseLicense', function($cookies) {
  return function() {
      if ($cookies.licenseexpiryDateString!=null && $cookies.licenseexpiryDateString!='Not Applicable' && $cookies.isEnterpriseLicense !=null && $cookies.isEnterpriseLicense === 'true'){
          return true;
      } else {
          return false;
      }
  };
});

mainApp.filter('roundMathCeil', function() {
    return function(number) {
        return Math.ceil(number);
    };
});

mainApp.filter('roundMathFloor', function() {
    return function(number) {
        return Math.floor(number);
    };
});

mainApp.filter('createPercentageBarValue', function() {
    return function(percentile) {
        if (percentile<=0){
            return "0%";
        } else {
            return (percentile-0.01)+"%";
        }
    };
});

mainApp.filter('secondsToDateTime', function() {
    return function(input) {
        function hr(n) { return (n > 1 ? n+' Hrs.' : (n<1? '' : n+' Hr.')); }
        function min(n) { return (n > 1 ? n+' Mins.' : (n<1? '' : n+' Min.')); }
        function sec(n) { return (n > 1 ? n+' Secs.' : (n<1? '' : n+' Sec.')); }
        var seconds = 0;
        var minutes = 0;
        var hours = 0;
        if (input>0){
            var seconds = Math.ceil(input % 60);
            var minutes = Math.floor(input % 3600 / 60);
            var hours = Math.floor(input / 3600);
            return (hr(hours) + ' ' + min(minutes) + ' ' + sec(seconds));
        } else {
            return "0 Sec.";
        }
    };
});

mainApp.filter('nospace', function () {
    return function (value) {
        return (!value) ? '' : value.replace(/ /g, '');
    };
});

mainApp.filter('validateEmail', function () {
    return function (email) {
        var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
        
    };
});

mainApp.filter('validateDate', function () {
    return function (expiryDate) {
        var pattern =/^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/;
        if (pattern.test(expiryDate)){
            var bits = expiryDate.split('/');
            var d = new Date(bits[2], bits[1] - 1, bits[0]);
            return d && (d.getMonth() + 1) == bits[1] && d.getDate() == Number(bits[0]) && d.getFullYear() == bits[2];
        } else {
            return false;
        }
    };
});

mainApp.filter('stripSpecialCharactersFromString', function () {
    return function (str) {
        var result = str.replace(/[^A-Za-z0-9]/g, '');
        return result;
        
    };
});

mainApp.filter('isStringFreeFromSpecialCharacters', function () {
    return function (qry) {
    	var pattern = /[^A-Za-z0-9]/g
		if(qry.match(pattern)) {
		    return false;
		}
		else{
		    return true;
		}        
    };
});

mainApp.filter('getRequestParameter', function () {
    return function (sParam) {
        var sPageURL = window.location.search.substring(1);
        var sURLVariables = sPageURL.split('&');
        for (var i = 0; i < sURLVariables.length; i++) {
            var sParameterName = sURLVariables[i].split('=');
            if (sParameterName[0] == sParam) {
                return sParameterName[1];
            }
        }
        
    };
});

mainApp.filter('reverse', function() {
    return function(items) {
        return items.slice().reverse();
    };
});
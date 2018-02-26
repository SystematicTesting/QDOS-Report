<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<%-- <%@page session="false"%>
<%@include file="/libs/foundation/global.jsp" %> --%>

<html>
	<head>
		<title>Autocomplete Address</title>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
		<meta charset="utf-8">
		
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">
		<link type="text/css" rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500">
	  	<style>
	  		html, body {
	  			height:100%;
				margin:0;
				padding:0
			}
			#locationField, #controls {
			  position: relative;
			}
			.label {
			  text-align: right;
			  font-weight: bold;
			  width: 100px;
			  color: #303030;
			}
			#address {
			  background-color: #f0f0ff;
			  padding-right: 2px;
			}
			#address td {
			  font-size: 10pt;
			}
			.field {
			  width: 100%;
			}
			.slimField {
			  width: 40%;
			}
			.wideField {
			  width: 60%;
			}
			.container-fluid{
			  height:100%;
			  display:table;
			  width: 100%;
			  padding: 0;
			}
			.row-fluid {height: 100%; display:table-cell; vertical-align: middle;}
			.centering {
			  float:none;
			  margin:0 auto;
			}
			.logs{
				background-color:black;
				color:chartreuse;
			}
		</style>
	</head>
  	<body>
	  	<div class="container-fluid">
	  		<div class="row-fluid">
				<div class="col-md-2">
				</div>
				<div class="col-md-8 centering">
					<div class="panel panel-primary">
						<div class="panel-heading">Google Autocomplete | United States</div>
						<div class="panel-body">
					    	<div class="input-group" id="locationField">
								<span class="input-group-addon" id="basic-addon1">Search</span>
								<input id="autocomplete" type="text" class="form-control" placeholder="Enter address" aria-describedby="basic-addon1">
							</div>
							<br/>
					
							<table id="address">
							  <tr>
							    <td class="label">Street address</td>
							    <td class="slimField"><input class="field" id="street_number" disabled="true"></input></td>
							    <td class="wideField" colspan="2"><input class="field" id="route" disabled="true"></input></td>
							  </tr>
							  <tr>
							    <td class="label">City</td>
							    <td class="wideField" colspan="3"><input class="field" id="locality" disabled="true"></input></td>
							  </tr>
							  <tr>
							    <td class="label">State</td>
							    <td class="slimField"><input class="field" id="administrative_area_level_1" disabled="true"></input></td>
							    <td class="label">Zip code</td>
							    <td class="wideField"><input class="field" id="postal_code" disabled="true"></input></td>
							  </tr>
							  <tr>
							    <td class="label">Country</td>
							    <td class="wideField" colspan="3"><input class="field" id="country" disabled="true"></input></td>
							  </tr>
							</table>
						</div>
					</div>
					<br/>
					<c:if test="${param.debug == 'true'}">
						<div class="panel panel-success">
							<div class="panel-heading">Debug | Logs</div>
							<div class="panel-body logs" id="debugLogs">
								Waiting...
							</div>
						</div>
					</c:if>
					
				</div>
				<div class="col-md-2">
				</div>
			</div>
			<div class="row-fluid">
			</div>
	  	</div>
    	

	    <script>
			var placeSearch, autocomplete;
			var debugEnabled = ${param.debug=='true'?'true':'false'};
			var componentForm = {
				street_number: 'short_name',
				route: 'long_name',
				locality: 'long_name',
				administrative_area_level_1: 'short_name',
				country: 'long_name',
				postal_code: 'short_name'
			};
	
			function initAutocomplete() {
			  // Create the autocomplete object, restricting the search to geographical
			  // location types.
			  autocomplete = new google.maps.places.Autocomplete(
					(document.getElementById('autocomplete')),
					{
				  		componentRestrictions: {'country': 'us'}
			  		});
			
			  // When the user selects an address from the dropdown, populate the address
			  // fields in the form.
			  autocomplete.addListener('place_changed', fillInAddress);
			}
	
			function fillInAddress() {
				// Get the place details from the autocomplete object.
				var place = autocomplete.getPlace();
				var postalCodeStatusInPlace = isPostalCodeAvailableInPlace(place);
				if (debugEnabled){
					$("#debugLogs").html("");
					var logStr = "User Selected Place : lat="+place.geometry.location.lat()+" :: lng="+place.geometry.location.lng()+" :: isPostalCodeAvailable="+postalCodeStatusInPlace;
					printLogs(logStr);
					console.log(place);
				}
				
				if (postalCodeStatusInPlace) {
					if (debugEnabled){
						printLogs("Populating user selected place : PROCESSING COMPLETE.");
					}
					populateSelectedPlace(place);
				} else {
					if (debugEnabled){
						printLogs("Finding Address with a POSTAL CODE on selected Lat-Lng");
					}
					var geocoder = new google.maps.Geocoder();
					var latLng = new google.maps.LatLng(place.geometry.location.lat(),place.geometry.location.lng());
					
					if (geocoder) {
						geocoder.geocode({ 'latLng': latLng}, function (results, status) {
							if (status == google.maps.GeocoderStatus.OK) {
				        	
								if (debugEnabled){
									for(var debugIndex in results){
										var logStr = "Result "+debugIndex+" : lat="+results[debugIndex].geometry.location.lat()+" :: lng="+results[debugIndex].geometry.location.lng();
										printLogs(logStr);
										console.log(results[debugIndex]);
									}
								}
								place = autoSelectAddressWithPostalCode(results);
								if (debugEnabled){
									printLogs("Populating autoselected place : PROCESSING COMPLETE.");
								}
								populateSelectedPlace(place);
							} else {
								if (debugEnabled){
									printLogs("Geocoding failed: " + status);
								}
				         		alert("Selected Address doesn't have a postal code. Please try again."); 
							}
						});
					} else {
						printLogs("Google API failed to create instance of geocoder");
						alert("Some error has occured with Google APIs. Please try again later"); 
				 	}
				}
			}
			
			function printLogs(logStr){
				console.log(logStr);
				$("#debugLogs").append("<br/>=> "+logStr);
			}
	      
		    function autoSelectAddressWithPostalCode(results){
				for (var index in results){
					for (var componentIndex in results[index].address_components){
						for (var typeIndex in results[index].address_components[componentIndex].types){
							if (results[index].address_components[componentIndex].types[typeIndex]=="postal_code"){
								var place = results[index];
								if (debugEnabled){
									var logStr = "Autoselected Place (with postal_code) : Result "+index+" : lat="+place.geometry.location.lat()+" :: lng="+place.geometry.location.lng();
									printLogs(logStr);
									console.log(place);
								}
								return place;
							}
						}
					}
				}
		    }
	      
			function isPostalCodeAvailableInPlace(place){
				for (var componentIndex = 0; componentIndex < place.address_components.length; componentIndex++){
					for (var typeIndex in place.address_components[componentIndex].types){
						if (place.address_components[componentIndex].types[typeIndex]=="postal_code"){
							return true;
						}
					}
				}
				return false;
			}
	      
			function populateSelectedPlace(place){
			    for (var component in componentForm) {
			        document.getElementById(component).value = '';
			        document.getElementById(component).disabled = false;
			      }
			      // Get each component of the address from the place details
			      // and fill the corresponding field on the form.
			      for (var i = 0; i < place.address_components.length; i++) {
			        var addressType = place.address_components[i].types[0];
			        if (componentForm[addressType]) {
			          var val = place.address_components[i][componentForm[addressType]];
			          document.getElementById(addressType).value = val;
			        }
			      }
			}
	    </script>
	    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyApKL0O6o9Ux20HEmvo7UCMh8sZpO39U6w&libraries=places&callback=initAutocomplete" async defer></script>
	    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	</body>
</html>
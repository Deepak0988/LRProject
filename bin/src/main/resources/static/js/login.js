$(function() {
	var custom_interface_option = {};
	custom_interface_option.templateName = 'loginradiuscustom_tmpl';
	LRObject.util.ready(function() {
		LRObject.customInterface(".interfacecontainerdiv", custom_interface_option);
	});

	var sl_options = {};
	sl_options.onSuccess = function(response) {
		//On Success
		//Here you get the access token
		console.log(response);
	};
	sl_options.onError = function(errors) {
		//On Errors
		console.log(errors);
	};
	sl_options.container = "sociallogin-container";

	LRObject.util.ready(function() {
		LRObject.init('socialLogin', sl_options);
	});
	
	$("#loginBtn").click(function() {
	    $.ajax({
	        type: "POST",
	        url: "/login",
	        dataType: "json",
	        data: $.param({
	            email: $("#username").val(),
	            password: $("#password").val()
	        }),
	        success: function(res) {
	        	console.log(res);
	           	getProfile(res.access_token, res.Profile.Uid);
	        },
	        error: function(xhr, status, error) {
	        	let respObj = JSON.parse(xhr.responseText);
	            $("#errorMsg").html(respObj.message);
	        }
	    });
	});
});



function getProfile(access_token, profile_uid) {
    localStorage.setItem('LRTokenKey', access_token);
    localStorage.setItem('lr-user-uid', profile_uid);
    $.ajax({
        type: "POST",
        url: "/profile",
        dataType: "json",
        data: $.param({
        	accessToken:  localStorage.getItem('LRTokenKey')
        }),
        success: function(res) {
        	window.location.href
        },
        error: function(xhr, status, error) {
        	consol.log(error);
        }
    });

}
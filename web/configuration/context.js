function testClientMode() {
	if(window.location.href.indexOf('file://') == 0)
		return true;
	return false;
}
function testClientModeCache(){
	var url = window.location.href;
	var index = url.indexof("?");
	if(index == -1)
		return true;
	var paramstr = url.substring(index + 1);
	var params = paramstr.split("&");
	for(var i = 0; i < params.length; i ++){
		if(params[i].startWith("ccache")){
			var pair = params[i].split("=");
			if(pair[1] == null || pair[1] == true)
				return true;
			return false;
		}
	}
	return true;
}
window.clientMode = testClientMode();
if(window.clientMode){
	window.clientModeCachable = testClientModeCache();
	if(window.DesignMode || !window.clientModeCachable)
		window.ClientMode_Postfix = parseInt(Math.random() * 1000) + "";
}
window.mainCtx = "#CTX#";
window.frameCtx = "#FRMCTX#";

if(window.clientMode){
	window.frameworkPath = "#FRAME_PATH#";
	window.contextMappings = {
		'#CTX#' : '#CTXPATH#applications/'
	};
	window.contextMappings[window.frameCtx] = '#FRAME_PATH#applications/';
}
else{
	window.frameworkPath = "/" + window.frameCtx + "/";
	window.contextMappings = {
		'#CTX#' : '/#CTX#/applications/'
	};
	window.contextMappings[window.frameCtx] = '/' + window.frameCtx + "/applications/";
}


var req = document.createElement("script");
req.src = window.frameworkPath + "ext-lib/requirejs/require.js";
document.body.appendChild(req);

var ready = false;
req.onreadystatechange = function(){
	if(req.readyState == "loaded" || req.readyState == "complete"){
		if(!ready){
			ready = true;
			sys_loadCommon();
		}
	} 
};

req.onload = function(){
	if(!ready){
		ready = true;
		sys_loadCommon();
	}
};
req.onerror = function(){
	alert('Not Found (404): require');
};

function sys_loadCommon() {
	var common = document.createElement("script");
	common.src = window.frameworkPath + "library/common/common.js";
	document.body.appendChild(common);
}
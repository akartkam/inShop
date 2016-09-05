/**
 * 
 */

function getRootWebSitePath()
{
    var _location = document.location.toString();
    var applicationNameIndex = _location.indexOf('/', _location.indexOf('://') + 3);
    var applicationName = _location.substring(0, applicationNameIndex) + '/';
    var webFolderIndex = _location.indexOf('/', _location.indexOf(applicationName) + applicationName.length);
    var webFolderFullPath = _location.substring(0, webFolderIndex);

    return webFolderFullPath;
}

(function($) {
    $.fn.hasScrollBar = function() {
        return this.get(0).scrollHeight > this.height();
    }
})(jQuery)

$.urlParam = function(name){
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    if (results==null){
       return null;
    }
    else{
       return results[1] || 0;
    }
}

function formatNumberTo6dot2 (input) {
	var txt = $(input).val(); 
	var oldTxt = $(input).data("oldtext"); 
	var reg = /^(?:(?=\d{1,6}\.{1})(\d{1,6}\.\d?\d?)|(\d{1,6}))$/gi; 
	if (!reg.test(txt)) {
		$(input).val(oldTxt);
	} else {
		$(input).data("oldtext", txt);
	}
}
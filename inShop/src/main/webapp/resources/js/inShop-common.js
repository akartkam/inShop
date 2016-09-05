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

function formatInputNumber (input) {
	var reg;
	if ($(input).hasClass("price-val")) {
		reg = /^(?:(?=\d{0,6}\.{1})(\d{0,6}\.\d?\d?)|(\d{0,6}))$/gi; 
	} else if ($(input).hasClass("quantity-val")) {
		reg = /^\d{0,6}$/;
	}
	var txt = $(input).val(); 
	var oldTxt = $(input).data("oldtext"); 
	if (!reg.test(txt)) {
		$(input).val(oldTxt);
	} else {
		$(input).data("oldtext", txt);
	}
}
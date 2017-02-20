/**
 * 
 */

var extraDataDiv   = "ajax-extra-data";

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

function slugify(str) {
    str = str.replace(/^\s+|\s+$/g, ''); // trim
    str = str.toLowerCase();

    var from = "абвгдеё жзийклмнопрстуфхцч ш щ ыэю я ·/_,:;";
    var to   = "abvgdeyojziiklmnoprstufhcchshshyeyuya------";
    var curFrom, curTo;
    for (var i=0, l=from.length ; i<l ; i++) {
       curFrom = from.charAt(i);
       if (from.charAt(i+1)==" ") {
    	  curTo = to.substr(i,2);
    	  i++;
       } else {
    	  curTo = to.charAt(i); 
       }
    	   
       str = str.replace(new RegExp(curFrom, 'g'), curTo);
    }
    str = str.replace(/[^a-z0-9 -]/g, '') 
    .replace(/\s+/g, '-')
    .replace(/-+/g, '-');

    return str;
};


function getExtraData($data) {
    return extractData($data, extraDataDiv);
}

function extractData($data, dataDivId) {
    if (!($data instanceof jQuery)) {
        return null;
    }
    
	var extractedData = null;

    var $dataDiv = $data.find('#' + dataDivId);
	if ($dataDiv.length > 0) {
	    try {
	        extractedData = $.parseJSON($dataDiv.text());
	    } catch (e) {
	        console.log("Could not parse data as JSON: " + $dataDiv.text());
	    }
        $dataDiv.remove();
	}

    return extractedData;
}

$("body").on("mouseenter", ".mightOverflow", function() { 
	var $t = $(this); 
	var titleText="";
	//data-title exists
	if ($(this).data("title")) {
		titleText = $(this).data("title");
	} else {
		titleText = $t.text();
	}	
	var title = $t.attr("title");
	if (!title){ if (this.offsetWidth < this.scrollWidth) $t.attr("title", titleText) } 
	else { if (this.offsetWidth >= this.scrollWidth && title == titleText) $t.removeAttr("title")}}); 
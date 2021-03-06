 //<![CDATA[
$(function(){
	
    var modalReviewOptions = {
            minWidth	: 500,
    		maxWidth    : 750,
            minHeight   : 100,
            maxHeight   : 600
        };

    $("body").on("click", ".modal-submit-review", function() {
    	var modalClick = $(this).parents('.simplemodal-wrap').length > 0;
    	var $form = $(this).closest("form");
    	var $url = $form.attr("action");
    	var token = $form.find("input[name=_csrf]");
    	if ($form.length) $form = $form.serialize();
    	if (token.length) token = token.val();
    	var header = $('#_csrf_header').attr('content');    	
     	$.ajax({
            url: $url,
            type: "POST",
            cache: true,
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            }
        }).done(function (data){
            	  if (modalClick) $.modal.close();
               	  $.modal(data, modalReviewOptions);
               	  $("#simplemodal-container").css("height", "auto");
            	  $.modal.update(); 
   				  $("#rating-input").rating({showCaption: false, showClear: false});
          }).fail(function() {
      	    alert(defaultClientnErrorMessage);
          });
    	return false;
    });
    
	$("body").on("click", ".do-submit-review", function() {
		var modalClick = $(this).parents(".simplemodal-wrap").length > 0
    	var $form = $(this).closest("form");
    	var $url = $form.attr("action");
    	var token = $form.find("input[name=_csrf]");
    	if ($form.length) $form = $form.serialize();
    	if (token.length) token = token.val();
    	var header = $('#_csrf_header').attr('content');
  	  	$.ajax({
          url: $url,
          type: "POST",
          data: $form,
          cache: false,
          beforeSend: function(xhr) {
              xhr.setRequestHeader(header, token);
          }           
        }).done(function (data){
          	  if (modalClick) $.modal.close();
      		  $.modal(data, modalReviewOptions);  
          	  $("#simplemodal-container").css("height", "auto");
          	  $.modal.update();
          	  $("#rating-input").rating({showCaption: false, showClear: false});
        }).fail(function() {
    	    alert(defaultClientnErrorMessage);
       });
	});
    
	
});
//]]>
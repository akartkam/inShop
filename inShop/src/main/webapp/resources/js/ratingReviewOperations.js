 //<![CDATA[
$(function(){
	
    var modalReviewOptions = {
            minWidth	: 400,
    		maxWidth    : 750,
            minHeight   : 100,
            maxHeight   : 500
        };

    $("body").on("click", ".do-submit-review", function() {
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
            data: $form,
            cache: false,
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
          }           
        }).done(function (data){
    		  $(".po-error-quantity").css("display", "none");
    		  $(".po-error-incomplete").css("display", "none"); 
    		  $(".po-error").css("display", "none");
        	  var ajaxExtraData = getExtraData($(data));
        	  if (ajaxExtraData && ajaxExtraData.errors) {
        		  if (ajaxExtraData.errors.quantity) {
        			  $(".po-error-quantity").css("display", "block");
        		  }
        		  if (ajaxExtraData.errors.criticalError){
        			  if ("allOptionsRequired".localeCompare(ajaxExtraData.errors.criticalError) == 0){
        			     $(".po-error-incomplete").css("display", "block");
        			  } else {
        				 $(".po-error").css("display", "block");
        			  }
        		  }
        		  
        	  } else {
            	  if (modalClick) $.modal.close();
            	  if (ajaxExtraData && ajaxExtraData.cartItemCount) {
            		  updateHeaderCartItems(ajaxExtraData.cartItemCount, ajaxExtraData.cartTotal);            		  
            	  }
            	  if (ajaxExtraData && ajaxExtraData.type && ajaxExtraData.type == "buy1click") {
            		  $.modal(data, modalBuy1clickOptions);  
            	  } else {
                	  $.modal(data, modalCartOptions);
            	  }
              	  $("#simplemodal-container").css("height", "auto");
            	  $.modal.update();        	          		  
        	  }  
          }).fail(function() {
      	    alert(defaultClientnErrorMessage);
          });
    	};
    	return false;
    });
    
	$("body").on("click", ".modal-submit-review", function() {
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
      		  $.modal(data, modalBuy1clickOptions);  
          	  $("#simplemodal-container").css("height", "auto");
          	  $.modal.update();        	          		  
        }).fail(function() {
    	    alert(defaultClientnErrorMessage);
       });
	});
    
	
});
//]]>
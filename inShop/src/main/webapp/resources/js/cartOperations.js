 //<![CDATA[
$(function(){
	
    var modalCartOptions = {
            minWidth	: 400,
    		maxWidth    : 750,
            minHeight   : 100,
            maxHeight   : 500
        };

    var modalProductOptions = {
            maxWidth    : 300,
            minWidth 	: 300,
            minHeight   : 300,
            maxHeight	: 400
        };
    var modalBuy1clickOptions = {
            maxWidth    : 350,
            minWidth 	: 300,
            minHeight   : 100,
            maxHeight	: 450
        };
	
	function updateHeaderCartItems(newCount, newTotal) {
		$(".cart-amunt").html(newTotal);
		$(".product-count").html(newCount);
	}
	
	$("body").on("click", "a.modalcart", function() {
		$.ajax({
			type: "GET",
			url: $(this).attr("href")	
		}).done(function (data){
      	   $.modal(data, modalCartOptions);
      	   $("#simplemodal-container").css("height", "auto");
    	   $.modal.update();			
		});
		return false;
	});
	
	
    $("body").on("click", ".add-to-cart, .buy1click", function() {
    	var modalClick = $(this).parents('.simplemodal-wrap').length > 0;
    	var $form = $(this).closest("form");
    	var $url = "";
    	var $quantity = $form.find("[name='quantity']"); 
    	if ($quantity.val() == "") $quantity.val("0");
    	var $existsProductOptions = $form.find("[name='hasProductOptions']") 
    	var $hasProductOptions = false;
    	if ($existsProductOptions.length) {
    		$hasProductOptions = ($existsProductOptions.val() == "true" )
    	}
		if ($(this).hasClass("add-to-cart")) $url = $form.data("add-to-cart-path")
		else if ($(this).hasClass("buy1click")) $url = $form.data("buy1click-path")
    	var token = $form.find("input[name=_csrf]");
    	if ($form.length) $form = $form.serialize();
    	if (token.length) token = token.val();
    	var header = $('#_csrf_header').attr('content');
    	if($hasProductOptions) {
    		 $.ajax({
    	            url: $url,
    	            type: "GET",
    	            cache: false,
    	            beforeSend: function(xhr) {
    	                xhr.setRequestHeader(header, token);
    	            }           
    	          }).done(function (data){
	    	        	  if (modalClick){
	    	        		  $.modal.close();
	    	        	  }
	    	        	  $.modal(data, modalProductOptions); 
	    	          	  $("#simplemodal-container").css("height", "auto");
	    	        	  $.modal.update();    	        	      	        		  
    	          }).fail(function() {
  	        	    alert(defaultClientnErrorMessage);
    	          });
    		 
    	} else {
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
    
    $("body").on("click", ".remove-from-cart, .recalc-cart", function(){
    	var modalClick = $(this).parents(".simplemodal-wrap").length > 0;
    	var recalc = $(this).hasClass("recalc-cart");
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
        	  var ajaxExtraData = getExtraData($(data));
        	  $(".cart-error").css("display", "none");
        	  if (recalc && ajaxExtraData && ajaxExtraData.errors) {
        		  $(".cart-error").css("display", "block");
        	  } else {
	        	  updateHeaderCartItems(ajaxExtraData.cartItemCount, ajaxExtraData.cartTotal);
	        	  if (modalClick) {
	            	  $("#simplemodal-data").html(data);
	        	  } else {
	            	  $.modal(data, modalCartOptions);        		  
	        	  }	
	          	  $("#simplemodal-container").css("height", "auto");
	        	  $.modal.update();
        	  }
          }).fail(function() {
      	    alert(defaultClientnErrorMessage);
          });   	
    });
    
	$("body").on("click", ".place-buy1click", function() {
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
    
    $("body").on("click", ".checkout", function(){
    	window.location = root+"checkout";
    });
    

	
});
//]]>
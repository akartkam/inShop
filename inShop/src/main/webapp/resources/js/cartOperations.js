$(function(){ 
	
    $("body").on("click", "button.addToCart", function() {
    	var modalClick = $(this).parents('.simplemodal-wrap').length > 0;
    	var $form = $(this).closest('form');
    	
    	$.ajax({
            url: $form.attr('action'),
            type: "POST",
            dataType: "json",
            cache: false
          }).done(function (data){
        	  if (modalClick) $.modal.close();
        	  if(!data.error) {
        		  
        	  } else {
        		  
        	  }
          });
    });
	
});
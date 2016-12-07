$(function(){ 
	
    $("body").on("click", "button.addToCart", function() {
    	var modalClick = $(this).parents('.simplemodal-wrap').length > 0;
    	var $form = $(this).closest("form");
    	var $url = $form.attr("action");
    	$.ajax({
            url: $url,
            type: "POST",
            data: $form,
            cache: false
          }).done(function (data){
        	  if (modalClick) $.modal.close();
        	  $.modal(data, { maxWidth: 750, maxHeight: 450, minHeight: 350});
          });
    });
	
});

/*
var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

$.ajax({
                        type: "POST",
                        url: url,
                        beforeSend: function(xhr) {
                            xhr.setRequestHeader(header, token);
                        },

                        success: function(data, textStatus, jqXHR) {
                            alert(status);
                        },
                        error: function(request, status, error) {
                            alert(status);
                        }
                        
*/                        
                    });
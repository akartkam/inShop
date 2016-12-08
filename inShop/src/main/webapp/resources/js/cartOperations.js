$(function(){
	
	function updateHeaderCartItems(newCount, newTotal) {
		$(".cart-amunt").html(newTotal);
		$(".product-count").html(newCount);
	}
	
    $("body").on("click", "button.addToCart", function() {
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
        	  if (modalClick) $.modal.close();
        	  var ajaxExtraData = getExtraData($(data));
        	  updateHeaderCartItems(ajaxExtraData.cartItemCount, ajaxExtraData.cartTotal);
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
                        
                        
                    });

*/
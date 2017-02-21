$(function(){
	$("body").on("change", ".inp-delivery", function() {
		var id = $(this).val();
		$.ajax({
			type: "GET",
			url: root+"ajax-delivery-details?ID="+id
		}).done(function (data){
      	   $("#co-delivery-detail").html(data);			
		});
		return false;		
	});
	
});
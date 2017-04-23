$(function(){
	function formatSlStores(state) {
		  if (!state.id) { return state.text; }
		  var t = state.text.split("|");
		  var t1 = t[0];
		  var t2 = "";
		  if (t.length > 1) {
			  t2 = t[1];
		  }
		  var state = "<span style='font-size: 10pt;'>"+ t1 + "</span>"
		  if (t2.length) {
			  state+="<br/><span style='font-size: 8pt; font-weight: bold;'>&nbsp;("+ t2 + ")</span>" 
		  }
		  return $(state);		
	};
	
	function formatSlStoresSelection(repo) {
		if (repo.id == "") return repo.text;
		  var t = repo.text.split("|");
		  var t1 = t[0];
		  return t1;
	};
	
	$("body").on("change", ".inp-delivery", function() {
		var id = $(this).val();
		$.ajax({
			type: "GET",
			url: root+"ajax-delivery-details?ID="+id
		}).done(function (data){
      	   $("#co-delivery-detail").html(data);
      	   $("#sl-stores").select2({
      		 minimumResultsForSearch: -1,
      		 templateResult: formatSlStores,
      		 templateSelection: formatSlStoresSelection
      	   });
		   var $selStore = $("#selected-store"); 
		   if($selStore.length) {
				$("#sl-stores").val($selStore.val()).trigger("change").trigger("select2:select");
		   }
      	   
		});
		return false;		
	});

	$("body").on("select2:select", "#sl-stores", function (e){
		   var $option = $("option:selected",this); 
           var addr = $option.data("store-address");
           var phone = $option.data("store-phone");
           var wschedule = $option.data("store-wschedule");
           var ret = (addr? "<span style='font-weight: bold;'>Адрес:</span>&nbsp;"+addr: "") + 
                     (phone?"<br/><span style='font-weight: bold;'>Телефон:</span>&nbsp;"+phone: "")+
                     (wschedule? "<br/><span style='font-weight: bold;'>Режим работы:</span>&nbsp;"+wschedule: "");
           if (ret && ret.length) {
        	   $("#co-store-detail").html(ret);
        	   $("#co-store-detail").css("display","");
           } else {
        	   $("#co-store-detail").css("display","none");
           };
           
           var map = $option.data("store-map");
           if (map && map.length) {
       		   $("#co-store-map").html(map);
           }else{
        	   $("#co-store-map").html(""); 
           };
           
           var descr = $option.data("store-description");
           if(descr && descr.length) {
        	   $("#co-store-description").html(descr) ;
           } else {
        	   $("#co-store-description").html("") ;
           }
           
         });
	
	$(".place-order-button").one("click", function(form){
	    $(this).attr("disabled","disabled");
	    $(this).text("Отправка заказа...");
	    var $form = $(this).closest("form");
	    $form.submit();
	    return true;
	});	
	
});
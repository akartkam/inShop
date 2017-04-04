$(function(){
     var table = $("#productDataTable").DataTable( {
        	"language": dataTableI18nObject,
            "bStateSave": true
		});
        $('#productDataTable tfoot th').each( function () {
        	 if ($(this).is(":first-child") || $(this).is(":last-child")) return;
             var title = $('#productDataTable thead th').eq( $(this).index() ).text();
             $(this).html( '<input type="text" placeholder="Фильтр '+title+'" />' );
        });
     	table.columns().every( function () {
           var that = this;
    
           $( 'input', this.footer() ).on( 'keyup change', function () {
        	   that
                   .search( this.value )
                   .draw();
               
           });
       });
       //Проверка перед сабмитом
       $("body").on("click", ".btnSubmit" , function() {
        	//check for change category
        	var previd = $("#category").data("previd");
        	var currid = $("#category").val();
        	if (previd && previd!=currid)
        		return confirm(/*[[#{admin.catalog.product.edit.confirmCategoryChange}]]*/"") ;
        });
		//Добавление input для productStatus
 		$("body").on("change", "#productStatus", function(){
			$("#productStatusIc").find("input").each(function(i,v){$(v).remove()});    		 			
	        $(this).find("option:selected").each(function(i, v){
	        	$("<input>").attr({
	        	    type: "hidden",
	        	    name: "productStatus["+i+"]",
	        	    value: $(v).val()
	        	}).appendTo("#productStatusIc");								    		 		            
	        });    		 			
	
		});
 		$("body").on("change",  "#productOption", function(e){
			$("#productOptionIc").find("input").each(function(i,v){$(v).remove()});    		 			
	        $(this).find("option:selected").each(function(i, v){
	        	$("<input>").attr({
	        	    type: "hidden",
	        	    name: "productOptionsForForm["+i+"]",
	        	    value: $(v).val()
	        	}).appendTo("#productOptionIc");								    		 		            
	        });    		 			
	
		});
	 	$("body").on("change", "#inputSImg", function () {
				// Get content from Summernote
				var content = $("#summernote").summernote("code");
				// Add content from summernote to textarea
				$("#summernote").html( content );    				
    			var form = new FormData(document.forms.pform);
    			var path= $(this).data("path");
    			var formser;
    			if (typeof form != "undefined" && form != null) formser = form;
                $.ajax({
                	type: "POST",
                    url: path,
                    data: formser,
                    cache: false,
                    contentType: false,
                    processData: false		                        
                }).done(function (html) {
                            $("#dImageTable").html(html);
                            var s = $("#pImagesTable tbody > tr:last-child").position();
    			    	    $("#editModalBody" ).scrollTop( s.top );
    			    	    //for clean the file input
    			    	    $("#inputSImg").replaceWith($("#inputSImg").clone(true));
    			    	    
                });
    	 });
	     $("body").on("click", ".row-move-up",  function () { 
		    var row = $(this).closest("tr");
		    var rowOther = row.next();
		    row.insertAfter(row.next());
		    var inp = $(row).find(".image-url");
		    var inpOther = $(rowOther).find(".image-url");
		    var indexOther = $(rowOther).index();
		    var index = $(row).index();
		    $(inp).attr("name","defaultSku.images["+index+"]");
		    $(inp).attr("id","defaultSku.images"+index);
		    $(inpOther).attr("name","defaultSku.images["+indexOther+"]");
		    $(inpOther).attr("id","defaultSku.images"+indexOther);	    			    
	      });	    			   
   		$("body").on("click", ".row-move-down", function () { 
			    var row = $(this).closest('tr');
			    var rowOther = row.prev(); 
			    row.prev().insertAfter(row);
			    var inp = $(row).find(".image-url");
			    var inpOther = $(rowOther).find(".image-url");
			    var indexOther = $(rowOther).index();
			    var index = $(row).index();
			    $(inp).attr("name","defaultSku.images["+index+"]");
			    $(inp).attr("id","defaultSku.images"+index);
			    $(inpOther).attr("name","defaultSku.images["+indexOther+"]");
			    $(inpOther).attr("id","defaultSku.images"+indexOther);	    			    
		    }); 
   		$("body").on("click", ".open-deleteImageDialog", function () {
        	   var row = $(this).closest('tr');
        	   var index = $(row).index();
        	   $(row).remove();
        	   $('#pImagesTable tbody tr').slice(index).each( function () {
        		 var inp = $(this).find(".image-url");
        		 var currIndex = $(this).index();
        		 $(inp).attr("name","defaultSku.images["+currIndex+"]");
			     $(inp).attr("id","defaultSku.images"+currIndex);
        	   });
          });
        //Открытие окна удаления
        $("body").on("click", ".open-deleteDialog", function () {
    	     var Id = $(this).data("id");
    	     var prName = $(this).data("name");
    	     var action;
    	     $("#delete-modal-footer #ID").val(Id);
    	     $("#delete-modal-body #prName").html(prName);
    	});    	    
        $("body").on("click", ".open-editDialog", function () {
          var id = $(this).data("id");
          var path = $(this).data("path");
    	  if (typeof id == 'undefined') id=null;  
          $("#editModalContent").html("");
          $.ajax({
            url: path+((id != null)?"?ID="+id:"") ,
            cache: false
          }).done(function (html) {
                    $("#editModalContent").html(html);
                    $('#summernote').summernote(summernoteObject);
                    $(".selectpicker").selectpicker();
                    $(".datetimepicker").datetimepicker({
                        locale: "ru"
                    });
                    $(".zoomed-image").imagezoomsl({
                    	magnifiersize: [250, 250],
                    	zoomrange: [3,10],
                    	zoomstart: 4
                    });                    
                    //$(".zoomed-image").elevateZoom({cursor: "crosshair", zoomType: "lens"});
                    //$('#active-start-date').data("DateTimePicker").date($("active-start-date-input").val());
                    $("#name").focus();
          });
    	});  
   	    $("body").on("click", "#urlRefreshBtn", function() {
		   $("#urlForForm").val(slugify($("#defaultSku\\.name").val()));
	    });

})
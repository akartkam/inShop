  function renderProductName(data, type, full, meta){
	 var obj = $.parseJSON(data);
     var ret = "<div class='a-photo-out mightOverflow domain-col' data-title='"+obj.name+"'>"+
                "<span class='photo-block'>"+
                "  <span class='helper'></span>"+
                "  <img src='"+root+obj.image.substring(1)+"' />"+
                "</span>"+
			    "   <span>"+obj.name+" </span> "+
			    "   <br/>"+
			    "   <span style='font-size: 9pt;'>Артикул: "+obj.codes+"</span>"+
			    "</div>"	
     return ret;
   } 
  function renderBtnGroup(data, type, full, meta){
	var obj = $.parseJSON(data);
	var ret =
	  	"<div class='btn-group' role='group'>"+ 
        "<button type='button' class='open-editDialog btn btn-outline btn-default btn-xs' data-toggle='modal' "+ 
        "        data-target='#editModal' data-operation='edit' data-backdrop='static' data-keyboard='false' "+ 
        "        data-path='"+root+"admin/catalog/product/edit' data-id="+obj.id+">"+ 
        "  <i class='fa fa-pencil'></i>"+
        "</button> "+	
        "<button type='button' class='open-editDialog btn btn-outline btn-default btn-xs' "+ 
        "        data-toggle='modal' data-target='#editModal' data-operation='add' data-backdrop='static' "+ 
        "        data-keyboard='false' data-path='"+root+"admin/catalog/product/add'" + 
        "        data-id="+obj.id+">"+
        "  <i class='fa fa-copy'></i>"+
        "</button> "+
        "<button type='button' data-toggle='modal' data-target='#deleteModal' "+ 
        "        class='open-deleteDialog btn btn-outline btn-default btn-xs' data-name='"+obj.name+ "'"+ 
        "        data-id="+obj.id+">"+
        "  <i class='fa fa-trash-o'></i>"+
        "</button> "+
        "<button class='btn btn-outline btn-default btn-xs' type='button' data-toggle='dropdown' aria-haspopup='true' "+ 
        "        aria-expanded='false'> "+
		"		<i class='fa fa-sort-down'></i> "+
	    "</button> "+
        "<ul class='dropdown-menu'> "+
		"  <li><a href='"+root+"admin/catalog/sku?productID="+obj.id+"'>Варианты</a></li>"+
	    "</ul>"+
		"</div>"		  	  
    return ret;
  }
  function renderEnabled(data, type, full, meta){
	  var ret = "";
	  if (data == "y") {
		ret = "<i class='fa fa-check-circle'></i>"
  		}
      return ret;
  }

$(function(){
     var table = $("#productDataTable").DataTable( {
     	"language": dataTableI18nObject,
    	"searchDelay": 500,
    	"stateSave": true,
        "processing": true,
        "serverSide": true,
        "ajax": root+"product-ajax-load",
        'columnDefs': [{
            'targets': 0,
            'className': 'mightOverflow domain-col',
            'render': renderProductName
         },{
            'targets': 1,
            'className': 'mightOverflow domain-col'
         },{
            'targets': 2,
            'className': 'mightOverflow domain-col'
         },{
            'targets': 7,
            'className': 'text-center',
            'render': renderEnabled
         },{
            'targets': 8,
            'searchable': false,
            'orderable': false,
            'className': 'text-center',
            'render': renderBtnGroup
         }		            	 
         ]		            
    });
     /*  
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
       */
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
    			    	    var $imagesAddError = $("#pImagesTable tbody #images-add-error");
    			    	    if ($imagesAddError && $imagesAddError.length) {
    			    	    	alert("Ошибка добавления файла изображения. Неверный формат или размер файла.");
    			    	    	$imagesAddError.remove();
    			    	    }
    			    	    
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
                    /*$(".datetimepicker").datetimepicker({
                        locale: "ru"
                    });
                    $(".zoomed-image").imagezoomsl({
                    	magnifiersize: [250, 250],
                    	zoomrange: [3,10],
                    	zoomstart: 4
                    });*/                    
                    //$(".zoomed-image").elevateZoom({cursor: "crosshair", zoomType: "lens"});
                    //$('#active-start-date').data("DateTimePicker").date($("active-start-date-input").val());
                    $("#name").focus();
          });
    	});  
   	    $("body").on("click", "#urlRefreshBtn", function() {
		   $("#urlForForm").val(slugify($("#defaultSku\\.name").val()));
	    });

})
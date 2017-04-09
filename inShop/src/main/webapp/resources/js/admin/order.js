  function renderBtnGroup(data, type, full, meta){
	var obj = $.parseJSON(data);
	var ret =
	  	"<div class='btn-group' role='group'>"+ 
        "<button type='button' class='open-editDialog btn btn-outline btn-default btn-xs' data-toggle='modal' "+ 
        "        data-target='#editModal' data-operation='edit' data-backdrop='static' data-keyboard='false' "+ 
        "        data-path='"+root+"admin/order/edit' data-id="+obj.id+">"+ 
        "  <i class='fa fa-pencil'></i>"+
        "</button> "+
		"</div>"		  	  
    return ret;
  }
 
	function updateTotals() {
		var $subTotal = 0;
		var $total = 0;
		var pn;
		$("table#orderItemsDT>tbody>tr>td>span[id^=rowTotal_]").each(function(){
			pn = parseFloat($(this).html());
			if (!isNaN(pn)) $subTotal = $subTotal + pn;
		});
		$total = $subTotal;
		$("#subTotal").html($subTotal.toFixed(2));
		$("#total").html($total.toFixed(2));
	}
  
	$(function() {
		$("#oDataTable").DataTable( {
	     	"language": dataTableI18nObject,
	     	"autoWidth": false,
	    	"searchDelay": 500,
	    	"stateSave": true,
	        "processing": true,
	        "serverSide": true,
	        "ajax": root+"order-ajax-load",
	        "order": [[ 1, "desc" ]],
	        "columnDefs": [
	         {
	            "targets": 6,
	            "searchable": false,
	            "orderable": false,
	            "className": "text-center",
	            "render": renderBtnGroup
	         }		            	 
	         ]		            
	    });		
	   /*
        $('#oDataTable tfoot th').each( function () {
        	 if ($(this).is(":first-child") || $(this).is(":last-child")) return;
             var title = $('#oDataTable thead th').eq( $(this).index() ).text();
             $(this).html( '<input type="text" placeholder="Фильтр '+title+'" />' );
        });
 		table.columns().every( function () {
           var that = this;	  	        
           $( 'input', this.footer() ).on( 'keyup change', function () {
        	   that
                   .search( this.value )
                   .draw();
               
           });
       });*/
	   
       $(document).on("click", ".open-editDialog", function () {
          var id = $(this).data("id");
          var path = $(this).data("path");
    	  if (typeof id == 'undefined') id=null;  
          $("#editModalContent").html("");
          $.ajax({
            url: path+((id != null)?"?ID="+id:"") ,
            cache: false
          }).done(function (html) {
                    $("#editModalContent").html(html);
                    $(".selectpicker").selectpicker();
                   makeSkuSelect2 ($("#slSearchSku"), /*[[@{/product-search}]]*/'');
          });
    	});	    			
        //Открытие окна удаления
        $("body").on("click",".open-deleteDialog", function () {
    	     var Id = $(this).data("id");
    	     var poName = $(this).data("name");
    	     if (typeof poName == "undefined" || poName ==null) poName = '';
    	     var modalName = $(this).data("target");
    	     $(modalName).find(".modal-footer .btn-primary").val(Id);
    	     $(modalName).find(".modal-body .objname").html(poName);
    	     if (modalName == "#deleteModalPov") {
    	    	 var canRemove = $(this).data("canremove");
    	    	 var checkRemove = $(modalName).find("#phisycalDeletePov")
    	    	 if (typeof checkRemove != "undefined" && checkRemove !=null) checkRemove.attr("disabled", !canRemove);
    	     }
    	});
    });
	$("body").on("click", ".add-new-sku", function () { 
    	var id = $("#slSearchSku").val();
    	if (typeof id == "undefined") return;
		var form = $("#oeform");
		var formser;
		if (typeof form != "undefined" && form != null) formser = form.serialize();
           $.ajax({
           	   type: "POST",
               url: /*[[@{/admin/order/add-item}]]*/'',
               data: formser+"&skuId="+id,
               cache: false
           }).done(function (html) {
        	   $("#dOrderItemTable").html(html);
        	   updateTotals();
           });
	});
	$("body").on("click", ".open-deleteDialog-oi", function () { 
    	var id = $(this).data("id");
    	if (typeof id == "undefined") return;
		var form = $("#oeform");
		var formser;
		if (typeof form != "undefined" && form != null) formser = form.serialize();
           $.ajax({
           	   type: "POST",
               url: /*[[@{/admin/order/del-item}]]*/'',
               data: formser+"&ID="+id,
               cache: false
           }).done(function (html) {
        	   $("#dOrderItemTable").html(html);
        	   updateTotals();
           });
	});	    			    		
	$("body").on("input", ".row-total-changer", function (event) { 
		    formatInputNumber(event.target);
			var id = $(event.target).data("id");
			if (typeof id == "undefined" || id == null) return;
			
			var $price = $("#price_"+id).val();
			var $quant = $("#quantity_"+id).val();
			$price = parseFloat($price);
			$quant = parseInt($quant);
			if (isNaN($price) || isNaN($quant)) {
				$("#rowTotal_"+id).html("0");
				updateTotals();
				return;
			};
			var $rowTotal = $price * $quant;
			$("#rowTotal_"+id).html($rowTotal.toFixed(2));
			updateTotals();
	});
	

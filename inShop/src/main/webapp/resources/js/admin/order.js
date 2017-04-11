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
  function renderStatus(data, type, full, meta){
		var obj = $.parseJSON(data);
		var ret =
		  	"<span class='order-status-"+obj.status.toLowerCase()+"'>"+obj.label+ 
			"</span>"		  	  
	    return ret;
  }

  /*function updateTotals() {
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
	}*/
  
	$(function() {
		var urlPar = $.urlParam("status");
		$("#oDataTable").DataTable( {
	     	"language": dataTableI18nObject,
	     	"autoWidth": false,
	    	"searchDelay": 500,
	    	"stateSave": true,
	        "processing": true,
	        "serverSide": true,
	        "ajax": root+"order-ajax-load"+(urlPar != null && urlPar != "undefined"? "?status="+urlPar: ""),
	        "order": [[ 1, "desc" ]],
	        "columnDefs": [
			{
			    "targets": 2,
			    "className": "mightOverflow domain-col"
			 },	                       
			 {
			    "targets": 3,
			    "className": "mightOverflow domain-col"
			 },	                       
			 {
			    "targets": 5,
			    "className": "mightOverflow domain-col",
			    "render": renderStatus
			 },	                       
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
                    $("#order-submit-date-input").pickmeup_twitter_bootstrap({
                    	  locale		 : "ru",
                    	  format         : 'd.m.Y',
                    	  position       : "right",
                    	  hide_on_select : true
                    	});
                    makeSkuSelect2 ($("#slSearchSku"), ajaxProductSearch);
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
               url: addSkuItem,
               data: formser+"&skuId="+id,
               cache: false
           }).done(function (html) {
        	   $("#dOrderItemTable").html(html);
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
               url: delSkuItem,
               data: formser+"&ID="+id,
               cache: false
           }).done(function (html) {
        	   $("#dOrderItemTable").html(html);
           });
	});	    			    		
	$("body").on("input", ".row-total-changer", function (event) {
		var $val = $(this).val();
		if ($val=="" || isNaN($val) || parseInt($val) <= 0) return;
		var form = $("#oeform");
		var formser;
		if (typeof form != "undefined" && form != null) formser = form.serialize();
           $.ajax({
           	   type: "POST",
               url: updateOrder,
               data: formser,
               cache: false
           }).done(function (html) {
        	   $("#dOrderItemTable").html(html);
           });
	});
	

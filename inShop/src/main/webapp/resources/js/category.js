$(function(){
    $("#categoryDataTable").treegrid({
		treeColumn: 1, 
		saveState: true,
	}); 
    $("body").on("change", "#productAttributes", function(e){
		$("#productAttributesIc").find("input").each(function(i,v){$(v).remove()});    		 			
	        $(this).find("option:selected").each(function(i, v){
	          $("<input>").attr({
	        	    type: "hidden",
	        	    name: "attributesForForm["+i+"]",
	        	    value: $(v).val()
	        	}).appendTo("#productAttributesIc");								    		 		            
	        });     		 			
	
     });
     //Открытие окна удаления
     $("body").on("click", ".open-deleteDialog", function () {
	     var ctgId = $(this).data("id");
	     var ctgName = $(this).data("name");
	     $("#delete-modal-footer #categoryID").val(ctgId);
	     $("#delete-modal-body #ctgName").html(ctgName);
	 });
     //Открытие окна редактированя
     $("body").on("click", ".open-editDialog", function () {
    	 var path = $(this).data("path");
 	     $("#editModalContent").text("");
         $.ajax({
             url: path ,
             cache: false
           }).done(function (html) {
                $("#editModalContent").append(html);
                $("#summernote").summernote({
				    lang: "ru-RU"
				     });
                $(".selectpicker").selectpicker();
                $("#productAttributes").change();
                $("#name").focus();
           });
     });
     $("body").on("mouseenter", ".mightOverflow", function() { 
       	 var $t = $(this); 
    	 var title = $t.attr("title");
    	 if (!title){ if (this.offsetWidth < this.scrollWidth) $t.attr("title", $t.text()) } 
    	 else { if (this.offsetWidth >= this.scrollWidth && title == $t.text()) $t.removeAttr("title")}
	 });
	 $("body").on("click", ".checkCategory", function() {
	 	 var checkId = $("#categoryDataTable").data("checkid");
		 var thisId = $(this).attr("value");
		 var isChecked = $(this).prop("checked"); 
		 if (thisId==checkId) {
			$("#categoryDataTable").data("checkid","");
			$("#main-panel").css("max-height","");
			$("#main-panel").css("overflow","");
			$(".treegrid-"+thisId).removeClass("info");
			return;
		 }
		 $("#check-"+checkId).prop("checked", false);
		 $(".treegrid-"+checkId).removeClass("info");
		 $("#categoryDataTable").data("checkid",thisId);
		 $("#main-panel").css("max-height","35vh");
		 $("#main-panel").css("overflow","auto");
		 $(".treegrid-"+thisId).addClass("info");
	     if ($("#main-panel").hasScrollBar()) $("#main-panel").scrollTop($(".treegrid-"+thisId).offset().top - ($("#main-panel").height()/2) );
			
	 });
	 $("body").on("click", "#urlRefreshBtn", function() {
		 $("#url").val(slugify($("#name").val()));
	 });
     
});
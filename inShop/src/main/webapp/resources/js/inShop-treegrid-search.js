/*
 * Поиск для treegrid jquery boostrap
 * 
 */
     $(document).on("click", "#clearSearch", function() {
		    $("#searchTerm").val("");
   		    $("#searchTerm").attr("placeholder","");
    	    $('#categoryDataTable').find('tr').each(function(){
            var row = $(this)[0];
            if (row.rowIndex < 2) return true;
        	for (var i=0; i < row.cells.length; i++) {
	        		$(this).css("background", ""); 
        			$(row.cells[i]).find("span").each(function(){ 
        				if ($(this).hasClass("for-border")) { 
        					$(this).html($(this).html().replace("<mark>", ""));
        					$(this).html($(this).html().replace("</mark>", ""));
        				}
        			   });
	        	  } 		              
    	   });
       });
      $(document).on("keyup", "#searchTerm", function(event) {
	    	if (event.which == 13){
	          $("#doSearch").trigger("click");
	       }  
	      });
      $(document).on("click", "#doSearch", function() {
    	    var searchText = $("#searchTerm").val();
        	$('#categoryDataTable').find('tr').each(function(){
              var row = $(this)[0];
              if (row.rowIndex < 2) return true;
        	  for (var i=0; i < row.cells.length; i++) {
        		  $(this).css("background", "");
        		  $(row.cells[i]).find("span").each(function(){ 
        				if ($(this).hasClass("for-border")) { 
        					$(this).html($(this).html().replace("<mark>", ""));
        					$(this).html($(this).html().replace("</mark>", ""));
        				}
        		  });
        		  if ($(row.cells[i]).text().indexOf(searchText)>=0) {
          			$(this).css("background", "#ffec82");
          			$(row.cells[i]).find("span").each(function(){ 
          				if ($(this).hasClass("for-border")) { 
          					 $(this).html($(this).html().replace(searchText, "<mark>"+searchText+"</mark>"));
          				}
          			   });
          			var parent = $(this).treegrid("getParentNode");
          			if (parent != null) checkNode(parent);
          		  } 
        	  }
        	});
        	$("#searchTerm").attr("placeholder",searchText);
        }); 		      
      function checkNode(node) {
    	  $(node).treegrid('expand');
    	  var parent = $(node).treegrid('getParentNode');
    	  if (parent != null) checkNode(parent);
      }
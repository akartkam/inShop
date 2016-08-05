/*
 * Поиск для treegrid jquery boostrap
 * 
 */
	 function clearSearchableDataTable() {
	    $(".searchableDataTable span.for-search:has(mark)").each(function () {
	        var col = $($(this)[0]);
	        col.css("background", "");
	        col.html(col.html().replace("<mark>", ""));
	        col.html(col.html().replace("</mark>", ""));
	    });
	 }
	 
	 $(document).on("click", "#clearSearch", function () {
		    $("#searchTerm").val("");
		    $("#searchTerm").attr("placeholder", "");
		    clearSearchableDataTable();
		    return false;
		});	 
     $(document).on("keyup", "#searchTerm", function(event) {
	    	if (event.which == 13){
	          $("#doSearch").trigger("click");
	       }  
	 });
     
     $(document).on("click", "#doSearch", function () {
    	    var searchText = $("#searchTerm").val();
    	    if (searchText === "") return true;
    	    clearSearchableDataTable();
    	    var pos;
    	    var col;
    	    var st;
    	    var idx;
    	    var tr;
    	    $(".searchableDataTable span.for-search").filter(function () { return $(this).text().toLowerCase().indexOf(searchText.toLowerCase()) > -1 }).each(function (index) {
    	        col = $(this).closest('td');
    	        tr = col.closest('tr');
    	        if (tr.index() < 1) return true;
    	        if (index == 0) {
    	            window.scrollTo(0, col.position().top);
    	        }
    	        col.css("background", "#ffec82");
    	        idx = col.text().toLowerCase().indexOf(searchText.toLowerCase());
    	        st = col.text().substr(idx, searchText.length);
    	        col.html(col.html().replace(st, "<mark>" + st + "</mark>"));
      			var parent = $(tr).treegrid("getParentNode");
      			if (parent != null) checkNode(parent);
    	    });
    	    $("#searchTerm").attr("placeholder", searchText);
    	    return false;
    	});     
     
      function checkNode(node) {
    	  $(node).treegrid('expand');
    	  var parent = $(node).treegrid('getParentNode');
    	  if (parent != null) checkNode(parent);
      }
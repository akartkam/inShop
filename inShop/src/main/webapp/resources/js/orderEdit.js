function formatRepo (repo) {
      if (repo.loading) return repo.text;
      var markup = "<div class='select2-result-repository clearfix'>" +
       "<div class='a-photo-out'>"+
        "<div class='photo-block'><img src='" + (repo.images[0]) + "' /></div></div>" +
        "<div class='select2-result-repository__meta'>" +
          "<div class='select2-result-repository__title'>" + repo.name + "</div>";
      markup += "<div class='select2-result-repository__description'>" + (repo.code ? "Артикул:" + repo.code : "") + 
    		    (repo.optionValues.length ? (repo.code ? "&nbsp;<span style='display: inline-block; color: #aaa; font-size: 11px;'>&#9679;</span>&nbsp;": "")  + 
    		    "Вариант:" + repo.optionValues.join()  : "") + 
    		    (repo.quantityAvailable ? (repo.optionValues.length ? "&nbsp;<span style='display: inline-block; color: #aaa; font-size: 11px;'>&#9679;</span>&nbsp;" : "" )  +
    		    "Остаток:" + repo.quantityAvailable  : "") +
    		    "</div>";
      markup += "<div class='select2-result-repository__additional-info'>" +
        "<div class='select2-result-repository__additional'><i class='fa fa-rub'></i> " + (repo.salePrice ? "<span style='text-decoration: line-through;'>"+repo.retailPrice.toFixed(2)+"</span> <span>"+ repo.salePrice.toFixed(2) +"</span>" : repo.retailPrice.toFixed(2))+"</div>"
      if (repo.model) {  
    	  markup += "<div class='select2-result-repository__additional'><i class='fa fa-gears'></i> " + repo.model + "</div>"
      }
      if (repo.brand) {  
    	  markup += "<div class='select2-result-repository__additional'><i class='fa fa-star-o'></i> " + repo.brand + "</div>"
      }
      for(i=0;i<repo.productStatus.length;i++){  
      	markup += "<div class='select2-result-repository__additional'><i class='"+repo.productStatus[i].productStatusIcon+"'></i> " + repo.productStatus[i].productStatusDisplayName + "</div>"
	  }
	  markup += "</div></div></div>";
      
      return markup;
 }

 function formatRepoSelection (repo) {
      return repo.name;
 }
 
 function makeSkuSelect2 ($ajax) {  
	 $ajax.select2({
	   width: '100%',
	   ajax: {
	        url: /*[[@{/order/product-search}]]*/'',
	        dataType: 'json',
	        delay: 250,
	        data: function (params) {
	          return {
	            q: params.term, // search term
	            page: params.page
	          };
	        },
	        processResults: function (data, params) {
	          params.page = params.page || 1;
	          return {
	            results: data.items,
	            pagination: {
	              more: (params.page * 30) < data.itemsCount
	            }
	          };
	        },
	        cache: true
	      },
	      escapeMarkup: function (markup) { return markup; },
	      minimumInputLength: 3,
	      templateResult: formatRepo,
	      templateSelection: formatRepoSelection
	 });
 }
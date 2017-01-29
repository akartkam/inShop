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
        "<div class='select2-result-repository__additional'><i class='fa fa-rub'></i> " + (repo.salePrice ? "<span style='text-decoration: line-through;'>"+repo.retailPrice.toFixed(2)+
        "</span>&nbsp;<span>"+ repo.salePrice.toFixed(2) +"</span>" : repo.retailPrice.toFixed(2))+pricePieceUnitDelemiter+pricePieceUnit+
        (repo.priceForPackage != repo.retailPrice && repo.priceForPackage != repo.salePrice ? "&nbsp;&nbsp;<span>"+repo.priceForPackage.toFixed(2)+
        pricePieceUnitDelemiter+pricePackageUnit+"</span>":"")+
        "</div>"
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
	  if (repo.id == "") return repo.text;
	  var price = "", retailPrice = "", salePrice = "";
	  var sku="";
	  if (repo.optionValues && repo.optionValues.length) {
		sku ="<span style='font-size: 8pt;'>"+repo.optionValues.join()+"</span>"; 
	  }
	  if(repo.retailPrice) {
		  retailPrice = "<span style='font-size: 8pt;'>"+repo.retailPrice.toFixed(2)+"</span>";
		  price = retailPrice;
		  if (repo.salePrice) {
			  retailPrice = "<span style='font-size: 8pt; text-decoration: line-through;'>"+repo.retailPrice.toFixed(2)+"</span>";
			  salePrice = retailPrice + " <span style='font-size: 8pt;'>"+repo.salePrice.toFixed(2)+"</span>"; 
			  price = salePrice;
		  }
	  }
	  if (price != "" && sku != "") price = " | " + price;
      return repo.name+"("+sku+price+")" ;
 }
 
 function makeSkuSelect2 ($ajax, $url) {  
	 $ajax.select2({
	   width: "100%",
	   placeholder: "Введите артикул или наименование товара для поиска",
	   allowClear: true,
	   ajax: {
	        url: $url,
	        dataType: "json",
	        delay: 250,
	        data: function (params) {
	          return {
	            q: params.term || "", // search term
	            page: params.page || 1
	          };
	        },
	        processResults: function (data, params) {
	          params.page = params.page || 1;
	          return {
	            results: data.items,
	            pagination: {
	              more: (params.page * 20) < data.totalItemsCount
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
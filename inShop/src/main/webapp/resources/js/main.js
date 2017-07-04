jQuery(document).ready(function($){
    
	    // jQuery sticky Menu
	    
		//$(".mainmenu-area").sticky({topSpacing:0});
		if (window.innerWidth >= 1000) {
			$(".sticky-menu").stick_in_parent({parent : $("body")});
			$(".sidemenu-area").stick_in_parent({
			                               parent : $(".sidemenu-sticky-parent"), 
										   offset_top : $(".navbar-sidebar-title").height() + $(".mainmenu-area").height() - 10
										   });
		}
	    
	    $('.product-carousel').owlCarousel({
	        loop:true,
	        nav:true,
	        navText:["<",">"],
	        margin:30,
	        responsive:{
	            0:{
	                items:1,
	            },
	            600:{
	                items:3,
	            },
	            1200:{
	                items:4,
	            },
	            1500:{
	            	items:5
	            }
	        }
	    });  
	    
	    $('.related-products-carousel').owlCarousel({
	        loop:true,
	        nav:true,
	        navText:["<",">"],
	        margin:20,
	        responsive:{
	            0:{
	                items:1,
	            },
	            600:{
	                items:2,
	            },
	            1000:{
	                items:2,
	            },
	            1200:{
	                items:3,
	            }
	        }
	    });  
	    
	    $('.brand-list').owlCarousel({
	        loop:true,
	        nav:true,
	        navText:["<",">"],
	        margin:20,
	        responsive:{
	            0:{
	                items:1,
	            },
	            600:{
	                items:3,
	            },
	            1000:{
	                items:4,
	            }
	        }
	    });    
	    
	    
	    // Bootstrap Mobile Menu fix
	    /*$(".navbar-nav li a").click(function(){
	        $(".navbar-collapse").removeClass('in');
	    });*/    
	    
	    // jQuery Scroll effect
	    $('.navbar-nav li a, .scroll-to-up').bind('click', function(event) {
	        var $anchor = $(this);
	        var headerH = $('.header-area').outerHeight();
	        $('html, body').stop().animate({
	            scrollTop : $($anchor.attr('href')).offset().top - headerH + "px"
	        }, 1200, 'easeInOutExpo');
	
	        event.preventDefault();
	    });    
	    
	    // Bootstrap ScrollPSY
	    $('body').scrollspy({ 
	        target: '.navbar-collapse',
	        offset: 95
	    }) 
		
		// Toggel the class "active" for main menubar
		$('.sidemenu-area ul.navbar-nav li, .addmainmenu-area ul.navbar-nav li').click(function(){
			$('.sidemenu-area ul.navbar-nav li, .mainmenu-area ul.navbar-nav li').removeClass("active");
			$(this).addClass("active");
		});
	    
	
	    //Quick review product zoom and galary
	    
		/*$("body").on("click", "#quick-review-product-zoom", function(e) {  
		  var ez =   $('#quick-review-product-zoom').data('elevateZoom');
		  ez.closeAll(); 
		  $("#lightbox-container a").each(function(){$(this).remove()});
		  $(ez.getGalleryList()).each(function(){
	        	$("<a>").attr({
	        	    href: this.href,
	        	    "data-lightbox": "lightbox-container",
	        	}).appendTo("#lightbox-container");				  
		  });
		  $("#lightbox-container a").eq(0).trigger("click");
		  return false;
		});*/
		
		$("body").on("click", ".quick-review-product-link", function(e)
		{
			e.preventDefault();
	        $.ajax({ url: $(this).data("path") }).done(function(data) {
	            $.modal(data, { maxWidth: 600, maxHeight: 450, minHeight: 350});
	        	$("#simplemodal-container").css("height", "auto");
	        	$.modal.update();
	        	$(".quick-review-product-zoom").imagezoomsl({ 
	        			magnifiersize: [200, 250],
	                   	zoomrange: [3,8],
	                   	zoomstart: 4
	        	});
	        	//$("#quick-review-product-zoom").elevateZoom({gallery:"quick-review-product-gallery", cursor: 'pointer', scrollZoom: "true", 
	            //       responsive: "true", galleryActiveClass: "active", easing : true}); 
	            
	        });
	
	        return false;				
		});
		
		$("body").on("click", ".view-details-link", function(e)
				{
			      window.location.href=$(this).attr("href");
			
				}
		);
		
		$("body").on("click", ".imzoom-gallery", function() {
	        var that = this;
	        if ($(this).hasClass("active")) return;
	        $(".gallery img.imzoom-gallery").removeClass("active");
	        $(this).addClass("active");
	        //копируем атрибуты из превью-картинки в контейнер-картинку
	        $(".quick-review-product-zoom").fadeOut(200, function(){
	            $(this).attr("src",              $(that).attr("src"))              // путь до small картинки
	                   .attr("data-large",       $(that).attr("data-large"))       // путь до big картинки
	                   //дополнительные атрибуты, если есть
	                   //.attr("data-title",       $(that).attr("data-title"))       // заголовок подсказки
	                   //.attr("data-help",        $(that).attr("data-help"))        // текст подсказки    
	                   //.attr("data-text-bottom", $(that).attr("data-text-bottom")) // текст снизу картинки
	                   .fadeIn(300);				
	          });		
		}); 
		
		//to prevent move on carusel
		$("body").on("mousemove", ".product-carousel a.add-to-cart-link, " +
				                  ".product-carousel a.view-details-link, " +
				                  ".product-carousel a.quick-review-product-link", 
	       function(e) {
			e.preventDefault();
			return false;
		   }
		);
		
		//Show/Hide buttons on small product image
		$("body").on("click", ".product-f-image", function(e) {
		    e.preventDefault();
			$(this).find("a.add-to-cart-link").css("top", "5%");
		    $(this).find("a.view-details-link").css("bottom", "5%");
		    $(this).find("a.quick-review-product-link").css("left", "10%");
			return false;
		});
	
		$("body").on("mouseleave",".product-f-image", function(e) {
			$(this).find("a.add-to-cart-link").css("top", "-50%");
		    $(this).find("a.view-details-link").css("bottom", "-50%");
		    $(this).find("a.quick-review-product-link").css("left", "-80%");
			return false;
		});
		
		$("body").on("change", "select.sort-items", function(e){
			var sort_selector = $(this).data("sort-selector");
			if ($(sort_selector).length) {
				var sort_options = $(this).val();
				var options = $.parseJSON(sort_options);
				//options = $.extend(options, {charOrder:"абвгд[её]жз[ий]клмнопрстуфхцчшщъыьэюя"});
				tinysort(sort_selector, options);
				$("body").trigger("sort-have-performed");
			}
		});
		
		$("body").on("change", ".product-option-values", function() {
	        changeProductOption();
	        return false;
	    });	

    });

    function updateCurrentImage() {
    	
    };
    
    function updatePriceDisplay() {
        var selectedProductOptions = getSelectedProductOptions();
        var productOptionPricing = getPricingData();
        var priceForUnit;
        var priceForUnitOld;
        var priceForPkg;
        
        for (var i = 0; i < productOptionPricing.length; i++) {
            var pricing = productOptionPricing[i];
            if ($(pricing.selectedOptions).not(selectedProductOptions).length == 0 && $(selectedProductOptions).not(pricing.selectedOptions).length == 0) {
            	priceForUnit = pricing.priceForUnit;
            	priceForUnitOld = pricing.priceForUnitOld;
            	priceForPkg = pricing.priceForPkg;
                break;
            }
        }
        
        if (priceForUnit) {
            var $priceForUnit = $('ins#priceForUnit');
            if ($priceForUnit.length != 0) {
            	$priceForUnit.text(priceForUnit);
            }
        }

        var $priceForUnitOld = $('del#priceForUnitOld');
        if (priceForUnitOld) {
            if ($priceForUnitOld.length != 0) {
            	$priceForUnitOld.text(priceForUnitOld);
            }
        } else if ($priceForUnitOld.length) {
        	$priceForUnitOld.text("");
        }
 
        if (priceForPkg) {
            var $priceForPkg = $('ins#priceForPkg');
            if ($priceForPkg.length != 0) {
            	$priceForPkg.text(priceForPkg);
            }
        }        
        
    }
    
    
    function updateAdditSkuAttr() {
        var selectedProductOptions = getSelectedProductOptions();
        var additSkuAttrValues = getAdditSkuAttrValuesData();
        if (!additSkuAttrValues || !additSkuAttrValues.length) return ;
        for (var i = 0; i < additSkuAttrValues.length; i++) {
            var additSkuAttrVal = additSkuAttrValues[i];
            if ($(additSkuAttrVal.selectedOptions).not(selectedProductOptions).length == 0 && $(selectedProductOptions).not(additSkuAttrVal.selectedOptions).length == 0) {
            	$(".addit-sku-attr-value").each(function(){
            	  var attrId = $(this).attr("id").replace("attr_","");
            	  var attrArr = additSkuAttrVal.attr[attrId]; 
            	  var attrVal = attrArr[0];
            	  var attrUnit = "";
            	  if (attrArr.length > 1) attrUnit = attrArr[1];
            	  $(this).html(attrVal);
            	  $("#attr_unit_"+attrId).html(attrUnit);
            	});
            	break;
            }
        }
        return ;
    }
	
	function changeProductOption() {
        updateCurrentImage();
        updatePriceDisplay();	
        updateAdditSkuAttr()
	};
	
	function getSelectedProductOptions() {
        var ret = [];
        $.each($(".product-option-values option:selected"), function(){            
        	ret.push($(this).data("id"));
        });
        return ret;
	}
	
	function getAdditSkuAttrValuesData() {
		return $("#sku-attr-data").data("sku-attr");
	}
	
    function getPricingData() {
        return $("#po-data").data("po-pricing");
    };
    
    function initProductPageContent(){
		  $(".product-f-image img").lazyload({
		        event : "turnPage",
		        effect : "fadeIn"
		  });				
			
		  var intemPerPage=4;
		  if(window.innerWidth > 600) { 
			  intemPerPage = 8;
		  }; 
		  if (window.innerWidth > 1200) { 		  
			  intemPerPage = 12;
		  }; 
		  if (window.innerWidth > 1500) {
			  intemPerPage = 15;
		  };					
		  $("div.pagination-holder").jPages({
		    containerID : "pagination-container",
		    perPage     : intemPerPage,
		    previous	: "«",
		    next		: "»",
		    callback    : function( pages, items ){
				            items.showing.find("img").trigger("turnPage");
				            /*items.oncoming.find("img").trigger("turnPage");*/
				          }			    
		  });
		  $("body").on("sort-have-performed", function(){
			  $(".product-f-image img").trigger("turnPage");  
		  });
		  /* To correct display single product after sort*/
		  setTimeout(function(){$(".product-list-container>div.product-col").equalHeights()}, 800);
    	
    };
    
    $("body").on("click", ".drop-filter", function(){
    	var url = $(this).data("path");
    	if (url != null && url != "undefined") window.location=url;
    });
    
    
    
    
    $("body").on("click", ".apply-filter", function(){
    	var $form = $(this).closest("form");
    	var $url = $form.attr("action");
    	var token = $form.find("input[name=_csrf]");
    	if ($form.length) $form = $form.serialize();
    	if (token.length) token = token.val();
    	var header = $('#_csrf_header').attr('content');
    	$.ajax({
            url: $url,
            type: "POST",
            data: $form,
            cache: false,
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            }           
          }).done(function (data){
        	  $("#filtered-content").html(data);
        	  initProductPageContent();
          }).fail(function() {
      	    alert(defaultClientnErrorMessage);
          });
      return false;    	
   	});


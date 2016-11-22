jQuery(document).ready(function($){
    
    // jQuery sticky Menu
    
	//$(".mainmenu-area").sticky({topSpacing:0});
	$(".sticky-menu").stick_in_parent({parent : $("body")});
	$(".sidemenu-area").stick_in_parent({
	                               parent : $(".sidemenu-sticky-parent"), 
								   offset_top : $(".navbar-sidebar-title").height() + $(".mainmenu-area").height() - 10
								   });
    
    $('.product-carousel').owlCarousel({
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
                items:5,
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
	$('.sidemenu-area ul.navbar-nav li, .mainmenu-area ul.navbar-nav li').click(function(){
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
	
	$("body").on('click', '.quick-review-product-link', function(e)
	{
		e.preventDefault();
        $.ajax({ url: $(this).attr('href') }).done(function(data) {
            $.modal(data, { maxWidth: 600, maxHeight: 300});
        	$("#simplemodal-container").css('height', 'auto');
        	$.modal.update();
            $("#quick-review-product-zoom").elevateZoom({gallery:"quick-review-product-gallery", cursor: 'pointer', scrollZoom: "true", 
                   responsive: "true", galleryActiveClass: "active", easing : true}); 
            
        });

        return false;				
	});
	
	
	//Show/Hide buttons on small product image
	$("body").on("click", ".product-f-image", function(e) {
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


});


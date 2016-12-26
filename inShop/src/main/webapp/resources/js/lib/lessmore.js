/*!
 * dolessmore jQuery plugin
 * author: justdial
 * Licensed : MIT license
 * Dependancy : bootstrap css
 */


// the semi-colon before the function invocation is a safety 
// net against concatenated scripts and/or other plugins 
// that are not closed properly.
;(function ( $, window, document, undefined ) {
    
    // undefined is used here as the undefined global 
    // variable in ECMAScript 3 and is mutable (i.e. it can 
    // be changed by someone else). undefined isn't really 
    // being passed in so we can ensure that its value is 
    // truly undefined. In ES5, undefined can no longer be 
    // modified.
    
    // window and document are passed through as local 
    // variables rather than as globals, because this (slightly) 
    // quickens the resolution process and can be more 
    // efficiently minified (especially when both are 
    // regularly referenced in your plugin).

    // Create the defaults once
    var pluginName = 'dolessmore',
        defaults = {            
			lHeight : 100
        };

    // The actual plugin constructor
    function Plugin( element, options ) {
        this.element = element;

        // jQuery has an extend method that merges the 
        // contents of two or more objects, storing the 
        // result in the first object. The first object 
        // is generally empty because we don't want to alter 
        // the default options for future instances of the plugin
        this.options = $.extend( {}, defaults, options) ;
        
        this._defaults = defaults;
        this._name = pluginName;
		this.autoHeight =  $(this.element).height();
		
		//init call
        this.init();
    }

    Plugin.prototype.init = function () {
        // Place initialization logic here
        // You already have access to the DOM element and
        // the options via the instance, e.g. this.element 
        // and this.options
		var el = this.element;
		
		if(this.autoHeight > this.options.lHeight){
			$(el)
				.addClass('dolessmoreblock')
				.addClass('dlmcontract')
				.height(this.options.lHeight)
				.append('<div class="lm-control"><a href="javascript:void(0)" class="glyphicon glyphicon-menu-down"></a></div>');
			
			this.lmControl(this.autoHeight);
		}
				
    };
	
	Plugin.prototype.lmControl = function(autoHeight){
		var el = this.element,
			lH = this.options.lHeight;	
		$(el).find('.lm-control a').click(function(){
			
			
			if($(this).hasClass('lmcontract')){ //do less
				$(this)
					.removeClass('lmcontract')
					.removeClass('glyphicon-menu-up')
					.addClass('glyphicon-menu-down');				
				$(el)
					.animate({'height':lH},300)					
					.addClass('dlmcontract')
					.removeClass('dlmexpand');
				
			}else{ //do more
				$(this)
					.addClass('lmcontract')
					.removeClass('glyphicon-menu-down')
					.addClass('glyphicon-menu-up');
				
				$(el)
					.animate({'height':autoHeight},300,function(){$(this).css({'height':'auto'})})					
					.addClass('dlmexpand')
					.removeClass('dlmcontract');
			}			
		});
	}	

    // A really lightweight plugin wrapper around the constructor, 
    // preventing against multiple instantiations
    $.fn[pluginName] = function ( options ) {
        return this.each(function () {
            if (!$.data(this, 'plugin_' + pluginName)) {
                $.data(this, 'plugin_' + pluginName, 
                new Plugin( this, options ));
            }
        });
    }

})( jQuery, window, document );
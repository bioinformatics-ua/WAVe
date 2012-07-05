jQuery.fn.fadeSlideShow = function(options) {
	return this.each(function(){
		settings = jQuery.extend({
     		width: 800,
     		height: 600,
			speed: 1000,
			interval: 10000
	 	}, options);
		
		// set style for wrapper element
		jQuery(this).css({
			width: settings.width,
			height: settings.height,
			position: 'relative',
			overflow: 'hidden'
		});
		
		// set styles for child element
		jQuery('> *',this).css({
			position: 'absolute',
			width: settings.width,
			height: settings.height
		});
		
		// count number of slides
		Slides = jQuery('> *', this).length;
		Slides = Slides - 1;
		ActSlide = Slides;
		
		jQslide = jQuery('> *', this);
		
		intval = setInterval(function(){
			jQslide.eq(ActSlide).fadeOut(settings.speed);
			if(ActSlide <= 0){
				jQslide.fadeIn(settings.speed);
				ActSlide = Slides;
			}else{
				ActSlide = ActSlide - 1;	
			}
		}, settings.interval);
	});
};

jQuery.log = function(message) {
  if(window.console) {
     console.debug(message);
  } else {
     alert(message);
  }
};
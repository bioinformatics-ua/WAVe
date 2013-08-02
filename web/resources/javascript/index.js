$(document).ready(function() {
    // default focus
    $('#hgnc').focus();
    
    // crossfader
    var doFadeIn = function() {
        $('.fadein').css({
            opacity:0,
            visibility:'visible'
        }).fadeTo(500,1);
    };

    $('body').one('mousemove',doFadeIn);
    $('#s').one('blur',doFadeIn);

    // partners boxes
    $('.boxgrid.caption').hover(function(){
        $(".cover", this).stop().animate({
            top:'50px'
        },{
            queue:false,
            duration:160
        });
    }, function() {
        $(".cover", this).stop().animate({
            top:'67px'
        },{
            queue:false,
            duration:160
        });
    });

    // (pseudo)form handling
    $.widget("custom.catcomplete", $.ui.autocomplete, {
        _renderMenu: function( ul, items ) {
            var self = this,
            currentCategory = "";
            $.each( items, function( index, item ) {
                if ( item.category != currentCategory ) {
                    ul.append( "<li class='ui-autocomplete-category'>" + item.category + "</li>" );
                    currentCategory = item.category;
                }
                self._renderItem( ul, item );
            });
        },
        select: function( event, ui ) {
            event.preventDefault();
            window.location.href = "/WAVe/search/" + $("#hgnc").attr("value");
        }
    });
    
    $("#hgnc").catcomplete({
        delay: 500,
        source: '/WAVe/autocomplete',
        select: function( event, ui ) {
            event.preventDefault();
            window.location.href = "/WAVe/search/" + $("#hgnc").attr("value");
        }
    });

    //$("#hgnc").autocomplete('autocomplete');
    $("#submit").click(function(){
        window.location.href = "/WAVe/search/" + $("#hgnc").attr("value");
    });
    $("#hgnc").keypress(function(e){
        if(e.keyCode == 13) {
            window.location.href = "/WAVe/search/" + $("#hgnc").attr("value");
        }
    });
    
    $('.ex').tipsy({
        fade: true,
        gravity: 's'
    });
});
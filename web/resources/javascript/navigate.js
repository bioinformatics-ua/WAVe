$(document).ready(function(){
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

    $(function() {
        var $placeholder = $('input[title]');
        if ($placeholder.length > 0) {
            var attrPh = $placeholder.attr('title');
            $placeholder.attr('value', attrPh)
            .bind('focus', function() {
                var $this = $(this);
                if($this.val() === attrPh)
                    $this.val('').css('color','#171207');

            }).bind('blur', function() {
                var $this = $(this);
                if($this.val() === '')
                    $this.val(attrPh).css('color','#333');
            });
        }
    });


    // mesh control
    $('#mesher').click(function() {
        var mesh = $('#mesh');
        if (mesh.attr('class') === 'hiddenMesh') {
            mesh.attr('class','openMesh');
            mesh.fadeIn('slow', null);
            setTimeout(function() {
                mesh.attr('class','hiddenMesh');
                mesh.fadeOut('slow', null);
            },10000);
        } else if (mesh.attr('class') === 'openMesh') {
            mesh.attr('class','hiddenMesh');
            mesh.fadeOut('slow', null);
        }        
    });

    $('#toolmesh').click(function() {
        var mesh = $('#mesh');
        if (mesh.attr('class') === 'hiddenMesh') {
            mesh.attr('class','openMesh');
            mesh.fadeIn('slow', null);
            setTimeout(function() {
                mesh.attr('class','hiddenMesh');
                mesh.fadeOut('slow', null);
            },10000);
        } else if (mesh.attr('class') === 'openMesh') {
            mesh.attr('class','hiddenMesh');
            mesh.fadeOut('slow', null);
        }
    });

    // sidebar navigation
    $(".toggler").click(function () {
        var width = $(window).width();
        var newWidth = width - 24;
        if ($(this).attr("id") === "opened") {
            $('#mesh').attr('class','hiddenMesh');
            $('#mesh').fadeOut('slow', null);
            $("#content").animate({
                width: newWidth + 'px'
            }, 1000);
            $("#toolbox").animate({
                left: '0.5%'
            }, 1000);
            $("#sidebar").animate({
                width:'toggle'
            },1000);
            $(this).attr("id","closed");
            $(this).attr("title","Contract LiveView");
            $(this).parent().attr("class", "collapse");
            $('.tool').tipsy({
                fade: true,
                gravity: 'w'
            });
        } else if ($(this).attr("id") === "closed") {
            $("#content").animate({
                width: '79%'
            }, 1000);
            $("#toolbox").animate({
                left: '17%'
            }, 1000);
            $("#sidebar").animate({
                width:'toggle'
            },1000);
            $(this).attr("id","opened");
            $(this).attr("title","Expand content view");
            $(this).parent().attr("class", "expand");
            $('.tool').tipsy({
                fade: true,
                gravity: 'e'
            });
        }
    });
    // tree
    $("#tree").treeview({
        animated: "medium",
        control:"#sidetreecontrol",
        collapsed: true,
        persist: "cookie"
    });
    // frame content
    $('.frame').click(function() {
        $('#external').attr("href", $(this).attr("value"));
        $("#frame").attr("src", $(this).attr("value"));
        $('#frame').css("background-color", "#FFFFFF");
    });    
    $('.frame').tipsy({
        fade: true,
        gravity: 'w'
    });
    $('.tool').tipsy({
        fade: true,
        gravity: 'e'
    });
});
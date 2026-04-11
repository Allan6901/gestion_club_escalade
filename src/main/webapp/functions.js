function showStack() {
    var base = ($('<a href=".">')[0].href);
    $.ajax({
        type : 'GET',
        url : (base + "calculator/show"),
        data : '200',
        timeout : 3000,
        success : function(data) {
            $('#numbers').hide();
            $('#numbers').html("");
            jQuery.each(data, function(i, val) {
                $("#numbers").append(val + ", ");
            });
            $('#numbers').show();
        }
    });
}

function show() {
    showStack();
    $('#message').html("");
}

function add() {
    var base = ($('<a href=".">')[0].href);
    $.ajax({
        type : 'GET',
        url : (base + "calculator/add"),
        timeout : 3000,
        error : function() {
            $('#message').html('Addition impossible');
            showStack();
        },
        success : function(data) {
            $('#message').html('Addition r&eacute;alis&eacute;e');
            showStack();
        }
    });
}

function put() {
    var base = ($('<a href=".">')[0].href);
    var value = ($('#input').val());
    $.ajax({
        type : 'POST',
        url : (base + "calculator/put"),
        data : value,
        timeout : 3000,
        dataType : "text",
        contentType : "application/json",
        success : function(data) {
            showStack();
            $('#message').html('donn&eacute;e ajout&eacute;e.');
            $('#input').val("");
        },
        error : function() {
            showStack();
            $('#message').html('donn&eacute;e invalide.');
            $('#input').val("");
        }
    });

function sub() {
    var base = ($('<a href=".">')[0].href);
    $.ajax({
        type : 'GET',
        url : (base + "calculator/sub"),
        timeout : 3000,
        error : function() {
            $('#message').html('Soustraction impossible');
            showStack();
        },
        success : function(data) {
            $('#message').html('Soustraction reussite');
            showStack();
        }
    });
}
}
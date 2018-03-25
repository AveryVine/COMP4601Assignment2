$(document).ready(function() {

    let baseURL = "http://localhost:8080/COMP4601-RS/rest/rs";

    function reset(dir) {
        $.get(baseURL + "/reset/" + dir, function(data) {
            alert("Reset action performed: " + data.toString());
        });
    }

    function context() {
        $.get(baseURL + "/context", function(data) {
            alert("Context action performed: " + data.toString());
        });
    }

    function community() {
        $.get(baseURL + "/community", function(data) {
            alert("Community action performed: " + data.toString());
        });
    }

    function fetch() {
        $.get(baseURL + "/fetch", function(data) {
            alert("Fetch action performed: " + data.toString());
        });
    }

    function advertising() {
        $.get(baseURL + "/advertising", function(data) {
            alert("Advertising action performed: " + data.toString());
        });
    }

});
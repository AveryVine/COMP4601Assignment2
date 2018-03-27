$(document).ready(function() {

    let baseURL = "http://localhost:8080/COMP4601-RS/rest/rs";

    $("#resetTraining").on('click', function() {
        reset("training");
    });

    $("#resetTesting").on('click', function() {
        reset("testing");
    })

    function reset(dir) {
        $.get(baseURL + "/reset/" + dir, function(data) {
            alert("Reset action performed: " + data.toString());
        });
    }

    $("#context").on('click', function() {
        $.get(baseURL + "/context", function(data) {
            console.log("Context action performed: " + data.toString());
            $("html").html(data);
        });
    });

    $("#community").on('click', function() {
        $.get(baseURL + "/community", function(data) {
            alert("Community action performed: " + data.toString());
        });
    });

    $("#fetch").on('click', function() {
        $.get(baseURL + "/fetch", function(data) {
            alert("Fetch action performed: " + data.toString());
        });
    });

    $("#advertising").on('click', function() {
        $.get(baseURL + "/advertising", function(data) {
            alert("Advertising action performed: " + data.toString());
        });
    });

});
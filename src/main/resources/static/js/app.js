var stompClient = null;
var userName = "";

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect(id) {
    var socket = new SockJS('/secured/room');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log(stompClient.ws);

        setConnected(true);
        console.log('Connected: ' ,frame, frame.headers['user-name']);
        userName = frame.headers['user-name'];
        stompClient.subscribe('/game/'  + id + '/notification', function (greeting) {
        // stompClient.subscribe('/secured/user/' + userName + '/secured/user/queue/specific-user/notification', function (greeting) {
            //showGreeting(JSON.parse(greeting.body).content);
            console.log('receive!', greeting);
            // abort("受信");
            window.location.href = "/game/show?id=" + id;
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName(selectBlock, x, y, angle, id, pass) {
    stompClient.send("/spring-security-mvc-socket/oku", {},
                     JSON.stringify({'selectBlock': selectBlock,
                                    'x': x,
                                    'y': y,
                                    'angle': angle,
                                    'id': id,
                                    'pass': pass,}
                                    ));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});

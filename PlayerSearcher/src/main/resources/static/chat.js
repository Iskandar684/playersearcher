var stompClient = null;
var isConnected = false;

function setConnected(connected) {
	isConnected = connected;
	if (connected) {
		$("#conversation").show();
	}
	else {
		$("#conversation").hide();
	}
	$("#conversationBody").html("");
}

function connect() {
	var socket = new SockJS('/playersearcher-websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/private', function(greeting) {
			showGreeting(JSON.parse(greeting.body).content);
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

function sendName() {
	if (!isConnected) {
		connect();
	}
	stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({ 'content': $("#name").val() }));
}

function showGreeting(message) {
	$("#conversationBody").append("<tr><td>" + message + "</td></tr>");
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#send").click(function() { sendName(); });
	$(document).ready(function() {
		console.log("ready!");
		connect();
	});
});


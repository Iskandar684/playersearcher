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
	var currentLogin = $('#login').html();
	console.log('currentLogin ' + currentLogin);
	var socket = new SockJS('/playersearcher-websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/private/' + currentLogin, function(message) {
			var mess = JSON.parse(message.body);
			showMessage(mess.senderAndContent);
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

function sendMessage() {
	if (!isConnected) {
		connect();
	}
	var recipientLogin = $('#recipient').html();
	console.log('recipientLogin ' + recipientLogin);
	stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({ 'content': $("#message").val(), 'recipientLogin': $("#recipient").html() }));
	$("#message").val("")
}

function showMessage(message) {
	$("#conversation").append("<tr><td>" + message + "</td></tr>");
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#send").click(function() { sendMessage(); });
	$(document).ready(function() {
		console.log("ready!");
		connect();
	});
});


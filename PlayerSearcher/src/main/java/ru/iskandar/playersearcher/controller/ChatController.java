package ru.iskandar.playersearcher.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.extern.java.Log;
import ru.iskandar.playersearcher.model.ChatMessage;
import ru.iskandar.playersearcher.model.Player;
import ru.iskandar.playersearcher.repo.PlayersRepo;

@Log
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate _messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    // @SendTo("/topic/private")
    public void sendMessage(ChatMessage aMessage, Principal aSenderPrincipal) throws Exception {
        String sender = PlayersRepo.getInstance().findPlayerByLogin(aSenderPrincipal.getName())
                .map(Player::getName).orElseGet(aSenderPrincipal::getName);
        aMessage.setSender(sender);
        String recipientTopicId = String.format("/topic/private/%s", aMessage.getRecipientLogin());
        log.info("send mess recipientTopicId " + recipientTopicId);
        _messagingTemplate.convertAndSend(recipientTopicId, aMessage);

        String senderTopicId = String.format("/topic/private/%s", aSenderPrincipal.getName());
        log.info("send mess senderTopicId " + senderTopicId);
        _messagingTemplate.convertAndSend(senderTopicId, aMessage);
        // return ChatMessage.builder().sender(sender).content(message.getContent()).build();
    }

}

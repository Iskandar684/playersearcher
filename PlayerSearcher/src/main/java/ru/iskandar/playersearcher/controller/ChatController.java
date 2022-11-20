package ru.iskandar.playersearcher.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import ru.iskandar.playersearcher.model.ChatMessage;
import ru.iskandar.playersearcher.model.Player;
import ru.iskandar.playersearcher.repo.PlayersRepo;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/private")
    public ChatMessage sendMessage(ChatMessage message, Principal aPrincipal) throws Exception {
        String sender = PlayersRepo.getInstance().findPlayerByLogin(aPrincipal.getName())
                .map(Player::getName).orElseGet(aPrincipal::getName);
        return ChatMessage.builder().sender(sender).content(message.getContent()).build();
    }

}

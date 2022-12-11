package ru.iskandar.playersearcher.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.java.Log;
import ru.iskandar.playersearcher.model.ChatInfo;
import ru.iskandar.playersearcher.model.ChatMessage;
import ru.iskandar.playersearcher.model.Player;
import ru.iskandar.playersearcher.repo.ChatMessageRepo;
import ru.iskandar.playersearcher.repo.PlayersRepo;

@Log
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate _messagingTemplate;

    @Autowired
    private EmailService _emailService;

    @MessageMapping("/chat.sendMessage")
    // @SendTo("/topic/private")
    public void sendMessage(ChatMessage aMessage, Principal aSenderPrincipal) throws Exception {
        if (StringUtils.isEmpty(aMessage.getContent())) {
            return;
        }
        ChatMessageRepo.INSTANCE.addMessage(aMessage);
        Player sender = PlayersRepo.getInstance().findPlayerByLogin(aSenderPrincipal.getName())
                .orElseThrow();
        aMessage.setSender(sender);
        String recipientTopicId = String.format("/topic/private/%s", aMessage.getRecipientLogin());
        log.info("send mess recipientTopicId " + recipientTopicId);
        _messagingTemplate.convertAndSend(recipientTopicId, aMessage);

        String senderTopicId = String.format("/topic/private/%s", aSenderPrincipal.getName());
        log.info("send mess senderTopicId " + senderTopicId);
        _messagingTemplate.convertAndSend(senderTopicId, aMessage);
        // return ChatMessage.builder().sender(sender).content(message.getContent()).build();

        Player recipient = PlayersRepo.getInstance().findPlayerByLogin(aMessage.getRecipientLogin())
                .orElseThrow();
        if (Strings.isNotEmpty(recipient.getEmail())) {
            // TODO отправлять сообщение только если сообщение не было прочитано в течении 10 минут.
            _emailService.sendEmail(recipient.getEmail(), "Новое личное сообщение",
                    String.format("%s отправил вам личное сообщение.",
                            sender.getName()));
        }
    }

    @RequestMapping(value = {"/chat"}, method = RequestMethod.GET)
    public String getChat(Model model, @ModelAttribute("login") String aRecipientLogin) {
        Player currentUser = getCurrentUser();
        addCurrentUserName(model);
        Player recipient =
                PlayersRepo.getInstance().findPlayerByLogin(aRecipientLogin).orElseThrow();
        model.addAttribute("recipient", recipient);
        List<ChatMessage> messages = ChatMessageRepo.INSTANCE
                .getMessagesBySenderAndRecipient(currentUser.getLogin(), aRecipientLogin);
        model.addAttribute("messages", messages);
        return "chat";
    }

    @RequestMapping(value = {"/openChat"}, method = RequestMethod.GET)
    public String openChat(Model model, @ModelAttribute("login") String aLogin) {
        System.out.println("openChat " + "  aLogin " + aLogin);
        addCurrentUserName(model);
        Player recipient = PlayersRepo.getInstance().findPlayerByLogin(aLogin).orElseThrow();
        model.addAttribute("recipient", recipient.getName());
        return String.format("redirect:/chat?login=%s", aLogin);
    }

    @RequestMapping(value = {"/messenger"}, method = RequestMethod.GET)
    public String getMessenger(Model model) {
        addCurrentUserName(model);
        Player currentUser = getCurrentUser();
        List<ChatInfo> chats = PlayersRepo.getInstance().getPlayers().stream()
                .filter(Predicate.not(currentUser::equals))
                .map((player -> ChatInfo.builder().sender(player)
                        .link(ChatLinkCreator.createChatLink(player.getName(), player)).build()))
                .collect(Collectors.toList());
        model.addAttribute("chats", chats);
        return "messenger";
    }

    private void addCurrentUserName(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
    }

    private Player getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        Optional<Player> player =
                PlayersRepo.getInstance().findPlayerByLogin(principal.getUsername());
        return player
                .orElseThrow(() -> new IllegalStateException("Не определен текущий пользователь."));
    }

}

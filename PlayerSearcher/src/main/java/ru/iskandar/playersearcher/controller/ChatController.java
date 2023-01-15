package ru.iskandar.playersearcher.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
import ru.iskandar.playersearcher.utils.StringUtils;

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
        if (StringUtils.isNullOrEmpty(aMessage.getContent())) {
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
        ChatMessageRepo.INSTANCE
                .getMessagesBySenderAndRecipient(aSenderPrincipal.getName(), recipient.getLogin())
                .stream()
                .filter(message -> aSenderPrincipal.getName().equals(message.getRecipientLogin()))
                .forEach(message -> message.setViewed(true));
        if (!StringUtils.isNullOrEmpty(recipient.getEmail())) {
            // TODO отправлять сообщение только если сообщение не было прочитано в течении 10 минут.
            _emailService.sendEmail(recipient.getEmail(), "Новое личное сообщение",
                    String.format("%s отправил вам личное сообщение.",
                            sender.getName()));
        }
    }

    @RequestMapping(value = {"/chat"}, method = RequestMethod.GET)
    public String getChat(Model model, @ModelAttribute("login") String aRecipientLogin) {
        Player currentUser = getCurrentUser();
        ModelAttributes.fill(model, currentUser);
        Player recipient =
                PlayersRepo.getInstance().findPlayerByLogin(aRecipientLogin).orElseThrow();
        model.addAttribute("recipient", recipient);
        List<ChatMessage> messages = ChatMessageRepo.INSTANCE
                .getMessagesBySenderAndRecipient(currentUser.getLogin(), aRecipientLogin);
        messages.stream()
                .filter(message -> currentUser.getLogin().equals(message.getRecipientLogin()))
                .forEach(message -> message.setViewed(true));
        model.addAttribute("messages", messages);
        return "chat";
    }

    @RequestMapping(value = {"/openChat"}, method = RequestMethod.GET)
    public String openChat(Model model, @ModelAttribute("login") String aLogin) {
        System.out.println("openChat " + "  aLogin " + aLogin);
        ModelAttributes.fill(model, getCurrentUser());
        Player recipient = PlayersRepo.getInstance().findPlayerByLogin(aLogin).orElseThrow();
        model.addAttribute("recipient", recipient.getName());
        return String.format("redirect:/chat?login=%s", aLogin);
    }

    @RequestMapping(value = {"/messenger"}, method = RequestMethod.GET)
    public String getMessenger(Model model) {
        Player currentUser = getCurrentUser();
        ModelAttributes.fill(model, currentUser);
        List<ChatMessage> messages = ChatMessageRepo.INSTANCE
                .getMessagesBySenderAndRecipient(currentUser.getLogin(), currentUser.getLogin());
        Map<String, Long> unviewedMessCount = new HashMap<>();
        for (ChatMessage mess : messages) {
            if (!mess.isViewed() && currentUser.getLogin().equals(mess.getRecipientLogin())) {
                String senderLogin = mess.getSender().getLogin();
                long count =
                        unviewedMessCount.computeIfAbsent(senderLogin, login -> 0L);
                count++;
                unviewedMessCount.put(senderLogin, count);
            }
        }
        Set<String> logins = messages.stream()
                .map(mess -> Set.of(mess.getRecipientLogin(), mess.getSender().getLogin()))
                .flatMap(Collection::stream).filter(Predicate.not(currentUser.getLogin()::equals))
                .collect(Collectors.toSet());
        List<ChatInfo> chats = logins.stream()
                .map(login -> PlayersRepo.getInstance().findPlayerByLogin(login).orElseThrow())
                .sorted(Comparator.comparing(Player::getName))
                .map(player -> ChatInfo.builder().sender(player)
                        .link(ChatLinkCreator.createChatLink(player.getName(), player))
                        .unviewedMessagesCount(unviewedMessCount.computeIfAbsent(player.getLogin(),
                                login -> 0L))
                        .build())
                .collect(Collectors.toList());
        model.addAttribute("chats", chats);
        return "messenger";
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

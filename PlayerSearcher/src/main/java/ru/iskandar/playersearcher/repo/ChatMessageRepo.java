package ru.iskandar.playersearcher.repo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.experimental.Accessors;
import ru.iskandar.playersearcher.model.ChatMessage;

@Accessors(prefix = "_")
public class ChatMessageRepo {

    /** Экземпляр класса */
    public static final ChatMessageRepo INSTANCE = new ChatMessageRepo();

    private final List<ChatMessage> _messages = new ArrayList<>();

    public void addMessage(@NonNull ChatMessage aMessage) {
        _messages.add(aMessage);
    }

    public List<ChatMessage> getMessagesBySenderAndRecipient(@NonNull String aSenderLogin,
            @NonNull String aRecipientLogin) {
        Predicate<ChatMessage> filter = message -> {
            boolean result = aSenderLogin.equals(message.getSender().getLogin())
                    || aSenderLogin.equals(message.getRecipientLogin());
            result = result && aRecipientLogin.equals(message.getRecipientLogin())
                    || aRecipientLogin.equals(message.getSender().getLogin());
            return result;
        };
        return _messages.stream()
                .filter(filter)
                .sorted(Comparator.comparing(ChatMessage::getDate))
                .collect(Collectors.toList());
    }

}

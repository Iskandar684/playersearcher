package ru.iskandar.playersearcher.controller;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.iskandar.playersearcher.model.LinkDescription;
import ru.iskandar.playersearcher.model.Player;
import ru.iskandar.playersearcher.model.Suggestion;

@UtilityClass
public class ChatLinkCreator {

    public LinkDescription createChatLink(@NonNull String aText, @NonNull Player aRecipient) {
        return createChatLink(aText, aRecipient.getLogin());
    }

    public LinkDescription createChatLink(@NonNull String aText, @NonNull String aRecipientLogin) {
        String link = String.format("%s%s", "/openChat?login=", aRecipientLogin);
        return new LinkDescription(aText, link);
    }

    public LinkDescription createChatLink(@NonNull Suggestion aSuggestion) {
        return ChatLinkCreator.createChatLink("Написать", aSuggestion.getPlayer());
    }

}

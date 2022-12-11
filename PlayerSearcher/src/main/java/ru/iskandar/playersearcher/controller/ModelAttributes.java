package ru.iskandar.playersearcher.controller;

import org.springframework.ui.Model;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.iskandar.playersearcher.model.Player;
import ru.iskandar.playersearcher.repo.ChatMessageRepo;

@UtilityClass
public class ModelAttributes {

    public static void fill(@NonNull Model model, @NonNull Player aCurrentUser) {
        model.addAttribute("currentUser", aCurrentUser);
        model.addAttribute("currentUserMessagesLinkText",
                getCurrentUserMessagesLinkText(aCurrentUser));
    }

    private String getCurrentUserMessagesLinkText(@NonNull Player aCurrentUser) {
        long count = ChatMessageRepo.INSTANCE.getUnviewedMessagesCount(aCurrentUser.getLogin());
        String text = "Мои сообщения";
        return count == 0 ? text : String.format("%s (%s)", text, count);
    }

}

package ru.iskandar.playersearcher.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Сообщение чата.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(prefix = "_")
public class ChatMessage {

    /** Отправитель */
    private Player _sender;

    /** Содержимое */
    private String _content;

    /** Логин получателя */
    private String _recipientLogin;

    /** Дата отправки */
    @Builder.Default
    private Date _date = new Date();

    public String getSenderAndContent() {
        return String.format("%s: %s", _sender.getName(), _content);
    }

}

package ru.iskandar.playersearcher.model;

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
    private String _sender;

    /** Содержимое */
    private String _content;

}

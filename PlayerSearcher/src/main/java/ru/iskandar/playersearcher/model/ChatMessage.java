package ru.iskandar.playersearcher.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Сообщение чата.
 */
@Setter
@Getter
@Accessors(prefix = "_")
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private String _content;

}

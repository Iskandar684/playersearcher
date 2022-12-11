package ru.iskandar.playersearcher.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Getter
@Accessors(prefix = "_")
@Builder
public class ChatInfo {

    @NonNull
    private final Player _sender;

    @NonNull
    private final LinkDescription _link;

    /** Количество непросмотренных сообщений */
    private final long _unviewedMessagesCount;

    public String getText() {
        if (_unviewedMessagesCount == 0) {
            return _sender.getName();
        }
        return String.format("%s (%d)", _sender.getName(), _unviewedMessagesCount);
    }

}

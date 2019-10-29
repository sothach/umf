package org.anized.common;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MessageBundle {
    private final ResourceBundle resources;

    public MessageBundle(final ResourceBundle resources) {
        this.resources = resources;
    }

    public String text(final String key, Object... params) {
        final String message = resources.getString(key);
        assert(message != null && !message.isEmpty());
        return MessageFormat.format(message, params);
    }
}


package com.aidn5.universalchat.common;

public enum Channel {
    NONE(""),
    UNIVERSAL("Universal"),
    ;

    private final String displayName;

    Channel(String displayName) {
        this.displayName = displayName;
    }

    public static Channel getChannel(String query) {
        for (Channel channel : values()) {
            if (channel.displayName.toLowerCase().startsWith(query.toLowerCase())) {
                return channel;
            }
        }

        return NONE;
    }

    public String displayName() {
        return displayName.toUpperCase();
    }
}

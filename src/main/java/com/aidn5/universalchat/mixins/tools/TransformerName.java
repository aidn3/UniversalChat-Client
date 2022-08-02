package com.aidn5.universalchat.mixins.tools;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class TransformerName {
    @Nonnull
    private final String clean;

    @Nonnull
    private final String obfuscated;

    @Nonnull
    private final String notch;

    public TransformerName(@Nonnull String clean, @Nonnull String obfuscated, @Nonnull String notch) {
        this.clean = Objects.requireNonNull(clean);
        this.obfuscated = Objects.requireNonNull(obfuscated);
        this.notch = Objects.requireNonNull(notch);
    }

    @Nonnull
    public String getClean() {
        return clean;
    }

    @Nonnull
    public String getObfuscated() {
        return obfuscated;
    }

    @Nonnull
    public String getNotch() {
        return notch;
    }

    public boolean match(@Nonnull String query) {
        return query.equals(getClean())
                || query.equals(getObfuscated())
                || query.equals(getNotch());
    }
}

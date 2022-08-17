package com.aidn5.universalchat.common;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Player {
    private static final Pattern validUsername = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");

    private Player() {
        throw new AssertionError();
    }

    /**
     * Check whether the username is a valid minecraft-username.
     *
     * @param username the username to validate.
     * @return <code>true</code> if the username is valid.
     */
    public static boolean isValidUsername(@Nullable String username) {
        try {
            validateUsername(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check whether the username is valid as a minecraft-username.
     * <p>
     * <b>Usage Example:</b>
     * <code>
     * void method(String username) throws IllegalArgumentException {
     * this.username = validateUsername(username);
     * }
     * </code>
     *
     * @param username the username to validate
     * @return returns {@code username} back.
     * @throws NotValidUsername if the username is not valid with {@link Exception#getMessage()}
     *                          for the reason.
     */
    @Nonnull
    public static String validateUsername(@Nullable String username) throws NotValidUsername {
        if (username == null || username.isEmpty()) {
            throw new NotValidUsername("Username must not be null or empty");
        }

        if (username.length() < 3 || username.length() > 16) {
            throw new NotValidUsername("Username length must be between 2 and 16. " + username.length() + " is given");
        }

        if (!validUsername.matcher(username).find()) {
            throw new NotValidUsername(
                    "Username must only contain a-z, A-Z, 0-9 and '_'");
        }

        return username;
    }

    /**
     * Indicates that the provided username is not a valid minecraft-username.
     *
     * @author aidn5
     * @since 1.0
     */
    public static class NotValidUsername extends IllegalArgumentException {
        /**
         * Construct an empty exception.
         */
        public NotValidUsername() {
            // empty constructor
        }

        /**
         * Construct the exception.
         *
         * @param username the invalid username.
         */
        public NotValidUsername(String username) {
            super(username);
        }
    }
}
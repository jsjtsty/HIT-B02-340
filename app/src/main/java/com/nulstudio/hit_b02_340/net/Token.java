package com.nulstudio.hit_b02_340.net;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Token {
    private final String token;

    public Token() {
        this.token = "";
    }

    public Token(String token) {
        this.token = token;
    }

    public Token(String userName, String passHash) {
        String raw = userName + "|" + passHash;
        this.token = Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public String getToken() {
        return token;
    }

    @NonNull
    @Override
    public String toString() {
        return token;
    }
}

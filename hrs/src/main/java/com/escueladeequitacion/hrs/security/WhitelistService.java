package com.escueladeequitacion.hrs.security;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
public class WhitelistService {
    @Value("${app.whitelist.emails}")
    private List<String> whitelistedEmails;

    public boolean isEmailAllowed(String email) {
        return whitelistedEmails.contains(email.toLowerCase());
    }
}

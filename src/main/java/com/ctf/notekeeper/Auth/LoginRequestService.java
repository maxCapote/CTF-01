package com.ctf.notekeeper.Auth;

import java.util.regex.Pattern;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ctf.notekeeper.Token.TokenService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoginRequestService {
    private static final Pattern USER_REGEX = Pattern.compile("^[a-zA-Z0-9]{1,100}$");
    private static final Pattern PASS_REGEX = Pattern.compile("^.{1,512}$");

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public String getToken(String username, String password) {
        // make sure those creds are valid before swiping a token
        validateCreds(username, password);
        Authentication authentication = authenticate(username, password);
        return tokenService.generateToken(authentication);
    }

    private void validateCreds(String username, String password) {
        if (username == null || password == null ||
            !(USER_REGEX.matcher(username).matches() && PASS_REGEX.matcher(password).matches())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    private Authentication authenticate(String username, String password) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }
}

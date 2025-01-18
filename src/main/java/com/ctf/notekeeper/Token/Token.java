package com.ctf.notekeeper.Token;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tokens")
public class Token {
    @Id
    private String jti;
    private String issuer;
    private Instant issuedAt;
    private Instant expiresAt;
    private String subject;
    private String roles;
}

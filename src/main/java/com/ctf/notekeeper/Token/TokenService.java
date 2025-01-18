package com.ctf.notekeeper.Token;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Service;

import com.ctf.notekeeper.Role.RoleEnum;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TokenService {
    private static final String EXPECTED_ISSUER = "notekeeper";
    private static final String CLAIM_ISSUER = "iss";
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_JTI = "jti";

    private final JwtEncoder encoder;
    private final TokenRepository tokenRepository;

    // we'll construct the jwt here
    // this should be okay
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant expiry = now.plus(15, ChronoUnit.MINUTES);
        String subject = authentication.getName();
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        String jti = UUID.randomUUID().toString();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(EXPECTED_ISSUER)
            .issuedAt(now)
            .expiresAt(expiry)
            .subject(subject)
            .claim(CLAIM_ROLES, roles)
            .claim(CLAIM_JTI, jti)
            .build();

        String token = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        Token tokenEntry = new Token(jti, EXPECTED_ISSUER, now, expiry, subject, roles);
        tokenRepository.save(tokenEntry);
        return token;
    }

    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        return new Converter<Jwt, AbstractAuthenticationToken>() {
            @Override
            public AbstractAuthenticationToken convert(Jwt jwt) {
                validateJwt(jwt);

                JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
                grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
                grantedAuthoritiesConverter.setAuthoritiesClaimName(CLAIM_ROLES);

                JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
                authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
                return authenticationConverter.convert(jwt);
            }
        };
    }

    private void validateJwt(Jwt jwt) {
        validateJti(jwt);
        validateSubject(jwt);
        validateIssuer(jwt);
        validateRoles(jwt);
    }

    private void validateJti(Jwt jwt) {
        String jti = jwt.getClaimAsString(CLAIM_JTI);
        if (!tokenRepository.existsById(jti)) {
            throw new AccessDeniedException("Invalid JWT");
        }
    }

    private void validateSubject(Jwt jwt) {
        String jti = jwt.getClaimAsString(CLAIM_JTI);
        Token token = tokenRepository.findById(jti)
                .orElseThrow(() -> new AccessDeniedException("Invalid JWT"));
        if (!token.getSubject().equals(jwt.getSubject())) {
            throw new AccessDeniedException("Invalid JWT");
        }
    }

    private void validateIssuer(Jwt jwt) {
        String issuer = jwt.getClaimAsString(CLAIM_ISSUER);
        if (issuer == null || !issuer.equals(EXPECTED_ISSUER)) {
            throw new AccessDeniedException("Invalid JWT");
        }
    }

    private void validateRoles(Jwt jwt) {
        String rolesStr = jwt.getClaimAsString(CLAIM_ROLES);
        if (rolesStr == null || rolesStr.isEmpty()) {
            throw new AccessDeniedException("Invalid JWT");
        }

        List<String> roles = Arrays.asList(rolesStr.split(" "));
        if (!roles.stream().allMatch(this::isValidRole)) {
            throw new AccessDeniedException("Invalid JWT");
        }
    }

    private boolean isValidRole(String role) {
        return EnumSet.allOf(RoleEnum.class).stream()
                .anyMatch(e -> e.name().equals(role));
    }

    public void removeExpiredTokens() {
        Instant now = Instant.now();
        tokenRepository.findAll().stream()
                .filter(token -> token.getExpiresAt() != null && token.getExpiresAt().isBefore(now))
                .forEach(tokenRepository::delete);
    }
}

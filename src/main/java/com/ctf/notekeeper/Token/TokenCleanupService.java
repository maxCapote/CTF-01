package com.ctf.notekeeper.Token;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TokenCleanupService {
    private final TokenService tokenService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void startScheduler() {
        scheduler.scheduleAtFixedRate(() -> tokenService.removeExpiredTokens(), 0, 15, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void stopScheduler() {
        scheduler.shutdown();
    }
}

package com.ctf.notekeeper.Auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ctf.notekeeper.Misc.CustomResponse;
import com.ctf.notekeeper.Misc.CustomResponseFactory;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthController {
    private final LoginRequestService loginRequestService;

    @PostMapping("/login")
    public CustomResponse token(@RequestBody LoginRequest loginRequest) {
        return CustomResponseFactory.createResponse("jwt", loginRequestService.getToken(loginRequest.username(), loginRequest.password()));
    }
}

package com.ctf.notekeeper.User;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ctf.notekeeper.Misc.CustomResponse;
import com.ctf.notekeeper.Misc.CustomResponseFactory;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public User registUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/change-password")
    public User changePassword(@RequestBody User user) {
        return userService.changePassword(user);
    }

    @GetMapping("/whoami")
    public CustomResponse whoami(Principal principal) {
        return CustomResponseFactory.createResponse("name", principal.getName());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/flag")
    public CustomResponse getFlag() {
        return CustomResponseFactory.createResponse("flag", "{Zm9vYmFy}");
    }
}

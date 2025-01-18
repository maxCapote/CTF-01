package com.ctf.notekeeper.User;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ctf.notekeeper.ResponseHandling.CustomResponse;
import com.ctf.notekeeper.ResponseHandling.CustomResponseFactory;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    // no need to allow registration at the moment
    //@PostMapping("/register")
    //public CustomResponse registUser(@RequestBody User user) {
        //return CustomResponseFactory.createResponse("user", userService.registerUser(user));
    //}

    @PostMapping("/change-password")
    public CustomResponse changePassword(@RequestBody User user) {
        return CustomResponseFactory.createResponse("message",  userService.changePassword(user));
    }

    // I don't even know whoami any more
   @GetMapping("/whoami")
    public CustomResponse whoami(Principal principal) {
        return CustomResponseFactory.createResponse("name", principal.getName());
    }

    // have to keep the goods locked down :)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/flag")
    public CustomResponse getFlag() {
        return CustomResponseFactory.createResponse("flag", "{Zm9vYmFy}");
    }
}

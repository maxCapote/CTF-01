package com.ctf.notekeeper.User;

//import java.util.ArrayList;
//import java.util.List;
import java.util.regex.Pattern;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

//import com.ctf.notekeeper.Role.RoleEnum;
import com.ctf.notekeeper.Role.RoleUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private static final Pattern USER_REGEX = Pattern.compile("^[a-zA-Z0-9]{1,100}$");
    private static final Pattern PASS_REGEX = Pattern.compile("^.{1,512}$");

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /*public User registerUser(User user) {
        validateCreds(user.getUsername(), user.getPassword());
        if (userRepository.findByUsername(user.getUsername()) == null) {
            List<RoleEnum> roles = new ArrayList<>();
            roles.add(RoleEnum.USER);
            user.setId(generateUserId());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(roles);
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }*/

    // I guess we'll allow password change since that seems useful sometimes
    public User changePassword(User user) {
        validateCreds(user.getUsername(), user.getPassword());

        if (!user.getUsername().equals(getCurrentUser())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        User targetUser = userRepository.findByUsername(user.getUsername());
        targetUser.setId(user.getId() != null ? user.getId() : targetUser.getId());
        targetUser.setUsername(user.getUsername());
        targetUser.setPassword(passwordEncoder.encode(user.getPassword()));
        targetUser.setRoles(user.getRoles() != null ? user.getRoles() : targetUser.getRoles());
        return userRepository.save(targetUser);
    }

    private void validateCreds(String username, String password) {
        if (username == null || password == null ||
            !(USER_REGEX.matcher(username).matches() && PASS_REGEX.matcher(password).matches())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    //private synchronized Integer generateUserId() {
        //return userRepository.findAll().size() + 1;
    //}

    // the rest of this is a bunch of utility

    private String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    } 

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                RoleUtils.getAuthorities(user.getRoles())
        );
    }
}

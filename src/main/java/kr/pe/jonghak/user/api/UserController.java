package kr.pe.jonghak.user.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<String>> users() {
        String username = "Ned Stark";
        List<String> users = userRepository.findAll(username).stream().map(user -> {
            log.info("mongo data: {}", user.getName());
            return user.getName();
        }).toList();
        log.info("response user list: {}", users);
        return ResponseEntity.ok(users);
    }
}

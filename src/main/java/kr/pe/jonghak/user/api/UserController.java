package kr.pe.jonghak.user.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    @GetMapping("/users")
    public ResponseEntity<List<String>> users() {
        List<String> users = new ArrayList<>();
        users.add("john doe");
        users.add("jane doe");
        log.info("response user list: {}", users);
        return ResponseEntity.ok(users);
    }
}

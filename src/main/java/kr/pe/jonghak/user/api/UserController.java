package kr.pe.jonghak.user.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class UserController {
    private final UserRepository userRepository;
    private static final String TOPIC = "devops-user";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserController(UserRepository userRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/users")
    public ResponseEntity<List<String>> users() {
        List<String> users = userRepository.findAll().stream().map(User::getName).toList();
        log.info("response user list: {}", users);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        log.info("saved user: {}", savedUser.getName());
        String key = "created";
        String value = savedUser.getEmail();
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, key, value);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("error", ex);
            } else {
                log.info(String.format("\n\n Produced event to topic %s: key = %-10s value = %s \n\n", TOPIC, key, value));
            }
        });
        return ResponseEntity.ok(savedUser);
    }
}

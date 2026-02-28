package dev.ixlax.backend;

import dev.ixlax.backend.utils.MessageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<?> checkHealth() {

        MessageException messageException = new MessageException("200", "OK");

        return ResponseEntity.ok(messageException);
    }

}

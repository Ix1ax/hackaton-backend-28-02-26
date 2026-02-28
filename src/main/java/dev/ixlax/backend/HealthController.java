package dev.ixlax.backend;

import dev.ixlax.backend.common.dto.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<?> checkHealth() {

        MessageResponse messageException = new MessageResponse("200", "OK");

        return ResponseEntity.ok(messageException);
    }

}

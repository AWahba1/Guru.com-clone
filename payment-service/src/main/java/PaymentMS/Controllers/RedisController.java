package PaymentMS.Controllers;

import PaymentMS.Services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paymentRedis")
public class RedisController {

    private final RedisService redisService;

    @Autowired
    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("/cache/{key}")
    public ResponseEntity<String> save(@PathVariable String key, @RequestBody String value) {
        redisService.save(key, value);
        return ResponseEntity.ok("Value saved successfully");
    }

    @GetMapping("/cache/{key}")
    public ResponseEntity<String> find(@PathVariable String key) {
        String value = redisService.find(key);
        if (value != null) {
            return ResponseEntity.ok(value);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


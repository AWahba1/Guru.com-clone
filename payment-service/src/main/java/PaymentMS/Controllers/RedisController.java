package PaymentMS.Controllers;

import PaymentMS.Services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paymentRedis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/paymentApp/cache/{key}")
    public void save(@PathVariable String key, @RequestBody Object value) {
        redisService.save(key, value);
    }

    @GetMapping("/paymentApp/cache/{key}")
    public Object find(@PathVariable String key) {
        return redisService.find(key);
    }
}

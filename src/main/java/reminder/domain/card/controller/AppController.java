package reminder.domain.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/")
    public String home() {
        redisTemplate.opsForValue().set("abc", "def");
        return "Hello World!";
    }
}

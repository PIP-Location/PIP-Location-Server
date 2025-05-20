package kr.co.pinpick.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    // 키-벨류 설정
    public void setValues(Long key, String value, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key.toString(), value, duration);
    }

    // 키값으로 벨류 가져오기
    public String getValues(Long key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key.toString());
    }

    // 키-벨류 삭제
    public void delValues(Long key) {
        redisTemplate.delete(key.toString());
    }
}
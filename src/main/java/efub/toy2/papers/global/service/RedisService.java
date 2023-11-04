package efub.toy2.papers.global.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    /* 만료 시간이 지나면, 자동 삭제 */
    public void setValues(String key, String value, Long timeOut){
        redisTemplate.opsForValue().set(key,value,timeOut, TimeUnit.MILLISECONDS);
    }

    @Transactional(readOnly = true)
    public String getValues(String key){
        return redisTemplate.opsForValue().get(key);
    }

    @Transactional(readOnly = true)
    public Boolean checkValues(String key){
        return redisTemplate.hasKey(key);
    }
}

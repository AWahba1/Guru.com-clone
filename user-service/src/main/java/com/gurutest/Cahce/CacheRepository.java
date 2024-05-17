package com.gurutest.Cahce;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import com.gurutest.Cahce.CacheToken;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CacheRepository {
    public static final String HASH_KEY = "Token";
//
    @Autowired
    private RedisTemplate template;
////
    public CacheToken save(CacheToken cachedToken){
        template.opsForHash().put(HASH_KEY, cachedToken.getToken(), cachedToken);
        return cachedToken;
    }
    public List<Object> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }
    public CacheToken findCachedTokenByToken (String token){
        System.out.println("Called the db");
        return ((CacheToken) template.opsForHash().get(HASH_KEY,token));

    }
    public String deleteCachedToken(String token){
        template.opsForHash().delete(HASH_KEY,token);
        return "Token deleted successfully";
    }
}

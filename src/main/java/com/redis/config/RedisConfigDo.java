package com.redis.config;

import com.alibaba.fastjson.JSONObject;
import com.redis.domain.UserVo;
import com.redis.service.RedisService;
import com.redis.util.RedisKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;
import com.redis.Application;
import javax.annotation.Resource;
import javax.tools.JavaCompiler;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
/**
 * @Author LT-0024
 * @Date 2020/10/22 14:41
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class RedisConfigDo {
    Logger logger= LoggerFactory.getLogger(RedisConfigDo.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private ValueOperations<String,Object> valueOperations;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private ListOperations<String, Object> listOperations;

    @Autowired
    private SetOperations<String, Object> setOperations;

    @Autowired
    private ZSetOperations<String, Object> zSetOperations;

    @Resource
    private RedisService redisService;

    @Test
    public void testObj() throws Exception{
        synchronized (this){
        UserVo userVo = new UserVo();
        userVo.setAddress("上海");
        userVo.setName("测试2");
        userVo.setAge(123);
        ValueOperations<String,Object> operations = redisTemplate.opsForValue();
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set("1",uuid,60,TimeUnit.SECONDS);
        Boolean flag= stringRedisTemplate.opsForValue().setIfAbsent("1","2",60,TimeUnit.SECONDS);
        //String key = RedisKeyUtil.getKey(UserVo.Table,"name",userVo.getName());
        String key = userVo.getName();
        Boolean result = operations.setIfAbsent(key,userVo);
        //String json = JSONObject.toJSON(userVo).toString();
        //stringRedisTemplate.opsForValue().set(key,json);
            if (result!=true){
                logger.info("这是redis已存在的用户 {}",JSONObject.toJSONString(userVo));
            }
        }
        stringRedisTemplate.delete("1");
    }

    @Test
    public void testValueOption( )throws  Exception{
        UserVo userVo = new UserVo();
        userVo.setAddress("上海");
        userVo.setName("jantent");
        userVo.setAge(23);
        valueOperations.set("test",userVo);

        System.out.println(valueOperations.get("test"));
    }

    @Test
    public void testSetOperation() throws Exception{
        UserVo userVo = new UserVo();
        userVo.setAddress("北京");
        userVo.setName("jantent");
        userVo.setAge(23);
        UserVo auserVo = new UserVo();
        auserVo.setAddress("n柜昂周");
        auserVo.setName("antent");
        auserVo.setAge(23);
        setOperations.add("user:test",userVo,auserVo);
        Set<Object> result = setOperations.members("user:test");
        System.out.println(result);
    }

    @Test
    public void HashOperations() throws Exception{
        UserVo userVo = new UserVo();
        userVo.setAddress("北京");
        userVo.setName("jantent");
        userVo.setAge(23);
        hashOperations.put("hash:user",userVo.hashCode()+"",userVo);
        System.out.println(hashOperations.get("hash:user",userVo.hashCode()+""));
    }

    @Test
    public void  ListOperations() throws Exception{
        UserVo userVo = new UserVo();
        userVo.setAddress("北京");
        userVo.setName("jantent");
        userVo.setAge(23);
//        listOperations.leftPush("list:user",userVo);
//        System.out.println(listOperations.leftPop("list:user"));
        // pop之后 值会消失
        System.out.println(listOperations.leftPop("list:user"));
    }
}

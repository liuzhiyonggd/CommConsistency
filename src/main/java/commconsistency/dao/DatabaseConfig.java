package commconsistency.dao;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {
	
//	@Bean
//    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
//        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
//
//        redisTemplate.setConnectionFactory(factory);
//        redisTemplate.afterPropertiesSet();
//        setSerializer(redisTemplate);
//        return redisTemplate;
//    }
//
//    private void setSerializer(RedisTemplate<String, String> template) {
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//    }

}

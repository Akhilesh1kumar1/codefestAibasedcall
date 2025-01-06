package com.sr.capital.config;

import com.sr.capital.listner.KeyExpirationListener;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.io.IOException;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedisConfig {

    @Value("${redis.config}")
    String file;

    @Bean
    public RedissonClient redissonClient() throws IOException {
        Config config =Config.fromYAML(new ClassPathResource(file).getInputStream());
        config.setCodec(new StringCodec());
        return Redisson.create(config);
    }

    /*@Bean
    public RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory redisConnectionFactory, KeyExpirationListener keyExpirationListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
//      Set the keyspace notification configuration using RedisCallback
        Topic topic = new ChannelTopic("__keyevent@0__:expired");
        container.addMessageListener(keyExpirationListener, topic);
        return container;
    }
    @Bean
    public MessageListenerAdapter listenerAdapter(KeyExpirationListener listener) {
        return new MessageListenerAdapter(listener);
    }*/

}

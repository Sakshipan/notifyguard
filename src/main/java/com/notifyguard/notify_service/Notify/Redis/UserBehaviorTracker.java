package com.notifyguard.notify_service.Notify.Redis;

import com.notifyguard.notify_service.Notify.entity.ChannelType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBehaviorTracker {

    private final RedisTemplate<String, Object> redisTemplate;

    private String getKey(String userId, ChannelType channel) {
        return "user:behavior:" + userId + ":" + channel;
    }

    public void recordSent(String userId, ChannelType channel) {
        String key = getKey(userId, channel);
        redisTemplate.opsForHash().increment(key, "totalSent", 1);
    }

    public void recordResponse(String userId, ChannelType channel) {
        String key = getKey(userId, channel);

        redisTemplate.opsForHash().increment(key, "totalResponded", 1);

        LocalDateTime now = LocalDateTime.now();
        redisTemplate.opsForHash().put(key, "lastRespondedAt", now.toString());

        int hour = now.getHour();
        if (hour >= 6 && hour < 12) {
            redisTemplate.opsForHash().increment(key, "morningResponses", 1);
        } else if (hour >= 12 && hour < 18) {
            redisTemplate.opsForHash().increment(key, "afternoonResponses", 1);
        } else {
            redisTemplate.opsForHash().increment(key, "eveningResponses", 1);
        }
    }

    public UserChannelBehavior getBehavior(String userId, ChannelType channel) {

        String key = getKey(userId, channel);
        HashOperations<String, String, Object> hashOps =
                redisTemplate.opsForHash();

        int totalSent = getIntValue(hashOps, key, "totalSent");
        int totalResponded = getIntValue(hashOps, key, "totalResponded");
        int morningResponses = getIntValue(hashOps, key, "morningResponses");
        int afternoonResponses = getIntValue(hashOps, key, "afternoonResponses");
        int eveningResponses = getIntValue(hashOps, key, "eveningResponses");

        double responseRate = totalSent > 0
                ? ((double) totalResponded / totalSent) * 100
                : 0;

        String lastRespondedAtValue =
                (String) hashOps.get(key, "lastRespondedAt");
        LocalDateTime lastRespondedAt = lastRespondedAtValue != null
                ? LocalDateTime.parse(lastRespondedAtValue)
                : null;

        String bestTimeOfDay = getBestTimeOfDay(
                morningResponses, afternoonResponses, eveningResponses);

        return UserChannelBehavior.builder()
                .userId(userId)
                .channel(channel)
                .totalSent(totalSent)
                .totalResponded(totalResponded)
                .responseRate(responseRate)
                .lastRespondedAt(lastRespondedAt)
                .morningResponses(morningResponses)
                .afternoonResponses(afternoonResponses)
                .eveningResponses(eveningResponses)
                .bestTimeOfDay(bestTimeOfDay)
                .build();
    }

    public List<UserChannelBehavior> getAllChannelBehaviors(String userId) {
        List<UserChannelBehavior> behaviors = new ArrayList<>();
        for (ChannelType channel : ChannelType.values()) {
            behaviors.add(getBehavior(userId, channel));
        }
        return behaviors;
    }

    public String getBestChannel(String userId) {
        return getAllChannelBehaviors(userId)
                .stream()
                .filter(b -> b.getTotalSent() > 0)
                .max(Comparator.comparingDouble(
                        UserChannelBehavior::getResponseRate))
                .map(b -> b.getChannel().name())
                .orElse("EMAIL");
    }

    private int getIntValue(
            HashOperations<String, String, Object> hashOps,
            String key,
            String field) {
        Object value = hashOps.get(key, field);
        return value != null
                ? Integer.parseInt(value.toString())
                : 0;
    }

    private String getBestTimeOfDay(
            int morning, int afternoon, int evening) {
        if (morning >= afternoon && morning >= evening) return "MORNING";
        if (afternoon >= morning && afternoon >= evening) return "AFTERNOON";
        return "EVENING";
    }
}
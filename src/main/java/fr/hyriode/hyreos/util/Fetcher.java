package fr.hyriode.hyreos.util;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyreos.api.HyreosRedisKey;

public class Fetcher {

    public static boolean exists(HyreosRedisKey key) {
        return Fetcher.exists(key.getKey());
    }

    public static boolean exists(String key) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.exists(key));
    }

    public static void update(HyreosRedisKey key, long value) {
        Fetcher.update(key.getKey(), value);
    }

    public static void update(String key, long value) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.set(key, String.valueOf(value)));
    }

    public static long fetch(HyreosRedisKey key) {
        return Fetcher.fetch(key.getKey());
    }

    public static long fetch(String key) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String value = jedis.get(key);

            return value == null ? 0 : Long.parseLong(value);
        });
    }
}

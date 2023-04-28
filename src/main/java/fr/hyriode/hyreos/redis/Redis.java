package fr.hyriode.hyreos.redis;

import fr.hyriode.hyreos.api.HyreosAPI;
import fr.hyriode.hyreos.config.nested.RedisConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by AstFaster
 * on 14/10/2022 at 19:24
 */
public class Redis {

    private boolean running;
    private ScheduledFuture<?> process;

    private JedisPool jedisPool;

    private final RedisConfig config;

    public Redis(RedisConfig config) {
        this.config = config;

        this.start();
    }

    private void start() {
        this.running = true;

        this.connect();

        this.process = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                this.jedisPool.getResource().close();
            } catch (Exception e) {
                System.out.println("An error occurred in Redis connection! Trying to reconnect...");

                this.connect();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void connect() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxIdle(0);
        poolConfig.setMaxTotal(-1);
        poolConfig.setJmxEnabled(false);

        final String hostname = this.config.getHostname();
        final int port = this.config.getPort();
        final String password = this.config.getPassword();

        if (password != null && !password.isEmpty()) {
            this.jedisPool = new JedisPool(poolConfig, hostname, port, 2000, password);
        } else {
            this.jedisPool = new JedisPool(poolConfig, hostname, port, 2000);
        }

        try {
            this.jedisPool.getResource().close();

            System.out.println("Connection set between " + HyreosAPI.NAME + " and Redis");
        } catch (Exception e) {
            e.printStackTrace();

            System.err.println("An error occurred while connecting to Redis! ");
            System.err.println("Try to fix it! Stopping " + HyreosAPI.NAME + "...");
            System.exit(-1);
        }
    }

    public void stop() {
        if (this.running) {
            System.out.println("Stopping Redis connection...");

            this.running = false;
            this.process.cancel(true);

            this.jedisPool.close();
            this.jedisPool.destroy();
        }
    }

    public JedisPool getPool() {
        return this.jedisPool;
    }

}

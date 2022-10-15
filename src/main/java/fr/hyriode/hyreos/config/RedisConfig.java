package fr.hyriode.hyreos.config;

/**
 * Created by AstFaster
 * on 14/10/2022 at 19:28
 */
public class RedisConfig {

    private final String hostname;
    private final int port;
    private final String password;

    public RedisConfig(String hostname, int port, String password) {
        this.hostname = hostname;
        this.port = port;
        this.password = password;
    }

    public String getHostname() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public String getPassword() {
        return this.password;
    }

}

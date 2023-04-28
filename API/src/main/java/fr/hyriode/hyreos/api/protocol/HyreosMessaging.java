package fr.hyriode.hyreos.api.protocol;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.hyriode.hyreos.api.HyreosAPI;
import fr.hyriode.hyreos.api.protocol.packet.HyreosPacket;
import redis.clients.jedis.JedisPubSub;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by AstFaster
 * on 14/10/2022 at 19:04
 */
public class HyreosMessaging {

    /** {@link Gson} instance */
    private static final Gson GSON = new Gson();
    /** The channel used by the communication protocol. All packets are send on it */
    private static final String CHANNEL = "hyreos";

    /** All the registered receivers */
    private final List<ReceiverContext<?>> receivers;

    /** The subscriber object */
    private final Subscriber subscriber;
    /** The task to keep the subscriber alive */
    private final Thread subscriberThread;

    /** The {@link HyreosAPI} instance */
    private final HyreosAPI hyreosAPI;

    /**
     * Constructor of {@link HyreosMessaging}
     *
     * @param hyreosAPI The {@link HyreosAPI} instance
     */
    public HyreosMessaging(HyreosAPI hyreosAPI) {
        this.hyreosAPI = hyreosAPI;
        this.receivers = new ArrayList<>();
        this.subscriber = new Subscriber();
        this.subscriberThread = new Thread(() -> {
            System.out.println("Starting " + HyreosAPI.NAME + " messaging...");

            this.hyreosAPI.processWithRedis(jedis -> jedis.subscribe(this.subscriber, CHANNEL));
        });
        this.subscriberThread.start();
    }

    /**
     * Stop the messaging system
     */
    public void stop() {
        System.out.println("Stopping " + HyreosAPI.NAME + " messaging...");

        this.subscriberThread.interrupt();

        if (this.subscriber.isSubscribed()) {
            this.subscriber.unsubscribe();
        }
    }

    /**
     * Send a packet
     *
     * @param packet The {@link HyreosPacket}
     */
    public void sendPacket(HyreosPacket packet) {
        this.hyreosAPI.processWithRedis(jedis -> jedis.publish(CHANNEL, this.encodePacket(packet)));
    }

    /**
     * Register a packet receiver
     *
     * @param packetsClass The type of the packet to listen
     * @param receiver The receiver to register
     * @param <T> The type of the packet and receiver
     */
    public <T extends HyreosPacket> void registerReceiver(Class<T> packetsClass, HyreosReceiver<T> receiver) {
        this.receivers.add(new ReceiverContext<>(packetsClass, receiver));
    }

    /**
     * Encode a given packet to string
     *
     * @param packet The packet to encode
     * @return The encoded packet
     */
    private String encodePacket(HyreosPacket packet) {
        final JsonObject object = new JsonObject();

        object.addProperty("id", packet.getClass().getName());
        object.add("data", GSON.toJsonTree(packet));

        return Base64.getEncoder().encodeToString(object.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * The subscriber class
     */
    private class Subscriber extends JedisPubSub {

        @Override
        public void onMessage(String channel, String message) {
            try {
                final JsonObject object = GSON.fromJson(new String(Base64.getDecoder().decode(message), StandardCharsets.UTF_8), JsonObject.class);
                final Class<?> packetClass = Class.forName(object.get("id").getAsString());
                final HyreosPacket packet = (HyreosPacket) GSON.fromJson(object.get("data").getAsJsonObject(), packetClass);

                for (ReceiverContext<?> context : receivers) {
                    if (packetClass.isAssignableFrom(context.getPacketClass())) {
                        context.run(packet);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception ignored) {}
        }

    }

    /**
     * The receiver wrapping class
     *
     * @param <T> The type of the receiver
     */
    private static class ReceiverContext<T extends HyreosPacket> {

        private final Class<T> packetClass;
        private final HyreosReceiver<T> receiver;

        public ReceiverContext(Class<T> packetClass, HyreosReceiver<T> receiver) {
            this.packetClass = packetClass;
            this.receiver = receiver;
        }

        public Class<T> getPacketClass() {
            return this.packetClass;
        }

        public void run(HyreosPacket packet) {
            this.receiver.onReceive(this.packetClass.cast(packet));
        }

    }

}

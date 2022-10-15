package fr.hyriode.hyreos.api.protocol;

import fr.hyriode.hyreos.api.protocol.packet.HyreosPacket;

/**
 * Created by AstFaster
 * on 14/10/2022 at 19:05
 *
 * This class represents a {@link HyreosPacket} receiver.<br>
 * This receiver will be listening for all incoming packets with the queried type.
 */
@FunctionalInterface
public interface HyreosReceiver<T extends HyreosPacket> {

    /**
     * This method is triggered when a packet is received
     *
     * @param packet The received {@linkplain HyreosPacket packet}
     */
    void onReceive(T packet);

}

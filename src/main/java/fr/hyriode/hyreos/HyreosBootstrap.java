package fr.hyriode.hyreos;

import fr.hyriode.hyreos.util.References;

/**
 * Created by AstFaster
 * on 13/10/2022 at 20:49
 */
public class HyreosBootstrap {

    public static void main(String[] args) {
        if (Float.parseFloat(System.getProperty("java.class.version")) < 62.0D) {
            System.err.println("*** ERROR *** " + References.NAME + " requires Java >= 18 to function!");
            return;
        }

        new Hyreos().start();
    }

}

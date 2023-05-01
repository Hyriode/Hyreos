package fr.hyriode.hyreos.metrics;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.metrics.MetricsRedisKeys;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyreos.Hyreos;
import fr.hyriode.hyreos.api.IHyreosMetric;
import fr.hyriode.hyreos.api.money.EmittedCurrencies;
import fr.hyriode.hyreos.api.money.MoneyType;
import fr.hyriode.hyreos.api.players.ConnectedPlayers;
import fr.hyriode.hyreos.api.players.HyriPlusPlayers;
import fr.hyriode.hyreos.api.players.RegisteredPlayers;

import java.util.concurrent.atomic.AtomicLong;

public class PlayerRelatedHandler {

    public void start() {
        for (final MetricsRedisKeys key : MetricsRedisKeys.values()) {
            if (this.exists(key)) {
                return;
            }
        }

        final AtomicLong hyris = new AtomicLong();
        final AtomicLong hyodes = new AtomicLong();
        final AtomicLong hyriplus = new AtomicLong();
        final AtomicLong registered = new AtomicLong();
        for (final IHyriPlayer player : HyriAPI.get().getPlayerManager().getPlayers()) {
            hyris.addAndGet(player.getHyris().getAmount());
            hyodes.addAndGet(player.getHyodes().getAmount());

            if (player.getHyriPlus().has()) {
                hyriplus.incrementAndGet();
            }

            registered.incrementAndGet();
        }

        this.update(MetricsRedisKeys.HYRIS, hyris.get());
        this.update(MetricsRedisKeys.HYODES, hyodes.get());
        this.update(MetricsRedisKeys.HYRI_PLUS, hyriplus.get());
        this.update(MetricsRedisKeys.REGISTRED_PLAYERS, registered.get());
    }

    public void handle() {
        this.handleCurrencies();
        this.handleHyriPlus();
        this.handleRegisteredPlayers();
        this.handleConnectedPlayers();
    }

    public void handleCurrencies() {
        final IHyreosMetric hyrisMetric = new EmittedCurrencies(MoneyType.HYRIS, this.fetch(MetricsRedisKeys.HYRIS));
        final IHyreosMetric hyodesMetric = new EmittedCurrencies(MoneyType.HYODES, this.fetch(MetricsRedisKeys.HYODES));

        Hyreos.get().getInfluxDB().sendMetrics(hyrisMetric);
        Hyreos.get().getInfluxDB().sendMetrics(hyodesMetric);
    }

    public void handleHyriPlus() {
        final IHyreosMetric metric = new HyriPlusPlayers(this.fetch(MetricsRedisKeys.HYRI_PLUS));

        Hyreos.get().getInfluxDB().sendMetrics(metric);
    }

    public void handleRegisteredPlayers() {
        final IHyreosMetric metric = new RegisteredPlayers(this.fetch(MetricsRedisKeys.REGISTRED_PLAYERS));

        Hyreos.get().getInfluxDB().sendMetrics(metric);
    }

    public void handleConnectedPlayers() {
        final IHyreosMetric metric = new ConnectedPlayers(HyriAPI.get().getNetworkManager().getPlayerCounter().getPlayers());

        Hyreos.get().getInfluxDB().sendMetrics(metric);
    }

    private boolean exists(MetricsRedisKeys key) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> jedis.exists(key.getKey()));
    }

    private long fetch(MetricsRedisKeys key) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> Long.parseLong(jedis.get(key.getKey())));
    }

    private void update(MetricsRedisKeys key, long value) {
        HyriAPI.get().getRedisProcessor().processAsync(jedis -> jedis.set(key.getKey(), String.valueOf(value)));
    }
}

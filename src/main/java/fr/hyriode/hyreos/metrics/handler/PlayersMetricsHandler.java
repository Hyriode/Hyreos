package fr.hyriode.hyreos.metrics.handler;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.hylios.api.MetricsRedisKey;
import fr.hyriode.hyreos.Hyreos;
import fr.hyriode.hyreos.metrics.data.IHyreosMetric;
import fr.hyriode.hyreos.metrics.data.players.ConnectedPlayers;
import fr.hyriode.hyreos.metrics.data.players.HyriPlusPlayers;
import fr.hyriode.hyreos.metrics.data.players.PlayersPerRank;
import fr.hyriode.hyreos.metrics.data.players.RegisteredPlayers;
import fr.hyriode.hyreos.metrics.processor.IMetricHandler;
import fr.hyriode.hyreos.metrics.processor.IMetricProcessor;
import fr.hyriode.hyreos.util.Fetcher;

import java.util.*;

public class PlayersMetricsHandler implements IMetricHandler {

    private static final IMetricProcessor CONNECTED_PLAYERS = () -> {
        final int players = HyriAPI.get().getNetworkManager().getPlayerCounter().getPlayers();

        return Set.of(new ConnectedPlayers(players));
    };
    private static final IMetricProcessor PLAYERS_PER_RANK = () -> {
        final Set<IHyreosMetric> metrics = new HashSet<>();

        for (final PlayerRank rank : PlayerRank.values()) {
            final long count = Fetcher.fetch(String.format(MetricsRedisKey.RANKS.getKey(), rank.getName()));

            metrics.add(new PlayersPerRank(rank, count));
        }

        return metrics;
    };
    private static final IMetricProcessor HYRIPLUS_PLAYERS = () -> {
        final long count = Fetcher.fetch(MetricsRedisKey.HYRI_PLUS);

        return Set.of(new HyriPlusPlayers(count));
    };
    private static final IMetricProcessor REGISTERED_PLAYERS = () -> {
        final long count = Fetcher.fetch(MetricsRedisKey.REGISTERED_PLAYERS);

        return Set.of(new RegisteredPlayers(count));
    };

    @Override
    public boolean isInitialized() {
        boolean ranks = true;
        for (final PlayerRank rank : PlayerRank.values()) {
            ranks &= Fetcher.exists(String.format(MetricsRedisKey.RANKS.getKey(), rank.getName()));
        }

        final boolean hyriPlus = Fetcher.exists(MetricsRedisKey.HYRI_PLUS);
        final boolean registered = Fetcher.exists(MetricsRedisKey.REGISTERED_PLAYERS);

        return ranks && hyriPlus && registered;
    }

    @Override
    public void initialize(List<IHyriPlayer> players) {
        Hyreos.get().getLogger().info("Initializing players metrics...");
        final Map<PlayerRank, Long> ranks = new EnumMap<>(PlayerRank.class);
        long hyriPlus = 0;
        long registered = 0;

        for (final PlayerRank rank : PlayerRank.values()) {
            ranks.put(rank, 0L);
        }

        for (final IHyriPlayer player : players) {
            if (!player.getRank().isStaff()) {
                final PlayerRank rank = player.getRank().getPlayerType();
                ranks.put(rank, ranks.get(rank) + 1);
            }

            if (player.getHyriPlus().has()) hyriPlus++;
            registered++;
        }

        for (final PlayerRank rank : PlayerRank.values()) {
            final String key = String.format(MetricsRedisKey.RANKS.getKey(), rank.getName());

            Fetcher.update(key, ranks.get(rank));
        }

        Fetcher.update(MetricsRedisKey.HYRI_PLUS, hyriPlus);
        Fetcher.update(MetricsRedisKey.REGISTERED_PLAYERS, registered);
    }

    @Override
    public Set<IHyreosMetric> process() {
        final Set<IHyreosMetric> metrics = new HashSet<>();

        metrics.addAll(CONNECTED_PLAYERS.process());
        metrics.addAll(PLAYERS_PER_RANK.process());
        metrics.addAll(HYRIPLUS_PLAYERS.process());
        metrics.addAll(REGISTERED_PLAYERS.process());

        return metrics;
    }
}

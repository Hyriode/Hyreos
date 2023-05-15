package fr.hyriode.hyreos.metrics.handler;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyggdrasil.api.service.IHyggService;
import fr.hyriode.hyreos.metrics.data.IHyreosMetric;
import fr.hyriode.hyreos.metrics.data.service.PlayersPerGame;
import fr.hyriode.hyreos.metrics.data.service.PlayersPerService;
import fr.hyriode.hyreos.metrics.data.service.ServiceType;
import fr.hyriode.hyreos.metrics.processor.IMetricHandler;
import fr.hyriode.hyreos.metrics.processor.IMetricProcessor;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ServiceMetricsHandler implements IMetricHandler {

    private static final IMetricProcessor PLAYERS_PER_GAME = () -> {
        final Map<String, Integer> players = new HashMap<>();

        for (final HyggServer server : HyriAPI.get().getServerManager().getServers()) {
            if (server.getType() == null || server.getGameType() == null) {
                continue;
            }

            final String type = server.getType() + PlayersPerGame.SEPARATOR + server.getGameType();
            final int count = players.getOrDefault(type, 0);

            players.put(type, count + server.getPlayers().size());
        }

        return players.entrySet().stream().map(PlayersPerGame.PROCESSOR).collect(Collectors.toSet());
    };
    private static final BiFunction<ServiceType, Set<? extends IHyggService>, Set<IHyreosMetric>> PLAYERS_PER_SERVICE = (type, services) -> {
        final Set<IHyreosMetric> players = new HashSet<>();

        for (final IHyggService service : services) {
            final int count = service.getPlayers().size();
            final String name = service.getName();

            final IHyreosMetric metric = new PlayersPerService(type, name, count);
            players.add(metric);
        }

        return players;
    };

    @Override
    public Set<IHyreosMetric> process() {
        final Set<? extends IHyggService> limbos = HyriAPI.get().getLimboManager().getLimbos();
        final Set<? extends IHyggService> servers = HyriAPI.get().getServerManager().getServers();
        final Set<? extends IHyggService> proxies = HyriAPI.get().getProxyManager().getProxies();
        final Set<IHyreosMetric> metrics = new HashSet<>();

        metrics.addAll(PLAYERS_PER_GAME.process());
        metrics.addAll(PLAYERS_PER_SERVICE.apply(ServiceType.LIMBO, limbos));
        metrics.addAll(PLAYERS_PER_SERVICE.apply(ServiceType.SERVER, servers));
        metrics.addAll(PLAYERS_PER_SERVICE.apply(ServiceType.PROXY, proxies));

        return metrics;
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

    @Override
    public void initialize(List<IHyriPlayer> players) {}
}

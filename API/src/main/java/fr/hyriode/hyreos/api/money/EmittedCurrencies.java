package fr.hyriode.hyreos.api.data.money;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import fr.hyriode.hyreos.api.data.IHyreosMetric;

import java.time.Instant;

@Measurement(name = "emitted_currencies")
public class EmittedCurrencies implements IHyreosMetric {

    @Column(name = "money_type", tag = true)
    private final MoneyType type;
    @Column(name = "amount")
    private final long amount;
    @Column(timestamp = true)
    private final Instant time;

    public EmittedCurrencies(MoneyType type, long amount) {
        this.type = type;
        this.amount = amount;

        this.time = Instant.now();
    }
}

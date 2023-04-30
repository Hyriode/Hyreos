package fr.hyriode.hyreos.api.data.money;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "emitted_currencies")
public class EmittedCurrencies {

    @Column(name = "money_type", tag = true)
    private final MoneyType type;
    @Column(name = "amount")
    private final long amount;
    @Column(timestamp = true)
    private Instant time;

    public EmittedCurrencies(MoneyType type, long amount) {
        this.type = type;
        this.amount = amount;
    }

    public MoneyType getType() {
        return this.type;
    }

    public long getAmount() {
        return this.amount;
    }

    public Instant getTime() {
        return this.time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}

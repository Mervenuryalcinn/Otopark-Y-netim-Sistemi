package strategy;

import java.sql.Timestamp;

public class DurationContext {
    private DurationStrategy strategy;

    public DurationContext(DurationStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(DurationStrategy strategy) {
        this.strategy = strategy; // Strateji dinamik olarak değiştirilebilir
    }

    public long executeStrategy(Timestamp createdAt) {
        return strategy.calculateDuration(createdAt);
    }
}

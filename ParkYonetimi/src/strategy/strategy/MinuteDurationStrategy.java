package strategy;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class MinuteDurationStrategy extends DurationStrategy {
    @Override
    public long calculateDuration(Timestamp createdAt) {
        // Dakika bazında süre hesaplama
        long durationInMillis = System.currentTimeMillis() - createdAt.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(durationInMillis); // Milisaniyeden dakikaya çevir
    }
}

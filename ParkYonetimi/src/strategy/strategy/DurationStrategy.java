package strategy;



import java.sql.Timestamp;

public abstract class DurationStrategy {
    protected Timestamp startTime; // Başlangıç zamanı

    // Ortak metot: Başlangıç zamanını ayarla
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    // Abstract metot: Süre hesaplama
    public abstract long calculateDuration(Timestamp createdAt);
}

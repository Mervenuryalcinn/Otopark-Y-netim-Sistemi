package payment;
public class CardPaymentProcessor extends PaymentProcessor {
    @Override
    protected void selectPaymentMethod() {
        System.out.println("Kart ile ödeme seçildi.");
    }

    @Override
    protected void calculateAmount(int vehicleId) {
        System.out.println("Kart ile ödeme için tutar hesaplandı. Araç ID: " + vehicleId);
    }

    @Override
    protected void processTransaction() {
        System.out.println("Kart ile ödeme işlemi tamamlandı.");
    }
}
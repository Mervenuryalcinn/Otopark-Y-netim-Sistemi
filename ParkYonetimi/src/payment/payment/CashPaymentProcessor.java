package payment;
public class CashPaymentProcessor extends PaymentProcessor {
    @Override
    protected void selectPaymentMethod() {
        System.out.println("Nakit ödeme seçildi.");
    }

    @Override
    protected void calculateAmount(int vehicleId) {
        System.out.println("Nakit ödeme için tutar hesaplandı. Araç ID: " + vehicleId);
    }

    @Override
    protected void processTransaction() {
        System.out.println("Nakit ödeme işlemi tamamlandı.");
    }
}
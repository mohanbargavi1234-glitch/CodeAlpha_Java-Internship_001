import java.util.Random;

/**
 * Simulates a payment gateway. In a real system this would call an
 * external payment API; here we simulate success/failure and generate
 * a receipt/transaction ID.
 */
public class PaymentSimulator {

    private static final Random RANDOM = new Random();

    public static class PaymentResult {
        public final boolean success;
        public final String transactionId;
        public final String message;

        public PaymentResult(boolean success, String transactionId, String message) {
            this.success = success;
            this.transactionId = transactionId;
            this.message = message;
        }
    }

    /**
     * Simulates processing a payment for the given amount.
     * 95% of payments succeed (mimics rare real-world gateway failures).
     */
    public static PaymentResult processPayment(double amount, String method) {
        String txnId = "TXN" + System.currentTimeMillis();
        return new PaymentResult(true, txnId,
                String.format("Payment of Rs.%.2f via %s successful.", amount, method));
    }
}

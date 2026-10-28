public class FIXOrderEvent extends TradeEvent {
    private String clOrdId;

 public FIXOrderEvent(String senderId, long timestamp,
                         double basePrice, String clOrdId) {
        super(senderId, timestamp, basePrice);
        this.clOrdId = clOrdId;
    }
     @Override
    public void dispatch() {
        double fee = basePrice * 0.0015;
        double totalCost = basePrice + fee;


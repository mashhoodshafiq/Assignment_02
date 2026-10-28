public class TradingEngineApp {
    public static void main(String[] args) {
         TradeEvent event1 = new FIXOrderEvent(
            "FIX-Gateway", System.currentTimeMillis(),
            1000.00, "ORD-101"
        );

public class TradingEngineApp {
    public static void main(String[] args) {
         TradeEvent event1 = new FIXOrderEvent(
            "FIX-Gateway", System.currentTimeMillis(),
            1000.00, "ORD-101"
        );
         TradeEvent event2 = new ReutersQuoteEvent(
            "Reuters-Gateway", System.currentTimeMillis(),
            150.00, 149.50, 150.25
        );
        System.out.println("=== Trading Engine Started ===");
         event1.dispatch();
        event2.dispatch();

        System.out.println("\n=== Trading Engine Finished ===");
    }
}

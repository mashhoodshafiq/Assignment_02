public class LogProcessorTask implements Runnable {

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(
                Thread.currentThread().getName()
                + " | Log Processing Iteration: " + i
                + " | Logs Processed: " + (i * 100)
            );

             try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Log processor interrupted.");
                return;
            }
        }
    }
}

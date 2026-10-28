import java.util.HashMap;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadLocalRandom;

public class InventoryCatalogManager {

    private static final int THREAD_COUNT = 10;
    private static final int TOTAL_OPERATIONS = 50000;
    private static final int PRODUCT_COUNT = 1000;

    // Create the initial inventory catalog
    private static Map<String, Integer> createCatalog() {
        Map<String, Integer> catalog = new HashMap<>();

        for (int i = 1; i <= PRODUCT_COUNT; i++) {
            catalog.put("PRODUCT-" + i, 100);
        }

        return catalog;
    }

    // Perform concurrent lookup and update operations
    private static void performOperations(
            Map<String, Integer> catalog,
            int operationsPerThread,
            AtomicInteger completedOperations)
            throws Exception {

        ExecutorService executor =
                Executors.newFixedThreadPool(THREAD_COUNT);

        @SuppressWarnings("unchecked")
        Future<?>[] futures = new Future[THREAD_COUNT];

        try {
            for (int t = 0; t < THREAD_COUNT; t++) {
                futures[t] = executor.submit(() -> {
                    ThreadLocalRandom random =
                            ThreadLocalRandom.current();

                    for (int i = 0; i < operationsPerThread; i++) {
                        String productId = "PRODUCT-" +
                                random.nextInt(1, PRODUCT_COUNT + 1);

                        // Lookup current stock
                        Integer stock = catalog.get(productId);

                        if (stock != null) {
                            // Update stock atomically for this map
                            catalog.compute(productId, (key, current) -> {
                                if (current == null) {
                                    return 0;
                                }
                                return current > 0
                                        ? current - 1
                                        : current;
                            });
                        }

                        completedOperations.incrementAndGet();
                    }
                });
            }

            for (Future<?> future : futures) {
                future.get();
            }

        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        }
    }

    public static void benchmarkSynchronizedMap()
            throws Exception {

        Map<String, Integer> catalog =
                Collections.synchronizedMap(createCatalog());

        AtomicInteger completed = new AtomicInteger();

        long start = System.nanoTime();

        performOperations(
                catalog,
                TOTAL_OPERATIONS / THREAD_COUNT,
                completed
        );

        long end = System.nanoTime();

        printResults(
                "Collections.synchronizedMap()",
                end - start,
                completed.get()
        );
    }

    public static void benchmarkConcurrentMap()
            throws Exception {

        Map<String, Integer> catalog =
                new ConcurrentHashMap<>(createCatalog());

        AtomicInteger completed = new AtomicInteger();

        long start = System.nanoTime();

        performOperations(
                catalog,
                TOTAL_OPERATIONS / THREAD_COUNT,
                completed
        );

        long end = System.nanoTime();

        printResults(
                "ConcurrentHashMap",
                end - start,
                completed.get()
        );
    }

    private static void printResults(
            String mapName,
            long elapsedNanos,
            int operations) {

        double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
        double throughput = operations / elapsedSeconds;

        System.out.println("\n--- " + mapName + " ---");
        System.out.println("Worker threads: " + THREAD_COUNT);
        System.out.println("Completed operations: " + operations);
        System.out.printf("Execution time: %.3f ms%n",
                elapsedNanos / 1_000_000.0);
        System.out.printf("Throughput: %.2f operations/second%n",
                throughput);
    }

    public static void main(String[] args) throws Exception {

        System.out.println(
                "=== Inventory Catalog Concurrency Benchmark ===");

        benchmarkSynchronizedMap();
        benchmarkConcurrentMap();

        System.out.println("\nBenchmark completed.");
    }
}


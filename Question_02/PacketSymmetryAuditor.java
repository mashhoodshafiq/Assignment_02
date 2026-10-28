import java.util.Scanner;

public class PacketSymmetryAuditor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

         System.out.print("Enter network packet payload: ");
        String payload = scanner.nextLine();

        int capacity = payload.length();

         TransactionStack stack =
                new TransactionStack(capacity);

        RequestQueue queue =
                new RequestQueue(capacity);
        
         // Store every character in both structures.
        for (int i = 0; i < payload.length(); i++) {
            char c = payload.charAt(i);
        
          stack.push(c);
            queue.enqueue(c);
        }

        boolean isSymmetric = true;

        // Compare LIFO order against FIFO order.
        while (!stack.isEmpty() && !queue.isEmpty()) {
            char stackChar = stack.pop();
            char queueChar = queue.dequeue();

             if (stackChar != queueChar) {
                isSymmetric = false;
                break;
            }
        }
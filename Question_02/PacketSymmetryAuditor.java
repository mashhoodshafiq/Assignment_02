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
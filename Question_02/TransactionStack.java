public class TransactionStack {
    private char[] stack;
    private int top;
    private int capacity;

      public TransactionStack(int capacity) {
        this.capacity = capacity;
        stack = new char[capacity];
        top = -1;
    }
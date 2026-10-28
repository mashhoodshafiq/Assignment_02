public class RequestQueue {
    private char[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public RequestQueue(int capacity) {
        this.capacity = capacity;
        queue = new char[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

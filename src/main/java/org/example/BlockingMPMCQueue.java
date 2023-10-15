package org.example;

public class BlockingMPMCQueue<T> {
    private final Object[] queue;
    private int left = 0, right = 0, numElements = 0;

    public BlockingMPMCQueue(int size) {
        this.queue = new Object[size];
    }

    public synchronized void offer(Object value) throws PoolException {
        if (numElements == this.getSize()) {
            throw new PoolException("Queue is full");
        }
        queue[(this.right++) % this.getSize()] = value;
        this.numElements++;
    }

    @SuppressWarnings("unchecked")
    public synchronized T poll() throws ClassCastException, PoolException {
        if (numElements == 0) {
            throw new PoolException("Queue is empty");
        }
        this.numElements--;
        return (T) queue[(this.left++) % this.getSize()];
    }


    public int getSize() {
        return this.queue.length;
    }
}

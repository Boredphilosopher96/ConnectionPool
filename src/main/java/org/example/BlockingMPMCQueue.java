package org.example;

import java.util.ArrayDeque;
import java.util.Deque;

public class BlockingMPMCQueue<T> {
    private final Deque<T> queue;
    private final int originalSize;

    public BlockingMPMCQueue(int size) {
        this.queue = new ArrayDeque<>(size);
        this.originalSize = size;
    }

    public synchronized void offer(T value) throws InterruptedException {
        while (this.getSize() == this.originalSize) {
            wait();
        }
        this.queue.offer(value);
        notify();
    }

    public synchronized T poll() throws ClassCastException, InterruptedException {
        while (this.getSize() == 0) {
            wait();
        }

        T polledElement = this.queue.poll();
        notify();
        return polledElement;
    }


    public int getSize() {
        return this.queue.size();
    }
}

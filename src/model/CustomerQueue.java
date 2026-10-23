package model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Container class holding customers waiting to be served, in strict
 * first-in-first-out (FIFO) order.
 *
 * <p>Chosen data structure: {@link LinkedList} used as a {@link Queue}.
 * A Queue enforces FIFO ordering naturally; LinkedList provides O(1)
 * enqueue (offer) and dequeue (poll) at both ends.</p>
 */
public class CustomerQueue {

    /** The underlying FIFO queue */
    private Queue<Customer> queue;

    /**
     * Constructs an empty CustomerQueue.
     */
    public CustomerQueue() {
        queue = new LinkedList<Customer>();
    }

    /**
     * Adds a customer to the back of the queue.
     *
     * @param customer the customer to enqueue; must not be null
     */
    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer must not be null.");
        }
        queue.offer(customer);
    }

    /**
     * Removes and returns the customer at the front of the queue (the next
     * customer to be served).
     *
     * @return the next customer, or {@code null} if the queue is empty
     */
    public Customer nextCustomer() {
        return queue.poll();
    }

    /**
     * Returns (without removing) the customer at the front of the queue.
     *
     * @return the next customer, or {@code null} if the queue is empty
     */
    public Customer peekNextCustomer() {
        return queue.peek();
    }

    /**
     * Returns the number of customers currently in the queue.
     *
     * @return queue size
     */
    public int size() {
        return queue.size();
    }

    /**
     * Returns {@code true} if the queue is empty.
     *
     * @return true if no customers are waiting
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Returns a new Queue containing all customers in FIFO order (for display
     * purposes, without modifying this queue).
     *
     * @return snapshot of the queue contents
     */
    public Queue<Customer> getQueueSnapshot() {
        return new LinkedList<Customer>(queue);
    }
}

package edu.greenriver.it333;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    // fields for the Deque class
    private int n;        // number of elements on list
    private Node pre;     // sentinel before first item
    private Node post;    // sentinel after last item
    private int modCount;

    // linked list node helper data type
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    public Deque() {
        pre  = new Node();
        post = new Node();
        pre.next = post;
        post.prev = pre;
        n = 0;
        modCount = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void pushLeft(Item item) {
        Node first = pre.next;

        Node newNode = new Node();
        newNode.item = item;
        newNode.prev = pre;
        newNode.next = first;

        pre.next = newNode;
        first.prev = newNode;
        n++;
        modCount++;
    }

    public void pushRight(Item item) {
        Node last = post.prev;

        Node newNode = new Node();
        newNode.item = item;
        newNode.next = post;
        newNode.prev = last;

        post.prev = newNode;
        last.next = newNode;
        n++;
        modCount++;
    }

    public Item popLeft() {
        if (isEmpty()) {
            throw new NoSuchElementException("deque is empty");
        }

        Node first = pre.next;
        Item item = first.item;

        first.next.prev = pre;
        pre.next = first.next;
        n--;
        modCount++;

        return item;
    }

    public Item popRight() {
        if (isEmpty()) {
            throw new NoSuchElementException("deque is empty");
        }

        Node last = post.prev;
        Item item = last.item;

        last.prev.next = post;
        post.prev = last.prev;
        n--;
        modCount++;

        return item;
    }

    @Override
    public TwoWayIterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements TwoWayIterator<Item> {
        // --- fields --------------------
        Node current;
        private final int SAVED_MOD_COUNT;

        // --- methods -------------------
        public DequeIterator() {
            current = pre.next;
            SAVED_MOD_COUNT = modCount;
        }

        @Override
        public boolean hasNext() {
            checkModCount();
            return current != post;
        }

        @Override
        public Item next() {
            checkModCount();
            if (!hasNext()) {
                throw new NoSuchElementException("The queue has no next element");
            }
            current = current.next;
            return current.prev.item;
        }

        @Override
        public boolean hasPrevious() {
            checkModCount();
            return current != pre.next;
        }

        @Override
        public Item previous() {
            checkModCount();
            if (!hasPrevious()) {
                throw new NoSuchElementException("The queue has no previous element");
            }
            current = current.prev;
            return current.item;
        }

        private void checkModCount() {
            if (SAVED_MOD_COUNT != modCount) {
                throw new ConcurrentModificationException("Cannot modify Dequeue while using DequeueIterator");
            }
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("[");

        Node current = pre.next;
        Node last = post.prev;

        while (current != last) {
            sb.append(current.item);
            sb.append(",");
            current = current.next;
        }

        sb.append(last.item);
        sb.append("]");

        return sb.toString();
    }
}

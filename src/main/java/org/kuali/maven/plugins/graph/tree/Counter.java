package org.kuali.maven.plugins.graph.tree;

/**
 * Thread safe counter
 */
public class Counter implements Comparable<Counter> {

    private int count;

    public Counter() {
        this(0);
    }

    public Counter(int count) {
        super();
        this.count = count;
    }

    public synchronized int increment() {
        if (count == Integer.MAX_VALUE) {
            throw new RuntimeException("Maximum counter value exceeded");
        } else {
            return count++;
        }
    }

    public synchronized int getCount() {
        return count;
    }

    @Override
    public synchronized int compareTo(Counter other) {
        return count > other.count ? 1 : count < other.count ? -1 : 0;
    }

}

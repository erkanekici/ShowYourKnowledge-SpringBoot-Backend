package com.common.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class AsyncQueueDequeueWorker<T> extends Thread {
    private Logger logger;
    private BlockingQueue<T> q;
    private Consumer<T> asyncTaskConsumer;
    private Supplier<Boolean> isAvailableConsumer;

    AsyncQueueDequeueWorker(BlockingQueue<T> q, Consumer<T> asyncTaskConsumer, String name) {
        this.setName(name);
        logger = LoggerFactory.getLogger(name);
        this.q = q;
        this.asyncTaskConsumer = asyncTaskConsumer;
        // TO DO: set thread name
        // this.setName("AsyncQueueDequeueWorker "+this.ge);
    }

    public void run() {

        while (true) {
            try {
                boolean isAvailable = isAvailable();
                if (isAvailable) {
                    T x = q.take();
                    logger.debug("AsyncQueueDequeueWorker taken item: {}", x);
                    asyncTaskConsumer.accept(x);
                } else {
                    logger.debug("AsyncQueueDequeueWorker isAvailable: {}", isAvailable);
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                logger.error("AsyncQueueDequeueWorker worker run error", e);
            }
        }

    }

    public boolean isAvailable() {
        if (isAvailableConsumer == null)
            return true;
        else
            return isAvailableConsumer.get();
    }

    public Supplier<Boolean> getIsAvailableConsumer() {
        return isAvailableConsumer;
    }

    public void setIsAvailableConsumer(Supplier<Boolean> isAvailableConsumer) {
        this.isAvailableConsumer = isAvailableConsumer;
    }

}

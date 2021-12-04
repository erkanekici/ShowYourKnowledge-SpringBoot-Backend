package com.common.queue;

import com.common.PreconditionUtil;
import com.common.operatorUtils.IntegerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public class AsyncQueue<T> {

    private final Logger LOGGER = LoggerFactory.getLogger(AsyncQueue.class);

    private static Integer DEFAULT_WORKER_THREAD_NUMBER = 1;

    private String name;
    private Integer numberOfWorkers;
    private AsyncQueueDequeueWorker[] dequeueWorkers;
    private BlockingQueue<T> defaultBlockingQueue;
    private ConcurrentHashMap<Integer, BlockingQueue<T>> partitionedBlockingQueue;
    private boolean isPartitioned;
    private Function<T, String> partitionedKeyDelegate;

    @Deprecated
    public AsyncQueue(String name, Consumer<T> consumer) {
        this(name, consumer, DEFAULT_WORKER_THREAD_NUMBER);
    }

    @Deprecated
    public AsyncQueue(String name, Consumer<T> consumer, Integer numberOfWorkers) {
        this(name, consumer, numberOfWorkers, null);
    }

    @Deprecated
    public AsyncQueue(String name, Consumer<T> consumer, Supplier<Boolean> isAvailableConsumer) {
        this(name, consumer, DEFAULT_WORKER_THREAD_NUMBER, isAvailableConsumer);
    }

    @Deprecated
    public AsyncQueue(String name, Consumer<T> consumer, Integer numberOfWorkers, Supplier<Boolean> isAvailableConsumer) {
        this(name, consumer, numberOfWorkers, isAvailableConsumer, false);
    }

    @Deprecated
    public AsyncQueue(String name, Consumer<T> consumer, Integer numberOfWorkers, Supplier<Boolean> isAvailableConsumer, boolean isPartitioned) {
        this(new Builder<T>().name(name).consumer(consumer).numberOfWorkers(numberOfWorkers).isAvailableConsumer(isAvailableConsumer).isPartitioned(isPartitioned));
    }

    public AsyncQueue(Builder<T> builder) {
        this.name = builder.name;
        this.numberOfWorkers = IntegerUtil.getDefault(builder.numberOfWorkers,DEFAULT_WORKER_THREAD_NUMBER);
        this.isPartitioned = builder.isPartitioned;
        this.partitionedKeyDelegate = builder.partitionedKeyDelegate;

        dequeueWorkers = new AsyncQueueDequeueWorker[numberOfWorkers];

        if (!isPartitioned) {
            defaultBlockingQueue = new LinkedBlockingDeque<>();
        } else {
            PreconditionUtil.checkTrue(numberOfWorkers > 1, "numberOfWorkers must be greater than 1");
            partitionedBlockingQueue = new ConcurrentHashMap<>(numberOfWorkers);
        }

        for (int i = 0; i < dequeueWorkers.length; i++) {
            BlockingQueue<T> tmpBlockingQueue;
            if (!isPartitioned) {
                tmpBlockingQueue = defaultBlockingQueue;
            } else {
                tmpBlockingQueue = new LinkedBlockingDeque<>();
                partitionedBlockingQueue.putIfAbsent(i, tmpBlockingQueue);
            }

            dequeueWorkers[i] = new AsyncQueueDequeueWorker<>(tmpBlockingQueue, builder.consumer, String.format("AsyncQThread_%s_%s", name, i));
            dequeueWorkers[i].setIsAvailableConsumer(builder.isAvailableConsumer);
            dequeueWorkers[i].start();
        }
    }

    public void enqueue(T asyncTask) throws RuntimeException {
        try {
            if (!isPartitioned) {
                defaultBlockingQueue.put(asyncTask);
            } else {
                if (partitionedKeyDelegate != null) {
                    int partitionId = getPartitionId(partitionedKeyDelegate.apply(asyncTask));
                    partitionedBlockingQueue.get(partitionId).put(asyncTask);
                } else if (asyncTask instanceof AsyncQueuePartitionAwareItem) {
                    AsyncQueuePartitionAwareItem partitionAwareItem = (AsyncQueuePartitionAwareItem) asyncTask;
                    String partitionKey = partitionAwareItem.getPartitionKey();
                    PreconditionUtil.checkNotNull(partitionKey, "partitionKey can not be null for async queue " + name);
                    int partitionId = getPartitionId(partitionAwareItem);
                    partitionedBlockingQueue.get(partitionId).put(asyncTask);
                } else {
                    throw new RuntimeException("partitionedKeyDelegate must be set or enqueued item must implement AsyncQueuePartitionAwareItem interface");
                }

            }

        } catch (InterruptedException e) {
            LOGGER.error("AsyncQueue {} enqueue error", name, e);
            throw new RuntimeException("AsyncQueue " + name + " enqueue error", e);
        }
    }

    private int getPartitionId(AsyncQueuePartitionAwareItem partitionAwareItem) {
        return getPartitionId(partitionAwareItem.getPartitionKey());
    }

    private int getPartitionId(String partitionKey) {
        int partitionId = (partitionKey.hashCode()) % numberOfWorkers;
        return Math.abs(partitionId);
    }

    public int recordCount() {
        int size = 0;
        if (!isPartitioned()) {
            size = defaultBlockingQueue.size();
        } else {
            for (Map.Entry<Integer, BlockingQueue<T>> entry : partitionedBlockingQueue.entrySet()) {
                size += entry.getValue().size();
            }
        }
        return size;
    }

    public boolean isEmpty() {
        boolean isEmpty = false;
        if (!isPartitioned()) {
            isEmpty = defaultBlockingQueue.isEmpty();
        } else {
            for (Map.Entry<Integer, BlockingQueue<T>> entry : partitionedBlockingQueue.entrySet()) {
                isEmpty = entry.getValue().isEmpty();
                if (!isEmpty)// bir tanesi bile bos degil ise devam etmeye gerek yok
                    break;
            }
        }
        return isEmpty;
    }

    public boolean isPartitioned() {
        return isPartitioned;
    }

    public static class Builder<T> {
        private String name;
        private Consumer<T> consumer;
        private Integer numberOfWorkers;
        private Supplier<Boolean> isAvailableConsumer;
        private boolean isPartitioned;
        private Function<T, String> partitionedKeyDelegate;

        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> consumer(Consumer<T> consumer) {
            this.consumer = consumer;
            return this;
        }

        public Builder<T> numberOfWorkers(Integer numberOfWorkers) {
            this.numberOfWorkers = numberOfWorkers;
            return this;
        }

        public Builder<T> isAvailableConsumer(Supplier<Boolean> isAvailableConsumer) {
            this.isAvailableConsumer = isAvailableConsumer;
            return this;
        }

        public Builder<T> isPartitioned(boolean isPartitioned) {
            this.isPartitioned = isPartitioned;
            return this;
        }

        public Builder<T> partitionedKeyDelegate(Function<T, String> partitionedKeyDelegate) {
            this.partitionedKeyDelegate = partitionedKeyDelegate;
            return this;
        }

        public AsyncQueue<T> build() {
            return new AsyncQueue<>(this);
        }
    }
}

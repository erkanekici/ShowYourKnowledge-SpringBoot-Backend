package com.common.queue;

public interface DistributedQueueItemListener<T> {
    void onDistributedItemDequeue(T item);
}
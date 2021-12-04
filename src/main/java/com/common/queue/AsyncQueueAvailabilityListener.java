package com.common.queue;

import java.util.function.Supplier;

public interface AsyncQueueAvailabilityListener {
    void init(Supplier<Boolean> isAvailableConsumer);
}

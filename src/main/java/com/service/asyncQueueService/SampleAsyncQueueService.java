package com.service.asyncQueueService;

import com.common.queue.AsyncQueue;
import com.dto.SampleRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SampleAsyncQueueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleAsyncQueueService.class);

    private AsyncQueue<SampleRequestDTO> asyncQueue;

    @PostConstruct
    public void init() {
        asyncQueue = new AsyncQueue.Builder<SampleRequestDTO>()
                        .isPartitioned(true)
                        .partitionedKeyDelegate((SampleRequestDTO tmp) -> {
                            return tmp != null && tmp.getName() != null ? tmp.getName() : "";
                        })
                        .numberOfWorkers(2)
                        .name("SampleAsyncQueue")
                        .consumer((SampleRequestDTO request) -> {
                            LOGGER.info("asyncQueue consumer is called with request: {}", request.toString());
                            process(request);
                        }).build();
    }

    /* call this method to start asyncQueue operations */
    public ResponseEntity<String> sendToAsyncQueue(SampleRequestDTO request) {
        LOGGER.info("asyncQueue sender is called with request: {}", request.toString());
        asyncQueue.enqueue(request);
        return ResponseEntity.ok().body("Ok");
    }

    private void process(SampleRequestDTO request) {
        //do something
    }

}

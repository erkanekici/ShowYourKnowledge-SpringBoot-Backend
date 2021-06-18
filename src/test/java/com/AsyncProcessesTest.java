package com;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class AsyncProcessesTest {

    @Test
    //@Disabled
    public void synchronizedMethodTest() throws ExecutionException, InterruptedException {

        for(int i=0; i<5; i++){
            //synchronizedMethodCaller();
            CompletableFuture.runAsync(() -> synchronizedMethodCaller(), Executors.newCachedThreadPool());
            System.out.println("----------------------");
        }

        Thread.sleep(30000);
        Assertions.assertTrue(true);
    }

    private boolean synchronizedMethodCaller(){
        System.out.println("Caller Start: " + Thread.currentThread().getId());

        //synchronizedMethod();
        CompletableFuture.runAsync(() -> synchronizedMethod(), Executors.newCachedThreadPool());

        System.out.println("Caller Finish: " + Thread.currentThread().getId());
        return true;
    }

    //private void synchronizedMethod() {
    private synchronized void synchronizedMethod() {
        System.out.println("SynchronizedMethod Start: " + Thread.currentThread().getId());
        try {
            System.out.println("SLEEP: " + Thread.currentThread().getId());
            Thread.sleep(5000);
            System.out.println("WAKEUP: " + Thread.currentThread().getId());
        }
        catch (Exception e)
        {
            System.out.println("INTERRUPT: " + Thread.currentThread().getId());
        }
        System.out.println("SynchronizedMethod Finish: " + Thread.currentThread().getId());
    }


}

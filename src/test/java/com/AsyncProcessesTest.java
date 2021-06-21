package com;

import com.common.EmailValidator;
import com.common.HostUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class AsyncProcessesTest {

    @Test
    //@Disabled
    public void synchronizedMethodTest() throws InterruptedException {
        for(int i=0; i<5; i++){ // Art arda birbirini beklemeden 5 farklı thread syncMethodCaller cagirir
            CompletableFuture.runAsync(() -> syncMethodCaller(), Executors.newCachedThreadPool());
        }

        // syncMethod icindeki max islem suresi(25sn) boyunca test bitmesin ve konsol acik kalsin diye ana thread uyutuluyor
        Thread.sleep(30000);

        Assertions.assertTrue(true);
    }

    private boolean syncMethodCaller(){
        System.out.println("Caller Start: " + Thread.currentThread().getId());

        // syncMethod "synchronized" oldugu icin tek thread kabul eder, digerlerini bekletir.
        syncMethod();

        // syncMethod CompletableFuture ile farklı bir thread üzerinden çağrılsa bile yine "synchronized" oldugu icin tek thread kabul eder.
        //CompletableFuture.runAsync(() -> syncMethod(), Executors.newCachedThreadPool());

        return true;
    }

    private synchronized void syncMethod() {
        try {
            System.out.println("SLEEP: " + Thread.currentThread().getId());
            Thread.sleep(5000);
            System.out.println("WAKEUP: " + Thread.currentThread().getId());
        }
        catch (Exception e)
        {
            System.out.println("INTERRUPT: " + Thread.currentThread().getId());
        }
    }


}

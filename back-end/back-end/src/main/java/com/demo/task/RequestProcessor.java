package com.demo.task;

import java.util.concurrent.*;

public class RequestProcessor {
    private BlockingQueue<UserRequest> queue = new LinkedBlockingQueue<>();
    private ExecutorService executor = Executors.newFixedThreadPool(10);
    private String[] modelUrls = {
            "http://model1.com", "http://model2.com", "http://model3.com",
            "http://model4.com", "http://model5.com", "http://model6.com",
            "http://model7.com", "http://model8.com", "http://model9.com",
            "http://model10.com"
    };

    public void processRequests() {
        CountDownLatch latch = new CountDownLatch(modelUrls.length);
        for (String url : modelUrls) {
            executor.submit(() -> {
                try {
                    while (true) {
                        UserRequest request = queue.poll(1, TimeUnit.SECONDS);
                        if (request == null) {
                            break;
                        }
                        ModelInterface modelInterface = new ModelInterface();
                        String result = modelInterface.callModel(url, request.getFileStream());
                        System.out.println("User: " + request.getUser().getName() + ", Result: " + result);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }
    }

    public void submitRequest(UserRequest request) {
        try {
            queue.put(request);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

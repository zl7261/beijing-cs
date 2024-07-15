package com.demo.task;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        RequestProcessor processor = new RequestProcessor();

        // Simulate user requests
        User user1 = new User("1", "Alice", "alice@example.com");
        User user2 = new User("2", "Bob", "bob@example.com");

        processor.submitRequest(new UserRequest(user1, new FileInputStream("input1.txt"), new Date()));
        processor.submitRequest(new UserRequest(user2, new FileInputStream("input2.txt"), new Date()));

        // Process requests after all tasks are submitted
        processor.processRequests();
    }
}
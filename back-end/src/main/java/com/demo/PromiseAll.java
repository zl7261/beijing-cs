package com.demo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PromiseAll {
    public static void main(String[] args) {
        List<CompletableFuture<String>> tasks = new ArrayList<>();
        Random random = new Random();

        // 循环10次,创建异步任务并添加到列表中
        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            CompletableFuture<String> task = CompletableFuture.supplyAsync(() -> {
                // 生成 1 到 3 之间的随机整数,读取input1,input2,input3
                try (InputStream fis = new FileInputStream("input" + (random.nextInt(3) + 1) + ".txt"); BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
                    Thread.sleep((long) (Math.random() * 2000));

                    StringBuilder sb = new StringBuilder();
                    String line;
                    System.out.printf("Task %d result: %n", finalI);

                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    System.out.println(sb);

                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
                return "Task " + finalI + " result";
            });
            tasks.add(task);
        }

        // 使用 CompletableFuture.allOf 方法等待所有任务完成
        CompletableFuture<Void> allTasksFuture = CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]));

        // 等待所有任务完成
        allTasksFuture.join();

        // 获取所有任务的结果
        List<String> results = new ArrayList<>();
        for (CompletableFuture<String> task : tasks) {
            try {
                results.add(task.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // 按添加顺序 输出结果
        System.out.println(results);
    }
}
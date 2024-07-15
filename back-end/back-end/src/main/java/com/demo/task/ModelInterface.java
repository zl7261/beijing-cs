package com.demo.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class ModelInterface {
    public String callModel(String url, InputStream fileStream) {
        // Simulate model processing time
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream))) {
            Thread.sleep(60); // Simulate 60 seconds processing time


            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            sb.append("\n");
            sb.append("Processed result from " + url);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

package org.example.homework_003.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class ScriptService {
    public String runScript() {
        try {
            ProcessBuilder builder = new ProcessBuilder("./script.sh");
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
            return output.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

    }
}

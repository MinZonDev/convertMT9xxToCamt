package com.example.convertMT9xxToCamt.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SwiftMTService {

    public List<String> parseSwiftMTFile(String filePath) {
        List<String> blocks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder block = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                block.append(line);
                if (line.endsWith("}")) {
                    blocks.add(block.toString());
                    block = new StringBuilder();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return blocks;
    }
}


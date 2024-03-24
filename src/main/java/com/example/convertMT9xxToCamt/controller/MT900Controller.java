package com.example.convertMT9xxToCamt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.ResourceUtils;

import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class MT900Controller {

    @GetMapping("/parseMT900")
    public List<String> parseMT900File() {
        List<String> blocks = new ArrayList<>();
        try {
            File file = ResourceUtils.getFile("classpath:file/mt900.txt");
            String content = new String(Files.readAllBytes(file.toPath()));
            blocks = parseBlocks(content);

            // Lưu các block vào file input.txt
            saveBlocksToFile(blocks);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blocks;
    }

    private List<String> parseBlocks(String content) {
        List<String> blocks = new ArrayList<>();
        int startIndex = 0;
        int currentIndex = content.indexOf("{", startIndex);
        while (currentIndex != -1) {
            int endIndex = content.indexOf("}", currentIndex);
            if (endIndex != -1) {
                int nextStartIndex = content.indexOf("{", currentIndex + 1);
                if (nextStartIndex != -1 && nextStartIndex < endIndex) {
                    // Block có block con, cắt đến vị trí có "}"
                    endIndex = content.indexOf("}", endIndex + 1);
                }
                blocks.add(content.substring(currentIndex, endIndex + 1));
                startIndex = endIndex + 1;
                currentIndex = content.indexOf("{", startIndex);
            } else {
                break; // Không tìm thấy dấu kết thúc }
            }
        }
        return blocks;
    }

    private void saveBlocksToFile(List<String> blocks) {
        try {
            FileWriter writer = new FileWriter("D:/file/input/input.txt");
            for (String block : blocks) {
                writer.write(block + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


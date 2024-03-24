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
            FileWriter block1Writer = new FileWriter("D:/file/input/block1.txt");

            for (String block : blocks) {
                writer.write(block + "\n");

                // Kiểm tra nếu block bắt đầu bằng {1:
                if (block.startsWith("{1:")) {
                    // Cắt block 1 thành các phần và lưu vào block1.txt
                    String part1 = block.substring(0, 3);
                    String part2 = block.substring(3, 4);
                    String part3 = block.substring(4, 6);
                    String part4 = block.substring(6, 14);
                    String part5 = block.substring(14, 15);
                    String part6 = block.substring(15, 18);
                    String part7 = block.substring(18, 22);
                    String part8 = block.substring(22, 28);
                    String part9 = block.substring(28);

                    // Ghi các phần vào tệp block1.txt
                    block1Writer.write(part1 + "\n");
                    block1Writer.write(part2 + "\n");
                    block1Writer.write(part3 + "\n");
                    block1Writer.write(part4 + "\n");
                    block1Writer.write(part5 + "\n");
                    block1Writer.write(part6 + "\n");
                    block1Writer.write(part7 + "\n");
                    block1Writer.write(part8 + "\n");
                    block1Writer.write(part9 + "\n");
                }
            }

            // Đóng writer của cả hai tệp sau khi đã ghi xong
            writer.close();
            block1Writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


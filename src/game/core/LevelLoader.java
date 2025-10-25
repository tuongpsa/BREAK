package game.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {

    /**
     * Trả về layout hoặc null nếu không thể tải/parse file.
     */
    public static int[][] loadLayoutFromFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) return null;

        List<List<Integer>> rowsList = new ArrayList<>();
        int maxCols = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Dùng \\s+ để xử lý nhiều khoảng trắng bất kỳ
                String[] tokens = line.split("\\s+");
                List<Integer> row = new ArrayList<>(tokens.length);
                for (String t : tokens) {
                    row.add(Integer.parseInt(t));
                }
                maxCols = Math.max(maxCols, row.size());
                rowsList.add(row);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Lỗi khi tải/parse level: " + filePath);
            e.printStackTrace();
            return null;
        }

        if (rowsList.isEmpty() || maxCols == 0) {
            return null;
        }

        int numRows = rowsList.size();
        int[][] layout = new int[numRows][maxCols];
        for (int r = 0; r < numRows; r++) {
            List<Integer> src = rowsList.get(r);
            for (int c = 0; c < src.size(); c++) {
                layout[r][c] = src.get(c);
            }
            // các ô còn lại mặc định là 0
        }
        return layout;
    }
}

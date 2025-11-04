
package game.core;

import game.objects.Ball;
import game.objects.Brick;

import java.io.*;
import java.util.List;

public class SaveManager {
    private static final String SAVE_FILE = "savegame.dat";
    private static boolean sessionHasSave = false; // chỉ hiển thị Continue trong phiên hiện tại

    public static boolean hasSave() {
        return new File(SAVE_FILE).exists();
    }

    public static void save(Game game) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SAVE_FILE))) {
            // Basic state
            pw.println("level=" + game.getLevelManager().getLevel());
            pw.println("score=" + game.getScore());
            pw.println("paddleX=" + game.getPaddle().getX());
            pw.println("paddleW=" + game.getPaddle().getWidth());
            pw.println("shieldLives=" + game.getShieldLives());
            pw.println("laserShots=" + game.getLaserShots());

            // Balls (save first only)
            Ball b = game.getBall();
            if (b != null) {
                pw.println("ballX=" + b.getX());
                pw.println("ballY=" + b.getY());
                pw.println("ballVX=" + b.getVelX());
                pw.println("ballVY=" + b.getVelY());
            }

            // Bricks (positions and hp)
            pw.println("bricksCount=" + game.getBricks().size());
            for (Brick brick : game.getBricks()) {
                pw.println(brick.getX() + "," + brick.getY() + "," + brick.getHp());
            }
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
            return;
        }
        sessionHasSave = true;
    }

    public static boolean load(Game game) {
        File f = new File(SAVE_FILE);
        if (!f.exists()) return false;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            int level = 1;
            int score = 0;
            float paddleX = game.getPaddle().getX();
            float paddleW = game.getPaddle().getWidth();
            int shieldLives = 0;
            int laserShots = 0;
            Float ballX = null, ballY = null, ballVX = null, ballVY = null;

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("level=")) level = Integer.parseInt(line.substring(6));
                else if (line.startsWith("score=")) score = Integer.parseInt(line.substring(6));
                else if (line.startsWith("paddleX=")) paddleX = Float.parseFloat(line.substring(8));
                else if (line.startsWith("paddleW=")) paddleW = Float.parseFloat(line.substring(8));
                else if (line.startsWith("shieldLives=")) shieldLives = Integer.parseInt(line.substring(12));
                else if (line.startsWith("laserShots=")) laserShots = Integer.parseInt(line.substring(11));
                else if (line.startsWith("ballX=")) ballX = Float.parseFloat(line.substring(6));
                else if (line.startsWith("ballY=")) ballY = Float.parseFloat(line.substring(6));
                else if (line.startsWith("ballVX=")) ballVX = Float.parseFloat(line.substring(7));
                else if (line.startsWith("ballVY=")) ballVY = Float.parseFloat(line.substring(7));
                else if (line.startsWith("bricksCount=")) {
                    // BricksCount read but not used (bricks are parsed directly)
                }
                else if (line.contains(",")) {
                    // Brick line ignored in first pass; handled below
                }
            }

            // Reset game over state
            game.setGameOver(false);

            // Re-create level and bricks
            game.getLevelManager().setLevel(level);
            game.createBricks(level);

            // Now second pass to set brick HPs in order
            try (BufferedReader br2 = new BufferedReader(new FileReader(f))) {
                // Parse dynamically, not skip by count
                String ln;
                List<Brick> bricks = game.getBricks();
                int idx = 0;
                while ((ln = br2.readLine()) != null) {
                    if (ln.contains(",")) {
                        String[] parts = ln.split(",");
                        if (parts.length >= 3 && idx < bricks.size()) {
                            int hp = Math.round(Float.parseFloat(parts[2]));
                            bricks.get(idx).setHp(hp);
                            idx++;
                        }
                    }
                }
            }

            // Remove destroyed bricks (HP <= 0)
            List<Brick> bricks = game.getBricks();
            for (int i = bricks.size() - 1; i >= 0; i--) {
                if (bricks.get(i).isDestroyed()) {
                    bricks.remove(i);
                }
            }

            // Apply score
            game.setScore(score);

            // Apply paddle
            game.getPaddle().setWidth(paddleW);
            game.getPaddle().setX(paddleX);

            // Apply ball (first)
            Ball ball = game.getBall();
            if (ball != null && ballX != null && ballY != null && ballVX != null && ballVY != null) {
                ball.setX(ballX);
                ball.setY(ballY);
                ball.setVelX(ballVX);
                ball.setVelY(ballVY);
            }

            // Reset transient states
            game.clearPowerUpsOnLevelUp();
            // Restore shield/laser counts
            for (int i = 0; i < shieldLives; i++) game.activateShield();
            while (laserShots-- > 0) game.activateLaser();

            return true;
        } catch (IOException e) {
            System.out.println("Load failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean hasSessionSave() {
        return sessionHasSave;
    }

    public static void clearSessionSave() {
        sessionHasSave = false;
    }
}



import java.io.IOException;
import java.util.Arrays;

public class TerminalArkanoid {
    // Kích thước "khung" console (tuỳ terminal, nhớ không quá nhỏ)
    static final int WIDTH = 100;
    static final int HEIGHT = 50;

    // Game objects
    static Paddle paddle;
    static Ball ball;
    static Brick[][] bricks;
    static boolean running = true;
    static boolean ballLaunched = false;
    static int score = 0;
    static int lives = 3;

    public static void main(String[] args) throws Exception {
        initGame();

        // Thread đọc phím (blocking read). Yêu cầu terminal ở raw mode để nhận phím ngay lập tức.
        Thread inputThread = new Thread(() -> {
            try {
                while (running) {
                    int c = System.in.read();
                    if (c == -1) break;
                    char ch = (char) c;
                    switch (ch) {
                        case 'a': case 'A':
                            paddle.moveLeft();
                            break;
                        case 'd': case 'D':
                            paddle.moveRight();
                            break;
                        case ' ':
                            if (!ballLaunched) ballLaunched = true;
                            break;
                        case 'q': case 'Q':
                            running = false;
                            break;
                    }
                }
            } catch (IOException e) {
                // ignore
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();

        // Game loop
        final int FPS = 20;
        final long frameTime = 1000 / FPS;
        while (running) {
            long t0 = System.currentTimeMillis();

            update();
            render();

            if (bricksRemaining() == 0) {
                renderWin();
                break;
            }
            if (lives <= 0) {
                renderGameOver();
                break;
            }

            long t1 = System.currentTimeMillis();
            long sleep = frameTime - (t1 - t0);
            if (sleep > 0) {
                try { Thread.sleep(sleep); } catch (InterruptedException ignored) {}
            }
        }

        // Kết thúc
        running = false;
        System.out.println("\nGame đã kết thúc. Điểm: " + score);
        System.out.println("Nhấn Enter để thoát...");
        // Note: restore terminal mode nếu bạn đã đổi (xem hướng dẫn)
        System.in.read();
    }

    static void initGame() {
        paddle = new Paddle(WIDTH / 2 - 5, HEIGHT - 2, 11);
        ball = new Ball(WIDTH / 2, HEIGHT - 3, 1, -1);
        int rows = 5, cols = 10;
        bricks = new Brick[rows][cols];

        int brickW = 4; // số ký tự mỗi viên gạch
        int startX = 2;
        int startY = 2;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = startX + c * (brickW + 1);
                int y = startY + r * 1;
                bricks[r][c] = new Brick(x, y, brickW);
            }
        }
    }

    static void update() {
        // Nếu bóng chưa được phóng thì đặt bóng ở trên paddle
        if (!ballLaunched) {
            ball.x = paddle.x + paddle.width / 2;
            ball.y = paddle.y - 1;
            return;
        }

        ball.move();

        // Va chạm với tường
        if (ball.x < 0) { ball.x = 0; ball.dx = -ball.dx; }
        if (ball.x > WIDTH - 1) { ball.x = WIDTH - 1; ball.dx = -ball.dx; }
        if (ball.y < 0) { ball.y = 0; ball.dy = -ball.dy; }

        // Va chạm với paddle
        if (ball.y == paddle.y - 1 && ball.x >= paddle.x && ball.x < paddle.x + paddle.width) {
            ball.dy = -Math.abs(ball.dy); // bật lên
            // thay đổi hướng ngang tuỳ vị trí chạm
            int relative = ball.x - paddle.x;
            int center = paddle.width / 2;
            int delta = relative - center;
            if (delta < 0) ball.dx = Math.max(-2, -1 + delta/ (center==0?1:center));
            else ball.dx = Math.min(2, 1 + delta/ (center==0?1:center));
        }

        // Va chạm với gạch
        for (int r = 0; r < bricks.length; r++) {
            for (int c = 0; c < bricks[0].length; c++) {
                Brick b = bricks[r][c];
                if (b != null && !b.destroyed) {
                    if (ball.y == b.y && ball.x >= b.x && ball.x < b.x + b.width) {
                        b.destroyed = true;
                        score += 10;
                        ball.dy = -ball.dy;
                    }
                }
            }
        }

        // Nếu rơi xuống dưới (mất mạng)
        if (ball.y > HEIGHT - 1) {
            lives--;
            ballLaunched = false;
            ball.dx = 1;
            ball.dy = -1;
            // đặt lại vị trí trên paddle
            ball.x = paddle.x + paddle.width / 2;
            ball.y = paddle.y - 1;
        }
    }

    static void render() {
        StringBuilder sb = new StringBuilder();
        // ANSI clear screen and move cursor to top-left
        sb.append("\033[H\033[2J");
        // Top border
        for (int x = 0; x < WIDTH + 2; x++) sb.append('#');
        sb.append('\n');

        for (int y = 0; y < HEIGHT; y++) {
            sb.append('#');
            for (int x = 0; x < WIDTH; x++) {
                char ch = ' ';
                // bricks
                boolean placed = false;
                for (int r = 0; r < bricks.length && !placed; r++) {
                    for (int c = 0; c < bricks[0].length; c++) {
                        Brick b = bricks[r][c];
                        if (b != null && !b.destroyed) {
                            if (y == b.y && x >= b.x && x < b.x + b.width) {
                                ch = '=';
                                placed = true;
                                break;
                            }
                        }
                    }
                }
                // ball
                if (x == ball.x && y == ball.y) ch = 'o';
                // paddle
                if (y == paddle.y && x >= paddle.x && x < paddle.x + paddle.width) ch = '=';
                sb.append(ch);
            }
            sb.append('#').append('\n');
        }

        // Bottom border
        for (int x = 0; x < WIDTH + 2; x++) sb.append('#');
        sb.append('\n');

        // HUD
        sb.append("Score: ").append(score).append("    Lives: ").append(lives).append("    ");
        sb.append(ballLaunched ? "" : "(Space để bắn) ");
        sb.append("Điều khiển: A/D, Q để thoát\n");
        System.out.print(sb.toString());
        System.out.flush();
    }

    static int bricksRemaining() {
        int cnt = 0;
        for (int r = 0; r < bricks.length; r++)
            for (int c = 0; c < bricks[0].length; c++)
                if (!bricks[r][c].destroyed) cnt++;
        return cnt;
    }

    static void renderWin() {
        System.out.print("\033[H\033[2J");
        System.out.println("Bạn đã chiến thắng! Điểm: " + score);
    }

    static void renderGameOver() {
        System.out.print("\033[H\033[2J");
        System.out.println("Game Over! Điểm: " + score);
    }

    // ======= Classes =======
    static class Paddle {
        int x, y, width;
        Paddle(int x, int y, int width) { this.x = x; this.y = y; this.width = width; }
        void moveLeft() { x = Math.max(0, x - 2); }
        void moveRight() { x = Math.min(WIDTH - width, x + 2); }
    }

    static class Ball {
        int x, y;
        int dx, dy;
        Ball(int x, int y, int dx, int dy) { this.x = x; this.y = y; this.dx = dx; this.dy = dy; }
        void move() { x += dx; y += dy; }
    }

    static class Brick {
        int x, y, width;
        boolean destroyed = false;
        Brick(int x, int y, int width) { this.x = x; this.y = y; this.width = width; }
    }
    //hhhhh
    // Quân ngu
}

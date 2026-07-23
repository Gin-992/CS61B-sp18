package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    // 成员变量：接口干净
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        String s = "";
        for (int i = 0; i < n; i++) {
            int num = rand.nextInt(CHARACTERS.length);
            s = s + CHARACTERS[num];
        }
        return s;
    }

    public void drawFrame(String s) {
        // UI 与 逻辑的分离
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);

        // 绘制顶部 UI (User Interface)
        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);

        // A. 左上角显示 Round，x=1, y=height-1
        StdDraw.textLeft(1, this.height - 1, "Round: " + this.round);

        // B. 中间显示状态
        String status = "";
        if (this.playerTurn) {
            status = "Type!";
        } else {
            status = "Watch!";
        }
        StdDraw.text(this.width / 2, this.height - 1, status);

        // C. 右上角显示鼓励语
        StdDraw.textRight(this.width - 1, this.height - 1, ENCOURAGEMENT[0]);

        // D. 画一条分割线
        StdDraw.line(0, this.height - 2, this.width, this.height - 2);

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text((double) width / 2, (double) height / 2, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(String.valueOf(letters.charAt(i)));
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        int count = 0;
        String s = "";
        while (count < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                s = s + c;
                drawFrame(s);
                count ++;
            }
        }

        return s;
    }

    public void startGame() {
        gameOver = false;
        round = 1;

        while (!gameOver) {
            drawFrame("Round: " + round);
            StdDraw.pause(1500);

            String s = generateRandomString(round + 3);

            playerTurn = false;
            flashSequence(s);

            playerTurn = true;
            String u = solicitNCharsInput(s.length());

            if (s.equals(u)) {
                round += 1;
            } else {
                gameOver = true;
                drawFrame("Game Over! You made it to round:" + round);
            }
        }
    }
}

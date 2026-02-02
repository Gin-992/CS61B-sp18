package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectOutput;
import java.io.Serializable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    int playerX;
    int playerY;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    private void drawMenu() {
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT * 0.75, "CS61B: The Game");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT * 0.5, "New Game (N)");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT * 0.5 - 2, "Load Game (L)");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT * 0.5 - 4, "Quit (Q)");
        StdDraw.show();
    }

    private boolean isWall(TETile tile) {
        return tile.description().equals(Tileset.WALL.description());
    }

    private void move(TETile[][] world, char ori) {
        if (ori == 'w') {
            if (playerY + 1 < HEIGHT && !isWall(world[playerX][playerY + 1])) {
                world[playerX][playerY] = Tileset.FLOOR;
                playerY += 1;
                world[playerX][playerY] = Tileset.PLAYER;
            }
        } else if (ori == 's') {
            if (playerY - 1 >= 0 && !isWall(world[playerX][playerY - 1])) {
                world[playerX][playerY] = Tileset.FLOOR;
                playerY -= 1;
                world[playerX][playerY] = Tileset.PLAYER;
            }
        } else if (ori == 'a') {
            if (playerX - 1 >= 0 && !isWall(world[playerX - 1][playerY])) {
                world[playerX][playerY] = Tileset.FLOOR;
                playerX -= 1;
                world[playerX][playerY] = Tileset.PLAYER;
            }
        } else if (ori == 'd') {
            if (playerX + 1 < WIDTH && !isWall(world[playerX + 1][playerY])) {
                world[playerX][playerY] = Tileset.FLOOR;
                playerX += 1;
                world[playerX][playerY] = Tileset.PLAYER;
            }
        }
    }

    private void startGameLoop(TETile[][] world) {
        while (true) {
            // 键盘输入处理
            if (StdDraw.hasNextKeyTyped()) {
                char cmd = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (cmd == 'w' || cmd == 's' || cmd == 'a' || cmd == 'd') {
                    move(world, cmd);
                } else if (cmd == ':') {
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            if (Character.toLowerCase(StdDraw.nextKeyTyped()) == 'q') {
                                saveGame(world);
                                System.exit(0);
                            } else {
                                break;
                            }
                        }
                    }
                }
            }

            ter.renderFrame(world);

            // HUD 显示
            int mouseX = (int) StdDraw.mouseX();
            int mouseY = (int) StdDraw.mouseY();
            if (mouseX >= 0 && mouseX < WIDTH && mouseY >= 0 && mouseY < HEIGHT) {
                StdDraw.setPenColor(Color.white);
                StdDraw.text(5, HEIGHT - 1, world[mouseX][mouseY].description());
            }
            StdDraw.show();
            StdDraw.pause(10);
        }
    }

    // 2. 提取出的种子输入界面
    private long enterSeed() {
        String seedStr = "";
        while (true) {
            StdDraw.clear(Color.black);
            StdDraw.setPenColor(Color.white);
            StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2, "Enter Seed: " + seedStr);
            StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 2, "Press S to start");
            StdDraw.show();

            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (Character.isDigit(c)) {
                    seedStr += c;
                } else if (c == 's' || c == 'S') {
                    if (!seedStr.isEmpty()) {
                        return Long.parseLong(seedStr);
                    }
                }
            }
        }
    }

    public void playWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        drawMenu();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());

                if (key == 'n') {
                    long seed = enterSeed();
                    TETile[][] world = generateWorld(seed);
                    startGameLoop(world);
                } else if (key == 'l') {
                    try {
                        TETile[][] world = loadGame();
                        if (world != null) {
                            startGameLoop(world);
                        }
                    } catch (RuntimeException e) {
                        System.exit(0);
                    }
                } else if (key == 'q') {
                    System.exit(0);
                }
            }
        }
    }

    // 不访问外部变量的非静态成员（变量、方法）都要加上static
    private static class Room {
        int x;
        int y;
        int w;
        int h;

        public Room(int xx, int yy, int width, int height) {
            x = xx;
            y = yy;
            w = width;
            h = height;
        }

        public boolean isOverlapping(Room r) {
            boolean overlap = true;
            if (r.y > y + h) {
                overlap = false;
            } else if (y > r.y + r.h) {
                overlap = false;
            } else if (x > r.x + r.w) {
                overlap = false;
            } else if (x + w < r.x) {
                overlap = false;
            }

            return overlap;
        }

        public int[] getCent() {
            int [] c = new int[2];
            c[0] = x + (w / 2);
            c[1] = y + (h / 2);
            return c;
        }
    }


    private void drawRoom(TETile[][] world, Room room) {
        for (int i = 0; i < room.h; i++) {
            for (int j = 0; j < room.w; j++) {
                world[room.x + j][room.y + i] = Tileset.WALL;
            }
        }

        for (int i = 1; i < room.h - 1; i++) {
            for (int j = 1; j < room.w - 1; j++) {
                world[room.x + j][room.y + i] = Tileset.FLOOR;
            }
        }
    }

    private void generateAllRooms(TETile[][] world, List<Room> rooms) {
        for (Room room : rooms) {
            drawRoom(world, room);
        }
    }

    private void drawHorizontalLine(TETile[][] world, int x1, int x2, int y) {
        int start = Math.min(x1, x2);
        int end = Math.max(x1, x2);
        for (int i = start; i <= end; i++) {
            world[i][y] = Tileset.FLOOR;
        }
    }

    private void drawVerticalLine(TETile[][] world, int y1, int y2, int x) {
        int start = Math.min(y1, y2);
        int end = Math.max(y1, y2);
        for (int i = start; i <= end; i++) {
            world[x][i] = Tileset.FLOOR;
        }
    }

    private void drawHallway(TETile[][] world, Room r1, Room r2, Random random) {
        int x1 = r1.getCent()[0];
        int y1 = r1.getCent()[1];
        int x2 = r2.getCent()[0];
        int y2 = r2.getCent()[1];

        if (random.nextBoolean()) {
            drawHorizontalLine(world, x1, x2, y1);
            drawVerticalLine(world, y1, y2, x2);
        } else {
            drawVerticalLine(world, y1, y2, x1);
            drawHorizontalLine(world, x1, x2, y2);
        }
    }

    private void generateAllHallways(TETile[][] world, List<Room> rooms, Random random) {
        rooms.sort((r1, r2) -> Integer.compare(r1.x, r2.x));

        for (int i = 0; i < rooms.size() - 1; i++) {
            Room r1 = rooms.get(i);
            Room r2 = rooms.get(i + 1);
            drawHallway(world, r1, r2, random);
        }
    }

    private boolean hasFloorNeighbor(TETile[][] world, int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < WIDTH && j >= 0 && j < HEIGHT) {
                    if (world[i][j] == Tileset.FLOOR) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void addWalls(TETile[][] world) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (world[x][y] == Tileset.NOTHING) {
                    if (hasFloorNeighbor(world, x, y)) {
                        world[x][y] = Tileset.WALL;
                    }
                }
            }
        }
    }

    private TETile[][] generateWorld(long seed) {
        Random random = new Random(seed);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        int roomNum = random.nextInt(10) + 15;
        List<Room> rooms = new ArrayList<>();

        int maxFailures = 1000;
        int currentFailures = 0;

        while (rooms.size() < roomNum) {
            if (currentFailures > maxFailures) {
                break;
            }

            int w = random.nextInt(8) + 6;
            int h = random.nextInt(8) + 6;
            int x = random.nextInt(WIDTH - w);
            int y = random.nextInt(HEIGHT - h);

            Room newRoom = new Room(x, y, w, h);
            boolean isAdd = true;
            for (Room r : rooms) {
                if (newRoom.isOverlapping(r)) {
                    isAdd = false;
                    break;
                }
            }

            if (isAdd) {
                rooms.add(new Room(x, y, w, h));
            } else {
                currentFailures++;
            }
        }

        generateAllRooms(world, rooms);
        generateAllHallways(world, rooms, random);
        addWalls(world);

        int th = random.nextInt(rooms.size());
        int[] playerXY = rooms.get(th).getCent();
        playerX = playerXY[0];
        playerY = playerXY[1];
        world[playerX][playerY] = Tileset.PLAYER;

        return world;
    }

    //  序列化是将对象状态转换为字节流的过程
    private static class GameState implements Serializable {
        private static final long serialVersionUID = 123123123L;
        char[][] savedGrid; // 改存字符
        int savedPlayerX;
        int savedPlayerY;

        public GameState(TETile[][] world, int x, int y) {
            this.savedPlayerX = x;
            this.savedPlayerY = y;
            this.savedGrid = new char[WIDTH][HEIGHT];

            // 将 TETile[][] 转换为 char[][]
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    savedGrid[i][j] = world[i][j].character();
                }
            }
        }
    }


    private TETile charToTile(char c) {
        if (c == Tileset.WALL.character()) {
            return Tileset.WALL;
        }
        if (c == Tileset.FLOOR.character()) {
            return Tileset.FLOOR;
        }
        if (c == Tileset.PLAYER.character()) {
            return Tileset.PLAYER;
        }
        if (c == Tileset.NOTHING.character()) {
            return Tileset.NOTHING;
        }
        return Tileset.NOTHING;
    }

    private TETile[][] loadGame() {
        File f = new File("./save_game.txt");
        if (!f.exists()) {
            // 如果存档不存在，在 Autograder 测试中最好直接抛出异常或者返回默认世界
            // 绝对不能 System.exit(0)
            throw new RuntimeException("Save file not found.");
        }

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            GameState gs = (GameState) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();

            // 还原坐标
            this.playerX = gs.savedPlayerX;
            this.playerY = gs.savedPlayerY;

            // 还原地图：从 char[][] 变回 TETile[][]
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    world[x][y] = charToTile(gs.savedGrid[x][y]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            return null;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("class not found");
            return null;
        }

        return world;
    }

    private void saveGame(TETile[][] world) {
        File f = new File("./save_game.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            ObjectOutput objectOutput = new ObjectOutputStream(fileOutputStream);

            GameState gs = new GameState(world, playerX, playerY);
            objectOutput.writeObject(gs);

            objectOutput.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    public TETile[][] playWithInputString(String input) {
        input = input.toLowerCase();
        char firstChar = input.charAt(0);
        TETile[][] world = null;
        int index = 0;

        if (firstChar == 'n') {
            index = 1;
            String s = "";
            // 解析种子
            while (index < input.length() && Character.isDigit(input.charAt(index))) {
                s += input.charAt(index);
                index++;
            }
            // 检查 S
            if (index == input.length() || input.charAt(index) != 's') {
                throw new IllegalArgumentException("Seed must be followed by S");
            }
            index++;
            world = generateWorld(Long.parseLong(s));
        } else if (firstChar == 'l') {
            world = loadGame();
            index = 1;
        } else {
            throw new IllegalArgumentException("Input must start with N or L");
        }

        // 统一处理移动逻辑
        for (int i = index; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == ':') {
                if (i + 1 < input.length()) {
                    char nextChar = Character.toLowerCase(input.charAt(i + 1));
                    if (nextChar == 'q') {
                        saveGame(world);
                        return world;
                    }
                }
            } else {
                move(world, c);
            }
        }
        return world;
    }
}

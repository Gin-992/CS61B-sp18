package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    // static 方法只能直接使用 static 变量
    private static final int HEIGHT = 50;

    // final: 对象的内容不可变
    private static final int SEED = 1000;
    // final: 引用不可变
    private static final Random RANDOM = new Random(SEED);


    private static int hexRowWidth(int s, int i) {
        if (i < s) {
            return s + 2 * i;
        } else if (i < 2 * s) {
            return 5 * s - 2 - 2 * i;
        }

        throw new IllegalArgumentException("Row index out of bounds.");
    }

    @Test
    public void testhexRowWidth() {
        // --- Size = 3 ---
        // 1.1 底部
        assertEquals(3, hexRowWidth(3, 0));
        // 1.2 中间部分
        assertEquals(7, hexRowWidth(3, 2));
        assertEquals(7, hexRowWidth(3, 3));
        // 1.3 顶部
        assertEquals(3, hexRowWidth(3, 5));

        // --- Size = 2 ---
        // 2.1 底部
        assertEquals(2, hexRowWidth(2, 0));
        // 2.2 中间部分
        assertEquals(4, hexRowWidth(2, 1));
        assertEquals(4, hexRowWidth(2, 2));
        // 2.3 顶部
        assertEquals(2, hexRowWidth(2, 3));
    }

    private static int hexRowOffset(int s, int i) {
        if (i < s) {
            return s - 1 - i;
        } else if (i < 2 * s) {
            return i - s;
        }

        throw new IllegalArgumentException("Row index out of bounds.");
    }

    // x,y 是开始填充的坐标
    private static void addRow(TETile[][] world, TETile tile, int width, int x, int y) {
        for (int i = 0; i < width; i++) {
            world[x + i][y] = tile;
        }
    }

    public static void addHexagon(TETile[][] world, TETile tile, int s, int x, int y) {
        for (int i = 0; i < 2 * s; i++) {
            addRow(world, tile, hexRowWidth(s, i), x + hexRowOffset(s, i), y + i);
        }
    }


    public static int getHexCount(int i) {
        if (i <= 2) {
            return 3 + i;
        } else if (i <= 4) {
            return 7 - i;
        }

        throw new IllegalArgumentException("column index out of bounds.");
    }

    public static int getYOffset(int i, int s) {
        if (i <= 2) {
            return (2 - i) * s;
        } else if (i <= 4) {
            return (i - 2) * s;
        }

        throw new IllegalArgumentException("column index out of bounds.");
    }

    public static int getXOffset(int i, int s) {
        return i * (2 * s - 1);
    }

    public static void addColumn(TETile[][] world, int num, int s, int x, int y) {
        for (int i = 0; i < num; i++) {
            TETile tile = randomTile();
            addHexagon(world, tile, s, x, y + i * 2 * s);
        }
    }

    public static void draw(TETile[][] world, int s) {
        for (int i = 0; i < 5; i++) {
            addColumn(world, getHexCount(i), s, getXOffset(i, s), getYOffset(i, s));
        }
    }

    private static TETile randomTile() {
        // 静态方法不能直接访问非静态变量。
        int num = RANDOM.nextInt(4);
        switch (num) {
            case 0: return Tileset.TREE;
            case 1: return Tileset.GRASS;
            case 2: return Tileset.FLOWER;
            case 3: return Tileset.MOUNTAIN;
            default: return Tileset.NOTHING;
        }
    }

    // 静态方法不能直接访问非静态变量。
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        int s = RANDOM.nextInt(2, 6);

        draw(world, s);
        ter.renderFrame(world);
    }
}

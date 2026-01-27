package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }







    private class Room {
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


    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }

        char firstChar = input.charAt(0);
        if (firstChar != 'N' && firstChar != 'n') {
            throw new IllegalArgumentException("The input must start with N or n");
        }

        int index = 1;
        String s = "";
        while (index < input.length() && Character.isDigit(input.charAt(index))) {
            char c = input.charAt(index);
            s = s + c;
            index += 1;
        }

        if (index == input.length() || (input.charAt(index) != 's' && input.charAt(index) != 'S')) {
            throw new IllegalArgumentException("The seed must be followed by S or s");
        }

        if (s.isEmpty()) {
            throw new IllegalArgumentException("Please enter a numeric SEED");
        }

        Random random = new Random(Long.parseLong(s));
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        int roomNum = random.nextInt(15) + 15;
        List<Room> rooms = new ArrayList<>();
        while (rooms.size() < roomNum) {
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
            }
        }

        generateAllRooms(world, rooms);
        generateAllHallways(world, rooms, random);
        addWalls(world);

        return world;
    }
}

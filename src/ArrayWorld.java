import java.util.Arrays;

public class ArrayWorld extends World implements Cloneable {

    private boolean[][] world;
    private boolean[] deadRow;

    public ArrayWorld(Pattern patternObject) throws PatternFormatException {
        super(patternObject);
        Pattern pattern = getPattern();
        int width = getWidth();
        int height = getHeight();
        world = new boolean[height][width];
        deadRow = new boolean[width];
        pattern.initialise(this);
        // set all-false rows to point to deadRow
        for (int i = 0; i < height; i++) {
            if(Arrays.equals(world[i], deadRow)) {
                world[i] = deadRow;
            }
        }
    }

    public ArrayWorld(String format) throws PatternFormatException {
        super(new Pattern(format));
        Pattern pattern = getPattern();
        int width = getWidth();
        int height = getHeight();
        world = new boolean[height][width];
        deadRow = new boolean[width];
        pattern.initialise(this);
        // set all-false rows to point to deadRow
        for (int i = 0; i < height; i++) {
            if(Arrays.equals(world[i], deadRow)) {
                world[i] = deadRow;
            }
        }
    }

    /*
    copy constructor
    deep copy
     */
    public ArrayWorld(ArrayWorld arrWorld){
        super(arrWorld);
        int worldHeight = getHeight();
        int worldWidth = getWidth();
        world = new boolean[worldHeight][worldWidth];
        deadRow = arrWorld.deadRow;
        for (int i = 0; i < worldHeight; i++) {
            if(Arrays.equals(arrWorld.world[i], deadRow)) {
                world[i] = deadRow;
            }
            else{
                world[i] = arrWorld.world[i].clone();
            }
        }
    }

    /*
    deep copy clone
    requires (Marker / Tag) Clonable Interface
    or else CloneNotSupportedException thrown
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        ArrayWorld copy = (ArrayWorld) super.clone();
        int worldHeight = getHeight();
        int worldWidth = getWidth();
        copy.deadRow = deadRow;
        boolean[][] deepWorld = new boolean[worldHeight][worldWidth];
        for (int i = 0; i < worldHeight; i++) {
            if(Arrays.equals(world[i], deadRow)) {
                deepWorld[i] = deadRow;
            }
            else{
                deepWorld[i] = world[i].clone();
            }
        }
        copy.world = deepWorld;
        return copy;
    }

    @Override
    public void nextGenerationImpl(){
        int width = getWidth();
        int height = getHeight();
        boolean[][] newWorld = new boolean[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                boolean aliveNext = computeCell(col, row);
                newWorld[row][col] = aliveNext;
            }
        }
        world = newWorld;
    }

    @Override
    public boolean getCell(int col, int row) {
        int width = getWidth();
        int height = getHeight();
        if(col < 0 || col > width - 1 || row < 0 || row > height - 1) return false;
        return world[row][col];
    }

    @Override
    public void setCell(int col, int row, boolean value) {
        int width = getWidth();
        int height = getHeight();
        if(col < 0 || col > width - 1 || row < 0 || row > height - 1 ) return;
        world[row][col] = value;
    }
}

public class PackedWorld extends World implements Cloneable{

    long world;

    public PackedWorld(Pattern patternObject) throws PatternFormatException {
        super(patternObject);
        Pattern pattern = getPattern();
        pattern.initialise(this);
    }

    public PackedWorld(String format) throws PatternFormatException {
        super(new Pattern(format));
        Pattern pattern = getPattern();
        pattern.initialise(this);
    }

    // copy constructor
    public PackedWorld(PackedWorld packedWorld){
        super(packedWorld);
        world = packedWorld.world;
    }

    // impliment deep clone with Cloneable interface
    @Override
    public Object clone() throws CloneNotSupportedException {
        PackedWorld copy = (PackedWorld) super.clone();
        copy.world = world;
        return copy;
    }

    @Override
    public void nextGenerationImpl() {
        long newWorld = 0l;
        int height = getHeight();
        int width = getWidth();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                boolean aliveNext = computeCell(col, row);
                int position = width*row + col;
                if (aliveNext) {
                    newWorld |= (1L << position);
                }
                else {
                    newWorld &= ~(1L << position);
                }
            }
        }
        world = newWorld;
    }

    @Override
    public boolean getCell(int col, int row) {
        if(col < 0 || col > getWidth()-1 || row < 0 || row > getHeight()-1 ) return false;
        int position = 8*row + col;
        return ((world >>> position) & 1) == 1;
    }

    @Override
    public void setCell(int col, int row, boolean value) {
        int position = getWidth()*row + col;
        if (value) {
            world |= (1L << position);
        }
        else {
            world &= ~(1L << position);
        }
    }

}

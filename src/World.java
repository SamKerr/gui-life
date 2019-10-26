public abstract class World extends Object implements Cloneable {


    private Pattern pattern;
    private int generation;

    public World(Pattern patternObject) {
        pattern = patternObject;
    }

    public World(String format) throws PatternFormatException {
        pattern = new Pattern(format);
    }

    public World(World w){
        pattern = w.pattern;
        generation = w.generation;
    }


    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getWidth(){
        return pattern.getWidth();
    }

    public int getHeight(){
        return pattern.getHeight();
    }

    public int getGenerationCount(){
        return generation;
    }

    protected void incrementGenerationCount(){
        generation++;
    }

    protected Pattern getPattern(){
        return pattern;
    }

    public void nextGeneration(){
        nextGenerationImpl();
        incrementGenerationCount();
    }

    public abstract void nextGenerationImpl();

    public abstract boolean getCell(int col, int row);

    public abstract void setCell(int col, int row, boolean value);

    private int countNeighbours(int col, int row){
        int neighbours = 0;
        for(int c = col - 1; c <= col + 1; c++){
            for(int r = row -1; r <= row + 1; r++){
                if(getCell(c, r) && !(c == col && r == row)){
                    neighbours++;
                }
            }
        }
        return neighbours;
    }

    protected boolean computeCell(int col, int row){
        boolean liveCell = getCell(col, row);
        int neighbours = countNeighbours(col, row);
        boolean nextCell = false;

        if (liveCell && neighbours < 2) {
            nextCell = false;
        } else if(liveCell && (neighbours == 2 || neighbours == 3)){
            nextCell = true;
        } else if(liveCell && neighbours > 3){
            nextCell = false;
        } else if(!liveCell && neighbours == 3){
            nextCell = true;
        }

        return nextCell;
    }
}

public class Pattern implements Comparable<Pattern>  {

    private String name;
    private String author;
    private int width;
    private int height;
    private int startX;
    private int startY;
    private String cells;


    //format is NAME:AUTHOR:WIDTH:HEIGHT:STARTUPPERCOL:STARTUPPERROW:CELLS
    public Pattern(String format) throws PatternFormatException {

        String[] values = format.split(":");
        int formatLength = values.length;
        if(formatLength == 0){
            throw new PatternFormatException("Please specify a pattern.");
        } else if(formatLength != 7 ){
            throw new PatternFormatException("Invalid pattern format: Incorrect number of fields in pattern (found " + formatLength + ").");
        }

        name = values[0];
        author = values[1];
        try{
            width = Integer.parseInt(values[2]);
        } catch (Exception e){
            throw new PatternFormatException("Invalid pattern format: Could not interpret the " + "width" + " field as a number ('" + values[2] + "' given).");
        }

        try{
            height = Integer.parseInt(values[3]);
        } catch (Exception e){
            throw new PatternFormatException("Invalid pattern format: Could not interpret the " + "height" + " field as a number ('" + values[3] + "' given).");
        }

        try{
            startX = Integer.parseInt(values[4]);
        } catch (Exception e){
            throw new PatternFormatException("Invalid pattern format: Could not interpret the " + "startX" + " field as a number ('" + values[4] + "' given).");
        }

        try{
            startY = Integer.parseInt(values[5]);
        } catch (Exception e){
            throw new PatternFormatException("Invalid pattern format: Could not interpret the " + "startY" + " field as a number ('" + values[5] + "' given).");
        }

        cells = values[6];
    }

    public void initialise(World world) throws PatternFormatException {
        String[] rows = cells.split(" ");
        for(int y = 0; y < rows.length; y++) {
            char[] row = rows[y].toCharArray();
            for(int x = 0; x < row.length; x++) {
                boolean cell;
                char cellChar = row[x];
                if(!Character.isDigit(cellChar)) {
                    throw new PatternFormatException("Invalid pattern format: Malformed pattern '" + cells + "'.");
                }
                cell = (cellChar - '0') == 1;
                int r = y + startY;
                int c = x + startX;
                world.setCell(c,r,cell);
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public String getCells() {
        return cells;
    }

    @Override
    public int compareTo(Pattern other) {
        String name1 = name;
        String name2 = other.getName();
        return name1.compareTo(name2);
    }

    @Override
    public String toString(){
        return (name + " " + "(" + author + ")");
    }
}
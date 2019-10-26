import java.awt.Color;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private World world = null;

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        // Paint the background white
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        if(world == null) return;
        int panHeight = getHeight();
        int panWidth = getWidth();

        int h = world.getHeight();
        int w = world.getWidth();

        int cellHeight = panHeight/h;
        int cellWidth = panWidth/w;

        int squareSize = Math.min(cellWidth,cellHeight);
        int gridHeight = h*squareSize;
        int gridWidth = w*squareSize;

        g.setColor(Color.LIGHT_GRAY);
        // horizontal lines
        for(int y = 0; y <= h; y++){
            g.drawLine(0,y*squareSize,gridWidth,y*squareSize);
        }

        // vertical lines
        for(int x = 0; x <= w; x++){
            g.drawLine(x*squareSize,0,x*squareSize,gridHeight);
        }

        g.setColor(Color.BLACK);
        //Fill Squares
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if(world.getCell(x,y)){
                    g.fillRect(x*(squareSize)+2,y*(squareSize)+2,squareSize-3,squareSize-3);
                }
            }
        }
        g.drawString("Generation: " + world.getGenerationCount(),  Math.floorDiv(panWidth,80),Math.floorDiv(39*panHeight,40));
    }

    public void display(World w) {
        world = w;
        repaint();
    }
}
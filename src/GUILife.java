import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class GUILife extends JFrame implements ListSelectionListener {

    private World world;
    private PatternStore store;
    private ArrayList<World> cashedWorlds = new ArrayList<>();
    private GamePanel gp;
    private JButton playButton;
    private Timer timer;
    private boolean playing;

    public static void main(String[] args) {
        try {
            String source = "https://www.cl.cam.ac.uk/teaching/1819/OOProg/ticks/life.txt";
            PatternStore ps = new PatternStore(source);
            GUILife gui = new GUILife(ps);
            gui.setVisible(true);
        } catch (IOException e) {
            System.out.println("Failed to load pattern store");
        }
    }

    public GUILife(PatternStore ps)  {
        super("Game of Life");
        store=ps;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1024,768);
        add(createPatternsPanel(),BorderLayout.WEST);
        add(createControlPanel(),BorderLayout.SOUTH);
        add(createGamePanel(),BorderLayout.CENTER);
    }

    private World copyWorld(boolean useCloning) {
        World copy = null;
        if(useCloning){
            try {
                copy = (World) world.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        else {
            if(world instanceof ArrayWorld){
                copy = new ArrayWorld((ArrayWorld) world);
            } else {
                copy = new PackedWorld((PackedWorld) world);
            }
        }
        return copy;
    }

    private void moveForward(){
        if (world == null) {
            System.out.println("Please select a pattern to play (l to list):");
        }
        // if next generation is not in cashed worlds
        // generate next world and cashe it
        else if(world.getGenerationCount() + 1 >= cashedWorlds.size()) {
            world = copyWorld(true);
            world.nextGeneration();
            cashedWorlds.add(world);
        }
        // else get next world generation from cashedWorlds
        else {
            int currentGen = world.getGenerationCount();
            world = cashedWorlds.get(currentGen+1);
        }
        gp.display(world);
    }

    private void runOrPause() {
        if (playing) {
            timer.cancel();
            playing=false;
            playButton.setText("Play");
        }
        else {
            playing=true;
            playButton.setText("Stop");
            timer = new Timer(true);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    moveForward();
                }
            }, 0, 500);
        }
    }

    private void moveBack(){
        if (world == null) {
            System.out.println("Please select a pattern to play (l to list):");
        }
        // get previous world generation from cashedWorlds
        else{
            int currentGen = world.getGenerationCount();
            if (currentGen != 0) {
                world = cashedWorlds.get(currentGen-1);
            }
        }
        gp.display(world);
    }

    private void addBorder(JComponent component, String title) {
        Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border tb = BorderFactory.createTitledBorder(etch,title);
        component.setBorder(tb);
    }

    private JPanel createGamePanel() {
        GamePanel gamePanel = new GamePanel();
        addBorder(gamePanel,"Game Panel");
        gp = gamePanel;
        return gamePanel;
    }

    private JPanel createPatternsPanel() {
        JPanel patt = new JPanel();
        addBorder(patt,"Patterns");
        patt.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane();
        List<Pattern> sortedPatterns = store.getPatternsNameSorted();
        JList patternList = new JList(sortedPatterns.toArray());
        patternList.addListSelectionListener(this::valueChanged);
        scrollPane.setViewportView(patternList);

        patt.add(scrollPane);
        return patt;
    }

    private JPanel createControlPanel() {
        JPanel ctrl =  new JPanel();
        addBorder(ctrl,"Controls");
        ctrl.setLayout(new GridLayout());

        JButton back = new JButton("< Back");
        back.addActionListener(e -> {
            moveBack();
            if(playing){
                playButton.doClick();
            }
        });
        ctrl.add(back, BorderLayout.WEST);

        JButton play = new JButton("Play");
        playButton = play;
        play.addActionListener(e -> {
            runOrPause();
        });
        ctrl.add(play, BorderLayout.CENTER);

        JButton forward = new JButton("Forward >");
        forward.addActionListener(e -> {
            moveForward();
            if(playing){
                playButton.doClick();
            }
        });
        ctrl.add(forward, BorderLayout.EAST);
        return ctrl;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList<Pattern> list = (JList<Pattern>) e.getSource();
        Pattern p = list.getSelectedValue();
        int width = p.getWidth();
        int height = p.getHeight();
        // if world can be stored in 64 bits ie a long then use PackedWorld
        try{
            if(width*height<=64) {
                world = new PackedWorld(p);
            } else {
                world = new ArrayWorld(p);
            }
        }catch (PatternFormatException exception){
            System.out.println("Pattern format exception occured on pattern: " + p.getName() + "(" + p.getAuthor() + ")");
        }
        cashedWorlds.clear();
        cashedWorlds.add(world);
        if(playing){
            playButton.doClick();
        }
        gp.display(world);
    }
}
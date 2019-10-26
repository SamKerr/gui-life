import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class PatternStore {

    private List<Pattern> patterns = new LinkedList<>();
    private Map<String,List<Pattern>> mapAuths = new HashMap<>();
    private Map<String,Pattern> mapName = new HashMap<>();

    public PatternStore(String source) throws IOException {
        if (source.startsWith("http://") || source.startsWith("https://")) {
            loadFromURL(source);
        }
        else {
            loadFromDisk(source);
        }
    }

    public PatternStore(Reader source) throws IOException {
        load(source);
    }

    private void load(Reader r) throws IOException {
        BufferedReader b = new BufferedReader(r);
        String line;
        while ( (line = b.readLine()) != null) {
            try{
                Pattern pattern = new Pattern(line);
                patterns.add(pattern);
                mapName.put(pattern.getName(), pattern);
                String author = pattern.getAuthor();

                if(mapAuths.containsKey(author)){
                    List<Pattern> ps = mapAuths.get(author);
                    List<Pattern> copy = new ArrayList<>(ps);
                    copy.add(pattern);
                    mapAuths.put(author, copy);
                } else {
                    mapAuths.put(author, Arrays.asList(pattern));
                }

            } catch (PatternFormatException e){
                System.out.println( "Warning! Invalid format on line: " + line);
            }
        }

    }

    private void loadFromURL(String url) throws IOException {
        URL destination = new URL(url);
        URLConnection conn = destination.openConnection();
        Reader r = new InputStreamReader(conn.getInputStream());
        load(r);
    }

    private void loadFromDisk(String filename) throws IOException {
        Reader r = new FileReader(filename);
        load(r);
    }

    public List<Pattern> getPatternsNameSorted() {
        List<Pattern> copy = new LinkedList<>(patterns);
        Collections.sort(copy);
        return copy;
    }

    public List<Pattern> getPatternsAuthorSorted() {
        List<Pattern> copy = new LinkedList<>(patterns);
        Collections.sort(copy, new Comparator<Pattern>() {
            public int compare(Pattern p1, Pattern p2) {
                int compareByAuthor = (p1.getAuthor()).compareTo(p2.getAuthor());
                int compareByName = p1.getName().compareTo(p2.getName());
                return (compareByAuthor == 0) ? compareByName : compareByAuthor;
            }
        });
        return copy;
    }

    public List<Pattern> getPatternsByAuthor(String author) throws PatternNotFound {
        if(!mapAuths.containsKey(author)){
            throw new PatternNotFound("Pattern not found");
        }
        List<Pattern> authorPatterns = mapAuths.get(author);
        List<Pattern> copy = new LinkedList<>(authorPatterns);
        Collections.sort(copy);
        return copy;
    }

    public Pattern getPatternByName(String name) throws PatternNotFound {
        if(mapName.containsKey(name)){
            return mapName.get(name);
        } else {
            throw new PatternNotFound("Pattern name not found");
        }

    }

    public List<String> getPatternAuthors() {
        List<String> authors =  new ArrayList<>(mapAuths.keySet());
        Collections.sort(authors, String::compareToIgnoreCase);
        return authors;
    }

    public static void main(String[] args) throws IOException, PatternNotFound {
        PatternStore ps = new PatternStore(args[0]);
        Pattern p1 = ps.getPatternByName("test");
        System.out.println(p1.getName());
    }

    public List<String> getPatternNames() {
        List<String> names = new ArrayList<>(mapName.keySet());
        Collections.sort(names, String::compareToIgnoreCase);
        return names;
    }
}

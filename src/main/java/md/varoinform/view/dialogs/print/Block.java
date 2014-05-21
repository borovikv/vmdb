package md.varoinform.view.dialogs.print;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/20/14
 * Time: 1:56 PM
 */
public class Block implements Iterable<String>{
    List<String> fields;
    Map<String, Pair> parts;

    public Block() {
        parts = new HashMap<>();
        fields = new ArrayList<>();
        fields.add("title");
        fields.add("creationdate");
        fields.add("workplaces");
        fields.add("phones");
        fields.add("faxes");
        fields.add("businessentitytype");
        fields.add("address");
        fields.add("foreingcapital");
        fields.add("emails");
        fields.add("urls");
        fields.add("goods");
        fields.add("branches");
        fields.add("contactperson");
        fields.add("checkdate");

    }

    public void addLines(String field, List<String> lines, Font font){
        if (parts.containsKey(field)){
           parts.get(field).strings.addAll(lines);
        } else {
            parts.put(field, new Pair(lines, font));
        }

    }


    public List<String> getLines(String field){
        Pair pair = parts.get(field);
        if (pair != null) return pair.strings;
        return null;
    }

    public Font getFont(String field){
        Pair pair = parts.get(field);
        if (pair != null) return pair.font;
        return null;
    }

    public double height(){
        double height = 0;
        Canvas canvas = new Canvas();
        for (String key : fields) {
            Pair pair = parts.get(key);
            if (pair == null) {
                continue;
            }
            FontMetrics metrics = canvas.getFontMetrics(pair.font);
            height += pair.strings.size() * metrics.getHeight();
        }
        return height;
    }

    @Override
    public Iterator<String> iterator() {
        return fields.iterator();
    }

    private class Pair{
        List<String> strings;
        Font font;

        private Pair(List<String> strings, Font font) {
            this.strings = strings;
            this.font = font;
        }
    }
    
}

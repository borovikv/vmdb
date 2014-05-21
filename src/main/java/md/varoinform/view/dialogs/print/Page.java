package md.varoinform.view.dialogs.print;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/20/14
 * Time: 1:26 PM
 */
public class Page implements Iterable<Block>{
    private List<Block> pages = new ArrayList<>();

    public void addBlock(Block lines){
        pages.add(lines);
    }

    @Override
    public Iterator<Block> iterator() {
        return pages.iterator();
    }
}

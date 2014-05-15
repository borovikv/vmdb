package md.varoinform.model.search;

import md.varoinform.model.entities.Enterprise;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/24/14
 * Time: 9:45 AM
 */
public abstract class Searcher implements Comparable<Searcher>{
    public abstract List<Enterprise> search(String q);

    public String getName(){
        return this.getClass().getSimpleName().replace("Searcher", "").toLowerCase();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Searcher o) {
        if (o == null) {
            throw new NullPointerException();
        }
        String oName = o.getName();
        String name = getName();
        if (name != null && oName != null)
            return name.compareTo(oName);
        return 0;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}

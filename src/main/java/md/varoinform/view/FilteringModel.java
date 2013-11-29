package md.varoinform.view;

import javax.swing.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/29/13
 * Time: 11:54 AM
 */
public class FilteringModel<T> extends AbstractListModel<T>{
    List<T> list = new ArrayList<>();
    List<T> filteredList = new ArrayList<>();
    String lastSearch;

    public FilteringModel(List<T> tags) {
        list.addAll(tags);
        filteredList.addAll(tags);
    }

    @Override
    public int getSize() {
        return filteredList.size();
    }

    @Override
    public T getElementAt(int index) {
        if (index < filteredList.size())
            return filteredList.get(index);

        return null;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void addElement(T element) {
        list.add(element);
        filter();
    }

    private void filter() {
        if(lastSearch != null){
            filter(lastSearch);
        } else {
            filteredList.addAll(list);
        }
    }

    public void filter(String lastSearch) {
        this.lastSearch = lastSearch;
        filteredList.clear();
        for (T el : list) {
            if (el.toString().contains(lastSearch)){
                filteredList.add(el);
            }
        }
        Collections.sort(filteredList, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        fireContentsChanged(this, 0, getSize());

    }

    public void clear() {
        list.clear();
        filteredList.clear();
    }

    public void addAll(List<T> all) {
        list.addAll(all);
        filter();
        fireContentsChanged(this, 0, getSize());
    }

    public int getIndexAtElement(T t) {
        return filteredList.indexOf(t);
    }
}

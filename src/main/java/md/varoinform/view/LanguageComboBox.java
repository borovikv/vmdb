package md.varoinform.view;

import md.varoinform.controller.LanguageProxy;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/3/13
 * Time: 3:20 PM
 */
public class LanguageComboBox extends JComboBox<Long> {
    private final List<Long> languages;
    private List<Long> disabledLanguages = new ArrayList<>();
    private List<Long> currentLanguages = new ArrayList<>();

    public LanguageComboBox() {
        languages = LanguageProxy.instance.getLanguages();
        setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String langTitle = LanguageProxy.instance.getTitle((Long) value);
                label.setText(langTitle);
                return label;
            }
        });
        setCurrentLanguages();

    }

    public void setCurrentLanguages() {
        currentLanguages = new ArrayList<>(languages);
        for (Long langID : disabledLanguages) {
            currentLanguages.remove(langID);
        }

        DefaultComboBoxModel<Long> model = new DefaultComboBoxModel<>(currentLanguages.toArray(new Long[languages.size()]));
        Long curLangID = LanguageProxy.instance.getCurrentLanguage();
        if (currentLanguages.contains(curLangID)) {
            model.setSelectedItem(curLangID);
        }
        removeAllItems();
        setModel(model);
    }

    public void setEnableItem(Long language, boolean enable){
        if (language == null) return;
        if (languages.contains(language)){
            if (enable){
                disabledLanguages.remove(language);
            } else if (!disabledLanguages.contains(language)){
                disabledLanguages.add(language);
            }
            setCurrentLanguages();
        }
    }

}

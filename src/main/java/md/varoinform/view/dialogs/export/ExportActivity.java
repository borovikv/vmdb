package md.varoinform.view.dialogs.export;

import au.com.bytecode.opencsv.CSVWriter;
import md.varoinform.controller.Cache;
import md.varoinform.controller.comparators.ColumnPriorityComparator;
import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.util.ResourceBundleHelper;
import md.varoinform.util.StringUtils;
import md.varoinform.view.dialogs.progress.Activity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 5/17/14
 * Time: 11:00 AM
 */
public class ExportActivity extends Activity {
    private final File file;
    private final List<String> selectedColumns;
    private final List<Long> enterprises;
    private String format;

    public ExportActivity(File file, List<String> selectedColumns, List<Long> enterprises) {
        this.file = file;
        this.selectedColumns = selectedColumns;
        this.enterprises = enterprises;
        format = ResourceBundleHelper.getString("exported_i_from_size", super.getFormat());
        Collections.sort(selectedColumns, new ColumnPriorityComparator());
    }

    @Override
    protected Void doInBackground() throws Exception {
        try (FileWriter fileWriter = new FileWriter(file); CSVWriter writer = new CSVWriter(fileWriter, ';')){
            writer.writeNext(selectedColumns.toArray(new String[selectedColumns.size()]));

            int size = enterprises.size();
            for (int i = 0; i < size; i++) {
                Thread.sleep(millis);
                writeLine(writer, selectedColumns, Cache.instance.getProxy((long)i));

                int progress = i * 100 / size;
                setProgress(progress);
                setNote(i, size);
            }

        } catch (IOException ignored){
        }
        return null;
    }

    private void writeLine(CSVWriter writer, List<String> selectedColumns, EnterpriseProxy proxy) {
        String[] entries = new String[selectedColumns.size()];
        for (int i = 0; i < selectedColumns.size(); i++) {
            Object obj = proxy.get(selectedColumns.get(i));
            entries[i] = StringUtils.valueOf(obj);
        }
        writer.writeNext(entries);
    }

    @Override
    public String getFormat() {
        return format;
    }
}

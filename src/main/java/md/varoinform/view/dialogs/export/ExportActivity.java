package md.varoinform.view.dialogs.export;

import au.com.bytecode.opencsv.CSVWriter;
import md.varoinform.controller.cache.Cache;
import md.varoinform.controller.comparators.ColumnPriorityComparator;
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
    private final List<Integer> idEnterprises;
    private String format;

    public ExportActivity(File file, List<String> selectedColumns, List<Integer> idEnterprises) {
        this.file = file;
        this.selectedColumns = selectedColumns;
        this.idEnterprises = idEnterprises;
        format = ResourceBundleHelper.getString("exported_i_from_size", super.getFormat());
        Collections.sort(selectedColumns, new ColumnPriorityComparator());
    }

    @Override
    protected Void doInBackground() throws Exception {
        try (FileWriter fileWriter = new FileWriter(file); CSVWriter writer = new CSVWriter(fileWriter, ';')){
            writer.writeNext(selectedColumns.toArray(new String[selectedColumns.size()]));

            int size = idEnterprises.size();
            for (int i = 0; i < size; i++) {
                Thread.sleep(millis);
                writeLine(writer, selectedColumns, i);

                int progress = i * 100 / size;
                setProgress(progress);
                setNote(i, size);
            }

        } catch (IOException ignored){
        }
        return null;
    }

    private void writeLine(CSVWriter writer, List<String> selectedColumns, int row) {
        String[] entries = new String[selectedColumns.size()];
        Integer eid = idEnterprises.get(row);
        for (int i = 0; i < selectedColumns.size(); i++) {
            String field = selectedColumns.get(i);
            Object obj = Cache.instance.getRawValue(eid, field);
            entries[i] = StringUtils.valueOf(obj);
        }
        writer.writeNext(entries);
    }

    @Override
    public String getFormat() {
        return format;
    }
}

package md.varoinform.creator;

import java.io.File;
import java.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/13/14
 * Time: 11:33 AM
 */
public class Creator {
    private final File root;
    private final String outputDB;

    public Creator(File root){
        this.root = root;
        outputDB = root.getAbsolutePath() + File.separator + "DB";
    }

    public File create() throws CreateException {
        File[] files = getFiles();

        for (File file : files) {
            try {
                createTableFromCsv(file);
            } catch (SQLException | ClassNotFoundException e) {
                throw new CreateException(e);
            }
        }

        return getOutputDB();
    }

    private File[] getFiles() {
        File[] files = root.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        return files != null ? files : new File[0];
    }

    private void createTableFromCsv(File file) throws SQLException, ClassNotFoundException {
        String sql = "CREATE TABLE " /*+ "DB_"*/ + getName(file.getName()) +
                " AS SELECT * FROM CSVREAD('" + file.getAbsolutePath() +  "');";
        executeSQL(sql);
    }

    private void executeSQL(String sql) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        try (Connection conn = DriverManager.getConnection("jdbc:h2:" + outputDB, "admin", "password")) {
            conn.createStatement().execute(sql);
        }
    }

    private String getName(String csv) {
        String name = new File(csv).getName().split("\\.")[0];
        if (name.contains("+")){
            name = name.split("\\+")[1];
        }
        return name;
    }


    private File getOutputDB() throws CreateException {
        File file = new File(outputDB + ".h2.db");
        if (!file.exists()) throw new CreateException("file not created");
        return file;
    }

}

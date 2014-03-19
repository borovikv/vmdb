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
    private static final String CSV_ROOT = "src/main/java/md/varoinform/creator/csvdb_";
    //private final Session session = HibernateSessionFactory.getSession();

    private Creator(){}

    public static int updateDb() {
        return new Creator().update();

    }

    private int update() {
        readFolder(1);

        return 0;
    }

    private void readFolder(int count) {
        File folder = new File(CSV_ROOT + count);
        File[] files = folder.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        for (File file : files != null ? files : new File[0]) {
            try {
                createTableFromCsv(file);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void createTableFromCsv(File file) throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:database/DB", "admin", "password");
        conn.createStatement().execute("CREATE TABLE " +
                "DB_" + getName(file.getName()) +
                " AS SELECT * FROM CSVREAD('" +
                file.getAbsolutePath() +
                "');");
        conn.close();

    }

    private String getName(String csv) {
        String name = new File(csv).getName().split("\\.")[0];
        if (name.contains("+")){
            name = name.split("\\+")[1];
        }
        return name;
    }


}

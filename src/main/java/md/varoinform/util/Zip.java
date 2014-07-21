package md.varoinform.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 7/21/14
 * Time: 2:11 PM
 */
public class Zip {
    private static final int BUFFER_SIZE = 2048;

    public static Path unzip(File zipFile) throws IOException {
        String absPath = zipFile.getAbsolutePath();
        absPath = absPath.substring(0, absPath.lastIndexOf("."));
        Path unzipDir = Paths.get(absPath);
        Files.createDirectories(unzipDir);

        try(FileInputStream fin = new FileInputStream(zipFile); ZipInputStream zin = new ZipInputStream(fin)){
            ZipEntry entry;
            while ((entry= zin.getNextEntry()) != null){
                File file = Paths.get(unzipDir.toString(), entry.getName()).toFile();
                try (FileOutputStream fout = new FileOutputStream(file); BufferedOutputStream bufOut = new BufferedOutputStream(fout)){

                    byte[] buff = new byte[BUFFER_SIZE];
                    int count;
                    while ((count = zin.read(buff, 0, BUFFER_SIZE)) != -1){
                        bufOut.write(buff, 0, count) ;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                zin.closeEntry();
            }
        }

        return unzipDir;
    }
}

package utils;

import filemanager.ConfigFiles;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

public class WritePathsFile {

    private File file;
    private FileWriter filewriter;

    public WritePathsFile(String fileName) {
        String path = null;
        try {
            path = new File(ConfigFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        path = path.replaceAll("%20", " ");
        file = new File(path + "/" + fileName);
        try {
            filewriter = new FileWriter(file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String value) {
        try {
            filewriter = new FileWriter(file, true);
            PrintWriter printer = new PrintWriter(filewriter);
            printer.write(value);
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

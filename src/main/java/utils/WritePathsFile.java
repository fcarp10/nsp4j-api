package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WritePathsFile {

    private File file;
    private FileWriter filewriter;

    public WritePathsFile(String path, String fileName) {
        file = new File(path + "/" + fileName + ".txt");
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

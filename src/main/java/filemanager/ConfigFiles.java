package filemanager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ConfigFiles {

    public static Scanner scanPlainTextFileInResources(String filename) {
        InputStream inputStream = TypeReference.class.getResourceAsStream(filename);
        Scanner scanner = null;
        try {
            scanner = new Scanner(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scanner;
    }

    public static Parameters readParameters(String filename) {
        String path = null;
        try {
            path = new File(ConfigFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        path = path.replaceAll("%20", " ");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Parameters parameters = null;
        try {
            parameters = mapper.readValue(new File(path + "/" + filename), Parameters.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return parameters;
    }
}

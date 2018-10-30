package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import manager.Parameters;

import java.io.File;
import java.io.InputStream;
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

    public static Parameters readParameters(String path, String filename) {
        path = path.replaceAll("%20", " ");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Parameters parameters = null;
        try {
            parameters = mapper.readValue(new File(path + filename), Parameters.class);
            filename = filename.replace("/", "");
            parameters.setScenario(filename.split("\\.")[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parameters;
    }
}

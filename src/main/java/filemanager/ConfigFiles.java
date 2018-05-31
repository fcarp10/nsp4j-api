package filemanager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ConfigFiles {

    public static Scanner scanPlainTextFile(String filename) {

        Scanner scannerTextPlain = null;
        ClassLoader classLoader = ConfigFiles.class.getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());

        try {
            scannerTextPlain = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return scannerTextPlain;
    }

    public static InputParameters readInputParameters(String filename) {

        TypeReference<InputParameters> typeReference = new TypeReference<>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream(filename);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        InputParameters inputParameters = null;
        try {
            inputParameters = mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputParameters;
    }

}

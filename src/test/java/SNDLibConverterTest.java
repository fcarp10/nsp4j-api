import org.junit.jupiter.api.Test;
import utils.ConfigFiles;
import utils.SNDLibConverter;

import java.io.File;
import java.net.URISyntaxException;

public class SNDLibConverterTest {

   @Test
   public void inputParameters() throws URISyntaxException {
      final String TOPOLOGY = "janos26";
      String path = new File(ConfigFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
      SNDLibConverter.run(path, TOPOLOGY);
   }
}

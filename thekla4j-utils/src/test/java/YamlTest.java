import com.teststeps.thekla4j.utils.yaml.YAML;
import org.junit.jupiter.api.Test;

import java.util.List;

public class YamlTest {


  @Test
  public void testYaml() {

    List<String> list = List.of("test");

    String yml = YAML.jStringify(list);

    System.out.println(yml);
  }

}

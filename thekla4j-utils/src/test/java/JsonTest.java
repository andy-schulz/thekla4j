import com.teststeps.thekla4j.utils.json.JSON;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class JsonTest {

  @Test
  public void testStringify() throws Exception {
    Map<String, String> testMap = Map.of("k1", "v1");

    String str = JSON.valueAsString(testMap).getOrElse("Error parsing object to String");
    System.out.println(str);

  }
}

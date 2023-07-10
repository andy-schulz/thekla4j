import com.teststeps.thekla4j.core.base.persona.Actor;
import com.teststeps.thekla4j.http.httpConn.HcHttpClient;
import com.teststeps.thekla4j.http.spp.HttpOptions;
import com.teststeps.thekla4j.http.spp.Request;
import com.teststeps.thekla4j.http.spp.abilities.UseTheRestApi;
import com.teststeps.thekla4j.http.spp.activities.Get;
import org.junit.Test;

public class GetTest {

  @Test
  public void sendGetRequest() {
    Actor tester = Actor.named("Tester")
                        .whoCan(UseTheRestApi.with(HcHttpClient.using(HttpOptions.empty())));

    Request httpBin = Request.on("https://httpbin.org/get");

    tester.attemptsTo(
        Get.from(httpBin))
        .peek(System.out::println);
  }
}

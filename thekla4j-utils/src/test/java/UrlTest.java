import com.teststeps.thekla4j.utils.url.UrlHelper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UrlTest {


  @Test
  public void testSanitizeUrl() {
    String url = "http://user:password@host:8080/file";
    assertThat("user and password are removed from url", UrlHelper.sanitizeUrl.apply(url).get(), equalTo("http://**@host:8080/file"));
  }

  @Test
  public void sanitizeWithoutCredentials() {
    String url = "http://host:8080/file";
    assertThat("url is not changed", UrlHelper.sanitizeUrl.apply(url).get(), equalTo(url));
  }

  @Test
  public void sanitizeWithoutPassword() {
    String url = "http://user@host:8080/file";
    assertThat("user is removed from url", UrlHelper.sanitizeUrl.apply(url).get(), equalTo("http://**@host:8080/file"));
  }
}

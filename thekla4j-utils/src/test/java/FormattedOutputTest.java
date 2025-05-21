import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.teststeps.thekla4j.utils.terminal.FormattedOutput;
import org.junit.jupiter.api.Test;

public class FormattedOutputTest {

  // write testes to check the ColoredOutput utility class

  @Test
  public void redFont() {
    String font = FormattedOutput.RED("Hello World");
    assertThat("font is red", font, equalTo("\u001B[31mHello World\u001B[0m"));

    String background = FormattedOutput.RED_BG("Hello World");
    assertThat("background is red", background, equalTo("\u001B[41mHello World\u001B[0m"));
  }

  @Test
  public void greenFont() {
    String font = FormattedOutput.GREEN("Hello World");
    assertThat("font is green", font, equalTo("\u001B[32mHello World\u001B[0m"));

    String background = FormattedOutput.GREEN_BG("Hello World");
    assertThat("background is green", background, equalTo("\u001B[42mHello World\u001B[0m"));
  }

  @Test
  public void yellowFont() {
    String font = FormattedOutput.YELLOW("Hello World");
    assertThat("font is yellow", font, equalTo("\u001B[33mHello World\u001B[0m"));

    String background = FormattedOutput.YELLOW_BG("Hello World");
    assertThat("background is yellow", background, equalTo("\u001B[43mHello World\u001B[0m"));
  }

  @Test
  public void blueFont() {
    String font = FormattedOutput.BLUE("Hello World");
    assertThat("font is blue", font, equalTo("\u001B[34mHello World\u001B[0m"));

    String background = FormattedOutput.BLUE_BG("Hello World");
    assertThat("background is blue", background, equalTo("\u001B[44mHello World\u001B[0m"));
  }

  @Test
  public void purpleFont() {
    String font = FormattedOutput.PURPLE("Hello World");
    assertThat("font is purple", font, equalTo("\u001B[35mHello World\u001B[0m"));

    String background = FormattedOutput.PURPLE_BG("Hello World");
    assertThat("background is purple", background, equalTo("\u001B[45mHello World\u001B[0m"));
  }

  @Test
  public void cyanFont() {
    String font = FormattedOutput.CYAN("Hello World");
    assertThat("font is cyan", font, equalTo("\u001B[36mHello World\u001B[0m"));

    String background = FormattedOutput.CYAN_BG("Hello World");
    assertThat("background is cyan", background, equalTo("\u001B[46mHello World\u001B[0m"));
  }

  @Test
  public void whiteFont() {
    String font = FormattedOutput.WHITE("Hello World");
    assertThat("font is white", font, equalTo("\u001B[37mHello World\u001B[0m"));

    String background = FormattedOutput.WHITE_BG("Hello World");
    assertThat("background is white", background, equalTo("\u001B[47mHello World\u001B[0m"));
  }

  @Test
  public void blackFont() {
    String font = FormattedOutput.BLACK("Hello World");
    assertThat("font is black", font, equalTo("\u001B[30mHello World\u001B[0m"));

    String background = FormattedOutput.BLACK_BG("Hello World");
    assertThat("background is black", background, equalTo("\u001B[40mHello World\u001B[0m"));
  }

  @Test
  public void boldFont() {
    String font = FormattedOutput.BOLD("Hello World");
    assertThat("font is bold", font, equalTo("\u001B[1mHello World\u001B[0m"));
  }

  @Test
  public void underlineFont() {
    String font = FormattedOutput.UNDERLINE("Hello World");
    assertThat("font is underline", font, equalTo("\u001B[4mHello World\u001B[0m"));
  }

  @Test
  public void italicFont() {
    String font = FormattedOutput.ITALIC("Hello World");
    assertThat("font is italic", font, equalTo("\u001B[3mHello World\u001B[0m"));
  }

}

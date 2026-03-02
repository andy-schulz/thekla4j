# Ausgelassene Integrationstests (Playwright)

Die folgenden Selenium-Integrationstests wurden **nicht** als Playwright-Gegenstücke implementiert.

---

## IT_AbilityDumpTest

**Grund:** Der Test prüft, ob ein Screenshot-Anhang im Activity-Log den Dateinamen
`<sessionId>.png` enthält. Die Session-ID wird über `browser.getSessionId()` ermittelt
und entspricht in Selenium der WebDriver-Session-UUID, die im Dateinamen des Screenshots
auftaucht.

Playwright vergibt keine WebDriver-kompatible Session-ID. `PlaywrightBrowser.getSessionId()`
gibt einen internen Bezeichner zurück, der nicht direkt mit dem Screenshot-Dateinamen
zusammenhängt. Der Test würde daher strukturell abweichen und wäre kein gleichwertiger
Port des Selenium-Tests.

---

## IT_SeleniumActorLogListenerTest

**Grund:** Der Test testet explizit selenium-spezifische Browser-Log-Mechanismen:

- Den klassischen WebDriver-Log-Listener (CDP-basiert, nur Chrome/Firefox).
- Das Selenium **BiDi**-Protokoll (`SELENIUM_BIDI_LOG`-Property), das über
  `SeleniumLoader` und `ListenToBrowserLogs` aktiviert wird.

Playwright hat kein äquivalentes `ListenToBrowserLogs`-Ability und kein BiDi-Property.
`PlaywrightBrowser.getLogEntries()` gibt immer eine leere Liste zurück und
`PlaywrightBrowser.initBrowserLog()` loggt lediglich intern via Log4j2.
Ein Port würde ausschließlich leere Assertions testen und keinen Mehrwert liefern.

---

## IT_ResizeTest

**Grund:** Der Test prüft nach jeder Resize-Operation die **tatsächliche Fenstergröße**
direkt über die Selenium-WebDriver-API:

```java
WebDriver driver = ((SeleniumDriver) browser).getDriver().get();
org.openqa.selenium.Dimension size = driver.manage().window().getSize();
assertThat((double) size.getWidth(), closeTo(1920, 1930));
```

Playwright arbeitet mit Viewports statt mit nativen Betriebssystem-Fenstern.
`ResizeWindow` setzt in Playwright intern `page.setViewportSize(width, height)`.
Die Viewport-Größe ist zwar überprüfbar, entspricht aber nicht der
OS-Fenstergröße — insbesondere bei `toMaximum()`, `toMinimum()` und `toFullscreen()`,
die in Playwright gar nicht unterstützt werden (`UnsupportedOperationException`).
Ein Port würde entweder andere Assertions benötigen oder Playwright-spezifische
Einschränkungen offenlegen, die nichts mit dem Verhalten des Selenium-Tests gemein haben.

---

## IT_ScrollTo – `scrollAreaDownByPixels`

**Grund:** Diese einzelne Testmethode aus `IT_ScrollTo` wurde ausgelassen
(die anderen beiden Methoden `scrollElementToTop` und `scrollToEndOfArea`
wurden portiert).

`scrollAreaDownByPixels` verwendet `ElementIsVisible.withinArea(element, scrollArea)`,
eine Hilfsklasse aus dem Paket `com.teststeps.thekla4j.browser.selenium.data`,
die ausschließlich im Selenium-Modul existiert und Selenium-interne APIs nutzt.
Es gibt kein Playwright-Äquivalent für diese Klasse.


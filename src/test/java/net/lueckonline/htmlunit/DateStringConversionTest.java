package net.lueckonline.htmlunit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.net.URL;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class DateStringConversionTest {

  private static final Logger logger = LogManager.getLogger();
  
  @Test
  public void testSubmit() throws FailingHttpStatusCodeException, IOException {
    
    logger.atDebug().log("Getting page");

    
    try(WebClient webClient = new WebClient(
        new BrowserVersion.BrowserVersionBuilder(BrowserVersion.BEST_SUPPORTED)
          .setSystemTimezone(TimeZone.getTimeZone("Europe/Berlin"))
          .build())) 
    {
      
      URL resource = this.getClass().getResource("/dateStringConversionTest.html");
      HtmlPage formPage = webClient.getPage(resource);
      webClient.waitForBackgroundJavaScript(100);
      
      HtmlForm form = formPage.getHtmlElementById("theform");
      
      assertThat(form.getInputByName("timestampField").getValueAttribute(), is("2021-12-18T22:23"));
      assertThat(form.getInputByName("textField").getValueAttribute(), is("2021-12-18T21:23:00.000Z"));
    }
  }
  
}

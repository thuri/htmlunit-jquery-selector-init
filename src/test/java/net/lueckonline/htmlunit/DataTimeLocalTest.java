package net.lueckonline.htmlunit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class DataTimeLocalTest {

  private static final Logger logger = LogManager.getLogger();
  
  @Test
  public void testSubmit() throws FailingHttpStatusCodeException, IOException {
    
    logger.atDebug().log("Getting page");
    
    try(WebClient webClient = new WebClient()) {
      URL resource = this.getClass().getResource("/datetime-local.html");
      HtmlPage formPage = webClient.getPage(resource);
      
      HtmlForm form = formPage.getHtmlElementById("theform");
      form.getInputByName("textField").setValueAttribute("Foobar");
      form.getInputByName("timestampField").setValueAttribute("2021-12-31T12:00");
  
      logger.atDebug().log("Click submit button");
      HtmlPage afterSubmit = form.getInputByName("submit").click();
      
      HtmlForm formAfterSubmit = afterSubmit.getHtmlElementById("theform");
      assertThat(formAfterSubmit.getInputByName("textField").getValueAttribute(), is("Foobar"));
      assertThat(formAfterSubmit.getInputByName("timestampField").getValueAttribute(), is("2021-12-31T12:00"));
    }
    logger.atDebug().log("finished");
  }
  
}

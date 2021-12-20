package net.lueckonline.htmlunit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.net.URL;

import org.junit.jupiter.api.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class EmailFieldValidationTest {
  
  @Test
  void stopSubmissionOnIncorretEmail() throws Exception {
    
    try(WebClient webClient = new WebClient()) {
      URL resource = this.getClass().getResource("/emailinputvalidation.html");
      HtmlPage formPage = webClient.getPage(resource);
      HtmlForm form = formPage.getHtmlElementById("theform");
      form.getInputByName("emailfield").setValueAttribute("foobar");
      form.getInputByName("numberfield").setValueAttribute("10");
//      assertThat(form.isValid(), is(false));
      
      assertThat(form.getInputByName("submit").click(), is(sameInstance(formPage)));
    }
  }
  
  @Test
  void stopSubmissionOnIncorretNumber() throws Exception {
    
    try(WebClient webClient = new WebClient()) {
      URL resource = this.getClass().getResource("/emailinputvalidation.html");
      HtmlPage formPage = webClient.getPage(resource);
      HtmlForm form = formPage.getHtmlElementById("theform");
      form.getInputByName("emailfield").setValueAttribute("foobar@example.org");
      form.getInputByName("numberfield").setValueAttribute("1");
      
//      assertThat(form.isValid(), is(false));
      
      assertThat(form.getInputByName("submit").click(), is(sameInstance(formPage)));
    }
  }
}

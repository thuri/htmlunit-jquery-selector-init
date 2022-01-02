package net.lueckonline.htmlunit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

import net.lueckonline.htmlunit.AJAXEndpointController.FormContent;

@WebMvcTest(controllers = AJAXEndpointController.class)
public class AJAXControllerTest {

  @Autowired 
  private MockMvc mockMvc;
  
  private WebClient webClient;
  
  @BeforeEach
  void setup() {
    webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc).build();
  }
  
  @Test
  void submissionFails() throws Exception {
    var page = (HtmlPage) webClient.getPage("http://localhost/ajax/form");
    
    var methodSelect = (HtmlSelect) page.getElementById("httpMethod");
    methodSelect.setSelectedAttribute(methodSelect.getOptionByValue("GET"), true);
    
    var encTypeSelect = (HtmlSelect) page.getElementById("enctype");
    encTypeSelect.setSelectedAttribute(encTypeSelect.getOptionByValue("multipart/form-data"), true);
    
    var form = (HtmlForm) page.getElementById("theform");
    form.getInputByName("submit").click();
    webClient.waitForBackgroundJavaScript(5000);
    
    assertThat(page.getElementById("resultcontainer").getTextContent().trim(), 
               is("no content"));
  }
  
  @ParameterizedTest
  @CsvSource({
    "GET, application/x-www-form-urlencoded",
    "POST, application/x-www-form-urlencoded",
    "POST, multipart/form-data",
  })
  void submissionsucceeds(String httpMethod, String encType) throws Exception {
    var page = (HtmlPage) webClient.getPage("http://localhost/ajax/form");
    
    var methodSelect = (HtmlSelect) page.getElementById("httpMethod");
    methodSelect.setSelectedAttribute(methodSelect.getOptionByValue(httpMethod), true);
    
    var encTypeSelect = (HtmlSelect) page.getElementById("enctype");
    encTypeSelect.setSelectedAttribute(encTypeSelect.getOptionByValue(encType), true);
    
    var form = (HtmlForm) page.getElementById("theform");
    
    final var textInputValue = String.format("%s %s", httpMethod, encType);
    
    form.getInputByName("textInput").setValueAttribute(textInputValue);
    
    form.getInputByName("submit").click();
    webClient.waitForBackgroundJavaScript(5000);
    
    assertThat(page.getElementById("resultcontainer").getTextContent().trim(), is(new FormContent(textInputValue, false).toString()));
  }
  
  @Test
  void submissionWithoutAJAX() throws Exception {
    var page = (HtmlPage) webClient.getPage("http://localhost/ajax/form");
    
    
    var form = (HtmlForm) page.getElementById("nonajaxFormWithPOST");
    
    final var textInputValue = String.format("%s %s", "POST", "application/x-www-form-urlencode");
    
    form.getInputByName("textInput").setValueAttribute(textInputValue);
    
    Page newPage = form.getInputByName("submit").click();

    assertThat(newPage.getWebResponse().getContentAsString(), is(new FormContent(textInputValue, false).toString()));
  }
  
//      2022-01-02 20:28:52.723  INFO 14083 --- [           main] c.gargoylesoftware.htmlunit.WebConsole   : method  = POST
//      2022-01-02 20:28:52.724  INFO 14083 --- [           main] c.gargoylesoftware.htmlunit.WebConsole   : enctype  = multipart/form-data
//      2022-01-02 20:28:52.724  INFO 14083 --- [           main] c.gargoylesoftware.htmlunit.WebConsole   : uriparamValue  = 
//      null
  
//      2022-01-02 20:28:40.492  INFO 14083 --- [           main] c.gargoylesoftware.htmlunit.WebConsole   : method  = POST
//      2022-01-02 20:28:40.492  INFO 14083 --- [           main] c.gargoylesoftware.htmlunit.WebConsole   : enctype  = application/x-www-form-urlencoded
//      2022-01-02 20:28:40.493  INFO 14083 --- [           main] c.gargoylesoftware.htmlunit.WebConsole   : uriparamValue  = 
//      textInput=POST+application%2Fx-www-form-urlencoded&checkboxInput=false&hiddenInput=DPQ0TT24S6AX2GC
  
//      2022-01-02 20:28:29.879  INFO 14083 --- [           main] c.gargoylesoftware.htmlunit.WebConsole   : method  = GET
//      2022-01-02 20:28:29.879  INFO 14083 --- [           main] c.gargoylesoftware.htmlunit.WebConsole   : enctype  = application/x-www-form-urlencoded
//      2022-01-02 20:28:29.879  INFO 14083 --- [           main] c.gargoylesoftware.htmlunit.WebConsole   : uriparamValue  = 
//      null
}


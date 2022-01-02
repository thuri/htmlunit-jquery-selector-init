package net.lueckonline.htmlunit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AJAXEndpointController {

  
  @GetMapping("/ajax/form")
  public ModelAndView getForm() {
    return new ModelAndView("ajax/testpage");
  }
  
  @RequestMapping(path="/ajax/testpage", method = {RequestMethod.GET, RequestMethod.POST}, consumes = {
      MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      MediaType.MULTIPART_FORM_DATA_VALUE
  })
  public @ResponseBody String getTestpage(@RequestParam(name = "param", required = false) String param,
                                  @RequestParam(required=false) String textInput,
                                  @RequestParam(required=false) Boolean checkboxInput,
                                  @RequestParam(required=false) String hiddenInput,
                                  FormContent formContent,
                                  BindingResult formContentBindingResult,
                                  HttpServletRequest request) throws IOException {

    getTestPage(request);
    
    if(textInput != null || checkboxInput != null) 
      return formContent.toString();
    else 
      return "no content";
  }
  
//  @RequestMapping(path="/ajax/testpage", method = {RequestMethod.GET, RequestMethod.POST})
  public @ResponseBody String getTestPage(HttpServletRequest request) throws IOException {
    
    try( var bis = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
      var line = bis.readLine();
      System.out.println(line);
    }
    
    return "no content";
  }
  
  
  public static class FormContent {
    private String textInput;
    private boolean checkboxInput;
    
    public FormContent() {
      super();
    }
    public FormContent(String textInput, boolean checkboxInput) {
      super();
      this.textInput = textInput;
      this.checkboxInput = checkboxInput;
    }
    
    public String getTextInput() {
      return textInput;
    }
    public void setTextInput(String textInput) {
      this.textInput = textInput;
    }
    public boolean isCheckboxInput() {
      return checkboxInput;
    }
    public void setCheckboxInput(boolean checkboxInput) {
      this.checkboxInput = checkboxInput;
    }
    @Override
    public String toString() {
      return "FormContent [textInput=" + textInput + ", checkboxInput=" + checkboxInput + "]";
    }
  }
}

# POST application/x-www-form-urlencoded mit MockMVC

There is an issue when POSTing AJAX Requests with enctype application/x-www-form-urlencoded to a Spring Controller
and using HtmlUnit on a MockMvc object.

When a browser is executing the ajax request it is processed by some webserver/spring logic resulting in an native request object that doesn't have the a body (reading javax.servlet.ServletRequest.getInputStream() returns null in the controller) but the `org.springframework.web.context.request.NativeWebRequest#getParameterValues`  has entries for every element in the request (when the method params are resolved in `org.springframework.web.method.annotation.RequestParamMethodArgumentResolver#resolveName`)

When running with htmlunit on mockmvc the nativeWebRequest delegates to the request that was build by `org.springframework.test.web.servlet.htmlunit.HtmlUnitRequestBuilder#buildRequest(ServletContext)` where the content/request body is set with the urlencoded string but the paramValues are not filled.

The `HtmlUnitRequestBuilder` used the `WebRequest` that was build by `com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequest` and especially the `prepareRequestContent(Object)` method.
That method does set the content of the request but not the request params as it would do it when the ajax call would use a `FormData` object.

In case of a simple form submission without any javascript htmlunit will also set the request parameters and NOT the body of the request. see `com.gargoylesoftware.htmlunit.html.HtmlForm.getWebRequest(SubmittableElement)` for this
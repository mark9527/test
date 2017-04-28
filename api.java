package com.hsbc.rbwm.digital.amh.appname;
import java.util.Map;
import com.hsbc.rbwm.digital.amh.appname.model.ApiRequest;import com.hsbc.rbwm.digital.amh.appname.model.ApiResponse;
import feign.HeaderMap;import feign.Headers;import feign.RequestLine;
public interface ApiCall {    @RequestLine("POST /dummy1")    @Headers({"Content-Type: application/json"})    public abstract ApiResponse getSomething(ApiRequest paramRequest, @HeaderMap Map<String, String> paramMap);
}

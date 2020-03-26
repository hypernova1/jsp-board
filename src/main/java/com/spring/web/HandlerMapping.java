package com.spring.web;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface HandlerMapping {

    Map<String, Object> getHandler(HttpServletRequest request);

}

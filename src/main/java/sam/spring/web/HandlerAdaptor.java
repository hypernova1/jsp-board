package sam.spring.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface HandlerAdaptor {

    String handle(Map<String, Object> target, HttpServletRequest request, HttpServletResponse response);

}

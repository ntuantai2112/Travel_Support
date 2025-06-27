package travs.common.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@Component
@Log4j2
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(String.format("%s : %s", request.getMethod(), request.getRequestURL().toString()));
        log.info("Params: " + new ObjectMapper().writeValueAsString(request.getParameterMap()));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info(String.format("RESPONSE %s : %s ", request.getMethod(), request.getRequestURL().toString()));

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpStatus.OK.value());
            response.setHeader("Access-Control-Allow-Origin", null);
        }
        log.info(String.format("Response Code: %s", response.getStatus()));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info(String.format("Response : %s", getResponsePayload(response)) + " with status:" + response.getStatus());

    }

    private String getResponsePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {

            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, 5120);
                try {
                    String response_payload = new String(buf, 0, length, wrapper.getCharacterEncoding());

                    log.info("response_payload:" + response_payload);

                    return response_payload;
                } catch (UnsupportedEncodingException ex) {
                    log.error(ex.getMessage(), ex);
                    // NOOP
                }
            }
        }
        return "[unknown]";
    }

}

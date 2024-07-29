package br.com.diogotorresdev.moneyapi.api.token;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessorFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if("/oauth/token".equalsIgnoreCase(httpRequest.getRequestURI()) &&
         "refresh_token".equals(httpRequest.getParameter("grant_type"))
        && httpRequest.getCookies() != null) {
            for(Cookie cookie : httpRequest.getCookies()) {
                if(cookie.getName().equals("refreshToken")) {
                    String refreshToken = cookie.getValue();
                    httpRequest = new MyServeletRequestWrapper(httpRequest, refreshToken);
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void destroy() {

    }

    static class MyServeletRequestWrapper extends HttpServletRequestWrapper {

        private String refreshToken;

        public MyServeletRequestWrapper(HttpServletRequest request,String refreshToken) {
            super(request);
            this.refreshToken = refreshToken;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
            map.put("refresh_token", new String[] {refreshToken});
            map.setLocked(true);
            return map;
        }
    }


}

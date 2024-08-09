package br.com.diogotorresdev.moneyapi.api.cors;

import br.com.diogotorresdev.moneyapi.api.config.property.MoneyApiProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    private MoneyApiProperty moneyApiProperty;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("Access-Control-Allow-Origin", moneyApiProperty.getPermittedOrigin());
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true"); //Tem que ter para o cookie do token seja enviado

        if("OPTIONS".equals(httpRequest.getMethod()) && moneyApiProperty.getPermittedOrigin().equals(httpRequest.getHeader("Origin"))) {
            httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            httpResponse.setHeader("Access-Control-Allow-Headers","Authorization, Content-Type, Accept");
            httpResponse.setHeader("Access-Control-Max-Age", "3600");

            httpResponse.setStatus(HttpServletResponse.SC_OK);
        }else {
            chain.doFilter(request, response);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }



    @Override
    public void destroy() {

    }
}

package vn.vnptnet.archetype.warconfigurable.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by User on 6/30/2017.
 */
@WebFilter(filterName="rewriteGroovyFilter")
public class RewriteGroovyFilter implements Filter{
    FilterConfig config;
    public void setFilterConfig(FilterConfig config) {
        this.config = config;
    }

    public FilterConfig getFilterConfig() {
        return config;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        setFilterConfig(config);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        //String url = httpServletRequest.getRequestURI();
        //String contextPath = httpServletRequest.getContextPath();
        String servletPath = httpServletRequest.getServletPath();
        //ServletContext context = getFilterConfig().getServletContext();
        //System.out.println("groovy servletPath = "+servletPath);
        if(servletPath.toLowerCase().contains(".groovy")){
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            //test on http://www.regexe.com/
            String neServletPath = servletPath.replaceFirst("([/\\\\]{1})gs(([/\\\\]{1}[a-zA-Z0-9\\-]{1,}){0,})([/\\\\]{1}[^/\\?\\\\#]{1,})","$1gs$2$4.groovy");
            //((HttpServletResponse) servletResponse).sendRedirect(newUrl);
            servletRequest.getRequestDispatcher(neServletPath).forward(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}

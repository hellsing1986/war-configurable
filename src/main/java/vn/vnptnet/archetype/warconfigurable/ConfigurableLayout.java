package vn.vnptnet.archetype.warconfigurable;

import groovy.lang.Writable;
import groovy.text.StreamingTemplateEngine;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import vn.vnptnet.archetype.warconfigurable.util.GroovyStreamTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public class ConfigurableLayout extends HttpServlet {
    //private String message;
    private static StreamingTemplateEngine engine = new groovy.text.StreamingTemplateEngine();
    public void init() throws ServletException {
        // Do required initialization
        //message = "Hello World";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ctxPath = request.getServletPath() + request.getPathInfo();
        System.out.println("ctxPath = "+ctxPath);
        try {
            PropertiesConfiguration cfg = App.getNavBuilder().getConfiguration();
            String rootCtxPath = cfg.getString("contextpath");
            List pageNames = cfg.getList("pages");

            Optional<String> oPageName = pageNames.stream().filter(ipageName->{
                String path = cfg.getString(ipageName+".path");
                System.out.println("    compare to = "+rootCtxPath+path);
                return ctxPath.equalsIgnoreCase(rootCtxPath+path);
            }).findFirst();

            if(!oPageName.isPresent()) throw new Exception("not found in navigation.properties");

            String pageName = oPageName.get();
            String layoutName = cfg.getString(pageName+".layout");
            // response.sendRedirect("/index.jsp");
            // Set response content type

            // Actual logic goes here.

            //out.println("<h1>" + message + "</h1>");
            //out.println("<h1>" + request.getServletPath() + "</h1>");
            //out.println("<h1>" + request.getContextPath() + "</h1>");
            //out.println("<h1>" + request.getMethod() + "</h1>");
            //out.println("<h1>" + request.getPathInfo() + "</h1>");
            //out.println("<h1>" + request.getPathTranslated() + "</h1>");
            //out.println("<h1>" + request.getRequestURI() + "</h1>");
            //out.println("<h1>" + request.getRequestURL() + "</h1>");
            LayoutConfiguration lc = LayoutConfiguration.getLayoutConfiguration(layoutName);
            List<ConfigurablePage> pages = lc.getPages(pageName);

            LinkedHashMap<String,String> bindOfLayout = new LinkedHashMap<>();
            for (int i=0;i<pages.size();i++){
                ConfigurablePage page = pages.get(i);

                String pageBody = page.onViewRequest(request, response);
                bindOfLayout.put("page_"+(i+1), pageBody);
            }
            Writable template = engine.createTemplate(GroovyStreamTemplate.class.getClassLoader().getResource("../../layout/"+lc.getLayoutName()+".html")).make(bindOfLayout);

            if(request.getAttribute("redirect") != null){
                response.sendRedirect(request.getAttribute("redirect").toString());
            }
            //String htmlbody = template.toString();
            //response.sendRedirect("/index.jsp");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println(template.toString());

        } catch (ConfigurationException e) {
            //e.printStackTrace();
            throw new ServletException(e);
        } catch (IOException e) {
            //e.printStackTrace();
            throw new IOException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException(e);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    public void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    public void destroy() {
        // do nothing.
    }
}

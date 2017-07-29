package vn.vnptnet.archetype.warconfigurable;

import groovy.lang.Writable;
import groovy.text.StreamingTemplateEngine;
import vn.vnptnet.archetype.warconfigurable.util.GroovyStreamTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;

public class ConfigurableLayout extends HttpServlet {
    //private String message;
    private static StreamingTemplateEngine engine = new groovy.text.StreamingTemplateEngine();
    public void init() throws ServletException {
        // Do required initialization
        //message = "Hello World";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Set response content type
        response.setContentType("text/html");
        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        //out.println("<h1>" + message + "</h1>");
        //out.println("<h1>" + request.getServletPath() + "</h1>");
        //out.println("<h1>" + request.getContextPath() + "</h1>");
        //out.println("<h1>" + request.getMethod() + "</h1>");
        //out.println("<h1>" + request.getPathInfo() + "</h1>");
        //out.println("<h1>" + request.getPathTranslated() + "</h1>");
        //out.println("<h1>" + request.getRequestURI() + "</h1>");
        //out.println("<h1>" + request.getRequestURL() + "</h1>");
        LayoutConfiguration lc = null;
        List<ConfigurablePage> pages = null;
        try {
            lc = LayoutConfiguration.getLayoutConfiguration("R_12_R_4_8");
            pages = lc.getPages();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        }
        LinkedHashMap<String,String> bindOfLayout = new LinkedHashMap<>();
        for (int i=0;i<pages.size();i++){
            ConfigurablePage page = pages.get(i);
            try {
                String pageBody = page.onViewRequest(request, response);
                bindOfLayout.put("page_"+(i+1), pageBody);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        }
        Writable template = null;
        try {
            template = engine.createTemplate(GroovyStreamTemplate.class.getClassLoader().getResource("../../layout/"+lc.getLayoutName()+".html")).make(bindOfLayout);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
        //String htmlbody = template.toString();
        out.println(template.toString());
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

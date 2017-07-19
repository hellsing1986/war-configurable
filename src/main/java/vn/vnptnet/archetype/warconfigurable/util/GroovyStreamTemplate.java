package vn.vnptnet.archetype.warconfigurable.util;

import groovy.lang.Writable;
import groovy.text.StreamingTemplateEngine;
import groovy.text.Template;
import groovy.text.markup.MarkupTemplateEngine;
import org.apache.commons.lang3.StringUtils;
import vn.vnptnet.archetype.warconfigurable.App;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by User on 7/10/2017.
 */
public class GroovyStreamTemplate {
    private static StreamingTemplateEngine engine = new groovy.text.StreamingTemplateEngine();
    /*public static String html(String body, Map bind, String title, String layout) throws IOException, ClassNotFoundException {
        //println this.getClass().getClassLoader().getResourceAsStream("../../gs/hello/hello.html").getPath()
        String goodBody = StringUtils.strip(body,"/");
        String goodLayout = StringUtils.strip(layout,"/");
        Writable template = engine.createTemplate(GroovyStreamTemplate.class.getClassLoader().getResource("../../tpl/"+goodBody+".html")).make(bind);
        String htmlbody = template.toString();

        Map blayout = new LinkedHashMap();
        blayout.put("body",htmlbody);
        blayout.put("title",title);
        Writable htmlpage = engine.createTemplate(GroovyStreamTemplate.class.getClassLoader().getResource("../../tpl/"+goodLayout+".html")).make(blayout);
        return htmlpage.toString();
    }*/

    public static String html(Map bind) throws IOException, ClassNotFoundException {
        String goodBody = StringUtils.strip(bind.get("body").toString(),"/");
        String goodLayout = StringUtils.strip(bind.get("layout").toString(),"/");
        Writable template = engine.createTemplate(GroovyStreamTemplate.class.getClassLoader().getResource("../../tpl/"+goodBody+".html")).make(bind);
        String htmlbody = template.toString();

        bind.put("html_body",htmlbody);

        Writable htmlpage = engine.createTemplate(GroovyStreamTemplate.class.getClassLoader().getResource("../../tpl/"+goodLayout+".html")).make(bind);
        return htmlpage.toString();
    }

    public static File getTemplateFile(String templatePath){
        return new File(GroovyStreamTemplate.class.getClassLoader().getResource(templatePath).getPath());
    }

    public static String component(String componentName) throws IOException, URISyntaxException, ClassNotFoundException {
        String tpl = new String(Files.readAllBytes(
                Paths.get(
                        GroovyStreamTemplate.class
                                .getClassLoader()
                                .getResource("../../gs/ui_component/"+componentName+".tpl")
                                .toURI()
                )
        ), StandardCharsets.UTF_8.name());
        return tpl;//new MarkupTemplateEngine().createTemplate(tpl);
    }
}

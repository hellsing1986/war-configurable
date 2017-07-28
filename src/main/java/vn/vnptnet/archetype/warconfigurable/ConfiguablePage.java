package vn.vnptnet.archetype.warconfigurable;

import groovy.lang.GroovyShell;
import groovy.lang.Writable;
import groovy.text.StreamingTemplateEngine;
import vn.vnptnet.archetype.warconfigurable.util.GroovyStreamTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Properties;

public class ConfiguablePage {
    private String name;
    private Properties properties;
    private File viewTemplateFile;
    private File editTemplateFile;
    private File configTemplateFile;
    private static StreamingTemplateEngine engine = new groovy.text.StreamingTemplateEngine();
    private static GroovyShell shell = new GroovyShell();
    public ConfiguablePage(String name, Properties properties) throws Exception {
        setViewTemplateFile(getFile(name,"view"));
        setEditTemplateFile(getFile(name,"edit"));
        setConfigTemplateFile(getFile(name,"config"));
        this.setName(name);
        this.setProperties(properties);
    }
    private static File getFile(String name, String mode) throws Exception{
        return new File(GroovyStreamTemplate.class.getClassLoader().getResource(
                String.format("../../page/%s/%s.groovy",name,mode)
        ).getPath());
    }

    private LinkedHashMap<String,Object> getBind(ServletRequest request, ServletResponse response){
        LinkedHashMap<String,Object> bind = new LinkedHashMap<>();
        bind.put("request", request);
        bind.put("response", response);
        bind.put("properties", properties);
        return bind;
    }

    public String onViewRequest(ServletRequest request, ServletResponse response) throws Exception {
        LinkedHashMap<String,Object> bind = getBind(request, response);
        Writable template = engine.createTemplate(getFile(getName(),"view")).make(bind);
        return template.toString();
    }
    public String onEditRequest(ServletRequest request, ServletResponse response) throws Exception {
        LinkedHashMap<String,Object> bind = getBind(request, response);
        return "";
    }
    public String onConfigRequest(ServletRequest request, ServletResponse response) throws Exception {
        LinkedHashMap<String,Object> bind = getBind(request, response);
        Writable template = engine.createTemplate(getFile(getName(),"config")).make(bind);
        return template.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getViewTemplateFile() {
        return viewTemplateFile;
    }

    public void setViewTemplateFile(File viewTemplateFile) {
        this.viewTemplateFile = viewTemplateFile;
    }

    public File getEditTemplateFile() {
        return editTemplateFile;
    }

    public void setEditTemplateFile(File editTemplateFile) {
        this.editTemplateFile = editTemplateFile;
    }

    public File getConfigTemplateFile() {
        return configTemplateFile;
    }

    public void setConfigTemplateFile(File configTemplateFile) {
        this.configTemplateFile = configTemplateFile;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}

package vn.vnptnet.archetype.warconfigurable;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import org.apache.commons.configuration2.PropertiesConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LayoutConfiguration {
    public static RegularExpression exp = new RegularExpression("R(_[\\d]+)+(_R(_[\\d]+)+)*");
    private String layoutName;
    public LayoutConfiguration(){}
    public static LayoutConfiguration getLayoutConfiguration(String pageName) throws Exception {

        if(!exp.matches(pageName)) throw new Exception("layout name is not valid, not match ^(R(_\\d){1,})(_R(_\\d){1,}){0,}$");

        LayoutConfiguration lc = new LayoutConfiguration();
        lc.setLayoutName(pageName);
        return lc;
    }

    public List<ConfigurablePage> getPages(String bigPageName) throws Exception {
        System.out.println("getPages:"+bigPageName);
        PropertiesConfiguration cfg = App.getNavBuilder().getConfiguration();
        List ret = new ArrayList();
        cfg.getKeys(bigPageName+".fragment").forEachRemaining(key->{
            System.out.println("    key:"+key);
            String val = cfg.getString(key);
            try {
                ConfigurablePage cp = new ConfigurablePage(val, null);
                ret.add(cp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return ret;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }
}

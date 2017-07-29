package vn.vnptnet.archetype.warconfigurable;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

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

    public List<ConfigurablePage> getPages() throws Exception {
        ConfigurablePage cp = new ConfigurablePage("login", null);
        return Arrays.asList( cp,cp,cp );
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }
}

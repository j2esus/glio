package com.jeegox.glio.config.general;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author j2esus
 */
public final class DataConfig {
    public static final String title = "jeegox | glio";
    public static final List<Page> pages = new ArrayList<>();
    public static final List<String> privatesUrls = new ArrayList<>();
    
    static{
        putPages();
        putPrivateUrls();
    }
    
    public static void putPages(){
        //pages.add(new Page("index","/WEB-INF/views/index.jsp", Layout.EMPTY));
        pages.add(new Page("400","/WEB-INF/views/error/400.jsp", Layout.HEADER));
        pages.add(new Page("403","/WEB-INF/views/error/403.jsp", Layout.HEADER));
        pages.add(new Page("404","/WEB-INF/views/error/404.jsp", Layout.HEADER));
        pages.add(new Page("register","/WEB-INF/views/register.jsp", Layout.HEADER));
        pages.add(new Page("thanks","/WEB-INF/views/thanks.jsp", Layout.HEADER));
        
        pages.add(new Page("forbidden","/WEB-INF/views/error/forbidden.jsp", Layout.DEFAULT));
        
        pages.add(new Page("helloworld2","/WEB-INF/views/helloworld2.jsp", Layout.DEFAULT));
        pages.add(new Page("helloworld","/WEB-INF/views/helloworld.jsp", Layout.DEFAULT));
        
        
        //admin
        pages.add(new Page("user/init","/WEB-INF/views/admin/user/init.jsp", Layout.DEFAULT));
        pages.add(new Page("company/init","/WEB-INF/views/admin/company/init.jsp", Layout.DEFAULT));
        pages.add(new Page("userType/init","/WEB-INF/views/admin/userType/init.jsp", Layout.DEFAULT));
        pages.add(new Page("token/init","/WEB-INF/views/admin/token/init.jsp", Layout.DEFAULT));
        pages.add(new Page("session/init","/WEB-INF/views/admin/session/init.jsp", Layout.DEFAULT));
        //aim
        pages.add(new Page("project/init","/WEB-INF/views/aim/project/init.jsp", Layout.DEFAULT));
        pages.add(new Page("task/init","/WEB-INF/views/aim/task/init.jsp", Layout.DEFAULT));
        pages.add(new Page("advance/init","/WEB-INF/views/aim/advance/init.jsp", Layout.DEFAULT));
        pages.add(new Page("productivity/init","/WEB-INF/views/aim/productivity/init.jsp", Layout.DEFAULT));
        pages.add(new Page("activity/init","/WEB-INF/views/aim/activity/init.jsp", Layout.DEFAULT));
        //expenses
        pages.add(new Page("category/init","/WEB-INF/views/expenses/category/init.jsp", Layout.DEFAULT));
        pages.add(new Page("expense/init","/WEB-INF/views/expenses/expense/init.jsp", Layout.DEFAULT));
        pages.add(new Page("analytic/init","/WEB-INF/views/expenses/analytic/init.jsp", Layout.DEFAULT));
        
        //all
        pages.add(new Page("all/dash","/WEB-INF/views/all/dash.jsp", Layout.DEFAULT_WITHOUT_HEADER));
        pages.add(new Page("all/configuration","/WEB-INF/views/all/configuration.jsp", Layout.DEFAULT_WITHOUT_HEADER));
        pages.add(new Page("all/module","/WEB-INF/views/all/module.jsp", Layout.DEFAULT));
    }
    
    public static void putPrivateUrls(){
        privatesUrls.add("/user/");
        privatesUrls.add("/userType/");
        privatesUrls.add("/company/");
        privatesUrls.add("/token/");
        privatesUrls.add("/session/");
        //aim
        privatesUrls.add("/project/");
        privatesUrls.add("/task/");
        privatesUrls.add("/all/");
        privatesUrls.add("/productivity/");
        privatesUrls.add("/activity/");
        //expenses
        privatesUrls.add("/category/");
        privatesUrls.add("/expense/");
        privatesUrls.add("/analytic/");
    }
    
    public static boolean isPrivate(String path){
        boolean pathPrivate = false;
        for(String item: privatesUrls){
            if(path.contains(item)){
                pathPrivate = true;
                break;
            }
        }
        return pathPrivate;
    }
    
    public static class Page{
        private String name;
        private String url;
        private Layout layout;
        
        public Page(String name, String url, Layout layout){
            this.name = name;
            this.url = url;
            this.layout = layout;
        }       

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Layout getLayout() {
            return layout;
        }

        public void setLayout(Layout layout) {
            this.layout = layout;
        }
    }
    
    public enum Layout{
        EMPTY, DEFAULT, HEADER, DEFAULT_WITHOUT_HEADER;
    }
}

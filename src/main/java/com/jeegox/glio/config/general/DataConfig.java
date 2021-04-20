package com.jeegox.glio.config.general;

import java.util.ArrayList;
import java.util.List;

public final class DataConfig {
    public static final String TITLE = "jeegox | glio";
    public static final List<Page> PAGES = new ArrayList<>();
    public static final List<String> PRIVATES_URLS = new ArrayList<>();
    
    static{
        putPages();
        putPrivateUrls();
    }
    
    public static void putPages(){
        PAGES.add(new Page("400","/WEB-INF/views/error/400.jsp", Layout.HEADER));
        PAGES.add(new Page("403","/WEB-INF/views/error/403.jsp", Layout.HEADER));
        PAGES.add(new Page("404","/WEB-INF/views/error/404.jsp", Layout.HEADER));
        PAGES.add(new Page("register","/WEB-INF/views/register.jsp", Layout.HEADER));
        PAGES.add(new Page("thanks","/WEB-INF/views/thanks.jsp", Layout.HEADER));
        
        PAGES.add(new Page("forbidden","/WEB-INF/views/error/forbidden.jsp", Layout.DEFAULT));
        
        PAGES.add(new Page("helloworld2","/WEB-INF/views/helloworld2.jsp", Layout.DEFAULT));
        PAGES.add(new Page("helloworld","/WEB-INF/views/helloworld.jsp", Layout.DEFAULT));
        
        PAGES.add(new Page("user/init","/WEB-INF/views/admin/crudUser.jsp", Layout.DEFAULT));
        PAGES.add(new Page("company/init","/WEB-INF/views/admin/crudCompany.jsp", Layout.DEFAULT));
        PAGES.add(new Page("userType/init","/WEB-INF/views/admin/crudUserType.jsp", Layout.DEFAULT));
        PAGES.add(new Page("token/init","/WEB-INF/views/admin/crudToken.jsp", Layout.DEFAULT));
        PAGES.add(new Page("session/init","/WEB-INF/views/admin/crudSession.jsp", Layout.DEFAULT));
        
        PAGES.add(new Page("project/init","/WEB-INF/views/aim/crudProject.jsp", Layout.DEFAULT));
        PAGES.add(new Page("task/init","/WEB-INF/views/aim/taskBoard.jsp", Layout.DEFAULT));
        PAGES.add(new Page("advance/init","/WEB-INF/views/aim/projectAdvance.jsp", Layout.DEFAULT));
        PAGES.add(new Page("productivity/init","/WEB-INF/views/aim/taskProductivityPerUser.jsp", Layout.DEFAULT));
        PAGES.add(new Page("activity/init","/WEB-INF/views/aim/taskActivityPerDate.jsp", Layout.DEFAULT));

        PAGES.add(new Page("category/init","/WEB-INF/views/expenses/crudCategoryAndSubcategory.jsp", Layout.DEFAULT));
        PAGES.add(new Page("expense/init","/WEB-INF/views/expenses/crudExpense.jsp", Layout.DEFAULT));
        PAGES.add(new Page("analytic/init","/WEB-INF/views/expenses/expensesAnalysis.jsp", Layout.DEFAULT));

        PAGES.add(new Page("article/init","/WEB-INF/views/supply/crudArticle.jsp", Layout.DEFAULT));
        PAGES.add(new Page("categoryArticle/init","/WEB-INF/views/supply/crudCategoryArticle.jsp", Layout.DEFAULT));
        PAGES.add(new Page("size/init","/WEB-INF/views/supply/crudSize.jsp", Layout.DEFAULT));
        PAGES.add(new Page("depot/init","/WEB-INF/views/supply/depot.jsp", Layout.DEFAULT));
        PAGES.add(new Page("stockIn/init","/WEB-INF/views/supply/stockInOut.jsp", Layout.DEFAULT));
        PAGES.add(new Page("stockOut/init","/WEB-INF/views/supply/stockInOut.jsp", Layout.DEFAULT));
        PAGES.add(new Page("availableStock/init","/WEB-INF/views/supply/availableStock.jsp", Layout.DEFAULT));
        PAGES.add(new Page("inventoryAnalysis/init","/WEB-INF/views/supply/inventoryAnalysis.jsp", Layout.DEFAULT));

        PAGES.add(new Page("client/init","/WEB-INF/views/commerce/person.jsp", Layout.DEFAULT));
        PAGES.add(new Page("provider/init","/WEB-INF/views/commerce/person.jsp", Layout.DEFAULT));

        PAGES.add(new Page("all/dash","/WEB-INF/views/all/dash.jsp", Layout.DEFAULT_WITHOUT_HEADER));
        PAGES.add(new Page("all/configuration","/WEB-INF/views/all/configuration.jsp", Layout.DEFAULT_WITHOUT_HEADER));
        PAGES.add(new Page("all/module","/WEB-INF/views/all/module.jsp", Layout.DEFAULT));
    }
    
    public static void putPrivateUrls(){
        PRIVATES_URLS.add("/user/");
        PRIVATES_URLS.add("/userType/");
        PRIVATES_URLS.add("/company/");
        PRIVATES_URLS.add("/token/");
        PRIVATES_URLS.add("/session/");

        PRIVATES_URLS.add("/project/");
        PRIVATES_URLS.add("/task/");
        PRIVATES_URLS.add("/all/");
        PRIVATES_URLS.add("/productivity/");
        PRIVATES_URLS.add("/activity/");

        PRIVATES_URLS.add("/category/");
        PRIVATES_URLS.add("/expense/");
        PRIVATES_URLS.add("/analytic/");

        PRIVATES_URLS.add("/article/");
        PRIVATES_URLS.add("/categoryArticle/");
        PRIVATES_URLS.add("/size/");
        PRIVATES_URLS.add("/depot/");
        PRIVATES_URLS.add("/stockIn/");
        PRIVATES_URLS.add("/stockOut/");
        PRIVATES_URLS.add("/availableStock/");
        PRIVATES_URLS.add("/inventoryAnalysis/");

        PRIVATES_URLS.add("/client/");
        PRIVATES_URLS.add("/provider/");
    }
    
    public static boolean isPrivate(String path){
        boolean pathPrivate = false;
        for(String item: PRIVATES_URLS){
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

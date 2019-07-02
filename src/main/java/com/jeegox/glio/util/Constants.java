package com.jeegox.glio.util;

/**
 *
 * @author j2esus
 */
public interface Constants {
    interface Security{
        String USER_SESSION = "userSession";
        String MENU = "menuSession";
        //String OPTIONS_LIST = "optionsList";
        String CATEGORY_LIST = "categoryList";
        String OPTIONS_MAP = "optionsMap";
    }
    
    interface Captcha{
        String SITE_KEY ="6Lc0EjAUAAAAAGuBbK2QpLfyYwqXOy1cdPxgVEbS";
        String SECRET_KEY ="6Lc0EjAUAAAAAKb5v2qw72tSQqXe5njKqfXZ2ANx";
    }
    
    interface Mail{
        String EMAIL = "glio@jeegox.com";
        Integer PORT = 465;
        String HOST = "host.hddpool8.net";
    }
}

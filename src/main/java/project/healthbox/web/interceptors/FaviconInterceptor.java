package project.healthbox.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FaviconInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, ModelAndView modelAndView) {

        String link = "https://www.wellnesshsf.com/wp-content/uploads/sites/3646/2016/11/favicon.png";

        if (modelAndView != null) {
            modelAndView.addObject("favicon", link);
        }
    }
}
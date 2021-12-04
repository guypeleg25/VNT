package com.example.demo.Config;

import com.example.demo.Util.MyUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyCustomInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        if (!MyUtil.validInput(request.getRequestURI())) {
            response.getWriter().write("Validation error");
            response.setStatus(400);
            return false;
        }
        return true;
    }
}
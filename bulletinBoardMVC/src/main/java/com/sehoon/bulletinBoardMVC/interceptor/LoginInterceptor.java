package com.sehoon.bulletinBoardMVC.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        // request의 cookie값 확인하기
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("JSESSIONID")) {
                System.out.println("cookie 내 JSESSIONID의 값 : " + cookie.getValue());
            }
        }

        if (session != null) {
            System.out.println("JSESSIONID = " + session.getId());
            Long userId = (Long) session.getAttribute("userId");
            request.setAttribute("userId", userId);
        } else {
            return false;
        }
        return true;
    }
}

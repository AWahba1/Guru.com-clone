package com.gurutest.Utility;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtil {
    public static String getApplicationUrl(HttpServletRequest request){
        String url=request.getRequestURL().toString();
        return url.replace(request.getServletPath(), "");
    }
}

package com.saberpro.config;

import com.saberpro.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CurrentUriInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res,
                           Object handler, ModelAndView mav) {
        if (mav == null) return;
        mav.addObject("currentUri", req.getRequestURI());

        // Inyectar usuario y rolNombre desde sesión en todos los modelos
        Object u = req.getSession(false) != null
            ? req.getSession(false).getAttribute("usuario") : null;
        if (u instanceof Usuario usuario) {
            mav.addObject("usuario",   usuario);
            mav.addObject("rolNombre", usuario.getRol().name());
        }
    }
}

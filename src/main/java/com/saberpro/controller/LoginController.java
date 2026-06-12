package com.saberpro.controller;

import com.saberpro.model.Usuario;
import com.saberpro.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired UsuarioRepository usuarioRepo;

    /** Al entrar a / redirige al login si no hay sesión */
    @GetMapping("/login")
    public String loginForm(HttpSession session, Model model) {
        if (session.getAttribute("usuario") != null) {
            return redirectPorRol((Usuario) session.getAttribute("usuario"));
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes ra) {
        return usuarioRepo.findByEmailAndPassword(email.trim(), password.trim())
            .map(u -> {
                session.setAttribute("usuario", u);
                session.setAttribute("rolNombre", u.getRol().name());
                return redirectPorRol(u);
            })
            .orElseGet(() -> {
                ra.addFlashAttribute("error", "Credenciales incorrectas. Verifica tu email y contraseña.");
                return "redirect:/login";
            });
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    /** Ruta base: si hay sesión va al dashboard, si no al login */
    @GetMapping("/")
    public String root(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        if (u == null) return "redirect:/login";
        return redirectPorRol(u);
    }

    private String redirectPorRol(Usuario u) {
        return switch (u.getRol()) {
            case ADMIN       -> "redirect:/admin/dashboard";
            case COORDINADOR -> "redirect:/coordinador/dashboard";
            case DOCENTE     -> "redirect:/docente/dashboard";
            case ESTUDIANTE  -> "redirect:/estudiante/dashboard";
        };
    }
}

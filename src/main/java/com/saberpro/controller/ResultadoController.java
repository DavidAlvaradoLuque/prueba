package com.saberpro.controller;

import com.saberpro.model.*;
import com.saberpro.repository.*;
import com.saberpro.service.ResultadoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/resultados")
public class ResultadoController {

    @Autowired ResultadoRepository repo;
    @Autowired AlumnoRepository    alumnoRepo;
    @Autowired FacultadRepository  facultadRepo;
    @Autowired ResultadoService    resultadoSvc;

    private boolean sinAcceso(HttpSession s) {
        Usuario u = (Usuario) s.getAttribute("usuario");
        return u == null || Usuario.Rol.ESTUDIANTE.equals(u.getRol());
    }

    @GetMapping
    public String listar(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("resultados", repo.findAll());
        return "comun/resultados/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("resultado", new Resultado());
        m.addAttribute("alumnos", alumnoRepo.findAll());
        m.addAttribute("tiposExamen", Resultado.TipoExamen.values());
        return "comun/resultados/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Resultado r, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        // Cargar alumno completo
        if (r.getAlumno() != null && r.getAlumno().getId() != null) {
            alumnoRepo.findById(r.getAlumno().getId()).ifPresent(r::setAlumno);
        }
        String err = resultadoSvc.validar(r);
        if (err != null) { ra.addFlashAttribute("error", err); return "redirect:/resultados/nuevo"; }
        resultadoSvc.guardar(r);
        ra.addFlashAttribute("msg", "Resultado guardado correctamente.");
        return "redirect:/resultados";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("resultado", repo.findById(id).orElseThrow());
        m.addAttribute("alumnos", alumnoRepo.findAll());
        m.addAttribute("tiposExamen", Resultado.TipoExamen.values());
        return "comun/resultados/formulario";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Resultado r, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        r.setId(id);
        if (r.getAlumno() != null && r.getAlumno().getId() != null) {
            alumnoRepo.findById(r.getAlumno().getId()).ifPresent(r::setAlumno);
        }
        String err = resultadoSvc.validar(r);
        if (err != null) { ra.addFlashAttribute("error", err); return "redirect:/resultados/editar/" + id; }
        resultadoSvc.guardar(r);
        ra.addFlashAttribute("msg", "Resultado actualizado.");
        return "redirect:/resultados";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        resultadoSvc.eliminar(id);
        ra.addFlashAttribute("msg", "Resultado eliminado.");
        return "redirect:/resultados";
    }
}

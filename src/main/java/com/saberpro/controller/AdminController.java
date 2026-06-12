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
@RequestMapping("/admin")
public class AdminController {

    @Autowired DirectorRepository  directorRepo;
    @Autowired DocenteRepository   docenteRepo;
    @Autowired FacultadRepository  facultadRepo;
    @Autowired ResultadoService    resultadoSvc;
    @Autowired AlumnoRepository    alumnoRepo;

    private boolean sinAcceso(HttpSession s) {
        Usuario u = (Usuario) s.getAttribute("usuario");
        return u == null || !Usuario.Rol.ADMIN.equals(u.getRol());
    }

    @GetMapping("/dashboard")
    public String dashboard(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("usuario",        s.getAttribute("usuario"));
        m.addAttribute("totalDirectores",directorRepo.count());
        m.addAttribute("totalDocentes",  docenteRepo.count());
        m.addAttribute("totalFacultades",facultadRepo.count());
        m.addAttribute("totalAlumnos",   alumnoRepo.count());
        m.addAttribute("facultades",     facultadRepo.findAll());
        return "admin/dashboard";
    }

    // ══ DIRECTORES ═══════════════════════════════════════════════
    @GetMapping("/directores")
    public String directores(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("directores", directorRepo.findAll());
        return "admin/directores/listar";
    }
    @GetMapping("/directores/nuevo")
    public String directorNuevo(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("director", new Director());
        m.addAttribute("facultades", facultadRepo.findAll());
        return "admin/directores/formulario";
    }
    @PostMapping("/directores/guardar")
    public String directorGuardar(@ModelAttribute Director d, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        if (d.getFacultad() != null && d.getFacultad().getId() != null)
            facultadRepo.findById(d.getFacultad().getId()).ifPresent(d::setFacultad);
        directorRepo.save(d);
        ra.addFlashAttribute("msg", "Director guardado correctamente.");
        return "redirect:/admin/directores";
    }
    @GetMapping("/directores/editar/{id}")
    public String directorEditar(@PathVariable Long id, Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("director", directorRepo.findById(id).orElseThrow());
        m.addAttribute("facultades", facultadRepo.findAll());
        return "admin/directores/formulario";
    }
    @PostMapping("/directores/actualizar/{id}")
    public String directorActualizar(@PathVariable Long id, @ModelAttribute Director d, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        d.setId(id);
        if (d.getFacultad() != null && d.getFacultad().getId() != null)
            facultadRepo.findById(d.getFacultad().getId()).ifPresent(d::setFacultad);
        directorRepo.save(d);
        ra.addFlashAttribute("msg", "Director actualizado.");
        return "redirect:/admin/directores";
    }
    @GetMapping("/directores/eliminar/{id}")
    public String directorEliminar(@PathVariable Long id, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        directorRepo.deleteById(id);
        ra.addFlashAttribute("msg", "Director eliminado.");
        return "redirect:/admin/directores";
    }

    // ══ DOCENTES ═════════════════════════════════════════════════
    @GetMapping("/docentes")
    public String docentes(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("docentes", docenteRepo.findAll());
        return "admin/docentes/listar";
    }
    @GetMapping("/docentes/nuevo")
    public String docenteNuevo(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("docente", new Docente());
        m.addAttribute("facultades", facultadRepo.findAll());
        return "admin/docentes/formulario";
    }
    @PostMapping("/docentes/guardar")
    public String docenteGuardar(@ModelAttribute Docente d, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        if (d.getFacultad() != null && d.getFacultad().getId() != null)
            facultadRepo.findById(d.getFacultad().getId()).ifPresent(d::setFacultad);
        docenteRepo.save(d);
        ra.addFlashAttribute("msg", "Docente guardado correctamente.");
        return "redirect:/admin/docentes";
    }
    @GetMapping("/docentes/editar/{id}")
    public String docenteEditar(@PathVariable Long id, Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("docente", docenteRepo.findById(id).orElseThrow());
        m.addAttribute("facultades", facultadRepo.findAll());
        return "admin/docentes/formulario";
    }
    @PostMapping("/docentes/actualizar/{id}")
    public String docenteActualizar(@PathVariable Long id, @ModelAttribute Docente d, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        d.setId(id);
        if (d.getFacultad() != null && d.getFacultad().getId() != null)
            facultadRepo.findById(d.getFacultad().getId()).ifPresent(d::setFacultad);
        docenteRepo.save(d);
        ra.addFlashAttribute("msg", "Docente actualizado.");
        return "redirect:/admin/docentes";
    }
    @GetMapping("/docentes/eliminar/{id}")
    public String docenteEliminar(@PathVariable Long id, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        docenteRepo.deleteById(id);
        ra.addFlashAttribute("msg", "Docente eliminado.");
        return "redirect:/admin/docentes";
    }

    // ══ FACULTADES ═══════════════════════════════════════════════
    @GetMapping("/facultades")
    public String facultades(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("facultades", facultadRepo.findAll());
        return "admin/facultades/listar";
    }
    @GetMapping("/facultades/nueva")
    public String facultadNueva(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("facultad", new Facultad());
        m.addAttribute("codigos", Facultad.CodigoFacultad.values());
        return "admin/facultades/formulario";
    }
    @PostMapping("/facultades/guardar")
    public String facultadGuardar(@ModelAttribute Facultad f, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        facultadRepo.save(f);
        ra.addFlashAttribute("msg", "Facultad guardada.");
        return "redirect:/admin/facultades";
    }
    @GetMapping("/facultades/editar/{id}")
    public String facultadEditar(@PathVariable Long id, Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("facultad", facultadRepo.findById(id).orElseThrow());
        m.addAttribute("codigos", Facultad.CodigoFacultad.values());
        return "admin/facultades/formulario";
    }
    @PostMapping("/facultades/actualizar/{id}")
    public String facultadActualizar(@PathVariable Long id, @ModelAttribute Facultad f, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        f.setId(id);
        facultadRepo.save(f);
        ra.addFlashAttribute("msg", "Facultad actualizada.");
        return "redirect:/admin/facultades";
    }
    @GetMapping("/facultades/eliminar/{id}")
    public String facultadEliminar(@PathVariable Long id, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        facultadRepo.deleteById(id);
        ra.addFlashAttribute("msg", "Facultad eliminada.");
        return "redirect:/admin/facultades";
    }

    // ══ BENEFICIOS ═══════════════════════════════════════════════
    @GetMapping("/beneficios")
    public String beneficios(@RequestParam(required = false) Long facultadId, Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("facultades", facultadRepo.findAll());
        m.addAttribute("facultadId", facultadId);
        m.addAttribute("resultados", facultadId != null
            ? resultadoSvc.conBeneficiosPorFacultad(facultadId)
            : resultadoSvc.conBeneficios());
        if (facultadId != null) m.addAttribute("facultadSel", facultadRepo.findById(facultadId).orElse(null));
        return "admin/beneficios";
    }
}

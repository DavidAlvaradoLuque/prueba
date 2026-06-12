package com.saberpro.controller;

import com.saberpro.model.*;
import com.saberpro.repository.*;
import com.saberpro.service.AlumnoService;
import com.saberpro.service.ResultadoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/coordinador")
public class CoordinadorController {

    @Autowired AlumnoRepository    alumnoRepo;
    @Autowired FacultadRepository  facultadRepo;
    @Autowired ResultadoRepository resultadoRepo;
    @Autowired AlumnoService       alumnoSvc;
    @Autowired ResultadoService    resultadoSvc;

    private boolean sinAcceso(HttpSession s) {
        Usuario u = (Usuario) s.getAttribute("usuario");
        return u == null || !Usuario.Rol.COORDINADOR.equals(u.getRol());
    }

    @GetMapping("/dashboard")
    public String dashboard(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("usuario", s.getAttribute("usuario"));
        List<Alumno> all = alumnoRepo.findAll();
        m.addAttribute("totalAlumnos",  all.size());
        m.addAttribute("totalTyt",      all.stream().filter(Alumno::isAprobadoTyt).count());
        m.addAttribute("totalSaberPro", all.stream().filter(Alumno::isAprobadoSaberPro).count());
        m.addAttribute("totalSinTyt",   all.stream().filter(a -> !a.isAprobadoTyt()).count());
        m.addAttribute("facultades",    facultadRepo.findAll());
        return "coordinador/dashboard";
    }

    // ══ ALUMNOS CRUD ═════════════════════════════════════════════
    @GetMapping("/alumnos")
    public String listar(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        List<Alumno> all = alumnoRepo.findAll();
        m.addAttribute("alumnos",       all);
        m.addAttribute("totalTyt",      all.stream().filter(Alumno::isAprobadoTyt).count());
        m.addAttribute("totalSaberPro", all.stream().filter(Alumno::isAprobadoSaberPro).count());
        m.addAttribute("totalSinTyt",   all.stream().filter(a -> !a.isAprobadoTyt()).count());
        return "coordinador/alumnos/listar";
    }

    @GetMapping("/alumnos/nuevo")
    public String alumnoNuevo(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("alumno", new Alumno());
        m.addAttribute("facultades", facultadRepo.findAll());
        m.addAttribute("fcseCode", Facultad.CodigoFacultad.FCSE);
        m.addAttribute("fcniCode", Facultad.CodigoFacultad.FCNI);
        m.addAttribute("programasEnum", ProgramaAcademico.values());
        return "coordinador/alumnos/formulario";
    }

    @PostMapping("/alumnos/guardar")
    public String alumnoGuardar(@ModelAttribute Alumno a, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";

        // Cargar facultad desde BD
        if (a.getFacultad() != null && a.getFacultad().getId() != null) {
            facultadRepo.findById(a.getFacultad().getId()).ifPresent(a::setFacultad);
        }

        // Validar cédula única
        String errCed = alumnoSvc.validarCedulaUnica(a);
        if (errCed != null) { ra.addFlashAttribute("error", errCed); return "redirect:/coordinador/alumnos/nuevo"; }

        // Validar semestre
        String errSem = alumnoSvc.validarSemestre(a);
        if (errSem != null) { ra.addFlashAttribute("error", errSem); return "redirect:/coordinador/alumnos/nuevo"; }

        // Validar programas
        String errProg = alumnoSvc.validarProgramas(a);
        if (errProg != null) { ra.addFlashAttribute("error", errProg); return "redirect:/coordinador/alumnos/nuevo"; }

        // Si marca aprobado SP pero no tiene TyT → rechazar
        if (a.isAprobadoSaberPro() && !a.isAprobadoTyt()) {
            ra.addFlashAttribute("error", "No puedes marcar Saber Pro como aprobado sin tener TyT aprobada.");
            return "redirect:/coordinador/alumnos/nuevo";
        }

        alumnoSvc.guardar(a);
        ra.addFlashAttribute("msg", "Alumno '" + a.getNombreCompleto() + "' guardado correctamente.");
        return "redirect:/coordinador/alumnos";
    }

    @GetMapping("/alumnos/editar/{id}")
    public String alumnoEditar(@PathVariable Long id, Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("alumno", alumnoRepo.findById(id).orElseThrow());
        m.addAttribute("facultades", facultadRepo.findAll());
        m.addAttribute("programasEnum", ProgramaAcademico.values());
        return "coordinador/alumnos/formulario";
    }

    @PostMapping("/alumnos/actualizar/{id}")
    public String alumnoActualizar(@PathVariable Long id, @ModelAttribute Alumno a, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        a.setId(id);
        if (a.getFacultad() != null && a.getFacultad().getId() != null) {
            facultadRepo.findById(a.getFacultad().getId()).ifPresent(a::setFacultad);
        }
        String errCed = alumnoSvc.validarCedulaUnica(a);
        if (errCed != null) { ra.addFlashAttribute("error", errCed); return "redirect:/coordinador/alumnos/editar/" + id; }
        String errSem = alumnoSvc.validarSemestre(a);
        if (errSem != null) { ra.addFlashAttribute("error", errSem); return "redirect:/coordinador/alumnos/editar/" + id; }
        String errProg = alumnoSvc.validarProgramas(a);
        if (errProg != null) { ra.addFlashAttribute("error", errProg); return "redirect:/coordinador/alumnos/editar/" + id; }
        if (a.isAprobadoSaberPro() && !a.isAprobadoTyt()) {
            ra.addFlashAttribute("error", "No se puede marcar SP aprobado sin TyT.");
            return "redirect:/coordinador/alumnos/editar/" + id;
        }
        alumnoSvc.guardar(a);
        ra.addFlashAttribute("msg", "Alumno actualizado.");
        return "redirect:/coordinador/alumnos";
    }

    @GetMapping("/alumnos/eliminar/{id}")
    public String alumnoEliminar(@PathVariable Long id, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        alumnoSvc.eliminar(id);
        ra.addFlashAttribute("msg", "Alumno eliminado.");
        return "redirect:/coordinador/alumnos";
    }

    @GetMapping("/alumnos/aprobar-tyt/{id}")
    public String aprobarTyt(@PathVariable Long id, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        String err = alumnoSvc.aprobarTyt(id);
        if (err != null) ra.addFlashAttribute("error", err);
        else ra.addFlashAttribute("msg", "TyT aprobada correctamente.");
        return "redirect:/coordinador/alumnos";
    }

    @GetMapping("/alumnos/aprobar-saberpro/{id}")
    public String aprobarSaberPro(@PathVariable Long id, RedirectAttributes ra, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        String err = alumnoSvc.aprobarSaberPro(id);
        if (err != null) ra.addFlashAttribute("error", err);
        else ra.addFlashAttribute("msg", "Saber Pro aprobado correctamente.");
        return "redirect:/coordinador/alumnos";
    }

    // ══ INFORMES ══════════════════════════════════════════════════
    @GetMapping("/informe-total")
    public String informeTotal(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("alumnos", alumnoRepo.findAll());
        m.addAttribute("tipoFiltro", "TOTAL");
        return "coordinador/informes";
    }

    @GetMapping("/informe-unico")
    public String informeUnico(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        var ultimos = alumnoRepo.findAll().stream()
            .map(a -> resultadoRepo.findTopByAlumnoIdOrderByFechaExamenDesc(a.getId()).orElse(null))
            .filter(r -> r != null).toList();
        m.addAttribute("resultados", ultimos);
        m.addAttribute("tipoFiltro", "UNICO");
        return "coordinador/informes";
    }

    @GetMapping("/beneficios")
    public String beneficios(@RequestParam(required = false) Long facultadId, Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("facultades", facultadRepo.findAll());
        m.addAttribute("facultadId", facultadId);
        m.addAttribute("resultados", facultadId != null
            ? resultadoSvc.conBeneficiosPorFacultad(facultadId)
            : resultadoSvc.conBeneficios());
        if (facultadId != null) m.addAttribute("facultadSel", facultadRepo.findById(facultadId).orElse(null));
        return "comun/beneficios";
    }
}

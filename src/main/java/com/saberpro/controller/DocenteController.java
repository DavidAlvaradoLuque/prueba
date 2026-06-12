package com.saberpro.controller;

import com.saberpro.model.*;
import com.saberpro.repository.*;
import com.saberpro.service.ResultadoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    @Autowired AlumnoRepository    alumnoRepo;
    @Autowired FacultadRepository  facultadRepo;
    @Autowired ResultadoRepository resultadoRepo;
    @Autowired ResultadoService    resultadoSvc;

    private boolean sinAcceso(HttpSession s) {
        Usuario u = (Usuario) s.getAttribute("usuario");
        return u == null || !Usuario.Rol.DOCENTE.equals(u.getRol());
    }

    @GetMapping("/dashboard")
    public String dashboard(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("usuario", s.getAttribute("usuario"));
        m.addAttribute("facultades", facultadRepo.findAll());
        long totalAlumnos = alumnoRepo.count();
        m.addAttribute("totalAlumnos", totalAlumnos);
        m.addAttribute("totalTyt",      alumnoRepo.findByAprobadoTytTrue().size());
        m.addAttribute("totalSaberPro", alumnoRepo.findByAprobadoSaberProTrue().size());
        return "docente/dashboard";
    }

    @GetMapping("/por-facultad")
    public String porFacultad(@RequestParam(required = false) Long facultadId,
                               @RequestParam(required = false) String tipo,
                               Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("facultades",  facultadRepo.findAll());
        m.addAttribute("tiposExamen", Resultado.TipoExamen.values());
        m.addAttribute("facultadId",  facultadId);
        m.addAttribute("tipo",        tipo);
        if (facultadId != null) {
            m.addAttribute("facultadSel", facultadRepo.findById(facultadId).orElse(null));
            m.addAttribute("alumnos",     alumnoRepo.findByFacultadId(facultadId));
            var resultados = (tipo != null && !tipo.isBlank())
                ? resultadoRepo.findByAlumnoFacultadIdAndTipoExamen(facultadId, Resultado.TipoExamen.valueOf(tipo))
                : resultadoRepo.findByAlumnoFacultadId(facultadId);
            m.addAttribute("resultados", resultados);
            m.addAttribute("totalTyt", resultados.stream().filter(r -> Resultado.TipoExamen.TYT.equals(r.getTipoExamen())).count());
            m.addAttribute("totalSp",  resultados.stream().filter(r -> Resultado.TipoExamen.SABER_PRO.equals(r.getTipoExamen())).count());
        }
        return "docente/por-facultad";
    }

    @GetMapping("/por-cedula")
    public String porCedula(@RequestParam(required = false) String cedula, Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("cedula", cedula);
        if (cedula != null && !cedula.isBlank()) {
            alumnoRepo.findByCedula(cedula.trim()).ifPresentOrElse(a -> {
                m.addAttribute("alumno", a);
                var todos = resultadoRepo.findByAlumnoId(a.getId());
                m.addAttribute("resultadosTyt", todos.stream()
                    .filter(r -> Resultado.TipoExamen.TYT.equals(r.getTipoExamen())).toList());
                m.addAttribute("resultadosSp", todos.stream()
                    .filter(r -> Resultado.TipoExamen.SABER_PRO.equals(r.getTipoExamen())).toList());
            }, () -> m.addAttribute("noEncontrado", true));
        }
        return "docente/por-cedula";
    }

    @GetMapping("/informe-total")
    public String informeTotal(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        m.addAttribute("alumnos", alumnoRepo.findAll());
        m.addAttribute("tipoFiltro", "TOTAL");
        return "docente/informes";
    }

    @GetMapping("/informe-unico")
    public String informeUnico(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        var ultimos = alumnoRepo.findAll().stream()
            .map(a -> resultadoRepo.findTopByAlumnoIdOrderByFechaExamenDesc(a.getId()).orElse(null))
            .filter(r -> r != null).toList();
        m.addAttribute("resultados", ultimos);
        m.addAttribute("tipoFiltro", "UNICO");
        return "docente/informes";
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

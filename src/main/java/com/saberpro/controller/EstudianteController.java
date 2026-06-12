package com.saberpro.controller;

import com.saberpro.model.*;
import com.saberpro.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    @Autowired AlumnoRepository    alumnoRepo;
    @Autowired ResultadoRepository resultadoRepo;

    private boolean sinAcceso(HttpSession s) {
        Usuario u = (Usuario) s.getAttribute("usuario");
        return u == null || !Usuario.Rol.ESTUDIANTE.equals(u.getRol());
    }

    /** Obtiene el alumno vinculado al usuario en sesión usando cedulaAlumno. */
    private Alumno getAlumno(HttpSession s) {
        Usuario u = (Usuario) s.getAttribute("usuario");
        if (u == null) return null;
        // Buscar por cedulaAlumno (campo directo del usuario)
        if (u.getCedulaAlumno() != null && !u.getCedulaAlumno().isBlank()) {
            return alumnoRepo.findByCedula(u.getCedulaAlumno()).orElse(null);
        }
        // Fallback: buscar por email
        return alumnoRepo.findAll().stream()
            .filter(a -> a.getEmail() != null && a.getEmail().equalsIgnoreCase(u.getEmail()))
            .findFirst().orElse(null);
    }

    @GetMapping("/dashboard")
    public String dashboard(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        Usuario u = (Usuario) s.getAttribute("usuario");
        Alumno alumno = getAlumno(s);
        m.addAttribute("usuario", u);
        if (alumno == null) {
            m.addAttribute("error", "No se encontró tu perfil de alumno. Contacta al coordinador.");
            return "estudiante/dashboard";
        }
        m.addAttribute("alumno", alumno);
        var resultados = resultadoRepo.findByAlumnoId(alumno.getId());
        m.addAttribute("ultimoTyt", resultados.stream()
            .filter(r -> Resultado.TipoExamen.TYT.equals(r.getTipoExamen()))
            .max((a, b) -> { if (a.getFechaExamen() == null) return -1;
                             if (b.getFechaExamen() == null) return 1;
                             return a.getFechaExamen().compareTo(b.getFechaExamen()); }).orElse(null));
        m.addAttribute("ultimoSaberPro", resultados.stream()
            .filter(r -> Resultado.TipoExamen.SABER_PRO.equals(r.getTipoExamen()))
            .max((a, b) -> { if (a.getFechaExamen() == null) return -1;
                             if (b.getFechaExamen() == null) return 1;
                             return a.getFechaExamen().compareTo(b.getFechaExamen()); }).orElse(null));
        return "estudiante/dashboard";
    }

    @GetMapping("/pago-tyt")
    public String formPagoTyt(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        Alumno a = getAlumno(s);
        if (a == null) return "redirect:/login";
        m.addAttribute("alumno", a);
        m.addAttribute("tipo", "TYT");
        return "estudiante/cargue-pago";
    }

    @PostMapping("/pago-tyt")
    public String guardarPagoTyt(@RequestParam String comprobante, HttpSession s, RedirectAttributes ra) {
        if (sinAcceso(s)) return "redirect:/login";
        Alumno a = getAlumno(s);
        if (a == null) return "redirect:/login";
        if (comprobante == null || comprobante.isBlank()) {
            ra.addFlashAttribute("error", "El número de comprobante no puede estar vacío.");
            return "redirect:/estudiante/pago-tyt";
        }
        a.setComprobantePagoTyt(comprobante.trim());
        alumnoRepo.save(a);
        ra.addFlashAttribute("msg", "✔ Comprobante TyT registrado. El coordinador revisará y aprobará tu inscripción.");
        return "redirect:/estudiante/dashboard";
    }

    @GetMapping("/pago-saberpro")
    public String formPagoSaberPro(Model m, HttpSession s, RedirectAttributes ra) {
        if (sinAcceso(s)) return "redirect:/login";
        Alumno a = getAlumno(s);
        if (a == null) return "redirect:/login";
        if (!a.isAprobadoTyt()) {
            ra.addFlashAttribute("error", "⚠ Debes tener la prueba TyT aprobada antes de cargar el pago Saber Pro.");
            return "redirect:/estudiante/dashboard";
        }
        if (a.getProgramaSaberPro() == null) {
            ra.addFlashAttribute("error", "⚠ No tienes programa Saber Pro asignado. Contacta al coordinador.");
            return "redirect:/estudiante/dashboard";
        }
        m.addAttribute("alumno", a);
        m.addAttribute("tipo", "SABER_PRO");
        return "estudiante/cargue-pago";
    }

    @PostMapping("/pago-saberpro")
    public String guardarPagoSaberPro(@RequestParam String comprobante, HttpSession s, RedirectAttributes ra) {
        if (sinAcceso(s)) return "redirect:/login";
        Alumno a = getAlumno(s);
        if (a == null) return "redirect:/login";
        if (!a.isAprobadoTyt()) {
            ra.addFlashAttribute("error", "⚠ No puedes pagar Saber Pro sin TyT aprobada.");
            return "redirect:/estudiante/dashboard";
        }
        if (comprobante == null || comprobante.isBlank()) {
            ra.addFlashAttribute("error", "El número de comprobante no puede estar vacío.");
            return "redirect:/estudiante/pago-saberpro";
        }
        a.setComprobantePagoSaberPro(comprobante.trim());
        alumnoRepo.save(a);
        ra.addFlashAttribute("msg", "✔ Comprobante Saber Pro registrado. El coordinador revisará y aprobará tu inscripción.");
        return "redirect:/estudiante/dashboard";
    }

    @GetMapping("/resultado-unico")
    public String resultadoUnico(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        Alumno a = getAlumno(s);
        if (a == null) return "redirect:/login";
        m.addAttribute("alumno", a);
        var resultados = resultadoRepo.findByAlumnoId(a.getId());
        m.addAttribute("ultimoTyt", resultados.stream()
            .filter(r -> Resultado.TipoExamen.TYT.equals(r.getTipoExamen()))
            .max((x, y) -> { if (x.getFechaExamen() == null) return -1;
                             if (y.getFechaExamen() == null) return 1;
                             return x.getFechaExamen().compareTo(y.getFechaExamen()); }).orElse(null));
        m.addAttribute("ultimoSaberPro", resultados.stream()
            .filter(r -> Resultado.TipoExamen.SABER_PRO.equals(r.getTipoExamen()))
            .max((x, y) -> { if (x.getFechaExamen() == null) return -1;
                             if (y.getFechaExamen() == null) return 1;
                             return x.getFechaExamen().compareTo(y.getFechaExamen()); }).orElse(null));
        return "estudiante/resultado-unico";
    }

    @GetMapping("/resultados-total")
    public String resultadosTotal(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        Alumno a = getAlumno(s);
        if (a == null) return "redirect:/login";
        List<Resultado> todos = resultadoRepo.findByAlumnoId(a.getId());
        m.addAttribute("alumno", a);
        m.addAttribute("resultadosTyt", todos.stream()
            .filter(r -> Resultado.TipoExamen.TYT.equals(r.getTipoExamen())).toList());
        m.addAttribute("resultadosSp", todos.stream()
            .filter(r -> Resultado.TipoExamen.SABER_PRO.equals(r.getTipoExamen())).toList());
        return "estudiante/resultados-total";
    }

    @GetMapping("/beneficios")
    public String beneficios(Model m, HttpSession s) {
        if (sinAcceso(s)) return "redirect:/login";
        Alumno a = getAlumno(s);
        if (a == null) return "redirect:/login";
        m.addAttribute("alumno", a);
        m.addAttribute("resultados", resultadoRepo.findByAlumnoId(a.getId()).stream()
            .filter(Resultado::tieneBeneficio).toList());
        return "estudiante/beneficios";
    }
}

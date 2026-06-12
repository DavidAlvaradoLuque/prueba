package com.saberpro.service;

import com.saberpro.model.*;
import com.saberpro.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService {

    @Autowired private AlumnoRepository alumnoRepo;
    @Autowired private FacultadRepository facultadRepo;

    // ── VALIDACIONES DE NEGOCIO ──────────────────────────────────

    /**
     * Valida que los programas TyT y SaberPro sean compatibles entre sí y con la facultad.
     */
    public String validarProgramas(Alumno a) {
        ProgramaAcademico tyt = a.getProgramaTyt();
        ProgramaAcademico sp  = a.getProgramaSaberPro();
        Facultad fac          = a.getFacultad();

        if (tyt == null) return "Debes seleccionar un programa TyT.";
        if (fac == null) return "Debes asignar una facultad.";

        // El programa TyT debe corresponder a la facultad seleccionada
        if (tyt.getCodigoFacultad() != fac.getCodigo()) {
            return "El programa TyT seleccionado no pertenece a la facultad '" + fac.getNombre() + "'.";
        }

        // Si eligió programa Saber Pro, validar compatibilidad
        if (sp != null) {
            if (sp.getCodigoFacultad() != fac.getCodigo()) {
                return "El programa Saber Pro seleccionado no pertenece a la facultad '" + fac.getNombre() + "'.";
            }
            if (!ProgramaAcademico.sonCompatibles(tyt, sp)) {
                return "El programa Saber Pro '" + sp.getNombre() + "' no es la continuación correcta de '" + tyt.getNombre() + "'. Revisa la tabla de rutas académicas.";
            }
        }

        return null; // sin error
    }

    /**
     * Valida semestre según prueba.
     * TyT: semestre 1–6. Saber Pro: semestre 7–10.
     */
    public String validarSemestre(Alumno a) {
        if (a.getSemestre() == null) return "El semestre es obligatorio.";
        if (a.getSemestre() < 1 || a.getSemestre() > 10)
            return "El semestre debe estar entre 1 y 10.";
        return null;
    }

    /** Valida que la cédula no esté duplicada (excepto para el mismo alumno). */
    public String validarCedulaUnica(Alumno a) {
        Optional<Alumno> existente = alumnoRepo.findByCedula(a.getCedula());
        if (existente.isPresent() && !existente.get().getId().equals(a.getId())) {
            return "Ya existe un alumno registrado con la cédula '" + a.getCedula() + "'.";
        }
        return null;
    }

    /** Valida que para aprobar SaberPro se tenga TyT aprobada. */
    public String validarAprobacionSaberPro(Alumno a) {
        if (!a.isAprobadoTyt()) {
            return "No se puede aprobar Saber Pro: el alumno '" + a.getNombreCompleto() + "' no tiene TyT aprobada.";
        }
        if (a.getProgramaSaberPro() == null) {
            return "No se puede aprobar Saber Pro: el alumno no tiene programa Saber Pro asignado.";
        }
        return null;
    }

    // ── CRUD ─────────────────────────────────────────────────────

    @Transactional
    public Alumno guardar(Alumno a) { return alumnoRepo.save(a); }

    @Transactional
    public void eliminar(Long id) { alumnoRepo.deleteById(id); }

    public Optional<Alumno> buscarPorId(Long id) { return alumnoRepo.findById(id); }
    public Optional<Alumno> buscarPorCedula(String cedula) { return alumnoRepo.findByCedula(cedula.trim()); }
    public List<Alumno> listarTodos() { return alumnoRepo.findAll(); }
    public List<Alumno> listarPorFacultad(Long facultadId) { return alumnoRepo.findByFacultadId(facultadId); }

    @Transactional
    public String aprobarTyt(Long alumnoId) {
        Alumno a = alumnoRepo.findById(alumnoId).orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        if (a.getComprobantePagoTyt() == null || a.getComprobantePagoTyt().isBlank()) {
            return "No se puede aprobar TyT: el alumno no tiene comprobante de pago registrado.";
        }
        a.setAprobadoTyt(true);
        alumnoRepo.save(a);
        return null;
    }

    @Transactional
    public String aprobarSaberPro(Long alumnoId) {
        Alumno a = alumnoRepo.findById(alumnoId).orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        String err = validarAprobacionSaberPro(a);
        if (err != null) return err;
        if (a.getComprobantePagoSaberPro() == null || a.getComprobantePagoSaberPro().isBlank()) {
            return "No se puede aprobar Saber Pro: el alumno no tiene comprobante de pago Saber Pro registrado.";
        }
        a.setAprobadoSaberPro(true);
        alumnoRepo.save(a);
        return null;
    }
}

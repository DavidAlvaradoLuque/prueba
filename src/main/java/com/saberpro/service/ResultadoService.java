package com.saberpro.service;

import com.saberpro.model.*;
import com.saberpro.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ResultadoService {

    @Autowired private ResultadoRepository resultadoRepo;
    @Autowired private AlumnoRepository alumnoRepo;

    /** Validaciones rigurosas antes de guardar un resultado. */
    public String validar(Resultado r) {
        if (r.getAlumno() == null || r.getAlumno().getId() == null)
            return "Debes seleccionar un alumno.";
        if (r.getTipoExamen() == null)
            return "Debes seleccionar el tipo de examen (TyT o Saber Pro).";

        Alumno a = alumnoRepo.findById(r.getAlumno().getId()).orElse(null);
        if (a == null) return "Alumno no encontrado.";

        if (Resultado.TipoExamen.SABER_PRO.equals(r.getTipoExamen())) {
            if (!a.isAprobadoTyt())
                return "El alumno '" + a.getNombreCompleto() + "' no tiene TyT aprobada. No puede registrar resultado Saber Pro.";
            if (!a.isAprobadoSaberPro())
                return "El alumno no está aprobado para Saber Pro. El coordinador debe aprobarlo primero.";
            if (a.getProgramaSaberPro() == null)
                return "El alumno no tiene programa Saber Pro asignado.";
        }

        if (Resultado.TipoExamen.TYT.equals(r.getTipoExamen())) {
            if (a.getProgramaTyt() == null)
                return "El alumno no tiene programa TyT asignado.";
        }

        if (r.getPuntajeGlobal() == null)
            return "El puntaje global es obligatorio.";
        if (r.getPuntajeGlobal() < 0 || r.getPuntajeGlobal() > 300)
            return "El puntaje global debe estar entre 0 y 300.";

        // Validar componentes si se suministran
        for (Double comp : new Double[]{
            r.getLecturaEscritura(), r.getRazonamientoCuantitativo(),
            r.getCompetenciasCiudadanas(), r.getIngles(), r.getComponenteEspecifico()
        }) {
            if (comp != null && (comp < 0 || comp > 300))
                return "Los puntajes de componentes deben estar entre 0 y 300.";
        }

        if (r.getPeriodo() != null && !r.getPeriodo().isBlank()) {
            if (!r.getPeriodo().matches("^\\d{4}-[12]$"))
                return "El formato del periodo debe ser YYYY-1 o YYYY-2. Ej: 2024-1";
        }

        return null;
    }

    @Transactional
    public Resultado guardar(Resultado r) { return resultadoRepo.save(r); }

    @Transactional
    public void eliminar(Long id) { resultadoRepo.deleteById(id); }

    public Optional<Resultado> buscarPorId(Long id) { return resultadoRepo.findById(id); }
    public List<Resultado> listarTodos() { return resultadoRepo.findAll(); }
    public List<Resultado> listarPorAlumno(Long alumnoId) { return resultadoRepo.findByAlumnoId(alumnoId); }
    public Optional<Resultado> ultimoPorAlumno(Long alumnoId) { return resultadoRepo.findTopByAlumnoIdOrderByFechaExamenDesc(alumnoId); }
    public List<Resultado> conBeneficios() { return resultadoRepo.findConBeneficios(); }
    public List<Resultado> conBeneficiosPorFacultad(Long fid) { return resultadoRepo.findConBeneficiosByFacultad(fid); }
}

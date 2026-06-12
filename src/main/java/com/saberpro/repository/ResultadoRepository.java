package com.saberpro.repository;
import com.saberpro.model.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {
    List<Resultado> findByAlumnoId(Long alumnoId);
    List<Resultado> findByAlumnoCedula(String cedula);
    List<Resultado> findByAlumnoFacultadId(Long facultadId);
    List<Resultado> findByTipoExamen(Resultado.TipoExamen tipo);
    List<Resultado> findByAlumnoFacultadIdAndTipoExamen(Long facultadId, Resultado.TipoExamen tipo);
    Optional<Resultado> findTopByAlumnoIdOrderByFechaExamenDesc(Long alumnoId);
    @Query("SELECT r FROM Resultado r WHERE r.puntajeGlobal >= 160 AND r.tipoExamen = 'SABER_PRO'")
    List<Resultado> findConBeneficios();
    @Query("SELECT r FROM Resultado r WHERE r.alumno.facultad.id = :facultadId AND r.puntajeGlobal >= 160 AND r.tipoExamen = 'SABER_PRO'")
    List<Resultado> findConBeneficiosByFacultad(Long facultadId);
}

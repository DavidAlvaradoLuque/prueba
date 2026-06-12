package com.saberpro.repository;
import com.saberpro.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    Optional<Alumno> findByCedula(String cedula);
    List<Alumno> findByFacultadId(Long facultadId);
    List<Alumno> findByAprobadoTytTrue();
    List<Alumno> findByAprobadoSaberProTrue();
}

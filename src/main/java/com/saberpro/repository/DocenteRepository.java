package com.saberpro.repository;
import com.saberpro.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    List<Docente> findByFacultadId(Long facultadId);
    Optional<Docente> findByCedula(String cedula);
}

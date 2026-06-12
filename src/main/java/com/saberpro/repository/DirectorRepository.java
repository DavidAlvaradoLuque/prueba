package com.saberpro.repository;
import com.saberpro.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DirectorRepository extends JpaRepository<Director, Long> {
    List<Director> findByFacultadId(Long facultadId);
}

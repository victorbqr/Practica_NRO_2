package com.universidad.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.universidad.model.Estudiante;
import com.universidad.model.Inscripcion;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    @Query("SELECT i FROM Inscripcion i WHERE i.estudiante.id = :estudianteId AND i.activo = true")
    List<Inscripcion> findByEstudianteId(@Param("estudianteId") Long estudianteId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Inscripcion> findByIdAndActivoTrue(Long id); 

    boolean existsByEstudianteIdAndMateriaIdAndActivoTrue(Long estudianteId, Long materiaId);
    @Query("SELECT COUNT(i) > 0 FROM Inscripcion i " +
           "WHERE i.estudiante.id = :estudianteId " +
           "AND i.materia.id IN (SELECT p.id FROM Materia m JOIN m.prerequisitos p WHERE m.id = :materiaId) " +
           "AND i.estado = 'APROBADA'")
    boolean verificarPrerequisitosCompletos(@Param("estudianteId") Long estudianteId, @Param("materiaId") Long materiaId);
}
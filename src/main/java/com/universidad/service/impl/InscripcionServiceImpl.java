package com.universidad.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // Importa la anotación Service de Spring

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Estudiante;
import com.universidad.model.Inscripcion;
import com.universidad.model.Inscripcion.EstadoInscripcion;
import com.universidad.model.Materia;
import com.universidad.repository.EstudianteRepository;
import com.universidad.repository.InscripcionRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IInscripcionService;


import jakarta.persistence.EntityNotFoundException;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;




@Service
public class InscripcionServiceImpl implements IInscripcionService {
    @Autowired
    private final InscripcionRepository inscripcionRepository = null;
    @Autowired
    private final EstudianteRepository estudianteRepository = null;
    @Autowired
    private final MateriaRepository materiaRepository = null;

    @Override 
    @Cacheable(value = "inscripcionesEstudiante", key = "#estudianteId")
    public List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long estudianteId) {
        return inscripcionRepository.findByEstudianteId(estudianteId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "inscripcion", key = "#id")
    public InscripcionDTO obtenerPorId(Long id) {
        Inscripcion inscripcion = inscripcionRepository.findByIdAndActivoTrue(id)
            .orElseThrow(() -> new EntityNotFoundException("Inscripcion no encontrada"));
        return convertToDTO(inscripcion);
    }

    @Override
    @CacheEvict(value = {"inscripcionesEstudiante", "estudianteInscripciones"}, allEntries = true)
    public InscripcionDTO crear(InscripcionDTO inscripcionDTO) {
        // Validar estudiante
        Estudiante estudiante = estudianteRepository.findById(inscripcionDTO.getEstudianteId())
            .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado o inactivo"));

        // Validar materia
        Materia materia = materiaRepository.findById(inscripcionDTO.getMateriaId())
            .orElseThrow(() -> new EntityNotFoundException("Materia no encontrada o inactiva"));

        // Validar inscripción existente
        if (inscripcionRepository.existsByEstudianteIdAndMateriaIdAndActivoTrue(
                estudiante.getId(), materia.getId())) {
            throw new RuntimeException("El estudiante ya está inscrito en esta materia");
        }

        // Validar prerequisitos
        if (!materia.getPrerequisitos().isEmpty() && 
            !inscripcionRepository.verificarPrerequisitosCompletos(
                estudiante.getId(), materia.getId())) {
            throw new RuntimeException("No cumple con los prerequisitos necesarios");
        }

        Inscripcion inscripcion = Inscripcion.builder()
            .estudiante(estudiante)
            .materia(materia)
            .fechaInscripcion(inscripcionDTO.getFechaInscripcion())
            .estado(EstadoInscripcion.PENDIENTE)
            .activo(true)
            .build();

        Inscripcion saved = inscripcionRepository.save(inscripcion);
        return convertToDTO(saved);
    }

    @Override
    @CachePut(value = "inscripcion", key = "#id")
    @CacheEvict(value = {"inscripcionesEstudiante", "estudianteInscripciones"}, allEntries = true)
    public InscripcionDTO actualizarEstado(Long id, EstadoInscripcion estado) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada"));
        
        if (!inscripcion.getActivo()) {
            throw new RuntimeException("No se puede modificar una inscripción inactiva");
        }
        
        inscripcion.setEstado(estado);
        return convertToDTO(inscripcionRepository.save(inscripcion));
    }

    @Override
    @CacheEvict(value = {"inscripcion", "inscripcionesEstudiante", "estudianteInscripciones"}, key = "#id")
    public void cancelarInscripcion(Long id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada"));
        
        if (inscripcion.getEstado() == EstadoInscripcion.APROBADA) {
            throw new RuntimeException("No se puede cancelar una inscripción aprobada");
        }
        
        inscripcion.setEstado(EstadoInscripcion.CANCELADA);
        inscripcion.setActivo(false);
        inscripcionRepository.save(inscripcion);
    }

    private InscripcionDTO convertToDTO(Inscripcion inscripcion) {
        return InscripcionDTO.builder()
            .id(inscripcion.getId())
            .estudianteId(inscripcion.getEstudiante().getId())
            .estudianteNombre(inscripcion.getEstudiante().getNombre()+ " "+inscripcion.getEstudiante().getApellido())
            .materiaId(inscripcion.getMateria().getId())
            .materiaNombre(inscripcion.getMateria().getNombreMateria())
            .fechaInscripcion(inscripcion.getFechaInscripcion())
            .estado(inscripcion.getEstado())
            .activo(inscripcion.getActivo())
            .build();
    }

}

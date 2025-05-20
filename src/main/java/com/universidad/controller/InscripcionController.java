package com.universidad.controller;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Inscripcion.EstadoInscripcion;
import com.universidad.registro.security.JwtUtils;
import com.universidad.service.IInscripcionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v3/inscripciones")
@Tag(name = "Inscripciones", description = "Gestión de inscripciones de estudiantes")
public class InscripcionController {
    private final IInscripcionService inscripcionService;
    private final JwtUtils jwtUtils;
    @Autowired // Anotación que indica que el constructor debe ser usado para inyección de dependencias
    public InscripcionController(IInscripcionService inscripcionService) { // Constructor que recibe el servicio de estudiantes
        this.inscripcionService = inscripcionService; // Asigna el servicio de estudiantes a la variable de instancia
        this.jwtUtils = new JwtUtils();
    }
    
    @Operation(summary = "Obtener inscripciones por estudiante")
    @GetMapping("/estudiante/{estudianteId}")
    @PreAuthorize("hasRole('ESTUDIANTE') and hasRole('ADMIN')")
    public ResponseEntity<List<InscripcionDTO>> obtenerPorEstudiante(@PathVariable Long estudianteId){
        return ResponseEntity.ok(inscripcionService.obtenerInscripcionesPorEstudiante(estudianteId));
    }

    @Operation(summary = "Crear nueva inscripción")
    @PostMapping
    @PreAuthorize("hasRole('ESTUDIANTE') and hasRole('ADMIN')")
    @CacheEvict(value = {"inscripcionesEstudiante", "estudianteInscripciones"}, allEntries = true)
    public ResponseEntity<InscripcionDTO> crear(
        @Valid @RequestBody InscripcionDTO inscripcionDTO,
        HttpServletRequest request) {
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(inscripcionService.crear(inscripcionDTO));
    }

   
    @Operation(summary = "Actualizar estado de inscripción")
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    @CachePut(value = "inscripcion", key = "#id")
    @CacheEvict(value = {"inscripcionesEstudiante", "estudianteInscripciones"}, allEntries = true)
    public ResponseEntity<InscripcionDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoInscripcion estado) {
        return ResponseEntity.ok(inscripcionService.actualizarEstado(id, estado));
    }

    @Operation(summary = "Cancelar inscripción")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ESTUDIANTE') and @inscripcionServiceImpl.validarPropietario(#id, principal.id) or hasRole('ADMIN')")
    @CacheEvict(value = {"inscripcion", "inscripcionesEstudiante", "estudianteInscripciones"}, key = "#id")
    public ResponseEntity<Void> cancelar(
            @PathVariable Long id) {
        inscripcionService.cancelarInscripcion(id);
        return ResponseEntity.noContent().build();
    }
}

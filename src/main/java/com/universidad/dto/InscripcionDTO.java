package com.universidad.dto;

import java.time.LocalDate;

import com.universidad.model.Inscripcion.EstadoInscripcion;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionDTO {
    private Long id;
    
    @NotNull(message = "El ID del estudiante es obligatorio")
    private Long estudianteId;
    
    private String estudianteNombre;
    
    @NotNull(message = "El ID de la materia es obligatorio")
    private Long materiaId;
    
    private String materiaNombre;
    
    @NotNull(message = "La fecha de inscripción es obligatoria")
    @FutureOrPresent(message = "La fecha debe ser hoy o en el futuro")
    private LocalDate fechaInscripcion;
    
    private EstadoInscripcion estado;
    
    private Boolean activo;
}


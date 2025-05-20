package com.universidad.service;

import java.util.List;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Inscripcion.EstadoInscripcion;

public interface IInscripcionService {
    List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long estudianteId);

    InscripcionDTO obtenerPorId(Long id);

    InscripcionDTO crear(InscripcionDTO inscripcionDTO);

    InscripcionDTO actualizarEstado(Long id, EstadoInscripcion estado);

    void cancelarInscripcion(Long id);


}

package com.agenciaviajes.backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.agenciaviajes.backend.exception.ResourceNotFoundException;
import com.agenciaviajes.backend.model.Destino;
import com.agenciaviajes.backend.repository.DestinoRepository;
import com.agenciaviajes.backend.service.DestinoService;

@Service
public class DestinoServiceImpl implements DestinoService{
    private final DestinoRepository destinoRepository;

    public DestinoServiceImpl(DestinoRepository destinoRepository){
        this.destinoRepository=destinoRepository;
    }

    @Override
    public List<Destino> listar() {
        return destinoRepository.findAll();
    }

    @Override
    public Destino buscarPorId(Integer id) {
        return destinoRepository.findById(id)
        .orElseThrow(()-> new ResourceNotFoundException("Destino no encontrado con id: "+ id));    
    }

    @Override
    public Destino guardar(Destino destino) {
        return destinoRepository.save(destino);
    }

    @Override
    public Destino actualizar(Integer id, Destino destino) {
       Destino existente = buscarPorId(id);

        existente.setNombre(destino.getNombre());
        existente.setPais(destino.getPais());
        existente.setCiudad(destino.getCiudad());
        existente.setDescripcion(destino.getDescripcion());
    return destinoRepository.save(existente);
    }

    @Override
    public void eliminar(Integer id) {
     Destino destino= buscarPorId(id);
        destinoRepository.delete(destino);
        
    }  

}

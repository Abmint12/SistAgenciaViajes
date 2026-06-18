package com.agenciaviajes.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.agenciaviajes.backend.dto.ReservaRequest;
import com.agenciaviajes.backend.dto.ReservaResponse;
import com.agenciaviajes.backend.model.Cliente;
import com.agenciaviajes.backend.model.Destino;
import com.agenciaviajes.backend.model.Reserva;
import com.agenciaviajes.backend.repository.ClienteRepository;
import com.agenciaviajes.backend.repository.DestinoRepository;
import com.agenciaviajes.backend.repository.ReservaRepository;
import com.agenciaviajes.backend.service.ReservaService;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final DestinoRepository destinoRepository;

    public ReservaServiceImpl(
            ReservaRepository reservaRepository,
            ClienteRepository clienteRepository,
            DestinoRepository destinoRepository) {

        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.destinoRepository = destinoRepository;
    }

    @Override
    public List<ReservaResponse> listar() {

        return reservaRepository.findAll()
                .stream()
                .map(this::convertirResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReservaResponse obtener(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        return convertirResponse(reserva);
    }

    @Override
    public ReservaResponse crear(ReservaRequest request) {

        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Destino destino = destinoRepository.findById(request.getIdDestino())
                .orElseThrow(() -> new RuntimeException("Destino no encontrado"));

        Reserva reserva = new Reserva();

        reserva.setCliente(cliente);
        reserva.setDestino(destino);
        reserva.setFechaViaje(request.getFechaViaje());
        reserva.setCantPasajes(request.getCantPasajes());
        reserva.setCostoTotal(request.getCostoTotal());
        reserva.setEstado(request.getEstado());

        return convertirResponse(reservaRepository.save(reserva));
    }

    @Override
    public ReservaResponse actualizar(Long id, ReservaRequest request) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Destino destino = destinoRepository.findById(request.getIdDestino())
                .orElseThrow(() -> new RuntimeException("Destino no encontrado"));

        reserva.setCliente(cliente);
        reserva.setDestino(destino);
        reserva.setFechaViaje(request.getFechaViaje());
        reserva.setCantPasajes(request.getCantPasajes());
        reserva.setCostoTotal(request.getCostoTotal());
        reserva.setEstado(request.getEstado());
        //return 
        return convertirResponse(reservaRepository.save(reserva));
    }

    @Override
    public void eliminar(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reservaRepository.delete(reserva);
    }

    private ReservaResponse convertirResponse(Reserva reserva) {

        ReservaResponse response = new ReservaResponse();

        response.setId(reserva.getId());

        response.setIdCliente(reserva.getCliente().getId());

        response.setNombreCliente(
                reserva.getCliente().getNombre() + " "
                + reserva.getCliente().getApellido());

        response.setIdDestino(reserva.getDestino().getIdDestino());

        response.setNombreDestino(reserva.getDestino().getNombre());

        response.setFechaViaje(reserva.getFechaViaje());

        response.setCantPasajes(reserva.getCantPasajes());

        response.setCostoTotal(reserva.getCostoTotal());

        response.setEstado(reserva.getEstado());

        return response;
    }
}
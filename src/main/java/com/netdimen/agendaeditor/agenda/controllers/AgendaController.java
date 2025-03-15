package com.netdimen.agendaeditor.agenda.controllers;

import com.netdimen.agendaeditor.agenda.dto.*;

import com.netdimen.agendaeditor.agenda.services.AgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/agendas")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    @GetMapping
    public ResponseEntity<Page<AgendaResponseDTO>> getAllAgendas(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(agendaService.getAllAgendas(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> getAgendaById(@PathVariable Long id) {
        Optional<AgendaResponseDTO> agenda = agendaService.getAgendaById(id);
        return agenda.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AgendaResponseDTO> saveAgenda(@RequestBody AgendaRequestDTO agendaDTO) {
        AgendaResponseDTO savedAgenda = agendaService.saveOrUpdateAgenda(null, agendaDTO);
        return ResponseEntity.ok(savedAgenda);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> updateAgenda(@PathVariable Long id, @RequestBody AgendaRequestDTO agendaDTO) {
        AgendaResponseDTO updatedAgenda = agendaService.saveOrUpdateAgenda(id, agendaDTO);
        return ResponseEntity.ok(updatedAgenda);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgenda(@PathVariable Long id) {
        agendaService.deleteAgenda(id);
        return ResponseEntity.noContent().build();
    }
}



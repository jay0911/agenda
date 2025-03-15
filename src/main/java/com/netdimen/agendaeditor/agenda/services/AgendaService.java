package com.netdimen.agendaeditor.agenda.services;

import com.netdimen.agendaeditor.agenda.dto.*;
import com.netdimen.agendaeditor.agenda.models.Agenda;
import com.netdimen.agendaeditor.agenda.models.AgendaItem;
import com.netdimen.agendaeditor.agenda.repositories.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;

    @Cacheable("agendas")
    public Page<AgendaResponseDTO> getAllAgendas(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return agendaRepository.findAll(pageRequest).map(this::convertToAgendaResponseDTO);
    }

    @Cacheable(value = "agenda", key = "#id")
    public Optional<AgendaResponseDTO> getAgendaById(Long id) {
        return agendaRepository.findById(id).map(this::convertToAgendaResponseDTO);
    }

    @Transactional
    @CachePut(value = "agenda", key = "#result.id")
    @CacheEvict(value = "agendas", allEntries = true)
    public AgendaResponseDTO saveOrUpdateAgenda(Long id, AgendaRequestDTO agendaDTO) {
        Agenda agenda = id != null ? agendaRepository.findById(id).orElse(new Agenda()) : new Agenda();
        agenda.setName(agendaDTO.getName());

        // Fetch existing agenda items if updating
        Set<Long> existingItemIds = agenda.getAgendaItemList().stream()
                .map(AgendaItem::getId)
                .collect(Collectors.toSet());

        List<AgendaItem> updatedItems = agendaDTO.getAgendaItems().stream().map(itemDTO -> {
            AgendaItem item = new AgendaItem();
            item.setItemOrder(itemDTO.getItemOrder());
            item.setPhase(itemDTO.getPhase());
            item.setContent(itemDTO.getContent());
            item.setObjectives(itemDTO.getObjectives());
            item.setDuration(itemDTO.getDuration());
            item.setCreditable(itemDTO.isCreditable());
            item.setAgenda(agenda);
            return item;
        }).collect(Collectors.toList());

        // Remove agenda items that are not in the updated list
        agenda.getAgendaItemList().removeIf(existingItem -> !updatedItems.contains(existingItem));
        agenda.getAgendaItemList().clear();
        agenda.getAgendaItemList().addAll(updatedItems);

        Agenda savedAgenda = agendaRepository.save(agenda);
        return convertToAgendaResponseDTO(savedAgenda);
    }

    @Transactional
    @CacheEvict(value = {"agenda", "agendas"}, key = "#id", allEntries = true)
    public void deleteAgenda(Long id) {
        agendaRepository.deleteById(id);
    }

    private AgendaResponseDTO convertToAgendaResponseDTO(Agenda agenda) {
        AgendaResponseDTO dto = new AgendaResponseDTO();
        dto.setId(agenda.getId());
        dto.setName(agenda.getName());
        dto.setAgendaItems(agenda.getAgendaItemList().stream().map(this::convertToAgendaItemResponseDTO).collect(Collectors.toList()));
        return dto;
    }

    private AgendaItemResponseDTO convertToAgendaItemResponseDTO(AgendaItem item) {
        AgendaItemResponseDTO dto = new AgendaItemResponseDTO();
        dto.setId(item.getId());
        dto.setItemOrder(item.getItemOrder());
        dto.setPhase(item.getPhase());
        dto.setContent(item.getContent());
        dto.setObjectives(item.getObjectives());
        dto.setDuration(item.getDuration());
        dto.setCreditable(item.isCreditable());
        return dto;
    }
}
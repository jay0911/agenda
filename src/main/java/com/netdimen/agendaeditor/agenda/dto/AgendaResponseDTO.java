package com.netdimen.agendaeditor.agenda.dto;

import lombok.Data;

import java.util.List;

@Data
public class AgendaResponseDTO {
    private Long id;
    private String name;
    private List<AgendaItemResponseDTO> agendaItems;
}
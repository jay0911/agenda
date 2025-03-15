package com.netdimen.agendaeditor.agenda.dto;

import lombok.Data;

import java.util.List;

@Data
public class AgendaRequestDTO {
    private String name;
    private List<AgendaItemRequestDTO> agendaItems;
}

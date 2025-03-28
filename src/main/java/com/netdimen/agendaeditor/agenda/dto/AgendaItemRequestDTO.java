package com.netdimen.agendaeditor.agenda.dto;

import lombok.Data;

@Data
public class AgendaItemRequestDTO {
    private int itemOrder;
    private String phase;
    private String content;
    private String objectives;
    private Long duration;
    private boolean creditable;
}

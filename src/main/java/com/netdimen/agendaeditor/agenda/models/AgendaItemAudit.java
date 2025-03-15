package com.netdimen.agendaeditor.agenda.models;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Audited
@Table(name = "agenda_item_audit")
public class AgendaItemAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long agendaItemId;

    private String changedBy;

    private String changeType;

    private LocalDateTime changeDate;
}
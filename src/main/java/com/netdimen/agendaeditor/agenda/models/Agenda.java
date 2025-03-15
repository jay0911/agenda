package com.netdimen.agendaeditor.agenda.models;

import com.netdimen.agendaeditor.agenda.models.AgendaItem;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agenda")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgendaItem> agendaItemList = new ArrayList<>();

    public void addAgendaItem(AgendaItem item) {
        agendaItemList.add(item);
        item.setAgenda(this);
    }

    public void removeAgendaItem(AgendaItem item) {
        agendaItemList.remove(item);
        item.setAgenda(null);
    }

    public Agenda(String name) {
        this.name = name;
    }
}
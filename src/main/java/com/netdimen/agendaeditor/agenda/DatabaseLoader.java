package com.netdimen.agendaeditor.agenda;

import com.netdimen.agendaeditor.agenda.models.Agenda;
import com.netdimen.agendaeditor.agenda.models.AgendaItem;
import com.netdimen.agendaeditor.agenda.models.Role;
import com.netdimen.agendaeditor.agenda.models.User;
import com.netdimen.agendaeditor.agenda.repositories.AgendaItemRepository;
import com.netdimen.agendaeditor.agenda.repositories.AgendaRepository;
import com.netdimen.agendaeditor.agenda.repositories.RoleRepository;
import com.netdimen.agendaeditor.agenda.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final AgendaRepository agendaRepository;

    private final AgendaItemRepository agendaItemRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public DatabaseLoader(AgendaRepository agendaRepository, AgendaItemRepository agendaItemRepository, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.agendaRepository = agendaRepository;
        this.agendaItemRepository = agendaItemRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        createAgendaWithItem(1);
        createAgendaWithItem(2);
        createAgendaWithItem(3);
        createAgendaWithItem(4);
        createAgendaWithItem(5);

        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roleRepository.save(userRole);

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        roleRepository.save(adminRole);

        // Create admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRoles(Set.of(adminRole));
        userRepository.save(admin);

        // Create regular user
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        System.out.println("Users and roles inserted successfully!");
    }

    private void createAgendaWithItem(int count) {
        Agenda agenda = new Agenda("Agenda " + count);
        AgendaItem item = new AgendaItem(1, "Welcome", "", "", 15l, false, agenda);
        agendaRepository.save(agenda);
        agendaItemRepository.save(item);
    }
}

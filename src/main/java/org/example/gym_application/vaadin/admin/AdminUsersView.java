package org.example.gym_application.vaadin.admin;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin/users", layout = AdminLayout.class)
@PageTitle("Uzytkownicy | GymApp")
@RolesAllowed("ADMIN")
public class AdminUsersView extends VerticalLayout {

    public AdminUsersView() {
        setPadding(true);
        H1 title = new H1("Zarzadzanie uzytkownikami");
        title.getStyle().set("color", "#e74c3c");
        add(title, new Paragraph("Dodawaj, edytuj i usuwaj uzytkownikow systemu."));
    }
}


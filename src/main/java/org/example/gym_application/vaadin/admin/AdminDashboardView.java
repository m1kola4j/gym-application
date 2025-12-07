package org.example.gym_application.vaadin.admin;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin/dashboard", layout = AdminLayout.class)
@PageTitle("Panel Admina | GymApp")
@RolesAllowed("ADMIN")
public class AdminDashboardView extends VerticalLayout {

    public AdminDashboardView() {
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("Panel Administratora");
        title.getStyle().set("color", "#e74c3c");

        Paragraph welcome = new Paragraph("Witaj! Masz pelna kontrole nad systemem.");

        add(title, welcome);
    }
}

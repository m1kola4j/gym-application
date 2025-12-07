package org.example.gym_application.vaadin.admin;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin/reservations", layout = AdminLayout.class)
@PageTitle("Rezerwacje | GymApp")
@RolesAllowed("ADMIN")
public class AdminReservationsView extends VerticalLayout {

    public AdminReservationsView() {
        setPadding(true);
        H1 title = new H1("Rezerwacje");
        title.getStyle().set("color", "#e74c3c");
        add(title, new Paragraph("Wszystkie rezerwacje w systemie."));
    }
}


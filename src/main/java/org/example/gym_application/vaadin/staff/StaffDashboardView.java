package org.example.gym_application.vaadin.staff;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "staff/dashboard", layout = StaffLayout.class)
@PageTitle("Panel Pracownika | GymApp")
@RolesAllowed("STAFF")
public class StaffDashboardView extends VerticalLayout {

    public StaffDashboardView() {
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("Panel Pracownika");
        title.getStyle().set("color", "#2ecc71");

        Paragraph welcome = new Paragraph("Witaj! Obsluguj klientow i zarzadzaj zajeciami.");

        add(title, welcome);
    }
}

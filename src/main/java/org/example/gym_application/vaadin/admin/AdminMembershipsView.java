package org.example.gym_application.vaadin.admin;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin/memberships", layout = AdminLayout.class)
@PageTitle("Karnety | GymApp")
@RolesAllowed("ADMIN")
public class AdminMembershipsView extends VerticalLayout {

    public AdminMembershipsView() {
        setPadding(true);
        H1 title = new H1("Karnety");
        title.getStyle().set("color", "#e74c3c");
        add(title, new Paragraph("Zarzadzanie karnetami i cenami."));
    }
}


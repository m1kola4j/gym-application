package org.example.gym_application.vaadin.staff;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "staff/members", layout = StaffLayout.class)
@PageTitle("Czlonkowie | GymApp")
@RolesAllowed("STAFF")
public class StaffMembersView extends VerticalLayout {

    public StaffMembersView() {
        setPadding(true);
        H1 title = new H1("Czlonkowie");
        title.getStyle().set("color", "#2ecc71");
        add(title, new Paragraph("Lista czlonkow silowni."));
    }
}

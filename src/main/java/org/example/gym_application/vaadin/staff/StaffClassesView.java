package org.example.gym_application.vaadin.staff;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "staff/classes", layout = StaffLayout.class)
@PageTitle("Zajecia | GymApp")
@RolesAllowed("STAFF")
public class StaffClassesView extends VerticalLayout {

    public StaffClassesView() {
        setPadding(true);
        H1 title = new H1("Zajecia");
        title.getStyle().set("color", "#2ecc71");
        add(title, new Paragraph("Lista zajec i uczestnicy."));
    }
}

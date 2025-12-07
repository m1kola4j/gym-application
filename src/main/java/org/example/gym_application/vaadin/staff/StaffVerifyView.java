package org.example.gym_application.vaadin.staff;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "staff/verify", layout = StaffLayout.class)
@PageTitle("Weryfikacja | GymApp")
@RolesAllowed("STAFF")
public class StaffVerifyView extends VerticalLayout {

    public StaffVerifyView() {
        setPadding(true);
        H1 title = new H1("Weryfikacja wejscia");
        title.getStyle().set("color", "#2ecc71");
        add(title, new Paragraph("Sprawdz czy klient ma aktywny karnet."));
    }
}

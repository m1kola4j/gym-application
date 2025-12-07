package org.example.gym_application.vaadin.member;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "member/reservations", layout = MemberLayout.class)
@PageTitle("Moje rezerwacje | GymApp")
@RolesAllowed("MEMBER")
public class MemberReservationsView extends VerticalLayout {

    public MemberReservationsView() {
        setPadding(true);
        H1 title = new H1("Moje rezerwacje");
        title.getStyle().set("color", "#3498db");
        add(title, new Paragraph("Lista Twoich rezerwacji."));
    }
}

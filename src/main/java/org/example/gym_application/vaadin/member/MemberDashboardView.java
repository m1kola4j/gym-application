package org.example.gym_application.vaadin.member;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "member/dashboard", layout = MemberLayout.class)
@PageTitle("Moj panel | GymApp")
@RolesAllowed("MEMBER")
public class MemberDashboardView extends VerticalLayout {

    public MemberDashboardView() {
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("Witaj!");
        title.getStyle().set("color", "#3498db");

        Paragraph welcome = new Paragraph("To Twoj panel czlonkowski.");

        add(title, welcome);
    }
}

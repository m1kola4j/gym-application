package org.example.gym_application.vaadin.member;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "member/classes", layout = MemberLayout.class)
@PageTitle("Zajecia | GymApp")
@RolesAllowed("MEMBER")
public class MemberClassesView extends VerticalLayout {

    public MemberClassesView() {
        setPadding(true);
        H1 title = new H1("Zajecia");
        title.getStyle().set("color", "#3498db");
        add(title, new Paragraph("Przegladaj dostepne zajecia."));
    }
}

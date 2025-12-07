package org.example.gym_application.vaadin.member;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.server.VaadinServletRequest;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.ServletException;

@RolesAllowed("MEMBER")
public class MemberLayout extends AppLayout {

    public MemberLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("GymApp");
        logo.getStyle().set("font-size", "1.5rem").set("margin", "0").set("color", "#3498db");

        Span badge = new Span("CZLONEK");
        badge.getStyle()
            .set("background", "#3498db")
            .set("color", "white")
            .set("padding", "4px 12px")
            .set("border-radius", "20px")
            .set("font-size", "0.8rem")
            .set("font-weight", "bold");

        Button logoutBtn = new Button("Wyloguj", VaadinIcon.SIGN_OUT.create(), e -> logout());
        logoutBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        HorizontalLayout left = new HorizontalLayout(new DrawerToggle(), logo, badge);
        left.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        HorizontalLayout header = new HorizontalLayout(left, logoutBtn);
        header.setWidthFull();
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.getStyle().set("padding", "10px 20px");
        header.expand(left);

        addToNavbar(header);
    }

    private void createDrawer() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "/member/dashboard", VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Moj karnet", "/member/membership", VaadinIcon.CREDIT_CARD.create()));
        nav.addItem(new SideNavItem("Zajecia", "/member/classes", VaadinIcon.CALENDAR.create()));
        nav.addItem(new SideNavItem("Moje rezerwacje", "/member/reservations", VaadinIcon.CLIPBOARD_CHECK.create()));
        nav.addItem(new SideNavItem("Profil", "/member/profile", VaadinIcon.USER.create()));

        VerticalLayout drawer = new VerticalLayout(nav);
        drawer.setPadding(true);
        addToDrawer(drawer);
    }

    private void logout() {
        try {
            VaadinServletRequest.getCurrent().getHttpServletRequest().logout();
        } catch (ServletException e) { }
        getUI().ifPresent(ui -> ui.getPage().setLocation("/"));
    }
}

package org.example.gym_application.vaadin;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.servlet.ServletException;

@AnonymousAllowed
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("GymApp");
        logo.getStyle()
            .set("font-size", "1.5rem")
            .set("margin", "0")
            .set("color", "#667eea");

        Button logoutBtn = new Button("Wyloguj", VaadinIcon.SIGN_OUT.create(), e -> logout());
        logoutBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        HorizontalLayout left = new HorizontalLayout(new DrawerToggle(), logo);
        left.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        left.expand(logo);

        HorizontalLayout header = new HorizontalLayout(left, logoutBtn);
        header.setWidthFull();
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.getStyle().set("padding", "10px 20px");
        header.expand(left);

        addToNavbar(header);
    }

    private void createDrawer() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "/dashboard", VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Członkowie", "/members", VaadinIcon.USERS.create()));
        nav.addItem(new SideNavItem("Trenerzy", "/trainers", VaadinIcon.USER.create()));
        nav.addItem(new SideNavItem("Zajęcia", "/classes", VaadinIcon.CALENDAR.create()));
        nav.addItem(new SideNavItem("Rezerwacje", "/reservations", VaadinIcon.CLIPBOARD_CHECK.create()));
        nav.addItem(new SideNavItem("Karnety", "/memberships", VaadinIcon.CREDIT_CARD.create()));

        addToDrawer(nav);
    }

    private void logout() {
        // Wyloguj przez Servlet request
        try {
            VaadinServletRequest.getCurrent().getHttpServletRequest().logout();
        } catch (ServletException e) {
            // ignoruj
        }
        // Przekieruj na stronę główną
        getUI().ifPresent(ui -> ui.getPage().setLocation("/"));
    }
}

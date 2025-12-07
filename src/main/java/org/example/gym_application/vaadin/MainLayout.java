package org.example.gym_application.vaadin;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("GymFlow");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "/", VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Członkowie", "/members", VaadinIcon.USERS.create()));
        nav.addItem(new SideNavItem("Trenerzy", "/trainers", VaadinIcon.USER.create()));
        nav.addItem(new SideNavItem("Zajęcia", "/classes", VaadinIcon.CALENDAR.create()));
        nav.addItem(new SideNavItem("Rezerwacje", "/reservations", VaadinIcon.CLIPBOARD_CHECK.create()));
        nav.addItem(new SideNavItem("Karnety", "/memberships", VaadinIcon.CREDIT_CARD.create()));

        addToDrawer(nav);
    }
}


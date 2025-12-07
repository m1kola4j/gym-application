package org.example.gym_application.vaadin;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * Minimalny widok startowy służący do potwierdzenia, że integracja Vaadin działa.
 */
/**
 * Stary widok startowy - zastąpiony przez DashboardView z MainLayout.
 * Można usunąć lub zostawić jako fallback.
 */
@Route("old")
@PageTitle("GymFlow - Stary widok")
@AnonymousAllowed
public class MainView extends VerticalLayout {

    public MainView() {
        setSizeFull();
        setSpacing(false);
        setPadding(true);

        add(new H1("GymFlow – panel startowy"));
        add(new Paragraph(
                "Ten widok został zastąpiony przez DashboardView. Przejdź do / aby zobaczyć nowy dashboard."));
    }
}




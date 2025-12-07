package org.example.gym_application.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * Minimalny widok startowy służący do potwierdzenia, że integracja Vaadin działa.
 */
@Route("")
@PageTitle("GymFlow")
@AnonymousAllowed
public class MainView extends VerticalLayout {

    public MainView() {
        setSizeFull();
        setSpacing(false);
        setPadding(true);

        add(new H1("GymFlow – panel startowy"));
        add(new Paragraph(
                "To tylko widok testowy. W kolejnych etapach dodamy nawigację i dashboard."));
        add(new Button("Przejdź do panelu", click -> click.getSource().setText("Niedługo dostępne")));
    }
}




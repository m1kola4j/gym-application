package org.example.gym_application.vaadin;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.example.gym_application.service.*;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | GymApp")
@AnonymousAllowed
public class DashboardView extends VerticalLayout {

    private final   MemberService memberService;
    private final TrainerService trainerService;
    private final ClassSessionService classSessionService;
    private final ReservationService reservationService;
    private final MembershipService membershipService;

    public DashboardView(MemberService memberService,
                        TrainerService trainerService,
                        ClassSessionService classSessionService,
                        ReservationService reservationService,
                        MembershipService membershipService) {
        this.memberService = memberService;
        this.trainerService = trainerService;
        this.classSessionService = classSessionService;
        this.reservationService = reservationService;
        this.membershipService = membershipService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H1("Dashboard"));
        add(createStatsSection());
    }

    private VerticalLayout createStatsSection() {
        VerticalLayout statsLayout = new VerticalLayout();
        statsLayout.setSpacing(true);
        statsLayout.setPadding(false);

        try {
            long membersCount = memberService.findAll().size();
            long trainersCount = trainerService.findAll().size();
            long classesCount = classSessionService.findAll().size();
            long reservationsCount = reservationService.findAll().size();
            long membershipsCount = membershipService.findAll().size();

            statsLayout.add(
                    createStatCard("Członkowie", String.valueOf(membersCount)),
                    createStatCard("Trenerzy", String.valueOf(trainersCount)),
                    createStatCard("Zajęcia", String.valueOf(classesCount)),
                    createStatCard("Rezerwacje", String.valueOf(reservationsCount)),
                    createStatCard("Karnety", String.valueOf(membershipsCount))
            );
        } catch (Exception e) {
            statsLayout.add(new Paragraph("Błąd podczas ładowania statystyk: " + e.getMessage()));
        }

        return statsLayout;
    }

    private VerticalLayout createStatCard(String title, String value) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames("border", "rounded", "p-m", "bg-contrast-5");
        card.setPadding(true);
        card.setSpacing(false);
        card.setWidth("200px");

        H2 valueH2 = new H2(value);
        valueH2.addClassNames("text-primary", "font-bold");
        Paragraph titleP = new Paragraph(title);
        titleP.addClassNames("text-secondary", "text-s");

        card.add(valueH2, titleP);
        return card;
    }
}


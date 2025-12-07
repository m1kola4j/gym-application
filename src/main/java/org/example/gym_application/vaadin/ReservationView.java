package org.example.gym_application.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.example.gym_application.domain.ReservationStatus;
import org.example.gym_application.dto.ClassSessionDTO;
import org.example.gym_application.dto.MemberDTO;
import org.example.gym_application.dto.ReservationDTO;
import org.example.gym_application.service.*;

@Route(value = "reservations", layout = MainLayout.class)
@PageTitle("Rezerwacje | GymFlow")
@AnonymousAllowed
public class ReservationView extends VerticalLayout {

    private final ReservationService reservationService;
    private final MemberService memberService;
    private final ClassSessionService classSessionService;
    private final Grid<ReservationDTO> grid = new Grid<>(ReservationDTO.class, false);
    private final Dialog quickReservationDialog = new Dialog();

    public ReservationView(ReservationService reservationService,
                          MemberService memberService,
                          ClassSessionService classSessionService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
        this.classSessionService = classSessionService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Zarządzanie rezerwacjami"));
        add(createToolbar());
        addAndExpand(createGrid());
        createQuickReservationDialog();
    }

    private HorizontalLayout createToolbar() {
        Button quickReserveButton = new Button("Szybka rezerwacja");
        quickReserveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        quickReserveButton.addClickListener(e -> quickReservationDialog.open());

        HorizontalLayout toolbar = new HorizontalLayout(quickReserveButton);
        toolbar.setSpacing(true);
        return toolbar;
    }

    private Grid<ReservationDTO> createGrid() {
        grid.addColumn(ReservationDTO::getId).setHeader("ID").setWidth("80px");
        grid.addColumn(ReservationDTO::getMemberName).setHeader("Członek");
        grid.addColumn(ReservationDTO::getClassSessionName).setHeader("Zajęcia");
        grid.addColumn(ReservationDTO::getStatus).setHeader("Status");
        grid.addColumn(ReservationDTO::getCreatedAt).setHeader("Data utworzenia");

        grid.addComponentColumn(reservation -> {
            if (reservation.getStatus() == ReservationStatus.RESERVED) {
                Button cancelButton = new Button("Anuluj");
                cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                cancelButton.addClickListener(e -> cancelReservation(reservation));
                return cancelButton;
            }
            return null;
        }).setHeader("Akcje");

        grid.setSizeFull();
        refreshGrid();
        return grid;
    }

    private void createQuickReservationDialog() {
        ComboBox<MemberDTO> memberComboBox = new ComboBox<>("Członek");
        memberComboBox.setItems(memberService.findAll());
        memberComboBox.setItemLabelGenerator(member -> member.getFirstName() + " " + member.getLastName());

        ComboBox<ClassSessionDTO> classComboBox = new ComboBox<>("Zajęcia");
        classComboBox.setItems(classSessionService.findAll());
        classComboBox.setItemLabelGenerator(ClassSessionDTO::getName);
        classComboBox.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                boolean available = classSessionService.isAvailable(e.getValue().getId());
                if (!available) {
                    Notification.show("Brak wolnych miejsc na te zajęcia", 3000, Notification.Position.TOP_CENTER);
                }
            }
        });

        Button reserveButton = new Button("Zarezerwuj");
        reserveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        reserveButton.addClickListener(e -> {
            if (memberComboBox.getValue() != null && classComboBox.getValue() != null) {
                try {
                    reservationService.create(memberComboBox.getValue().getId(), classComboBox.getValue().getId());
                    Notification.show("Rezerwacja została utworzona", 3000, Notification.Position.TOP_CENTER);
                    quickReservationDialog.close();
                    refreshGrid();
                    memberComboBox.clear();
                    classComboBox.clear();
                } catch (Exception ex) {
                    Notification.show("Błąd: " + ex.getMessage(), 5000, Notification.Position.TOP_CENTER);
                }
            } else {
                Notification.show("Wybierz członka i zajęcia", 3000, Notification.Position.TOP_CENTER);
            }
        });

        Button cancelButton = new Button("Anuluj");
        cancelButton.addClickListener(e -> quickReservationDialog.close());

        HorizontalLayout buttons = new HorizontalLayout(reserveButton, cancelButton);
        buttons.setSpacing(true);

        VerticalLayout dialogContent = new VerticalLayout(memberComboBox, classComboBox, buttons);
        dialogContent.setSpacing(true);
        quickReservationDialog.add(dialogContent);
        quickReservationDialog.setWidth("400px");
    }

    private void cancelReservation(ReservationDTO reservation) {
        try {
            // Anulowanie = całkowite usunięcie rezerwacji z bazy
            reservationService.delete(reservation.getId());
            Notification.show("Rezerwacja została anulowana i usunięta", 3000, Notification.Position.TOP_CENTER);
            refreshGrid();
        } catch (Exception e) {
            Notification.show("Błąd: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }

    private void refreshGrid() {
        grid.setItems(reservationService.findAll());
    }
}


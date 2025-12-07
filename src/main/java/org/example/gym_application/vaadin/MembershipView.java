package org.example.gym_application.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.example.gym_application.domain.MembershipType;
import org.example.gym_application.dto.MemberDTO;
import org.example.gym_application.dto.MembershipDTO;
import org.example.gym_application.service.MemberService;
import org.example.gym_application.service.MembershipService;

@Route(value = "memberships", layout = MainLayout.class)
@PageTitle("Karnety | GymFlow")
@AnonymousAllowed
public class MembershipView extends VerticalLayout {

    private final MembershipService membershipService;
    private final MemberService memberService;
    private final Grid<MembershipDTO> grid = new Grid<>(MembershipDTO.class, false);
    private final Binder<MembershipDTO> binder = new BeanValidationBinder<>(MembershipDTO.class);
    private final Dialog dialog = new Dialog();
    private MembershipDTO selectedMembership;
    private ComboBox<MemberDTO> memberComboBox;
    private DatePicker endDateField;
    private BigDecimalField priceField;

    public MembershipView(MembershipService membershipService, MemberService memberService) {
        this.membershipService = membershipService;
        this.memberService = memberService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Zarządzanie karnetami"));
        add(createToolbar());
        addAndExpand(createGrid());
        createDialog();
    }

    private HorizontalLayout createToolbar() {
        Button addButton = new Button("Dodaj karnet");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> openDialog(new MembershipDTO()));

        HorizontalLayout toolbar = new HorizontalLayout(addButton);
        toolbar.setSpacing(true);
        return toolbar;
    }

    private Grid<MembershipDTO> createGrid() {
        grid.addColumn(MembershipDTO::getId).setHeader("ID").setWidth("80px");
        grid.addColumn(MembershipDTO::getType).setHeader("Typ");
        grid.addColumn(MembershipDTO::getStartDate).setHeader("Data rozpoczęcia");
        grid.addColumn(MembershipDTO::getEndDate).setHeader("Data zakończenia");
        grid.addColumn(MembershipDTO::getPrice).setHeader("Cena");
        grid.addColumn(MembershipDTO::getMemberName).setHeader("Członek");

        grid.addComponentColumn(membership -> {
            Button editButton = new Button("Edytuj");
            editButton.addClickListener(e -> openDialog(membership));
            return editButton;
        }).setHeader("Akcje");

        grid.addComponentColumn(membership -> {
            Button deleteButton = new Button("Usuń");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteMembership(membership));
            return deleteButton;
        }).setHeader("");

        grid.setSizeFull();
        refreshGrid();
        return grid;
    }

    private void createDialog() {
        Select<MembershipType> typeSelect = new Select<>();
        typeSelect.setLabel("Typ karnetu");
        typeSelect.setItems(MembershipType.values());
        typeSelect.setItemLabelGenerator(type -> {
            return switch (type) {
                case MONTHLY -> "Miesięczny";
                case QUARTERLY -> "Kwartalny";
                case YEARLY -> "Roczny";
            };
        });

        DatePicker startDateField = new DatePicker("Data rozpoczęcia");
        endDateField = new DatePicker("Data zakończenia");
        endDateField.setReadOnly(true);
        endDateField.setHelperText("Obliczana automatycznie na podstawie typu karnetu");
        priceField = new BigDecimalField("Cena");
        priceField.setReadOnly(true);
        priceField.setHelperText("Ustawiana automatycznie na podstawie typu karnetu");
        memberComboBox = new ComboBox<>("Członek");
        memberComboBox.setItems(memberService.findAll());
        memberComboBox.setItemLabelGenerator(member -> member.getFirstName() + " " + member.getLastName());

        // Automatyczne obliczanie daty zakończenia i ceny
        typeSelect.addValueChangeListener(e -> {
            calculateEndDate(e.getValue(), startDateField.getValue(), endDateField);
            calculatePrice(e.getValue());
        });
        startDateField.addValueChangeListener(e -> calculateEndDate(typeSelect.getValue(), e.getValue(), endDateField));

        FormLayout formLayout = new FormLayout();
        formLayout.add(typeSelect, startDateField, endDateField, priceField, memberComboBox);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        binder.forField(typeSelect).asRequired("Typ karnetu jest wymagany").bind(MembershipDTO::getType, MembershipDTO::setType);
        binder.forField(startDateField).asRequired("Data rozpoczęcia jest wymagana")
                .bind(MembershipDTO::getStartDate, MembershipDTO::setStartDate);
        binder.forField(endDateField).asRequired("Data zakończenia jest wymagana")
                .bind(MembershipDTO::getEndDate, MembershipDTO::setEndDate);
        binder.forField(priceField).asRequired("Cena jest wymagana")
                .bind(MembershipDTO::getPrice, MembershipDTO::setPrice);
        binder.forField(memberComboBox).asRequired("Członek jest wymagany")
                .bind(dto -> memberService.findAll().stream()
                                .filter(m -> m.getId().equals(dto.getMemberId()))
                                .findFirst().orElse(null),
                        (dto, member) -> {
                            if (member != null) {
                                dto.setMemberId(member.getId());
                            }
                        });

        Button saveButton = new Button("Zapisz");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> saveMembership());

        Button cancelButton = new Button("Anuluj");
        cancelButton.addClickListener(e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setSpacing(true);

        VerticalLayout dialogContent = new VerticalLayout(formLayout, buttons);
        dialogContent.setSpacing(true);
        dialog.add(dialogContent);
        dialog.setWidth("500px");
    }

    private void openDialog(MembershipDTO membership) {
        selectedMembership = membership;
        binder.readBean(membership);
        // Jeśli edytujemy istniejący karnet, oblicz datę zakończenia i cenę
        if (membership.getType() != null) {
            if (membership.getStartDate() != null && endDateField != null) {
                calculateEndDate(membership.getType(), membership.getStartDate(), endDateField);
            }
            calculatePrice(membership.getType());
        }
        dialog.open();
    }

    private void saveMembership() {
        if (binder.writeBeanIfValid(selectedMembership)) {
            try {
                if (selectedMembership.getId() == null) {
                    membershipService.create(selectedMembership);
                    Notification.show("Karnet został dodany", 3000, Notification.Position.TOP_CENTER);
                } else {
                    membershipService.update(selectedMembership.getId(), selectedMembership);
                    Notification.show("Karnet został zaktualizowany", 3000, Notification.Position.TOP_CENTER);
                }
                dialog.close();
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Błąd: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }
        }
    }

    private void deleteMembership(MembershipDTO membership) {
        try {
            membershipService.delete(membership.getId());
            Notification.show("Karnet został usunięty", 3000, Notification.Position.TOP_CENTER);
            refreshGrid();
        } catch (Exception e) {
            Notification.show("Błąd: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }

    private void refreshGrid() {
        grid.setItems(membershipService.findAll());
    }

    private void calculateEndDate(MembershipType type, java.time.LocalDate startDate, DatePicker endDateField) {
        if (type != null && startDate != null) {
            java.time.LocalDate endDate = switch (type) {
                case MONTHLY -> startDate.plusMonths(1);
                case QUARTERLY -> startDate.plusMonths(3);
                case YEARLY -> startDate.plusYears(1);
            };
            endDateField.setValue(endDate);
            if (selectedMembership != null) {
                selectedMembership.setEndDate(endDate);
            }
        }
    }

    private void calculatePrice(MembershipType type) {
        if (type != null) {
            java.math.BigDecimal price = type.getPrice();
            priceField.setValue(price);
            if (selectedMembership != null) {
                selectedMembership.setPrice(price);
            }
        }
    }
}


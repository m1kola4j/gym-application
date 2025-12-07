package org.example.gym_application.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.example.gym_application.dto.ClassSessionDTO;
import org.example.gym_application.dto.TrainerDTO;
import org.example.gym_application.service.ClassSessionService;
import org.example.gym_application.service.TrainerService;

@Route(value = "classes", layout = MainLayout.class)
@PageTitle("Zajęcia | GymFlow")
@AnonymousAllowed
public class ClassSessionView extends VerticalLayout {

    private final ClassSessionService classSessionService;
    private final TrainerService trainerService;
    private final Grid<ClassSessionDTO> grid = new Grid<>(ClassSessionDTO.class, false);
    private final Binder<ClassSessionDTO> binder = new BeanValidationBinder<>(ClassSessionDTO.class);
    private final Dialog dialog = new Dialog();
    private ClassSessionDTO selectedClassSession;
    private ComboBox<TrainerDTO> trainerComboBox;

    public ClassSessionView(ClassSessionService classSessionService, TrainerService trainerService) {
        this.classSessionService = classSessionService;
        this.trainerService = trainerService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Zarządzanie zajęciami"));
        add(createToolbar());
        addAndExpand(createGrid());
        createDialog();
    }

    private HorizontalLayout createToolbar() {
        Button addButton = new Button("Dodaj zajęcia");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> openDialog(new ClassSessionDTO()));

        HorizontalLayout toolbar = new HorizontalLayout(addButton);
        toolbar.setSpacing(true);
        return toolbar;
    }

    private Grid<ClassSessionDTO> createGrid() {
        grid.addColumn(ClassSessionDTO::getId).setHeader("ID").setWidth("80px");
        grid.addColumn(ClassSessionDTO::getName).setHeader("Nazwa");
        grid.addColumn(ClassSessionDTO::getDescription).setHeader("Opis");
        grid.addColumn(ClassSessionDTO::getStartTime).setHeader("Data i godzina");
        grid.addColumn(ClassSessionDTO::getMaxParticipants).setHeader("Max uczestników");
        grid.addColumn(ClassSessionDTO::getCurrentReservations).setHeader("Zarezerwowane");
        grid.addColumn(ClassSessionDTO::getTrainerName).setHeader("Trener");

        grid.addComponentColumn(session -> {
            Button editButton = new Button("Edytuj");
            editButton.addClickListener(e -> openDialog(session));
            return editButton;
        }).setHeader("Akcje");

        grid.addComponentColumn(session -> {
            Button deleteButton = new Button("Usuń");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteClassSession(session));
            return deleteButton;
        }).setHeader("");

        grid.setSizeFull();
        refreshGrid();
        return grid;
    }

    private void createDialog() {
        TextField nameField = new TextField("Nazwa zajęć");
        TextArea descriptionField = new TextArea("Opis");
        DateTimePicker startTimeField = new DateTimePicker("Data i godzina rozpoczęcia");
        IntegerField maxParticipantsField = new IntegerField("Maksymalna liczba uczestników");
        trainerComboBox = new ComboBox<>("Trener");
        trainerComboBox.setItems(trainerService.findAll());
        trainerComboBox.setItemLabelGenerator(trainer -> trainer.getFirstName() + " " + trainer.getLastName());

        FormLayout formLayout = new FormLayout();
        formLayout.add(nameField, descriptionField, startTimeField, maxParticipantsField, trainerComboBox);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        binder.forField(nameField)
                .asRequired("Nazwa jest wymagana")
                .withValidator(value -> value == null || value.matches("^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ\\s\\-']+$"), 
                        "Nazwa zajęć może zawierać tylko litery, spacje, myślniki i apostrofy")
                .bind(ClassSessionDTO::getName, ClassSessionDTO::setName);
        binder.forField(descriptionField).bind(ClassSessionDTO::getDescription, ClassSessionDTO::setDescription);
        binder.forField(startTimeField).asRequired("Data i godzina są wymagane")
                .bind(ClassSessionDTO::getStartTime, ClassSessionDTO::setStartTime);
        binder.forField(maxParticipantsField).asRequired("Maksymalna liczba uczestników jest wymagana")
                .bind(ClassSessionDTO::getMaxParticipants, ClassSessionDTO::setMaxParticipants);
        binder.forField(trainerComboBox).asRequired("Trener jest wymagany")
                .bind(dto -> trainerService.findAll().stream()
                                .filter(t -> t.getId().equals(dto.getTrainerId()))
                                .findFirst().orElse(null),
                        (dto, trainer) -> {
                            if (trainer != null) {
                                dto.setTrainerId(trainer.getId());
                            }
                        });

        Button saveButton = new Button("Zapisz");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> saveClassSession());

        Button cancelButton = new Button("Anuluj");
        cancelButton.addClickListener(e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setSpacing(true);

        VerticalLayout dialogContent = new VerticalLayout(formLayout, buttons);
        dialogContent.setSpacing(true);
        dialog.add(dialogContent);
        dialog.setWidth("500px");
    }

    private void openDialog(ClassSessionDTO classSession) {
        selectedClassSession = classSession;
        binder.readBean(classSession);
        dialog.open();
    }

    private void saveClassSession() {
        if (binder.writeBeanIfValid(selectedClassSession)) {
            try {
                if (selectedClassSession.getId() == null) {
                    classSessionService.create(selectedClassSession);
                    Notification.show("Zajęcia zostały dodane", 3000, Notification.Position.TOP_CENTER);
                } else {
                    classSessionService.update(selectedClassSession.getId(), selectedClassSession);
                    Notification.show("Zajęcia zostały zaktualizowane", 3000, Notification.Position.TOP_CENTER);
                }
                dialog.close();
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Błąd: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }
        }
    }

    private void deleteClassSession(ClassSessionDTO classSession) {
        try {
            classSessionService.delete(classSession.getId());
            Notification.show("Zajęcia zostały usunięte", 3000, Notification.Position.TOP_CENTER);
            refreshGrid();
        } catch (Exception e) {
            Notification.show("Błąd: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }

    private void refreshGrid() {
        grid.setItems(classSessionService.findAll());
    }
}


package org.example.gym_application.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.example.gym_application.dto.TrainerDTO;
import org.example.gym_application.service.TrainerService;

@Route(value = "trainers", layout = MainLayout.class)
@PageTitle("Trenerzy | GymFlow")
@AnonymousAllowed
public class TrainerView extends VerticalLayout {

    private final TrainerService trainerService;
    private final Grid<TrainerDTO> grid = new Grid<>(TrainerDTO.class, false);
    private final Binder<TrainerDTO> binder = new BeanValidationBinder<>(TrainerDTO.class);
    private final Dialog dialog = new Dialog();
    private TrainerDTO selectedTrainer;

    public TrainerView(TrainerService trainerService) {
        this.trainerService = trainerService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Zarządzanie trenerami"));
        add(createToolbar());
        addAndExpand(createGrid());
        createDialog();
    }

    private HorizontalLayout createToolbar() {
        Button addButton = new Button("Dodaj trenera");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> openDialog(new TrainerDTO()));

        HorizontalLayout toolbar = new HorizontalLayout(addButton);
        toolbar.setSpacing(true);
        return toolbar;
    }

    private Grid<TrainerDTO> createGrid() {
        grid.addColumn(TrainerDTO::getId).setHeader("ID").setWidth("80px");
        grid.addColumn(TrainerDTO::getFirstName).setHeader("Imię");
        grid.addColumn(TrainerDTO::getLastName).setHeader("Nazwisko");
        grid.addColumn(TrainerDTO::getSpecialization).setHeader("Specjalizacja");
        grid.addColumn(TrainerDTO::getDescription).setHeader("Opis");

        grid.addComponentColumn(trainer -> {
            Button editButton = new Button("Edytuj");
            editButton.addClickListener(e -> openDialog(trainer));
            return editButton;
        }).setHeader("Akcje");

        grid.addComponentColumn(trainer -> {
            Button deleteButton = new Button("Usuń");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteTrainer(trainer));
            return deleteButton;
        }).setHeader("");

        grid.setSizeFull();
        refreshGrid();
        return grid;
    }

    private void createDialog() {
        TextField firstNameField = new TextField("Imię");
        TextField lastNameField = new TextField("Nazwisko");
        TextField specializationField = new TextField("Specjalizacja");
        TextArea descriptionField = new TextArea("Opis");

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstNameField, lastNameField, specializationField, descriptionField);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        binder.forField(firstNameField)
                .asRequired("Imię jest wymagane")
                .withValidator(value -> value == null || value.matches("^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ\\s\\-']+$"), 
                        "Imię może zawierać tylko litery, spacje, myślniki i apostrofy")
                .bind(TrainerDTO::getFirstName, TrainerDTO::setFirstName);
        binder.forField(lastNameField)
                .asRequired("Nazwisko jest wymagane")
                .withValidator(value -> value == null || value.matches("^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ\\s\\-']+$"), 
                        "Nazwisko może zawierać tylko litery, spacje, myślniki i apostrofy")
                .bind(TrainerDTO::getLastName, TrainerDTO::setLastName);
        binder.forField(specializationField)
                .withValidator(value -> value == null || value.trim().isEmpty() || value.matches("^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ\\s\\-']+$"), 
                        "Specjalizacja może zawierać tylko litery, spacje, myślniki i apostrofy")
                .bind(TrainerDTO::getSpecialization, TrainerDTO::setSpecialization);
        binder.forField(descriptionField).bind(TrainerDTO::getDescription, TrainerDTO::setDescription);

        Button saveButton = new Button("Zapisz");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> saveTrainer());

        Button cancelButton = new Button("Anuluj");
        cancelButton.addClickListener(e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setSpacing(true);

        VerticalLayout dialogContent = new VerticalLayout(formLayout, buttons);
        dialogContent.setSpacing(true);
        dialog.add(dialogContent);
        dialog.setWidth("500px");
    }

    private void openDialog(TrainerDTO trainer) {
        selectedTrainer = trainer;
        binder.readBean(trainer);
        dialog.open();
    }

    private void saveTrainer() {
        if (binder.writeBeanIfValid(selectedTrainer)) {
            try {
                if (selectedTrainer.getId() == null) {
                    trainerService.create(selectedTrainer);
                    Notification.show("Trener został dodany", 3000, Notification.Position.TOP_CENTER);
                } else {
                    trainerService.update(selectedTrainer.getId(), selectedTrainer);
                    Notification.show("Trener został zaktualizowany", 3000, Notification.Position.TOP_CENTER);
                }
                dialog.close();
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Błąd: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }
        }
    }

    private void deleteTrainer(TrainerDTO trainer) {
        try {
            trainerService.delete(trainer.getId());
            Notification.show("Trener został usunięty", 3000, Notification.Position.TOP_CENTER);
            refreshGrid();
        } catch (Exception e) {
            Notification.show("Błąd: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }

    private void refreshGrid() {
        grid.setItems(trainerService.findAll());
    }
}


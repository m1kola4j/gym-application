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
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.example.gym_application.dto.MemberDTO;
import org.example.gym_application.service.MemberService;

import java.time.LocalDate;

@Route(value = "members", layout = MainLayout.class)
@PageTitle("Członkowie | GymFlow")
@AnonymousAllowed
public class MemberView extends VerticalLayout {

    private final MemberService memberService;
    private final Grid<MemberDTO> grid = new Grid<>(MemberDTO.class, false);
    private final Binder<MemberDTO> binder = new BeanValidationBinder<>(MemberDTO.class);
    private final Dialog dialog = new Dialog();
    private MemberDTO selectedMember;

    public MemberView(MemberService memberService) {
        this.memberService = memberService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Zarządzanie członkami"));
        add(createToolbar());
        addAndExpand(createGrid());
        createDialog();
    }

    private HorizontalLayout createToolbar() {
        Button addButton = new Button("Dodaj członka");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> openDialog(new MemberDTO()));

        HorizontalLayout toolbar = new HorizontalLayout(addButton);
        toolbar.setSpacing(true);
        return toolbar;
    }

    private Grid<MemberDTO> createGrid() {
        grid.addColumn(MemberDTO::getId).setHeader("ID").setWidth("80px");
        grid.addColumn(MemberDTO::getFirstName).setHeader("Imię");
        grid.addColumn(MemberDTO::getLastName).setHeader("Nazwisko");
        grid.addColumn(MemberDTO::getEmail).setHeader("Email");
        grid.addColumn(MemberDTO::getPhoneNumber).setHeader("Telefon");
        grid.addColumn(MemberDTO::getJoinDate).setHeader("Data dołączenia");

        grid.addComponentColumn(member -> {
            Button editButton = new Button("Edytuj");
            editButton.addClickListener(e -> openDialog(member));
            return editButton;
        }).setHeader("Akcje");

        grid.addComponentColumn(member -> {
            Button deleteButton = new Button("Usuń");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteMember(member));
            return deleteButton;
        }).setHeader("");

        grid.setSizeFull();
        refreshGrid();
        return grid;
    }

    private void createDialog() {
        TextField firstNameField = new TextField("Imię");
        TextField lastNameField = new TextField("Nazwisko");
        EmailField emailField = new EmailField("Email");
        TextField phoneField = new TextField("Telefon");
        com.vaadin.flow.component.datepicker.DatePicker joinDateField = 
                new com.vaadin.flow.component.datepicker.DatePicker("Data dołączenia");

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstNameField, lastNameField, emailField, phoneField, joinDateField);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        binder.forField(firstNameField)
                .asRequired("Imię jest wymagane")
                .withValidator(value -> value == null || value.matches("^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ\\s\\-']+$"), 
                        "Imię może zawierać tylko litery, spacje, myślniki i apostrofy")
                .bind(MemberDTO::getFirstName, MemberDTO::setFirstName);
        binder.forField(lastNameField)
                .asRequired("Nazwisko jest wymagane")
                .withValidator(value -> value == null || value.matches("^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ\\s\\-']+$"), 
                        "Nazwisko może zawierać tylko litery, spacje, myślniki i apostrofy")
                .bind(MemberDTO::getLastName, MemberDTO::setLastName);
        binder.forField(emailField).asRequired("Email jest wymagany").bind(MemberDTO::getEmail, MemberDTO::setEmail);
        binder.forField(phoneField).bind(MemberDTO::getPhoneNumber, MemberDTO::setPhoneNumber);
        binder.forField(joinDateField).asRequired("Data dołączenia jest wymagana")
                .bind(dto -> dto.getJoinDate() != null ? dto.getJoinDate() : LocalDate.now(),
                        MemberDTO::setJoinDate);

        Button saveButton = new Button("Zapisz");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> saveMember());

        Button cancelButton = new Button("Anuluj");
        cancelButton.addClickListener(e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setSpacing(true);

        VerticalLayout dialogContent = new VerticalLayout(formLayout, buttons);
        dialogContent.setSpacing(true);
        dialog.add(dialogContent);
        dialog.setWidth("400px");
    }

    private void openDialog(MemberDTO member) {
        selectedMember = member;
        binder.readBean(member);
        dialog.open();
    }

    private void saveMember() {
        if (binder.writeBeanIfValid(selectedMember)) {
            try {
                if (selectedMember.getId() == null) {
                    memberService.create(selectedMember);
                    Notification.show("Członek został dodany", 3000, Notification.Position.TOP_CENTER);
                } else {
                    memberService.update(selectedMember.getId(), selectedMember);
                    Notification.show("Członek został zaktualizowany", 3000, Notification.Position.TOP_CENTER);
                }
                dialog.close();
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Błąd: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }
        }
    }

    private void deleteMember(MemberDTO member) {
        try {
            memberService.delete(member.getId());
            Notification.show("Członek został usunięty", 3000, Notification.Position.TOP_CENTER);
            refreshGrid();
        } catch (Exception e) {
            Notification.show("Błąd: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }

    private void refreshGrid() {
        grid.setItems(memberService.findAll());
    }
}


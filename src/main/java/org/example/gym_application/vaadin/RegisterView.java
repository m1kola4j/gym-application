package org.example.gym_application.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.example.gym_application.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import jakarta.servlet.http.HttpServletRequest;

@Route("register")
@PageTitle("Rejestracja | GymFlow")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final HttpServletRequest httpServletRequest;

    public RegisterView(UserService userService, 
                       AuthenticationManager authenticationManager,
                       HttpServletRequest httpServletRequest) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.httpServletRequest = httpServletRequest;

        setSizeFull();
        setPadding(true);
        setSpacing(true);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Tytuł
        H1 title = new H1("Zarejestruj się");
        title.addClassNames("text-primary", "text-2xl", "font-bold");

        // Formularz
        TextField firstNameField = new TextField("Imię");
        firstNameField.setRequired(true);
        firstNameField.setWidth("300px");

        TextField lastNameField = new TextField("Nazwisko");
        lastNameField.setRequired(true);
        lastNameField.setWidth("300px");

        EmailField emailField = new EmailField("Email");
        emailField.setRequired(true);
        emailField.setWidth("300px");

        TextField phoneField = new TextField("Telefon");
        phoneField.setWidth("300px");

        PasswordField passwordField = new PasswordField("Hasło");
        passwordField.setRequired(true);
        passwordField.setWidth("300px");

        PasswordField confirmPasswordField = new PasswordField("Potwierdź hasło");
        confirmPasswordField.setRequired(true);
        confirmPasswordField.setWidth("300px");

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstNameField, lastNameField, emailField, phoneField, passwordField, confirmPasswordField);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        // Przyciski
        Button registerButton = new Button("Zarejestruj się");
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.addClickListener(e -> {
            String firstName = firstNameField.getValue();
            String lastName = lastNameField.getValue();
            String email = emailField.getValue();
            String phone = phoneField.getValue();
            String password = passwordField.getValue();
            String confirmPassword = confirmPasswordField.getValue();

            // Walidacja
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Notification.show("Wypełnij wszystkie wymagane pola", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            if (!password.equals(confirmPassword)) {
                Notification.show("Hasła nie są identyczne", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            if (password.length() < 6) {
                Notification.show("Hasło musi mieć co najmniej 6 znaków", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            try {
                // Rejestracja użytkownika
                userService.registerMember(firstName, lastName, email, phone, password);

                // Automatyczne logowanie po rejestracji
                UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(email, password);
                authToken.setDetails(new WebAuthenticationDetails(httpServletRequest));
                
                Authentication authentication = authenticationManager.authenticate(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Przekierowanie do dashboardu członka
                getUI().ifPresent(ui -> ui.navigate("/member/dashboard"));
                Notification.show("Rejestracja zakończona pomyślnie!", 3000, Notification.Position.TOP_CENTER);
            } catch (IllegalArgumentException ex) {
                Notification.show("Błąd: " + ex.getMessage(), 5000, Notification.Position.TOP_CENTER);
            } catch (Exception ex) {
                Notification.show("Wystąpił błąd podczas rejestracji", 5000, Notification.Position.TOP_CENTER);
            }
        });

        Button backButton = new Button("Powrót do strony głównej");
        backButton.addClickListener(e -> backButton.getUI().ifPresent(ui -> ui.navigate("/")));

        Paragraph loginLink = new Paragraph("Masz już konto? ");
        com.vaadin.flow.component.html.Anchor loginAnchor = 
                new com.vaadin.flow.component.html.Anchor("/login", "Zaloguj się");
        loginLink.add(loginAnchor);

        VerticalLayout formContainer = new VerticalLayout();
        formContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        formContainer.setSpacing(true);
        formContainer.setPadding(false);
        formContainer.setWidth("400px");
        formContainer.add(formLayout, registerButton, backButton, loginLink);

        add(title, formContainer);
    }
}

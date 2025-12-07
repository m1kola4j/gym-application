package org.example.gym_application.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.servlet.http.HttpServletRequest;
import org.example.gym_application.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Route("register")
@PageTitle("Rejestracja | GymApp")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    public RegisterView(UserService userService, 
                        AuthenticationManager authenticationManager,
                        HttpServletRequest httpServletRequest) {

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle().set("background", "linear-gradient(135deg, #0f0c29 0%, #302b63 50%, #24243e 100%)");

        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setSizeFull();
        mainContainer.setAlignItems(Alignment.CENTER);
        mainContainer.setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout form = new VerticalLayout();
        form.setAlignItems(Alignment.CENTER);
        form.setWidth("500px");
        form.getStyle()
            .set("background", "rgba(255,255,255,0.95)")
            .set("border-radius", "20px")
            .set("padding", "50px")
            .set("box-shadow", "0 25px 80px rgba(0,0,0,0.5)");

        H2 title = new H2("Rejestracja");
        title.getStyle()
            .set("color", "#1a1a2e")
            .set("font-size", "2rem")
            .set("font-weight", "700")
            .set("margin", "0 0 10px 0");

        Paragraph subtitle = new Paragraph("Dołącz do nas!");
        subtitle.getStyle().set("color", "#666").set("margin", "0 0 30px 0");

        HorizontalLayout row1 = new HorizontalLayout();
        row1.setWidthFull();
        TextField firstNameField = new TextField("Imię");
        firstNameField.setWidthFull();
        firstNameField.setRequired(true);
        TextField lastNameField = new TextField("Nazwisko");
        lastNameField.setWidthFull();
        lastNameField.setRequired(true);
        row1.add(firstNameField, lastNameField);

        EmailField emailField = new EmailField("Email");
        emailField.setWidthFull();
        emailField.setRequired(true);

        TextField phoneField = new TextField("Telefon (opcjonalnie)");
        phoneField.setWidthFull();

        HorizontalLayout row2 = new HorizontalLayout();
        row2.setWidthFull();
        PasswordField passwordField = new PasswordField("Hasło");
        passwordField.setWidthFull();
        passwordField.setRequired(true);
        PasswordField confirmPasswordField = new PasswordField("Potwierdź hasło");
        confirmPasswordField.setWidthFull();
        confirmPasswordField.setRequired(true);
        row2.add(passwordField, confirmPasswordField);

        Button registerButton = new Button("Zarejestruj się", e -> {
            String firstName = firstNameField.getValue().trim();
            String lastName = lastNameField.getValue().trim();
            String email = emailField.getValue().trim();
            String phone = phoneField.getValue().trim();
            String password = passwordField.getValue();
            String confirmPassword = confirmPasswordField.getValue();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Notification.show("Wypełnij wszystkie wymagane pola", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            if (!password.equals(confirmPassword)) {
                Notification.show("Hasła nie są identyczne", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            if (password.length() < 6) {
                Notification.show("Hasło minimum 6 znaków", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                userService.registerMember(firstName, lastName, email, phone, password);
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
                authToken.setDetails(new WebAuthenticationDetails(httpServletRequest));
                Authentication authentication = authenticationManager.authenticate(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                Notification.show("Zarejestrowano!", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                
                getUI().ifPresent(ui -> ui.navigate("dashboard"));
                
            } catch (Exception ex) {
                Notification.show("Błąd: " + ex.getMessage(), 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        registerButton.setWidthFull();
        registerButton.setHeight("50px");
        registerButton.getStyle()
            .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
            .set("border-radius", "10px")
            .set("margin-top", "30px")
            .set("font-weight", "600");

        HorizontalLayout loginLink = new HorizontalLayout();
        loginLink.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        loginLink.setWidthFull();
        Paragraph text = new Paragraph("Masz konto? ");
        text.getStyle().set("margin", "0").set("color", "#666");
        Anchor anchor = new Anchor("login", "Zaloguj się");
        anchor.getStyle().set("color", "#667eea").set("font-weight", "600");
        loginLink.add(text, anchor);
        loginLink.getStyle().set("margin-top", "25px");

        Button backButton = new Button("← Powrót", e -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backButton.getStyle().set("color", "#888").set("margin-top", "15px");

        form.add(title, subtitle, row1, emailField, phoneField, row2, registerButton, loginLink, backButton);
        mainContainer.add(form);
        add(mainContainer);
    }
}

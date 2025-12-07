package org.example.gym_application.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Route("login")
@PageTitle("Logowanie | GymApp")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView(AuthenticationManager authenticationManager, HttpServletRequest httpServletRequest) {
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
        form.setWidth("420px");
        form.getStyle()
            .set("background", "rgba(255,255,255,0.95)")
            .set("border-radius", "20px")
            .set("padding", "50px")
            .set("box-shadow", "0 25px 80px rgba(0,0,0,0.5)");

        H2 title = new H2("Logowanie");
        title.getStyle()
            .set("color", "#1a1a2e")
            .set("font-size", "2rem")
            .set("font-weight", "700")
            .set("margin", "0 0 40px 0");

        TextField emailField = new TextField("Email");
        emailField.setWidthFull();

        PasswordField passwordField = new PasswordField("Hasło");
        passwordField.setWidthFull();

        Button loginButton = new Button("Zaloguj się", e -> {
            String email = emailField.getValue();
            String password = passwordField.getValue();
            
            if (email.isEmpty() || password.isEmpty()) {
                Notification.show("Wypełnij wszystkie pola", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            
            try {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
                authToken.setDetails(new WebAuthenticationDetails(httpServletRequest));
                Authentication authentication = authenticationManager.authenticate(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                Notification.show("Zalogowano!", 2000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                
                String redirectUrl = getRedirectUrlByRole(authentication);
                getUI().ifPresent(ui -> ui.navigate(redirectUrl));
                
            } catch (AuthenticationException ex) {
                Notification.show("Nieprawidłowy email lub hasło", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        loginButton.setWidthFull();
        loginButton.setHeight("50px");
        loginButton.getStyle()
            .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
            .set("border-radius", "10px")
            .set("margin-top", "30px")
            .set("font-weight", "600");

        HorizontalLayout registerLink = new HorizontalLayout();
        registerLink.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        registerLink.setWidthFull();
        Paragraph text = new Paragraph("Nie masz konta? ");
        text.getStyle().set("margin", "0").set("color", "#666");
        Anchor anchor = new Anchor("register", "Zarejestruj się");
        anchor.getStyle().set("color", "#667eea").set("font-weight", "600");
        registerLink.add(text, anchor);
        registerLink.getStyle().set("margin-top", "25px");

        Button backButton = new Button("← Powrót", e -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backButton.getStyle().set("color", "#888").set("margin-top", "15px");

        form.add(title, emailField, passwordField, loginButton, registerLink, backButton);
        mainContainer.add(form);
        add(mainContainer);
    }

    private String getRedirectUrlByRole(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                return "admin/dashboard";
            } else if (role.equals("ROLE_STAFF")) {
                return "staff/dashboard";
            } else if (role.equals("ROLE_MEMBER")) {
                return "member/dashboard";
            }
        }
        return "member/dashboard";
    }
}

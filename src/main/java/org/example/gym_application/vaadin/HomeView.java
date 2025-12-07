package org.example.gym_application.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("")
@PageTitle("GymApp - Twoja siłownia")
@AnonymousAllowed
public class HomeView extends VerticalLayout {

    public HomeView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle()
            .set("background", "linear-gradient(135deg, #0f0c29 0%, #302b63 50%, #24243e 100%)")
            .set("min-height", "100vh");

        VerticalLayout hero = new VerticalLayout();
        hero.setSizeFull();
        hero.setAlignItems(Alignment.CENTER);
        hero.setJustifyContentMode(JustifyContentMode.CENTER);
        hero.setPadding(true);
        hero.getStyle().set("flex", "1");

        H1 title = new H1("GymApp");
        title.getStyle()
            .set("color", "white")
            .set("font-size", "5rem")
            .set("font-weight", "900")
            .set("margin", "0")
            .set("letter-spacing", "-3px")
            .set("text-shadow", "0 4px 20px rgba(0,0,0,0.3)");

        H2 subtitle = new H2("System zarządzania siłownią");
        subtitle.getStyle()
            .set("color", "rgba(255,255,255,0.8)")
            .set("font-weight", "300")
            .set("font-size", "1.8rem")
            .set("margin", "15px 0 50px 0");

        Paragraph description = new Paragraph(
            "Witaj w naszej siłowni! Zarządzaj swoim karnetem, rezerwuj zajęcia grupowe i śledź swoje postępy treningowe."
        );
        description.getStyle()
            .set("color", "rgba(255,255,255,0.7)")
            .set("text-align", "center")
            .set("font-size", "1.3rem")
            .set("line-height", "1.8")
            .set("max-width", "700px")
            .set("margin-bottom", "60px");

        Button loginButton = new Button("Zaloguj się", e -> getUI().ifPresent(ui -> ui.navigate("login")));
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        loginButton.setWidth("240px");
        loginButton.setHeight("60px");
        loginButton.getStyle()
            .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
            .set("color", "white")
            .set("font-size", "1.2rem")
            .set("font-weight", "600")
            .set("border-radius", "50px")
            .set("border", "none")
            .set("box-shadow", "0 10px 40px rgba(102, 126, 234, 0.4)");

        Button registerButton = new Button("Zarejestruj się", e -> getUI().ifPresent(ui -> ui.navigate("register")));
        registerButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        registerButton.setWidth("240px");
        registerButton.setHeight("60px");
        registerButton.getStyle()
            .set("background", "transparent")
            .set("color", "white")
            .set("border", "2px solid rgba(255,255,255,0.5)")
            .set("font-size", "1.2rem")
            .set("font-weight", "600")
            .set("border-radius", "50px");

        HorizontalLayout buttons = new HorizontalLayout(loginButton, registerButton);
        buttons.setSpacing(true);
        buttons.getStyle().set("gap", "30px");

        hero.add(title, subtitle, description, buttons);

        HorizontalLayout features = new HorizontalLayout();
        features.setWidthFull();
        features.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        features.setPadding(true);
        features.getStyle()
            .set("gap", "80px")
            .set("padding", "60px")
            .set("background", "rgba(0,0,0,0.2)");

        features.add(
            createFeature("Rezerwacje", "Rezerwuj zajęcia online"),
            createFeature("Karnety", "Zarządzaj karnetami"),
            createFeature("Trenerzy", "Profesjonalna kadra"),
            createFeature("Statystyki", "Śledź postępy")
        );

        Paragraph footer = new Paragraph("© 2024 GymApp");
        footer.getStyle()
            .set("color", "rgba(255,255,255,0.4)")
            .set("font-size", "0.9rem")
            .set("text-align", "center")
            .set("padding", "30px")
            .set("margin", "0");

        add(hero, features, footer);
    }

    private VerticalLayout createFeature(String title, String desc) {
        VerticalLayout item = new VerticalLayout();
        item.setAlignItems(Alignment.CENTER);
        item.setSpacing(false);
        item.setPadding(false);
        item.setWidth("180px");

        H3 titleH3 = new H3(title);
        titleH3.getStyle()
            .set("color", "white")
            .set("font-weight", "700")
            .set("font-size", "1.2rem")
            .set("margin", "0 0 8px 0");

        Paragraph descP = new Paragraph(desc);
        descP.getStyle()
            .set("color", "rgba(255,255,255,0.6)")
            .set("font-size", "0.95rem")
            .set("text-align", "center")
            .set("margin", "0");

        item.add(titleH3, descP);
        return item;
    }
}

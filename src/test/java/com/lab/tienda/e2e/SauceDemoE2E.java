package com.lab.tienda.e2e;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SauceDemoE2E {
    static final String URL = "https://www.saucedemo.com";
    static Playwright playwright;
    static Browser browser;
    BrowserContext contexto;
    Page page;

    @BeforeAll
    static void iniciar() {
        playwright = Playwright.create();
        // setHeadless(false) abre el navegador visualmente
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
    }

    @AfterAll
    static void cerrar() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void nuevaPagina() {
        contexto = browser.newContext();
        page = contexto.newPage();
    }

    @AfterEach
    void cerrarContexto() {
        contexto.close();
    }

    private void login(String usuario, String clave) {
        page.navigate(URL);
        page.fill("#user-name", usuario);
        page.fill("#password", clave);
        page.click("#login-button");
    }

    @Test
    void loginExitoso() {
        login("standard_user", "secret_sauce");
        assertThat(page).hasURL(URL + "/inventory.html");
    }

    @Test
    void loginConClaveIncorrecta() {
        login("standard_user", "incorrecta");
        assertThat(page.locator("[data-test=error]")).containsText("do not match");
    }

    @Test
    void agregaProductoAlCarrito() {
        login("standard_user", "secret_sauce");
        page.click("#add-to-cart-sauce-labs-backpack");
        assertThat(page.locator(".shopping_cart_badge")).hasText("1");
    }
}
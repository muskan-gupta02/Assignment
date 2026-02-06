package com.assignment;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.*;

import java.util.regex.Pattern;

public class CaseKaroTest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    static void closeBrowser() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @Test
    void caseKaroAssignmentTest() {
        page.navigate("https://casekaro.com/pages/mobile-covers?_pos=8&_sid=a6c37e43b&_ss=r");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).fill("Apple");
        Locator appleBrand = page.locator("text=Apple");

        Page iPhonePage = context.waitForPage(() -> appleBrand.click());
        iPhonePage.waitForLoadState(LoadState.DOMCONTENTLOADED);

        iPhonePage.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).fill("iPhone 16 Pro");
        iPhonePage.waitForTimeout(2000); 

        iPhonePage.locator("text=iPhone 16 Pro").first().click();

        System.err.println(">>> [DEBUG] Waiting 4 seconds for grid...");
        iPhonePage.waitForTimeout(4000);
        
        // Locators scoped to the first product
        Locator firstProduct = iPhonePage.locator("#product-grid li.grid__item").first();
        Locator chooseOptionsBtn = firstProduct.locator("button[name='add']");
        Locator closeCartBtn = iPhonePage.locator("button.drawer__close[aria-label='Close']");

        // --- 1. HARD ---
        addVariant(iPhonePage, chooseOptionsBtn, "Hard", closeCartBtn);

        // --- 2. SOFT ---
        addVariant(iPhonePage, chooseOptionsBtn, "Soft", closeCartBtn);

        // --- 3. GLASS (Final) ---
        System.err.println(">>> [DEBUG] Adding Material: Glass...");
        chooseOptionsBtn.dispatchEvent("click");
        
        Locator modal = iPhonePage.locator("quick-add-modal:visible").first();
        modal.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        
        // Exact match regex to ensure clean selection
        modal.locator("label").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^\\s*Glass\\s*$", Pattern.MULTILINE))).click();
        modal.locator("button[name='add']").click();
        
        iPhonePage.waitForTimeout(2000);

        // --- PRINT FINAL CART ---
        printCartDetails(iPhonePage);

        iPhonePage.waitForTimeout(4000);
    }

    private void addVariant(Page page, Locator openBtn, String materialName, Locator closeBtn) {
        System.err.println(">>> [DEBUG] Adding Material: " + materialName + "...");
        openBtn.dispatchEvent("click");
        
        
        Locator modal = page.locator("quick-add-modal:visible").first();
        modal.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        
        // Regex ensures "Soft" doesn't accidentally hit "Black Soft"
        modal.locator("label").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^\\s*" + materialName + "\\s*$", Pattern.MULTILINE))).click();
        
        modal.locator("button[name='add']").click();
        page.waitForTimeout(2000);
        closeBtn.click();
        
        // Wait for cart overlay to clear
        page.locator(".drawer").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        page.waitForTimeout(1000);
    }

    private void printCartDetails(Page page) {
        System.err.println("\n>>> [DEBUG] Final Cart Summary:");
        page.locator("cart-drawer-items").waitFor();
        Locator cartRows = page.locator("tr.cart-item");
        
        System.err.println("===============================================================");
        for (int i = 0; i < cartRows.count(); i++) {
            Locator row = cartRows.nth(i);
            String mat = row.locator("dd").innerText().trim();
            String prc = row.locator(".price").first().innerText().trim();
            String url = "https://casekaro.com" + row.locator("a.cart-item__name").getAttribute("href");

            System.err.println("ITEM #" + (i + 1));
            System.err.println("   Material : " + mat);
            System.err.println("   Price    : " + prc);
            System.err.println("   Link     : " + url);
            System.err.println("---------------------------------------------------------------");
        }
        System.err.println("===============================================================");
    }
}
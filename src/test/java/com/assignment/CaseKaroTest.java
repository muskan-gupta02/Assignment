package com.assignment;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.*;
import java.util.List;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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

        Page iPhoneBackCoverPage = context.waitForPage(() -> appleBrand.click());
        iPhoneBackCoverPage.waitForLoadState(LoadState.DOMCONTENTLOADED);

        iPhoneBackCoverPage.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).fill("iPhone 16 Pro");
        iPhoneBackCoverPage.waitForTimeout(2000); 

        iPhoneBackCoverPage.locator("text=iPhone 16 Pro").first().click();

        System.err.println(">>> [DEBUG] Waiting 4 seconds for grid...");
        iPhoneBackCoverPage.waitForTimeout(4000);
        
        Locator chooseOptionsBtn = iPhoneBackCoverPage.locator("#product-grid li.grid__item").first().locator("button[name='add']");
        Locator closeCartBtn = iPhoneBackCoverPage.locator("button.drawer__close[aria-label='Close']");
        Locator addToCartBtn = iPhoneBackCoverPage.locator("quick-add-modal button[name='add']");

        // --- 1.HARD ---
        chooseOptionsBtn.dispatchEvent("click");
        iPhoneBackCoverPage.waitForTimeout(2000);
        iPhoneBackCoverPage.locator("quick-add-modal label:has-text('Hard')").first().click();
        addToCartBtn.click();
        iPhoneBackCoverPage.waitForTimeout(2000);
        closeCartBtn.click();
        iPhoneBackCoverPage.waitForTimeout(2000);

        // --- 2. SOFT ---
        chooseOptionsBtn.dispatchEvent("click");
        iPhoneBackCoverPage.waitForTimeout(2000);
        iPhoneBackCoverPage.locator("quick-add-modal label:has-text('Soft')").first().click();
        addToCartBtn.click();
        iPhoneBackCoverPage.waitForTimeout(2000);
        closeCartBtn.click();
        iPhoneBackCoverPage.waitForTimeout(2000);

        // --- 3. GLASS ---
        chooseOptionsBtn.dispatchEvent("click");
        iPhoneBackCoverPage.waitForTimeout(2000);
        iPhoneBackCoverPage.locator("quick-add-modal label:has-text('Glass')").first().click();
        addToCartBtn.click();
        iPhoneBackCoverPage.waitForTimeout(2000);

        // --- PRINT FINAL ITEMS ---
        System.err.println("\n>>> [DEBUG] Printing Cart Summary...");
        Locator cartItems = iPhoneBackCoverPage.locator(".cart-item__name");
        List<String> productNames = cartItems.allTextContents();

        System.err.println("===========================================");
        System.err.println("FINAL CART INVENTORY (" + productNames.size() + " items)");
        System.err.println("===========================================");
        productNames.forEach(name -> System.err.println("- " + name.trim()));
        System.err.println("===========================================");

        iPhoneBackCoverPage.waitForTimeout(4000);
    }
}
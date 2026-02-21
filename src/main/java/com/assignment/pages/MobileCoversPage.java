package com.assignment.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import java.util.regex.Pattern;

public class MobileCoversPage {
    private final Page page;

    public MobileCoversPage(Page page) {
        this.page = page;
    }

    public void navigate() {
        page.navigate("https://casekaro.com/pages/mobile-covers?_pos=8&_sid=a6c37e43b&_ss=r");
        page.waitForLoadState();
    }

    public void searchForProduct(String productName) {
        Locator search = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
        search.waitFor();
        search.fill(productName);
        page.keyboard().press("Enter");
        page.waitForLoadState();
    }

    public void clickAppleBrandButton() {
        // Using strict text matching to find the brand button
        Locator appleBtn = page.locator("button.brand-name-container:has-text('Apple')");
        appleBtn.waitFor();
        appleBtn.click();
    }

    public void selectIphoneModel(String modelName) {
        // Requested style: Use getByRole for search bar and text= for click
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).fill(modelName);
        page.waitForTimeout(2000); 

        // .first() ensures we hit 'Pro' even if 'Pro Max' is listed
        Locator model = page.locator("text=" + modelName).first();
        model.waitFor();
        model.click();
        page.waitForLoadState();
    }

    public boolean isBrandVisible(String brandName) {
        return page.locator("button.brand-name-container:has-text('" + brandName + "')").isVisible();
    }
}
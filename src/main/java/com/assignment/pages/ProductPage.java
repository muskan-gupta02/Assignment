package com.assignment.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import java.util.regex.Pattern;

public class ProductPage {
    private final Page page;
    private final Locator firstProduct;
    private final Locator quickAddModal;

    public ProductPage(Page page) {
        this.page = page;
        this.firstProduct = page.locator("#product-grid li.grid__item").first();
        this.quickAddModal = page.locator("quick-add-modal:visible").first();
    }

    public void openQuickAdd() {
        firstProduct.locator("button[name='add']").waitFor();
        firstProduct.locator("button[name='add']").dispatchEvent("click");
        quickAddModal.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void selectMaterialAndAdd(String materialName) {
        // Regex handles variant selection precisely
        quickAddModal.locator("label")
            .filter(new Locator.FilterOptions().setHasText(Pattern.compile("^\\s*" + materialName + "\\s*$", Pattern.MULTILINE)))
            .click();
        
        quickAddModal.locator("button[name='add']").click();
        page.waitForTimeout(1000); 
    }
}
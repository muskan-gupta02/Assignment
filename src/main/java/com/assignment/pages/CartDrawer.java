package com.assignment.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import java.util.ArrayList;
import java.util.List;

public class CartDrawer {
    private final Page page;
    private final Locator cartRows;

    public CartDrawer(Page page) {
        this.page = page;
        this.cartRows = page.locator("tr.cart-item");
    }

    public void close() {
        page.locator("button.drawer__close").first().click();
        page.locator(".drawer").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public int getItemCount() {
        page.locator("cart-drawer-items").waitFor();
        return cartRows.count();
    }

    public List<String> getAddedMaterials() {
        List<String> materials = new ArrayList<>();
        for (int i = 0; i < cartRows.count(); i++) {
            materials.add(cartRows.nth(i).locator("dd").innerText().trim());
        }
        return materials;
    }

    public void printCartDetails() {
        System.err.println("\n>>> [DEBUG] Final Cart Summary:");
        page.locator("cart-drawer-items").waitFor();
        
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
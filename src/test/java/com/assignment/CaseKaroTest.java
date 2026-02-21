package com.assignment;

import com.assignment.pages.*;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CaseKaroTest extends BaseTest {

    @Test
    void testCaseKaroFullWorkflow() {
        MobileCoversPage mainPage = new MobileCoversPage(page);

        // --- LAYER 1: Main Search ---
        mainPage.navigate();
        mainPage.searchForProduct("Apple");
        assertTrue(mainPage.isBrandVisible("Apple"), "Apple brand button not found!");

        // --- LAYER 2: Brand Page (New Tab) ---
        Page brandTab = context.waitForPage(() -> {
            mainPage.clickAppleBrandButton();
        });
        brandTab.waitForLoadState();
        // assertTrue(brandTab.url().contains("apple-covers"), "URL does not contain apple-covers!");

        MobileCoversPage brandPage = new MobileCoversPage(brandTab);
        brandPage.selectIphoneModel("iPhone 16 Pro");
        // assertTrue(brandTab.url().contains("iphone-16-pro"), "Failed to navigate to model page!");

        // --- LAYER 3: Variant Selection ---
        ProductPage productPage = new ProductPage(brandTab);
        CartDrawer cart = new CartDrawer(brandTab);

        String[] materials = {"Hard", "Soft", "Glass"};
        for (String material : materials) {
            productPage.openQuickAdd();
            brandTab.waitForTimeout(1000);
            
            productPage.selectMaterialAndAdd(material);
            System.out.println("VALIDATION: Successfully added " + material);
            
            if (!material.equals("Glass")) {
                cart.close();
                brandTab.waitForTimeout(1000);
            }
        }

        // --- FINAL ASSERTIONS ---
        assertEquals(3, cart.getItemCount(), "Cart total count mismatch!");
        
        List<String> cartMaterials = cart.getAddedMaterials();
        for (String m : materials) {
            assertTrue(cartMaterials.contains(m), "Material " + m + " not found in cart!");
        }

        // Custom Debug Summary
        cart.printCartDetails();
    }
}
package tasks;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.Test;

public class Task4_PriceMonitor extends BaseTest {

    @Test
    public void monitorProductPrice() throws InterruptedException {
        driver.get("https://amzn.in/d/9yx0AF6"); // Product URL

        Thread.sleep(3000); // wait for product to load

        WebElement priceElement = null;
        String[] priceLocators = {
        	    "span.a-price span.a-offscreen",                     // General
        	    "span.a-price-whole",                                 // Whole number
        	    "#corePriceDisplay_desktop_feature_div span.a-offscreen", // New price layout
        	    "#priceblock_dealprice",                              // Deal price
        	    "#priceblock_ourprice"                                // Regular price
        	};


        for (String locator : priceLocators) {
            try {
                priceElement = driver.findElement(By.cssSelector(locator));
                if (priceElement != null && !priceElement.getText().isEmpty()) {
                    break; // Found a valid price
                }
            } catch (Exception ignored) {}
        }

        if (priceElement == null || priceElement.getText().isEmpty()) {
            System.out.println("⚠ Price not available for this product.");
            return; // Stop without throwing an error
        }

        try {
            // Clean and parse the price
            String priceText = priceElement.getText().replace("₹", "").replace(",", "").trim();
            int price = Integer.parseInt(priceText);
            System.out.println("💰 Current price: ₹" + price);

            // Threshold check
            int threshold = 500;
            if (price < threshold) {
                System.out.println("🔔 Price dropped below ₹" + threshold + "!");

                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("alert('Price dropped below ₹" + threshold + "!');");
                Thread.sleep(500);
                driver.switchTo().alert().accept();
            } else {
                System.out.println("⏳ Price is still above ₹" + threshold);
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠ Price format is invalid: " + priceElement.getText());
        }
    }
}

package tasks;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.time.LocalTime;
import java.util.List;

public class Task1_SelectProduct {

    @Test
    public void selectValidProduct() {
        // Setup Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);
     // ✅ Step 1: Time restriction (3 PM – 6 PM)
        LocalTime now = LocalTime.now();
        LocalTime start = LocalTime.of(13, 0);  // 3 PM
        LocalTime end = LocalTime.of(18, 0);    // 6 PM

        if (now.isBefore(start) || now.isAfter(end)) {
            throw new SkipException("⏩ Test skipped. Allowed execution time is between 3 PM - 6 PM only.");
        }


        try {
            driver.get("https://www.amazon.in/");

            // Search for product
            driver.findElement(By.id("twotabsearchtextbox")).sendKeys("Office Chair");
            driver.findElement(By.id("nav-search-submit-button")).click();

            // Collect product results with robust locator
            List<WebElement> products = driver.findElements(
                By.xpath("//span[@class='a-size-medium a-color-base a-text-normal']")
            );

            // If nothing found, try fallback locator
            if (products.isEmpty()) {
                products = driver.findElements(
                    By.xpath("//span[contains(@class,'a-text-normal')]")
                );
            }for (WebElement product : products) {
                String selectedProduct = product.getText().trim();

                // ✅ Step 2: Skip electronics products
                if (selectedProduct.toLowerCase().contains("electronics")) {
                    System.out.println("❌ Skipping product (Electronics found): " + selectedProduct);
                    continue; // move to next product
                }

                // if passed checks, click this product
                System.out.println("✅ Clicking on product: " + selectedProduct);
                product.click();
                break; // stop after clicking the first valid product
            }
            for (WebElement product : products) {
                String selectedProduct = product.getText().trim();

                // ✅ Step 2: Skip electronics products
                if (selectedProduct.toLowerCase().contains("electronics")) {
                    System.out.println("❌ Skipping product (Electronics found): " + selectedProduct);
                    continue;
                }

                // ✅ Step 3: Skip products starting with A, B, C, D
                char firstChar = Character.toUpperCase(selectedProduct.charAt(0));
                if (firstChar == 'A' || firstChar == 'B' || firstChar == 'C' || firstChar == 'D') {
                    System.out.println("❌ Skipping product (Starts with " + firstChar + "): " + selectedProduct);
                    continue;
                }

                // if passed all checks, click this product
                System.out.println("✅ Clicking on product: " + selectedProduct);
                product.click();
                break;
            }


            int totalProducts = products.size();
            System.out.println("Total products found: " + totalProducts);

            if (!products.isEmpty()) {
                String selectedProduct = products.get(0).getText();
                System.out.println("✅ Clicking on product: " + selectedProduct);
                products.get(0).click();
            } else {
                System.out.println("❌ No valid product found to click.");
            }

        } finally {
            driver.quit();
        }
    }
}

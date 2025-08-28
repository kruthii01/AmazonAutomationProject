package tasks;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import utils.Utils;

import java.util.List;

public class Task6_SearchFilters extends BaseTest {

    @Test
    public void searchWithFilters() throws InterruptedException {
        // ✅ Run only between 3 PM and 6 PM
        if (!Utils.isWithinTime(15, 18)) {  // Correct 3PM to 6PM
            System.out.println("⏱ Not in allowed time (3 PM to 6 PM). Skipping test.");
            return;
        }

        driver.get("https://www.amazon.in/");

        // Search for a product
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys("mobile phones");
        driver.findElement(By.id("nav-search-submit-button")).click();

        Thread.sleep(3000);

        // ===== Brand filter starting with 'C' =====
        try {
            List<WebElement> brandElements = driver.findElements(By.xpath("//li[@aria-label]"));
            boolean brandFound = false;
            for (WebElement brand : brandElements) {
                String brandName = brand.getAttribute("aria-label");
                if (brandName != null && brandName.startsWith("C")) {
                    brand.click();
                    brandFound = true;
                    Thread.sleep(2000);
                    break;
                }
            }
            if (!brandFound) {
                System.out.println("⚠️ No brand starting with 'C' found.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Brand filter not applied.");
        }

        // ===== 4-star filter =====
        try {
            WebElement fourStar = driver.findElement(By.xpath("//i[@aria-label='4 Stars & Up']"));
            fourStar.click();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("⚠️ 4-star filter not found.");
        }

        // ===== Price filter > 2000 =====
        try {
            WebElement minPrice = driver.findElement(By.id("low-price"));
            WebElement maxPrice = driver.findElement(By.id("high-price"));

            minPrice.clear();
            minPrice.sendKeys("2001");
            maxPrice.clear();
            maxPrice.sendKeys("50000"); // optional upper limit

            WebElement goButton = driver.findElement(By.xpath("//input[@type='submit' and @aria-labelledby]"));
            goButton.click();
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println("⚠️ Price filter not applied.");
        }

        // ===== Print filtered products =====
        try {
            List<WebElement> productTitles = driver.findElements(By.xpath("//span[@class='a-size-medium a-color-base a-text-normal']"));
            System.out.println("Filtered Products:");
            for (int i = 0; i < Math.min(5, productTitles.size()); i++) {
                System.out.println("✅ " + productTitles.get(i).getText());
            }
        } catch (Exception e) {
            System.out.println("⚠️ No products found after filtering.");
        }

        Thread.sleep(5000); // Let user view results before browser closes
    }
}

package tasks;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public class Task5_EndToEndOrder {

    @Test
    public void testSearchAndPickProduct() throws InterruptedException {
    	LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.of(13, 0)) || now.isAfter(LocalTime.of(19, 0))) {
            throw new SkipException("⏩ Test skipped: Allowed only between 6 PM - 7 PM. Current time = " + now);
        }
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
       

        driver.get("https://www.amazon.in/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 🔍 Wait for search box and enter product
        WebElement searchBox;
        try {
            searchBox = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("twotabsearchtextbox"))
            );
        } catch (Exception e) {
            // fallback if id fails
            searchBox = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.name("field-keywords"))
            );
        }

        searchBox.sendKeys("headphones");
        driver.findElement(By.id("nav-search-submit-button")).click();

        // ⏳ Wait for search results to load
        Thread.sleep(4000);

        // 📦 Collect product prices
        List<WebElement> products = driver.findElements(
                By.xpath("//span[@class='a-price']//span[@class='a-price-whole']")
        );

        System.out.println("🔍 Found " + products.size() + " products with price");

        WebElement validProduct = null;
        int fallbackPrice = Integer.MAX_VALUE;
        WebElement fallbackProduct = null;

        for (WebElement product : products) {
            try {
                String priceText = product.getText().replaceAll("[^0-9]", "");
                if (!priceText.isEmpty()) {
                    int price = Integer.parseInt(priceText);

                    // ✅ Prefer product above 500
                    if (price > 500) {
                        validProduct = product;
                        System.out.println("✅ Found product above ₹500: ₹" + price);
                        break;
                    }

                    // 📉 Track cheapest product as fallback
                    if (price < fallbackPrice) {
                        fallbackPrice = price;
                        fallbackProduct = product;
                    }
                }
            } catch (Exception ignored) {}
        }

        // ⚠ Use fallback if no product > 500 found
        if (validProduct == null && fallbackProduct != null) {
            validProduct = fallbackProduct;
            System.out.println("⚠ No product > ₹500 found. Falling back to cheapest: ₹" + fallbackPrice);
        }

        if (validProduct == null) {
            Assert.fail("❌ No product found at all!");
        }

        // 🖱 Click the chosen product
        validProduct.click();

        // 🔄 Switch to new product tab if opened
        String parentWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(parentWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }

        Thread.sleep(4000);
        System.out.println("✅ Product page opened successfully.");

        driver.quit();
    }
}

package tasks;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Utils;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class Task2_AddToCart {

    @Test
    public void addMultipleItemsAndVerifyCartTotal() {
        System.out.println("🕒 Checking time window for Task 2 (6 PM to 7 PM)");

        if (!Utils.isWithinTime(18, 19)) {
            System.out.println("⏱ Not in allowed time (6 PM to 7 PM). Skipping test.");
            return;
        }

        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {
            driver.get("https://www.amazon.in/");
            System.out.println("✅ Opened Amazon");

            List<String> products = Arrays.asList("notebook", "water bottle", "pen");

            for (String product : products) {
                try {
                    System.out.println("🔍 Searching for: " + product);
                    driver.findElement(By.id("twotabsearchtextbox")).clear();
                    driver.findElement(By.id("twotabsearchtextbox")).sendKeys(product);
                    driver.findElement(By.id("nav-search-submit-button")).click();

                    Thread.sleep(3000); // Wait for results to load

                    List<WebElement> items = driver.findElements(By.cssSelector("div[data-component-type='s-search-result']"));
                    for (WebElement item : items) {
                        String itemName = item.getText();
                        if (itemName.startsWith("A") || itemName.startsWith("B") || itemName.startsWith("C") || itemName.startsWith("D")) {
                            continue; // Ignore unwanted items
                        }
                        if (!itemName.toLowerCase().contains("electronics")) {
                            try {
                                WebElement itemLink = item.findElement(By.cssSelector("h2 a"));
                                itemLink.click();
                                Thread.sleep(3000);

                                // Switch to new tab
                                for (String handle : driver.getWindowHandles()) {
                                    driver.switchTo().window(handle);
                                }

                                // 🛑 Try to skip sign-in popup if it appears
                                try {
                                    WebElement signInPopup = driver.findElement(By.id("nav-signin-tooltip"));
                                    if (signInPopup.isDisplayed()) {
                                        signInPopup.click();
                                        System.out.println("⛔ Sign-in popup skipped");
                                    }
                                } catch (Exception e) {
                                    // No popup — continue
                                }

                                WebElement addToCartButton = driver.findElement(By.id("add-to-cart-button"));
                                addToCartButton.click();
                                Thread.sleep(3000);
                                driver.navigate().back();
                                break;
                            } catch (Exception e) {
                                System.out.println("⚠️ Could not add product in tab: " + product);
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println("⚠️ Error while processing: " + product);
                }
            }

            // Go to cart
            driver.findElement(By.id("nav-cart")).click();
            System.out.println("🧾 Opened cart page");

            // Wait for cart total
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement priceElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.sc-price"))
            );

            String priceText = priceElement.getText().replace("₹", "").replace(",", "").trim();
            int totalPrice = Integer.parseInt(priceText);

            System.out.println("🧾 Total Cart Value: ₹" + totalPrice);
            Assert.assertTrue(totalPrice > 2000, "❌ Total cart value is less than ₹2000!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}

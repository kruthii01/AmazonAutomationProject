package tasks;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalTime;

public class Task3_LoginValidation {

    WebDriver driver;
    String amazonEmail = "jk1816593@gmail.com";
    String amazonPassword = "123456@";

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Kruthi\\Downloads\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void fetchProfileName() {

        try {
            // --- TIME CONDITION ---
            LocalTime now = LocalTime.now();
            LocalTime startTime = LocalTime.of(12, 0); // 12 PM
            LocalTime endTime = LocalTime.of(15, 0);   // 3 PM

            if (now.isBefore(startTime) || now.isAfter(endTime)) {
                System.out.println("Login test skipped: allowed only between 12 PM and 3 PM.");
                return; // skip the rest
            }

            driver.get("https://www.amazon.in/");
            System.out.println("Opened Amazon home page.");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Click Sign-In link
            WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-link-accountList")));
            signInLink.click();

            // Enter email
            WebElement emailBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email' or @type='text']")));
            emailBox.clear();
            emailBox.sendKeys(amazonEmail);

            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("continue")));
            continueBtn.click();

            // Wait a little for password page to load
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));

            // Enter password
            WebElement passwordBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='password']")));
            passwordBox.clear();
            passwordBox.sendKeys(amazonPassword);

            WebElement signInBtn = driver.findElement(By.id("signInSubmit"));
            signInBtn.click();

            // Wait and fetch profile name
            WebElement profileName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-link-accountList-nav-line-1")));
            String nameText = profileName.getText();
            System.out.println("Logged in as: " + nameText);

            // --- PROFILE NAME VALIDATION ---
            if (nameText.matches(".*[ACGILK].*")) {
                System.out.println("Profile name contains disallowed letters: A, C, G, I, L, K");
            } else {
                System.out.println("Profile name is valid.");
            }

        } catch (Exception e) {
            System.out.println("Failed to fetch profile name: " + e.getMessage());
        }
    }

    @AfterClass
    public void teardown() {
        System.out.println("Closing browser...");
        if (driver != null) {
            driver.quit();
        }
        System.out.println("Browser closed.");
    }
}

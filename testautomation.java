package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class testautomation {
    public static void main(String[] args) {
        // Set path to ChromeDriver if necessary
        // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Open the webpage
            driver.get("https://arjitnigam.github.io/myDreams/");

            // Wait for loading animation to appear
            WebElement loadingAnimation = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("loadingAnimation"))
            );

            // Wait for loading animation to disappear
            wait.until(ExpectedConditions.invisibilityOf(loadingAnimation));

            // Check main content and "My Dreams" button are visible
            WebElement mainContent = driver.findElement(By.id("mainContent"));
            WebElement myDreamsButton = driver.findElement(By.xpath("//button[@id='dreamButton']"));
            
            assert mainContent.isDisplayed();
            assert myDreamsButton.isDisplayed();

            // Click the "My Dreams" button
            myDreamsButton.click();

            // Switch to the second tab (dreams-diary.html)
            List<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1));

            // Validate dream entries
            List<WebElement> dreamEntries = driver.findElements(By.cssSelector(".dream-entry"));
            assert dreamEntries.size() == 10;

            for (WebElement entry : dreamEntries) {
                String dreamType = entry.findElement(By.cssSelector(".dream-type")).getText();
                assert dreamType.equals("Good") || dreamType.equals("Bad");

                String dreamName = entry.findElement(By.cssSelector(".dream-name")).getText();
                assert !dreamName.isEmpty();

                String daysAgo = entry.findElement(By.cssSelector(".days-ago")).getText();
                assert !daysAgo.isEmpty();
            }

            // Switch to the third tab (dreams-total.html)
            driver.switchTo().window(tabs.get(2));

            int goodDreams = Integer.parseInt(driver.findElement(By.id("goodDreams")).getText());
            int badDreams = Integer.parseInt(driver.findElement(By.id("badDreams")).getText());
            int totalDreams = Integer.parseInt(driver.findElement(By.id("totalDreams")).getText());
            int recurringDreams = Integer.parseInt(driver.findElement(By.id("recurringDreams")).getText());

            assert goodDreams == 6;
            assert badDreams == 4;
            assert totalDreams == 10;
            assert recurringDreams == 2;

            List<WebElement> recurringEntries = driver.findElements(By.cssSelector(".recurring-dream"));
            assert recurringEntries.stream().anyMatch(e -> e.getText().equals("Flying over mountains"));
            assert recurringEntries.stream().anyMatch(e -> e.getText().equals("Lost in maze"));

            System.out.println("All tests passed successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}

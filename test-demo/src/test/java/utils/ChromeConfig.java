package utils;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeConfig {
    public static void configOption(ChromeOptions chromeOptions){
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        chromeOptions.setExperimentalOption("useAutomationExtension", false);
    }
    public static void configDriver(ChromeDriver driver){
        driver.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
    }
}

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FirstTest{
    public void firstTest(){
        //浏览器驱动自动化
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();

        //浏览器允许所有驱动路径
        chromeOptions.addArguments("--remote-allow-origins=*");

        //打开浏览器
        WebDriver driver = new ChromeDriver(chromeOptions);

        //跳转到百度首页
        driver.get("https://www.baidu.com");

        //通过 ID 找到输入框，并输入文字
        driver.findElement(By.id("chat-textarea")).sendKeys("蔡徐坤");

        //通过 ID 找到提交按钮，并点击
        driver.findElement(By.id("chat-submit-button")).click();
        driver.quit();
    }
}
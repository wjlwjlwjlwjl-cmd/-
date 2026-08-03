import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ChromeConfig;
import utils.ScreenShot;

public class Test_2026_8_3 {

    public void test1(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.addArguments("--remote-allow-origins=*");

        ChromeDriver chromeDriver = new ChromeDriver();
        chromeDriver.get("https://www.baidu.com");
        //通过 cssSelector 选择元素
        //chromeDriver.findElement(By.cssSelector("#chat-textarea")).sendKeys("陈伯宁");

        //通过 xpath 选择元素
        /**
         * //* 页面中的所有元素
         * / 组建子元素
         * //[] 页面中的所有指定节点
         * .. 父节点
         * //*[@...] 根据属性选择，比如 //*[id="kw"]
         * //div/ul/li[3]
         */
        //chromeDriver.findElement(By.xpath("//*[@id=\"chat-textarea\"]")).sendKeys("陈伯宁");

        //获取所有热搜文字
        List<WebElement> rets = chromeDriver.findElements(By.xpath("//*[@id=\"hotsearch-content-wrapper\"]/li/a/span[2]"));
        if(rets.size() != 0){
            for(WebElement e: rets){
                System.out.println(e.getText());
            }
        }
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException e){
            System.out.println(e);
        }
        
        //点击、提交对象不能操作不可见、不存在的元素 .click
        //模拟键盘输入 .sendKeys，叠加操作
        //清空输入内容 .clear
        //获取文本 getText（文字不代表文本，input 的 value 就是不是文本而是属性）
        //获取属性 getAttribute
        /*chromeDriver.findElement(By.xpath("//*[@id=\"chat-textarea\"]")).sendKeys("陈伯宁");
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException e){
            System.out.println(e);
        }*/
        chromeDriver.findElement(By.xpath("//*[@id=\"chat-textarea\"]")).clear();
        String name = chromeDriver.findElement(By.xpath("//*[@id=\"chat-submit-button\"]")).getAttribute("value");
        System.out.println(name);
        System.out.println(chromeDriver.getTitle());
        System.out.println(chromeDriver.getCurrentUrl());
    }

    public void test2() throws InterruptedException{
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get("https://www.baidu.com");
        driver.findElement(By.xpath("//*[@id=\"s-top-left\"]/a[6]")).click();

        //页面的切换，一般只跳转一次，如果一定要跳转多次，直接使用 get
        Set<String> handles = driver.getWindowHandles();
        for(String handle: handles){
            if(!handle.equals(driver.getWindowHandle())){
                driver.switchTo().window(handle);
            }
        }
        driver.findElement(By.xpath("//*[@id=\"chat-textarea\"]")).sendKeys("陈伯宁");

        //窗口大小控制
        Thread.sleep(500);
        driver.manage().window().fullscreen();
        Thread.sleep(500);
        driver.manage().window().minimize();
        driver.manage().window().maximize();
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(ScreenShot.getFileName()));
        } catch (IOException e) {
            System.out.println(e);
        }
        driver.close(); //关闭标签页
        Thread.sleep(1000);
        driver.quit(); // 退出浏览器，销毁浏览器驱动
    }

    public void test3() throws InterruptedException{
        /**
         * 没有找到 Element
         * 1. css/xpath 复制错误
         * 2. css/xpath 搭配错误
         * 3. 自动化脚本的操作页面和手动操作的页面不同
         * 4. 动态标签
         * 5. 代码速度远大于页面加载速度
         */
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        ChromeConfig.configOption(chromeOptions);
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        ChromeConfig.configDriver(driver);

        driver.get("https://www.baidu.com");
        driver.findElement(By.cssSelector("#chat-textarea")).sendKeys("徐大虾");
        driver.findElement(By.cssSelector("#chat-submit-button")).click();

        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));//隐式等待，等待界面中的所有对象渲染完毕后执行后续动作
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(3));//显示等待（智能等待）
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#\\31  > div > h3 > a")));

        driver.findElement(By.cssSelector("#\\31  > div > h3 > a")).click();
        Thread.sleep(2000);
        Set<String> handles = driver.getWindowHandles();
        for(String handle: handles){
            if(!driver.getWindowHandle().equals(handle)){
                driver.switchTo().window(handle);
                System.out.println("switch");
            }
        }
        Thread.sleep(1000);
        driver.navigate().refresh();
        Thread.sleep(1000);
        driver.navigate().back();
        Thread.sleep(1000);
        driver.navigate().forward();
        driver.quit();
    }

    public void test4(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        ChromeConfig.configOption(chromeOptions);
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        ChromeConfig.configDriver(driver);

        Alert alert = driver.switchTo().alert(); //切换到 alert 弹窗上进行交互
        alert.accept();
        alert.dismiss();
        alert.sendKeys("hello");
        driver.quit();
    }

    public void test5() throws InterruptedException{
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        ChromeConfig.configOption(chromeOptions);
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        ChromeConfig.configDriver(driver);

        driver.get("C:\\Users\\diinki\\Git\\test-development-and-practice\\test-demo\\src\\test\\html\\upload.html");
        String file = "C:\\Users\\diinki\\Git\\test-development-and-practice\\test-demo\\src\\test\\screenshots\\2026-08-03\\16-36-42.png";
        driver.findElement(By.cssSelector("#upload")).sendKeys(file);
        Thread.sleep(1000);
        driver.quit();
    }

    public void test6() throws InterruptedException{
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        ChromeConfig.configOption(chromeOptions);
        //chromeOptions.addArguments("--headless=new");
        //chromeOptions.addArguments("--window-size=1920,1080");
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        ChromeConfig.configDriver(driver);

        driver.get("https://www.baidu.com");
        driver.findElement(By.cssSelector("#chat-textarea")).sendKeys("徐大虾");
        driver.findElement(By.cssSelector("#chat-submit-button")).click();

        //隐式等待只对findElement找不到元素有效果
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        Thread.sleep(2000);

        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try{
            FileUtils.copyFile(srcFile, new File(ScreenShot.getFileName()));
        }
        catch(IOException e){
            System.out.println(e);
        }
        System.out.println("截图保存成功");
        driver.quit();
    }

    public void test7(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        ChromeConfig.configOption(chromeOptions);
        //等待DOM上的主要元素加载完成再进行get的后续操作；none，不等待
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        ChromeConfig.configDriver(driver);
        driver.get("https://www.bilibili.com");
        driver.findElement(By.cssSelector("#app > div.bili-feed4 > main > div.feed2 > div > div.container.is-version8 > div:nth-child(2) > div > div > div > div.bili-video-card__info > div > h3 > a"));
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try{
            FileUtils.copyFile(srcFile, new File(ScreenShot.getFileName()));
        }
        catch(IOException e){
            System.out.println(e);
        }
        System.out.println("截图保存成功");
        driver.quit();
    }
}

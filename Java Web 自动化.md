# Java Web 自动化

# 一、QuickStart

## 1\.1 Selenium 和 WebDriverManager

Selenium 是我们常用来进行 Web 自动化测试的第三方库，Python 也支持使用 Selenium，支持 Chrome、Edge、Safari、Firefox 这些主流浏览器；WebDriverManager 是用来自动识别浏览器及其版本并下载对应驱动。

导入 Selenium 

```Java
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>4.23.0</version>
</dependency>
```

导入 WebDriverManager

```Java
<dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.8.0</version>
    <scope>test</scope>
</dependency>
```

## 1\.2 操作步骤

```Java
public void firstTest(){
    *//浏览器驱动自动化*
    WebDriverManager.chromedriver().setup();
    ChromeOptions chromeOptions = new ChromeOptions();
    
    *//浏览器允许所有驱动路径*
    chromeOptions.addArguments("--remote-allow-origins=*");

    *//打开浏览器*
    WebDriver driver = new ChromeDriver(chromeOptions);

    *//跳转到百度首页*
    driver.get("https://www.baidu.com");

    *//通过 ID 找到输入框，并输入文字*
    driver.findElement(By.id("chat-textarea")).sendKeys("蔡徐坤");

    *//通过 ID 找到提交按钮，并点击*
    driver.findElement(By.id("chat-submit-button")).click();
    driver.quit();
}
```

直接使用这种脚本，访问一个网站次数较多，很容易触发反爬机制，因为实际上我们实现 Web UI 测试自动化的技术直接更改写法就是写爬虫。出于练习自动化脚本的目的，而不是专门写爬虫，可以增加以下配置来伪装真人操作

```Java
chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
chromeOptions.setExperimentalOption("useAutomationExtension", false);

driver.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
```

# 二、常用函数

> Selenium 支持很多种选择方式，但是用的比较多的只有两种 `cssSelector` 和 `xpath`，而且前者更多
> 
> 无论哪种方式，在浏览器的开发者工具中都可以直接 copy 页面控件的相应元素，
> 
> 

## 2\.1 FindElement

FindElement 用来选择页面上的某个空间进行互动

### 2\.1\.1 选择方式

#### 2\.1\.1\.1 cssSelector

直接通过在样式表中的选择方式选择即可，比如 `<input id="name" value="UseName">`，可以通过下面的方式选中

```Java
*chromeDriver.findElement(By.cssSelector("#chat-textarea")).sendKeys("徐大虾");*
```

#### 2\.1\.1\.2 xpath

Xpath 相对规则多一些，有如下几条：

1. `//*` 表示页面下的所有元素

2. `/` 选择一个空间下的子元素

3. `/...` 选择一个界面下的某些元素，比如 `/input` 获取到的就是当前页面下的所有 input 标签

4. `/[@...]` 选择具有某种属性的所有元素，比如 `//*[@id="chat-textarea"]` 获取到的是当前页面 id 为 chat\-textarea 的元素

5. `..` 当前元素父元素

6. `/div[3]` 通过下标的方式获取同一层级中的元素中的一个，比如百度主页中的热搜内容条，都在同一层级，其中某一条通过 xpath 的方式选择就可以通过这种方式

```Java
**List**<**WebElement**> rets = chromeDriver.findElements(**By**.xpath("//*[@id=\"hotsearch-content-wrapper\"]/li/a/span[2]"));
**if**(rets.size() != 0){
    **for**(**WebElement** e: rets){
        **System**.out.println(e.getText());
    }
}
```

如上获取百度（https://www\.baidu\.com）的热搜内容。

### 2\.1\.2 互动行为

#### **click** 

模拟用户对空间的点击行为，可以操作 `button` `a` 等，但是并不是“万物可点击”，click 无法操作不可见、不存在的控件

**sendKeys** 

模拟键盘输入，对于控件的输入是叠加行为，可以直接通过字符串的方式传入内容

**clear** 

情况输入内容

**getText/getAttribute** 

FindElement/FindElements 返回的控件的类型是 WebElement。

前者用来获取标签的文本内容，后者用来获取标签某个属性的值，比如：

```HTML
<button>百度一下</button>
<button value="百度一下"><button>
```

前者文字内容的获取就可以通过 `getText`，但是后者就需要通过 `getAttribute("value")` 的方式

## 2\.2 window 句柄

windows句柄是抽象出来的用来标识一个标签页的字符串，通过 `getWindowHandle` 获取当前标签页句柄，`getWindowHandles` 获取所有标签页的句柄

**为什么会使用到这个句柄？** 例如在发生页面跳转的时候，有的就是直接在当前标签页展示，还有的是跳转到新的标签页进行展示。对于后者，我们的操作依然停留在旧的标签页，也就是说我们无法操作新的标签页，例如

```Java
driver.findElement(**By**.cssSelector("#chat-textarea")).sendKeys("徐大虾");
driver.findElement(**By**.cssSelector("#chat-submit-button")).click();

//这样写是有问题的，后面进行解释

driver.findElement(**By**.cssSelector("#\\31  > div > h3 > a")).click();
```

这几句代码模拟了在百度首页输入，点击“百度一下”，再点击第一个搜索结果的行为。此时信息展示页是在一个新的标签，如果继续直接操作的话，那么都是在老的标签页进行的

可以通过这种方式进行页面的切换：

```Java
**Set**<**String**> handles = driver.getWindowHandles();
**for**(**String** handle: handles){
    **if**(!handle.equals(driver.getWindowHandle())){
        driver.switchTo().window(handle);
    }
}
```

那么多个标签页呢？在 Web 自动化测试的场景下，基本最多两个标签页，更多就可以使用 `driver.get("url")` 的方式直接跳转

## 2\.3 窗口操作

```Java
driver.manage.window().fullscreen(); //全屏
driver.manage.window().minimize(); //最小化
driver.manage.window().maximize(); //最大化
```

## 2\.4 屏幕截图

```Java
File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
FileUtils.copyFile(srcFile, new File("test_screenshot.png"));
```

通过这种方式需要手动指定不同的截图名，否则会覆盖。同时为了日后整理方便，建议通过时间戳操作将截图文件通过类似 `年月日/时分秒.png` 的方式存储，其中 FileUtils 需要额外导包

```Java
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.6</version>
</dependency>
```

## 2\.5 等待

> 脚本执行中未找到元素抛出异常，可能是什么原因？
> 
> - xpath、css 从浏览器中拷贝错误
> 
> - xpath、css 和选择器种类搭配错误
> 
> - 动态标签
> 
> - 自动化页面和手动操作页面不同
> 
> - 页面加载完成前代码就已经执行完导致找不到元素
> 
> 

### 2\.5\.1 Thread\.sleep

因为时间是死的，所以可能要么太长，要么太短

### 2\.5\.2 隐式等待

隐式等待的等待对象是全局所有元素，当所有元素加载完成后 findElement/findElements 会正常执行；当到达超时时间会抛出异常（隐式等待的等待效果只作用于控件选择）

```Java
*driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));*
```

### 2\.5\.3 显式等待（智能等待）

显式等待，等待的控件可以自己手动指定，并且等待结束的条件同样可以自己指定。

相比隐式等待，写法更麻烦，但是可以不等待不需要的元素，并且等待行为更灵活，比如 `presenceOfElementLocated` 元素加载完成、`elementToBeClickable` 元素可点击（可见、未遮挡）

```Java
WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(3));*//显示等待（智能等待）*
webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#\\31  > div > h3 > a")));
```

## 2\.6 标签页操作

**向前跳转** 

```Java
driver.navigate().forward();
```

**向后跳转** 

```Java
driver.navigate().back();
```

**刷新** 

```Java
driver.navigate().refresh();
```

## 2\.7 弹窗处理

首先要明白弹窗（指的是浏览器弹窗而不是网页弹窗，比如 alert）是一种警告、异常一类的存在，有弹窗时是无法选择页内标签或者进行其他操作的

1. 切换到 alert 弹窗上进行操作

    ```Java
    Alert alert = driver.switchTo.alert();
    ```

2. 弹窗操作，如果只有一个按钮，那么 accept 和 dismiss 达成的行为是一样的：点击唯一的按钮

    ```Java
    alert.accept(); //点击确认按钮
    alert.dismiss(); // 点击取消按钮
    alert.sendKeys(); // 如果弹窗中有输入框，进行输入
    ```

## 2\.8 无头模式

所谓无头模式，就是整个脚本和浏览器相应都在后台进行，只有运行终端会进行相应输出。

建议使用 `options.addArgument("--headless=new")`，或者也可以使用 `options.addArgument("--headless")`

但是使用无头模式更容易被反爬机制识别

## 2\.9 PageLoadStrategy

页加载策略是每个标签页跳转后对页面元素的等待策略，默认是 NORMAL

- **NORMAL** 等待所有 DOM 解析完毕，图片、css、视频、js 加载完成后，才从阻塞中继续往下执行

- **EAGER** 等待 DOM 解析完毕，不等待图片、视频、js、css。但实际上现代很多页面结构中的标签在源代码中是不存在的，比如 Vue 中一些标签就是通过 js 渲染的。如果确定是源代码中的控件，不依赖 js 代码生成可以直接使用这个页加载策略，否则建议搭配显式等待

- **None** 不进行任何等待，直接完成页加载的过程

之所以要设置页加载策略，是因为我们的隐式等待、显式等待都会等待页加载完成。对于显式加载而言，即使所需空间符合等待条件了，也要等待页加载完成

## 3\.0 关闭

- 关闭页面，`driver.close()`，这种方式关闭的是当前标签页

- 关闭浏览器，`driver.quit()`，退出浏览器，销毁浏览器句柄


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp () throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("app", "C:/Users/User/Desktop/JavaAppiumAutomation/apks/org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown(){
        driver.quit();
    }

    @Test
    public void testChanelSearch(){

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "function",
                "Cannot find search input",
                5

        );

        waitForElementPresent(
                By.xpath("//android.widget.TextView[@text='Functional programming']"),
                "Article not found",
                5
        );

        waitForElementPresent(
                By.xpath("//android.widget.TextView[@text='Function (mathematics)']"),
                "Article not found",
                5
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to chanel search",
                5
        );

        waitForElementNotPresent(
                By.xpath("//android.widget.TextView[@text='Functional programming']"),
                "Article is still displayed",
                5
        );

        waitForElementNotPresent(
                By.xpath("//android.widget.TextView[@text='Function (mathematics)']"),
                "Article is still displayed",
                5
        );

    }

    @Test
    public void testSearchText(){

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        WebElement article_element = waitForElementPresent(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find article element",
                5
        );

        String text = article_element.getAttribute("text");
        Assert.assertEquals("Cannot text attribute", "Search…", text);

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Java",
                "Cannot find search input",
                5

        );

    }

    @Test
    public void testSearchAllArticleOnPage() {

        waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input ",
                5);

        waitForElementPresent(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "'Search Wikipedia' is not shown",
                5);

        waitForElementAndSendKeys(By.id("org.wikipedia:id/search_src_text"),
                "cycle",
                "Text cannot be send",
                5);

        List<WebElement> linkList = driver.findElements(By.id("org.wikipedia:id/page_list_item_title"));
        for(WebElement element: linkList){
            System.out.println(element.getText());
            Assert.assertTrue("Cannot text attribute", element.getText().contains("Cycle"));
        }

    }

    @Test
    public void testSwipeArticle() {

        waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input ",
                5);

        waitForElementPresent(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "'Search Wikipedia' is not shown",
                5);

        waitForElementAndSendKeys(By.id("org.wikipedia:id/search_src_text"),
                "Appium",
                "Text cannot be send",
                5);

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Appium']"),
                "Cannot find 'Appium' article",
                15);

        waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "'Search Wikipedia' is not shown",
                15);

        swipeUpToFindElement(
                By.xpath("//*[@text='View page in browser']"),
                "Cannot find the end of the article",
                20);
    }

    //Ожидание появления элемента на экране
    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds){
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    //Ожидание появления элемента на экране с 5 секундным ожиданием
    private WebElement waitForElementPresent(By by, String error_message){
        return  waitForElementPresent(by, error_message, 5);
    }

    //Нажатие на найденный элемент
    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds){
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    //Ввод какого-либо значения в найденный элемент
    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds){
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    //Ожидание отсутствия элемента на экране
    private Boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds){
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    //Очистка содержимого в найденном элементе
    private WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds){
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    //Свайп экрана снизу вверх
    protected void swipeUp(int timeOfSwipe){
        TouchAction action = new TouchAction(driver);
        //Получаем размер экрана по осям X и Y
        Dimension size = driver.manage().window().getSize();
        //Координаты первоночального нажатия на экран по горизонтали
        int x = size.width / 2;
        //Координаты первоночального нажатия на экран по вертикали
        int start_y = (int) (size.height * 0.8);
        //Координаты свайпа для конечной точки по вертикали
        int end_y = (int) (size.height * 0.2);

        //Метод для свайпа снизу вверх. Переменная timeOfSwipe отвечает за скорость свайпа
        action.press(x, start_y).waitAction(timeOfSwipe).moveTo(x, end_y).release().perform();
    }

    //Быстрый свайп
    protected void swipeUpQuick(){
        swipeUp(200);
    }

    //Свайп до определенного элемента
    protected void swipeUpToFindElement(By by, String error_message, int max_swipes){
        int already_swiped = 0;
        while (driver.findElements(by).size() == 0){

            if (already_swiped > max_swipes){
                //Метод отвечает за то, если элемент не найден, тогда тест упадет
                waitForElementPresent(by, "Cannot find element by swiping up. \n" + error_message, 0);
                //Выходим из функции, если элемент найден.
                return;
            }

            swipeUpQuick();
            ++already_swiped;
        }
    }
}

package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MainPageObject {

    protected AppiumDriver driver;

    public MainPageObject(AppiumDriver driver){
        this.driver = driver;
    };

    //Ожидание появления элемента на экране
    public WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds){
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    //Ожидание появления элемента на экране с 5 секундным ожиданием
    public WebElement waitForElementPresent(By by, String error_message){
        return  waitForElementPresent(by, error_message, 5);
    }

    //Нажатие на найденный элемент
    public WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds){
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    //Ввод какого-либо значения в найденный элемент
    public WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds){
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    //Ожидание отсутствия элемента на экране
    public Boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds){
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    //Очистка содержимого в найденном элементе
    public WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds){
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    //Свайп экрана снизу вверх
    public void swipeUp(int timeOfSwipe){
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
        action.
                press(x, start_y).
                waitAction(timeOfSwipe).
                moveTo(x, end_y).
                release().
                perform();
    }

    //Быстрый свайп
    public void swipeUpQuick(){
        swipeUp(200);
    }

    //Свайп до определенного элемента
    public void swipeUpToFindElement(By by, String error_message, int max_swipes){
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

    //Свайп определенного элемента справа налево.
    public void swipeElementToLeft(By by, String error_message){

        WebElement element = waitForElementPresent(
                by,
                error_message,
                15
        );

        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;

        //Метод для свайпа элемента справа налево.
        TouchAction action = new TouchAction(driver);
        action.
                press(right_x,middle_y).
                waitAction(300).
                moveTo(left_x, middle_y).
                release().
                perform();
    }

    //Получаем количество элементов в массиве, которые отобразились на экране устройства
    public int getAmountOfElements(By by) {
        List elements = driver.findElements(by);
        return elements.size();
    }

    //Проверка на то, что элемент отсутствует в списке
    public void assertElementNotPresent(By by, String error_message){
        int amount_of_elements = getAmountOfElements(by);
        if (amount_of_elements > 0){
            String default_message = "An element '" + by.toString() + "' supposed to be not present";
            throw new AssertionError(default_message + "" + error_message);
        }
    }

    //Метод отвечает за получение атрибута найденного элемента
    public String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeoutInSeconds){
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }

}

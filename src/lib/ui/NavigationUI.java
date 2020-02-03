package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;


//Методы навигации по приложению
public class NavigationUI extends MainPageObject{

    private static final String
            MY_LISTS_LINK = "//android.widget.FrameLayout[@content-desc='My lists']";

    public NavigationUI(AppiumDriver driver) {
        super(driver);
    }

    public void clickMyList() {
        this.waitForElementAndClick(
                By.xpath(MY_LISTS_LINK),
                "Cannot find navigation button to my list",
                15
        );
    }
}

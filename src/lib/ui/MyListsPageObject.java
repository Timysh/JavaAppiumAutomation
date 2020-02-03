package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

//Методы для взаимодействия с экраном MyLists
public class MyListsPageObject extends MainPageObject{

    public static final String
            FOLDER_BY_NAME_TPL = "//*[@text='{FOLDER_NAME}']",
            ARTICLE_BY_TITLE_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='{TITLE}']",
            ARTICLE_BY_DESCRIPTION_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_description'][@text='{DESCRIPTION}']";

    private static String getFolderXpathByName(String name_of_folder) {
        return FOLDER_BY_NAME_TPL.replace("{FOLDER_NAME}", name_of_folder);
    }

    private static String getSaveArticleXpathByTitle(String article_title) {
        return ARTICLE_BY_TITLE_TPL.replace("{TITLE}", article_title);
    }

    private static String getSaveArticleXpathByDescription(String article_description) {
        return ARTICLE_BY_DESCRIPTION_TPL.replace("{DESCRIPTION}", article_description);
    }

    public MyListsPageObject(AppiumDriver driver) {
        super(driver);
    }

    public void openFolderByName(String name_of_folder) {
        String folder_name_xpath = getFolderXpathByName(name_of_folder);
        this.waitForElementAndClick(
                By.xpath(folder_name_xpath),
                "Cannot find folder by name " + name_of_folder,
                25
        );
    }

    public void waitForArticleToAppearByTitle(String article_title) {
        String title_xpath = getSaveArticleXpathByTitle(article_title);
        this.waitForElementPresent(By.xpath(title_xpath), "Cannot find saved article by title " + article_title, 15);
    }

    public void waitForArticleToDisappearByTitle(String article_title) {
        String title_xpath = getSaveArticleXpathByDescription(article_title);
        this.waitForElementNotPresent(By.xpath(title_xpath), "Saved article still present with title " + article_title, 15);
    }

    public void swipeByArticleToDelete(String article_title) {
        this.waitForArticleToAppearByTitle(article_title);
        String article_xpath = getFolderXpathByName(article_title);
        this.swipeElementToLeft(By.xpath(article_xpath), "Cannot find save article");
        this.waitForArticleToDisappearByTitle(article_title);
    }

}

import lib.CoreTestCase;
import lib.ui.*;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FirstTest extends CoreTestCase {

    private MainPageObject MainPageObject;

    protected void setUp() throws Exception {
        super.setUp();
        MainPageObject = new MainPageObject(driver);
    }

    @Test
    public void testSearch()
    {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.waitForSearchResult("Object-oriented programming language");
    }

    @Test
    public void testChanelSearch(){

        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.waitForCancelButtonToAppear();
        SearchPageObject.clickCancelSearch();
        SearchPageObject.waitForCancelButtonToDisappear();
    }

    @Test
    public void testCompareArticleTitle(){
        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        String article_title = ArticlePageObject.getArticleTitle();

        assertEquals(
                "We see unexpected title",
                "Java (programming language)",
                article_title
            );
    }

    @Test
    public void testSearchText(){

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        WebElement article_element = MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find article element",
                5
        );

        String text = article_element.getAttribute("text");
        Assert.assertEquals("Cannot text attribute", "Search…", text);

        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

    }

    @Test
    public void testSearchAllArticleOnPage() {

        MainPageObject.waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input ",
                5);

        MainPageObject.waitForElementPresent(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "'Search Wikipedia' is not shown",
                5);

        MainPageObject.waitForElementAndSendKeys(By.id("org.wikipedia:id/search_src_text"),
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
        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Appium");
        SearchPageObject.clickByArticleWithSubstring("Appium");

        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        ArticlePageObject.waitForTitleElement();
        ArticlePageObject.swipeToFooter();
    }

    @Test
    public void testSaveFirstArticleToMyList() {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);
        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        ArticlePageObject.waitForTitleElement();
        String article_title = ArticlePageObject.getArticleTitle();
        String name_of_folder = "Learning programming";
        ArticlePageObject.addArticleToMyList(name_of_folder);
        ArticlePageObject.closeArticle();

        NavigationUI NavigationUI = new NavigationUI(driver);
        NavigationUI.clickMyList();

        MyListsPageObject MyListsPageObject = new MyListsPageObject(driver);
        try{Thread.sleep(3000);}  catch (Exception e){}
        MyListsPageObject.openFolderByName(name_of_folder);
        MyListsPageObject.swipeByArticleToDelete(article_title);
    }

    @Test
    public void testAmountOfNotEmptySearch() {

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input ",
                15);

        MainPageObject.waitForElementPresent(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "'Search Wikipedia' is not shown",
                15);

        String search_line = "Linkin park dis";
        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_line,
                "Text cannot be send",
                15);

        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_title']";
        MainPageObject.waitForElementPresent(
                By.xpath(search_result_locator),
                "Cannot find anything by the request " + search_line,
                15);

        //Помещаем в переменную полученное количество элементов
        int amount_of_search_results = MainPageObject.getAmountOfElements(
          By.xpath(search_result_locator)
        );

        Assert.assertTrue(
                "We found too few results",
                amount_of_search_results > 0
        );
    }

    @Test
    public void testAmountOfEmptySearch() {

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input ",
                5);

        String search_line = "dfghdsfhsdhsfsfgdgh";
        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_line,
                "Text cannot be send",
                5);

        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_title']";
        String empty_results_label = "//*[@text='No results found']";
        MainPageObject.waitForElementPresent(
                By.xpath(empty_results_label),
                "Cannot find empty result label by the request" + search_line,
                15);

        MainPageObject.assertElementNotPresent(
                By.xpath(search_result_locator),
                "We'we found some results by request " + search_line
        );

    }

    @Test
    public void testChangeScreenOrientationOnSearchResults() {

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input ",
                5);

        String search_line = "Java";
        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_line,
                "Text cannot be send",
                5);

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by " + search_line,
                15);

        //Получаем аттрибут text найденного элемента и сохраняем в переменной
        String title_before_rotation = MainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );

        //Поворачиваем экран устройства
        driver.rotate(ScreenOrientation.LANDSCAPE);

        String title_after_rotation = MainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );

        Assert.assertEquals(
                "Article title have been changed after screen rotation",
                title_before_rotation,
                title_after_rotation);

        driver.rotate(ScreenOrientation.PORTRAIT);

        String title_after_second_rotation = MainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );

        Assert.assertEquals(
                "Article title have been changed after screen rotation",
                title_before_rotation,
                title_after_second_rotation);
    }

    @Test
    public void testCheckSearchArticleInBackground() {

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input ",
                5);

        String search_line = "Java";
        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_line,
                "Text cannot be send",
                5);

        MainPageObject.waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language'",
                15);

        //Сворачиваем приложение на 2 секунды.
        driver.runAppInBackground(2);

        MainPageObject.waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find article after returning background",
                15);

    }

    @Test
    public void testSave2ArticlesToMyListAndDeleteFirstArticle() {

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input ",
                5);

        MainPageObject.waitForElementPresent(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "'Search Wikipedia' is not shown",
                5);

        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Text cannot be send",
                5);

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Java' article",
                15);

        MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "'Object-oriented programming language' is not shown",
                20);

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find button to open article options",
                5);

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Cannot find options to add article reading list",
                5);

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot find 'Got it' tip overlay",
                5);

        MainPageObject.waitForElementAndClear(
                By.id("org.wikipedia:id/text_input"),
                "Cannot find input to set name of articles folder",
                5
        );

        String name_of_folder = "Learning programming";
        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/text_input"),
                name_of_folder,
                "Cannot put text into articles folder input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='OK']"),
                "Cannot press OK button",
                5);

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot close article find X link",
                15);

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input ",
                15);

        MainPageObject.waitForElementPresent(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "'Search Wikipedia' is not shown",
                15);

        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Ruby",
                "Text cannot be send",
                15);

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Ruby (programming language)']"),
                "Cannot find 'Ruby' article",
                15);

        MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "'Ruby (programming language)' is not shown",
                20);

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find button to open article options",
                15);

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Cannot find options to add article reading list",
                15);

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.support.v7.widget.RecyclerView[@resource-id='org.wikipedia:id/list_of_lists']/android.widget.FrameLayout[@clickable='true']"),
                "Cannot find 'Learning programming' list",
                15);

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot close article find X link",
                15);

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot find navigation button to My list",
                15);

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='" + name_of_folder + "']"),
                "Cannot find created folder",
                15);

        MainPageObject.swipeElementToLeft(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot find saved article"
        );

        MainPageObject.waitForElementNotPresent(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot delete saved article",
                15
        );

        MainPageObject.waitForElementPresent(
                By.xpath("//*[@text='Ruby (programming language)']"),
                "Cannot find article 'Ruby (programming language)'",
                15
        );
    }

    @Test
    public void testCheckSearchArticle() {

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input ",
                5);

        String search_line = "Java";
        MainPageObject.waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_line,
                "Text cannot be send",
                5);

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by " + search_line,
                15);

        String article_title = MainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find title of article",
                15
        );

        Assert.assertEquals(
                "Article titles do not match",
                "Java (programming language)",
                article_title
        );
    }

}

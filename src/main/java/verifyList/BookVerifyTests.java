package verifyList;

import bussinesObject.Author;
import bussinesObject.Book;
import bussinesObject.Genre;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import service.BookService;
import service.GenreService2;

import java.util.logging.Logger;

public class BookVerifyTests {
    private static final Logger LOG = Logger.getLogger(BookVerifyTests.class.getName());

    BookService bookService = new BookService();

    @Step
    public void verifyStatusKodResponse(int statusKod, Response response) {
        Allure.addAttachment("Response statusCode", String.valueOf(response.statusCode()));
        LOG.info(String.format("Expected StatusKod %s, Get Status code %s ", statusKod, response.getStatusCode()));
        Assert.assertEquals(statusKod, response.statusCode());
    }



    @Step
    public void verifyAddNewBook(Book book, Response response) {
        LOG.info("Expected objects equals");
        Assert.assertEquals(book, response.body().as(Book.class));
    }

    @Step
    public void verifyDeleteBookInBD(Book book, Response response) {
        LOG.info(String.format("Expected not this Id in BD "));
        Assert.assertFalse(bookService.checkBookIdBD(book.getBookId()), "getIdBook from BD is not exists");
    }

    @Step
    public void verifyUpdateBook(Book book, Response response) {
        LOG.info(String.format("Expected equal bookId"));
        Assert.assertEquals(book.getBookId(), response.jsonPath().get("bookId"));
        LOG.info(String.format("Expected equal objects"));
        Assert.assertEquals(book, response.body().as(Book.class));
    }


    @Step
    public void verifyBadUpdateBook(Book book) {
        LOG.info(String.format("Expected equal bookId"));
        Assert.assertTrue(bookService.checkBookIdBD(book.getBookId()));  //check  our normalId
    }

    @Step
    public void verifyBookByFind(Book book, Response response) { //find by authorId
        Book responseBook = response.body().as(Book.class);
        LOG.info(String.format("Expected equal bookId"));
        Assert.assertEquals(book.getBookId(), responseBook.getBookId());
        LOG.info(String.format("Expected equal bookName"));
        Assert.assertEquals(book.getBookName(), responseBook.getBookName());
        LOG.info(String.format("Expected equal bookDescription"));
        Assert.assertEquals(book.getBookDescription(), responseBook.getBookDescription());
        LOG.info(String.format("Expected equal bookLang"));
        Assert.assertEquals(book.getBookLanguage(), responseBook.getBookLanguage());
        LOG.info(String.format("Expected equal bookVolume"));
        Assert.assertEquals(book.getVolume(), responseBook.getVolume());
        LOG.info(String.format("Expected equal bookYear"));
        Assert.assertEquals(book.getPublicationYear(), responseBook.getPublicationYear());
        LOG.info(String.format("Expected equal bookPageCount"));
        Assert.assertEquals(book.getAdditional().getPageCount(), responseBook.getAdditional().getPageCount());
        LOG.info(String.format("Expected equal bookLenght"));
        Assert.assertEquals(book.getAdditional().getSize().getLength(), responseBook.getAdditional().getSize().getLength());
        LOG.info(String.format("Expected equal bookHeight"));
        Assert.assertEquals(book.getAdditional().getSize().getHeight(), responseBook.getAdditional().getSize().getHeight());
        LOG.info(String.format("Expected equal bookWidth"));
        Assert.assertEquals(book.getAdditional().getSize().getWidth(), responseBook.getAdditional().getSize().getWidth());
        LOG.info(String.format("Expected equal objects"));
        Assert.assertEquals(book, responseBook);
    }

    @Step
    public void verifyBookBySearch(Response response, Book book) { //find by genreId for Author
        LOG.info(String.format("Find all from List for Author"));
        Assert.assertTrue(bookService.getBookAfterResponse(response, book));
    }

    @Step
    public void verifyBooksFromList(Response response, Book book, Book book2) { //find by genreId for Author
        LOG.info(String.format("Find all from List for Author"));
        Assert.assertTrue(bookService.getBookAfterResponse(response, book, book2));
    }


}

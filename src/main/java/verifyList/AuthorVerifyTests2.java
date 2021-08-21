package verifyList;

import bussinesObject.Author;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import service.AuthorService;

import java.util.logging.Logger;

public class AuthorVerifyTests2 {
    private static final Logger LOG = Logger.getLogger(AuthorVerifyTests2.class.getName());


    AuthorService authorService = new AuthorService();

    @Step
    public void verifyStatusKodResponse(int statusKod, Response response) {
        Allure.addAttachment("Response statusCode", String.valueOf(response.statusCode()));
        LOG.info(String.format("Expected StatusKod %s, Get Status code %s ", statusKod, response.getStatusCode()));
        Assert.assertEquals(statusKod, response.statusCode());
    }

    @Step
    public void verifyCheckIdInBDNotExists(Author author) {
        LOG.info(String.format("Expected not this Id in BD "));
        Assert.assertFalse(authorService.checkAuthorIdBD(author.getAuthorId()), "getIdAuthor from BD is not exists");
    }

    @Step
    public void verifyIdAuthorExists(Author author) {
        LOG.info(String.format("Expected Id exists in BD"));
        Assert.assertTrue(authorService.checkAuthorIdBD(author.getAuthorId()));  //check  our normalId
    }

    @Step
    public void verifyAddNewAuthor(Author author, Response response) {
        Allure.addAttachment("Response AuthorClass",response.body().as(Author.class).toString());
        Author result = response.body().as(Author.class); //response.body().as(this.responseClass);
        Assert.assertEquals(author, result);
    }


//    @Step
//    public void verifyDeleteAuthor(Author author, Response response) {
//        LOG.info(String.format("Expected not this Id in BD "));
//        Assert.assertFalse(authorService.checkAuthorIdBD(author.getAuthorId()), "getIdAuthor from BD is not exists");
//    }

    @Step
    public void verifyUpdateAuthor(Author author, Response response) {
        Author responseAuthor = response.body().as(Author.class);
        LOG.info(String.format("Expected 200, our Status code %s ", response.getStatusCode()));
        Assert.assertEquals(200, response.getStatusCode()); //200 OK update
        LOG.info(String.format("Expected equal authorId"));
        Assert.assertEquals(author.getAuthorId(), responseAuthor.getAuthorId());
        LOG.info(String.format("Expected equal authorNationality"));
        Assert.assertEquals(author.getNationality(), responseAuthor.getNationality());
        LOG.info(String.format("Expected equal authorDescription"));
        Assert.assertEquals(author.getAuthorDescription(), responseAuthor.getAuthorDescription());
        LOG.info(String.format("Expected equal authorFirstName"));
        Assert.assertEquals(author.getAuthorName().getFirst(), responseAuthor.getAuthorName().getFirst());
        LOG.info(String.format("Expected equal authorSecondName"));
        Assert.assertEquals(author.getAuthorName().getSecond(), responseAuthor.getAuthorName().getSecond());
        LOG.info(String.format("Expected equal authorCity"));
        Assert.assertEquals(author.getBirth().getCity(), responseAuthor.getBirth().getCity());
        LOG.info(String.format("Expected equal authorCountry"));
        Assert.assertEquals(author.getBirth().getCountry(), responseAuthor.getBirth().getCountry());
        LOG.info(String.format("Expected equal authorDate"));
        Assert.assertEquals(author.getBirth().getDate(), responseAuthor.getBirth().getDate());
        LOG.info(String.format("Expected equal objects"));
        Assert.assertEquals(author, response.body().as(Author.class));
    }



    @Step
    public void verifyAuthorByFind(Author author, Response response) { //find by authorId
        Allure.addAttachment("Response AuthorClass",response.body().as(Author.class).toString());
        LOG.info(String.format("Expected equal objects"));
        Assert.assertEquals(author, response.body().as(Author.class));
      }

    @Step
    public void verifyAuthorBySearch(Author author,Response response) {  //search by authorName
        LOG.info(String.format("Expected find equal objects"));
        Assert.assertTrue(authorService.getAuthorSearch2(author,response), "");
    }

    @Step
    public void verifyAuthorsForGenre(Author author, Author author2, Response response) { //find by genreId for Author
        LOG.info(String.format("Find all genres for Author"));
        Assert.assertTrue(authorService.getAuthorsAfterResponse(response, author, author2));
    }

}

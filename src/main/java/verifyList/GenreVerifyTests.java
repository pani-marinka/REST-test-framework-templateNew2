package verifyList;

import bussinesObject.Author;
import bussinesObject.Genre;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import response.BaseResponse;
import service.GenreService2;

import java.util.logging.Logger;

public class GenreVerifyTests {
    private static final Logger LOG = Logger.getLogger(GenreVerifyTests.class.getName());

    GenreService2 genreService = new GenreService2();

    @Step
    public void verifyStatusKodResponse(int statusKod, Response response) {
        LOG.info(String.format("Expected StatusKod %s, Get Status code %s ", statusKod, response.getStatusCode()));
        Allure.addAttachment("Response statusCode", String.valueOf(response.statusCode()));
        Assert.assertEquals(statusKod, response.statusCode());
    }


    @Step
    public void verifyGenreByFind(Genre genre, Response response) { //find by genreId
        Allure.addAttachment("Response statusCode", String.valueOf(response.body().as(Genre.class)));
        LOG.info(String.format("Expected equal objects"));
        Assert.assertEquals(genre, response.body().as(Genre.class));
    }

    @Step
    public void verifyGenresForAuthor(Genre genre, Genre genre2, Response response) { //find by genreId for Author
        LOG.info(String.format("Find all genres for Author"));
        Assert.assertTrue(genreService.getGenresAfterResponse(response, genre, genre2));
    }


    @Step
    public void verifyGenreBySearch(Genre genre, Response response) {  //search by genreName
        LOG.info(String.format("Expected find equal objects"));
        Assert.assertTrue(genreService.getGenresSearch2(genre, response), "");

    }

    @Step
    public void verifyAddNewGenre(Genre genre, Response response) {
        Integer d = response.jsonPath().get("genreId");
        Integer v = Math.toIntExact(genre.getGenreId());
        Genre result2 = response.body().as(Genre.class); //response.body().as(this.responseClass);
        LOG.info(String.format("Expected equal genreName"));
        Assert.assertEquals(genre.getGenreName(), response.jsonPath().get("genreName"));
        LOG.info(String.format("Expected equal genreDescription"));
        Assert.assertEquals(genre.getGenreDescription(), response.jsonPath().get("genreDescription"));
        LOG.info("Expected objects equals");
        Assert.assertEquals(genre, result2);
        LOG.info("Check ourId in BD");
        Assert.assertTrue(genreService.checkGenreIdBD(genre.getGenreId()));
    }

    @Step
    public void verifyAddBadNewGenre(Genre genre) {
        LOG.info("False check ourId in BD");
        Assert.assertFalse(genreService.checkGenreIdBD(genre.getGenreId()));
    }


    @Step
    public void verifyUpdateGenre(Genre genre, Response response) {
        Allure.addAttachment("Response statusCode", String.valueOf(response.body().as(Genre.class)));
        LOG.info(String.format("Expected equal objects"));
        Assert.assertEquals(genre, response.body().as(Genre.class));
    }

    @Step
    public void verifyBadUpdateGenre(Genre genre) {
        LOG.info(String.format("Expected old good Id"));
        Assert.assertTrue(genreService.checkGenreIdBD(genre.getGenreId()));  //check  our normalId has

    }

    @Step
    public void verifyAddTheSameGenreId(int code, Response response) {
        LOG.info(String.format("Expected %s, our Status code %s ", code, response.getStatusCode()));
        Assert.assertEquals(code, response.getStatusCode()); // 409 - already exists (Conflict: Genre with such 'genreId' already exists!")
    }


    @Step
    public void verifyDeleteGenre(Genre genre, Response response) {
        LOG.info(String.format("Expected not this Id in BD "));
        Assert.assertFalse(genreService.checkGenreIdBD(genre.getGenreId()), "getIdGenre from BD is not exists");
    }

    @Step
    public void verifyStatusKodResponseFromYurii(int statusKod, BaseResponse response) {
        LOG.info(String.format("Expected StatusKod %s, Get Status code %s ", statusKod, response.getStatusCode()));
        Assert.assertEquals(statusKod, response.getStatusCode());
    }
}

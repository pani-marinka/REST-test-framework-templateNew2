package service;

import bussinesObject.*;
import com.google.gson.Gson;
import enums.EndPoints;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.EndpointBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static io.restassured.RestAssured.*;

public class AuthorService {
    private static final Logger LOG = Logger.getLogger(AuthorService.class.getName());


    //@Attachment
    public Response createAuthor(Author author) {  // create genre
        String postEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("author").get();
        String jsonNewGenre = new Gson().toJson(author);
        LOG.info(jsonNewGenre);
        Response result = with()
                .contentType(ContentType.JSON)
                .body(jsonNewGenre)
                .when()
                .request("POST", postEndPoint)
                .then()
                .log()
                .all()
//                .statusCode(201)
                .extract()
                // .as(Genre.class);
                .response();
        Allure.addAttachment("Response createAuthor", result.getBody().print());
        return result;
    }


    @Attachment
    public int getAuthorMyId() {  // generation new Id author for add
        String d = String.valueOf(Collections.max(getListAuthorId()));
        int maxId = Integer.valueOf(d) + 1;
        return maxId++;
    }

    @Attachment
    public Author getTestingAuthor() {  //create New Testing Author
        Author newAuthor = new Author();
        int autoId = getAuthorMyId();
        AuthorName newAuthorName = new AuthorName();
        newAuthorName.setFirst("FirsNameTest" + autoId);
        newAuthorName.setSecond("SecondNameTest");

        Birth birthAuthor = new Birth();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String dateTestAuthor = format1.format(new Date());
        birthAuthor.setDate(dateTestAuthor);
        birthAuthor.setCountry("TestCountry");
        birthAuthor.setCity("TestCity");

        newAuthor.setAuthorId(autoId);
        newAuthor.setAuthorName(newAuthorName);
        newAuthor.setNationality("TestNationality");
        newAuthor.setBirth(birthAuthor);
        newAuthor.setAuthorDescription("TestDescription");

        return newAuthor;
    }


    @Attachment
    public boolean checkAuthorIdBD(int idAuthor) {  //check  idGenre in BD
        boolean flagIsId = getListAuthorId().stream().anyMatch(rn -> rn.equals(idAuthor));
        return flagIsId;
    }

    // @Attachment
    private List<Integer> getListAuthorId() {
        List<Integer> authorsIdList = new ArrayList<>();
        baseURI = EndPoints.END_POINTS.getValue();
        int x = 1;
        do {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/authors?page=" + x)
                    .then()
                    .extract().response();

            List<Integer> authorsIdListTmp = response.jsonPath().get("authorId");

            if (authorsIdListTmp.size() > 0) {

                authorsIdList.addAll(authorsIdListTmp);
                x++;
            }
            if (authorsIdListTmp.size() == 0) x = 0;
        } while (x > 0);
        Allure.addAttachment("Response GetListAuthorId", authorsIdList.toString());
        return authorsIdList;
    }


    //@Attachment
    public Response deleteAuthor(Author author) {
        String postEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("author").get() + "/" + author.getAuthorId();
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .request("DELETE", postEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Response deleteAuthor", String.valueOf(response.statusCode()));
        return response;
    }


    // @Attachment
    public Response updateAuthor(Author author) {  // create genre
        String postEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("author").get();
        String jsonNewGenre = new Gson().toJson(author);
        Response result = with()
                .contentType(ContentType.JSON)
                .body(jsonNewGenre)
                .when()
                .request("PUT", postEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Response updateAuthor", result.getBody().print());
        return result;
    }


    //@Attachment
    public Response getAllAuthors() {
        // String  getEndPoint = new EndpointBuilder().pathParameter("genres").get();
        String getEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("authors").get();
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request("GET", getEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Response GetAllAuthor", response.getBody().print());
        return response;
    }


    // @Attachment
    public Response getAuthorId(Author author) {
        String getEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("author").get() + "/" + author.getAuthorId();


        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request("GET", getEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Response GetAuthorId", response.getBody().print());
        return response;
    }


    //@Attachment
    public Response getAuthorIdForBookId(Book book) {
        String getEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("book").get() + "/" + book.getBookId() + "/author";
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request("GET", getEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Response getAuthorIdForBookId", response.getBody().print());
        return response;
    }


    //@Attachment
    public Response getAuthorSearchList(Author ourAuthor) {  // generation new Id author for add
        String byAuthorName = String.valueOf(ourAuthor.getAuthorId());
        String getEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("authors").get() + "/search?query=" + byAuthorName;

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .request("GET", getEndPoint)
                        .then()
                        .log()
                        .all()
                        .extract()
                        .response();
        Allure.addAttachment("Response getAuthorSearchList", response.getBody().print());
        return response;
    }

    @Attachment
    public boolean getAuthorSearch2(Author ourAuthor, Response response) {  // generation new Id author for add
        List<Author> authorSearchtList = new ArrayList<>();
        authorSearchtList = response.jsonPath().getList("$", Author.class);
        for (Author author : authorSearchtList) {
            if (author.equals(ourAuthor)) return true;
        }
        return false;
    }


//    @Attachment
    public Response getAuthorsForGenreId(Genre genre) {
        ///api/library/genre/{genreId}/authors
        String getEndPoint = new EndpointBuilder().pathParameter("genre").get() + "/" + genre.getGenreId() + "/authors";
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request("GET", getEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Response getAuthorsForGenreId", response.getBody().print());
        return response;
    }


    @Attachment
    public boolean getAuthorsAfterResponse(Response response, Author author, Author author2) {
        List<Author> genreSearchList = response.jsonPath().getList("$", Author.class);
        return genreSearchList.contains(author) && genreSearchList.contains(author) && genreSearchList.size() == 2;
    }


    @Attachment
    public Author updateAuthorForTestAuthor(Author author) {
        int newUpdateName = author.getAuthorId() + 5; //different from currentAuthor
        AuthorName authorName = new AuthorName();
        authorName.setFirst("Test Name for testing" + String.valueOf(newUpdateName));
        authorName.setSecond("Test Second for testing" + String.valueOf(newUpdateName));
        author.setAuthorName(authorName);

        Birth authorBirth = new Birth();
        authorBirth.setCountry("testCountryy" + String.valueOf(newUpdateName));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int addMinuteTime = 1440;  // minus 24 hours
        Date nowTime = new Date();
        long curTimeInMs = nowTime.getTime();
        final long ONE_MINUTE_IN_MILLIS = 60000;
        Date afterAddMins = new Date(curTimeInMs - (addMinuteTime * ONE_MINUTE_IN_MILLIS));
        String dateTestAuthor = dateFormat.format(afterAddMins);
        authorBirth.setDate(dateTestAuthor);
        authorBirth.setCity("test Cityy" + String.valueOf(newUpdateName));

        author.setBirth(authorBirth);

        author.setNationality("test Narionality" + String.valueOf(newUpdateName));

        author.setAuthorDescription("test Description" + String.valueOf(newUpdateName));
        return author;
    }

}

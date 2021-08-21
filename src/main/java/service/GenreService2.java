package service;

import bussinesObject.Author;
import bussinesObject.Genre;
import client.HttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.ListOptions;
import enums.EndPoints;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import response.BaseResponse;
import utils.EndpointBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static io.restassured.RestAssured.*;

public class GenreService2 {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOG = Logger.getLogger(GenreService2.class.getName());

    public BaseResponse<Genre> getGenreFromYurii(int genreId) {
        String endpoint = new EndpointBuilder().pathParameter("genre").pathParameter(genreId).get();
        return new BaseResponse<>(HttpClient.get(endpoint), Genre.class); // return 404
    }


    public BaseResponse<Object> getGenresFromYurii(ListOptions options) {
        EndpointBuilder endpoint = new EndpointBuilder().pathParameter("genres");
        if (options.orderType != null) endpoint.queryParam("orderType", options.orderType);
        endpoint
                .queryParam("page", options.page)
                .queryParam("pagination", options.pagination)
                .queryParam("size", options.size);
        if (options.sortBy != null) endpoint.queryParam("sortBy", options.sortBy);
        return new BaseResponse<>(HttpClient.get(endpoint.get()), Object.class);
    }


    //  TODO properly handle genre entity
    @Attachment
    public BaseResponse<Genre> createGenreFromYurii(Genre genre) {
        String genreJson = gson.toJson(genre, Genre.class);
        String endpoint = new EndpointBuilder().pathParameter("api/library/genre").get();
        LOG.info(gson.toJson(genreJson));
        Allure.addAttachment("My BaseResponceGson", genreJson);
        return new BaseResponse<>(HttpClient.post(endpoint, genreJson), Genre.class);
    }


    @Attachment
    public Genre createObjGenre() {
        Integer newId = getGenresMyId();
        Genre newGenre = new Genre(newId, "Test new new" + newId, "continueTest  test");
        return newGenre;
    }

    @Attachment
    public Genre createObjGenreId(int idGenre) {
        Genre newGenre = new Genre(idGenre, "Test new new" + idGenre, "continueTest  test");
        return newGenre;
    }


    // @Attachment
    public Response getAllGenres() {
        String getEndPoint = EndPoints.GENRESURL.getValue();
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request("GET", getEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Response GetAllGenres", response.getBody().print());
        return response;
    }


    // @Attachment
    public Response getGenreId(Genre genre) {
        String getEndPoint = new EndpointBuilder().pathParameter("genre").get() + "/" + genre.getGenreId();
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request("GET", getEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Response GetGenreId", response.getBody().print());
        return response;
    }


    // @Attachment
    public Response getGenreForBookId(int bookId) {    //api/library/book/{bookId}/genre

        String getEndPoint = new EndpointBuilder().pathParameter("book").get() + "/" + bookId + "/genre";
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request("GET", getEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Responce getGenreForBookId", response.getBody().print());
        return response;
    }


    //@Attachment
    public Response getGenresForAuthorId(Author author) { //api/library/author/{authorId}/genres
        String getEndPoint = new EndpointBuilder().pathParameter("author").get() + "/" + author.getAuthorId() + "/genres";
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request("GET", getEndPoint)
                //    .get("/genres")
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Responce GetGenresForAuthorId", response.getBody().print());
        return response;
    }


    @Attachment
    public boolean getGenresAfterResponse(Response response, Genre genre, Genre genre2) {
        List<Genre> genreSearchList = response.jsonPath().getList("$", Genre.class);
        return genreSearchList.contains(genre) && genreSearchList.contains(genre2) && genreSearchList.size() == 2;
    }


    public Response createGenre(Genre genre) {  // create genre
        String postEndPoint = new EndpointBuilder().pathParameter("genre").get();
        String jsonNewGenre = new Gson().toJson(genre);
        Response result = with()
                .contentType(ContentType.JSON)
                .body(jsonNewGenre)
                .when()
                .request("POST", postEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("My responceCreateGenre", result.getBody().print());
        return result;
    }


    //  @Attachment
    public Response updateGenre(Genre genre) {  // create genre
        String postEndPoint = new EndpointBuilder().pathParameter("genre").get();
        String jsonNewGenre = new Gson().toJson(genre);
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
        Allure.addAttachment("Response UpdateGenre", result.getBody().print());
        return result;
    }


   // @Attachment
    public Response getGenreSearchList(Genre genre) {  // generation new Id author for add
        String getEndPoint = new EndpointBuilder().pathParameter("genres").get() + "/search?query="
                + genre.getGenreName().substring(genre.getGenreName().length() - String.valueOf(genre.getGenreId()).length() - 3); //substr(byGenreName) == new+Id
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
        Allure.addAttachment("Response getGenreSearchList", response.getBody().print());
        return response;
    }

    @Attachment
    public boolean getGenresSearch2(Genre genre, Response response) {  // generation new Id author for add
        List<Genre> genreSearchList = response.jsonPath().getList("$", Genre.class);
        for (Genre genreFind : genreSearchList) {
            if (genreFind.equals(genre)) return true;
        }
        return false;
    }


    @Attachment
    public Integer getGenresMyId() {  // generation new Id genre for add
        int maxId = (int) (Collections.max(getListGenreId()) + 1);
        return maxId++;
    }


    @Attachment
    public boolean checkGenreIdBD(int idGenre) {  //check  idGenre in BD for Genre
        boolean flagIsId = getListGenreId().stream().anyMatch(rn -> rn.equals(idGenre));
        return flagIsId;
    }


    //@Attachment
    public Response deleteGenre(Genre genre) {
        String postEndPoint = new EndpointBuilder().pathParameter("genre").get() + "/" + genre.getGenreId();
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .request("DELETE", postEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Response deleteGenre", String.valueOf(response.statusCode()));
        return response;
    }


    @Attachment
    private List<Integer> getListGenreId() {
        List<Integer> genresIdList = new ArrayList<>();
        baseURI = EndPoints.END_POINTS.getValue();
        int x = 1;
        do {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/genres?page=" + x)
                    .then()
                    .extract().response();

            List<Integer> genresIdListTmp = response.jsonPath().get("genreId");
            if (genresIdListTmp.size() > 0) {
                genresIdList.addAll(genresIdListTmp);
                x++;
            }
            if (genresIdListTmp.size() == 0) x = 0;
        } while (x > 0);
        Allure.addAttachment("Response GetAllGenres", genresIdList.toString());
        return genresIdList;
    }


}

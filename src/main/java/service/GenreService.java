package service;

import bussinesObject.Author;
import bussinesObject.Genre;
import client.HttpClient;
import com.google.gson.Gson;
import entity.ListOptions;
import enums.EndPoints;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import response.BaseResponse;
import utils.EndpointBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;

@Deprecated
public class GenreService {

    public BaseResponse<Genre> getGenre(int genreId) {
        String endpoint = new EndpointBuilder().pathParameter("genre").pathParameter(genreId).get();
        //   String getEndPoint = new EndpointBuilder().pathParameter("genre").get() + "/" + genreId;
        //String endpoint = new EndpointBuilder().pathParameter("genre").get() + "/" + genreId;
        //return new BaseResponse<>(HttpClient.get(endpoint), Object.class);
        return new BaseResponse<>(HttpClient.get(endpoint), Genre.class); // return 404
    }


    public BaseResponse<Object> getGenres(ListOptions options) {
        EndpointBuilder endpoint = new EndpointBuilder().pathParameter("genres");
        if (options.orderType != null) endpoint.queryParam("orderType", options.orderType);
        endpoint
                .queryParam("page", options.page)
                .queryParam("pagination", options.pagination)
                .queryParam("size", options.size);
        if (options.sortBy != null) endpoint.queryParam("sortBy", options.sortBy);
        return new BaseResponse<>(HttpClient.get(endpoint.get()), Object.class);
    }

    /*
        // TODO properly handle genre entity
        public BaseResponse<Object> createGenre(Object genre) {
            String endpoint = new EndpointBuilder().pathParameter("genre").get();
            return new BaseResponse<>(HttpClient.post(endpoint, genre.toString()), Object.class);
        }
    */

    @Attachment //value = "aaditional", type = "Object.class", fileExtension = ".txt") // not visual
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


    @Attachment
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
        Allure.addAttachment("My attachment", response.getBody().toString());
        return response;
    }


    @Attachment
    public Response getGenreId(Genre genre) {
        String getEndPoint = new EndpointBuilder().pathParameter("genre").get() + "/" + genre.getGenreId();

        // String postEndPoint  = EndPoints.GENRESURL.getValue();
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

        return response;
    }

    @Step
    @Attachment
    public Response getGenreForBookId(int bookId) {
        //api/library/book/{bookId}/genre
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

        return response;
    }


    @Attachment
    public Response getGenresForAuthorId(Author author) {
        //api/library/author/{authorId}/genres
        String getEndPoint = new EndpointBuilder().pathParameter("author").get() + "/" + author.getAuthorId() + "/genres";
        List<Genre> genreSearchtList = new ArrayList<>();
        // String postEndPoint  = EndPoints.GENRESURL.getValue();
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
        return response;
    }

    @Step
    @Attachment
    public boolean getGenresAfterResponse(Response response, Genre genre, Genre genre2) {
        List<Genre> genreSearchList = response.jsonPath().getList("$", Genre.class);
        return genreSearchList.contains(genre) && genreSearchList.contains(genre2) && genreSearchList.size() == 2;
    }


    @Step
    @Attachment
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
///                .statusCode(201)
                .extract()
                // .as(Genre.class);
                .response();
        return result;
    }

    @Step
    @Attachment
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
///                .statusCode(201)
                .extract()
                // .as(Genre.class);
                .response();
        return result;
    }

    /*
        public ArrayList getGenresSearch(String byGenreName) {  // generation new Id genre for add
            String postEndPoint = new EndpointBuilder().pathParameter("genres").get() + "/search?query=" + byGenreName;
            //   Integer genreId = -1;
            ArrayList<Integer> genreIdListTmp = new ArrayList<>();
            ArrayList<String> genreIdListTmp2 = new ArrayList<>();
            ArrayList<String> genreIdListTmp3 = new ArrayList<>();
            Response response =
                    given()
                            .header("Content-type", "application/json")
                            .when()
                            .request("GET", postEndPoint)
                            .then()
                            .log()
                            .all()
                            .extract()
                            .response();
            if (response.getStatusCode() == 200) {
                Map<Integer, String> genreFindList = new ConcurrentHashMap<>();

                genreIdListTmp = response.jsonPath().get("genreId");
                genreIdListTmp2 = response.jsonPath().get("genreName");
                genreIdListTmp3 = response.jsonPath().get("genreDescription");
                //  genreIdListTmp.size();
                //  genreIdListTmp2.size();
                int i = 0;
                for (Integer keyGenre : genreIdListTmp) {
                    genreFindList.put(keyGenre, genreIdListTmp2.get(i));
                    if (genreIdListTmp2.get(i).contains(byGenreName)) {
                        String desriptionNameGenre = genreIdListTmp3.get(i);
                        genreIdListTmp3 = new ArrayList<>();
                        genreIdListTmp3.add(String.valueOf(keyGenre));
                        genreIdListTmp3.add(genreIdListTmp2.get(i));
                        genreIdListTmp3.add(desriptionNameGenre);
                        genreIdListTmp3.add(String.valueOf(response.getStatusCode()));
                        return genreIdListTmp3; //return IdGenry
                    }
                    i++;
                }
            }

            return genreIdListTmp3; //if not contains
        }
    */
    @Step
    public boolean getGenresSearch2(Genre genre) {  // generation new Id author for add
        // String byGenreName = String.valueOf(genre.getGenreId());
//public String substring(int beginIndex, int endIndex)
        //   int i = genre.getGenreName().substring(String.valueOf(genre.getGenreId()).length()+2)
        String getEndPoint = new EndpointBuilder().pathParameter("genres").get() + "/search?query="
                + genre.getGenreName().substring(genre.getGenreName().length() - String.valueOf(genre.getGenreId()).length() - 3); //substr(byGenreName) == new+Id
        List<Genre> genreSearchtList = new ArrayList<>();
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
        if (response.getStatusCode() == 200) {
            genreSearchtList = response.jsonPath().getList("$", Genre.class);
        }
        for (Genre genreFind : genreSearchtList) {
            if (genreFind.equals(genre)) return true;
        }
        return false;
    }

    @Step
    @Attachment
    public Integer getGenresMyId() {  // generation new Id genre for add
        RestAssured.baseURI = EndPoints.END_POINTS.getValue();
        List<Integer> genreIdList = new ArrayList<>();
        List<Integer> genreIdListTmp = new ArrayList<>();
        int maxId = 0;
        int x = 1;
        do {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/genres?page=" + x)
                    .then()
                    .extract().response();
            genreIdListTmp = response.jsonPath().get("genreId");

            if (genreIdListTmp.size() > 0) {
                genreIdList.size();
                genreIdList.addAll(genreIdListTmp);
                x++;
            }
            if (genreIdListTmp.size() == 0) x = 0;
        } while (x > 0);
        //  Collections.sort(genreIdList);

        maxId = (int) (Collections.max(genreIdList) + 1);
        return maxId++;
    }

    @Step
    @Attachment
    public boolean checkGenreIdBD(int idGenre) {  //check  idGenre in BD for Genre
        RestAssured.baseURI = EndPoints.END_POINTS.getValue();
        List<Integer> genreIdListTmp = new ArrayList<>();
        boolean flagIsId = false;
        int x = 1;
        do {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/genres?page=" + x)
                    .then()
                    .extract().response();
            genreIdListTmp = response.jsonPath().get("genreId");

            if (genreIdListTmp.size() > 0) {
                x++;
                flagIsId = genreIdListTmp.stream().anyMatch(rn -> rn.equals(idGenre));
                if (flagIsId) return flagIsId;
            }
            if (genreIdListTmp.size() == 0) x = 0;
        } while (x > 0);
        return flagIsId;
    }

    @Step
    @Attachment
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
        return response;
    }

}

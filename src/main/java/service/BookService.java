package service;

import bussinesObject.*;
import com.google.gson.Gson;
import enums.EndPoints;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.EndpointBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.*;

public class BookService {

    public Response getAllBooks() {
        String getEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("books").get();
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request("GET", getEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Response GetAllBooks", response.getBody().print());
        return response;
    }

    @Attachment
    public int getBookMyId() {  // generation new Id author for add
        String d = String.valueOf(Collections.max(getListBookId()));
        int maxId = Integer.valueOf(d) + 1;
        return maxId++;
    }



    private List<Integer> getListBookId() {
        List<Integer> booksIdList = new ArrayList<>();
        String postEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("books").get() + "?page=";
        int x = 1;
        do {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .request("GET", postEndPoint + x)
                    //.get("/authors?page=" + x)
                    .then()
                    .extract().response();

            List<Integer> booksIdListTmp = response.jsonPath().get("bookId");

            if (booksIdListTmp.size() > 0) {

                booksIdList.addAll(booksIdListTmp);
                x++;
            }
            if (booksIdListTmp.size() == 0) x = 0;
        } while (x > 0);
        Allure.addAttachment("Response booksIdList", booksIdList.toString());
        return booksIdList;
    }


    @Attachment
    public Book getTestingBook() {  //create New Testing Author
        int bookId = getBookMyId();
        Book newBook = new Book();

        Size newSize = new Size();
        newSize.setHeight(1d);
        newSize.setWidth(1d);
        newSize.setLength(1d);

        Additional newAdditional = new Additional();
        newAdditional.setPageCount(345);
        newAdditional.setSize(newSize);


        newBook.setBookId(bookId);
        newBook.setBookName("testName" + bookId);
        newBook.setBookLanguage("test lang");
        newBook.setBookDescription("test descr");
        newBook.setAdditional(newAdditional);
        newBook.setPublicationYear(2018);


        return newBook;
    }


    @Attachment
    public Book getTestingBookforUpdate(int bookId) {  //create New Testing Author

        Book newBook = new Book();
        int idBook = bookId;
        Size newSize = new Size();
        newSize.setHeight(2d);
        newSize.setWidth(2d);
        newSize.setLength(2d);

        Additional newAdditional = new Additional();
        newAdditional.setPageCount(9000);
        newAdditional.setSize(newSize);


        newBook.setBookId(idBook);
        newBook.setBookName("testName" + getBookMyId());
        newBook.setBookLanguage("test lang" + getBookMyId());
        newBook.setBookDescription("test descr" + getBookMyId());
        newBook.setAdditional(newAdditional);
        newBook.setPublicationYear(2019);


        return newBook;
    }

    public Response createBook(Book book, Integer authorId, Integer genreId) {  // create genre
        String postEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("book").get() + "/" + authorId + "/" + genreId; // використовувати pathParam
        String jsonNewGenre = new Gson().toJson(book);
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
        Allure.addAttachment("Response createBook", result.getBody().print()); //pritty print
        return result;
    }


    public Response deleteBook(Book book) {
        String deleteEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("book").get() + "/" + book.getBookId();
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .request("DELETE", deleteEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Response deleteGenre", String.valueOf(response.statusCode()));
        return response;
    }

    @Attachment
    public boolean checkBookIdBD(int idBook) {  //check  idGenre in BD
        boolean flagIsId = getListBookId().stream().anyMatch(rn -> rn.equals(idBook));
        return flagIsId;
    }

    public Response updateBook(Book book) {  // create genre
        String putEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("book").get();
        String jsonNewGenre = new Gson().toJson(book);
        Response result = with()
                .contentType(ContentType.JSON)
                .body(jsonNewGenre)
                .when()
                .request("PUT", putEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Response updateBook", result.getBody().print());
        return result;
    }


   // @Attachment
    public Response getBookId(Book book) { //api/library/book/{bookId}
        String getEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("book").get() + "/" + book.getBookId();

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request("GET", getEndPoint)
                .then()
                .log()
                .all()
                .extract()
                .response();
        Allure.addAttachment("Response getBookId", response.getBody().print());
        return response;
    }


   // @Attachment
    public Response getBookSearch(Book book) {
        String byBookName = String.valueOf(book.getBookId());
        String getEndPoint = EndPoints.END_POINTS.getValue() + new EndpointBuilder().pathParameter("books").get() + "/search?q=" + byBookName;


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
        Allure.addAttachment("Response getBookSearch", response.getBody().print());
        return response;
    }


    @Attachment
    public boolean getBookAfterResponse(Response response, Book book) {
        List<Book> genreSearchList = response.jsonPath().getList("$", Book.class);
        return genreSearchList.contains(book) && genreSearchList.size() == 1;
    }

    @Attachment
    public boolean getBookAfterResponse(Response response, Book book, Book book2) {
        List<Book> genreSearchList = response.jsonPath().getList("$", Book.class);
        return genreSearchList.contains(book) && genreSearchList.contains(book2) && genreSearchList.size() == 2;
    }

    /*
       @Step
    @Attachment
    public boolean getBooksAfterResponse(Response response, Book book, Book book2) {
        List<Book> genreSearchList = response.jsonPath().getList("$", Book.class);
        return genreSearchList.contains(book) && genreSearchList.contains(book2) && genreSearchList.size() == 2;
    }
     */

    //@Attachment
    public Response getBooksOfGenre(Genre genre) {
        //api/library/genre/{genreId}/books
        String getEndPoint = new EndpointBuilder().pathParameter("genre").get() + "/" + genre.getGenreId() + "/books";
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
        Allure.addAttachment("Response getBookOfGenre", response.getBody().print());
        return response;
    }


    public Response getBooksOfAuthor(Author author) {
        //api/library/author/{authorId}/books
        String getEndPoint = new EndpointBuilder().pathParameter("author").get() + "/" + author.getAuthorId() + "/books";
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
        Allure.addAttachment("Response getBooksOfAuthor", response.getBody().print());
        return response;
    }



    public Response getBooksOfAuthorofGenre(Author author, Genre genre) {
        //api/library/author/{authorId}/genre/{genreId}/books
        String getEndPoint = new EndpointBuilder().pathParameter("author").get() + "/" + author.getAuthorId() + "/genre/" + genre.getGenreId() + "/books";
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
        Allure.addAttachment("Response getBooksOfAuthorofGenre", response.getBody().print());
        return response;
    }


}

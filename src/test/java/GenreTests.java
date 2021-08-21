import bussinesObject.Author;
import bussinesObject.Book;
import bussinesObject.Genre;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import response.BaseResponse;
import service.AuthorService;
import service.BookService;
import service.GenreService2;
import verifyList.GenreVerifyTests;


public class GenreTests {


    GenreService2 genreService = new GenreService2();
    GenreVerifyTests testGenreVerify = new GenreVerifyTests();
    AuthorService authorService = new AuthorService();
    BookService bookService = new BookService();


    @Test(description = "get all genres")
    public void testGetAllGenres() {
        Response response = genreService.getAllGenres();
        testGenreVerify.verifyStatusKodResponse(200, response); //name200
    }

    @Test(description = "get genre by genreId")
    public void testGetGenreById() {
        Genre newGenre = genreService.createObjGenre();
        Response response = genreService.createGenre(newGenre); // prepare testing data
        testGenreVerify.verifyStatusKodResponse(201, response);  // prepare testing data
        /*start check*/
        response = genreService.getGenreId(newGenre);
        testGenreVerify.verifyStatusKodResponse(200, response);
        testGenreVerify.verifyGenreByFind(newGenre, response);
        /*end check*/
        response = genreService.deleteGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(204, response); // clear testing data
    }

    @Test(description = "get genres by search Name")
    public void testGetGenreBySearchName() {
        Genre newGenre = genreService.createObjGenre();
        Response response = genreService.createGenre(newGenre); // prepare tetsting data
        testGenreVerify.verifyStatusKodResponse(201, response); // prepare tetsting data
        /*start check*/
        response = genreService.getGenreSearchList(newGenre);
        testGenreVerify.verifyStatusKodResponse(200, response);
        testGenreVerify.verifyGenreBySearch(newGenre, response);///!;
        /*end check*/
        response = genreService.deleteGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(204, response); // clear testing data
    }

    @Test(description = "get Genre of special Book")
    public void testGetGenreByGenreForBook() { // get Genre of special Book
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepeare test  Author
        testGenreVerify.verifyStatusKodResponse(201, response); // prepare tetsting data
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        testGenreVerify.verifyStatusKodResponse(201, response); // prepare tetsting data
        Book newBook = bookService.getTestingBook();
        response = bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        testGenreVerify.verifyStatusKodResponse(201, response); // prepare tetsting data
        /*start check*/
        response = genreService.getGenreForBookId(newBook.getBookId());
        testGenreVerify.verifyGenreByFind(newGenre, response);
        /*end check*/
        response = bookService.deleteBook(newBook); // clear test data
        testGenreVerify.verifyStatusKodResponse(204, response); // prepare tetsting data
        response = genreService.deleteGenre(newGenre); //clear test data
        testGenreVerify.verifyStatusKodResponse(204, response); // prepare tetsting data
        response = authorService.deleteAuthor(newAuthor); //clear test data
        testGenreVerify.verifyStatusKodResponse(204, response); // prepare tetsting data
    }


    @Test(description = "get Genres of special Author")
    public void testGetGenreByGenresForAuthor() { //  ///api/library/author/{authorId}/genres  get all Genres of special Author
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepeare test  Author
        testGenreVerify.verifyStatusKodResponse(201, response); // prepare tetsting data
        int newId = genreService.getGenresMyId();
        Genre newGenre = genreService.createObjGenreId(newId);
        response = genreService.createGenre(newGenre); //prepeare test Genre
        testGenreVerify.verifyStatusKodResponse(201, response); // prepare tetsting data
        Book newBook = bookService.getTestingBook();
        response = bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        testGenreVerify.verifyStatusKodResponse(201, response); // prepare tetsting data
        /*Second genre + Second book for this Author*/
        //  newId = newId + 1;
        Genre newGenre2 = genreService.createObjGenreId(newId + 1);
        response = genreService.createGenre(newGenre2); //prepeare test Genre
        testGenreVerify.verifyStatusKodResponse(201, response); // prepare tetsting data
        Book newBook2 = bookService.getTestingBook();
        response = bookService.createBook(newBook2, newAuthor.getAuthorId(), newGenre2.getGenreId()); //prepeare test Book
        testGenreVerify.verifyStatusKodResponse(201, response); // prepare tetsting data
        /*start check*/
        response = genreService.getGenresForAuthorId(newAuthor);
        testGenreVerify.verifyStatusKodResponse(200, response);
        testGenreVerify.verifyGenresForAuthor(newGenre, newGenre2, response);
        /*end check*/
        response = bookService.deleteBook(newBook); // clear test data
        testGenreVerify.verifyStatusKodResponse(204, response); // prepare tetsting data
        response = genreService.deleteGenre(newGenre); //clear test data
        testGenreVerify.verifyStatusKodResponse(204, response); // prepare tetsting data
        response = bookService.deleteBook(newBook2); // clear test data
        testGenreVerify.verifyStatusKodResponse(204, response); // prepare tetsting data
        response = genreService.deleteGenre(newGenre2); //clear test data
        testGenreVerify.verifyStatusKodResponse(204, response); // prepare tetsting data
        response = authorService.deleteAuthor(newAuthor); //clear test data
        testGenreVerify.verifyStatusKodResponse(204, response); // prepare tetsting data
    }


    @Test(description = "verify add new genre without necessary parameters , Negative test")
    public void testAddBadGenre() {
        /*start check*/
        int newId = genreService.getGenresMyId();
        Genre newGenre = genreService.createObjGenreId(-1);
        Response response = genreService.createGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(400, response); // not create
        testGenreVerify.verifyAddBadNewGenreNotIdInBD(newGenre);//not in BD
        newGenre.setGenreId(newId); //return normalId
        newGenre.setGenreName("");//set bad Name
        Allure.addAttachment("My objNewGenre", newGenre.toString());
        response = genreService.createGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(400, response); // not create
        testGenreVerify.verifyAddBadNewGenreNotIdInBD(newGenre);// not in BD
    }


    @Test(description = "verify add new genre")
    public void testAddGenre() throws CloneNotSupportedException {
        Genre newGenre = genreService.createObjGenre();
        Genre newGenreForDeleteAfterTest = (Genre) newGenre.clone();
        /*start check*/
        Response response = genreService.createGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(201, response);
        testGenreVerify.verifyAddNewGenre(newGenre, response);
        Response responseTheSameGenre = genreService.createGenre(newGenre); // test add more of The same genre
        testGenreVerify.verifyAddTheSameGenreId(responseTheSameGenre);
        newGenre.setGenreId(genreService.getGenresMyId()); //set new Id Genre, description The Same
        responseTheSameGenre = genreService.createGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(409, responseTheSameGenre); // not create
        testGenreVerify.verifyAddBadNewGenreNotIdInBD(newGenre);// not in BD
        /*end check*/
        response = genreService.deleteGenre(newGenreForDeleteAfterTest); //clear test data
        testGenreVerify.verifyStatusKodResponse(204, response); // clear tetsting data
    }


    @Test(description = "verify update new genre")
    public void testUpdateGenre() {
        int newId = genreService.getGenresMyId();
        Genre newGenre = genreService.createObjGenreId(newId);
        Response response = genreService.createGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(201, response); //prepare
        /*start check*/
        int newAdd = newId + 5;
        newGenre.setGenreName("Test Name for testing" + String.valueOf(newAdd));
        newGenre.setGenreDescription("Test Test");
        response = genreService.updateGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(200, response); // clear tetsting data
        testGenreVerify.verifyUpdateGenre(newGenre, response);
        newGenre.setGenreId(-1);
        response = genreService.updateGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(400, response);
        newGenre.setGenreId(newId); //return Normal Id Genre
        testGenreVerify.verifyBadUpdateGenre(newGenre);//check  for normal ID
        /*end check*/
        response = genreService.deleteGenre(newGenre); //clear test data
        testGenreVerify.verifyStatusKodResponse(204, response); // clear tetsting data

    }


    @Test(description = "verify delete genre")
    public void deleteGenre() {
        Genre newGenre = genreService.createObjGenre();
        Response response = genreService.createGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(201, response);  //prepeare test genre
        response = genreService.deleteGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(204, response); // clear tetsting data
        testGenreVerify.verifyDeleteGenre(newGenre, response);
    }

    @Test(description = "create genre from Yurii")
    public void testGetGenreByIdFromYurii() {
        Genre newGenre = genreService.createObjGenre();
        BaseResponse<Genre> response = genreService.createGenreFromYurii(newGenre);
        testGenreVerify.verifyStatusKodResponseFromYurii(201, response);  // prepare testing data
    }

}

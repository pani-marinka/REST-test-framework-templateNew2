import bussinesObject.Author;
import bussinesObject.Book;
import bussinesObject.Genre;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import response.BaseResponse;
import service.AuthorService;
import service.BookService;
import service.GenreService2;
import verifyList.GenreVerifyTests;

import java.util.*;


public class GenreTests {


    GenreService2 genreService = new GenreService2();
    GenreVerifyTests testGenreVerify = new GenreVerifyTests();
    AuthorService authorService = new AuthorService();
    BookService bookService = new BookService();
    private static final int invalidId = -1;

    /*for test Data*/
    List<Genre> genreList = new ArrayList<>();
    List<Author> authorList = new ArrayList<>();
    List<Book> bookList = new ArrayList<>();

    @AfterClass(description = "clear test data")
    public void finishTest() {
        System.out.println("*************Begin clear after test**********");
        Book newBook; // = new Book();
        System.out.println("Size listBook: " + bookList.size());
        Iterator<Book> iterBook = bookList.iterator();
        while (iterBook.hasNext()) {
            newBook = iterBook.next();
            Response response = bookService.getBookId(newBook); //check existing
            if (response.getStatusCode() == 200) {
                System.out.println("Book Finish Clear" + newBook.toString());

                response = bookService.deleteBook(newBook); // clear test data
                testGenreVerify.verifyStatusKodResponse(204, response); //  check deleting
            }
            iterBook.remove();
        }
        System.out.println("Size listBook after clear: " + bookList.size());

        Genre newGenre;
        System.out.println("Size listGenre: " + genreList.size());
        Iterator<Genre> iterGenre = genreList.iterator();
        while (iterGenre.hasNext()) {
            newGenre = iterGenre.next();
            Response response = genreService.getGenreId(newGenre); //check existing
            if (response.getStatusCode() == 200) {
                System.out.println("Genre Finish Clear" + newGenre.toString());
                response = genreService.deleteGenre(newGenre); // clear test data
                testGenreVerify.verifyStatusKodResponse(204, response); //  check deleting
            }
            iterGenre.remove();
        }
        System.out.println("Size listGenre after clear: " + genreList.size());


        Author newAuthor;
        System.out.println("Size listAuthor: " + authorList.size());
        Iterator<Author> iterAuthor = authorList.iterator();
        while (iterAuthor.hasNext()) {
            newAuthor = iterAuthor.next();
            Response response = authorService.getAuthorId(newAuthor); //check existing
            if (response.getStatusCode() == 200) {
                System.out.println("Author Finish Clear " + newAuthor.toString());
                response = authorService.deleteAuthor(newAuthor); // clear test data
                testGenreVerify.verifyStatusKodResponse(204, response); //  check deleting
            }
            iterAuthor.remove();
        }
        System.out.println("Size listAuthor after clear: " + authorList.size());

    }


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
        genreList.add(newGenre);
        /*start check*/
        response = genreService.getGenreId(newGenre);
        testGenreVerify.verifyStatusKodResponse(200, response);
        testGenreVerify.verifyGenreByFind(newGenre, response);
    }

    @Test(description = "get genres by search Name")
    public void testGetGenreBySearchName() {
        Genre newGenre = genreService.createObjGenre();
        Response response = genreService.createGenre(newGenre); // prepare tetsting data
        testGenreVerify.verifyStatusKodResponse(201, response); // prepare tetsting data
        genreList.add(newGenre);
        /*start check*/
        response = genreService.getGenreSearchList(newGenre);
        testGenreVerify.verifyStatusKodResponse(200, response);
        testGenreVerify.verifyGenreBySearch(newGenre, response);///!;
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
        genreList.add(newGenre);
        authorList.add(newAuthor);
        bookList.add(newBook);
        /*start check*/
        response = genreService.getGenreForBookId(newBook.getBookId());
        testGenreVerify.verifyGenreByFind(newGenre, response);
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
        genreList.add(newGenre);
        authorList.add(newAuthor);
        bookList.add(newBook);
        /*Second genre + Second book for this Author*/
        //  newId = newId + 1;
        Genre newGenre2 = genreService.createObjGenreId(newId + 1);
        response = genreService.createGenre(newGenre2); //prepeare test Genre
        testGenreVerify.verifyStatusKodResponse(201, response); // prepare tetsting data
        Book newBook2 = bookService.getTestingBook();
        response = bookService.createBook(newBook2, newAuthor.getAuthorId(), newGenre2.getGenreId()); //prepare test Book
        testGenreVerify.verifyStatusKodResponse(201, response); // prepare tetsting data
        genreList.add(newGenre2);
        bookList.add(newBook2);
        /*start check*/
        response = genreService.getGenresForAuthorId(newAuthor);
        testGenreVerify.verifyStatusKodResponse(200, response);
        testGenreVerify.verifyGenresForAuthor(newGenre, newGenre2, response);
    }


    @Test(description = "verify add new genre with Invalid Description , Negative test")
    public void testAddBadDescriptionGenre() {
        /*start check*/
        Genre newGenre = genreService.createObjGenre();
        newGenre.setGenreName("");//set bad Name
        Allure.addAttachment("My objNewGenre", newGenre.toString());
        genreList.add(newGenre);
        Response response = genreService.createGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(400, response); // not create
        testGenreVerify.verifyAddBadNewGenre(newGenre);// not in BD
    }


    @Test(description = "verify add new genre without Invalid Id , Negative test")
    public void testAddBadGenre() {
        /*start check*/
        Genre newGenre = genreService.createObjGenreId(invalidId);
        Response response = genreService.createGenre(newGenre);
        genreList.add(newGenre);
        testGenreVerify.verifyStatusKodResponse(400, response); // not create
        testGenreVerify.verifyAddBadNewGenre(newGenre);//not in BD
    }


    @Test(description = "verify add new genre")
    public void testAddGenre() {
        Genre newGenre = genreService.createObjGenre();
        genreList.add(newGenre);
        //    Genre newGenreForDeleteAfterTest = (Genre) newGenre.clone();
        /*start check*/
        Response response = genreService.createGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(201, response);
        testGenreVerify.verifyAddNewGenre(newGenre, response);
    }


    @Test(description = "verify add The Same genre")
    public void testAddTheSameGenre() {
        Genre newGenre = genreService.createObjGenre();
        genreList.add(newGenre);
        Response response = genreService.createGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(201, response);
        /*start check*/
        Response responseTheSameGenre = genreService.createGenre(newGenre); // test add more of The same genre
        testGenreVerify.verifyStatusKodResponse(409, responseTheSameGenre); // not create
        newGenre.setGenreId(genreService.getGenresMyId()); //set new Id Genre, description The Same
        genreList.add(newGenre);// for clear after test
        responseTheSameGenre = genreService.createGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(409, responseTheSameGenre); // not create
        testGenreVerify.verifyAddBadNewGenre(newGenre);// not in BD
    }


    @Test(description = "verify update genre")
    public void testUpdateGenre() {
        int newId = genreService.getGenresMyId();
        Genre newGenre = genreService.createObjGenreId(newId);
        genreList.add(newGenre);
        Response response = genreService.createGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(201, response); //prepare
        /*start check*/
        int newAdd = newId + 5;
        newGenre.setGenreName("Test Name for testing" + String.valueOf(newAdd));
        newGenre.setGenreDescription("Test Test");
        genreList.add(newGenre);
        response = genreService.updateGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(200, response); // clear tetsting data
        testGenreVerify.verifyUpdateGenre(newGenre, response);
    }


    @Test(description = "verify update genre invalid Id")
    public void testUpdateGenreInvalidId() {
        int newId = genreService.getGenresMyId();
        Genre newGenre = genreService.createObjGenreId(newId);
        genreList.add(newGenre);
        Response response = genreService.createGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(201, response); //prepare
        /*start check*/
        newGenre.setGenreId(invalidId);
        genreList.add(newGenre);
        response = genreService.updateGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(400, response);
        newGenre.setGenreId(newId); //return Normal Id Genre
        testGenreVerify.verifyBadUpdateGenre(newGenre);//check  for normal ID
    }


    @Test(description = "verify delete genre")
    public void deleteGenre() {
        Genre newGenre = genreService.createObjGenre();
        genreList.add(newGenre);
        Response response = genreService.createGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(201, response);  //prepeare test genre
        response = genreService.deleteGenre(newGenre);
        testGenreVerify.verifyStatusKodResponse(204, response); // clear tetsting data
        testGenreVerify.verifyDeleteGenre(newGenre, response);
    }

    @Test(description = "create genre from Yurii")
    public void testGetGenreByHTTPClient() {
        Genre newGenre = genreService.createObjGenre();
        genreList.add(newGenre);
        BaseResponse<Genre> response = genreService.createGenreFromYurii(newGenre);
        testGenreVerify.verifyStatusKodResponseFromYurii(201, response);  // prepare testing data
    }

}

import bussinesObject.Author;
import bussinesObject.Book;
import bussinesObject.Genre;
import io.restassured.response.Response;
import org.testng.annotations.Test;
//import service.AuthorService;
import service.AuthorService;
import service.BookService;
import service.GenreService2;
//import verifyList.AuthorVerifyTests;
import verifyList.AuthorVerifyTests2;

public class AuthorTests {

    AuthorService authorService = new AuthorService();

    AuthorVerifyTests2 authorVerifyTests = new AuthorVerifyTests2();
    GenreService2 genreService = new GenreService2();
    BookService bookService = new BookService();


    @Test(description = "verify add new author")
    public void testAddNewAuthor() {
        Author newFirstAuthor = authorService.getTestingAuthor();
        Response responseFirstAuthor = authorService.createAuthor(newFirstAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, responseFirstAuthor);
        authorVerifyTests.verifyAddNewAuthor(newFirstAuthor, responseFirstAuthor);

        Response responseTheSameAuthor = authorService.createAuthor(newFirstAuthor);
        authorVerifyTests.verifyStatusKodResponse(409, responseTheSameAuthor);// test add more of The same genre

        int newIdAuthor = newFirstAuthor.getAuthorId();
        Author newAuthorWithExistId = authorService.getTestingAuthor();
//        newAuthorWithExistId.setAuthorId(newIdAuthor); //set Id First Author
        Response response = authorService.createAuthor(newAuthorWithExistId);
//        authorVerifyTests.verifyStatusKodResponse(409, response);// test add more of The same
        newIdAuthor = newIdAuthor + 1;
        newFirstAuthor.setAuthorId(newIdAuthor); //set new Id Author, description The Same
        responseTheSameAuthor = authorService.createAuthor(newFirstAuthor);
        authorVerifyTests.verifyStatusKodResponse(409, responseTheSameAuthor);// test add more of The same genre
        authorVerifyTests.verifyCheckIdInBDNotExists(newFirstAuthor); // check Id in Bd (not exists)
        /*end checking*/
        newFirstAuthor.setAuthorId(newAuthorWithExistId.getAuthorId()); //return IdFirstAuthor
        response = authorService.deleteAuthor(newFirstAuthor);  //delete testing data after Test
        authorVerifyTests.verifyStatusKodResponse(204, response);
    }

    @Test(description = "verify delete author")
    public void deleteAuthor() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        /*start checking*/
        response = authorService.deleteAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(204, response);
        authorVerifyTests.verifyCheckIdInBDNotExists(newAuthor);// check Get //name method
        /*end checking*/
    }

    @Test(description = "verify update new author")
    public void testUpdateAuthor() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        /*start checking*/
        int authorId = newAuthor.getAuthorId();
        newAuthor = authorService.updateAuthorForTestAuthor(newAuthor); //update info into class Author
        response = authorService.updateAuthor(newAuthor);
        authorVerifyTests.verifyUpdateAuthor(newAuthor, response);

        newAuthor.setAuthorId(-1); // const Invali AthorId
        response = authorService.updateAuthor(newAuthor);

        authorVerifyTests.verifyStatusKodResponse(400, response);// test add more of The same genre
        newAuthor.setAuthorId(authorId); //return Normal Id Genre
        authorVerifyTests.verifyIdAuthorExists(newAuthor);//check  for normal ID
        /*end checking*/
        response = authorService.deleteAuthor(newAuthor);  //delete testing data after Test
        authorVerifyTests.verifyStatusKodResponse(204, response);
    }


    @Test(description = "get all authors")
    public void testGetAllAuthors() {
        Response response = authorService.getAllAuthors();
        authorVerifyTests.verifyStatusKodResponse(200, response);
    }

    @Test(description = "get author by authorId")
    public void testGetAuthorsById() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        /*start checking*/
        response = authorService.getAuthorId(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(200, response); //prepare test
        authorVerifyTests.verifyAuthorByFind(newAuthor, response);
        /*end checking*/
        response = authorService.deleteAuthor(newAuthor);  //delete testing data after Test
        authorVerifyTests.verifyStatusKodResponse(204, response);
    }


    @Test(description = "get authors by search Name")
    public void testGetAuthorBySearchName() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        /*start checking*/
        response = authorService.getAuthorSearchList(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(200, response); //prepare test
        authorVerifyTests.verifyAuthorBySearch(newAuthor, response);///!
        /*end checking*/
        response = authorService.deleteAuthor(newAuthor);  //delete testing data after Test
        authorVerifyTests.verifyStatusKodResponse(204, response);
    }

    @Test(description = "get Author of special Book")
    public void testGetAuthorByBookId() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        Book newBook = bookService.getTestingBook();
        bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        /*start checking*/


        response = authorService.getAuthorIdForBookId(newBook);
        authorVerifyTests.verifyStatusKodResponse(200, response);
        authorVerifyTests.verifyAuthorByFind(newAuthor, response);
        /*end checking*/
        response = bookService.deleteBook(newBook); // clear test data
        authorVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test data
        authorVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        authorVerifyTests.verifyStatusKodResponse(204, response);
    }


    @Test(description = "get all Authors in special Genre")
    public void testGetGenreByGenresForAuthor() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        Author newAuthor2 = authorService.getTestingAuthor();
        response = authorService.createAuthor(newAuthor2);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        Book newBook = bookService.getTestingBook();
        bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        Book newBook2 = bookService.getTestingBook();
        bookService.createBook(newBook2, newAuthor2.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        /*start checking*/
        response = authorService.getAuthorsForGenreId(newGenre);
        authorVerifyTests.verifyStatusKodResponse(200, response);
        authorVerifyTests.verifyAuthorsForGenre(newAuthor, newAuthor2, response);
        /*end checking*/
        response = bookService.deleteBook(newBook); // clear test data
        authorVerifyTests.verifyStatusKodResponse(204, response);
        response = bookService.deleteBook(newBook2); // clear test data
        authorVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test data
        authorVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        authorVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor2); //clear test data
        authorVerifyTests.verifyStatusKodResponse(204, response);
    }

}

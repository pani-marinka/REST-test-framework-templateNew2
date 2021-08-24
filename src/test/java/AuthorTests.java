import bussinesObject.Author;
import bussinesObject.Book;
import bussinesObject.Genre;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
//import service.AuthorService;
import service.AuthorService;
import service.BookService;
import service.GenreService2;
//import verifyList.AuthorVerifyTests;
import verifyList.AuthorVerifyTests2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AuthorTests {

    AuthorService authorService = new AuthorService();

    AuthorVerifyTests2 authorVerifyTests = new AuthorVerifyTests2();
    GenreService2 genreService = new GenreService2();
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
                authorVerifyTests.verifyStatusKodResponse(204, response); //  check deleting
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
                authorVerifyTests.verifyStatusKodResponse(204, response); //  check deleting
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
                authorVerifyTests.verifyStatusKodResponse(204, response); //  check deleting
            }
            iterAuthor.remove();
        }
        System.out.println("Size listAuthor after clear: " + authorList.size());

    }


    @Test(description = "verify add new author")
    public void testAddNewAuthor() {
        Author newFirstAuthor = authorService.getTestingAuthor();
        authorList.add(newFirstAuthor);
        Response responseFirstAuthor = authorService.createAuthor(newFirstAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, responseFirstAuthor);
        authorVerifyTests.verifyAddNewAuthor(newFirstAuthor, responseFirstAuthor);
    }


    @Test(description = "verify add new  the same author")
    public void testAddNewTheSameAuthor() {
        Author newFirstAuthor = authorService.getTestingAuthor();
        authorList.add(newFirstAuthor);
        Response responseFirstAuthor = authorService.createAuthor(newFirstAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, responseFirstAuthor);
        authorVerifyTests.verifyAddNewAuthor(newFirstAuthor, responseFirstAuthor);
        /* start checking*/
        Response responseTheSameAuthor = authorService.createAuthor(newFirstAuthor);
        authorVerifyTests.verifyStatusKodResponse(409, responseTheSameAuthor);// test add more of The same genre
    }

    /*not confirmed by specification*/
    @Test(description = "verify add new  existing author(not confirmed by specification)")
    public void testAddNewExistAuthor() {
        Author newFirstAuthor = authorService.getTestingAuthor();
        authorList.add(newFirstAuthor);
        Response response = authorService.createAuthor(newFirstAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response);
        authorVerifyTests.verifyAddNewAuthor(newFirstAuthor, response);
        /* start checking*/
        int newIdAuthor = authorService.getAuthorMyId();  //newFirstAuthor.getAuthorId();
        newFirstAuthor.setAuthorId(newIdAuthor);
        authorList.add(newFirstAuthor);
        response = authorService.createAuthor(newFirstAuthor);
        authorVerifyTests.verifyStatusKodResponse(409, response);// test add more of The same genre
        authorVerifyTests.verifyCheckId(newFirstAuthor); // check Id in Bd (expected not in BD)
    }

    @Test(description = "verify delete author")
    public void deleteAuthor() {
        Author newAuthor = authorService.getTestingAuthor();
        authorList.add(newAuthor);
        Response response = authorService.createAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        /*start checking*/
        response = authorService.deleteAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(204, response);
        authorVerifyTests.verifyCheckId(newAuthor);// check Get //name method
    }

    @Test(description = "verify update author")
    public void testUpdateAuthor() {
        Author newAuthor = authorService.getTestingAuthor();
        authorList.add(newAuthor);
        Response response = authorService.createAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        /*start checking*/
        newAuthor = authorService.updateAuthorForTestAuthor(newAuthor); //update info Author
        response = authorService.updateAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(200, response);
        authorVerifyTests.verifyUpdateAuthor(newAuthor, response);

    }


    @Test(description = "verify update author invalid id")
    public void testUpdateAuthorInvalidId() {
        Author newAuthor = authorService.getTestingAuthor();
        authorList.add(newAuthor);
        Response response = authorService.createAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        /*start checking*/
        newAuthor.setAuthorId(invalidId);
        authorList.add(newAuthor);
        response = authorService.updateAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(400, response);
    }


    @Test(description = "get all authors")
    public void testGetAllAuthors() {
        Response response = authorService.getAllAuthors();
        authorVerifyTests.verifyStatusKodResponse(200, response);
    }

    @Test(description = "get author by authorId")
    public void testGetAuthorsById() {
        Author newAuthor = authorService.getTestingAuthor();
        authorList.add(newAuthor);
        Response response = authorService.createAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        /*start checking*/
        response = authorService.getAuthorId(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(200, response); //prepare test
        authorVerifyTests.verifyAuthorByFind(newAuthor, response);
        /*end checking*/
    }


    @Test(description = "get authors by search Name")
    public void testGetAuthorBySearchName() {
        Author newAuthor = authorService.getTestingAuthor();
        authorList.add(newAuthor);
        Response response = authorService.createAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        /*start checking*/
        response = authorService.getAuthorSearchList(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(200, response); //prepare test
        authorVerifyTests.verifyAuthorBySearch(newAuthor, response);///!
    }

    @Test(description = "get Author of special Book")
    public void testGetAuthorByBookId() {
        Author newAuthor = authorService.getTestingAuthor();
        authorList.add(newAuthor);
        Response response = authorService.createAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        Genre newGenre = genreService.createObjGenre();
        genreList.add(newGenre);
        response = genreService.createGenre(newGenre); //prepeare test Genre
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        Book newBook = bookService.getTestingBook();
        bookList.add(newBook);
        bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        /*start checking*/
        response = authorService.getAuthorIdForBookId(newBook);
        authorVerifyTests.verifyStatusKodResponse(200, response);
        authorVerifyTests.verifyAuthorByFind(newAuthor, response);
    }


    @Test(description = "get all Authors in special Genre")
    public void testGetGenreByGenresForAuthor() {
        Author newAuthor = authorService.getTestingAuthor();
        authorList.add(newAuthor);
        Response response = authorService.createAuthor(newAuthor);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        Author newAuthor2 = authorService.getTestingAuthor();
        authorList.add(newAuthor2);
        response = authorService.createAuthor(newAuthor2);
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        Genre newGenre = genreService.createObjGenre();
        genreList.add(newGenre);
        response = genreService.createGenre(newGenre); //prepeare test Genre
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        Book newBook = bookService.getTestingBook();
        bookList.add(newBook);
        bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        Book newBook2 = bookService.getTestingBook();
        bookList.add(newBook2);
        bookService.createBook(newBook2, newAuthor2.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        authorVerifyTests.verifyStatusKodResponse(201, response); //prepare test
        /*start checking*/
        response = authorService.getAuthorsForGenreId(newGenre);
        authorVerifyTests.verifyStatusKodResponse(200, response);
        authorVerifyTests.verifyAuthorsForGenre(newAuthor, newAuthor2, response);
           }

}

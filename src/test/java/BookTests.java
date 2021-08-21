import bussinesObject.Author;
import bussinesObject.Book;
import bussinesObject.Genre;
import io.restassured.response.Response;
import org.testng.annotations.Test;
//import service.AuthorService;
import service.AuthorService;
import service.BookService;
import service.GenreService2;
import verifyList.BookVerifyTests;

public class BookTests {

    BookService bookService = new BookService();
    GenreService2 genreService = new GenreService2();

    AuthorService authorService = new AuthorService();

    BookVerifyTests bookVerifyTests = new BookVerifyTests();

    @Test(description = "get all books")
    public void testGetAllBooks() {
        Response response = bookService.getAllBooks();
        bookVerifyTests.verifyStatusKodResponse(200, response);
    }

    @Test(description = "test Add New Book")
    public void testAddBook() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepeare test  Author
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        bookVerifyTests.verifyStatusKodResponse(201, response);
        /*start  checking*/
        Book newBook = bookService.getTestingBook();
        response = bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId());
        bookVerifyTests.verifyAddNewBook(newBook, response);
        /*end checking*/
        response = bookService.deleteBook(newBook); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
    }

    @Test(description = "verify delete book")
    public void deleteBook() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepeare test  Author
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Book newBook = bookService.getTestingBook();
        bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        bookVerifyTests.verifyStatusKodResponse(201, response);
        /*start checking*/
        response = bookService.deleteBook(newBook);
        bookVerifyTests.verifyStatusKodResponse(204, response);
        bookVerifyTests.verifyDeleteBookInBD(newBook, response);
        /*end checking*/
        response = genreService.deleteGenre(newGenre); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
    }

    @Test(description = "verify update new book")
    public void testUpdateGenre() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepeare test  Author
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Book newBook = bookService.getTestingBook();
        response = bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        bookVerifyTests.verifyStatusKodResponse(201, response);
        /*start checking*/
        int bookId = newBook.getBookId();
        Book newBook2 = bookService.getTestingBookforUpdate(newBook.getBookId()); //return book with IdFirstBook
        response = bookService.updateBook(newBook2);
        bookVerifyTests.verifyStatusKodResponse(200, response);
        bookVerifyTests.verifyUpdateBook(newBook2, response);
        newBook2.setBookId(-1);
        response = bookService.updateBook(newBook2);
        newBook2.setBookId(bookId); //return Normal Id Genre
        bookVerifyTests.verifyStatusKodResponse(400, response);
        bookVerifyTests.verifyBadUpdateBook(newBook2);//check  for normal ID, not badId in BD
        /*end checking*/
        response = bookService.deleteBook(newBook); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
    }


    @Test(description = "get book by bookId")
    public void testGetBookById() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepeare test  Author
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Book newBook = bookService.getTestingBook();
        response = bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        bookVerifyTests.verifyStatusKodResponse(201, response);
        /*start checking*/
        response = bookService.getBookId(newBook);
        bookVerifyTests.verifyStatusKodResponse(200, response);
        bookVerifyTests.verifyBookByFind(newBook, response);
        /*end checking*/
        response = bookService.deleteBook(newBook); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
    }


    @Test(description = "get book by search Name")
    public void testGetBookBySearchName() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepeare test  Author
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Book newBook = bookService.getTestingBook();
        response = bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        bookVerifyTests.verifyStatusKodResponse(201, response);
        /*start checking*/
        response = bookService.getBookSearch(newBook);
        bookVerifyTests.verifyStatusKodResponse(200, response);
        bookVerifyTests.verifyBookBySearch(response, newBook);
        /*end checking*/
        response = bookService.deleteBook(newBook); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
    }

    @Test(description = "get all Books of Genre")
    public void testGetAllBooksOfGenre() {  //get all Books of special Genre
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepeare test  Author
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Book newBook = bookService.getTestingBook();
        response = bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Book newBook2 = bookService.getTestingBook();
        bookService.createBook(newBook2, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        bookVerifyTests.verifyStatusKodResponse(201, response);
        /*start checking*/
        response = bookService.getBooksOfGenre(newGenre);
        bookVerifyTests.verifyStatusKodResponse(200, response);
        bookVerifyTests.verifyBooksFromList(response, newBook, newBook2);
        /*end checking*/
        response = bookService.deleteBook(newBook); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = bookService.deleteBook(newBook2); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
    }


    @Test(description = "get all Books of Author")
    public void testGetAllBooksOfAuthor() {  //get all Books of special Author
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepeare test  Author
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Book newBook = bookService.getTestingBook();
        response = bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Book newBook2 = bookService.getTestingBook();
        bookService.createBook(newBook2, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        bookVerifyTests.verifyStatusKodResponse(201, response);
        /*start checking*/

        response = bookService.getBooksOfAuthor(newAuthor);
        bookVerifyTests.verifyStatusKodResponse(200, response);
        bookVerifyTests.verifyBooksFromList(response, newBook, newBook2);

        /*end checking*/
        response = bookService.deleteBook(newBook); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = bookService.deleteBook(newBook2); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
    }


    @Test(description = "get all Books of special Author in special Genre")
    public void testGetAllBooksOfAuthorOfGenre() {  //get all Books of special Author
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepeare test  Author
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Book newBook = bookService.getTestingBook();
        response = bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Book newBook2 = bookService.getTestingBook();
        bookService.createBook(newBook2, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        bookVerifyTests.verifyStatusKodResponse(201, response);
        /*start checking*/
        response = bookService.getBooksOfAuthorofGenre(newAuthor, newGenre);
        bookVerifyTests.verifyStatusKodResponse(200, response);
        bookVerifyTests.verifyBooksFromList(response, newBook, newBook2);
        /*end checking*/
        response = bookService.deleteBook(newBook); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = bookService.deleteBook(newBook2); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
    }

}

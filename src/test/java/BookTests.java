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
import verifyList.BookVerifyTests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookTests {

    BookService bookService = new BookService();
    GenreService2 genreService = new GenreService2();
    AuthorService authorService = new AuthorService();
    BookVerifyTests bookVerifyTests = new BookVerifyTests();
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
                bookVerifyTests.verifyStatusKodResponse(204, response); //  check deleting
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
                bookVerifyTests.verifyStatusKodResponse(204, response); //  check deleting
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
                bookVerifyTests.verifyStatusKodResponse(204, response); //  check deleting
            }
            iterAuthor.remove();
        }
        System.out.println("Size listAuthor after clear: " + authorList.size());

    }

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
        genreList.add(newGenre);
        authorList.add(newAuthor);
        /*start  checking*/
        Book newBook = bookService.getTestingBook();
        bookList.add(newBook);// for clear after test
        response = bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId());
        bookVerifyTests.verifyAddNewBook(newBook, response);
        /*end checking*/

    }

    @Test(description = "test Add New Book with exist Id")
    public void testAddNewBookWithExistId() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepeare test  Author
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        bookVerifyTests.verifyStatusKodResponse(201, response);
        genreList.add(newGenre);
        authorList.add(newAuthor);
        Book newBook = bookService.getTestingBook();
        bookList.add(newBook);// for clear after test
        response = bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId());
        bookVerifyTests.verifyStatusKodResponse(201, response);
        /*start  checking*/
        int existId = newBook.getBookId();
        Book newBook2 = bookService.getTestingBook();
        newBook2.setBookId(existId);
        bookList.add(newBook2);
        response = bookService.createBook(newBook2, newAuthor.getAuthorId(), newGenre.getGenreId());
        bookVerifyTests.verifyStatusKodResponse(409, response);
        /*end checking*/
    }

    @Test(description = "test Add New Book with Invalid Id")
    public void testAddNewBookWithInvalidId() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepeare test  Author
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        bookVerifyTests.verifyStatusKodResponse(201, response);
        genreList.add(newGenre);
        authorList.add(newAuthor);
        /*start checking*/
        Book newBook = bookService.getTestingBook();
        newBook.setBookId(invalidId);
        bookList.add(newBook); // for clear test data
        response = bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId());
        bookVerifyTests.verifyStatusKodResponse(400, response);
        bookVerifyTests.verifyIdBook(newBook);// not badId in BD
        /*end checking*/
    }


    @Test(description = "verify delete book")
    public void deleteBook() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepare test  Author
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Book newBook = bookService.getTestingBook();
        bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepare test Book
        bookVerifyTests.verifyStatusKodResponse(201, response);
        genreList.add(newGenre);
        authorList.add(newAuthor);
        bookList.add(newBook);
        /*start checking*/
        response = bookService.deleteBook(newBook);
        bookVerifyTests.verifyStatusKodResponse(204, response);
        bookVerifyTests.verifyDeleteBookInBD(newBook, response);
    }

    @Test(description = "verify update book")
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
        genreList.add(newGenre);
        authorList.add(newAuthor);
        bookList.add(newBook);
        /*start checking*/
        int bookId = newBook.getBookId();
        Book newBook2 = bookService.getTestingBookforUpdate(newBook.getBookId()); //return book with IdFirstBook
        bookList.add(newBook2);
        response = bookService.updateBook(newBook2);
        bookVerifyTests.verifyStatusKodResponse(200, response);
        bookVerifyTests.verifyUpdateBook(newBook2, response);
    }


    @Test(description = "verify update book Invalid Id")
    public void testUpdateGenreInvalidId() {
        Author newAuthor = authorService.getTestingAuthor();
        Response response = authorService.createAuthor(newAuthor);  //prepeare test  Author
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Genre newGenre = genreService.createObjGenre();
        response = genreService.createGenre(newGenre); //prepeare test Genre
        bookVerifyTests.verifyStatusKodResponse(201, response);
        Book newBook = bookService.getTestingBook();
        response = bookService.createBook(newBook, newAuthor.getAuthorId(), newGenre.getGenreId()); //prepeare test Book
        bookVerifyTests.verifyStatusKodResponse(201, response);
        genreList.add(newGenre);
        authorList.add(newAuthor);
        bookList.add(newBook);
        /*start checking*/

        int normalId = newBook.getBookId();
        newBook.setBookId(invalidId);
        bookList.add(newBook); // for clear test data
        response = bookService.updateBook(newBook);
        bookVerifyTests.verifyStatusKodResponse(400, response);
        bookVerifyTests.verifyIdBook(newBook);// invalidId not in BD
        newBook.setBookId(normalId); //return Normal Id Genre
       bookVerifyTests.verifyBadUpdateBook(newBook);//check  for normal ID, in BD
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
        genreList.add(newGenre);
        authorList.add(newAuthor);
        bookList.add(newBook);
        /*start checking*/
        response = bookService.getBookId(newBook);
        bookVerifyTests.verifyStatusKodResponse(200, response);
        bookVerifyTests.verifyBookByFind(newBook, response);
        /*end checking
        response = bookService.deleteBook(newBook); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);*/
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
        genreList.add(newGenre);
        authorList.add(newAuthor);
        bookList.add(newBook);
        /*start checking*/
        response = bookService.getBookSearch(newBook);
        bookVerifyTests.verifyStatusKodResponse(200, response);
        bookVerifyTests.verifyBookBySearch(response, newBook);
        /*end checking
        response = bookService.deleteBook(newBook); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);*/
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
        genreList.add(newGenre);
        authorList.add(newAuthor);
        bookList.add(newBook);
        bookList.add(newBook2);
        /*start checking*/
        response = bookService.getBooksOfGenre(newGenre);
        bookVerifyTests.verifyStatusKodResponse(200, response);
        bookVerifyTests.verifyBooksFromList(response, newBook, newBook2);
        /*end checking
        response = bookService.deleteBook(newBook); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = bookService.deleteBook(newBook2); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);*/
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
        genreList.add(newGenre);
        authorList.add(newAuthor);
        bookList.add(newBook);
        bookList.add(newBook2);
        /*start checking*/

        response = bookService.getBooksOfAuthor(newAuthor);
        bookVerifyTests.verifyStatusKodResponse(200, response);
        bookVerifyTests.verifyBooksFromList(response, newBook, newBook2);

        /*end checking
        response = bookService.deleteBook(newBook); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = bookService.deleteBook(newBook2); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);*/
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
        genreList.add(newGenre);
        authorList.add(newAuthor);
        bookList.add(newBook);
        bookList.add(newBook2);
        /*start checking*/
        response = bookService.getBooksOfAuthorofGenre(newAuthor, newGenre);
        bookVerifyTests.verifyStatusKodResponse(200, response);
        bookVerifyTests.verifyBooksFromList(response, newBook, newBook2);
        /*end checking
        response = bookService.deleteBook(newBook); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = bookService.deleteBook(newBook2); // clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = genreService.deleteGenre(newGenre); //clear test
        bookVerifyTests.verifyStatusKodResponse(204, response);
        response = authorService.deleteAuthor(newAuthor); //clear test data
        bookVerifyTests.verifyStatusKodResponse(204, response);*/
    }

}

package web;

import model.Book;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *Class defines an online data miner for the Amazon website. Used to extract information about a book, given its ISBN number
 */
public class BookMiner {

    //All book info extracted from Amazon site
    private final String SOURCE_SITE = "https://www.amazon.com/dp/";

    private String URL; //URL of Amazon web page
    private WebPageReader wpReader; //Gets and reads HTML from a URL

    //Regex patterns for Amazon web pages
    private final String pricePattern = "<span class=\"a-size-medium a-color-price offer-price a-text-normal\">\\$(\\d*\\.\\d*)<\\/span>";
    private final String titlePattern = "<span id=\"productTitle\" class=\"a-size-large\">(.*)</span>";
    private final String authorPattern = "href=\"/.+/e/[A-Z0-9]+/ref=dp_byline_cont_book_[\\d]+\">(.+)</a>|href=\"/s/ref=dp_byline_sr_book_\\d.*\">(.+)</a>";
    private final String publisherPattern = "<li><b>Publisher:</b> (.*) \\(.*(\\d\\d\\d\\d)\\)</li>";
    private final String yearPattern = "\\(.*(\\d{4}).*\\)";
    private final String imagePattern = "var iUrl = \"(.*)\";";

    //Pattern Objects
    Pattern priceRegex;
    Pattern titleRegex;
    Pattern authorRegex;
    Pattern publisherRegex;
    Pattern yearRegex;
    Pattern imageRegex;

    public BookMiner() {
        //Compile pattern objects
        priceRegex = Pattern.compile(pricePattern);
        titleRegex = Pattern.compile(titlePattern);
        authorRegex = Pattern.compile(authorPattern);
        publisherRegex = Pattern.compile(publisherPattern);
        yearRegex = Pattern.compile(yearPattern);
        imageRegex = Pattern.compile(imagePattern);
    }

    public Book getBookInfo(String ISBN) {

        this.URL = SOURCE_SITE+ISBN;
        this.wpReader = new WebPageReader(URL);

        Book book = new Book(ISBN); //Book to be returned
        Matcher matcher; //Matcher to hold matched publisher

        try {

            String line = wpReader.readLine(); //Line of HTML code
            while (line != null) {
                //Match price
                matcher = priceRegex.matcher(line);
                if (matcher.find()) {
                    String price = matcher.group(1);
                    book.setPrice(price);
                }

                //Match Title
                matcher = titleRegex.matcher(line);
                if (matcher.find()) {
                    String title = matcher.group(1);
                    book.setTitle(title);
                }

                //Match Author
                matcher = authorRegex.matcher(line);
                if (matcher.find()) {
                    String author;
                    if (matcher.group(1) == (null)) {
                        author = matcher.group(2);
                    }
                    else {
                        author = matcher.group(1);
                    }
                    book.setAuthor(author);
                }

                //Match Publisher (Edition and Year on the same line
                matcher = publisherRegex.matcher(line);
                if (matcher.find()) {

                    String publisher = matcher.group(1);
                    if(publisher.contains(";")) {
                        int i = publisher.lastIndexOf(";");
                        publisher = publisher.substring(0, i);
                        if (publisher.contains("'")) {
                            publisher = publisher.replace("'", "''");
                        }
                        book.setPublisher(publisher);
                    }
                    book.setPublisher(publisher);

                    String year = matcher.group(2);
                    book.setYear(year);
                }

                //Match Image Link
                matcher = imageRegex.matcher(line);
                if (matcher.find()) {
                    String imageLink = matcher.group(1);
                    book.setImageLink(imageLink);
                }
                line = wpReader.readLine();
            } // while
        } catch (IOException e) {
            e.printStackTrace();
        }
       return book; //Returns book with complete info
    }
}

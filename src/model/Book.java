package model;

import paths.OutFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *Class defines a book object with its corresponding ISBN number, title, author, etc.
 */
public class Book {

    //Fields that define a book
    private String ISBN;
    private String title;
    private String author;
    private String publisher;
    private String year;
    private String price;
    private String imageLink;

    public Book(String ISBN) {
        this.ISBN = ISBN;
    }

    /*
    * Prints fields of book
    * */
    public static void printFields(BufferedWriter writer) {
        try {

            writer.append("ISBN" + " | ");
            writer.append("Title" + " | ");
            writer.append("Author" + " | ");
            writer.append("Publisher" + " | ");
            writer.append("Year" + " | ");
            writer.append("Price");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * Prints info about the book
    * */
    public void printDetails(BufferedWriter writer) {
        try {
            writer.append(ISBN + " | ");
            writer.append(title + " | ");
            writer.append(author + " | ");
            writer.append(publisher + " | ");
            writer.append(year + " | ");
            writer.append(price);
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * Getter and setter methods
    * */

    public String getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title.replace("&amp;","&");
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if(this.author == null) {
            this.author = author;
        }
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}

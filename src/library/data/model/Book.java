package library.data.model;

public class Book {

	String isbn;
	String title;
	String author;
	String edition_number;
	String publisher;
	Integer price;
	Boolean available;
	
	public Book (String isbn, String title, String edition_number, 
			String publisher, Integer price, Boolean available) {
		this.isbn = isbn;
		this.title = title;
		this.edition_number = edition_number;
		this.publisher = publisher;
		this.price = price;
		this.available = available;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getEdition_number() {
		return edition_number;
	}

	public void setEdition_number(String edition_number) {
		this.edition_number = edition_number;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}
}

package nl.brianvermeer.ai.AiService;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class LibraryTools {

    @Tool("Get all available books")
    public List<Book> getBooks() {
        return List.of(
                new Book("The Great Gatsby", "F. Scott Fitzgerald", 123456789, "Fiction", 180),
                new Book("To Kill a Mockingbird", "Harper Lee", 987654321, "Fiction", 281),
                new Book("1984", "George Orwell", 123456789, "Dystopian", 328),
                new Book("Pride and Prejudice", "Jane Austen", 123456789, "Romance", 248),
                new Book("The Catcher in the Rye", "J.D. Salinger", 123456789, "Fiction", 277),
                new Book("The Hobbit", "J.R.R. Tolkien", 123456789, "Fantasy", 310),
                new Book("Fahrenheit 451", "Ray Bradbury", 123456789, "Dystopian", 158),
                new Book("Brave New World", "Aldous Huxley", 123456789, "Dystopian", 268),
                new Book("The Picture of Dorian Gray", "Oscar Wilde", 123456789, "Philosophical", 254)
        );
    }

    @Tool("Get all registered users")
    public List<User> getUsers() {
        return List.of(
                new User("John", "Doe", 30, "123 Main St", "555-1234", "LIB123"),
                new User("Jane", "Smith", 25, "456 Elm St", "555-5678", "LIB456"),
                new User("Alice", "Johnson", 28, "789 Oak St", "555-8765", "LIB789"),
                new User("Bob", "Brown", 35, "321 Pine St", "555-4321", "LIB321"),
                new User("Charlie", "Davis", 22, "654 Maple St", "555-6789", "LIB654"),
                new User("Diana", "Wilson", 27, "987 Cedar St", "555-3456", "LIB987")
        );
    }

    public record Book(String title, String author, int isbn, String genre, int pages) {
    }
    
    public record User(String firstName, String lastName, int age, String address, String phone, String libraryCardNumber) {
    }


}

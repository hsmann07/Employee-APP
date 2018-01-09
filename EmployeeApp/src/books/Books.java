package books;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author MANN
 */
public class Books {
    private String title, author, language,publisherCompany;
    private LocalDate publishedDate;
    private BigDecimal price;
    private boolean administrator;
    private Image image;

    public Books(String title, String author, String language, BigDecimal price, String publisherCompany, LocalDate publishedDate) {

        setTitle(title);
        setAuthor(author);
        setLanguage(language);
        setPrice(price);
        setPublisherCompany(publisherCompany);
        setPublishedDate(publishedDate);
         try
        {
            BufferedImage bufferedImage = ImageIO.read(new File("./src/images/DefaultBook.jpg"));
            image = SwingFXUtils.toFXImage(bufferedImage, null);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
       if(!title.isEmpty())
       {
           if(title.matches("[A-Z][a-zA-Z]*"))
            this.title = title;
        else
            throw new IllegalArgumentException("Title must start with an upper"
                                            + "case letter and only contain letters");
       }
       else 
           throw new IllegalArgumentException ("Title Cannot be left empty");
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if(!author.equals(""))
        {
        if(author.matches("[A-Z][a-zA-Z]*"))
            this.author = author;
        else
         throw new IllegalArgumentException("Author must start with an upper"
                                            + "case letter and only contain letters");
        }
        else 
            throw new IllegalArgumentException("Author Cannot be left Empty");
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
       if(language !="")
       {
           if(language.equals("English") || language.equals("French") || language.equals("Spanish"))
           this.language = language;
           else throw new IllegalArgumentException("Language must be English, French or Spanish");
       }
       else 
           throw new IllegalArgumentException("Their must be some language it should not be empty");
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if(price != null)
        {
            if(price.doubleValue() > 0 )
        this.price = price;
        else throw new IllegalArgumentException("Price must be greater than 0");
        }
        else 
           throw new IllegalArgumentException("Their must be some Price it should not be empty");
    }

 
    public String getPublisherCompany() {
        return publisherCompany;
    }

    public void setPublisherCompany(String publisherCompany) {
       if(!publisherCompany.isEmpty()) 
           this.publisherCompany = publisherCompany;
       else 
           throw new IllegalArgumentException("Their must be any Company to Create a new book");
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        if(publishedDate !=null){
            if(publishedDate.isBefore(LocalDate.now()) || publishedDate.equals(LocalDate.now()))
        this.publishedDate = publishedDate;
        else
            throw new IllegalArgumentException("Published Date cannot be after today's date");
        }
        else 
            throw new IllegalArgumentException("Date cannot be left blank");
    }
      public boolean isAdministrator() {
        return administrator;
    }
    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }
}

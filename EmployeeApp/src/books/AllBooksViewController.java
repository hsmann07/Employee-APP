/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package books;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author MANN
 */
public class AllBooksViewController implements Initializable {
    
    private ObservableList<Books> bookList = FXCollections.observableArrayList();
    
 @FXML private TableView<Books> booksTable;
 
 @FXML private TableColumn<Books, String> titleColumn;
 @FXML private TableColumn<Books, String> authorColumn;
 @FXML private TableColumn<Books, String> languageColumn;
 
 
 @FXML private Label totalPriceLabel;
 @FXML private Label totalNumberBooks;
 @FXML private TableColumn<Books, String> publisherCompanyColumn;
 @FXML private TableColumn<Books, LocalDate> publishedDateColumn;
 @FXML private TableColumn<Books, Double> priceColumn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("author"));
        languageColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("language"));
        publisherCompanyColumn.setCellValueFactory(new PropertyValueFactory<Books, String>("publisherCompany"));
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<Books, LocalDate>("publishedDate"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Books, Double>("price"));
        
        bookList.add(new Books("Title", "Author", "Spanish",BigDecimal.valueOf(12), "Company",LocalDate.of(1966, Month.MARCH, 15)));
        //load dummy data
            booksTable.setItems(bookList);
       
            if (bookList.size() == 1) {
                this.totalPriceLabel.setText("Total Price: $" + bookList.get(0).getPrice().setScale(2));
            }
            totalNumberBooks.setText("Total number of books: " + bookList.size());
            
    }    
      /**
     * This method will populate the list of Books 
     * @param newList
     */
    public void loadBooks(ObservableList<Books> newList)
    {
        newList.remove(0);
        this.bookList.addAll(newList);
        
            BigDecimal totalPrice = BigDecimal.valueOf(0);
            
            for (Books books: bookList) {
                totalPrice = totalPrice.add(books.getPrice().setScale(2));
            }
            
            this.totalPriceLabel.setText("Total Price: $" + totalPrice);
                       totalNumberBooks.setText("Total number of books: " + bookList.size());
    }
    
     public void createNewBookButtonPushed(ActionEvent event) throws IOException
    {
   
         //load a new scene
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CreateBookView.fxml"));
        Parent parent = loader.load();
        Scene newBookScene = new Scene(parent);
        
        //access the controller of the newBookScene and send over
        //the current list of Books
        CreateBookViewController controller = loader.getController();
        controller.initialData(bookList);
        
        //Get the current "stage" (aka window) 
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        //change the scene to the new scene
        stage.setTitle("Create new Book");
        stage.setScene(newBookScene);
        stage.show();
    }
}

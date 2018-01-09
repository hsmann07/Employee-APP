/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package books;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author MANN
 */
public class CreateBookViewController implements Initializable {

    private ObservableList<Books> books;
    private ObservableList<String> languageList =FXCollections.observableArrayList("English","French","Spanish");
    
    @FXML private TextField titleTextField;
    @FXML private TextField authorTextField;
    @FXML private TextField priceTextField;
    
    @FXML private ComboBox languageComboBox;
    @FXML private TextField publisherCompanyTextField;
    @FXML private DatePicker publishedDate;
     @FXML private Label errorMsg;
     
     @FXML private ImageView bookImage ;
    
    //Used for the file chooser
    private FileChooser fileChooser;
    private File imageFile;

     public void initialData(ObservableList<Books> listOfBooks)
    {
        books = listOfBooks;
    }
     
     public void createNewBookButtonPushed(ActionEvent event) throws IOException
    {
        String
         price = priceTextField.getText();
        if (priceTextField.getText().contains(".")) {
            if (priceTextField.getText().length() - priceTextField.getText().indexOf(".") > 2) {
            price = priceTextField.getText().substring(0, priceTextField.getText().indexOf(".") + 3);
            }
        }
        
        try
        {
        Books newBook = new Books(titleTextField.getText(), 
                                                    authorTextField.getText(), 
                                                    (String) languageComboBox.getValue(), 
                                                    BigDecimal.valueOf(Double.parseDouble(price)),
                                                    publisherCompanyTextField.getText(),
                                                    publishedDate.getValue());
        books.add(newBook);
        
        //load the .fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AllBooksView.fxml"));
        Parent parent = loader.load();
        
        //create the scene
        Scene scene = new Scene(parent);
        
        //access the controller of the new Scene and send over
        //the current list of Books
        AllBooksViewController controller = loader.getController();
        controller.loadBooks(books);
        
        //get the stage (window) from the event passed in
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        stage.setTitle("ALL BOOKS");
        stage.setScene(scene);
        stage.show();
        }
        catch (IllegalArgumentException e)
        {
            errorMsg.setText(e.getMessage());
        }
    }
       
    /**
     * This method will call load the all employee view
     */
    public void cancelButtonPushed(ActionEvent event) throws IOException
    {
         //load the .fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AllBooksView.fxml"));
        Parent parent = loader.load();
        
        //create the scene
        Scene scene = new Scene(parent);
        
        //get the stage (window) from the event passed in
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        stage.setTitle("ALL BOOKS");
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * This method will launch a FileChooser and load the image if accessible
     */
    public void chooseImageButtonPushed(ActionEvent event)
    {
        //get the stage to open a new window
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        //filter for only .jpg and .png files
        FileChooser.ExtensionFilter jpgFilter 
                = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter 
                = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");
        
        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);
        
        
        //Set to the user's picture directory or C drive if not available
        String userDirectoryString = System.getProperty("user.home")+"\\Pictures";
        File userDirectory = new File(userDirectoryString);
        
        if (!userDirectory.canRead())
            userDirectory = new File("c:/");
        
        fileChooser.setInitialDirectory(userDirectory);
        
        //open the file dialog window
        imageFile = fileChooser.showOpenDialog(stage);
        
        //ensure the user selected a file
        if (imageFile.isFile())
        {
            try
            {
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                Image image = SwingFXUtils.toFXImage(bufferedImage,null);
                bookImage.setImage(image);
            }
            catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    languageComboBox.setValue("English");
    languageComboBox.setItems(languageList);
    
    
    
     try
        {
            BufferedImage bufferedImage = ImageIO.read(new File("./src/images/DefaultBook.jpg"));
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            bookImage.setImage(image);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
}

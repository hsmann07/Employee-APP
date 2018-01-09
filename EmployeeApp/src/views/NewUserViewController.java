/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import models.Employee;

/**
 * FXML Controller class
 *
 * @author MANN
 */
public class NewUserViewController implements Initializable, ControllerClass {
@FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField phoneTextField;
    @FXML private DatePicker birthday;
    @FXML private Label headerLabel;
    @FXML private Label errMsgLabel;
    @FXML private ImageView imageView;
    
    private File imageFile;
    private boolean imageFileChanged;
    private Employee employee;
     //used for the passwords
    @FXML private PasswordField pwField;
    @FXML private PasswordField confirmPwField;
    
     public void saveEmployeeButtonPushed(ActionEvent event)
    {
            if (validPassword())
        {
            try
            {
                 if (employee != null) //we need to edit/update an existing volunteer
                {
                    updateEmployee();
                    employee.updateEmployeeInDB();
                }
                 else{ 
                if(imageFileChanged)
                {
                    employee = new Employee(firstNameTextField.getText(),lastNameTextField.getText(),phoneTextField.getText(),birthday.getValue(),imageFile,pwField.getText());
                }
                else{
                    employee = new Employee(firstNameTextField.getText(),lastNameTextField.getText(),phoneTextField.getText(),birthday.getValue(),pwField.getText());
                }
                errMsgLabel.setText("");
             employee.insertIntoDB();
                 }
             SceneChanger sc= new SceneChanger();
             sc.changeScenes(event, "EmployeeTableView.fxml", "All Emlpoyees");
                     
            }
            catch (Exception e)
            {
                errMsgLabel.setText(e.getMessage());
            }
        }
    }
      /**
     * This method will validate that the passwords match
     * 
     */
    public boolean validPassword()
    {
        if (pwField.getText().length() < 5)
        {
            errMsgLabel.setText("Passwords must be greater than 5 characters in length");
            return false;
        }
        
        if (pwField.getText().equals(confirmPwField.getText()))
            return true;
        else
            return false;
    }
     
      /**
     * When this button is pushed, a FileChooser object is launched to allow the user
     * to browse for a new image file.  When that is complete, it will update the 
     * view with a new image
     */
    public void chooseImageButtonPushed(ActionEvent event)
    {
        //get the Stage to open a new window (or Stage in JavaFX)
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        //Instantiate a FileChooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        //filter for .jpg and .png
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);
        
        //Set to the user's picture directory or user directory if not available
        String userDirectoryString = System.getProperty("user.home")+"\\Pictures";
        File userDirectory = new File(userDirectoryString);
        
        //if you cannot navigate to the pictures directory, go to the user home
        if (!userDirectory.canRead())
            userDirectory = new File(System.getProperty("user.home"));
        
        fileChooser.setInitialDirectory(userDirectory);
        
        //open the file dialog window
        File tmpImageFile = fileChooser.showOpenDialog(stage);
        
        if (tmpImageFile != null)
        {
            imageFile = tmpImageFile;
        
            //update the ImageView with the new image
            if (imageFile.isFile())
            {
                try
                {
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    Image img = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView.setImage(img);
                    imageFileChanged = true;
                }
                catch (IOException e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }
        
    }
    
    public void cancelButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
            sc.changeScenes(event, "EmployeeTableView.fxml", "All Employees");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         birthday.setValue(LocalDate.now().minusYears(18));
        imageFileChanged = false;
        errMsgLabel.setText("");
        
        //load the defautl image for the avatar
        try{
            imageFile = new File("./src/images/defaultEmployee.jpg");
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
            
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }    

    @Override
    public void preloadData(Employee employee) {
     this.employee = employee;
        this.firstNameTextField.setText(employee.getFirstName());
        this.lastNameTextField.setText(employee.getLastName());
        this.birthday.setValue(employee.getBirthday());
        this.phoneTextField.setText(employee.getPhoneNumber());
        this.headerLabel.setText("Edit Employee");
        
        //load the image 
        try{
            String imgLocation = ".\\src\\images\\" + employee.getImageFile().getName();
            imageFile = new File(imgLocation);
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            Image img = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(img);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }    
    }

    private void updateEmployee() throws IOException {
      employee.setFirstName(firstNameTextField.getText());
        employee.setLastName(lastNameTextField.getText());
        employee.setPhoneNumber(phoneTextField.getText());
        employee.setBirthday(birthday.getValue());
        employee.setImageFile(imageFile);
        employee.copyImageFile();    
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Employee;

/**
 * FXML Controller class
 *
 * @author MANN
 */
public class EmployeeTableViewController implements Initializable {

        @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, Integer> employeeIDColumn;
    @FXML private TableColumn<Employee, String> firstNameColumn;
    @FXML private TableColumn<Employee, String> lastNameColumn;
    @FXML private TableColumn<Employee, String> phoneColumn;
    @FXML private TableColumn<Employee, LocalDate> birthdayColumn;
    
    @FXML private Button editEmployeeButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editEmployeeButton.setDisable(true);
        employeeIDColumn.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("employeeID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("phoneNumber"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<Employee, LocalDate>("birthday"));
   try{
            loadEmployees();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }    
    
    public void editButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        Employee employee = this.employeeTable.getSelectionModel().getSelectedItem();
        NewUserViewController nuvc = new NewUserViewController();
        
        sc.changeScenes(event, "NewUserView.fxml", "Edit Employee", employee, nuvc);
    }
    public void employeeSelected()
    {
        editEmployeeButton.setDisable(false);
    }

    private void loadEmployees() throws SQLException{
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try{
            //1. connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Employee", "root", "");
            //2.  create a statement object
            statement = conn.createStatement();
            
            //3.  create the SQL query
            resultSet = statement.executeQuery("SELECT * FROM Employees");
            
            //4.  create volunteer objects from each record
            while (resultSet.next())
            {
                Employee newEmployee = new Employee(resultSet.getString("firstName"),
                                                       resultSet.getString("lastName"),
                                                       resultSet.getString("phoneNumber"),
                                                       resultSet.getDate("birthday").toLocalDate(),
                                                       resultSet.getString("passwords"));
                                                      
                newEmployee.setEmployeeID(resultSet.getInt("EmployeeID"));
                newEmployee.setImageFile(new File(resultSet.getString("imageFile")));
                
                employees.add(newEmployee);
            }
            
            employeeTable.getItems().addAll(employees);
            
        } catch (Exception e)
        {
            System.err.println(e);
        }
        finally
        {
            if (conn != null)
                conn.close();
            if(statement != null)
                statement.close();
            if(resultSet != null)
                resultSet.close();
        }
    }
    
    public void newEmployeeButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "NewUserView.fxml", "Create New Employee");
    }
    public void booksInventoryButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "/books/AllBooksView.fxml", "All Books");
    }
}

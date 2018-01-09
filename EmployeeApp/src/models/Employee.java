/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author MANN
 */
public class Employee {
     private String firstName, lastName, phoneNumber,password;
    private LocalDate birthday;
    private File imageFile;
    private int employeeID;
    private byte[] salt;
    

    public Employee(String firstName, String lastName, String phoneNumber, LocalDate birthday, String password) throws NoSuchAlgorithmException {
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setBirthday(birthday);
        setImageFile(new File("./src/images/defaultEmployee.jpg"));
        salt = PasswordGenerator.getSalt();
        this.password = PasswordGenerator.getSHA512Password(password, salt);
    }

    public Employee(String firstName, String lastName, String phoneNumber, LocalDate birthday, File imageFile, String password) throws IOException, NoSuchAlgorithmException {
        this(firstName, lastName, phoneNumber, birthday,password);
        setImageFile(imageFile);
        copyImageFile();
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        if (employeeID >= 0)
            this.employeeID = employeeID;
        else
            throw new IllegalArgumentException("employeeID must be >= 0");
    }
    

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("[2-9]\\d{2}[-.]?\\d{3}[-.]\\d{4}"))
            this.phoneNumber = phoneNumber;
        else
            throw new IllegalArgumentException("Phone numbers must be in the pattern NXX-XXX-XXXX");
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        int age = Period.between(birthday, LocalDate.now()).getYears();
        
        if (age >= 10 && age <= 100)
            this.birthday = birthday;
        else
            throw new IllegalArgumentException("Volunteers must be 10-100 years of age.");
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
    public String toString()
    {
        return String.format("%s %s is %d years old", firstName, lastName, Period.between(birthday, LocalDate.now()).getYears());
    }
    
     public void copyImageFile() throws IOException
    {
        //create a new Path to copy the image into a local directory
        Path sourcePath = imageFile.toPath();
        
        String uniqueFileName = getUniqueFileName(imageFile.getName());
        
        Path targetPath = Paths.get("./src/images/"+uniqueFileName);
        
        //copy the file to the new directory
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        
        //update the imageFile to point to the new File
        imageFile = new File(targetPath.toString());
    }

    private String getUniqueFileName(String oldFileName) {
    String newName;
        
        //create a Random Number Generator
        SecureRandom rng = new SecureRandom();
        
        //loop until we have a unique file name
        do
        {
            newName = "";
            
            //generate 32 random characters
            for (int count=1; count <=32; count++)
            {
                int nextChar;
                
                do
                {
                    nextChar = rng.nextInt(123);
                } while(!validCharacterValue(nextChar));
                
                newName = String.format("%s%c", newName, nextChar);
            }
            newName += oldFileName;
            
        } while (!uniqueFileInDirectory(newName));
        
        return newName;   
    }

    private boolean uniqueFileInDirectory(String fileName) {
            File directory = new File("./src/images/");
        
        File[] dir_contents = directory.listFiles();
                
        for (File file: dir_contents)
        {
            if (file.getName().equals(fileName))
                return false;
        }
        return true;    
    }

    private boolean validCharacterValue(int asciiValue) {
       
        //0-9 = ASCII range 48 to 57
        if (asciiValue >= 48 && asciiValue <= 57)
            return true;
        
        //A-Z = ASCII range 65 to 90
        if (asciiValue >= 65 && asciiValue <= 90)
            return true;
        
        //a-z = ASCII range 97 to 122
        if (asciiValue >= 97 && asciiValue <= 122)
            return true;
        
        return false;   
    }
    
    /**
     * This method will write the instance of the Volunteer into the database
     */
    public void insertIntoDB() throws SQLException
    {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        
        try
        {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Employee", "root", "");
            
            //2. Create a String that holds the query with ? as user inputs
            String sql = "INSERT INTO Employees (firstName, lastName, phoneNumber, birthday, imageFile, passwords, salt)"
                    + "VALUES (?,?,?,?,?,?,?)";
                    
            //3. prepare the query
            preparedStatement = conn.prepareStatement(sql);
            
            //4. Convert the birthday into a SQL date
            Date db = Date.valueOf(birthday);
                   
            //5. Bind the values to the parameters
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setDate(4, db);
            preparedStatement.setString(5, imageFile.getName());
             preparedStatement.setString(6, password);
            preparedStatement.setBlob(7, new javax.sql.rowset.serial.SerialBlob(salt));
            
            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (preparedStatement != null)
                preparedStatement.close();
            
            if (conn != null)
                conn.close();
        }
    }

    public void updateEmployeeInDB() throws SQLException {
      Connection conn = null;
        PreparedStatement preparedStatement = null;
        
        try{
            //1.  connect to the DB
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Employee", "root", "");
            
            //2.  create a String that holds our SQL update command with ? for user inputs
            String sql = "UPDATE Employees SET firstName = ?, lastName = ?, phoneNumber=?, birthday = ?, imageFile = ?"
                    + "WHERE employeeID = ?";
            
            //3. prepare the query against SQL injection
            preparedStatement = conn.prepareStatement(sql);
            
            //4.  convert the birthday into a date object
            Date bd = Date.valueOf(birthday);
            
            //5. bind the parameters
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setDate(4, bd);
            preparedStatement.setString(5, imageFile.getName());
            preparedStatement.setInt(6, employeeID);
            
            //6. run the command on the SQL server
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (conn != null)
                conn.close();
            if (preparedStatement != null)
                preparedStatement.close();
        }    
    }
}

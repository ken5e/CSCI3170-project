package carrentingsystem;

import java.util.Scanner;
import java.sql.*;
import java.util.InputMismatchException;
import carrentingsystem.Admin;
import carrentingsystem.Manager;
import carrentingsystem.User;
public class carRentingSystem
{
    public static void main(String args[])
    {   
        String  dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db0?allowMultiQueries=true";
        String dbUsername="Group0";
        String dbPassword="group0gp";
        Connection con=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection(dbAddress,dbUsername,dbPassword);
        }catch (ClassNotFoundException e){
            System.out.println("[Error] Java MySQL DB Driver not found!!");
            System.exit(0);
        }catch (SQLException e){
            System.out.println(e);
        }
        while(true)
        {
            System.out.println("Welcome to Car Renting System!\n");
            System.out.println("What kind of operations would you like to perform?");
            System.out.println("1. Operations for Administrator");
            System.out.println("2. Operations for User");
            System.out.println("3. Operations for Manager");
            System.out.println("4. Exit this program");
            System.out.print("Enter Your Choice: ");
            Scanner input = new Scanner(System.in);
            int choice;
            try{
                choice=input.nextInt();
            } catch (InputMismatchException e)
            {
                System.out.println("Please input the integer within 1 to 4");
                continue;
            }
            
            switch(choice)
            {
                case 1:
                    System.out.print("\033[H\033[2J");  
                    System.out.flush();
                    Admin.adminMainMenu(con);
                    break;
                case 2:
                    System.out.print("\033[H\033[2J");  
                    System.out.flush();
                    User.UserMainMenu(con);
                    break;
                case 3:
                    System.out.print("\033[H\033[2J");  
                    System.out.flush();
                    Manager manager = new Manager();
                    manager.managerMainMenu(con);
                    break;
                case 4:
                    System.exit(0);
                default:
                    System.out.println("Please input integer (1-4)");
            }
        }
    }
}

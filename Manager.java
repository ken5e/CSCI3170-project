package carrentingsystem;

import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.*;
import java.util.InputMismatchException;

// test
public class Manager{// extends MainMenu{
    
    // ---------------------------------Connection to the DBMS------------------------------------------
    final String  dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db0";
    final String dbUsername="Group0";
    final String dbPassword="group0gp";
    private Connection conn;

    public Manager() {
        ;
    }

    public void connect() throws ClassNotFoundException,SQLException
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection(dbAddress,dbUsername,dbPassword);
        }catch (ClassNotFoundException e){
            System.out.println("[Error] Java MySQL DB Driver not found!!");
            System.exit(0);
        }catch (SQLException e){
            System.out.println(e);
        }
    }
    // --------------------------------------------End---------------------------------------------------
    

    public void printAllUnreturnCar(String startDate, String endDate,Connection conn)
    {
        try{
            Date startDate_d = new SimpleDateFormat("dd/mm/yyyy").parse(startDate);
            Date endDate_d = new  SimpleDateFormat("dd/mm/yyyy").parse(endDate);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
            String startDate_string = formatter.format(startDate_d);
            String endDate_string = formatter.format(endDate_d);
            java.sql.Date startDate_sql = java.sql.Date.valueOf(startDate_string);
            java.sql.Date endDate_sql = java.sql.Date.valueOf(endDate_string);

            String sql= "SELECT uid,callnum,copynum,checkout FROM rent WHERE checkout BETWEEN ? AND ?";
            PreparedStatement pstmt=conn.prepareStatement(sql);
            pstmt.setDate(1,startDate_sql);
            pstmt.setDate(2,endDate_sql);
        
            ResultSet resultSet =pstmt.executeQuery(sql);
            if(!pstmt.executeQuery().next())
            {
                System.out.println("No records found.");
                return;
            }
            else
            {
                System.out.println("|UID|CallNum|CopyNum|Checkout|");
                while(resultSet.next())
                {
                    System.out.print("|");
                    System.out.print(resultSet.getString(1)+"|");
                    System.out.print(resultSet.getString(2)+"|");
                    System.out.print(resultSet.getInt(3)+"|");
                    System.out.println(resultSet.getDate(4)+"|");
                }
                System.out.println("End of Query");
            }
        }catch (SQLException e )
        {
            System.out.println(e);
            System.out.println("[Error] Failed to list the records.");
        }
        catch (ParseException e1)
        {
            System.out.println("Please input the correct date format [dd/mm/yyyy].");
        }
    }

    public void returnCar(String userID, String callNum, int copyNum,Connection conn)
    {
        try{
            // check whether the user is exist or not
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM User WHERE uid=?");
                pstmt.setString(1,userID);
                if(!pstmt.executeQuery().next())
                {
                    System.out.println("[Error] This user does not exist. Please check the user ID.");
                    return;
                }
            
            // check whether the car is exist or not
                pstmt=conn.prepareStatement("SELECT * FROM car WHERE callnum=?");
                pstmt.setString(1,callNum);
                if(!pstmt.executeQuery().next())
                {
                    System.out.println("[Error] This car does not exist. Please check the call number.");
                    return;
                }

            // check whether this car is rented by user
                pstmt=conn.prepareStatement("SELECT * FROM rent WHERE uid=? AND callnum=? AND copynum=? AND `return` IS NULL");
                pstmt.setString(1,userID);
                pstmt.setString(2,callNum);
                pstmt.setInt(3,copyNum);
                if(!pstmt.executeQuery().next())
                {
                    System.out.println("[Error] There is no checkout record for the information that you have entered.");
                    return;
                }

            // update rent record
                java.util.Date myDate = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
                pstmt=conn.prepareStatement("UPDATE rent SET `return`=? WHERE uid=? AND callnum=? AND copynum=? AND `return` IS NULL");
                pstmt.setDate(1,sqlDate);
                pstmt.setString(2,userID);
                pstmt.setString(3,callNum);
                pstmt.setInt(4,copyNum);
                pstmt.execute();
                System.out.println("Car returning performed successfully.");
        } catch (SQLException e)
        {
            System.out.println("Car renting performed unsuccessfully.");
        }
    }
    
    public void rentCar(String userID, String callNum, int copyNum,Connection conn)
    {
        try{

            // check whether the user is exist or not
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM user WHERE uid=?");
                pstmt.setString(1,userID);
                if(!pstmt.executeQuery().next())
                {
                    System.out.println("[Error] This user does not exist. Please check the user ID.");
                    return;
                }
            
            // check whether the user has rented max. num of car
                int max_num=0;
                int userRentedNo=0;
                pstmt=conn.prepareStatement("SELECT C.max FROM user U natural join user_category C WHERE uid=?");
                pstmt.setString(1,userID);
                ResultSet resultSet=pstmt.executeQuery();
                if (resultSet.next())
                {
                    max_num=resultSet.getInt(1);
                }
                pstmt=conn.prepareStatement("SELECT count(*) FROM rent WHERE uid=? AND `return` IS NULL");
                pstmt.setString(1,userID);
                resultSet=pstmt.executeQuery();
                if(resultSet.next())
                {
                    userRentedNo=resultSet.getInt(1);
                }
                if (max_num<=userRentedNo)
                {
                    System.out.println("[Error] This user has rented maximum number of car.");
                    return;
                }

            // check whether the car is exist or not
                pstmt=conn.prepareStatement("SELECT * FROM car WHERE callnum=?");
                pstmt.setString(1,callNum);
                if(!pstmt.executeQuery().next())
                {
                    System.out.println("[Error] This car does not exist. Please check the call number.");
                    return;
                }
            
            // check whether the car is rented or not
                pstmt=conn.prepareStatement("SELECT * FROM rent WHERE uid=? AND callnum=? AND copynum=? AND `return` IS NULL");
                pstmt.setString(1,userID);
                pstmt.setString(2,callNum);
                pstmt.setInt(3,copyNum);
                if(pstmt.executeQuery().next())
                {
                    System.out.println("[Error] This car has been rented.");
                    return;
                }
            
            // rent car procedure
                Date myDate= new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
                pstmt=conn.prepareStatement("INSERT INTO rent VALUES (?,?,?,?,NULL)");
                pstmt.setString(1,userID);
                pstmt.setString(2,callNum);
                pstmt.setInt(3,copyNum);
                pstmt.setDate(4,sqlDate);
                pstmt.execute();
                System.out.println("Car renting performed successfully.");
            
        }catch(SQLException e)
        {
            System.out.println(e);
            System.out.println("Car renting performed unsuccessfully.");
        }
    }

    public void unreturnCar_input(Connection conn)
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Type in the starting date [dd/mm/yyyy]: ");
        String startDate=input.nextLine();
        System.out.print("Type in the ending date [dd/mm/yyyy]: ");
        String endDate=input.nextLine();
        printAllUnreturnCar(startDate,endDate,conn);
    }

    public void returnCar_input(Connection conn)
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter The User ID: ");
        String userID=input.nextLine();
        System.out.print("Enter The Call Number: ");
        String callNum=input.nextLine();
        System.out.print("Enter The Copy Number: ");
        int copyNum=input.nextInt();
        returnCar(userID,callNum,copyNum,conn);
    }

    public void rentCar_input(Connection conn)
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter The User ID: ");
        String userID=input.nextLine();
        System.out.print("Enter The Call Number: ");
        String callNum=input.nextLine();
        System.out.print("Enter The Copy Number: ");
        int copyNum=input.nextInt();
        rentCar(userID,callNum,copyNum,conn);
    }

    
    public void printMenu()
    {
        System.out.println("-----Main Menu-----");
        System.out.print("What kind of operatoin would you like to perform?\n1. Car Renting\n2. Car Returning\n3. List all un-returned car copies which are checked-out within a period\n4. Return to the main menu\n");
        System.out.print("Enter Your Choice: ");
    }

    public void managerMainMenu(Connection conn)
    {
        while(true)
        {
            printMenu();
            Scanner input= new Scanner(System.in);
            int num;
            try{
                num = input.nextInt();
            } catch (InputMismatchException e)
            {
                System.out.println("Please input the integer within 1 to 4");
                continue;
            }

            
            switch(num)
            {
                case 1 :
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                    rentCar_input(conn);
                    break;
                case 2 :
                    System.out.print("\033[H\033[2J");  
                    System.out.flush();
                    returnCar_input(conn);
                    break;
                case 3 :
                    System.out.print("\033[H\033[2J");  
                    System.out.flush();
                    unreturnCar_input(conn);
                    break;
                case 4 :
                    return;
                default : 
                    System.out.print("Please input the integer within 1 to 4");
                    break;
            }
        }
    }
    
}

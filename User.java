import java.sql.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.lang.Integer;



public class User{
    public static void main(String[] args) throws SQLException {
        final String  dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db0";
        final String dbUsername="Group0";
        final String dbPassword="group0gp";
        Connection con= null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        }catch (ClassNotFoundException e) {
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        }catch (SQLException e) {
            System.out.println(e);
        }

        User.UserMainMenu(con);
    }

    public static void printMenu() {
        System.out.println("-----Operations for user menu-----");
        System.out.print("What kind of operatoin would you like to perform?\n1. Search for Cars\n2. Show loan record of a user\n3. Return to the main menu\n");
        System.out.print("Enter Your Choice: ");
    }
    public static void carsearchbycall(Connection con, String call_num){
        System.out.println("Number of records in each table:");

            String psql = "SELECT DISTINCT * FROM copy co, rent r ,car c , car_category cc ,produce p WHERE c.callnum=r.callnum and r.callnum= co.callnum and c.callnum=? and c.ccid=cc.ccid and co.callnum=p.callnum ";

            try {
                PreparedStatement pstmt = con.prepareStatement(psql);
                pstmt.setString(1,call_num);
                ResultSet rs = pstmt.executeQuery();
                System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
                while(rs.next()) {
                    int nava=rs.getInt("co.copynum"); //total copy number
                    System.out.print("|" + rs.getString(1)); //call_num
                    System.out.print("|" + rs.getString("c.name"));//ccname
                    System.out.print("|" + rs.getString("cc.ccname")); //Car Cat
                    System.out.print("|" + rs.getString("p.cname") + "|"); // comp
                    System.out.print(String.valueOf(nava));
                    System.out.println("|");
                    }
            } catch (SQLException e) {
                System.out.println(e);
            }

    }
    public static void carsearchbyname(Connection con, String name){
        System.out.println("Number of records in each table:");

        String psql = "SELECT DISTINCT * FROM copy co, rent r ,car c , car_category cc ,produce p WHERE c.callnum=r.callnum and r.callnum= co.callnum and c.name like ? and c.ccid=cc.ccid and co.callnum=p.callnum ";

        try {
            PreparedStatement pstmt = con.prepareStatement(psql);
            String inp="%"+name+"%";
            pstmt.setString(1,inp);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
            while(rs.next()){
                System.out.print( "|"+ rs.getString(1)); //call_num
                int ava=rs.getInt(2)-rs.getInt(5); //copy_copy-rent_copy
                System.out.print( "|"+ rs.getString("c.name"));//ccname
                System.out.print( "|"+ rs.getString("cc.ccname")); //Car Cat
                System.out.print( "|"+ rs.getString("p.cname")+"|"); // comp
                System.out.print( String.valueOf(ava) );
                System.out.println("|");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public static void carsearchbycomp(Connection con, String comp){
        System.out.println("Number of records in each table:");

        String psql = "SELECT DISTINCT * FROM copy co, rent r ,car c , car_category cc ,produce p WHERE c.callnum=r.callnum and r.callnum= co.callnum and p.cname like ? and c.ccid=cc.ccid and co.callnum=p.callnum ";

        try {
            PreparedStatement pstmt = con.prepareStatement(psql);
            String inp="%"+comp+"%";
            pstmt.setString(1,inp);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
            while(rs.next()){
                System.out.print( "|"+ rs.getString(1)); //call_num
                int ava=rs.getInt(2)-rs.getInt(5); //copy_copy-rent_copy
                System.out.print( "|"+ rs.getString("c.name"));//ccname
                System.out.print( "|"+ rs.getString("cc.ccname")); //Car Cat
                System.out.print( "|"+ rs.getString("p.cname")+"|"); // comp
                System.out.print( String.valueOf(ava) );
                System.out.println("|");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public static void loanrecord(Connection con, String cuser){
        System.out.println("Load Record:");
        String psql = "SELECT DISTINCT * FROM rent r, car c, produce p WHERE r.uid= ? and r.callnum=c.callnum and c.callnum= p.callnum ";
        try {
            PreparedStatement pstmt = con.prepareStatement(psql);
            pstmt.setString(1,cuser);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("|CallNum|CopyNum|Name|Company|Check-out|Returned?|");
            while(rs.next()){
                System.out.print( "|"+ rs.getString("r.callnum")); //call_num
                System.out.print( "|"+ rs.getString("r.copynum"));//copynum
                System.out.print( "|"+ rs.getString("c.name")); //Car name
                System.out.print( "|"+ rs.getString("r.checkout")+"|"); // Check-out
                String Return=rs.getString("r.return");
                if (Return==null){
                    System.out.print("No");
                }else{
                    System.out.print("Yes");
                }
                System.out.println("|");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

    }
    public static void UserMainMenu(Connection con) {
        while(true) {
            printMenu();
            Scanner input= new Scanner(System.in);
            int num = input.nextInt();
            switch(num) {
                case 1 :  // search for Cars
                    System.out.println("Choose the Search criterion:");
                    System.out.println("1.call number\n2.name\n3.company");
                    System.out.print("Choose the Search criterion:");
                    Scanner input2 = new Scanner(System.in);
                    int num_1 = input2.nextInt();
                    System.out.print("Type in the Search Keyword:");
                    Scanner input3 = new Scanner(System.in);
                    String call_name_com = input3.nextline();

                    switch (num_1){
                        case 1:
                            carsearchbycall(con,call_name_com);
                            break;
                        case 2:
                            carsearchbyname(con,call_name_com);
                            break;
                        case 3:
                            carsearchbycomp(con,call_name_com);
                            break;

                    }
                    break;
                case 2:
                    System.out.print("Enter The cuser ID:");
                    Scanner input4 = new Scanner(System.in);
                    String cuser = input4.nextline();
                    loanrecord(con,cuser);
                    break;
            }

        }
    }
}


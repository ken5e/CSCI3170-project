package carrentingsystem;
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
        String psql = "SELECT DISTINCT co.callnum, c.name, cc.ccname,p.cname FROM copy co ,car c , car_category cc ,produce p WHERE c.callnum= co.callnum and c.callnum=? and c.ccid=cc.ccid and co.callnum=p.callnum ";
        String get_all="SELECT COUNT(*) FROM copy co WHERE  co.callnum=?  ";
        String used="SELECT COUNT(*)-COUNT(r.return_date) AS ava FROM rent r WHERE  r.callnum=?  ";
        try {
            PreparedStatement pstmt = con.prepareStatement(psql);
            PreparedStatement pst_all = con.prepareStatement(get_all);
            PreparedStatement pst_used = con.prepareStatement(used);
            pstmt.setString(1,call_num);
            pst_all.setString(1,call_num);
            pst_used.setString(1,call_num);
            ResultSet rs = pstmt.executeQuery();
            ResultSet rs_all = pst_all.executeQuery();
            ResultSet rs_used = pst_used.executeQuery();
            if (rs.next()) {
                System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
                rs_all.next();
                rs_used.next();
                int ava=rs_all.getInt(1)-rs_used.getInt(1); //copy_copy-rent_copy
                System.out.print("|" + rs.getString(1)); //call_num
                System.out.print("|" + rs.getString("c.name"));//ccname
                System.out.print("|" + rs.getString("cc.ccname")); //Car Cat
                System.out.print("|" + rs.getString("p.cname") + "|"); // comp
                System.out.print(String.valueOf(ava));
                System.out.println("|");
                while(rs.next()) {
                    rs_all.next();
                    rs_used.next();
                    ava=rs_all.getInt(1)-rs_used.getInt(1); //copy_copy-rent_copy
                    System.out.print("|" + rs.getString(1)); //call_num
                    System.out.print("|" + rs.getString("c.name"));//ccname
                    System.out.print("|" + rs.getString("cc.ccname")); //Car Cat
                    System.out.print("|" + rs.getString("p.cname") + "|"); // comp
                    System.out.print(String.valueOf(ava));
                    System.out.println("|");
                }
            }else{
                System.out.println("No such call number!" );
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

    }
    public static void carsearchbyname(Connection con, String name){
        System.out.println("Number of records in each table:");
        String psql = "SELECT DISTINCT co.callnum, c.name, cc.ccname,p.cname FROM copy co ,car c , car_category cc ,produce p WHERE c.name like ? and c.callnum= co.callnum and c.ccid=cc.ccid and co.callnum=p.callnum ";

        try {
            PreparedStatement pstmt = con.prepareStatement(psql);
            String inp="%"+name+"%";
            pstmt.setString(1,inp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String get_all="SELECT COUNT(*) FROM copy co ,car c WHERE  co.callnum=c.callnum and c.name =? ";
                String used="SELECT COUNT(*)-COUNT(r.return_date) AS ava FROM rent r, car c WHERE  r.callnum=c.callnum and c.name = ?";
                PreparedStatement pst_all = con.prepareStatement(get_all);
                PreparedStatement pst_used = con.prepareStatement(used);
                System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
                pst_all.setString(1,rs.getString("c.name"));
                pst_used.setString(1,rs.getString("c.name"));
                ResultSet rs_all = pst_all.executeQuery();
                ResultSet rs_used = pst_used.executeQuery();
                rs_all.next();
                rs_used.next();
                int ava=rs_all.getInt(1)-rs_used.getInt(1); //copy_copy-rent_copy
                System.out.print("|" + rs.getString(1)); //call_num
                System.out.print("|" + rs.getString("c.name"));//ccname
                System.out.print("|" + rs.getString("cc.ccname")); //Car Cat
                System.out.print("|" + rs.getString("p.cname") + "|"); // comp
                System.out.print(String.valueOf(ava));
                System.out.println("|");
                while(rs.next()) {
                    String get_all2="SELECT COUNT(*) FROM copy co ,car c WHERE  co.callnum=c.callnum and c.name =? ";
                    String used2="SELECT COUNT(*)-COUNT(r.return_date) AS ava FROM rent r, car c WHERE  r.callnum=c.callnum and c.name = ?";
                    PreparedStatement pst_all2 = con.prepareStatement(get_all2);
                    PreparedStatement pst_used2 = con.prepareStatement(used2);
                    pst_all2.setString(1,rs.getString("c.name"));
                    pst_used2.setString(1,rs.getString("c.name"));
                    ResultSet rs_all2 = pst_all2.executeQuery();
                    ResultSet rs_used2 = pst_used2.executeQuery();

                    rs_all2.next();
                    rs_used2.next();
                    ava=rs_all2.getInt(1)-rs_used2.getInt(1); //copy_copy-rent_copy
                    System.out.print("|" + rs.getString(1)); //call_num
                    System.out.print("|" + rs.getString("c.name"));//ccname
                    System.out.print("|" + rs.getString("cc.ccname")); //Car Cat
                    System.out.print("|" + rs.getString("p.cname") + "|"); // comp
                    System.out.print(String.valueOf(ava));
                    System.out.println("|");
                }
            }else{
                System.out.println("No such car name!" );
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public static void carsearchbycomp(Connection con, String comp){
        System.out.println("Number of records in each table:");
        String psql = "SELECT DISTINCT co.callnum, c.name, cc.ccname,p.cname FROM copy co ,car c , car_category cc ,produce p WHERE p.cname like ? and c.callnum= co.callnum and c.ccid=cc.ccid and co.callnum=p.callnum ";

        try {
            PreparedStatement pstmt = con.prepareStatement(psql);
            String inp="%"+comp+"%";
            pstmt.setString(1,inp);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String get_all="SELECT COUNT(*) FROM copy co ,produce p WHERE  co.callnum=p.callnum and p.cname =? ";
                String used="SELECT COUNT(*)-COUNT(r.return_date) AS ava FROM rent r, produce p WHERE  r.callnum=p.callnum and p.cname = ?";
                PreparedStatement pst_all = con.prepareStatement(get_all);
                PreparedStatement pst_used = con.prepareStatement(used);
                System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
                pst_all.setString(1,rs.getString("p.cname"));
                pst_used.setString(1,rs.getString("p.cname"));
                ResultSet rs_all = pst_all.executeQuery();
                ResultSet rs_used = pst_used.executeQuery();
                rs_all.next();
                rs_used.next();
                int ava=rs_all.getInt(1)-rs_used.getInt(1); //copy_copy-rent_copy
                System.out.print("|" + rs.getString(1)); //call_num
                System.out.print("|" + rs.getString("c.name"));//ccname
                System.out.print("|" + rs.getString("cc.ccname")); //Car Cat
                System.out.print("|" + rs.getString("p.cname") + "|"); // comp
                System.out.print(String.valueOf(ava));
                System.out.println("|");
                while(rs.next()) {
                    String get_all2="SELECT COUNT(*) FROM copy co ,produce p WHERE  co.callnum=p.callnum and p.cname =? ";
                    String used2="SELECT COUNT(*)-COUNT(r.return_date) AS ava FROM rent r, produce p WHERE  r.callnum=p.callnum and p.cname = ?";
                    PreparedStatement pst_all2 = con.prepareStatement(get_all2);
                    PreparedStatement pst_used2 = con.prepareStatement(used2);
                    pst_all2.setString(1,rs.getString("p.cname"));
                    pst_used2.setString(1,rs.getString("p.cname"));
                    ResultSet rs_all2 = pst_all2.executeQuery();
                    ResultSet rs_used2 = pst_used2.executeQuery();

                    rs_all2.next();
                    rs_used2.next();
                    ava=rs_all2.getInt(1)-rs_used2.getInt(1); //copy_copy-rent_copy
                    System.out.print("|" + rs.getString(1)); //call_num
                    System.out.print("|" + rs.getString("c.name"));//ccname
                    System.out.print("|" + rs.getString("cc.ccname")); //Car Cat
                    System.out.print("|" + rs.getString("p.cname") + "|"); // comp
                    System.out.print(String.valueOf(ava));
                    System.out.println("|");
                }
            }else{
                System.out.println("No such company!" );
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

            if (rs.next()){
                System.out.println("|CallNum|CopyNum|Name|Company|Check-out|Returned?|");
                System.out.print( "|"+ rs.getString("r.callnum")); //call_num
                System.out.print( "|"+ rs.getString("r.copynum"));//copynum
                System.out.print( "|"+ rs.getString("c.name")); //Car name
                System.out.print( "|"+ rs.getString("r.checkout")+"|"); // Check-out
                String Return=rs.getString("r.return_date");
                if (Return==null){
                    System.out.print("No");
                }else{
                    System.out.print("Yes");
                }
                System.out.println("|");
                while(rs.next()){
                    System.out.print( "|"+ rs.getString("r.callnum")); //call_num
                    System.out.print( "|"+ rs.getString("r.copynum"));//copynum
                    System.out.print( "|"+ rs.getString("c.name")); //Car name
                    System.out.print( "|"+ rs.getString("r.checkout")+"|"); // Check-out
                    Return=rs.getString("r.return_date");
                    if (Return==null){
                        System.out.print("No");
                    }else{
                        System.out.print("Yes");
                    }
                    System.out.println("|");
                }
            }else{
                System.out.println("No such userid");
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


                    switch (num_1){
                        case 1:
                            System.out.print("Type in the Search Keyword:");
                            Scanner input3 = new Scanner(System.in);
                            String call = input3.nextLine();
                            carsearchbycall(con,call);
                            break;
                        case 2:
                            System.out.print("Type in the Search Keyword:");
                            Scanner input4 = new Scanner(System.in);
                            String name = input4.nextLine();
                            carsearchbyname(con,name);
                            break;
                        case 3:
                            System.out.print("Type in the Search Keyword:");
                            Scanner input5 = new Scanner(System.in);
                            String com = input5.nextLine();
                            carsearchbycomp(con,com);
                            break;
                        default:
                            System.out.print("Please input the integer within 1 to 3");
                            break;
                    }
                    break;
                case 2:
                    System.out.print("Enter The cuser ID:");
                    Scanner input4 = new Scanner(System.in);
                    String cuser = input4.nextLine();
                    loanrecord(con,cuser);
                    break;
                case 3:
                    return;
                default :
                    System.out.print("Please input the integer within 1 to 3");
                    break;
            }

        }
    }
}


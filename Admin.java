package carrentingsystem;

import java.sql.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.lang.Integer;
import java.util.InputMismatchException;



public class Admin {
    public static void main(String[] args) throws SQLException {
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db0?allowMultiQueries=true";
        String dbUsername = "Group0";
        String dbPassword = "group0gp";

        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        }catch (ClassNotFoundException e) {
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        }catch (SQLException e) {
            System.out.println(e);
        }

        Admin.adminMainMenu(con);
    }

    public static Boolean executeSQLfile(Connection con, String str_path) {
        // For data definition language
        Path p = Paths.get(str_path);
        String sqlfilestr = null;
        try {
            sqlfilestr = Files.readString(p);
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
        
        try (Statement stmt = con.createStatement()) {
            Boolean hasMoreResultSets = stmt.execute(sqlfilestr);
            while ( hasMoreResultSets || stmt.getUpdateCount() != -1 ) {  
                if ( hasMoreResultSets ) {  
                    ResultSet rs = stmt.getResultSet();
                    // handle your rs here
                } // if has rs
                else { // if ddl/dml/...
                    int queryResult = stmt.getUpdateCount();  
                    if ( queryResult == -1 ) { // no more queries processed  
                        break;  
                    } // no more queries processed  
                    // handle success, failure, generated keys, etc here
                } // if ddl/dml/...

                // check to continue in the loop  
                hasMoreResultSets = stmt.getMoreResults();  
            } // while results
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public static Boolean creatTables(Connection con) {
        String str_path = "./sqlfiles/";
        String user_category = null;
        String file_path = null;
        List<String> exist_list = new ArrayList<String>();
        List<String> create_list =  Arrays.asList("user_category", "user", "car_category", "car", "copy", "rent", "produce");
        List<Boolean> arr = new ArrayList<Boolean>();
        Boolean one_result = false;

        try {
        exist_list = Admin.existTables(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
        if (!exist_list.isEmpty()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Type y if you want to truncate all existing tables");
            System.out.println("Type any other string if you want to return");
            String user_input = scanner.nextLine();
            if (!user_input.equals("y"))
                return false;
            else if (user_input.equals("y")) {
                Admin.deleteTables(con);
            }
        }
        for (String i: create_list) {
            file_path = str_path + i + ".sql";
            one_result = Admin.executeSQLfile(con, file_path);
            arr.add(one_result);
        }
        for(Boolean b : arr) if(!b) return false;
        return true;
    }

    public static Boolean deleteTables(Connection con) {
        String str_path = "./sqlfiles/dropTables.sql";
        Boolean result = false;
        result = Admin.executeSQLfile(con, str_path);
        return result;
    }

    public static List<String> existTables(Connection con) throws SQLException {
        List<String> exist_list = new ArrayList<String>();;
        String check_exist = "SHOW tables;";
        Statement stmt = con.createStatement();
        try {
            String exist_table = null;
            ResultSet rs = stmt.executeQuery(check_exist);
            while (rs.next()) {
                exist_table = rs.getString(1);
                if (exist_table != null) {
                    String warning = "[Warning] Table: " + exist_table + " already exists!";
                    System.out.println(warning);
                    exist_list.add(exist_table);
                }
            } 
        } catch (SQLException e) {
            System.out.println(e);
        }
        return exist_list;
    }


    public static Boolean insertUserCat(Connection con, String path_str) throws SQLException{
        BufferedReader br;
        String strLine;
        String[] arrOfStr;
        String psql = "INSERT INTO user_category VALUES(?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(psql);
        Integer i = 0;
        Boolean output = true;
        path_str = "./" + path_str + "/user_category.txt"; 
        br = Admin.readTxtLine(path_str);

        if (br != null) {
            try {
                //Read File Line By Line
                while ((strLine = br.readLine()) != null) {
                    i++;
                    arrOfStr = strLine.split("\t", 0);
                    if (arrOfStr.length < 3) {
                        System.out.println("Error in parsing line " + i.toString() + "of " + path_str);
                        return false;
                    }
                    
                    pstmt.setInt(1, Integer.parseInt(arrOfStr[0]));
                    pstmt.setInt(2, Integer.parseInt(arrOfStr[1]));
                    pstmt.setInt(3, Integer.parseInt(arrOfStr[2]));

                    try {
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e);
                        output = false;
                    }
                }
            }
             catch (IOException e) {
                System.out.println(e);
            }
            return output;
        } else {
            return false;
        }
    }

    public static Boolean insertCarCat(Connection con, String path_str) throws SQLException{
        BufferedReader br;
        String strLine;
        String[] arrOfStr;
        String psql = "INSERT INTO car_category(ccid, ccname) VALUES(?, ?)";
        PreparedStatement pstmt = con.prepareStatement(psql);
        Integer i = 0;
        Boolean output = true;

        path_str = "./" + path_str + "/car_category.txt";
        br = Admin.readTxtLine(path_str);

        if (br != null) {
            try {
                //Read File Line By Line
                while ((strLine = br.readLine()) != null) {
                    i++;
                    arrOfStr = strLine.split("\t", 0);
                    if (arrOfStr.length < 2) {
                        System.out.println("Error in parsing line " + i.toString() + " of " + path_str);
                        return false;
                    }
                    pstmt.setInt(1, Integer.parseInt(arrOfStr[0]));
                    pstmt.setString(2, arrOfStr[1]);

                    try {
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e);
                        output = false;
                    }
                }
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }
        return output;
    }

    public static Boolean insertCar(Connection con, String path_str) throws SQLException{
        BufferedReader br;
        String strLine;
        String[] arrOfStr;
        String psql = "INSERT INTO car(callnum, name, manufacture, time_rent, ccid) VALUES(?, ?, ?, ?, ?)";
        String psql2 = "INSERT INTO copy(callnum, copynum) VALUES(?, ?)";
        String psql3 = "INSERT INTO produce(cname, callnum) VALUES(?, ?)";
        PreparedStatement pstmt = con.prepareStatement(psql);
        PreparedStatement pstmt2 = con.prepareStatement(psql2);
        PreparedStatement pstmt3 = con.prepareStatement(psql3);
        Integer i = 0;
        Boolean output = true;

        path_str = "./" + path_str + "/car.txt";
        br = Admin.readTxtLine(path_str);

        if (br != null) {
            //Read File Line By Line
            try {
                while ((strLine = br.readLine()) != null) {
                    arrOfStr = strLine.split("\t", 0);
                    i++;
                    if (arrOfStr.length < 7) {
                        System.out.println("Error in parsing line " + i.toString() + " of " + path_str);
                        return false;
                    }
                    
                    pstmt.setString(1, arrOfStr[0]);
                    pstmt.setString(2, arrOfStr[2]);
                    pstmt.setString(3, arrOfStr[4]);
                    pstmt.setInt(4, Integer.parseInt(arrOfStr[5]));
                    pstmt.setInt(5, Integer.parseInt(arrOfStr[6]));

                    pstmt2.setString(1, arrOfStr[0]);
                    pstmt2.setInt(2, Integer.parseInt(arrOfStr[1]));

                    pstmt3.setString(1, arrOfStr[3]);
                    pstmt3.setString(2, arrOfStr[0]);

                    try {
                        pstmt.executeUpdate();
                        pstmt2.executeUpdate();
                        pstmt3.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e);
                        output = false;
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {
            return false;
        }
        return output;
    }


    public static Boolean insertUser(Connection con, String path_str) throws SQLException{
        BufferedReader br;  
        String strLine;
        String[] arrOfStr;
        String psql = "INSERT INTO user(uid, name, age, occupation, ucid) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(psql);
        Integer i = 0;
        Boolean output = true;

        path_str = "./" + path_str + "/user.txt";
        br = Admin.readTxtLine(path_str);

        if (br != null) {
            try {
                while ((strLine = br.readLine()) != null) {
                    arrOfStr = strLine.split("\t", 0);
                    i++;
                    if (arrOfStr.length < 5) {
                        System.out.println("Error in parsing line " + i.toString() + " of " + path_str);
                        return false;
                    }
                    
                    pstmt.setString(1, arrOfStr[0]);
                    pstmt.setString(2, arrOfStr[1]);
                    pstmt.setInt(3, Integer.parseInt(arrOfStr[2]));
                    pstmt.setString(4, arrOfStr[3]);
                    pstmt.setInt(5, Integer.parseInt(arrOfStr[4]));

                    try {
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e);
                        output = false;
                    }
                }
            }
            catch (IOException e) {
                System.out.println(e);
            }
        } else {
            return false;
        }
        return output;
    }
    


    public static Boolean insertRent(Connection con, String path_str) throws SQLException{
        String strLine;
        String[] arrOfStr;
        String psql = "INSERT INTO rent VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(psql);
        Integer i = 0;
        Boolean output = true;
        BufferedReader br;

        path_str = "./" + path_str + "/rent.txt";
        br = Admin.readTxtLine(path_str);

        if (br != null) {
            try {
                while ((strLine = br.readLine()) != null) {
                    arrOfStr = strLine.split("\t", 0);
                    i++;
                    if (arrOfStr.length < 5) {
                        System.out.println("Error in parsing line " + i.toString()  + " of " + path_str);
                        return false;
                    }
                    
                    pstmt.setString(1, arrOfStr[2]);
                    pstmt.setString(2, arrOfStr[0]);
                    pstmt.setInt(3, Integer.parseInt(arrOfStr[1]));
                    pstmt.setString(4, arrOfStr[3]);
                    if (arrOfStr[4].equals("NULL")) {
                        pstmt.setNull(5, java.sql.Types.CHAR);
                    } else {
                        pstmt.setString(5, arrOfStr[4]);
                    }

                    try {
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e);
                        output = false;
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        } else{
            return false;
        }
        return output;
    }

    public static BufferedReader readTxtLine(String path_str) {
        try {
            File file = new File(path_str);    //reads the file
            FileReader fr = new FileReader(file); //reads the file  
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream 
            return br; 
        } catch (FileNotFoundException e) {
            System.out.println(e);
            return null;
        }
    }

    public static Boolean insertAll(Connection con, String path_str) {
        Boolean[] arr = new Boolean[5];
        try {
            arr[0] = Admin.insertUserCat(con, path_str);
            arr[1] = Admin.insertCarCat(con, path_str);
            arr[2] = Admin.insertUser(con, path_str);
            arr[3] = Admin.insertCar(con, path_str);
            arr[4] = Admin.insertRent(con, path_str);
            for(Boolean b : arr) if(!b) return false;
        }catch (SQLException e) {
            return false;
        }
        return true;
    }

    public static Boolean showNumRecord(Connection con) {
        System.out.println("Number of records in each table:");
        Boolean output = true;

        String[] arr = {"user_category", "user", "car_category", "car", "copy", "rent", "produce"};
        for (String i: arr) {
            String psql = "SELECT COUNT(*) FROM ";
            psql = psql + i;
            try {
                PreparedStatement pstmt = con.prepareStatement(psql);
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                System.out.println(i + ": " + rs.getString(1));
            } catch (SQLException e) {
                System.out.println(e);
                output = false;
            }
        }
        return output;
    }

    public static void printMenu()
    {
        System.out.println("-----Operations for administrator menu-----");
        System.out.print("What kind of operatoin would you like to perform?\n1. Create all tables\n2. Delete all tables\n3. Load from datafile\n4. Show number of records in each table\n5. Return to the main menu\n");
        System.out.print("Enter Your Choice: ");
    }

    public static void adminMainMenu(Connection con)
    {
        while(true)
        {
            printMenu();
            int num = -1;
            Scanner input= new Scanner(System.in);
            try {
                num = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please input the integer within 1 to 5");
                continue;
            }
            Boolean output = true;

            switch(num)
            {
                case 1 :
                    System.out.print("Processing...");
                    output = Admin.creatTables(con);
                    if (output == true) {
                        System.out.println("Done. Datebase is intialized.");
                    } else {
                         System.out.println("Database has not been intialized.");
                    }
                    break;
                case 2 :
                    System.out.print("Processing...");
                    output = Admin.deleteTables(con);
                    if (output == true) {
                        System.out.println("Done. Datebase is removed.");
                    } else {
                         System.out.println("Database has not been removed.");
                    }                    
                    break;
                case 3 :
                    System.out.println("Type in the Source Data Folder Path: ");
                    Scanner input2 = new Scanner(System.in);
                    String path_str = input2.next();
                    output = Admin.insertAll(con, path_str);
                    if (output == true) {
                        System.out.println("Done. Data is inputted to the database");
                    } else {
                         System.out.println("Some or all data has not been input.");
                    }
                    break;
                case 4 :
                    output = Admin.showNumRecord(con);
                    break;
                case 5 :
                    return;
                default : 
                    System.out.println("Please input the integer within 1 to 5");
                    break;
            }
        }
    }
}



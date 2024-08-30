99% of storage used … If you run out, you can't create, edit and upload files. Get 100 GB of storage for ₹130.00 ₹0 for 1 month.
import java.util.*;
import java.sql.*;
import java.io.*;


class Train {
    private String TrainCompany;
    private String destination;
    private String departureTime;
    private boolean[][] seats = new boolean[10][6];

    public Train(String TrainCompany, String destination, String departureTime) {
        this.TrainCompany = TrainCompany;
        this.destination = destination;
        this.departureTime = departureTime;
    }

    public String getTrainCompany() {
        return TrainCompany;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public boolean[][] getSeats() {
        return seats;
    }

    public boolean selectSeat(int row, int col) {
    if (row < 1 || row > 10 || col < 1 || col > 6) {
        System.out.println("Invalid seat selection. Please try again.");
        return false;
    } else if (seats[row - 1][col - 1]) {
        System.out.println("Seat already taken. Please select another seat.");
        return false;
    } else {
        seats[row - 1][col - 1] = true;
        System.out.println("Seat selected successfully!");

        // Save seat selection data to a file
        saveSeatDataToFile();

        return true;
    }

    }
    private void saveSeatDataToFile() {
        String filename = this.getTrainCompany() + "_" + this.getDestination() + ".txt";
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            objectOutputStream.writeObject(this.seats);
            objectOutputStream.flush(); // Flush the stream to ensure data is written
            System.out.println("Seat data saved to file.");
        } catch (IOException e) {
            e.printStackTrace(); // Print the exception stack trace for debugging
            System.out.println("Error while saving seat data to file.");
        }
    }
    



    public void clearSeat(int row, int col) {
        seats[row - 1][col - 1] = false;
        System.out.println("Seat selection cleared.");
    }

    private double economyPrice = 300.0; // Economy class price
    private double businessPrice = 420.0; // Business class price
    private double premiumPrice = 600.0; // Premium class price

    public double getEconomyPrice() {
        return economyPrice;
    }

    public double getBusinessPrice() {
        return businessPrice;
    }

    public double getPremiumPrice() {
        return premiumPrice;
    }

    public void loadSeatDataFromFile() {
    String filename = this.getTrainCompany() + "_" + this.getDestination() + ".txt";
    try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))) {
        this.seats = (boolean[][]) objectInputStream.readObject();
        System.out.println(" data loaded ......");
    } catch (IOException | ClassNotFoundException e) {
        // Handle exceptions (e.g., file not found)
        System.out.println("No seat data found. Seats are empty.");
    }

    }
}

class UserT {
    private String name;
    private String email;
    private String aadharNumber;

    
    public UserT(String name, String email, String aadharNumber) {
        this.name = name;
        this.email = email;
        this.aadharNumber = aadharNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

     public String getAadharNumber() {
        return aadharNumber;
    }
}


class Booking 
{

    private static int bookingCounter = 1;
    private int bookingId;
    private Train train;
    private int row;
    private int col;
    private UserT user;
    private String ticketClass;
    private Connection con;

    public Booking(UserT user, Train train, int row, int col, String ticketClass) {
        this.bookingId = bookingCounter++;
        this.user = user;
        this.train = train;
        this.row = row;
        this.col = col;
        this.ticketClass = ticketClass;
    }

    

    public int getBookingId() {
        return bookingId;
    }

    public UserT getUser() {
        return user;
    }

    public Train getTrain() {
        return train;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getTicketClass() {
        return ticketClass;
    }

    

    public double calculateTotalPrice() {
        double basePrice = 0.0;

        switch (ticketClass) {
            case "Economy":
                basePrice = train.getEconomyPrice();
                break;
            case "Business":
                basePrice = train.getBusinessPrice();
                break;
            case "Premium":
                basePrice = train.getPremiumPrice();
                break;
            default:
                break;
        }

       
        return basePrice;
    }

   public void saveBookingDetailsToFile() {
        // Create a filename based on the user's Aadhar number
        String filename = this.user.getAadharNumber() + ".txt";

        try {
            File directory = new File("TrainBookingDetails");
            if (!directory.exists()) {
                directory.mkdirs(); // Creates the directory and any necessary parent directories
            }
            FileWriter fileWriter = new FileWriter(directory.getPath() + File.separator + filename);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Write booking details to the file
            printWriter.println("Booking ID: " + this.bookingId);
            printWriter.println("Name: " + this.user.getName());
            printWriter.println("Email: " + this.user.getEmail());
            
            printWriter.println("Aadhar Number: " + this.user.getAadharNumber()); // Include Aadhar number
            printWriter.println("Airline: " + this.train.getTrainCompany());
            printWriter.println("Destination: " + this.train.getDestination());
            printWriter.println("Departure Time: " + this.train.getDepartureTime());
            printWriter.println("Seat: Row " + this.row + ", Column " + (char) ('A' + this.col - 1));
            printWriter.println("Ticket Class: " + this.ticketClass);
            printWriter.println("Total  Price: " + this.calculateTotalPrice());
            printWriter.close();

            System.out.println("Booking details saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error while saving booking details to file.");
            e.printStackTrace();
        }
    }
    


    public void establishDatabaseConnection() {
        try {
            String dburl = "jdbc:mysql://localhost:3306/airline";
            String dbuser = "root";
            String dbpass = "";
            //String drivername = "com.mysql.jdbc.Driver";
            Class.forName("com.mysql.cj.jdbc.Driver");

             con = DriverManager.getConnection(dburl, dbuser, dbpass);

            // Check if the Connection is established
            if (con != null) {
                System.out.println("Connection Successful");
            } else {
                System.out.println("Connection Failed");
            }

            // Close the connection when it's no longer needed
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveBookingDetailsToDatabase(Connection con) {
    
        //Statement st = con.createStatement();
                
        String sql = "INSERT INTO Trainbooking (name, Email, AadharNumber, TrainName, Destination, Departuretime, Seat_Row, Seat_Column, TicketClass, TotalPrice) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getAadharNumber());
            pstmt.setString(4, train.getTrainCompany());
            pstmt.setString(5, train.getDestination());
            pstmt.setString(6, train.getDepartureTime());
            pstmt.setInt(7, getRow());
            pstmt.setInt(8, getCol());
            pstmt.setString(9, getTicketClass());
            pstmt.setDouble(10, calculateTotalPrice());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Insertion Success of record ");
            } else {
                System.out.println("Insertion failed of record ");
            }
        } catch (SQLException e) {
            System.out.println("Error while executing SQL query:");
            e.printStackTrace();
        }
        
    }

    public void processPayment() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your payment details:");
        System.out.print("Card Number: ");
        String cardNumber = input.next();
        System.out.print("Expiration Date (MM/YY): ");
        String expirationDate = input.next();
        System.out.print("CVV: ");
        String cvv = input.next();
        System.out.println("Processing payment...");
        System.out.println("Payment successful!");
    }



private static void bookTrain(Train[] trains, Booking[] bookingTrain, Scanner input, Connection con) throws SQLException {
    System.out.println("Please enter your name:");
    String name = input.next();

    System.out.println("Please enter your email:");
    String email = input.next();
    
    System.out.println("Please enter your Aadhar number:"); // Add Aadhar number input
    String aadharNumber = input.next();

    UserT user = new UserT(name, email, aadharNumber);

    System.out.println("Please select your destination:");

    for (int i = 0; i < trains.length; i++) {
        System.out.println((i + 1) + ". " + trains[i].getDestination());
    }

    int trainOption = input.nextInt();
    // Booking booking1 = null;

    if (trainOption >= 1 && trainOption <= trains.length) {
        Train selectedTrain = trains[trainOption - 1];

        System.out.println("Trains available to " + selectedTrain.getDestination() + ":");
        System.out.println("Trains: " + selectedTrain.getTrainCompany());
        System.out.println("Departure Time: " + selectedTrain.getDepartureTime());

        System.out.println("Please select your seat:");
        displaySeats(selectedTrain.getSeats());

        int row, col;
        do {
            System.out.print("Row: ");
            row = input.nextInt();
            System.out.print("Column: ");
            col = input.nextInt();
        } while (!selectedTrain.selectSeat(row, col));

        displaySeats(selectedTrain.getSeats());

        System.out.println("Please select your ticket class:");
        System.out.println("1. Economy (Price: " + selectedTrain.getEconomyPrice() + ")");
        System.out.println("2. Business (Price: " + selectedTrain.getBusinessPrice() + ")");
        System.out.println("3. Premium (Price: " + selectedTrain.getPremiumPrice() + ")");
        int classOption = input.nextInt();

        String ticketClass = "";
        switch (classOption) {
            case 1:
                ticketClass = "Economy";
                break;
            case 2:
                ticketClass = "Business";
                break;
            case 3:
                ticketClass = "Premium";
                break;
            default:
                System.out.println("Invalid ticket class option. Defaulting to Economy.");
                ticketClass = "Economy";
                break;
        }
        //booking1 = new Booking(user, selectedBus, row, col, ticketClass);
        Booking booking2= new Booking(user, selectedTrain, row, col, ticketClass);

        double totalPrice = booking2.calculateTotalPrice();
        System.out.println("Total Price: $" + totalPrice);

        System.out.println("Your ticket details:5");
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Airline: " + selectedTrain.getTrainCompany());
        System.out.println("Destination: " + selectedTrain.getDestination());
        System.out.println("Departure Time: " + selectedTrain.getDepartureTime());
        System.out.println("Seat: Row " + row + ", Column " + (char) ('A' + col - 1));
        System.out.println("Ticket Class: " + ticketClass);

        System.out.println("Proceed with the payment?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int paymentOption = input.nextInt();

        if (paymentOption == 1) {
            // After successfully booking a seat:
            booking2.processPayment();
            booking2.saveBookingDetailsToFile();
            booking2.establishDatabaseConnection();
            Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "");
            booking2.saveBookingDetailsToDatabase(con1);
            //booking.saveBookingDetailsToDatabase();
            bookingTrain[booking2.getBookingId() - 1] = booking2; // Store the booking in the array
            
            displayBookingDetails(booking2);
        } else if (paymentOption == 2) {
            // Clear the seat selection if the user chooses not to proceed with payment
            selectedTrain.clearSeat(row, col);
            System.out.println("Payment canceled. Seat selection cleared.");
        } else {
            System.out.println("Invalid payment option. Payment canceled.");
        }
    } else {
        System.out.println("Invalid flight option. Please try again.");
    }
}
    

private static void displaySeats(boolean[][] seats) {
    for (int i = 0; i < seats.length; i++) {
        for (int j = 0; j < seats[i].length; j++) {
            if (seats[i][j]) {
                System.out.print("X ");
            } else {
                System.out.print("O ");
            }
        }
        System.out.println();
    }
}

private static void viewBookedTrain(Booking[] bookings) {
    System.out.println("Booked Bus:");
    for (Booking booking : bookings) {
        if (booking != null) {
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Name: " + booking.getUser().getName());
            System.out.println("Email: " + booking.getUser().getEmail());
            System.out.println("Airline: " + booking.getTrain().getTrainCompany());
            System.out.println("Destination: " + booking.getTrain().getDestination());
            System.out.println("Departure Time: " + booking.getTrain().getDepartureTime());
            System.out.println("Seat: Row " + booking.getRow() + ", Column " + (char) ('A' + booking.getCol() - 1));
            System.out.println("Ticket Class: " + booking.getTicketClass());
            
            // Print selected meals
            
            
            System.out.println();
        }
    }
}




private static void searchBookingByFileName(Scanner input) {
    System.out.print("Enter File Name: ");
    String fileNameInput = input.next();
    File directory = new File("TrainBookingDetails");
    
    if (directory.exists() && directory.isDirectory()) {
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().equals(fileNameInput)) {
                // Found a matching file
                displayBookingFromFile(file);
                return;
            }
        }
    } else {
        System.out.println("Directory 'FlightBookingDetails' does not exist or is not a directory.");
    }

    // If no matching file was found
    System.out.println("No booking found for File Name: " + fileNameInput);
}

private static void displayBookingFromFile(File file) {
    try (Scanner scanner = new Scanner(file)) {
        System.out.println("Booking Details from File: " + file.getName());
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
        }
    } catch (FileNotFoundException e) {
        System.out.println("Error reading booking details from file.");
    }
}



private static void displayBookingDetails(Booking booking) {
    System.out.println("Booking ID: " + booking.getBookingId());
    System.out.println("Name: " + booking.getUser().getName());
    System.out.println("Email: " + booking.getUser().getEmail());
    System.out.println("Airline: " + booking.getTrain().getTrainCompany());
    System.out.println("Destination: " + booking.getTrain().getDestination());
    System.out.println("Departure Time: " + booking.getTrain().getDepartureTime());
    System.out.println("Seat: Row " + booking.getRow() + ", Column " + (char) ('A' + booking.getCol() - 1));
    System.out.println("Ticket Class: " + booking.getTicketClass());
    System.out.println("Total Price: " + booking.calculateTotalPrice());
    System.out.println();
}



static class Movie {
    private String MovieName;
    private String DirctorName;
    private String Time;
    private boolean[][] seats = new boolean[10][6];

    public Movie(String MovieName, String DirctorName, String Time) {
        this.MovieName = MovieName;
        this.DirctorName = DirctorName;
        this.Time = Time;
    }

    public String getMovieName() {
        return MovieName;
    }

    public String getDirectorName() {
        return DirctorName;
    }

    public String getTime() {
        return Time;
    }

    public boolean[][] getSeats() {
        return seats;
    }

    public boolean selectSeat(int row, int col) {
        if (row < 1 || row > 10 || col < 1 || col > 6) {
            System.out.println("Invalid seat selection. Please try again.");
            return false;
        } else if (seats[row - 1][col - 1]) {
            System.out.println("Seat already taken. Please select another seat.");
            return false;
        } else {
            seats[row - 1][col - 1] = true;
            System.out.println("Seat selected successfully!");

            // Save seat selection data to a file
            saveSeatDataToFile();

            return true;
        }
    }

    private void saveSeatDataToFile() {
        String filename = this.getMovieName() + "_" + this.getDirectorName() + ".txt";
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            objectOutputStream.writeObject(this.seats);
            System.out.println("Seat data saved to file.");
        } catch (IOException e) {
            // Handle exceptions (e.g., file write error)
            System.out.println("Error while saving seat data to file.");
        }
    }


    public void clearSeat(int row, int col) {
        seats[row - 1][col - 1] = false;
        System.out.println("Seat selection cleared.");
    }

    private double Gold = 200.0; // Economy class price
    private double Platinum = 320.0; // Business class price
    private double PrmiumLongug = 400.0; // Premium class price

    public double getGoldPrice() {
        return Gold;
    }

    public double getPlatinumPrice() {
        return Platinum;
    }

    public double getPremiumPrice() {
        return PrmiumLongug;
    }

    public void loadSeatDataFromFile() {
        String filename = this.getMovieName() + "_" + this.getDirectorName() + ".txt";
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))) {
            this.seats = (boolean[][]) objectInputStream.readObject();
            System.out.println(" data loaded ......");
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions (e.g., file not found)
            System.out.println("No seat data found. Seats are empty.");
        }
    }
}

static class User {
    private String name;
    private String email;
    private String aadharNumber;

    
    public User(String name, String email, String aadharNumber) {
        this.name = name;
        this.email = email;
        this.aadharNumber = aadharNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

     public String getAadharNumber() {
        return aadharNumber;
    }
}


static class BookingM {
    private  int bookingCounter = 1;
    private int bookingId;
    private Movie movie;
    private int row;
    private int col;
    private User user;
    private String ticketClass;
    private Connection con;

    public BookingM(User user, Movie movie, int row, int col, String ticketClass) {
        this.bookingId = bookingCounter++;
        this.user = user;
        this.movie = movie;
        this.row = row;
        this.col = col;
        this.ticketClass = ticketClass;
    }

    

    public int getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public Movie getMovie() {
        return movie;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getTicketClass() {
        return ticketClass;
    }

    

    public double calculateTotalPrice() {
        double basePrice = 0.0;

        switch (ticketClass) {
            case "Gold":
                basePrice = movie.getGoldPrice();
                break;
            case "Platinum":
                basePrice = movie.getPlatinumPrice();
                break;
            case "PrmiumLongug":
                basePrice = movie.getPremiumPrice();
                break;
            default:
                break;
        }

       
        return basePrice;
    }

   public void saveBookingDetailsToFileM() {
        // Create a filename based on the user's Aadhar number
        String filename = this.user.getAadharNumber() + ".txt";

        try {
            File directory = new File("MovieBookingDetails");
            if (!directory.exists()) {
                directory.mkdirs(); // Creates the directory and any necessary parent directories
            }
            FileWriter fileWriter = new FileWriter(directory.getPath() + File.separator + filename);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Write booking details to the file
            printWriter.println("Booking ID: " + this.bookingId);
            printWriter.println("Name: " + this.user.getName());
            printWriter.println("Email: " + this.user.getEmail());
            
            printWriter.println("Aadhar Number: " + this.user.getAadharNumber()); // Include Aadhar number
            printWriter.println("Movie: " + this.movie.getMovieName());
            printWriter.println("DirectorName: " + this.movie.getDirectorName());
            printWriter.println("Departure Time: " + this.movie.getTime());
            printWriter.println("Seat: Row " + this.row + ", Column " + (char) ('A' + this.col - 1));
            printWriter.println("Ticket Class: " + this.ticketClass);
            printWriter.println("Total  Price: " + this.calculateTotalPrice());
            printWriter.close();

            System.out.println("Booking details saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error while saving booking details to file.");
            e.printStackTrace();
        }
    }
    


    public void establishDatabaseConnectionM() {
        try {
            String dburl = "jdbc:mysql://localhost:3306/airline";
            String dbuser = "root";
            String dbpass = "";
            //String drivername = "com.mysql.jdbc.Driver";
            Class.forName("com.mysql.cj.jdbc.Driver");

             con = DriverManager.getConnection(dburl, dbuser, dbpass);

            // Check if the Connection is established
            if (con != null) {
                System.out.println("Connection Successful");
            } else {
                System.out.println("Connection Failed");
            }

            // Close the connection when it's no longer needed
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveBookingDetailsToDatabaseM(Connection con) {
            //Statement st = con.createStatement();
                
        String sql = "INSERT INTO movies (name, Email, AadharNumber, MovieName, DirectorName, Time, Seat_Row, Seat_Column, TicketClass, TotalPrice) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getAadharNumber());
            pstmt.setString(4, movie.getMovieName());
            pstmt.setString(5, movie.getDirectorName());
            pstmt.setString(6, movie.getTime());
            pstmt.setInt(7, getRow());
            pstmt.setInt(8, getCol());
            pstmt.setString(9, getTicketClass());
            pstmt.setDouble(10, calculateTotalPrice());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Insertion Success of record ");
            } else {
                System.out.println("Insertion failed of record ");
            }
        } catch (SQLException e) {
            System.out.println("Error while executing SQL query:");
            e.printStackTrace();
        }
        
    }     

    public void processPayment() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your payment details:");
        System.out.print("Card Number: ");
        String cardNumber = input.next();
        System.out.print("Expiration Date (MM/YY): ");
        String expirationDate = input.next();
        System.out.print("CVV: ");
        String cvv = input.next();
        System.out.println("Processing payment...");
        System.out.println("Payment successful!");
    }



}

    




private static void bookMovie(Movie[] movies, BookingM[] bookingMovie, Scanner input, Connection con) throws SQLException {
    System.out.println("Please enter your name:");
    String name = input.next();

    System.out.println("Please enter your email:");
    String email = input.next();

    System.out.println("Please enter your Aadhar number:"); // Add Aadhar number input
    String aadharNumber = input.next();

    User user = new User(name, email, aadharNumber);

    System.out.println("Please select your Movie:");

    for (int i = 0; i < movies.length; i++) {
        System.out.println((i + 1) + ". " + movies[i].getMovieName());
    }

    int MovieOption = input.nextInt();
    // Booking booking1 = null;

    if (MovieOption >= 1 && MovieOption <= movies.length) {
        Movie selectedMovie = movies[MovieOption - 1];

        System.out.println("Movir available to " + selectedMovie.getMovieName() + ":");
        System.out.println("Movie DirctorName: " + selectedMovie.getDirectorName());
        System.out.println("Time: " + selectedMovie.getTime());

        System.out.println("Please select your seat:");
        displaySeats(selectedMovie.getSeats());

        int row, col;
        do {
            System.out.print("Row: ");
            row = input.nextInt();
            System.out.print("Column: ");
            col = input.nextInt();
        } while (!selectedMovie.selectSeat(row, col));

        displaySeatsM(selectedMovie.getSeats());

        System.out.println("Please select your ticket class:");
        System.out.println("1. Gold (Price: " + selectedMovie.getGoldPrice() + ")");
        System.out.println("2. Platinum (Price: " + selectedMovie.getPlatinumPrice() + ")");
        System.out.println("3. PremiumLounge (Price: " + selectedMovie.getPremiumPrice() + ")");
        int classOption = input.nextInt();

        String ticketClass = "";
        switch (classOption) {
            case 1:
                ticketClass = "Gold";
                break;
            case 2:
                ticketClass = "Platinum";
                break;
            case 3:
                ticketClass = "PremiumLounge";
                break;
            default:
                System.out.println("Invalid ticket class option. Defaulting to Economy.");
                ticketClass = "Gold";
                break;
        }
        //booking1 = new Booking(user, selectedBus, row, col, ticketClass);
        BookingM bookingm= new BookingM(user, selectedMovie, row, col, ticketClass);

        double totalPrice = bookingm.calculateTotalPrice();
        System.out.println("Total Price: $" + totalPrice);

        System.out.println("Your ticket details:");
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Airline: " + selectedMovie.getMovieName());
        System.out.println("Destination: " + selectedMovie.getDirectorName());
        System.out.println("Departure Time: " + selectedMovie.getTime());
        System.out.println("Seat: Row " + row + ", Column " + (char) ('A' + col - 1));
        System.out.println("Ticket Class: " + ticketClass);

        System.out.println("Proceed with the payment?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int paymentOption = input.nextInt();

        if (paymentOption == 1) {
            // After successfully booking a seat:
            bookingm.processPayment();
            bookingm.saveBookingDetailsToFileM();
            bookingm.establishDatabaseConnectionM();
            Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "");
            bookingm.saveBookingDetailsToDatabaseM(con1);
            displayBookingDetailsM(bookingm);
            
            //booking.saveBookingDetailsToDatabase();
            //bookingMovie[bookingm.getBookingIdM() - 1] = bookingm; // Store the booking in the array
            //System.out.println("Booking ID: " + booking3.getBookingId() + " created.");
        } else if (paymentOption == 2) {
            // Clear the seat selection if the user chooses not to proceed with payment
            selectedMovie.clearSeat(row, col);
            System.out.println("Payment canceled. Seat selection cleared.");
        } else {
            System.out.println("Invalid payment option. Payment canceled.");
        }
    } else {
        System.out.println("Invalid flight option. Please try again.");
    }
}
    

private static void displaySeatsM(boolean[][] seats) {
    for (int i = 0; i < seats.length; i++) {
        for (int j = 0; j < seats[i].length; j++) {
            if (seats[i][j]) {
                System.out.print("X ");
            } else {
                System.out.print("O ");
            }
        }
        System.out.println();
    }
}
    private static void viewBookedMovie(BookingM[] bookings) {
        System.out.println("Booked Bus:");
        for (BookingM booking : bookings) {
            if (booking != null) {
                System.out.println("Booking ID: " + booking.getBookingId());
                System.out.println("Name: " + booking.getUser().getName());
                System.out.println("Email: " + booking.getUser().getEmail());
                System.out.println("Airline: " + booking.getMovie().getMovieName());
                System.out.println("Destination: " + booking.getMovie().getDirectorName());
                System.out.println("Departure Time: " + booking.getMovie().getTime());
                System.out.println("Seat: Row " + booking.getRow() + ", Column " + (char) ('A' + booking.getCol() - 1));
                System.out.println("Ticket Class: " + booking.getTicketClass());
                
                // Print selected meals
                
                
                System.out.println();
            }
        }
}



public static void searchBookingByFileNameM(Scanner input) {
    System.out.print("Enter File Name: ");
    String fileNameInput = input.next();
    File directory = new File("MovieBookingDetails");
    
    if (directory.exists() && directory.isDirectory()) {
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().equals(fileNameInput)) {
                // Found a matching file
                displayBookingFromFile(file);
                return;
            }
        }
    } else {
        System.out.println("Directory 'FlightBookingDetails' does not exist or is not a directory.");
    }

    // If no matching file was found
    System.out.println("No booking found for File Name: " + fileNameInput);
}




private static  void displayBookingDetailsM(BookingM booking) {
    System.out.println("╔════════════════════════════════╗");
        System.out.println("║          Booking Details        ║");
        System.out.println("╟────────────────────────────────╢");
        System.out.println("║ Booking ID: " + (booking.getBookingId()) + "                      ║");
        System.out.println("║ Name: " + (booking.getUser().getName() ) + "                      ║");
        System.out.println("║ Email: " + (booking.getUser().getEmail()) + "                     ║");
        System.out.println("║ Airline: " + (booking.getMovie().getMovieName()) + "               ║");
        System.out.println("║ Destination: " + (booking.getMovie().getDirectorName()) + "       ║");
        System.out.println("║ Departure Time: " + (booking.getMovie().getTime() + "   ║"));
        System.out.println("║ Seat: Row " + (booking.getRow()) + ", Column " + ('A' + booking.getCol() - 1) + "   ║");
        System.out.println("\u2551 Ticket Class:"+booking.getTicketClass());
        System.out.println("╚════════════════════════════════╝");
}
//Flight

static class Flight {
    private String airline;
    private String destination;
    private String departureTime;
    private boolean[][] seats = new boolean[10][6];

    public Flight(String airline, String destination, String departureTime) {
        this.airline = airline;
        this.destination = destination;
        this.departureTime = departureTime;
    }

    public String getAirline() {
        return airline;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public boolean[][] getSeats() {
        return seats;
    }

    public boolean selectSeat(int row, int col) {
        if (row < 1 || row > 10 || col < 1 || col > 6) {
            System.out.println("Invalid seat selection. Please try again.");
            return false;
        } else if (seats[row - 1][col - 1]) {
            System.out.println("Seat already taken. Please select another seat.");
            return false;
        } else {
            seats[row - 1][col - 1] = true;
            System.out.println("Seat selected successfully!");

            // Save seat selection data to a file
            saveSeatDataToFile();

            return true;
        }
    }

    private void saveSeatDataToFile() {
        String filename = this.getAirline() + "_" + this.getDestination() + ".dat";
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            objectOutputStream.writeObject(this.seats);
            System.out.println("Seat data saved to file.");
        } catch (IOException e) {
            // Handle exceptions (e.g., file write error)
            System.out.println("Error while saving seat data to file.");
        }
    }


    public void clearSeat(int row, int col) {
        seats[row - 1][col - 1] = false;
        System.out.println("Seat selection cleared.");
    }

    private double economyPrice = 30000.0; // Economy class price
    private double businessPrice = 42000.0; // Business class price
    private double premiumPrice = 60000.0; // Premium class price

    public double getEconomyPrice() {
        return economyPrice;
    }

    public double getBusinessPrice() {
        return businessPrice;
    }

    public double getPremiumPrice() {
        return premiumPrice;
    }

    public void loadSeatDataFromFile() {
        String filename = this.getAirline() + "_" + this.getDestination() + ".dat";
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))) {
            this.seats = (boolean[][]) objectInputStream.readObject();
            System.out.println("Seat data loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions (e.g., file not found)
            System.out.println("No seat data found. Seats are empty.");
        }

    
    }
}

static class UserF {
    private String name;
    private String email;
    private String aadharNumber;

    
    public UserF(String name, String email, String aadharNumber) {
        this.name = name;
        this.email = email;
        this.aadharNumber = aadharNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

     public String getAadharNumber() {
        return aadharNumber;
    }
}


static class BookingF {
    private int bookingCounter = 1;
    private int bookingId;
    private Flight flight;
    private int row;
    private int col;
    private UserF user;
    private String ticketClass;
    private Connection con;
   

    public  BookingF(UserF user2, Flight flight, int row, int col, String ticketClass) {
    this.bookingId = bookingCounter++;
    this.user = user2;
    this.flight = flight;
    this.row = row;
    this.col = col;
    this.ticketClass = ticketClass;
   
    }


    public int getBookingId() {
        return bookingId;
    }

    public UserF getUser() {
        return user;
    }

    public Flight getFlight() {
        return flight;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getTicketClass() {
        return ticketClass;
    }

    

    public double calculateTotalPrice() {
        double basePrice = 0.0;

        switch (ticketClass) {
            case "Economy":
                basePrice = flight.getEconomyPrice();
                break;
            case "Business":
                basePrice = flight.getBusinessPrice();
                break;
            case "Premium":
                basePrice = flight.getPremiumPrice();
                break;
            default:
                break;
        }

       
        return basePrice;
    }

    public void saveBookingDetailsToFile() {
        // Create a filename based on the user's Aadhar number
        String filename = this.user.getAadharNumber() + ".txt";

        try {
            File directory = new File("FlightBookingDetails");
            if (!directory.exists()) {
                directory.mkdirs(); // Creates the directory and any necessary parent directories
            }
            FileWriter fileWriter = new FileWriter(directory.getPath() + File.separator + filename);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Write booking details to the file
            printWriter.println("Booking ID: " + this.bookingId);
            printWriter.println("Name: " + this.user.getName());
            printWriter.println("Email: " + this.user.getEmail());
            
            printWriter.println("Aadhar Number: " + this.user.getAadharNumber()); // Include Aadhar number
            printWriter.println("Airline: " + this.flight.getAirline());
            printWriter.println("Destination: " + this.flight.getDestination());
            printWriter.println("Departure Time: " + this.flight.getDepartureTime());
            printWriter.println("Seat: Row " + this.row + ", Column " + (char) ('A' + this.col - 1));
            printWriter.println("Ticket Class: " + this.ticketClass);
            printWriter.println("Total  Price: " + this.calculateTotalPrice());
            printWriter.close();

            System.out.println("Booking details saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error while saving booking details to file.");
            e.printStackTrace();
        }
    }
    


    public void establishDatabaseConnection() {
        try {
            String dburl = "jdbc:mysql://localhost:3306/airline";
            String dbuser = "root";
            String dbpass = "";
            //String drivername = "com.mysql.jdbc.Driver";
            Class.forName("com.mysql.cj.jdbc.Driver");

             con = DriverManager.getConnection(dburl, dbuser, dbpass);

            // Check if the Connection is established
            if (con != null) {
                System.out.println("Connection Successful");
            } else {
                System.out.println("Connection Failed");
            }

            // Close the connection when it's no longer needed
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void saveBookingDetailsToDatabase(Connection con)
    {
        String sql = "INSERT INTO Flightbooking (name, Email, AadharNumber, AirLine, Destination, DepartureTime, Seat_Row, Seat_Column, TicketClass, TotalPrice) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getAadharNumber());
            pstmt.setString(4, flight.getAirline());
            pstmt.setString(5, flight.getDestination());
            pstmt.setString(6, flight.getDepartureTime());
            pstmt.setInt(7, getRow());
            pstmt.setInt(8, getCol());
            pstmt.setString(9, getTicketClass());
            pstmt.setDouble(10, calculateTotalPrice());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Insertion Success of record ");
            } else {
                System.out.println("Insertion failed of record ");
            }
        } catch (SQLException e) {
            System.out.println("Error while executing SQL query:");
            e.printStackTrace();
        }
    }
        
  
    






            

    public void processPayment() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your payment details:");
        System.out.print("Card Number: ");
        String cardNumber = input.next();
        System.out.print("Expiration Date (MM/YY): ");
        String expirationDate = input.next();
        System.out.print("CVV: ");
        String cvv = input.next();
        System.out.println("Processing payment...");
        System.out.println("Payment successful!");
    }



}

    




private static void bookFlight(Flight[] flights, BookingF[] bookings, Scanner input,Connection con) throws SQLException 
{
        
    System.out.println("Please enter your name:");
    String name = input.next();
    
    System.out.println("Please enter your email:");
    String email = input.next();
    
    System.out.println("Please enter your Aadhar number:"); // Add Aadhar number input
    String aadharNumber = input.next();
    
    UserF user = new UserF(name, email, aadharNumber);

    System.out.println("Please select your destination:");

    for (int i = 0; i < flights.length; i++) {
        System.out.println((i + 1) + ". " + flights[i].getDestination());
    }

    int flightOption = input.nextInt();
    BookingF booking = null;

    if (flightOption >= 1 && flightOption <= flights.length) {
        Flight selectedFlight = flights[flightOption - 1];

        System.out.println("Flights available to " + selectedFlight.getDestination() + ":");
        System.out.println("Airline: " + selectedFlight.getAirline());
        System.out.println("Departure Time: " + selectedFlight.getDepartureTime());

        System.out.println("Please select your seat:");
        displaySeats(selectedFlight.getSeats());

        int row, col;
        do {
            System.out.print("Row: ");
            row = input.nextInt();
            System.out.print("Column: ");
            col = input.nextInt();
        } while (!selectedFlight.selectSeat(row, col));

        displaySeats(selectedFlight.getSeats());

        System.out.println("Please select your ticket class:");
        System.out.println("1. Economy (Price: " + selectedFlight.getEconomyPrice() + ")");
        System.out.println("2. Business (Price: " + selectedFlight.getBusinessPrice() + ")");
        System.out.println("3. Premium (Price: " + selectedFlight.getPremiumPrice() + ")");
        int classOption = input.nextInt();

        String ticketClass = "";
        switch (classOption) {
            case 1:
                ticketClass = "Economy";
                break;
            case 2:
                ticketClass = "Business";
                break;
            case 3:
                ticketClass = "Premium";
                break;
            default:
                System.out.println("Invalid ticket class option. Defaulting to Economy.");
                ticketClass = "Economy";
                break;
        }
        booking = new BookingF(user, selectedFlight, row, col, ticketClass);

        double totalPrice = booking.calculateTotalPrice();
        System.out.println("Total Price: $" + totalPrice);

        System.out.println("Your ticket details:");
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Airline: " + selectedFlight.getAirline());
        System.out.println("Destination: " + selectedFlight.getDestination());
        System.out.println("Departure Time: " + selectedFlight.getDepartureTime());
        System.out.println("Seat: Row " + row + ", Column " + (char) ('A' + col - 1));
        

        System.out.println("Proceed with the payment?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int paymentOption = input.nextInt();

        if (paymentOption == 1) {
            // After successfully booking a seat:
            booking =new BookingF(user, selectedFlight, row, col,ticketClass);
            booking.processPayment();
            booking.saveBookingDetailsToFile();
            booking.establishDatabaseConnection();
            Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "");
            booking.saveBookingDetailsToDatabase(con1);
            displayBookingDetailsF(booking);

            //booking.saveBookingDetailsToDatabase();
            bookings[booking.getBookingId() - 1] = booking; // Store the booking in the array
            System.out.println("Booking ID: " + booking.getBookingId() + " created.");
        } else if (paymentOption == 2) {
            // Clear the seat selection if the user chooses not to proceed with payment
            selectedFlight.clearSeat(row, col);
            System.out.println("Payment canceled. Seat selection cleared.");
        } else {
            System.out.println("Invalid payment option. Payment canceled.");
        }
    } else {
        System.out.println("Invalid flight option. Please try again.");
    }
}

private static void displaySeatsF(boolean[][] seats) {
    for (int i = 0; i < seats.length; i++) {
        for (int j = 0; j < seats[i].length; j++) {
            if (seats[i][j]) {
                System.out.print("X ");
            } else {
                System.out.print("O ");
            }
        }
        System.out.println();
    }
}
private static void viewBookedFlights(BookingF[] bookings) {
    System.out.println("Booked Flights:");
    for (BookingF booking2 : bookings) {
        if (booking2 != null) {
            System.out.println("Booking ID: " + booking2.getBookingId());
            System.out.println("Name: " + booking2.getUser().getName());
            System.out.println("Email: " + booking2.getUser().getEmail());
            System.out.println("Airline: " + booking2.getFlight().getAirline());
            System.out.println("Destination: " + booking2.getFlight().getDestination());
            System.out.println("Departure Time: " + booking2.getFlight().getDepartureTime());
            System.out.println("Seat: Row " + booking2.getRow() + ", Column " + (char) ('A' + booking2.getCol() - 1));
            System.out.println("Ticket Class: " + booking2.getTicketClass());
            
            // Print selected meals
            
            
            System.out.println();
        }
    }
}


private static void searchBookingByFileNameF(Scanner input2) {
    System.out.print("Enter File Name: ");
    String fileNameInput = input2.next();
    File directory = new File("FlightBookingDetails");
    
    if (directory.exists() && directory.isDirectory()) {
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().equals(fileNameInput)) {
                // Found a matching file
                displayBookingFromFile(file);
                return;
            }
        }
    } else {
        System.out.println("Directory 'FlightBookingDetails' does not exist or is not a directory.");
    }

    // If no matching file was found
    System.out.println("No booking found for File Name: " + fileNameInput);
}

private static void displayBookingFromFileF(File file) {
    try (Scanner scanner = new Scanner(file)) {
        System.out.println("Booking Details from File: " + file.getName());
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
        }
    } catch (FileNotFoundException e) {
        System.out.println("Error reading booking details from file.");
    }
}



private static void displayBookingDetailsF(BookingF booking) {
    System.out.println("╔════════════════════════════════╗");
    System.out.println("║          Booking Details       ║");
    System.out.println("╟────────────────────────────────╢");
    System.out.println("║ Booking ID: " + (booking.getBookingId()) + "                      ║");
    System.out.println("║ Name: " + (booking.getUser().getName() ) + "                      ║");
    System.out.println("║ Email: " + (booking.getUser().getEmail()) + "                     ║");
    System.out.println("║ Airline: " + (booking.getFlight().getAirline()) + "               ║");
    System.out.println("║ Destination: " + (booking.getFlight().getDestination()) + "       ║");
    System.out.println("║ Departure Time: " + (booking.getFlight().getDepartureTime() + "   ║"));
    System.out.println("║ Seat: Row " + (booking.getRow()) + ", Column " + ('A' + booking.getCol() - 1) + "   ║");
    System.out.println("\u2551 Ticket Class:"+booking.getTicketClass());
    System.out.println("╚════════════════════════════════╝");
}


static class Bus 
{
    private String BusCom;
    private String destination;
    private String departureTime;
    private boolean[][] seats = new boolean[10][6];

    public Bus(String BusCom, String destination, String departureTime) {
        this.BusCom = BusCom;
        this.destination = destination;
        this.departureTime = departureTime;
    }

    public String getBusCom() {
        return BusCom;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public boolean[][] getSeats() {
        return seats;
    }

    public boolean selectSeat(int row, int col) 
    {
        if (row < 1 || row > 10 || col < 1 || col > 6) {
            System.out.println("Invalid seat selection. Please try again.");
            return false;
        } else if (seats[row - 1][col - 1]) {
            System.out.println("Seat already taken. Please select another seat.");
            return false;
        } else {
            seats[row - 1][col - 1] = true;
            System.out.println("Seat selected successfully!");

            // Save seat selection data to a file
            saveSeatDataToFile();

            return true;
        }
    }

    private void saveSeatDataToFile() 
    {
        String filename = this.getBusCom() + "_" + this.getDestination() + ".txt";
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            objectOutputStream.writeObject(this.seats);
            System.out.println("Seat data saved to file.");
        } catch (IOException e) {
            // Handle exceptions (e.g., file write error)
            System.out.println("Error while saving seat data to file.");
        }
    }


    public void clearSeat(int row, int col) {
        seats[row - 1][col - 1] = false;
        System.out.println("Seat selection cleared.");
    }

    private double economyPrice = 220.0; // Economy class price
    private double businessPrice = 300.0; // Business class price
    private double premiumPrice = 350.0; // Premium class price

    public double getEconomyPrice() {
        return economyPrice;
    }

    public double getBusinessPrice() {
        return businessPrice;
    }

    public double getPremiumPrice() {
        return premiumPrice;
    }

    public void loadSeatDataFromFile()
    {
        String filename = this.getBusCom() + "_" + this.getDestination() + ".txt";
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))) {
            this.seats = (boolean[][]) objectInputStream.readObject();
            System.out.println("Seat data loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions (e.g., file not found)
            System.out.println("No seat data found. Seats are empty.");
        }

    
    }
}

static class UserB {
    private String name;
    private String email;
    private String aadharNumber;

    
    public UserB(String name, String email, String aadharNumber) {
        this.name = name;
        this.email = email;
        this.aadharNumber = aadharNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

     public String getAadharNumber() {
        return aadharNumber;
    }
}


static class BookingB {
    private  int bookingCounter = 1;
    private int bookingId;
    private Bus bus;
    private int row;
    private int col;
    private UserB user;
    private String ticketClass;
    private Connection con;

    public BookingB(UserB user, Bus bus, int row, int col, String ticketClass) {
        this.bookingId = bookingCounter++;
        this.user = user;
        this.bus = bus;
        this.row = row;
        this.col = col;
        this.ticketClass = ticketClass;
    }

    public BookingB(){}


    public int getBookingId() {
        return bookingId;
    }

    public UserB getUser() {
        return user;
    }

    public Bus getBus() {
        return bus;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getTicketClass() {
        return ticketClass;
    }

    

    public double calculateTotalPrice() {
        double basePrice = 0.0;

        switch (ticketClass) {
            case "Economy":
                basePrice = bus.getEconomyPrice();
                break;
            case "Business":
                basePrice = bus.getBusinessPrice();
                break;
            case "Premium":
                basePrice = bus.getPremiumPrice();
                break;
            default:
                break;
        }

       
        return basePrice;
    }

   public void saveBookingDetailsToFile() {
        // Create a filename based on the user's Aadhar number
        String filename = this.user.getAadharNumber() + ".txt";

        try {
            File directory = new File("BusBookingDetails");
            if (!directory.exists()) {
                directory.mkdirs(); // Creates the directory and any necessary parent directories
            }
            FileWriter fileWriter = new FileWriter(directory.getPath() + File.separator + filename);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Write booking details to the file
            printWriter.println("Booking ID: " + this.bookingId);
            printWriter.println("Name: " + this.user.getName());
            printWriter.println("Email: " + this.user.getEmail());
            
            printWriter.println("Aadhar Number: " + this.user.getAadharNumber()); // Include Aadhar number
            printWriter.println("Airline: " + this.bus.getBusCom());
            printWriter.println("Destination: " + this.bus.getDestination());
            printWriter.println("Departure Time: " + this.bus.getDepartureTime());
            printWriter.println("Seat: Row " + this.row + ", Column " + (char) ('A' + this.col - 1));
            printWriter.println("Ticket Class: " + this.ticketClass);
            printWriter.println("Total  Price: " + this.calculateTotalPrice());
            printWriter.close();

            System.out.println("Booking details saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error while saving booking details to file.");
            e.printStackTrace();
        }
    }
    


    public void establishDatabaseConnection() {
        try {
            String dburl = "jdbc:mysql://localhost:3306/airline";
            String dbuser = "root";
            String dbpass = "";
            //String drivername = "com.mysql.jdbc.Driver";
            Class.forName("com.mysql.cj.jdbc.Driver");

             con = DriverManager.getConnection(dburl, dbuser, dbpass);

            // Check if the Connection is established
            if (con != null) {
                System.out.println("Connection Successful");
            } else {
                System.out.println("Connection Failed");
            }

            // Close the connection when it's no longer needed
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void saveBookingDetailsToDatabase(Connection con) 
    {
                   
        String sql = "INSERT INTO Busbooking (name, Email, AadharNumber, AirLine, Destination, DepartureTime, Seat_Row, Seat_Column, TicketClass, TotalPrice) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getAadharNumber());
            pstmt.setString(4, bus.getBusCom());
            pstmt.setString(5, bus.getDestination());
            pstmt.setString(6, bus.getDepartureTime());
            pstmt.setInt(7, getRow());
            pstmt.setInt(8, getCol());
            pstmt.setString(9, getTicketClass());
            pstmt.setDouble(10, calculateTotalPrice());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Insertion Success of record ");
            } else {
                System.out.println("Insertion failed of record ");
            }
        } catch (SQLException e) {
            System.out.println("Error while executing SQL query:");
            e.printStackTrace();
        }
    
    }
        
    public void processPayment() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your payment details:");
        System.out.print("Card Number: ");
        String cardNumber = input.next();
        System.out.print("Expiration Date (MM/YY): ");
        String expirationDate = input.next();
        System.out.print("CVV: ");
        String cvv = input.next();
        System.out.println("Processing payment...");
        System.out.println("Payment successful!");
    }



}
    




private static void bookBus(Bus[] buses, BookingB[] bookings, Scanner input, Connection con) throws SQLException {
    System.out.println("Please enter your name:");
    String name = input.next();

    System.out.println("Please enter your email:");
    String email = input.next();

    System.out.println("Please enter your Aadhar number:"); // Add Aadhar number input
    String aadharNumber = input.next();

    UserB userb = new UserB(name, email, aadharNumber);

    System.out.println("Please select your destination:");

    for (int i = 0; i < buses.length; i++) {
        System.out.println((i + 1) + ". " + buses[i].getDestination());
    }

    int busOption = input.nextInt();
    // Booking booking1 = null;

    if (busOption >= 1 && busOption <= buses.length) {
        Bus selectedBus = buses[busOption - 1];

        System.out.println("Flights available to " + selectedBus.getDestination() + ":");
        System.out.println("Airline: " + selectedBus.getBusCom());
        System.out.println("Departure Time: " + selectedBus.getDepartureTime());

        System.out.println("Please select your seat:");
        displaySeats(selectedBus.getSeats());

        int row, col;
        do {
            System.out.print("Row: ");
            row = input.nextInt();
            System.out.print("Column: ");
            col = input.nextInt();
        } while (!selectedBus.selectSeat(row, col));

        displaySeats(selectedBus.getSeats());

        System.out.println("Please select your ticket class:");
        System.out.println("1. Economy (Price: " + selectedBus.getEconomyPrice() + ")");
        System.out.println("2. Business (Price: " + selectedBus.getBusinessPrice() + ")");
        System.out.println("3. Premium (Price: " + selectedBus.getPremiumPrice() + ")");
        int classOption = input.nextInt();

        String ticketClass = "";
        switch (classOption) {
            case 1:
                ticketClass = "Economy";
                break;
            case 2:
                ticketClass = "Business";
                break;
            case 3:
                ticketClass = "Premium";
                break;
            default:
                System.out.println("Invalid ticket class option. Defaulting to Economy.");
                ticketClass = "Economy";
                break;
        }
        //booking1 = new Booking(user, selectedBus, row, col, ticketClass);
        BookingB booking3 = new BookingB(userb, selectedBus, row, col, ticketClass);

        double totalPrice = booking3.calculateTotalPrice();
        System.out.println("Total Price: $" + totalPrice);

        System.out.println("Your ticket details:");
        System.out.println("Name: " + userb.getName());
        System.out.println("Email: " + userb.getEmail());
        System.out.println("Bus: " + selectedBus.getBusCom());
        System.out.println("Destination: " + selectedBus.getDestination());
        System.out.println("Departure Time: " + selectedBus.getDepartureTime());
        System.out.println("Seat: Row " + row + ", Column " + (char) ('A' + col - 1));
        System.out.println("Ticket Class: " + ticketClass);

        System.out.println("Proceed with the payment?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int paymentOption = input.nextInt();

        if (paymentOption == 1) {
            // After successfully booking a seat:
            booking3.processPayment();
            booking3.saveBookingDetailsToFile();
            booking3.establishDatabaseConnection();
            Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "");
            booking3.saveBookingDetailsToDatabase(con1);
            //booking.saveBookingDetailsToDatabase();
            //bookings[booking1.getBookingId() - 1] = booking1; // Store the booking in the array
            //System.out.println("Booking ID: " + booking1.getBookingId() + " created.");
        } else if (paymentOption == 2) {
            // Clear the seat selection if the user chooses not to proceed with payment
            selectedBus.clearSeat(row, col);
            System.out.println("Payment canceled. Seat selection cleared.");
        } else {
            System.out.println("Invalid payment option. Payment canceled.");
        }
    } else {
        System.out.println("Invalid flight option. Please try again.");
    }
}
    

private static void displaySeatsB(boolean[][] seats) {
    for (int i = 0; i < seats.length; i++) {
        for (int j = 0; j < seats[i].length; j++) {
            if (seats[i][j]) {
                System.out.print("X ");
            } else {
                System.out.print("O ");
            }
        }
        System.out.println();
    }
}
private static void viewBookedBus(BookingB[] bookings) {
    System.out.println("Booked Bus:");
    for (BookingB booking : bookings) {
        if (booking != null) {
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Name: " + booking.getUser().getName());
            System.out.println("Email: " + booking.getUser().getEmail());
            System.out.println("Bus: " + booking.getBus().getBusCom());
            System.out.println("Destination: " + booking.getBus().getDestination());
            System.out.println("Departure Time: " + booking.getBus().getDepartureTime());
            System.out.println("Seat: Row " + booking.getRow() + ", Column " + (char) ('A' + booking.getCol() - 1));
            System.out.println("Ticket Class: " + booking.getTicketClass());
            
            // Print selected meals
            
            
            System.out.println();
        }
    }
}

private static void searchBooking(BookingB[] bookings, Scanner input) 
{
    System.out.print("Enter Booking ID: ");
    int bookingId = input.nextInt();
    BookingB booking = null;

    for (BookingB b : bookings) {
        if (b != null && b.getBookingId() == bookingId) {
            booking = b;
            break;
        }
    }

    if (booking != null) {
        System.out.println("╔════════════════════════════════╗");
        System.out.println("║          Booking Details        ║");
        System.out.println("╟────────────────────────────────╢");
        System.out.println("║ Booking ID: " + (booking.getBookingId()) + "                      ║");
        System.out.println("║ Name: " + (booking.getUser().getName() ) + "                      ║");
        System.out.println("║ Email: " + (booking.getUser().getEmail()) + "                     ║");
        System.out.println("║ Bus: " + (booking.getBus().getBusCom()) + "               ║");
        System.out.println("║ Destination: " + (booking.getBus().getDestination()) + "       ║");
        System.out.println("║ Departure Time: " + (booking.getBus().getDepartureTime() + "   ║"));
        System.out.println("║ Seat: Row " + (booking.getRow()) + ", Column " + ('A' + booking.getCol() - 1) + "   ║");
        System.out.println("\u2551 Ticket Class:"+booking.getTicketClass());
        System.out.println("╚════════════════════════════════╝");
    } else {
        System.out.println("Booking not found.");
    }
}


private static void searchBookingByFileNameB(Scanner input3) {
    System.out.print("Enter File Name: ");
    String fileNameInput = input3.next();
    File directory = new File("BusBookingDetails");
    
    if (directory.exists() && directory.isDirectory()) {
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().equals(fileNameInput)) {
                // Found a matching file
                displayBookingFromFile(file);
                return;
            }
        }
    } else {
        System.out.println("Directory 'FlightBookingDetails' does not exist or is not a directory.");
    }

    // If no matching file was found
    System.out.println("No booking found for File Name: " + fileNameInput);
}

private static void displayBookingFromFileB(File file) {
    try (Scanner scanner = new Scanner(file)) {
        System.out.println("Booking Details from File: " + file.getName());
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
        }
    } catch (FileNotFoundException e) {
        System.out.println("Error reading booking details from file.");
    }
}



private static void displayBookingDetailsB(BookingB booking) 
{
    System.out.println("Booking ID: " + booking.getBookingId());
    System.out.println("Name: " + booking.getUser().getName());
    System.out.println("Email: " + booking.getUser().getEmail());
    System.out.println("Airline: " + booking.getBus().getBusCom());
    System.out.println("Destination: " + booking.getBus().getDestination());
    System.out.println("Departure Time: " + booking.getBus().getDepartureTime());
    System.out.println("Seat: Row " + booking.getRow() + ", Column " + (char) ('A' + booking.getCol() - 1));
    System.out.println("Ticket Class: " + booking.getTicketClass());
    System.out.println("Total Price: " + booking.calculateTotalPrice());
    System.out.println();
}

static class BRTS {
    private String BRTSNumber;
    private String Departure;
    private String destination;
    private String departureTime;
   

    public BRTS(String BRTSNumber,String Departure, String destination, String departureTime) {
        this.BRTSNumber = BRTSNumber;
        this.Departure=Departure;
        this.destination = destination;
        this.departureTime = departureTime;
    }

    public String getBRTSNumber() {
        return BRTSNumber;
    }
    public String getDeparture() {
        return Departure;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    

    


   

    private double normalPrice = 30.0; // Economy class price
    private double workPrice = 20.0; // Business class price
    private double studentPrice = 15.0; // Premium class price

    public double getnormalPrice() {
        return normalPrice;
    }

    public double getworkPrice() {
        return workPrice;
    }

    public double getstudentPrice() {
        return studentPrice;
    }

   

    

}

static class UserBr {
    private String name;
    private String email;
    private String aadharNumber;

    
    public UserBr(String name, String email, String aadharNumber) {
        this.name = name;
        this.email = email;
        this.aadharNumber = aadharNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

     public String getAadharNumber() {
        return aadharNumber;
    }
}


static class BookingBr {
    private  int bookingCounter = 1;
    private int bookingId;
    private BRTS brts;
    
    private UserBr user;
    private String ticketClass;
    private Connection con;

    public BookingBr(UserBr user, BRTS brts, String ticketClass) {
        this.bookingId = bookingCounter++;
        this.user = user;
        this.brts = brts;
        this.ticketClass = ticketClass;
    }

    

    public int getBookingId() {
        return bookingId;
    }

    public UserBr getUser() {
        return user;
    }

    public BRTS getBrts() {
        return brts;
    }

    

    public String getTicketClass() {
        return ticketClass;
    }

    

    public double calculateTotalPrice() {
        double basePrice = 0.0;

        switch (ticketClass) {
            case "NORMAL":
                basePrice = brts.getnormalPrice();
                break;
            case "WORK":
                basePrice = brts.getworkPrice();
                break;
            case "STUDENT":
                basePrice = brts.getstudentPrice();
                break;
            default:
                break;
        }

       
        return basePrice;
    }

   public void saveBookingDetailsToFile() {
    // Create a filename based on the user's Aadhar number
    String filename = this.user.getAadharNumber() + ".txt";

        try {
            File directory = new File("BRTSBookingDetails");
            if (!directory.exists()) {
                directory.mkdirs(); // Creates the directory and any necessary parent directories
            }
            FileWriter fileWriter = new FileWriter(directory.getPath() + File.separator + filename);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Write booking details to the file
            printWriter.println("Booking ID: " + this.bookingId);
            printWriter.println("Name: " + this.user.getName());
            printWriter.println("Email: " + this.user.getEmail());
            
            printWriter.println("Aadhar Number: " + this.user.getAadharNumber()); // Include Aadhar number
            printWriter.println("Airline: " + this.brts.getBRTSNumber());
            printWriter.println("Departure From:: " + this.brts.getDeparture());
            printWriter.println("Destination: " + this.brts.getDestination());
            printWriter.println("Departure Time: " + this.brts.getDepartureTime());
            
            printWriter.println("Ticket Class: " + this.ticketClass);
            printWriter.println("Total  Price: " + this.calculateTotalPrice());
            printWriter.close();

            System.out.println("Booking details saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error while saving booking details to file.");
            e.printStackTrace();
        }
    }
    


    public void establishDatabaseConnection() {
        try {
            String dburl = "jdbc:mysql://localhost:3306/airline";
            String dbuser = "root";
            String dbpass = "";
            //String drivername = "com.mysql.jdbc.Driver";
            Class.forName("com.mysql.cj.jdbc.Driver");

             con = DriverManager.getConnection(dburl, dbuser, dbpass);

            // Check if the Connection is established
            if (con != null) {
                System.out.println("Connection Successful");
            } else {
                System.out.println("Connection Failed");
            }

            // Close the connection when it's no longer needed
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveBookingDetailsToDatabase(Connection con)
    {
    
                
        String sql = "INSERT INTO BRTSbooking (name, Email, AadharNumber, BRTSNumber,Departure, Destination, Departuretime, TicketClass, TotalPrice) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getAadharNumber());
            pstmt.setString(4, brts.getBRTSNumber());
            pstmt.setString(5, brts.getDeparture());
            pstmt.setString(6, brts.getDestination());
            pstmt.setString(7, brts.getDepartureTime());
            
            pstmt.setString(8, getTicketClass());
            pstmt.setDouble(9, calculateTotalPrice());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Insertion Success of record ");
            } else {
                System.out.println("Insertion failed of record ");
            }
        } catch (SQLException e) {
            System.out.println("Error while executing SQL query:");
            e.printStackTrace();
        }
    }
    
  
    






            

    public void processPayment() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your payment details:");
        System.out.print("Card Number: ");
        String cardNumber = input.next();
        System.out.print("Expiration Date (MM/YY): ");
        String expirationDate = input.next();
        System.out.print("CVV: ");
        String cvv = input.next();
        System.out.println("Processing payment...");
        System.out.println("Payment successful!");
    }



}
    




private static void bookBrts(BRTS[] brtss, BookingBr[] bookingBrts, Scanner input, Connection con) throws SQLException 
{
    System.out.println("Please enter your name:");
    String name = input.next();

    System.out.println("Please enter your email:");
    String email = input.next();

    System.out.println("Please enter your Aadhar number:"); // Add Aadhar number input
    String aadharNumber = input.next();

    UserBr userbr = new UserBr(name, email, aadharNumber);

    System.out.println("Please select your destination:");

    for (int i = 0; i < brtss.length; i++) {
        System.out.println((i + 1) + ". " + brtss[i].getDestination());
    }

    int brtsOption = input.nextInt();
    // Booking booking1 = null;

    if (brtsOption >= 1 && brtsOption <= brtss.length) {
        BRTS selectedbrts = brtss[brtsOption - 1];
        System.out.println("BRTS availabale From"+selectedbrts.getDeparture()+":");
        System.out.println("BRTS available to " + selectedbrts.getDestination() + ":");
        System.out.println("BrtsNumber: " + selectedbrts.getBRTSNumber());
        System.out.println("Departure Time: " + selectedbrts.getDepartureTime());

        
        

        System.out.println("Please select your ticket class:");
        System.out.println("1. Economy (Price: " + selectedbrts.getnormalPrice() + ")");
        System.out.println("2. Business (Price: " + selectedbrts.getworkPrice() + ")");
        System.out.println("3. Premium (Price: " + selectedbrts.getstudentPrice() + ")");
        int classOption = input.nextInt();

        String ticketClass = "";
        switch (classOption) {
            case 1:
                ticketClass = "Normal";
                break;
            case 2:
                ticketClass = "Work";
                break;
            case 3:
                ticketClass = "Student";
                break;
            default:
                System.out.println("Invalid ticket class option. Defaulting to Economy.");
                ticketClass = "Economy";
                break;
        }
        //booking1 = new Booking(user, selectedBus, row, col, ticketClass);
        BookingBr booking4= new BookingBr(userbr, selectedbrts, ticketClass);

        double totalPrice = booking4.calculateTotalPrice();
        System.out.println("Total Price: $" + totalPrice);

        System.out.println("Your ticket details:");
        System.out.println("Name: " + userbr.getName());
        System.out.println("Email: " + userbr.getEmail());
        System.out.println("Airline: " + selectedbrts.getBRTSNumber());
        System.out.println("Destination: " + selectedbrts.getDestination());
        System.out.println("Departure Time: " + selectedbrts.getDepartureTime());
        
        System.out.println("Ticket Class: " + ticketClass);

        System.out.println("Proceed with the payment?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int paymentOption = input.nextInt();

        if (paymentOption == 1) {
            
            booking4.processPayment();
            booking4.saveBookingDetailsToFile();
            booking4.establishDatabaseConnection();
            Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "");
            booking4.saveBookingDetailsToDatabase(con1);
            displayBookingDetailsBr(booking4);
            //booking.saveBookingDetailsToDatabase();
            //bookingBrts[booking2.getBookingId() - 1] = booking2; // Store the booking in the array
            //System.out.println("Booking ID: " + booking2.getBookingId() + " created.");
        } else if (paymentOption == 2) {
            // Clear the seat selection if the user chooses not to proceed with payment
            
            System.out.println("Payment canceled. Seat selection cleared.");
        } else {
            System.out.println("Invalid payment option. Payment canceled.");
        }
    } else {
        System.out.println("Invalid flight option. Please try again.");
    }
}



private static void viewBookedBrts(BookingBr[] bookingsss) {
    System.out.println("Booked Bus:");
    for (BookingBr booking : bookingsss) {
        if (booking != null) {
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Name: " + booking.getUser().getName());
            System.out.println("Email: " + booking.getUser().getEmail());
            System.out.println("Airline: " + booking.getBrts().getBRTSNumber());
            System.out.println("Destination: " + booking.getBrts().getDestination());
            System.out.println("Departure Time: " + booking.getBrts().getDepartureTime());
            
            System.out.println("Ticket Class: " + booking.getTicketClass());
            
            // Print selected meals
            
            
            System.out.println();
        }
    }
}

    private static void searchBookingBr(BookingBr[] bookings, Scanner input) 
    {
        System.out.print("Enter Booking ID: ");
        int bookingId = input.nextInt();
        BookingBr booking = null;

        for (BookingBr b : bookings) {
            if (b != null && b.getBookingId() == bookingId) {
                booking = b;
                break;
            }
    }

    if (booking != null) 
    {
        System.out.println("╔════════════════════════════════╗");
        System.out.println("║          Booking Details        ║");
        System.out.println("╟────────────────────────────────╢");
        System.out.println("║ Booking ID: " + (booking.getBookingId()) + "                      ║");
        System.out.println("║ Name: " + (booking.getUser().getName() ) + "                      ║");
        System.out.println("║ Email: " + (booking.getUser().getEmail()) + "                     ║");
        System.out.println("║ Airline: " + (booking.getBrts().getBRTSNumber()) + "               ║");
        System.out.println("║ Destination: " + (booking.getBrts().getDestination()) + "       ║");
        System.out.println("║ Departure Time: " + (booking.getBrts().getDepartureTime() + "   ║"));
        System.out.println("\u2551 Ticket Class:"+booking.getTicketClass());
        System.out.println("╚════════════════════════════════╝");
    } else {
        System.out.println("Booking not found.");
    }
}


private static void searchBookingByFileNameBr(Scanner input) 
{
    System.out.print("Enter File Name: ");
    String fileNameInput = input.next();
    File directory = new File("TrainBookingDetails");
    
    if (directory.exists() && directory.isDirectory()) {
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().equals(fileNameInput)) {
                // Found a matching file
                displayBookingFromFile(file);
                return;
            }
        }
    } else {
        System.out.println("Directory 'FlightBookingDetails' does not exist or is not a directory.");
 
    }

    // If no matching file was found
    System.out.println("No booking found for File Name: " + fileNameInput);
}

private static void displayBookingFromFileBr(File file) 
{
    try (Scanner scanner = new Scanner(file)) {
        System.out.println("Booking Details from File: " + file.getName());
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
        }
    } catch (FileNotFoundException e) {
        System.out.println("Error reading booking details from file.");
    }
}



private static void displayBookingDetailsBr(BookingBr booking) 
{
    System.out.println("╔════════════════════════════════╗");
        System.out.println("║          Booking Details        ║");
        System.out.println("╟────────────────────────────────╢");
        System.out.println("║ Booking ID: " + (booking.getBookingId()) + "                      ║");
        System.out.println("║ Name: " + (booking.getUser().getName() ) + "                      ║");
        System.out.println("║ Email: " + (booking.getUser().getEmail()) + "                     ║");
        System.out.println("║ Airline: " + (booking.getBrts().getBRTSNumber()) + "               ║");
        System.out.println("║ Destination: " + (booking.getBrts().getDestination()) + "       ║");
        System.out.println("║ Departure Time: " + (booking.getBrts().getDepartureTime() + "   ║"));
        System.out.println("\u2551 Ticket Class:"+booking.getTicketClass());
        System.out.println("╚════════════════════════════════╝");
}


























static class Final
{
    public  static void main(String[] args)throws SQLException {
        Scanner sc=new Scanner(System.in);
        while (true) {
            System.out.println("=======================================");
            System.out.println("|        Welcome To Booking Console   |");
            System.out.println("=======================================");
            System.out.println("| Options:                            |");
            System.out.println("| 1. Train Ticket Booking             |");
            System.out.println("| 2. Movie Ticket Booking             |");
            System.out.println("| 3. Flight Ticket Booking            |");
            System.out.println("| 4. Bus Ticket Booking               |");
            System.out.println("| 5. BRTS Ticket Booking              |");
            System.out.println("| 6. Exit App                         |");
            System.out.println("=======================================");       
             int ch=sc.nextInt();

            switch (ch) {
                case 1:
                    Train[] trains = {
                        new Train("Karnavti", "Mumbai", "10:00 AM"),
                        new Train("VandeBharat", "Surat", "1:00 PM"),
                        new Train("Sosyo", "Jamnagar", "4:00 PM"),
                        new Train("Intercity", "Rajkot", "7:00 PM"),
                        new Train("Asarva", "udaipur", "10:00 PM")
                    };
            
                    for (Train train : trains) {
                        train.loadSeatDataFromFile(); // Load seat data for each flight
                    }
                
            
                    Booking[] bookingTrain = new Booking[trains.length * 60];
            
                    Scanner input = new Scanner(System.in);
            
                    System.out.println("Welcome to the Bus booking app!");
            
                    while (true) {
                        System.out.println("=======================================");
                        System.out.println("|        Welcome To Bus Booking       |");
                        System.out.println("=======================================");
                        System.out.println("| Please select an option:            |");
                        System.out.println("| 1. Book a Bus                       |");
                        System.out.println("| 2. View booked Bus                  |");
                        System.out.println("| 3. Search for booking by Aadhar No. |");
                        System.out.println("| 4. Exit                             |");
                        System.out.println("=======================================");
            
                        int option = input.nextInt();
            
                        if (option == 1) {
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "");
                            bookTrain(trains, bookingTrain, input,con);
                        } else if (option == 2) {
                            viewBookedTrain(bookingTrain);
                        }else if (option == 3) {
                            searchBookingByFileName( input);
                        
                        } else if (option == 4) {
                            System.out.println("Thank you for using our app. Have a great day!");
                            break;
                        } else {
                            System.out.println("Invalid option. Please try again.");
                        }
                        

                    }
                    break;
                case 2:
                    Movie[] movies = {
                        new Movie("JAWAN", "RAJA MOURI", "10:00 AM"),
                        new Movie("BHUBALI", "YASH FIlms", "1:00 PM"),
                        new Movie("KABIR SINGH", "T-series", "4:00 PM"),
                        new Movie("Pushpa", "PennStudio", "7:00 PM"),
                        new Movie("KGF 2", "Universal", "10:00 PM")
                    };

                    for (Movie movie : movies) {
                        movie.loadSeatDataFromFile(); // Load seat data for each flight
                    }
                

                    BookingM[] bookingMovie = new BookingM[movies.length * 60];

                    Scanner input1 = new Scanner(System.in);

                    System.out.println("Welcome to the Movie booking app!");

                    while (true) {
                        System.out.println("=======================================");
                        System.out.println("|        Welcome To Movie Booking     |");
                        System.out.println("=======================================");
                        System.out.println("| Please select an option:            |");
                        System.out.println("| 1. Book a Movie Ticket              |");
                        System.out.println("| 2. View booked Ticket               |");
                        System.out.println("| 3. Search for booking by Aadhar No. |");
                        System.out.println("| 4. Exit                             |");
                        System.out.println("=======================================");

                        int option = input1.nextInt();

                        if (option == 1) {
                        try {
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "");
            

                                bookMovie(movies, bookingMovie, input1,con);
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else if (option == 2) {
                            viewBookedMovie(bookingMovie);
                        }else if (option == 3) {
                            searchBookingByFileNameM( input1);
                        
                        } else if (option == 4) {
                            System.out.println("Thank you for using our app. Have a great day!");
                            break;
                            
                        } else {
                            System.out.println("Invalid option. Please try again.");
                        }
                        
                        //Now, when you select option 3, you can search for bookings by Aadhar number, and option 4 will display files in the "FlightBookingDetails" directory.
                    }
                    break;
                case 3:
                    Flight[] flights = {
                        new Flight("Airline A", "New York", "10:00 AM"),
                        new Flight("Airline B", "Los Angeles", "1:00 PM"),
                        new Flight("Airline C", "Chicago", "4:00 PM"),
                        new Flight("Airline D", "Miami", "7:00 PM"),
                        new Flight("Airline E", "Seattle", "10:00 PM")
                    };

                    for (Flight flight : flights) {
                        flight.loadSeatDataFromFile(); // Load seat data for each flight
                    }
            

                    BookingF[] bookings = new BookingF[flights.length * 60];

                    Scanner input2 = new Scanner(System.in);

                    System.out.println("Welcome to the airline booking app!");

                    while (true) 
                    {
                    System.out.println("=======================================");
                    System.out.println("|        Welcome To Flight Booking    |");
                    System.out.println("=======================================");
                    System.out.println("| Please select an option:            |");
                    System.out.println("| 1. Book a flight                    |");
                    System.out.println("| 2. View booked flights              |");
                    System.out.println("| 3. Search for booking by Aadhar No. |");
                    System.out.println("| 4. Exit                             |");
                    System.out.println("=======================================");


                        int option = input2.nextInt();

                        if (option == 1) {
                        try {
                                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "");
                                bookFlight(flights,bookings, input2,con);
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } 
                        else if (option == 2) {
                            viewBookedFlights(bookings);
                        }else if (option == 3) {
                            searchBookingByFileName( input2);
                        
                        } else if (option == 4) {
                            System.out.println("Thank you for using our app. Have a great day!");
                            break;
                        } else {
                            System.out.println("Invalid option. Please try again.");
                        }
                        
                    }
                    break;
                case 4:
                    Bus[] buses = {
                        new Bus("Patel", "Shirdi", "10:00 AM"),
                        new Bus("GSRTC", "Ambaji", "1:00 PM"),
                        new Bus("Bothra", "Kota", "4:00 PM"),
                        new Bus("Falcon", "Jaipur", "7:00 PM"),
                        new Bus("ParshvNath", "HariDwar", "10:00 PM")
                    };

                    for (Bus bus : buses) {
                        bus.loadSeatDataFromFile(); // Load seat data for each flight
                    }
                

                    BookingB[] bookingss = new BookingB[buses.length * 60];

                    Scanner input3 = new Scanner(System.in);

                    System.out.println("Welcome to the Bus booking app!");

                    while (true) {
                        System.out.println("=======================================");
                        System.out.println("|        Welcome To Bus Booking       |");
                        System.out.println("=======================================");
                        System.out.println("| Please select an option:            |");
                        System.out.println("| 1. Book a Bus                       |");
                        System.out.println("| 2. View booked Bus                  |");
                        System.out.println("| 3. Search for booking by Aadhar No. |");
                        System.out.println("| 4. Exit                             |");
                        System.out.println("=======================================");

                        int option = input3.nextInt();

                        if (option == 1) {
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "");
                            bookBus(buses, bookingss, input3,con);
                        } else if (option == 2) {
                            viewBookedBus(bookingss);
                        }else if (option == 3) {
                            searchBookingByFileName( input3);
                        
                        } else if (option == 4) {
                            System.out.println("Thank you for using our app. Have a great day!");
                            break;
                        } else {
                            System.out.println("Invalid option. Please try again.");
                        }
                        
                    }
                    break;
                case 5:
                    BRTS[] brtss = {
                        new BRTS("1D", "ManiNagar","Ghuma Gam", "10:00 AM"),
                        new BRTS("1E", "GhumA Gam","JaiManagal", "1:00 PM"),
                        new BRTS("1S", "ManiNagar","Naroda", "4:00 PM"),
                        new BRTS("1U", "Sanand Chokdi","Nehrunagar", "7:00 PM"),
                        new BRTS("2S", "R.T.O circle ","L.D College", "10:00 PM")
                        };

                    
                

                        BookingBr[] bookingBrts = new BookingBr[brtss.length * 60];

                        Scanner input4 = new Scanner(System.in);

                        System.out.println("Welcome to the Brts booking app!");

                    while (true) 
                    {
                        System.out.println("=======================================");
                        System.out.println("|        Welcome To Brts  Booking     |");
                        System.out.println("=======================================");
                        System.out.println("| Please select an option:            |");
                        System.out.println("| 1. Book a BRTS                      |");
                        System.out.println("| 2. View booked Brts                 |");
                        System.out.println("| 3. Search for booking by Aadhar No. |");
                        System.out.println("| 4. Exit                             |");
                        System.out.println("=======================================");

                        int option = input4.nextInt();

                        if (option == 1) {
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/airline", "root", "");
                            bookBrts(brtss, bookingBrts, input4,con);
                        } else if (option == 2) {
                            viewBookedBrts(bookingBrts);
                        }else if (option == 3) {
                            searchBookingByFileName( input4);
                        
                        } else if (option == 4) {
                            System.out.println("Thank you for using our app. Have a great day!");
                            break;
                        } else {
                            System.out.println("Invalid option. Please try again.");
                        }
                    }
                    break;
                    


                    
                case 6:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                
                default:
                    break;
                    
                    
            }

        }
    }
}
}

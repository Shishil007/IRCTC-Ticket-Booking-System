package org.ticket.booking;

import org.ticket.booking.entities.Train;
import org.ticket.booking.entities.User;
import org.ticket.booking.services.UserBookingService;
import org.ticket.booking.utils.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {

    public static void main(String[] args) {

        System.out.println("Running Train Booking System");
        Scanner sc = new Scanner(System.in);

        int option = 0;
        UserBookingService userBookingService;

        try {
            userBookingService = new UserBookingService();
        } catch (IOException ex) {
            System.out.println("Something Went Wrong");
            return;
        }

        while (option != 7) {
            System.out.println("\nPlease Choose Option:");
            System.out.println("1. SignUp");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");

            option = sc.nextInt();
            sc.nextLine(); // 🔴 consume newline

            switch (option) {

                case 1:
                    System.out.println("Enter Username:");
                    String userName = sc.nextLine();

                    System.out.println("Enter Password:");
                    String password = sc.nextLine();

                    User user = new User(
                            userName,
                            password,
                            UserServiceUtil.hashPassword(password),
                            new ArrayList<>(),
                            UUID.randomUUID().toString()
                    );

                    userBookingService.signUp(user);
                    System.out.println("Signup successful!");
                    break;

                case 2:
                    System.out.println("Enter Username:");
                    String loginName = sc.nextLine();

                    System.out.println("Enter Password:");
                    String loginPassword = sc.nextLine();

                    User loginUser = new User(
                            loginName,
                            loginPassword,
                            UserServiceUtil.hashPassword(loginPassword),
                            new ArrayList<>(),
                            UUID.randomUUID().toString()
                    );

                    try {
                        userBookingService = new UserBookingService(loginUser);
                        if (userBookingService.loginUser()) {
                            System.out.println("Login successful!");
                        } else {
                            System.out.println("Invalid credentials");
                        }
                    } catch (IOException ex) {
                        System.out.println("User not found");
                    }
                    break;

                case 3:
                    System.out.println("Fetching your booking details:");
                    userBookingService.fetchBookings();   // ✅ fixed name
                    break;

                case 4:
                    System.out.println("Enter Source Station:");
                    String source = sc.nextLine();

                    System.out.println("Enter Destination Station:");
                    String destination = sc.nextLine();

                    userBookingService
                            .getTrains(source, destination)
                            .forEach(System.out::println);
                    break;

                case 5:
                    System.out.println("Enter Source Station:");
                    String src = sc.nextLine();

                    System.out.println("Enter Destination Station:");
                    String dest = sc.nextLine();

                    List<Train> trains = userBookingService.getTrains(src, dest);

                    if (trains.isEmpty()) {
                        System.out.println("No trains found.");
                        break;
                    }

                    System.out.println("Available Trains:");
                    for (int i = 0; i < trains.size(); i++) {
                        System.out.println(i + " -> " + trains.get(i));
                    }

                    System.out.println("Select Train Index:");
                    int trainIndex = sc.nextInt();
                    sc.nextLine();

                    if (trainIndex < 0 || trainIndex >= trains.size()) {
                        System.out.println("Invalid train selection");
                        break;
                    }

                    Train selectedTrain = trains.get(trainIndex);

                    List<List<Integer>> seats = userBookingService.fetchSeats(selectedTrain);

                    System.out.println("Seat Layout (0 = free, 1 = booked):");
                    for (int i = 0; i < seats.size(); i++) {
                        System.out.println(i + " : " + seats.get(i));
                    }

                    System.out.println("Enter row number:");
                    int row = sc.nextInt();

                    System.out.println("Enter seat number:");
                    int seat = sc.nextInt();
                    sc.nextLine();

                    Boolean booked = userBookingService.bookTrainSeat(selectedTrain, row, seat);

                    if (booked) {
                        System.out.println("Seat booked successfully!");
                    } else {
                        System.out.println("Seat booking failed.");
                    }
                    break;


                case 6:
                    System.out.println("Enter Ticket ID to cancel:");
                    String ticketId = sc.nextLine();

                    Boolean cancelled = userBookingService.cancelBooking(ticketId);

                    if (cancelled) {
                        System.out.println("Booking cancelled successfully.");
                    } else {
                        System.out.println("Failed to cancel booking.");
                    }
                    break;

                case 7:
                    System.out.println("User Logged out");
                    break;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}

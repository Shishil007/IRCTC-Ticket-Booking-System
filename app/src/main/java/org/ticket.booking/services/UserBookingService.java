package org.ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ticket.booking.entities.Train;
import org.ticket.booking.entities.User;
import org.ticket.booking.utils.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<User> userList;
    private User user;

    // ✅ Path consistent with TrainService
    private static final String USERS_DB_PATH =
            "app/src/main/java/org/ticket/booking/localDB/users.json";

    // ---------- CONSTRUCTORS ----------

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserListFromFile();
    }

    public UserBookingService() throws IOException {
        loadUserListFromFile();
    }

    // ---------- FILE OPERATIONS ----------

    private void loadUserListFromFile() throws IOException {
        File usersFile = new File(USERS_DB_PATH);
        userList = objectMapper.readValue(
                usersFile,
                new TypeReference<List<User>>() {}
        );
    }

    private void saveUserListToFile() throws IOException {
        objectMapper.writeValue(new File(USERS_DB_PATH), userList);
    }

    // ---------- AUTH ----------

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream()
                .filter(u ->
                        u.getName().equals(user.getName()) &&
                                UserServiceUtil.checkPassword(
                                        user.getPassword(),
                                        u.getHashedPassword()
                                )
                )
                .findFirst();

        return foundUser.isPresent();
    }

    public Boolean signUp(User newUser) {
        try {
            userList.add(newUser);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException e) {
            return Boolean.FALSE;
        }
    }

    // ---------- BOOKINGS ----------

    public void fetchBookings() {
        Optional<User> userFetched = userList.stream()
                .filter(u ->
                        u.getName().equals(user.getName()) &&
                                UserServiceUtil.checkPassword(
                                        user.getPassword(),
                                        u.getHashedPassword()
                                )
                )
                .findFirst();

        userFetched.ifPresent(User::printTickets);
    }

    public Boolean cancelBooking(String ticketId) {

        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }

        boolean removed = user.getTicketsBooked()
                .removeIf(ticket ->
                        ticket.getTicketId().equals(ticketId)
                );

        if (removed) {
            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
            return Boolean.TRUE;
        } else {
            System.out.println("No ticket found with ID " + ticketId);
            return Boolean.FALSE;
        }
    }

    // ---------- TRAINS ----------

    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();

            if (row < 0 || row >= seats.size()) return Boolean.FALSE;
            if (seat < 0 || seat >= seats.get(row).size()) return Boolean.FALSE;

            if (seats.get(row).get(seat) == 0) {
                seats.get(row).set(seat, 1);
                train.setSeats(seats);
                trainService.addTrain(train);
                return Boolean.TRUE;
            }

            return Boolean.FALSE;

        } catch (IOException e) {
            return Boolean.FALSE;
        }
    }
}

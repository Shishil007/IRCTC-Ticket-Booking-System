package org.ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {

    private List<Train> trainList;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TRAIN_DB_PATH =
            "app/src/main/java/org/ticket/booking/localDB/trains.json";

    public TrainService() throws IOException {
        loadTrainListFromFile();
    }

    private void loadTrainListFromFile() throws IOException {
        trainList = objectMapper.readValue(
                new File(TRAIN_DB_PATH),
                new TypeReference<List<Train>>() {}
        );
    }

    private void saveTrainListToFile() throws IOException {
        objectMapper.writeValue(new File(TRAIN_DB_PATH), trainList);
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream()
                .filter(train -> isValidTrain(train, source, destination))
                .collect(Collectors.toList());
    }

    public void addTrain(Train newTrain) throws IOException {
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i ->
                        trainList.get(i)
                                .getTrainId()
                                .equalsIgnoreCase(newTrain.getTrainId())
                )
                .findFirst();

        if (index.isPresent()) {
            trainList.set(index.getAsInt(), newTrain);
        } else {
            trainList.add(newTrain);
        }

        saveTrainListToFile();
    }

    private boolean isValidTrain(Train train, String source, String destination) {
        List<String> stations = train.getStations();
        if (stations == null || stations.isEmpty()) return false;

        int sourceIndex = stations.indexOf(source.toLowerCase());
        int destinationIndex = stations.indexOf(destination.toLowerCase());

        return sourceIndex != -1 &&
                destinationIndex != -1 &&
                sourceIndex < destinationIndex;
    }
}

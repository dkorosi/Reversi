package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StatisticRow {
    private final StringProperty gameType;
    private final StringProperty firstName;
    private final StringProperty secondName;
    private final StringProperty winnerName;

    public String getGameType() {
        return gameType.get();
    }

    public StringProperty gameTypeProperty() {
        return gameType;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getSecondName() {
        return secondName.get();
    }

    public StringProperty secondNameProperty() {
        return secondName;
    }

    public String getWinnerName() {
        return winnerName.get();
    }

    public StringProperty winnerNameProperty() {
        return winnerName;
    }

    public StatisticRow(String gameType, String firstName, String secondName, String winnerName) {
        this.gameType = new SimpleStringProperty(gameType);
        this.firstName = new SimpleStringProperty(firstName);
        this.secondName = new SimpleStringProperty(secondName);
        this.winnerName = new SimpleStringProperty(winnerName);
    }

    public static StatisticRow createStatisticRow(String line) {
        String[] split = line.split(",");

        return new StatisticRow(split[0], split[1], split[2], split[3]);
    }

    public String toCsvLine() {
        return gameType.get() + "," + firstName.get() + "," + secondName.get() + "," + winnerName.get() + "\n";
    }

}

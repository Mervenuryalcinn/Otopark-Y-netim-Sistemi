package views;

import controllers.Observer;
import controllers.Subject;
import java.util.ArrayList;
import java.util.List;

public class ParkArea implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private String name;
    private String email;
    private String parkArea;

    public void setParkArea(String name, String email, String parkArea) {
        this.name = name;
        this.email = email;
        this.parkArea = parkArea;
        notifyObservers();  // Değişiklik olduğunda gözlemcilere bildirim gönderir
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(name, email, parkArea);
        }
    }
}

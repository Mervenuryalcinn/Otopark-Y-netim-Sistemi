package controllers;

// Subject Arayüzü
public interface Subject {
    void addObserver(Observer observer);   // Gözlemciyi eklemek
    void removeObserver(Observer observer); // Gözlemciyi kaldırmak
    void notifyObservers();  // Gözlemcilere bildirim göndermek
}

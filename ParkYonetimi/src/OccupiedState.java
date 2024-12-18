public class OccupiedState implements ParkingSpotState {
    @Override
    public void handleRequest(ParkingSpot spot) {
        System.out.println(spot.getSpotNumber() + " park yeri doludur.");
    }
}

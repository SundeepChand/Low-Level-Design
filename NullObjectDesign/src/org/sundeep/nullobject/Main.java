package org.sundeep.nullobject;

abstract class Vehicle {
    abstract public String getName();
}

class Car extends Vehicle {
    public String getName() {
        return "CAR";
    }
}

// NullVehicle serves as the default null value for Vehicle class which handles the default handling of Vehicle class.
// This needs to be passed instead of null wherever applicable as it simplifies the null handling.
// In Java, calling a method on a null object will result in NullPointerException, and only static methods
// can be invoked via null instance of an object (though it is discouraged). Hence this pattern is followed to simplify
// null checks in the codebase.
class NullVehicle extends Vehicle {
    public String getName() {
        return "";
    }
}

class VehicleFactory {
    public static Vehicle createVehicle(String vehicleType) {
        switch (vehicleType) {
            case "CAR":
                return new Car();
            default:
                return new NullVehicle();
                // Returning only null will result in NullPointerException.
//                return null;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Vehicle car = VehicleFactory.createVehicle("CAR");
        Vehicle bike = VehicleFactory.createVehicle("BIKE");

        System.out.println("Car name: " + car.getName());
        System.out.println("Bike name: " + bike.getName());
    }
}

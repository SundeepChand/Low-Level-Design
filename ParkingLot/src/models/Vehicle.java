package models;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public abstract class Vehicle {
    @NonNull private String number;
    @NonNull private VehicleType vehicleType;
}

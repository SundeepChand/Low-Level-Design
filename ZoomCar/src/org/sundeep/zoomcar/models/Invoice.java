package org.sundeep.zoomcar.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Invoice {
	private double price;
	private PaymentStatus paymentStatus;
}

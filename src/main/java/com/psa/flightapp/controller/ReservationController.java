package com.psa.flightapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.psa.flightapp.dto.ReservationRequest;
import com.psa.flightapp.entities.Flight;
import com.psa.flightapp.entities.Reservation;
import com.psa.flightapp.repositories.FlightRepository;
import com.psa.flightapp.services.ReservationService;
import com.psa.flightapp.utilities.EmailUtil;
import com.psa.flightapp.utilities.PdfGenerator;

@Controller
public class ReservationController {

//	private static String filePath="C:\\Users\\Pankaj\\Documents\\workspace-spring-tool-suite-4-4.7.0.RELEASE\\flight_reservation_app_6\\src\\tickets\\booking";

	//"D:\pdf_flights"
	private static String filePath = "D:\\SkyBooking\\src\\tickets\\";

	@Autowired
	private EmailUtil emailUtil;


	@Autowired
	private FlightRepository flightRepo;

	@Autowired
	private ReservationService reservationService;

	@RequestMapping("/showCompleteReservation")
	public String completeReservation(@RequestParam("flightId") Long flightId,ModelMap modelMap) {
		System.out.println("showcomplete");
		System.out.println(flightId);

		Optional<Flight> findById = flightRepo.findById(flightId);
		Flight flight = findById.get();
		System.out.println("findbyid ::::"+findById);
		System.out.println(flight);


		modelMap.addAttribute("flights", flight);
		System.out.println("Done Controller\n");
		System.out.println(flightId);
		return "showCompleteReservation";
 	}



	@PostMapping("/confirmRegistration")
	public String confirmRegistration(ReservationRequest request,ModelMap modelMap) {
		System.out.println("11");
		System.out.println("Request: " + request);
		System.out.println(request.getId());
		System.out.println(request.getEmail());

		Reservation reservation = reservationService.bookFlight(request);
		System.out.println(112);
		PdfGenerator pdf = new PdfGenerator();
		System.out.println(1123);
		pdf.generatePDF(filePath+reservation.getId()+".pdf", reservation.getPassenger().getFirstName(), reservation.getPassenger().getEmail(), reservation.getPassenger().getPhone(), reservation.getFlight().getOperatingAirlines(), reservation.getFlight().getDateOfDeparture(), reservation.getFlight().getDepartureCity(), reservation.getFlight().getArrivalCity());
		System.out.println(11223);
		modelMap.addAttribute("reservationId", reservation.getId());
		System.out.println(12);
		EmailUtil util = new EmailUtil();
		String attachment = filePath+reservation.getId()+".pdf";
		System.out.println(13);
		emailUtil.sendItinerary(request.getEmail(), attachment);
		System.out.println(22);
		return "finalConfirmation";
	}
}
//	@RequestMapping("/confirmRegistration")
//	public String confirmRegistration(ReservationRequest request, ModelMap modelMap) {
//		System.out.println("Starting reservation confirmation");
//
//		Reservation reservation = reservationService.bookFlight(request);
//		if (reservation == null) {
//			System.out.println("Reservation creation failed");
//			modelMap.addAttribute("message", "Reservation could not be created.");
//			return "error";
//		}
//
//		System.out.println("Reservation created with ID: " + reservation.getId());
//
//		PdfGenerator pdf = new PdfGenerator();
//		String pdfFilePath = filePath + reservation.getId() + ".pdf";
//		try {
//			pdf.generatePDF(pdfFilePath,
//					reservation.getPassenger().getFirstName(),
//					reservation.getPassenger().getEmail(),
//					reservation.getPassenger().getPhone(),
//					reservation.getFlight().getOperatingAirlines(),
//					reservation.getFlight().getDateOfDeparture(),
//					reservation.getFlight().getDepartureCity(),
//					reservation.getFlight().getArrivalCity());
//			System.out.println("PDF generated at: " + pdfFilePath);
//		} catch (Exception e) {
//			System.out.println("Error generating PDF: " + e.getMessage());
//			modelMap.addAttribute("message", "Error generating PDF.");
//			return "error";
//		}
//
//		modelMap.addAttribute("reservationId", reservation.getId());
//
//		String attachment = filePath + reservation.getId() + ".pdf";
//		try {
//			emailUtil.sendItinerary(request.getEmail(), attachment);
//			System.out.println("Email sent to: " + request.getEmail());
//		} catch (Exception e) {
//			System.out.println("Error sending email: " + e.getMessage());
//			modelMap.addAttribute("message", "Error sending email.");
//			return "error";
//		}
//
//		System.out.println("Reservation confirmed successfully");
//		return "finalConfirmation";
//	}
//}

package com.movieflix.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.movieflix.entities.User;
import com.movieflix.services.UserServices;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {
	@Autowired
	UserServices service;
	
	@PostMapping("/createOrder")
	@ResponseBody
	public String createOrder() {
		Order order = null;
		try {
			RazorpayClient razorpay = new RazorpayClient("rzp_test_A9p6rhUUEHx94W", 
					"ujE59291HJ9ZYb0RjeONCJoD");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount",10000);
			orderRequest.put("currency","INR");
			orderRequest.put("receipt", "receipt#1");
			JSONObject notes = new JSONObject();
			notes.put("notes_key_1","Tea, Earl Grey, Hot");
			orderRequest.put("notes",notes);
		
			order = razorpay.orders.create(orderRequest);
		}
		catch(Exception e) {
			System.out.println("exception while creating order");
		}
		return order.toString();
	}
	
	@PostMapping("/verify")
	@ResponseBody
	public boolean verifyPayment(@RequestParam  String orderId, @RequestParam String paymentId,
											@RequestParam String signature) {
	    try {
	        // Initialize Razorpay client with your API key and secret
	        @SuppressWarnings("unused")
			RazorpayClient razorpayClient = new RazorpayClient("rzp_test_A9p6rhUUEHx94W", 
	        								"ujE59291HJ9ZYb0RjeONCJoD");
	        // Create a signature verification data string
	        String verificationData = orderId + "|" + paymentId;

	        // Use Razorpay's utility function to verify the signature
	        boolean isValidSignature = Utils.verifySignature(verificationData, signature, "ujE59291HJ9ZYb0RjeONCJoD");

	        return isValidSignature;
	    } catch (RazorpayException e) {
	        e.printStackTrace();
	        return false;
	    }
	}	
	
	//payment-success -> update to premium user
	@GetMapping("payment-success")
	public String paymentSuccess(HttpSession session, RedirectAttributes redirectedAttributes){
		
		String email = (String) session.getAttribute("email");
		User user = service.getUser(email);
		user.setPremium(true);
		service.updateUser(user);
		redirectedAttributes.addFlashAttribute("message", "Payment completed successfully, Enjoy your subscription!");
		return "redirect:/home";
	}
		
	//payment-failure -> redirect to login 
	@GetMapping("payment-failure")
	public String paymentFailure(RedirectAttributes redirectedAttributes){
		//payment-error page
		redirectedAttributes.addFlashAttribute("message", "The payment has been failed.");
		return "redirect:/home";
	}
	
}
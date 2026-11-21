package com.gcc.communityHall.paymentService.service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class TransactionNumberGenerator {

	public String generateTransactionNumber() {
        // Define the length of the transaction number
        int length = 12;

        // Define characters to be used in the transaction number
        String characters = "0123456789";

        // Create a StringBuilder to store the transaction number
        StringBuilder sb = new StringBuilder();

        // Create an instance of Random class
        Random random = new Random();

        // Generate the transaction number
        for (int i = 0; i < length; i++) {
            // Generate a random index within the range of characters
            int randomIndex = random.nextInt(characters.length());

            // Append the character at the random index to the transaction number
            sb.append(characters.charAt(randomIndex));
        }


        // Convert StringBuilder to String and return
        return sb.toString();
    }

}

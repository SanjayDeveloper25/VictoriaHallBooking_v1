package com.gcc.communityHall.configuration;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for handling UUID-based ref_id for booking verification
 * This provides a secure and URL-friendly approach for booking reference IDs
 */
public class UuidBookingUtil {
    
    // In-memory cache to store UUID to booking ID mapping
    // In production, this should be replaced with a database table or Redis
    private static final Map<String, Integer> uuidToBookingIdMap = new ConcurrentHashMap<>();
    private static final Map<Integer, String> bookingIdToUuidMap = new ConcurrentHashMap<>();
    
    /**
     * Generate a UUID ref_id for a booking ID
     * @param bookingId The original booking ID
     * @return UUID string that can be used as ref_id
     */
    public static String generateRefIdForBooking(int bookingId) {
        String uuid = UUID.randomUUID().toString();
        
        // Store the mapping
        uuidToBookingIdMap.put(uuid, bookingId);
        bookingIdToUuidMap.put(bookingId, uuid);
        
        return uuid;
    }
    
    /**
     * Get booking ID from ref_id UUID
     * @param refId The UUID string from URL
     * @return Original booking ID, or null if not found
     */
    public static Integer getBookingIdFromRefId(String refId) {
        return uuidToBookingIdMap.get(refId);
    }
    
    /**
     * Get ref_id UUID for existing booking ID (if already generated)
     * @param bookingId The booking ID
     * @return UUID string, or null if not found
     */
    public static String getRefIdForBooking(int bookingId) {
        return bookingIdToUuidMap.get(bookingId);
    }
    
    /**
     * Remove mapping when booking is cancelled or expired
     * @param bookingId The booking ID to remove
     */
    public static void removeBookingMapping(int bookingId) {
        String uuid = bookingIdToUuidMap.remove(bookingId);
        if (uuid != null) {
            uuidToBookingIdMap.remove(uuid);
        }
    }
    
    /**
     * Check if ref_id UUID is valid
     * @param refId The UUID string to validate
     * @return true if UUID exists in mapping
     */
    public static boolean isValidRefId(String refId) {
        return uuidToBookingIdMap.containsKey(refId);
    }
    
    /**
     * Get total number of active mappings (for monitoring)
     * @return Number of active UUID mappings
     */
    public static int getActiveMappingCount() {
        return uuidToBookingIdMap.size();
    }
} 
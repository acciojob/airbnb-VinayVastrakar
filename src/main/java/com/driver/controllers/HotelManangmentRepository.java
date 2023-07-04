package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class HotelManangmentRepository {

    private HashMap<String, Hotel> hotels=new HashMap<>();
    private HashMap<Integer, User> users=new HashMap<>();
    private HashMap<String , Booking> bookings = new HashMap<>();
    private HashMap<String ,Integer> rent = new HashMap<>();


    public String addHotel(Hotel hotel) {
        //You need to add a hotel to the database
        //incase the hotelName is null or the hotel Object is null return an empty a FAILURE
        //Incase somebody is trying to add the duplicate hotelName return FAILURE
        //in all other cases return SUCCESS after successfully adding the hotel to the hotelDb.
        if(hotel.getHotelName()==null){
            return "FAILURE";
        }
        if(hotels.containsKey(hotel.getHotelName())){
            return "FAILURE";
        }
        hotels.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }
    public Integer addUser(User user){

        //You need to add a User Object to the database
        //Assume that user will always be a valid user and return the aadharCardNo of the user

        users.put(user.getaadharCardNo(),user);

        return user.getaadharCardNo();
    }
    public String getHotelWithMostFacilities(){

        //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
        //Incase there is a tie return the lexicographically smaller hotelName
        //Incase there is not even a single hotel with atleast 1 facility return "" (empty string)
        int max= 0;
        for(String key : hotels.keySet()){
            int n=hotels.get(key).getFacilities().size();
            if(max<n){
                max=n;
            }
        }
        if(max==0){
            return "";
        }
        List<String> names= new ArrayList<>();
        for(String key: hotels.keySet()){
            List<Facility> facility = hotels.get(key).getFacilities();
            if(facility.size()==max){
                names.add(key);
            }
        }
        Collections.sort(names);
        return names.get(0);
    }
    public int bookARoom( Booking booking){

        //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
        //Have bookingId as a random UUID generated String
        //save the booking Entity and keep the bookingId as a primary key
        //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
        //If there aren't enough rooms available in the hotel that we are trying to book return -1
        //in other case return total amount paid
        String hotelName= booking.getHotelName();
        if(!hotels.containsKey(hotelName)) {
            return -1;
        }
        if(hotels.get(hotelName).getAvailableRooms() >= booking.getNoOfRooms()){
            Hotel hotel = hotels.get(hotelName);
            int totalRoomAvail= hotel.getAvailableRooms();
            totalRoomAvail-=booking.getNoOfRooms();

            hotel.setAvailableRooms(totalRoomAvail);
            hotels.put(hotelName,hotel);  //update
            String bookingId = UUID.randomUUID()+""; // uuid create and change into string

            int amountToBePaid = hotel.getPricePerNight() * booking.getNoOfRooms();

            bookings.put(bookingId,booking);

            rent.put(bookingId,amountToBePaid);

            return amountToBePaid;

        }

        return -1;
    }
    public int getBookings(Integer aadharCard)
    {
        //In this function return the bookings done by a person
        int cnt=0;
        for(String key: bookings.keySet()){
            if(aadharCard.equals(bookings.get(key).getBookingAadharCard()))
                cnt++;
        }
        return cnt;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName){

        //We are having a new facilites that a hotel is planning to bring.
        //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
        //return the final updated List of facilities and also update that in your hotelDb
        //Note that newFacilities can also have duplicate facilities possible
        if(!hotels.containsKey(hotelName)){
            return null;
        }
        Hotel hotel= hotels.get(hotelName);
        List<Facility> facility = hotel.getFacilities();
        for(int i=0;i<newFacilities.size();i++){
            if(!facility.contains(newFacilities.get(i))){
                facility.add(newFacilities.get(i));
            }
        }
        hotel.setFacilities(facility);
        hotels.put(hotelName,hotel);

        return hotel;

    }
}

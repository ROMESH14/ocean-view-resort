package com.oceanview.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation {

    private int id;
    private String reservationNo;
    private String guestName;
    private String address;
    private String contactNo;
    private String roomType;
    private int roomId;
    private String roomNumber;
    private int guestCount;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BigDecimal totalAmount;

    public Reservation() {}
    public Reservation(String reservationNo, String guestName,
                       String address, String contactNo,
                       String roomType, int roomId,
                       LocalDate checkIn, LocalDate checkOut,
                       int guestCount, BigDecimal totalAmount) {

        this.reservationNo = reservationNo;
        this.guestName = guestName;
        this.address = address;
        this.contactNo = contactNo;
        this.roomType = roomType;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guestCount = guestCount;
        this.totalAmount = totalAmount;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getReservationNo() {
        return reservationNo;
    }
    public void setReservationNo(String reservationNo) {
        this.reservationNo = reservationNo;
    }
    public String getGuestName() {
        return guestName;
    }
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getContactNo() {
        return contactNo;
    }
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    public String getRoomType() {
        return roomType;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    public int getRoomId() {
        return roomId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    public String getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    public int getGuestCount() {
        return guestCount;
    }
    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }
    public LocalDate getCheckIn() {
        return checkIn;
    }
    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }
    public LocalDate getCheckOut() {
        return checkOut;
    }
    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}

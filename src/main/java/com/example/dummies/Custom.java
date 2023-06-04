package com.example.dummies;



import com.example.model.User;

public class Custom {
    User user;
    Booking booking;

    int numberOfSeats;

    public Custom() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Custom(User user, Booking booking, int numberOfSeats) {
        this.user = user;
        this.booking = booking;
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public String toString() {
        return "Custom{" +
                "user=" + user +
                ", booking=" + booking +
                ", numberOfSeats=" + numberOfSeats +
                '}';
    }
}


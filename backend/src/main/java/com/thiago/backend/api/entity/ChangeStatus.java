package com.thiago.backend.api.entity;

import java.util.Date;

import com.thiago.backend.api.enums.StatusEnum;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ChangeStatus {

    @Id
    private String id;

    @DBRef(lazy = true)
    private Ticket ticket;

    @DBRef(lazy = true)
    private User userChange;

    private Date dateChangeStatus;

    private StatusEnum status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public User getUseChange() {
        return userChange;
    }

    public void setUseChange(User useChange) {
        this.userChange = useChange;
    }

    public Date getDateChangeStatus() {
        return dateChangeStatus;
    }

    public void setDateChangeStatus(Date dateChangeStatus) {
        this.dateChangeStatus = dateChangeStatus;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

}

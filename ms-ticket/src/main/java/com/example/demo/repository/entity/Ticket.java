package com.example.demo.repository.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tickets")
public class Ticket {

    public enum STATUS {
        CRIADO,
        ANDAMENTO,
        CONCLUIDO,
        CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "creator_email", nullable = false)
    private String creatorEmail;

    @Column(name = "assignee_email", nullable = false)
    private String assigneeEmail;

    @ElementCollection
    @CollectionTable(name = "tickets_observers", joinColumns = @JoinColumn(name = "ticket_id"))
    @Column(name = "observer_email")
    private Set<String> observerEmails = new HashSet<>();

    @Column(nullable = false, length = 255)
    private String object;

    @Column(nullable = false, length = 255)
    private String action;

    @Column(nullable = false, length = 255)
    private String details;

    @Column(nullable = false, length = 255)
    private String locality;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private STATUS status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;


    public Ticket() {
    }

    public Ticket(String creatorEmail, String assigneeEmail, Set<String> observerEmails,
                  String object, String action, String details, String locality) {
        this.creatorEmail = creatorEmail;
        this.assigneeEmail = assigneeEmail;
        this.observerEmails = observerEmails != null ? observerEmails : new HashSet<>();
        this.object = object;
        this.action = action;
        this.details = details;
        this.locality = locality;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();

        if (this.status == null) {
            this.status = STATUS.CRIADO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
    }

    public Set<String> getObserverEmails() {
        return observerEmails;
    }

    public void setObserverEmails(Set<String> observerEmails) {
        this.observerEmails = observerEmails;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
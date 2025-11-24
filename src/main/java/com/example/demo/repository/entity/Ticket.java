package com.example.demo.repository.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "tickets")
public class Ticket {

    public enum STATUS {
        CRIADO,
        ANDAMENTO,
        CONCLUIDO,
        CANCELADO
    }

    public Ticket() {
    }

    public Ticket(User creator, User assignee, STATUS status, Set<User> observers, String object, String action,
            String details, String locality) {
        this.creator = creator;
        this.assignee = assignee;
        this.status = status;
        this.observers = observers;
        this.object = object;
        this.action = action;
        this.details = details;
        this.locality = locality;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assignee_id", referencedColumnName = "id")
    private User assignee;

    @ManyToMany
    @JoinTable(name = "tickets_observers", joinColumns = @JoinColumn(name = "ticket_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> observers;

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

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public User getCreator() {
        return creator;
    }

    public User getAssignee() {
        return assignee;
    }

    public Set<User> getObservers() {
        return observers;
    }

    public String getObject() {
        return object;
    }

    public String getAction() {
        return action;
    }

    public String getDetails() {
        return details;
    }

    public String getLocality() {
        return locality;
    }

    public STATUS getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
    
}

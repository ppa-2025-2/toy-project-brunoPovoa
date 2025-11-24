package com.example.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.validation.annotation.Validated;

import com.example.demo.controller.dto.NewTicketDTO;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.entity.Ticket;
import com.example.demo.repository.entity.User;
import com.example.demo.service.stereotype.Business;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Business
@Validated
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(
            TicketRepository ticketRepository,
            UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public List<Ticket> getTickets() {
        return this.ticketRepository.findAll();
    }

    @Transactional
    public void newTicket(@Valid NewTicketDTO newTicket) {
        User creator = this.userRepository.findByEmail(newTicket.creatorEmail())
                .orElseThrow(() -> new IllegalArgumentException("Criador não encontrado"));

        User assignee = this.userRepository.findByEmail(newTicket.assigneeEmail())
                .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado"));

        Set<User> observers = new HashSet<>(this.userRepository.findByEmailIn(newTicket.observerEmails()));

        Ticket ticket = new Ticket(creator, assignee, Ticket.STATUS.CRIADO, observers, newTicket.object(),
                newTicket.action(), newTicket.details(), newTicket.locality());

        ticketRepository.save(ticket);
    }

    public void updateTicketStatus(int ticketID, Ticket.STATUS status) {
        Ticket ticket = this.ticketRepository.findById(ticketID)
                .orElseThrow(() -> new IllegalArgumentException("Ticket não encontrado"));

        ticket.setStatus(status);

        this.ticketRepository.save(ticket);
    }
}

package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.controller.dto.NewTicketDTO;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.entity.Ticket;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public void newTicket(NewTicketDTO dto) {
        Ticket ticket = new Ticket(
            dto.creatorEmail(),
            dto.assigneeEmail(),
            dto.observerEmails() != null ? new HashSet<>(dto.observerEmails()) : new HashSet<>(),
            dto.object(),
            dto.action(),
            dto.details(),
            dto.locality()
        );
        ticketRepository.save(ticket);
    }
            
    public List<Ticket> getTickets() {
        return this.ticketRepository.findAll();
    }

    public void updateTicketStatus(int ticketID, Ticket.STATUS status) {
        Ticket ticket = this.ticketRepository.findById(ticketID)
                .orElseThrow(() -> new IllegalArgumentException("Ticket não encontrado"));

        ticket.setStatus(status);

        this.ticketRepository.save(ticket);
    }
}

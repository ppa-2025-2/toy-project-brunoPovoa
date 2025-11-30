package com.example.demo.integration;

import com.example.demo.controller.dto.NewTicketDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-ticket", url = "http://localhost:8081/api/v1/tickets")
public interface TicketClient {

    @PostMapping
    void createTicket(@RequestBody NewTicketDTO ticketDTO);
}
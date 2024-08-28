package compasso.com.br.model.dao;

import compasso.com.br.model.entities.Ticket;

import java.util.List;

public interface TicketDao {
    void insert(Ticket ticket);
    void update(Ticket ticket);
    void deleteById(Integer id);
    Ticket findById(Integer id);
    List<Ticket> findAll();
}

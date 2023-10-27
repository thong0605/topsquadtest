package org.service.topsquad.repository;

import org.service.topsquad.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
    @Query(value = "SELECT EXISTS (SELECT * FROM TASK WHERE TICKET_NUMBER = :ticketNumber)", nativeQuery = true)
    boolean isTicketNumberExists(@Param("ticketNumber") String ticketNumber);

    @Query(value = "SELECT * FROM TASK WHERE TICKET_NUMBER = :ticketNumber", nativeQuery = true)
    Optional<TaskEntity> findByTicketNumber(@Param("ticketNumber") String name);

    @Query(value = "DELETE FROM TASK WHERE TICKET_NUMBER = :ticketNumber", nativeQuery = true)
    boolean deleteByTicketNumber(@Param("ticketNumber") String ticketNumber);
}

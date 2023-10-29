package org.service.topsquad.repository;

import org.service.topsquad.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Query(value = "SELECT * FROM USERTASK  WHERE ROLE = :role", nativeQuery = true)
    List<UserEntity> findAllByRole(@Param("role") String role);

    @Query(value = "SELECT * FROM USERTASK  WHERE USER_NAME = :name", nativeQuery = true)
    Optional<UserEntity> findByUserName(@Param("name") String name);

    @Query(value = "SELECT EXISTS (SELECT * FROM USERTASK WHERE USER_NAME = :userName AND STATUS = :status)", nativeQuery = true)
    boolean isUserExistsByStatus(@Param("userName") String ticketNumber, @Param("status") String status);

    @Query(value = "SELECT EXISTS (SELECT * FROM USERTASK WHERE USER_NAME = :userName)", nativeQuery = true)
    boolean isUserExistsByUserName(@Param("userName") String userName);

    @Query(value = "SELECT EXISTS (SELECT * FROM USERTASK WHERE PASSWORD = :password)", nativeQuery = true)
    boolean isPasswordMatch(@Param("password") String password);

    @Query(value = "SELECT EXISTS (SELECT * FROM USERTASK WHERE MAIL_ADDRESS = :mail)", nativeQuery = true)
    boolean isEmailExists(@Param("mail") String mail);
}

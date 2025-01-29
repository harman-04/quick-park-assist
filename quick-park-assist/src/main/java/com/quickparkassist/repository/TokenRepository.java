package com.quickparkassist.repository;


import com.quickparkassist.model.PasswordResetToken;
import com.quickparkassist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;


public interface TokenRepository extends JpaRepository<PasswordResetToken, Integer>{

    PasswordResetToken findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken t WHERE t.user = :user")
    void deleteByUser(User user);

}

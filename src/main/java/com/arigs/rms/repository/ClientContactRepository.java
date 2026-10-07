package com.arigs.rms.repository;

import com.arigs.rms.entity.ClientContact;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for client contacts.
 */
public interface ClientContactRepository extends JpaRepository<ClientContact, UUID> {

    List<ClientContact> findByClientIdAndActiveTrueOrderByPrimaryContactDescContactNameAsc(UUID clientId);
}

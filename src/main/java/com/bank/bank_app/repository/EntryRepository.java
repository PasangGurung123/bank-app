package com.bank.bank_app.repository;

import com.bank.bank_app.dto.EntryDTO;
import com.bank.bank_app.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<EntryDTO> findByAccountId(Long accountId);
}

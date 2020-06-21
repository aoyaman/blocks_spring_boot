package com.example.blocks.repository;

import com.example.blocks.entity.Account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
  public Account findByUsername(String username);
}

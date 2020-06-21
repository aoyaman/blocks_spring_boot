package com.example.blocks.repository;

import java.util.List;

import com.example.blocks.entity.Player;

import org.springframework.data.repository.CrudRepository;


public interface PlayerRepository extends CrudRepository<Player, Integer> {
  public List<Player> findByAccountName(String accountName);
  public List<Player> findByGameId(Integer gameId);
  public List<Player> findByGameIdAndNumber(Integer gameId, Integer number);
}

package com.example.blocks.repository;

import com.example.blocks.entity.Game;

import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Integer> {

}

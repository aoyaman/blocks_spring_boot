package com.example.blocks.repository;

import java.util.List;

import com.example.blocks.entity.Block;

import org.springframework.data.repository.CrudRepository;

public interface BlockRepository extends CrudRepository<Block, Integer> {

  public List<Block> findByGameIdAndStatus(Integer id, Integer status);
  public List<Block> findByGameIdAndStatusAndPlayer(Integer id, Integer status, Integer player);
  public List<Block> findByGameIdAndPlayerAndBlockType(Integer id, Integer player, Integer blockType);

}

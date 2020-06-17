package com.example.blocks;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface BlockRepository extends CrudRepository<Block, Integer> {

  public List<Block> findByGameIdAndStatus(Integer id, Integer status);
  public List<Block> findByGameIdAndStatusAndColor(Integer id, Integer status, Integer color);

}

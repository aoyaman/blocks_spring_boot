package com.example.blocks.repository;

import com.example.blocks.entity.Record;

import org.springframework.data.repository.CrudRepository;

public interface RecordRepository extends CrudRepository<Record, Integer> {

}

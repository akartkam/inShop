package com.akartkam.inShop.dao;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.Instruction;

public interface InstructionDAO extends GenericDAO<Instruction, UUID> {
	List<Object[]> findInstruction(UUID id);
}

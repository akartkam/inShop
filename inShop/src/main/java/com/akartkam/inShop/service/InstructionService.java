package com.akartkam.inShop.service;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.Instruction;

public interface InstructionService {
	List<Object[]> getAllInstructions();
	Instruction getInstructionById(UUID id);
	Instruction CreateInstruction(Instruction instruction);
	void deleteInstruction(Instruction instruction);
	void deleteInstructionById(UUID id);
	Instruction cloneInstructionById(UUID id) throws CloneNotSupportedException;
}

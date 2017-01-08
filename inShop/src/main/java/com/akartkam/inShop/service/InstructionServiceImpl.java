package com.akartkam.inShop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.InstructionDAO;
import com.akartkam.inShop.domain.Instruction;

@Service("InstructionService")
@Transactional(readOnly = true)
public class InstructionServiceImpl implements InstructionService {
	
	@Autowired
	private InstructionDAO instructionDAO;
	
	@Override
	public List<Object[]> getAllInstructions() {
		List<Object[]> resDao = instructionDAO.findInstruction(null);
		List<Object[]> res = new ArrayList<Object[]>();
		for(Object[] obj : resDao) {
			Object[] resItem = new Object[3];
			resItem[0] = obj[0];
			if (obj[1] != null) {
				String[] s1 = ((String)obj[1]).split(",");
				Map<String, UUID> pmap = new HashMap<String, UUID>();
				for (String ss : s1) {
					String[] s2 = ss.split("_");
					pmap.put(s2[0], UUID.fromString(s2[1]));
				}
				resItem[1] = pmap; 
			} else {
				resItem[1] = null;
			}
			if (obj[2] != null) {
				String[] s1 = ((String)obj[2]).split(",");
				Map<String, UUID> cmap = new HashMap<String, UUID>();
				for (String ss : s1) {
					String[] s2 = ss.split("_");
					cmap.put(s2[0], UUID.fromString(s2[1]));
				}
				resItem[2] = cmap; 
			} else {
				resItem[2] = null;
			}

		}
		return res;
	}

	@Override
	public Instruction getInstructionById(UUID id) {
		return instructionDAO.get(id);
	}

	@Override
	@Transactional(readOnly = false)
	public Instruction CreateInstruction(Instruction instruction) {
		return instructionDAO.create(instruction);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteInstruction(Instruction instruction) {
		instructionDAO.delete(instruction);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteInstructionById(UUID id) {
		instructionDAO.deleteById(id);
	}

	@Override
	public Instruction cloneInstructionById(UUID id) throws CloneNotSupportedException {
		Instruction instr = getInstructionById(id);
		if (instr != null) {
			return instr.clone();
		}
		return null; 
	}

}

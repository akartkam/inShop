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
				Map<UUID, String> pmap = new HashMap<UUID, String>();
				for (String ss : s1) {
					String[] s2 = ss.split("_");
					pmap.put(UUID.fromString(s2[1]), s2[0]);
				}
				resItem[1] = pmap; 
			} else {
				resItem[1] = null;
			}
			if (obj[2] != null) {
				String[] s1 = ((String)obj[2]).split(",");
				Map<UUID, String> cmap = new HashMap<UUID, String>();
				for (String ss : s1) {
					String[] s2 = ss.split("_");
					cmap.put(UUID.fromString(s2[1]), s2[0]);
				}
				resItem[2] = cmap; 
			} else {
				resItem[2] = null;
			}
			res.add(resItem);
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

	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(Instruction instr) {
		if (instr == null) return;
		final Instruction instrEx = instructionDAO.get(instr.getId());
		if (instrEx != null) {
			instrEx.setName(instr.getName());
			instrEx.setContent(instr.getContent());
		} else {
			CreateInstruction(instr);
		}
	}

}

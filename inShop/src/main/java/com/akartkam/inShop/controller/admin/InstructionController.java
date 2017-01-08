package com.akartkam.inShop.controller.admin;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.akartkam.inShop.domain.Instruction;
import com.akartkam.inShop.service.InstructionService;

@Controller
@RequestMapping("/admin/catalog/instruction")
public class InstructionController {
		
	  @Autowired
	  private InstructionService instructionService;
	
	  @ModelAttribute("allInstructions")
	  public List<?> getAllInstructions() {
		return instructionService.getAllInstructions();
	  }
		
	  @RequestMapping(method=GET)
	  public String instruction() {
		  return "/admin/catalog/instruction"; 
	  }	
		
}

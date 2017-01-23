package com.akartkam.inShop.controller.admin;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.akartkam.inShop.domain.Instruction;
import com.akartkam.inShop.service.InstructionService;

@Controller
@RequestMapping("/admin/catalog/instruction")
public class InstructionController {
		
	  @Autowired
	  private InstructionService instructionService;
	  
	  @Autowired
	  private MessageSource messageSource;
	
	  @ModelAttribute("allInstructions")
	  public List<?> getAllInstructions() {
		return instructionService.getAllInstructions();
	  }
	  
	  @InitBinder
	  public void initBinder(WebDataBinder binder) {
			binder.setAllowedFields(new String[] { "id", "name", "content"});
			binder.registerCustomEditor(UUID.class, "id", new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	 setValue(UUID.fromString(text));
			    }
			    });
			binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			    @Override
			    public void setAsText(String text) {
			    	if ("".equals(text) || "''".equals(text))
			    	  setValue(null);
			    	else
			    	  setValue(text);	
			    }
			    });
	  }	  
		
	  @RequestMapping(method=GET)
	  public String instruction() {
		  return "/admin/catalog/instruction"; 
	  }	
	  
	  @RequestMapping("/edit")
	  public String instrEdit(@RequestParam(value = "ID") String ID, Model model) {
		  if(!model.containsAttribute("instr")) {
			  if (ID == null || "".equals(ID)) throw new IllegalStateException("Instruction ID in instrEdit is empty" );
			  Instruction instr = instructionService.getInstructionById(UUID.fromString(ID));
			  model.addAttribute("instr", instr);
		  }
		  return "admin/catalog/instructionEdit";
	  }
	  
	  @RequestMapping("/add")
	  public String instrAdd(@RequestParam(value = "ID", required = false) String copyID, Model model) throws CloneNotSupportedException {
		  Instruction instr = null;
		  if (copyID != null && !"".equals(copyID)) instr = instructionService.cloneInstructionById(UUID.fromString(copyID));
		  else instr = new Instruction();
		  model.addAttribute("instr", instr);
		  return "admin/catalog/instructionEdit";
	  }
	  
	  @RequestMapping(value="/delete", method = RequestMethod.POST)
	  public String brandDelete(@RequestParam(value = "ID", required = false) String ID, 
			                    @RequestParam(value = "phisycalDelete", required = false) Boolean phisycalDelete,
				                final RedirectAttributes ra) {
		  Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  if (phisycalDelete != null && phisycalDelete)  {
			  List<Object[]> instrs = instructionService.getInstructionExById(UUID.fromString(ID));
			  if(!instrs.isEmpty() && instrs.get(0)[1] == null && instrs.get(0)[2] == null && authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
				  instructionService.deleteInstruction((Instruction)instrs.get(0)[0]);   
			  } else {
				  ra.addFlashAttribute("errormessage", this.messageSource.getMessage("admin.error.cannotdelete.message", new String[] {"инструкцию"} , null));
				  ra.addAttribute("error", true);
			  }

		  } else {
			  instructionService.softDeleteById(UUID.fromString(ID));
		  }
          return "redirect:/admin/catalog/instruction";		  
	  }
	  
	  @RequestMapping(value="/edit", method = RequestMethod.POST )
	  public String saveBrand(@ModelAttribute @Valid Instruction instr,
			                   final BindingResult bindingResult,
			                   final RedirectAttributes ra
			                         ) {
	        if (bindingResult.hasErrors()) {
	        	ra.addFlashAttribute("instr", instr);
	        	ra.addFlashAttribute("org.springframework.validation.BindingResult.instr", bindingResult);
	            return "redirect:/admin/catalog/instruction/edit?ID="+instr.getId().toString();
	        }	
	        instructionService.mergeWithExistingAndUpdateOrCreate(instr);
     		ra.addFlashAttribute("successmessage", messageSource.getMessage("admin.success.save.message", null , Locale.getDefault()));
		    return "redirect:/admin/catalog/instruction/edit?ID="+instr.getId().toString();
	  }
		
}

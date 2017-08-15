package commconsistency.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import commconsistency.dao.UserRepository;
import commconsistency.domain.User;


@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model) {
		return "index";
	}
	
	@RequestMapping(value="/signin",method=RequestMethod.POST)
	public String signin(@ModelAttribute User user,Model model) {
		
		List<String> roles = new ArrayList<String>();
		roles.add("USER");
		user.setRoles(roles);
		userRepository.insert(user);
		return "login";
	}
	
	@RequestMapping(value="/signin",method=RequestMethod.GET)
	public String signin(Model model) {
		User user = new User();
		model.addAttribute("user",user);
		return "signin";
	}
}

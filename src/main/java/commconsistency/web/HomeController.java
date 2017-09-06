package commconsistency.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import commconsistency.dao.EndLineVerifyRepository;
import commconsistency.dao.UserRepository;
import commconsistency.domain.EndLineVerify;
import commconsistency.domain.User;
import commconsistency.dto.UserVerify;
import commconsistency.service.EndLineVerifyService;


@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EndLineVerifyService endLineVerifyService;
	
	@RequestMapping("/")
	public String home(Model model) {
		List<EndLineVerify> endLineVerifyList = endLineVerifyService.findAll();
		Map<String,Integer> userVerifyMap = new TreeMap<String,Integer>();
		for(EndLineVerify endLineVerify:endLineVerifyList) {
			if(userVerifyMap.containsKey(endLineVerify.getUserName())) {
				userVerifyMap.put(endLineVerify.getUserName(), userVerifyMap.get(endLineVerify.getUserName())+1);
			}else {
			    userVerifyMap.put(endLineVerify.getUserName(), 1);
			}
		}
		
		List<UserVerify> userVerifyList = new ArrayList<UserVerify>();
		for(Map.Entry<String, Integer> entry:userVerifyMap.entrySet()) {
			UserVerify userVerify = new UserVerify();
			userVerify.setUserName(entry.getKey());
			userVerify.setVerifyNo(entry.getValue());
			userVerify.setRole("USER_ROLE");
			userVerifyList.add(userVerify);
		}
		
		model.addAttribute("verifylist", userVerifyList);
		model.addAttribute("verifyno", endLineVerifyList.size()+"");
		Logger.getLogger(this.getClass()).info("list size:"+endLineVerifyList.size());
		
		return "index";
	}
	
	@RequestMapping(value="/signin",method=RequestMethod.POST)
	public String signin(@ModelAttribute User user,Model model) {
		
		String username = user.getUserName();
		if(userRepository.findByUserName(username)!=null) {
			User t_user = new User();
			model.addAttribute("user",t_user);
			model.addAttribute("exist_username",true);
			return "signin";
		}
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
		model.addAttribute("exist_username",false);
		return "signin";
	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login(Model model) {
		if(SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal() instanceof UserDetails) {
		
			return "index";
		}else {
			return "login";
		}
	}
}

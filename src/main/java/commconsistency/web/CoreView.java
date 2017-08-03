package commconsistency.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class CoreView {

	@RequestMapping("/coreview")
	public String coreView(Model model) {
		return "coreview";
	}
}

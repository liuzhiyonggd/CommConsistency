package commconsistency.web;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import commconsistency.dao.CommentScopeRepository;
import commconsistency.domain.CommentScope;
import commconsistency.domain.EndLineVerify;
import commconsistency.domain.Line;
import commconsistency.domain.MethodExtractor;
import commconsistency.domain.SubCommentScope;
import commconsistency.domain.SubMethodExtractor;
import commconsistency.dto.CommentScopeDto;
import commconsistency.dto.MethodExtractorDto;
import commconsistency.service.CommentScopeService;
import commconsistency.service.EndLineVerifyService;
import commconsistency.service.MethodExtractorService;
import commconsistency.service.SubCommentScopeService;
import commconsistency.service.SubMethodExtractorService;
import commconsistency.utils.SpringDataPageable;

@Controller
public class RefactorController {

	@Autowired
	private CommentScopeService commentScopeService;
	@Autowired
	private SubCommentScopeService subCommentScopeService;
	@Autowired
	private EndLineVerifyService endLineVerifyService;
	
	@Autowired
	private MethodExtractorService methodExtractorService;
	
	@Autowired
	private SubMethodExtractorService subMethodExtractorService;

	// commentscope list(未验证) 展示
	@RequestMapping("/methodextractorlist")
	public String methodExtractor(@RequestParam("pageno") int pageNo,@RequestParam("pagesize") int pageSize,  Model model) {
		if(pageNo<=1) {
			pageNo = 1;
		}
		// 获取数据库中所有的数据，该部分需要修改，改成分页调用
		SpringDataPageable pageable = new SpringDataPageable();  
		//每页显示条数  
		pageable.setPagesize(pageSize);  
		//当前页  
		pageable.setPagenumber(pageNo);  
		
		Page<SubMethodExtractor> page = subMethodExtractorService.findAll(pageable); 
		
		if(page.isLast()) {
			pageNo = pageNo-1;
		}
		
		List<SubMethodExtractor> methodExtractorList = new ArrayList<SubMethodExtractor>();

		Iterator<SubMethodExtractor> iter = page.iterator();
		
		while(iter.hasNext()) {
			SubMethodExtractor methodExtractor = iter.next();
			String[] temps = methodExtractor.getCommitID().split(" ");
			methodExtractor.setCommitID2(temps[temps.length - 2]);
			temps = methodExtractor.getClassName().split("\\\\");
			temps = temps[temps.length - 1].split("\\.");
			methodExtractor.setClassName2(temps[0]);
			methodExtractorList.add(methodExtractor);
			
		}
		model.addAttribute("methodextractorlist", methodExtractorList);
		model.addAttribute("pageno",pageNo);
		return "methodextractor_list";
	}
	
	
	
	// 根据用户传递回来的commentID查找表，返回该comment的详细信息。
		@RequestMapping("/methodextractorview")
		public String methodExtractorView(@RequestParam("id") String id,Model model) {
             
			MethodExtractor methodExtractor = methodExtractorService.findByMethodExtractorId(Integer.parseInt(id));
			MethodExtractorDto methodExtractorDto = new MethodExtractorDto();
			methodExtractorDto.setMethodExtractorId(methodExtractor.getMethodExtractorId());;
			methodExtractorDto.setProject(methodExtractor.getProject());
			methodExtractorDto.setCommitID(methodExtractor.getCommitID());
			methodExtractorDto.setClassName(methodExtractor.getClassName());
			methodExtractorDto.setOldStartLine(methodExtractor.getOldStartLine());
			methodExtractorDto.setOldEndLine(methodExtractor.getOldEndLine());
			methodExtractorDto.setNewStartLine(methodExtractor.getNewStartLine());
			methodExtractorDto.setNewEndLine(methodExtractor.getNewEndLine());
			methodExtractorDto.setLocations(methodExtractor.getLocations());
			
			List<String> oldCodes = methodExtractor.getOldCodeList();
			StringBuilder sb = new StringBuilder();
			for(String str:oldCodes) {
				sb.append(str.replace("<", "&lt;")).append("\r\n");
			}
			methodExtractorDto.setOldCode(sb.toString());
			
			List<String> newCodes = methodExtractor.getNewCodeList();
			sb = new StringBuilder();
			for(String str:newCodes) {
				sb.append(str.replace("<", "&lt;")).append("\r\n");
			}
			methodExtractorDto.setNewCode(sb.toString());
			model.addAttribute("methodextractor", methodExtractorDto);
			int[] oldHighLight = new int[methodExtractorDto.getOldEndLine()-methodExtractorDto.getOldStartLine()+1];
			for(int i=methodExtractorDto.getOldStartLine();i<=methodExtractorDto.getOldEndLine();i++) {
				oldHighLight[i-methodExtractorDto.getOldStartLine()] = i;
			}
			model.addAttribute("oldhighlight", oldHighLight);
			int[] newHighLight = new int[methodExtractorDto.getNewEndLine()-methodExtractorDto.getNewStartLine()+1+methodExtractorDto.getLocations().size()];
			for(int i=methodExtractorDto.getNewStartLine();i<=methodExtractorDto.getNewEndLine();i++) {
				newHighLight[i-methodExtractorDto.getNewStartLine()] = i;
			}
			for(int i=methodExtractorDto.getNewEndLine()-methodExtractorDto.getNewStartLine()+1;i<newHighLight.length;i++) {
				newHighLight[i] = methodExtractorDto.getLocations().get(i-methodExtractorDto.getNewEndLine()+methodExtractorDto.getNewStartLine()-1);
			}
			model.addAttribute("oldhighlight", oldHighLight);
			model.addAttribute("newhighlight",newHighLight);
			return "methodextractor_view";
		}
	
	@RequestMapping("/refactorsave")
	public ModelAndView commentScopeSave(@ModelAttribute CommentScopeDto commentScopeDto,Model model) {
		int commentID = commentScopeDto.getCommentID();
		int verifyScopeEndLine = commentScopeDto.getScopeEndLine();
		CommentScope comment = commentScopeService.findByCommentID(commentID);
		List<Integer> verifyScopeEndLineList = comment.getVerifyScopeEndLineList();
		if(comment.getVerifyScopeEndLineList()==null) {
			verifyScopeEndLineList = new ArrayList<Integer>();
		}
		verifyScopeEndLineList.add(verifyScopeEndLine);
		comment.setVerifyScopeEndLineList(verifyScopeEndLineList);
		commentScopeService.save(comment);
		EndLineVerify endLineVerify = new EndLineVerify();
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal(); 
		
		endLineVerify.setUserName(userDetails.getUsername());
		endLineVerify.setEndLine(verifyScopeEndLine);
		endLineVerify.setCommentID(commentScopeDto.getCommentID());
		endLineVerifyService.insert(endLineVerify);
		
		SubCommentScope subCommentScope = subCommentScopeService.findByCommentID(commentID);
		subCommentScope.setVerify(true);
		subCommentScopeService.save(subCommentScope);
		
		return new ModelAndView("redirect:/commentscopeverification?commentID="+(commentID+1));
	}
}

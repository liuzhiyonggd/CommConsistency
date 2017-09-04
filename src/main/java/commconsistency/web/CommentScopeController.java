package commconsistency.web;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import commconsistency.domain.SubCommentScope;
import commconsistency.dto.CommentScopeDto;
import commconsistency.service.CommentScopeService;
import commconsistency.service.EndLineVerifyService;
import commconsistency.service.SubCommentScopeService;
import commconsistency.utils.SpringDataPageable;

@Controller
public class CommentScopeController {

	@Autowired
	private CommentScopeService commentScopeService;
	@Autowired
	private SubCommentScopeService subCommentScopeService;
	@Autowired
	private EndLineVerifyService endLineVerifyService;

	// commentscope list(未验证) 展示
	@RequestMapping("/commentscopelist_verification")
	public String commentScopeListVerification(@RequestParam("pageno") int pageNo,@RequestParam("pagesize") int pageSize,  Model model) {
		if(pageNo<=1) {
			pageNo = 1;
		}
		// 获取数据库中所有的数据，该部分需要修改，改成分页调用
		SpringDataPageable pageable = new SpringDataPageable();  
		//每页显示条数  
		pageable.setPagesize(pageSize);  
		//当前页  
		pageable.setPagenumber(pageNo);  
		
		//使用数据量更小的表sub_comment_scope，加快读取速度
		Page<SubCommentScope> page = subCommentScopeService.findByIsVerify(false, pageable);  
		
		if(page.isLast()) {
			pageNo = pageNo-1;
		}
		
		List<CommentScopeDto> commentList = new ArrayList<CommentScopeDto>();

		// 将获取到的数据表重新打包成更小结构的DTO对象，传送给页面展示
		Iterator<SubCommentScope> iter = page.iterator();
		
		while(iter.hasNext()) {
			SubCommentScope subCommentScope = iter.next();
			CommentScopeDto comment = new CommentScopeDto();
			comment.setCommentID(subCommentScope.getCommentID());
			comment.setProject(subCommentScope.getProject());
			String[] temps = subCommentScope.getCommitID().split(" ");
			comment.setCommitID(temps[temps.length - 2]);
			temps = subCommentScope.getClassName().split("\\\\");
			temps = temps[temps.length - 1].split("\\.");
			comment.setClassName(temps[0]);
			commentList.add(comment);
		}
		model.addAttribute("commentscopelist", commentList);
		model.addAttribute("pageno",pageNo);
		return "commentscopelist_verification";
	}
	
	// commentscope list(未验证) 展示
		@RequestMapping("/commentscopelist_view")
		public String commentScopeListView(@RequestParam("pageno") int pageNo,@RequestParam("pagesize") int pageSize,  Model model) {
			if(pageNo<=1) {
				pageNo = 1;
			}
			// 获取数据库中所有的数据，该部分需要修改，改成分页调用
			SpringDataPageable pageable = new SpringDataPageable();  
			//每页显示条数  
			pageable.setPagesize(pageSize);  
			//当前页  
			pageable.setPagenumber(pageNo);  
			
			Page<EndLineVerify> page = endLineVerifyService.findAll(pageable);  
			
			if(page.isLast()) {
				pageNo = pageNo-1;
			}
			
			List<CommentScopeDto> commentList = new ArrayList<CommentScopeDto>();

			// 将获取到的数据表重新打包成更小结构的DTO对象，传送给页面展示
			Iterator<EndLineVerify> iter = page.iterator();
			
			while(iter.hasNext()) {
				EndLineVerify endLineVerify = iter.next();
				SubCommentScope subCommentScope = subCommentScopeService.findByCommentID(endLineVerify.getCommentID());
				CommentScopeDto comment = new CommentScopeDto();
				comment.setCommentID(subCommentScope.getCommentID());
				comment.setProject(subCommentScope.getProject());
				String[] temps = subCommentScope.getCommitID().split(" ");
				comment.setCommitID(temps[temps.length - 2]);
				temps = subCommentScope.getClassName().split("\\\\");
				temps = temps[temps.length - 1].split("\\.");
				comment.setClassName(temps[0]);
				commentList.add(comment);
			}
			model.addAttribute("commentscopelist", commentList);
			model.addAttribute("pageno",pageNo);
			return "commentscopelist_view";
		}

	// 根据用户传递回来的commentID查找表，返回该comment的详细信息。
	@RequestMapping("/commentscopeverification")
	public String commentScopeVerification(@RequestParam("commentID") String paramsStr, Model model) {

		int commentID = Integer.parseInt(paramsStr);
		CommentScope commentScope = commentScopeService.findByCommentID(commentID);
		EndLineVerify verify = endLineVerifyService.findByCommentID(commentID);
		while(commentScope==null&&verify!=null) {
			commentID = nextCommentID(commentID);
			commentScope = commentScopeService.findByCommentID(commentID);
			endLineVerifyService.findByCommentID(commentID);
		}

		// 将查找到的对象进行DTO对象转换，转换成更小的对象传递给页面展示
		CommentScopeDto comment = new CommentScopeDto();
		comment.setCommentID(commentScope.getCommentID());
		comment.setProject(commentScope.getProject());
		String[] temps = commentScope.getCommitID().split(" ");
		comment.setCommitID(temps[temps.length - 2]);
		temps = commentScope.getClassName().split("\\\\");
		temps = temps[temps.length - 1].split("\\.");
		comment.setClassName(temps[0]);

		// 将源代码转换成可以在SyntaxHighlighter中显示的字符串，并保存在Model中。
		StringBuilder builder = new StringBuilder();
		Iterator<Line> iter = commentScope.getCodeList().iterator();
		int i = 0;
		while (iter.hasNext()) {
			i++;
			Line line = iter.next();
			if (i < commentScope.getMethodStartLine()) {
				continue;
			}
			if (i > commentScope.getScopeEndLine()) {
				break;
			}

			builder.append(line.getLine().replace("<", "&lt;")).append("\r\n");
		}
		comment.setCode(builder.toString());
		comment.setScopeEndLine(commentScope.getScopeEndLine());
		model.addAttribute("comment", comment);

		// firstline为SyntaxHighlighter 显示的第一行的行号
		model.addAttribute("firstline", commentScope.getMethodStartLine());

		// highlight 为该对象对应的注释行号，将注释高亮显示
		int[] highlightNums = new int[commentScope.getCommentEndLine() - commentScope.getCommentStartLine() + 1];
		for (int highlightNum = commentScope.getCommentStartLine(); highlightNum <= commentScope
				.getCommentEndLine(); highlightNum++) {
			highlightNums[highlightNum - commentScope.getCommentStartLine()] = highlightNum;
		}
		model.addAttribute("highlight", highlightNums);
		return "commentscope_verification";
	}
	
	private int nextCommentID(int commentID) {
		int nextCommentID = commentID;
		if((commentID>=111182&&commentID<111686)||(commentID>=114674&&commentID<115179)
				||(commentID>=122577&&commentID<123077)||(commentID>=124859&&commentID<125369)) {
			nextCommentID++;
		}
		else if(commentID<111182){
			nextCommentID = 111182;
		}else if(commentID>=111686&&commentID<114674) {
			nextCommentID = 114674;
		}else if(commentID>=115179&&commentID<122577) {
			nextCommentID = 122577;
		}else if(commentID>=123077&&commentID<124859) {
			nextCommentID = 124859;
		}else if(commentID>=125369){
			nextCommentID = 111182;
		}
		return nextCommentID;
	}
	
	
	// 根据用户传递回来的commentID查找表，返回该comment的详细信息。
		@RequestMapping("/commentscopeview")
		public String commentScopeView(@RequestParam("commentID") String paramsStr,@RequestParam("isNext") String isNext, Model model) {

			int commentID = Integer.parseInt(paramsStr);
			
			int minCommentID = 124860;
			int maxCommentID = 125074;
			if(commentID<minCommentID) {
				commentID = 124860;
			}
			if(commentID>maxCommentID) {
				commentID = 125074;
			}
		
			EndLineVerify endLineVerify = endLineVerifyService.findByCommentID(commentID);
			while(endLineVerify==null) {
				if(isNext.equals("true")) {
				    commentID++;
				    endLineVerify = endLineVerifyService.findByCommentID(commentID);
				}else {
					commentID--;
					endLineVerify = endLineVerifyService.findByCommentID(commentID);
				}
			}

			CommentScope commentScope = commentScopeService.findByCommentID(commentID);
			// 将查找到的对象进行DTO对象转换，转换成更小的对象传递给页面展示
			CommentScopeDto comment = new CommentScopeDto();
			comment.setCommentID(commentScope.getCommentID());
			comment.setProject(commentScope.getProject());
			String[] temps = commentScope.getCommitID().split(" ");
			comment.setCommitID(temps[temps.length - 2]);
			temps = commentScope.getClassName().split("\\\\");
			temps = temps[temps.length - 1].split("\\.");
			comment.setClassName(temps[0]);

			// 将源代码转换成可以在SyntaxHighlighter中显示的字符串，并保存在Model中。
			StringBuilder builder = new StringBuilder();
			Iterator<Line> iter = commentScope.getCodeList().iterator();
			int i = 0;
			while (iter.hasNext()) {
				i++;
				Line line = iter.next();
				if (i < commentScope.getMethodStartLine()) {
					continue;
				}
				if (i > commentScope.getScopeEndLine()) {
					break;
				}

				builder.append(line.getLine().replace("<", "&lt;")).append("\r\n");
			}
			comment.setCode(builder.toString());
			comment.setScopeEndLine(commentScope.getScopeEndLine());
			model.addAttribute("comment", comment);

			// firstline为SyntaxHighlighter 显示的第一行的行号
			model.addAttribute("firstline", commentScope.getMethodStartLine());

			// highlight 为该对象对应的注释行号，将注释高亮显示
			int endLine = endLineVerify.getEndLine();
			int[] highlightNums = new int[endLine - commentScope.getCommentStartLine() + 1];
			for (int highlightNum = commentScope.getCommentStartLine(); highlightNum <= endLine; highlightNum++) {
				highlightNums[highlightNum - commentScope.getCommentStartLine()] = highlightNum;
			}
			model.addAttribute("highlight", highlightNums);
			return "commentscope_view";
		}
	
	@RequestMapping("/save")
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

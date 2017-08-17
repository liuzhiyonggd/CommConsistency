package commconsistency.web;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import commconsistency.dao.CommentScopeRepository;
import commconsistency.domain.CommentScope;
import commconsistency.domain.Line;
import commconsistency.dto.CommentScopeDto;
import commconsistency.service.CommentScopeService;
import commconsistency.utils.SpringDataPageable;

@Controller
public class CommentScopeController {

	@Autowired
	private CommentScopeService commentScopeService;

	// commentscope list 展示
	@RequestMapping("/commentscopelist")
	public String commentScopeList(@RequestParam("pageno") int pageNo,@RequestParam("pagesize") int pageSize,  Model model) {
		if(pageNo<=1) {
			pageNo = 1;
		}
		// 获取数据库中所有的数据，该部分需要修改，改成分页调用
		SpringDataPageable pageable = new SpringDataPageable();  
		//每页显示条数  
		pageable.setPagesize(pageSize);  
		//当前页  
		pageable.setPagenumber(pageNo);  
		Page<CommentScope> page = commentScopeService.findByVerifyScopeEndLineList(null, pageable);  
		
		if(page.isLast()) {
			pageNo = pageNo-1;
		}
		
		List<CommentScopeDto> commentList = new ArrayList<CommentScopeDto>();

		// 将获取到的数据表重新打包成更小结构的DTO对象，传送给页面展示
		Iterator<CommentScope> iter = page.iterator();
		
		while(iter.hasNext()) {
			CommentScope commentScope = iter.next();
			CommentScopeDto comment = new CommentScopeDto();
			comment.setCommentID(commentScope.getCommentID());
			comment.setProject(commentScope.getProject());
			String[] temps = commentScope.getCommitID().split(" ");
			comment.setCommitID(temps[temps.length - 2]);
			temps = commentScope.getClassName().split("\\\\");
			temps = temps[temps.length - 1].split("\\.");
			comment.setClassName(temps[0]);
			commentList.add(comment);
		}
		model.addAttribute("commentscopelist", commentList);
		model.addAttribute("pageno",pageNo);
		return "commentscopelist";
	}

	// 根据用户传递回来的commentID查找数据库，返回该comment的详细信息。
	@RequestMapping("/commentscopeview")
	public String commentScopeView(@RequestParam("commentID") String paramsStr, Model model) {

		int commentID = Integer.parseInt(paramsStr);
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
		int[] highlightNums = new int[commentScope.getCommentEndLine() - commentScope.getCommentStartLine() + 1];
		for (int highlightNum = commentScope.getCommentStartLine(); highlightNum <= commentScope
				.getCommentEndLine(); highlightNum++) {
			highlightNums[highlightNum - commentScope.getCommentStartLine()] = highlightNum;
		}
		model.addAttribute("highlight", highlightNums);
		return "commentscope";
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
		return new ModelAndView("redirect:/commentscopeview?commentID="+(commentID+1));
	}
}

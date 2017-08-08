package commconsistency.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import commconsistency.dao.CommentScopeRepository;
import commconsistency.domain.CommentScope;
import commconsistency.domain.Line;
import commconsistency.dto.CommentScopeDto;
@Controller
public class CommentScopeController {

	@Autowired
	private CommentScopeRepository commentScopeRepository;
	
	@RequestMapping("/commentscopelist")
	public String commentScopeList(Model model) {
		List<CommentScope> commentScopeList = commentScopeRepository.findAll();
		List<CommentScopeDto> commentList = new ArrayList<CommentScopeDto>();
		for(CommentScope commentScope:commentScopeList) {
			CommentScopeDto comment = new CommentScopeDto();
			comment.setCommentID(commentScope.getCommentID());
			comment.setProject(commentScope.getProject());
			String[] temps = commentScope.getCommitID().split(" ");
			comment.setCommitID(temps[temps.length-2]);
			temps = commentScope.getClassName().split("\\\\");
			temps = temps[temps.length-1].split("\\.");
			comment.setClassName(temps[0]);
			commentList.add(comment);
		}
		model.addAttribute("commentscopelist", commentList);
		return "commentscopelist";
	}
	
	@RequestMapping("/commentscopeview")
	public String commentScopeView(@RequestParam("commentID") String paramsStr,Model model) {
		int commentID = Integer.parseInt(paramsStr);
		Logger.getLogger(CommentScopeController.class).info("commentID:"+commentID);
		CommentScope commentScope = commentScopeRepository.findByCommentID(commentID);
		
		CommentScopeDto comment = new CommentScopeDto();
		comment.setCommentID(commentScope.getCommentID());
		comment.setProject(commentScope.getProject());
		String[] temps = commentScope.getCommitID().split(" ");
		comment.setCommitID(temps[temps.length-2]);
		temps = commentScope.getClassName().split("\\\\");
		temps = temps[temps.length-1].split("\\.");
		comment.setClassName(temps[0]);
		
		StringBuilder builder = new StringBuilder();
		Iterator<Line> iter = commentScope.getCodeList().iterator();
		int i=0;
		Logger.getLogger(CommentScopeController.class).info("methodStartLine:"+commentScope.getMethodStartLine());
		Logger.getLogger(CommentScopeController.class).info("methodEndLine:"+commentScope.getMethodEndLine());
		Logger.getLogger(CommentScopeController.class).info("scopeEndLine:"+commentScope.getScopeEndLine());
		while(iter.hasNext()) {
			i++;
			Line line = iter.next();
			if(i<commentScope.getMethodStartLine()) {
				continue;
			}
			if(i>commentScope.getScopeEndLine()) {
				break;
			}
			
			builder.append(line.getLine().replace("<", "&lt;")).append("\r\n");
		}
		comment.setCode(builder.toString());
		
		model.addAttribute("comment", comment);
		model.addAttribute("firstline",commentScope.getMethodStartLine());
		int[] highlightNums = new int[commentScope.getCommentEndLine()-commentScope.getCommentStartLine()+1];
		for(int highlightNum = commentScope.getCommentStartLine();highlightNum<=commentScope.getCommentEndLine();highlightNum++) {
			highlightNums[highlightNum-commentScope.getCommentStartLine()] = highlightNum;
		}
//		String temp = "";
//		for(int h=0;h<highlightNums.length-1;h++) {
//			temp += highlightNums[h]+",";
//		}
//		temp += highlightNums[highlightNums.length-1];
		model.addAttribute("highlight",highlightNums);
		return "commentscope";
	}
}

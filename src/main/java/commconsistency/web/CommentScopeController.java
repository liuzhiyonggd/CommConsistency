package commconsistency.web;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import commconsistency.domain.CommentScope;
import commconsistency.domain.EndLineVerify;
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
	@RequestMapping("/commentscope/verificationlist")
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
			String[] temps = subCommentScope.getClassName().split("\\\\");
			temps = temps[temps.length - 1].split("\\.");
			comment.setClassName(temps[0]);
			commentList.add(comment);
		}
		//TODO 先关闭注释作用域的查看页面
		model.addAttribute("commentscopelist", commentList);
		model.addAttribute("pageno",pageNo);
		return "commentscope/verificationlist";
	}
	
	// commentscope list(未验证) 展示
		@RequestMapping("/commentscope/viewlist")
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
				String[] temps = subCommentScope.getClassName().split("\\\\");
				temps = temps[temps.length - 1].split("\\.");
				comment.setClassName(temps[0]);
				commentList.add(comment);
			}
			
			//TODO 先关闭注释作用域查看页面
			model.addAttribute("commentscopelist", commentList);
			model.addAttribute("pageno",pageNo);
			return "commentscope/viewlist";
		}

	// 根据用户传递回来的commentID查找表，返回该comment的详细信息。
	@RequestMapping("/commentscope/verification")
	public String commentScopeVerification(@RequestParam("commentID") String paramsStr, Model model) {

		int commentID = Integer.parseInt(paramsStr);
		CommentScope commentScope = commentScopeService.findByCommentID(commentID);
		EndLineVerify verify = endLineVerifyService.findByCommentID(commentID);
		while(commentScope==null||verify!=null) {
			commentID = getNextCommentID(commentID);
			commentScope = commentScopeService.findByCommentID(commentID);
			verify = endLineVerifyService.findByCommentID(commentID);
			System.out.println("commentScope:"+commentScope+" verify:"+verify);
		}

		// 将查找到的对象进行DTO对象转换，转换成更小的对象传递给页面展示
		CommentScopeDto comment = new CommentScopeDto();
		comment.setCommentID(commentScope.getCommentID());
		comment.setProject(commentScope.getProject());
		String[] temps = commentScope.getClassName().split("\\\\");
		temps = temps[temps.length - 1].split("\\.");
		comment.setClassName(temps[0]);

		// 将源代码转换成可以在SyntaxHighlighter中显示的字符串，并保存在Model中。
		StringBuilder builder = new StringBuilder();
		List<String> codeList = commentScope.getCodeList();
		for(int i=0;i<codeList.size();i++) {
			String line = codeList.get(i);
			if (i < commentScope.getMethodStartLine()-1) {
				continue;
			}
			if (i > commentScope.getScopeEndLine()-1) {
				break;
			}

			builder.append(line.replace("<", "&lt;")).append("\r\n");
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
		return "commentscope/verification";
	}
	
	
	// 根据用户传递回来的commentID查找表，返回该comment的详细信息。
		@RequestMapping("/commentscope/view")
		public String commentScopeView(@RequestParam("commentID") String paramsStr,@RequestParam("isNext") String isNext, Model model) {

			int commentID = Integer.parseInt(paramsStr);
			if(isNext.equals("true")) {
				commentID = getNextCommentID(commentID);
			}else {
				commentID = getPreviousCommentID(commentID);
			}
			EndLineVerify endLineVerify = endLineVerifyService.findByCommentID(commentID);
			while(endLineVerify==null) {
				if(isNext.equals("true")) {
				    commentID = getNextCommentID(commentID);
				    endLineVerify = endLineVerifyService.findByCommentID(commentID);
				}else {
					commentID = getPreviousCommentID(commentID);
					endLineVerify = endLineVerifyService.findByCommentID(commentID);
				}
			}

			CommentScope commentScope = commentScopeService.findByCommentID(commentID);
			// 将查找到的对象进行DTO对象转换，转换成更小的对象传递给页面展示
			CommentScopeDto comment = new CommentScopeDto();
			comment.setCommentID(commentScope.getCommentID());
			comment.setProject(commentScope.getProject());
			String[] temps = commentScope.getClassName().split("\\\\");
			temps = temps[temps.length - 1].split("\\.");
			comment.setClassName(temps[0]);

			// 将源代码转换成可以在SyntaxHighlighter中显示的字符串，并保存在Model中。
			StringBuilder builder = new StringBuilder();
			Iterator<String> iter = commentScope.getCodeList().iterator();
			int i = 0;
			while (iter.hasNext()) {
				i++;
				String line = iter.next();
				if (i < commentScope.getMethodStartLine()) {
					continue;
				}
				if (i > commentScope.getScopeEndLine()) {
					break;
				}

				builder.append(line.replace("<", "&lt;")).append("\r\n");
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
			return "commentscope/view";
		}
	
	@RequestMapping("/commentscope/save")
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
		
		return new ModelAndView("redirect:/commentscope/verification?commentID="+getNextCommentID(commentID));
	}
	
	@RequestMapping("/commentscope/uploadview")
	public String fileUploadView(Model model) {
		return "commentscope/uploadview";
	}
	
	@RequestMapping("/commentscope/uploadfile")
	public String fileUpload(HttpServletRequest req,MultipartHttpServletRequest multiReq,Model model) {
		
		File file = new File("d:/temp.java");
		try {
			multiReq.getFile("file").transferTo(file);
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		return "index";
	}
	private static Map<Integer,Integer> nextIDMap = new HashMap<Integer,Integer>();
	private static Map<Integer,Integer> previousIDMap = new HashMap<Integer,Integer>();
	static {
		try {
//			String path = CommentScopeController.class.getResource("/").getPath();
//			System.out.println(path);
//			String path2 = CommentScopeController.class.getResource("").getPath();
//			System.out.println(path2);
//			String path3 = CommentScopeController.class.getClassLoader().getResource("file/r_commentidlist.txt").getPath();
//			System.out.println(path3);
			Resource resource = new ClassPathResource("file/r_commentidlist.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			List<Integer> idList = new ArrayList<Integer>();
			String str = null;
			while((str = br.readLine())!=null) {
				idList.add(Integer.parseInt(str));
			}
			for(int i=0;i<idList.size()-1;i++) {
				nextIDMap.put(idList.get(i), idList.get(i+1));
			}
			nextIDMap.put(idList.get(idList.size()-1), idList.get(0));
			nextIDMap.put(0,idList.get(0));
			
			for(int i=1;i<idList.size();i++) {
				previousIDMap.put(idList.get(i), idList.get(i-1));
			}
			previousIDMap.put(idList.get(0), idList.get(idList.size()-1));
			previousIDMap.put(0, idList.get(0));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private int getNextCommentID(int commentID) {
		if(nextIDMap.containsKey(commentID)) {
		    return nextIDMap.get(commentID);
		}else {
			return nextIDMap.get(0);
		}
	}
	private int getPreviousCommentID(int commentID) {
		if(previousIDMap.containsKey(commentID)) {
			return previousIDMap.get(commentID);
		}else {
			return previousIDMap.get(0);
		}
	}
}

package commconsistency.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import commconsistency.domain.CommentEntry;
import commconsistency.domain.ConsistencyVerify;
import commconsistency.domain.DiffType;
import commconsistency.domain.SubCommentEntry;
import commconsistency.dto.CommentEntryDto;
import commconsistency.service.CommentEntryService;
import commconsistency.service.ConsistencyVerifyService;
import commconsistency.service.SubCommentEntryService;
import commconsistency.utils.SpringDataPageable;

@Controller
public class ConsistencyController {

	@Autowired
	private CommentEntryService commentEntryService;
	@Autowired
	private SubCommentEntryService subCommentEntryService;
	@Autowired
	private ConsistencyVerifyService consistencyVerifyService;
	
	// consistency list(未验证) 展示
	@RequestMapping("/consistency/verificationlist")
	public String commentEntryListVerification(@RequestParam("pageno") int pageNo,
			@RequestParam("pagesize") int pageSize, Model model) {
		if (pageNo <= 1) {
			pageNo = 1;
		}
		SpringDataPageable pageable = new SpringDataPageable();
		// 每页显示条数
		pageable.setPagesize(pageSize);
		// 当前页
		pageable.setPagenumber(pageNo);

		// 使用数据量更小的表sub_comment_scope，加快读取速度
		Page<SubCommentEntry> page = subCommentEntryService.findByIsVerify(false,pageable);

		if (page.isLast()) {
			pageNo = pageNo - 1;
		}

		List<CommentEntryDto> commentList = new ArrayList<CommentEntryDto>();

		// 将获取到的数据表重新打包成更小结构的DTO对象，传送给页面展示
		Iterator<SubCommentEntry> iter = page.iterator();

		while (iter.hasNext()) {
			SubCommentEntry subCommentEntry = iter.next();
			CommentEntryDto comment = new CommentEntryDto();
			comment.setCommentID(subCommentEntry.getCommentID());
			comment.setProject(subCommentEntry.getProject());
			String[] temps = subCommentEntry.getClassName().split("\\\\");
			temps = temps[temps.length - 1].split("\\.");
			comment.setClassName(temps[0]);
			commentList.add(comment);
		}
		model.addAttribute("commententrylist", commentList);
		model.addAttribute("pageno", pageNo);
		return "consistency/verificationlist";
	}
	
	// 根据用户传递回来的ID查找表，返回该methodExtractor的详细信息。
			@RequestMapping("/consistency/verificationview")
			public String commentEntryViewVerification(@RequestParam("id") String id,Model model) {
	            int commentId = Integer.parseInt(id);
				CommentEntry commentEntry = commentEntryService.findByCommentID(commentId);
//				while(commentEntry.isVerify()) {
//					commentId = getNextCommentID(commentId);
//					commentEntry = commentEntryService.findByCommentID(commentId);
//				}
				CommentEntryDto commentEntryDto = new CommentEntryDto();
				commentEntryDto.setProject(commentEntry.getProject());
				commentEntryDto.setCommitID(commentEntry.getCommitID());
				commentEntryDto.setClassName(commentEntry.getClassName());
				commentEntryDto.setCommentID(commentEntry.getCommentID());
				
				List<String> oldCodes = commentEntry.getOldComment();
				oldCodes.addAll(commentEntry.getOldCode());
				StringBuilder sb = new StringBuilder();
				for(String str:oldCodes) {
					sb.append(str.replace("<", "&lt;")).append("\r\n");
				}
				commentEntryDto.setOldCode(sb.toString());
				
				List<String> newCodes = commentEntry.getNewComment();
				newCodes.addAll(commentEntry.getNewCode());
				sb = new StringBuilder();
				for(String str:newCodes) {
					sb.append(str.replace("<", "&lt;")).append("\r\n");
				}
				commentEntryDto.setNewCode(sb.toString());
				model.addAttribute("commententry", commentEntryDto);
				
				Set<Integer> oldHighLightSet = new TreeSet<Integer>();
				Set<Integer> newHighLightSet = new TreeSet<Integer>();
				int newOffSet = newOffSetMap.get(commentEntry.getCommentID());
				int oldOffSet = oldOffSetMap.get(commentEntry.getCommentID());
				int entryNewStartLine = commentEntry.getNew_comment_startLine()-newOffSet;
				int entryOldStartLine =commentEntry.getOld_comment_startLine()-oldOffSet;
				for(DiffType diff:commentEntry.getDiffList()){
					if(diff.getType().indexOf("PARENT")<0&&diff.getType().indexOf("ORDER")<0){
					if(diff.getNewStartLine()>0){
						if(diff.getType().indexOf("CONDITION")<0){
						for(int k=diff.getNewStartLine();k<diff.getNewEndLine()+1;k++){
							int newChangeIndex = k-entryNewStartLine+1;
								newHighLightSet.add(newChangeIndex);
						}
						}else{
								newHighLightSet.add(diff.getNewStartLine()-entryNewStartLine+1);
						}
					}
					if(diff.getOldStartLine()>0){
						if(diff.getType().indexOf("CONDITION")<0){
						for(int k=diff.getOldStartLine();k<diff.getOldEndLine()+1;k++){
							int oldChangeIndex = k-entryOldStartLine+1;
								oldHighLightSet.add(oldChangeIndex);
						}
						}else{
							oldHighLightSet.add(diff.getOldStartLine()-entryOldStartLine+1);
							}
						}
					}
					}
				
				int[] oldHighLight = new int[oldHighLightSet.size()];
				Iterator<Integer> itea = oldHighLightSet.iterator();
				int i=0;
				while(itea.hasNext()) {
					oldHighLight[i] = itea.next();
					i++;
				}
				
				int[] newHighLight = new int[newHighLightSet.size()];
				itea = newHighLightSet.iterator();
				i=0;
				while(itea.hasNext()) {
					newHighLight[i] = itea.next();
					i++;
				}
				model.addAttribute("oldhighlight", oldHighLight);
				model.addAttribute("newhighlight",newHighLight);
				return "consistency/verificationview";
			}
		
			//保存methodExtractor
		@RequestMapping("/consistency/save")
		public ModelAndView consistencySave(String comment_id,String reason,Model model) {
			int commentId = Integer.parseInt(comment_id);
			boolean isChange = false;
			if(reason.equals("add_function")||reason.equals("change_related")||reason.equals("delete_related")) {
			    isChange = true;	
			}
			
			CommentEntry commentEntry = commentEntryService.findByCommentID(commentId);
			SubCommentEntry subCommentEntry = subCommentEntryService.findByCommentID(commentId);
			commentEntry.setVerify(true);
			commentEntryService.save(commentEntry);
			
			subCommentEntry.setVerify(true);
			subCommentEntryService.save(subCommentEntry);
			
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()  
				    .getAuthentication()  
				    .getPrincipal(); 
			
			ConsistencyVerify consistencyVerify = new ConsistencyVerify();
			consistencyVerify.setUserName(userDetails.getUsername());
			consistencyVerify.setCommentID(commentId);
			consistencyVerify.setChange(isChange);
			consistencyVerify.setChangeReason(reason);
			consistencyVerifyService.save(consistencyVerify);
			
			return new ModelAndView("redirect:/consistency/verificationview?id="+(getNextCommentID(commentId)));
		}
		
		private static Map<Integer,Integer> nextIDMap = new HashMap<Integer,Integer>();
		private static Map<Integer,Integer> oldOffSetMap = new HashMap<Integer,Integer>();
		private static Map<Integer,Integer> newOffSetMap = new HashMap<Integer,Integer>();
		static {
			try {
				List<String> randomIDFile = FileUtils.readLines(new File("file/false1_rq2_4000.txt"),"UTF-8");
				List<Integer> idList = new ArrayList<Integer>();
				for(String str:randomIDFile) {
					idList.add(Integer.parseInt(str));
				}
				Collections.sort(idList);
				for(int i=0;i<idList.size()-1;i++) {
					nextIDMap.put(idList.get(i), idList.get(i+1));
				}
				nextIDMap.put(idList.get(idList.size()-1), 0);
				
				List<String> oldOffSetFile = FileUtils.readLines(new File("file/oldchangeline2.csv"),"UTF-8");
				for(String str:oldOffSetFile) {
					String[] temps = str.split(",");
					oldOffSetMap.put(Integer.parseInt(temps[0]), Integer.parseInt(temps[1]));
				}
				List<String> newOffSetFile = FileUtils.readLines(new File("file/newchangeline2.csv"),"UTF-8");
				for(String str:newOffSetFile) {
					String[] temps = str.split(",");
					newOffSetMap.put(Integer.parseInt(temps[0]), Integer.parseInt(temps[1]));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		private int getNextCommentID(int commentID) {
			if(nextIDMap.containsKey(commentID)) {
			    return nextIDMap.get(commentID);
			}else {
				return 42;
			}
		}
		
		
		
		
}

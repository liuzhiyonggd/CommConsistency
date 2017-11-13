package commconsistency.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import commconsistency.dao.ConsistencyVerifyRepository;
import commconsistency.dao.RepositoryFactory;
import commconsistency.domain.ConsistencyVerify;

public class VerifyLoader {
	
	public static void main(String[] args) throws IOException {
		ConsistencyVerifyRepository verifyRepo = RepositoryFactory.getConsistencyRepository();
		List<ConsistencyVerify> verifyList = verifyRepo.findAll();
		Map<Integer,List<String>> result = new LinkedHashMap<Integer,List<String>>();
		
		for(ConsistencyVerify verify:verifyList) {
			int commentID = verify.getCommentID();
			if(result.containsKey(commentID)) {
				result.get(commentID).add(verify.getChangeReason()+","+(verify.isChange()?1:0));
			}else {
				List<String> strList = new ArrayList<String>();
				strList.add(verify.getChangeReason()+","+(verify.isChange()?1:0));
				result.put(commentID, strList);
			}
		}
		List<String> output = new ArrayList<String>();
		for(Map.Entry<Integer, List<String>> entry:result.entrySet()) {
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<entry.getValue().size()-1;i++) {
				sb.append(entry.getValue().get(i)+",");
			}
			sb.append(entry.getValue().get(entry.getValue().size()-1));
			
			output.add(entry.getKey()+","+sb.toString());
		}
		FileUtils.writeLines(new File("E:\\注释一致性实验\\数据\\old_data\\2\\statistic_result.csv"), output);
	}

}

package commconsistency.refactor.utils;

import java.util.ArrayList;
import java.util.List;


public class StringTool {
	
	public static List<String> splitPath(String path){
		List<String> result = new ArrayList<String>();
		
		String[] temps = path.split("\\\\");
		String project = temps[2];
		String commitID = temps[3];
		String className = "\\";
		for(int i=5;i<temps.length-1;i++) {
			className = className+temps[i]+"\\";
		}
		className += temps[temps.length-1];
		result.add(project);
		result.add(commitID);
		result.add(className);
		return result;
	}
	
}

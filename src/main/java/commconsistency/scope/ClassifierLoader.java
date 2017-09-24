package commconsistency.scope;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ClassifierLoader {

	public static RandomForestClassifier getClassify(String objPath) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(new File(objPath));  
		   ObjectInputStream ois = new ObjectInputStream(fis);  
		   RandomForestClassifier rs = (RandomForestClassifier) ois.readObject();  
		   ois.close();  
		   fis.close(); 
		   return rs;
	}
}

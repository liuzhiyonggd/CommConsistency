package commconsistency.dao;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class RepositoryFactory {
	
	private final static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class); 
	
	public static AnnotationConfigApplicationContext getContext(){
		return context;
	}
	
	
	public static CommentScopeRepository getCommentRepository(){
		return context.getBean(CommentScopeRepository.class);
	}
	
	public static EndLineVerifyRepository getEndLineVerifyRepository(){
		return context.getBean(EndLineVerifyRepository.class);
	}
	
	public static CommentEntryRepository getCommentEntryRepository() {
		return context.getBean(CommentEntryRepository.class);
	}
	
	public static SubCommentEntryRepository getSubCommentEntryRepository() {
		return context.getBean(SubCommentEntryRepository.class);
	}
	
	public static ConsistencyVerifyRepository getConsistencyRepository() {
		return context.getBean(ConsistencyVerifyRepository.class);
	}
	
	

}

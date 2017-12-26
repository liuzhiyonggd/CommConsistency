package commconsistency.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import commconsistency.dao.CommentEntryRepository;
import commconsistency.dao.SubCommentEntryRepository;
import commconsistency.domain.CommentEntry;
import commconsistency.domain.SubCommentEntry;
import commconsistency.utils.SpringDataPageable;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubCommentEntryRepositoryTest{
	
	@Autowired
	private CommentEntryRepository subCommRepo;
	
	@Test
	public void findByFilter2Test() {
		SpringDataPageable pageable = new SpringDataPageable();
		// 每页显示条数
		pageable.setPagesize(20);
		// 当前页
		pageable.setPagenumber(1);
		CommentEntry comment = subCommRepo.findASingleByCommentID(1);
		
		assertEquals(1, comment.getCommentID());
	}
	
}

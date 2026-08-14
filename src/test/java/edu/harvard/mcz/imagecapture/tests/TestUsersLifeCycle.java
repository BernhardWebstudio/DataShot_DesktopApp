package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.Users;
import edu.harvard.mcz.imagecapture.exceptions.NoSuchValueException;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycle;
import java.util.List;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

/**
 * Tests for UsersLifeCycle and user authentication/authorization.
 */
public class TestUsersLifeCycle extends TestCase {

	@Test
	public void testUsersCRUDAndAuthentication() throws SaveFailedException, NoSuchValueException {
		Session session = HibernateUtil.getTestSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		Users admin = new Users();
		admin.setUsername("testadmin");
		admin.setFullname("Admin User");
		admin.setRole(Users.ROLE_ADMINISTRATOR);
		admin.setHash("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8"); // test hash
		admin.setDescription("Administrator account");
		admin.setCanCopyPaste(true);
		session.persist(admin);

		Users editor = new Users();
		editor.setUsername("testeditor");
		editor.setFullname("Editor User");
		editor.setRole(Users.ROLE_EDITOR);
		editor.setHash("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8");
		editor.setDescription("Editor account");
		editor.setCanCopyPaste(false);
		session.persist(editor);

		transaction.commit();
		session.close();

		UsersLifeCycle uls = new UsersLifeCycle();

		// Test find by credentials
		List<Users> found = uls.findByCredentials("testadmin", "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8");
		assertEquals(1, found.size());
		assertEquals("Admin User", found.get(0).getFullname());
		assertTrue(found.get(0).canCopyPaste());

		// Test find with wrong password
		List<Users> notFound = uls.findByCredentials("testadmin", "wrong_hash");
		assertEquals(0, notFound.size());

		// Test isUserAdministrator
		assertTrue(UsersLifeCycle.isUserAdministrator(admin.getUserid()));
		assertFalse(UsersLifeCycle.isUserAdministrator(editor.getUserid()));

		// Test getFullNameForUserName
		assertEquals("Admin User", uls.getFullNameForUserName("testadmin"));
		assertEquals("Editor User", uls.getFullNameForUserName("testeditor"));

		// Test findAll
		List<Users> allUsers = UsersLifeCycle.findAll();
		assertTrue(allUsers.size() >= 2);

		// Test role hierarchy
		assertTrue(admin.isUserRole(Users.ROLE_ADMINISTRATOR));
		assertTrue(admin.isUserRole(Users.ROLE_DATAENTRY));
		assertTrue(admin.isUserRole(Users.ROLE_EDITOR));
		assertTrue(editor.isUserRole(Users.ROLE_EDITOR));
		assertTrue(editor.isUserRole(Users.ROLE_DATAENTRY));
		assertFalse(editor.isUserRole(Users.ROLE_ADMINISTRATOR));
	}
}

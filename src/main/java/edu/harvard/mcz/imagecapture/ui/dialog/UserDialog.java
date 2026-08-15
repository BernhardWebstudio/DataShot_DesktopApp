/**
 * UserDialog.java
 * edu.harvard.mcz.imagecapture
 *
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 */
package edu.harvard.mcz.imagecapture.ui.dialog;

import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.entity.Users;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycle;
import edu.harvard.mcz.imagecapture.ui.binding.FormBindingContext;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

/**
 * UserDialog is a user interface for editing metadata about participants in the
 * project, using declarative form bindings.
 *
 * @see Users
 */
public class UserDialog extends JDialog {

	private static final long serialVersionUID = -8881672324009775369L;

	private final UserDialog thisDialog;
	private final FormBindingContext<Users> bindingContext;
	private final Users userToEdit;
	private boolean wasCancled = true;

	private JLabel jLabelMessage;
	private JTextField jTextFieldUsername;

	/**
	 * Default constructor. Dialog is built as modal off of MainFrame.
	 */
	public UserDialog() {
		super(Singleton.getSingletonInstance().getMainFrame(), true);
		thisDialog = this;
		userToEdit = new Users();
		userToEdit.setRole("undefined");
		bindingContext = new FormBindingContext<>(Users.class, true);
		initialize();
		bindingContext.readFrom(userToEdit);
	}

	/**
	 * Constructor specifying a user to edit. Dialog is built as modal off of
	 * MainFrame.
	 */
	public UserDialog(Users aUser) {
		super(Singleton.getSingletonInstance().getMainFrame(), true);
		thisDialog = this;
		userToEdit = aUser != null ? aUser : new Users();
		bindingContext = new FormBindingContext<>(Users.class, true);
		initialize();
		bindingContext.readFrom(userToEdit);
		if (jTextFieldUsername != null) {
			jTextFieldUsername.setEditable(false);
		}
	}

	/**
	 * @return the user edited in this dialog.
	 */
	public Users getUser() {
		return userToEdit;
	}

	/**
	 * @return false if the user was saved, true otherwise.
	 */
	public boolean getWasCancled() {
		return wasCancled;
	}

	private void initialize() {
		this.setSize(new Dimension(504, 210));
		this.setPreferredSize(new Dimension(504, 210));
		this.setTitle("Details about a person");

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel formPanel = new JPanel(new MigLayout("wrap 2, fillx, insets 10"));
		jLabelMessage = new JLabel("Who is this?");
		formPanel.add(jLabelMessage, "span 2, center, gapbottom 10");

		formPanel.add(new JLabel("Database Username:"), "tag label, right");
		jTextFieldUsername = bindingContext.bindTextField("username", Users::getUsername, Users::setUsername);
		formPanel.add(jTextFieldUsername, "grow");

		formPanel.add(new JLabel("Full Name:"), "tag label, right");
		formPanel.add(bindingContext.bindTextField("fullname", Users::getFullname, Users::setFullname), "grow");

		formPanel.add(new JLabel("About this person:"), "tag label, right");
		formPanel.add(bindingContext.bindTextField("description", Users::getDescription, Users::setDescription),
				"grow");

		mainPanel.add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton jButtonCancel = new JButton("Cancel");
		jButtonCancel.addActionListener(e -> thisDialog.setVisible(false));
		buttonPanel.add(jButtonCancel);

		JButton jButtonSave = new JButton("Save");
		jButtonSave.addActionListener(e -> {
			bindingContext.writeTo(userToEdit);
			userToEdit.setRole("undefined");
			UsersLifeCycle u = new UsersLifeCycle();
			Users check = new Users();
			check.setUsername(userToEdit.getUsername());
			try {
				List<Users> usersToCheck = u.findByNames(check.getUsername(), check.getFullname());
				if (usersToCheck.isEmpty()) {
					u.persist(userToEdit);
				} else {
					u.attachDirty(userToEdit);
				}
				wasCancled = false;
				thisDialog.setVisible(false);
			} catch (SaveFailedException ex) {
				setMessage("Unable to save this record. Name or About may be too long; " + ex.getMessage());
			}
		});
		buttonPanel.add(jButtonSave);
		getRootPane().setDefaultButton(jButtonSave);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
	}

	/**
	 * Set the message to appear in the dialog above the data entry fields.
	 *
	 * @param text
	 *            the text of the message.
	 */
	public void setMessage(String text) {
		if (jLabelMessage != null) {
			jLabelMessage.setText(text);
		}
	}
}

/**
 * SpecimenBrowser.java
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
package edu.harvard.mcz.imagecapture;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.data.SpecimenCache;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.interfaces.DataChangeListener;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.ui.ButtonEditor;
import edu.harvard.mcz.imagecapture.ui.ButtonRenderer;
import edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditor;
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModel;
import edu.harvard.mcz.imagecapture.ui.tablemodel.TableColumnManager;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import org.hibernate.SessionException;
import org.hibernate.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SpecimenBrowser is a Searchable, Sortable, tabular view of multiple
 * specimens.
 */
public class SpecimenBrowser extends JPanel implements DataChangeListener {

	private static final long serialVersionUID = 1336228109288304785L;

	private static final Logger log = LoggerFactory.getLogger(SpecimenBrowser.class);

	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JPanel jPanel = null;
	private JTextField jTextField = null;
	private JComboBox jComboBox = null;
	private TableRowSorter<SpecimenListTableModel> sorter;
	private JTextField jTextFieldFamily = null;
	private JTextField jTextFieldDrawerNumber = null;
	private Map<String, Object> baseSearchCriteria = new HashMap<>();
	private Map<String, Object> searchCriteria2 = null;
	private boolean useLike = false;
	private int maxResults = 0;
	private int offset = 0;
	private javax.swing.Timer filterDebounceTimer = null;

	public static final int DEFAULT_PAGE_SIZE = 1000;

	private long totalCount = -1;
	private String sortProperty = "barcode";
	private boolean sortAscending = true;
	private int currentSortModelCol = SpecimenListTableModel.COL_BARCODE;

	private JPanel jPanelPagination = null;
	private JButton jButtonFirst = null;
	private JButton jButtonPrev = null;
	private JButton jButtonNext = null;
	private JButton jButtonLast = null;
	private JLabel jLabelPage = null;
	private JLabel jLabelJump = null;
	private JTextField jTextFieldPage = null;
	private JButton jButtonGo = null;

	/**
	 * This method initializes an instance of SpecimenBrowser
	 */
	public SpecimenBrowser() {
		this(null, true, DEFAULT_PAGE_SIZE, 0);
	}

	public SpecimenBrowser(Map<String, Object> criteria, boolean like, int limit, int offset) {
		super();
		this.useLike = like;
		this.baseSearchCriteria = (criteria != null) ? new HashMap<>(criteria) : new HashMap<>();
		this.searchCriteria2 = new HashMap<>(this.baseSearchCriteria);
		this.maxResults = limit > 0 ? limit : DEFAULT_PAGE_SIZE;
		this.offset = Math.max(0, offset);
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(444, 290));
		this.add(getJPanelPagination(), BorderLayout.SOUTH);
		this.add(getJScrollPane(), BorderLayout.CENTER);
		this.add(getJPanel(), BorderLayout.NORTH);
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			try {
				jScrollPane.setViewportView(getJTable());
			} catch (SessionException | TransactionException e) {
				log.debug(e.getMessage(), e);
				if (Singleton.getSingletonInstance().getMainFrame() != null) {
					Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Database Connection Error.");
				}
				HibernateUtil.terminateSessionFactory();
				this.setVisible(false);
			}
			jScrollPane.setPreferredSize(new Dimension(444, 290));
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable
	 *
	 * @return javax.swing.JTable
	 */
	public JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable();
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
			SpecimenLifeCycle s = new SpecimenLifeCycle();
			if (totalCount < 0) {
				totalCount = (searchCriteria2 != null)
						? s.countBy(this.searchCriteria2, useLike)
						: s.countBy(Collections.emptyMap(), false);
			}
			List<Specimen> results = s.findSpecimensForTable(
					(this.searchCriteria2 != null) ? this.searchCriteria2 : Collections.emptyMap(), maxResults, offset,
					useLike, sortProperty, sortAscending);
			SpecimenListTableModel model = new SpecimenListTableModel(results);
			model.setSortInfo(currentSortModelCol, sortAscending);
			jTable.setModel(model);
			new TableColumnManager(jTable);
			sorter = new TableRowSorter<>(model);
			for (int c = 0; c < model.getColumnCount(); c++) {
				sorter.setSortable(c, false);
			}
			jTable.setRowSorter(sorter);

			jTable.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					int viewCol = jTable.getTableHeader().columnAtPoint(e.getPoint());
					if (viewCol < 0) {
						return;
					}
					int modelCol = jTable.convertColumnIndexToModel(viewCol);
					if (!SpecimenDetailsViewPane.isCopyPasteActivated()) {
						modelCol += 1;
					}
					if (modelCol == SpecimenListTableModel.COL_ID || modelCol == SpecimenListTableModel.COL_COPY) {
						return;
					}
					if (modelCol == currentSortModelCol) {
						sortAscending = !sortAscending;
					} else {
						currentSortModelCol = modelCol;
						sortAscending = true;
					}
					sortProperty = getPropertyNameForColumn(modelCol);
					offset = 0;
					loadData();
				}
			});

			jTable.setDefaultRenderer(Specimen.class, new ButtonRenderer());
			jTable.setDefaultEditor(Specimen.class, new ButtonEditor());
			if (SpecimenDetailsViewPane.isCopyPasteActivated()) {
				jTable.getColumn(jTable.getColumnName(SpecimenListTableModel.COL_COPY))
						.setCellRenderer(new ButtonRenderer("Copy"));
				jTable.getColumn(jTable.getColumnName(SpecimenListTableModel.COL_COPY))
						.setCellEditor(new CopyRowButtonEditor(new JCheckBox()));
			}

			// Speculatively prefetch specimen on selection change
			jTable.getSelectionModel().addListSelectionListener(e -> {
				if (!e.getValueIsAdjusting()) {
					int selectedRow = jTable.getSelectedRow();
					if (selectedRow >= 0 && selectedRow < jTable.getRowCount()) {
						int modelRow = jTable.convertRowIndexToModel(selectedRow);
						Object val = jTable.getModel().getValueAt(modelRow, SpecimenListTableModel.COL_ID);
						if (val instanceof Specimen) {
							Specimen sp = (Specimen) val;
							if (sp.getSpecimenId() != null) {
								SpecimenCache.prefetchAsync(sp.getSpecimenId());
							}
						}
					}
				}
			});

			// Speculatively prefetch specimen on mouse hover
			jTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
				private int lastHoverRow = -1;

				@Override
				public void mouseMoved(java.awt.event.MouseEvent e) {
					int viewRow = jTable.rowAtPoint(e.getPoint());
					if (viewRow >= 0 && viewRow < jTable.getRowCount() && viewRow != lastHoverRow) {
						lastHoverRow = viewRow;
						int modelRow = jTable.convertRowIndexToModel(viewRow);
						Object val = jTable.getModel().getValueAt(modelRow, SpecimenListTableModel.COL_ID);
						if (val instanceof Specimen) {
							Specimen sp = (Specimen) val;
							if (sp.getSpecimenId() != null) {
								SpecimenCache.prefetchAsync(sp.getSpecimenId());
							}
						}
					}
				}
			});

			// set some column widths
			int characterWidth = Singleton.getSingletonInstance().getCharacterWidth();
			jTable.getColumnModel().getColumn(0).setPreferredWidth(characterWidth * 3);
			jTable.getColumnModel().getColumn(1).setPreferredWidth(characterWidth * 3);
			jTable.getColumnModel().getColumn(2).setPreferredWidth(characterWidth * 14);

			updatePaginationControls(results.size());
		}
		return jTable;
	}

	private String getPropertyNameForColumn(int modelColumnIndex) {
		switch (modelColumnIndex) {
			case SpecimenListTableModel.COL_BARCODE :
				return "barcode";
			case SpecimenListTableModel.COL_WORKFLOW :
				return "workFlowStatus";
			case SpecimenListTableModel.COL_FAMILY :
				return "family";
			case SpecimenListTableModel.COL_SUBFAMILY :
				return "subfamily";
			case SpecimenListTableModel.COL_TRIBE :
				return "tribe";
			case SpecimenListTableModel.COL_GENUS :
				return "genus";
			case SpecimenListTableModel.COL_SPECIFIC :
				return "specificEpithet";
			case SpecimenListTableModel.COL_SUBSPECIFIC :
				return "subspecificEpithet";
			case SpecimenListTableModel.COL_COUNTRY :
				return "country";
			case SpecimenListTableModel.COL_DIVISION :
				return "primaryDivison";
			case SpecimenListTableModel.COL_VERBLOCALITY :
				return "verbatimLocality";
			case SpecimenListTableModel.COL_COLLECTION :
				return "collection";
			case SpecimenListTableModel.COL_COLLECTION_NR :
				return "numbers.number";
			case SpecimenListTableModel.COL_ID :
			case SpecimenListTableModel.COL_COPY :
			default :
				return "specimenId";
		}
	}

	public JPanel getJPanelPagination() {
		if (jPanelPagination == null) {
			jPanelPagination = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));

			jButtonFirst = new JButton("|◀");
			jButtonFirst.setToolTipText("First page");
			jButtonFirst.setEnabled(offset > 0);
			jButtonFirst.addActionListener(e -> {
				offset = 0;
				loadData();
			});

			jButtonPrev = new JButton("◀ Prev");
			jButtonPrev.setToolTipText("Previous page");
			jButtonPrev.setEnabled(offset > 0);
			jButtonPrev.addActionListener(e -> {
				if (offset > 0) {
					offset = Math.max(0, offset - maxResults);
					loadData();
				}
			});

			jLabelPage = new JLabel("Loading specimens...");

			jLabelJump = new JLabel("Page:");
			jTextFieldPage = new JTextField(4);
			jTextFieldPage.setHorizontalAlignment(JTextField.CENTER);
			jTextFieldPage.addActionListener(e -> jumpToPage());

			jButtonGo = new JButton("Go");
			jButtonGo.addActionListener(e -> jumpToPage());

			jButtonNext = new JButton("Next ▶");
			jButtonNext.setToolTipText("Next page");
			jButtonNext.setEnabled(false);
			jButtonNext.addActionListener(e -> {
				offset = offset + maxResults;
				loadData();
			});

			jButtonLast = new JButton("▶|");
			jButtonLast.setToolTipText("Last page");
			jButtonLast.setEnabled(false);
			jButtonLast.addActionListener(e -> {
				if (totalCount > 0) {
					int totalPages = (int) Math.ceil((double) totalCount / maxResults);
					offset = Math.max(0, (totalPages - 1) * maxResults);
					loadData();
				}
			});

			jPanelPagination.add(jButtonFirst);
			jPanelPagination.add(jButtonPrev);
			jPanelPagination.add(jLabelPage);
			jPanelPagination.add(jLabelJump);
			jPanelPagination.add(jTextFieldPage);
			jPanelPagination.add(jButtonGo);
			jPanelPagination.add(jButtonNext);
			jPanelPagination.add(jButtonLast);

			int rowCount = (jTable != null && jTable.getModel() != null) ? jTable.getModel().getRowCount() : 0;
			updatePaginationControls(rowCount);
		}
		return jPanelPagination;
	}

	public void jumpToPage() {
		if (jTextFieldPage == null) {
			return;
		}
		try {
			int targetPage = Integer.parseInt(jTextFieldPage.getText().trim());
			long count = totalCount >= 0 ? totalCount : 0;
			int totalPages = (int) Math.ceil((double) (count > 0 ? count : 1) / maxResults);
			if (totalPages <= 0) {
				totalPages = 1;
			}
			if (targetPage < 1) {
				targetPage = 1;
			}
			if (targetPage > totalPages) {
				targetPage = totalPages;
			}
			offset = (targetPage - 1) * maxResults;
			loadData();
		} catch (NumberFormatException ex) {
			// ignore non-numeric input
		}
	}

	public void loadData() {
		MainFrame mainFrame = Singleton.getSingletonInstance().getMainFrame();
		if (mainFrame != null) {
			mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		try {
			SpecimenLifeCycle s = new SpecimenLifeCycle();
			if (totalCount < 0) {
				totalCount = (searchCriteria2 != null)
						? s.countBy(this.searchCriteria2, useLike)
						: s.countBy(Collections.emptyMap(), false);
			}
			List<Specimen> results = s.findSpecimensForTable(
					(this.searchCriteria2 != null) ? this.searchCriteria2 : Collections.emptyMap(), maxResults, offset,
					useLike, sortProperty, sortAscending);
			SpecimenListTableModel model = new SpecimenListTableModel(results);
			model.setSortInfo(currentSortModelCol, sortAscending);
			jTable.setModel(model);
			sorter.setModel(model);
			for (int c = 0; c < model.getColumnCount(); c++) {
				sorter.setSortable(c, false);
			}
			updatePaginationControls(results.size());
			if (jTable.getTableHeader() != null) {
				jTable.getTableHeader().repaint();
			}
			if (mainFrame != null) {
				int totalPages = (int) Math.ceil((double) (totalCount > 0 ? totalCount : 1) / maxResults);
				mainFrame.setStatusMessage("Found " + totalCount + " matching specimens (Page "
						+ ((offset / maxResults) + 1) + " of " + (totalPages > 0 ? totalPages : 1) + ")");
			}
		} finally {
			if (mainFrame != null) {
				mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	private void updatePaginationControls(int resultCount) {
		long count = totalCount >= 0 ? totalCount : resultCount;
		int totalPages = (int) Math.ceil((double) (count > 0 ? count : 1) / maxResults);
		if (totalPages <= 0) {
			totalPages = 1;
		}
		int currentPage = (offset / maxResults) + 1;
		if (jLabelPage != null) {
			if (count == 0) {
				jLabelPage.setText("No matching specimens");
			} else {
				int start = offset + 1;
				int end = Math.min(offset + resultCount, (int) count);
				jLabelPage.setText("Specimens " + start + "-" + end + " of " + count + " (Page " + currentPage + " of "
						+ totalPages + ")");
			}
		}
		if (jTextFieldPage != null) {
			jTextFieldPage.setText(String.valueOf(currentPage));
		}
		if (jButtonFirst != null) {
			jButtonFirst.setEnabled(offset > 0);
		}
		if (jButtonPrev != null) {
			jButtonPrev.setEnabled(offset > 0);
		}
		if (jButtonNext != null) {
			jButtonNext.setEnabled(offset + maxResults < count);
		}
		if (jButtonLast != null) {
			jButtonLast.setEnabled(offset + maxResults < count);
		}
		if (jButtonGo != null) {
			jButtonGo.setEnabled(count > 0);
		}
	}

	public JButton getJButtonFirst() {
		return jButtonFirst;
	}

	public JButton getJButtonPrev() {
		return jButtonPrev;
	}

	public JButton getJButtonNext() {
		return jButtonNext;
	}

	public JButton getJButtonLast() {
		return jButtonLast;
	}

	public JTextField getJTextFieldPage() {
		return jTextFieldPage;
	}

	public JButton getJButtonGo() {
		return jButtonGo;
	}

	public JLabel getJLabelPage() {
		return jLabelPage;
	}

	public int getOffset() {
		return offset;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public String getSortProperty() {
		return sortProperty;
	}

	public boolean isSortAscending() {
		return sortAscending;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridx = 7;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 6;
			gridBagConstraints3.gridy = 0;
			JLabel jLabel3 = new JLabel();
			jLabel3.setText("Drawer:");
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.gridy = 0;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.gridx = 5;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 4;
			gridBagConstraints11.gridy = 0;
			JLabel jLabel2 = new JLabel();
			jLabel2.setText("Family:");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints2.gridy = 0;
			JLabel jLabel1 = new JLabel();
			jLabel1.setText("Workflow:");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridx = 3;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.weightx = 1.0;
			JLabel jLabel = new JLabel();
			jLabel.setText("Find Barcode:");
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(jLabel, new GridBagConstraints());
			jPanel.add(getJTextField(), gridBagConstraints);
			jPanel.add(getJComboBox(), gridBagConstraints1);
			jPanel.add(jLabel1, gridBagConstraints2);
			jPanel.add(jLabel2, gridBagConstraints11);
			jPanel.add(getJTextFieldFamily(), gridBagConstraints21);
			jPanel.add(jLabel3, gridBagConstraints3);
			jPanel.add(getJTextFieldDrawerNumber(), gridBagConstraints4);
		}
		return jPanel;
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.addActionListener(e -> triggerFilterImmediately());
			jTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
				public void insertUpdate(javax.swing.event.DocumentEvent e) {
					triggerFilterDebounced();
				}

				public void removeUpdate(javax.swing.event.DocumentEvent e) {
					triggerFilterDebounced();
				}

				public void changedUpdate(javax.swing.event.DocumentEvent e) {
					triggerFilterDebounced();
				}
			});
		}
		return jTextField;
	}

	/**
	 * This method initializes jComboBox
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox(WorkFlowStatus.getWorkFlowStatusValues());
			jComboBox.addItem("");
			jComboBox.setSelectedItem("");
			jComboBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					triggerFilterImmediately();
				}
			});
		}
		return jComboBox;
	}

	public void triggerFilterImmediately() {
		if (filterDebounceTimer != null && filterDebounceTimer.isRunning()) {
			filterDebounceTimer.stop();
		}
		newFilter();
	}

	public void triggerFilterDebounced() {
		if (filterDebounceTimer == null) {
			filterDebounceTimer = new javax.swing.Timer(350, e -> newFilter());
			filterDebounceTimer.setRepeats(false);
		}
		filterDebounceTimer.restart();
	}

	public void newFilter() {
		Map<String, Object> activeCriteria = new HashMap<>(
				this.baseSearchCriteria != null ? this.baseSearchCriteria : Collections.emptyMap());
		String barcode = jTextField != null ? jTextField.getText().trim() : "";
		if (!barcode.isEmpty()) {
			activeCriteria.put("barcode", barcode);
		}
		String workflow = (jComboBox != null && jComboBox.getSelectedItem() != null)
				? jComboBox.getSelectedItem().toString().trim()
				: "";
		if (!workflow.isEmpty()) {
			activeCriteria.put("workFlowStatus", workflow);
		}
		String family = jTextFieldFamily != null ? jTextFieldFamily.getText().trim() : "";
		if (!family.isEmpty()) {
			activeCriteria.put("family", family);
		}
		String drawer = jTextFieldDrawerNumber != null ? jTextFieldDrawerNumber.getText().trim() : "";
		if (!drawer.isEmpty()) {
			activeCriteria.put("drawerNumber", drawer);
		}

		if (sorter != null) {
			sorter.setRowFilter(null);
		}

		this.searchCriteria2 = activeCriteria;
		this.totalCount = -1;
		this.offset = 0;
		loadData();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see edu.harvard.mcz.imagecapture.interfaces.DataChangeListener#
	 * notifyDataHasChanged()
	 */
	@Override
	public void notifyDataHasChanged() {
		((SpecimenListTableModel) jTable.getModel()).fireTableDataChanged();
		log.debug("Data change notified.");
	}

	/**
	 * This method initializes jTextFieldFamily
	 *
	 * @return javax.swing.JTextField
	 */
	public JTextField getJTextFieldFamily() {
		if (jTextFieldFamily == null) {
			jTextFieldFamily = new JTextField();
			jTextFieldFamily.addActionListener(e -> triggerFilterImmediately());
			jTextFieldFamily.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
				public void insertUpdate(javax.swing.event.DocumentEvent e) {
					triggerFilterDebounced();
				}

				public void removeUpdate(javax.swing.event.DocumentEvent e) {
					triggerFilterDebounced();
				}

				public void changedUpdate(javax.swing.event.DocumentEvent e) {
					triggerFilterDebounced();
				}
			});
		}
		return jTextFieldFamily;
	}

	/**
	 * This method initializes jTextFieldDrawerNumber
	 *
	 * @return javax.swing.JTextField
	 */
	public JTextField getJTextFieldDrawerNumber() {
		if (jTextFieldDrawerNumber == null) {
			jTextFieldDrawerNumber = new JTextField();
			jTextFieldDrawerNumber.addActionListener(e -> triggerFilterImmediately());
			jTextFieldDrawerNumber.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
				public void insertUpdate(javax.swing.event.DocumentEvent e) {
					triggerFilterDebounced();
				}

				public void removeUpdate(javax.swing.event.DocumentEvent e) {
					triggerFilterDebounced();
				}

				public void changedUpdate(javax.swing.event.DocumentEvent e) {
					triggerFilterDebounced();
				}
			});
		}
		return jTextFieldDrawerNumber;
	}

	public JTextField getJTextFieldBarcode() {
		return getJTextField();
	}

	public JComboBox getJComboBoxWorkflow() {
		return getJComboBox();
	}

	public JTextField getJTextFieldDrawer() {
		return getJTextFieldDrawerNumber();
	}

	public Map<String, Object> getSearchCriteria2() {
		return this.searchCriteria2;
	}

	public int getRowCount() {
		int result = 0;
		if (jTable != null) {
			result = jTable.getRowCount();
		}
		return result;
	}

} // @jve:decl-index=0:visual-constraint="10,10"

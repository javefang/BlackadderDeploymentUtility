package blackadderDeploymentUtility.structuredView;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.border.TitledBorder;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

import blackadderDeploymentUtility.data.BAConnection;
import blackadderDeploymentUtility.data.BAConnection.BAConnOptionIP;
import blackadderDeploymentUtility.data.BAConnection.BAConnOptionMAC;
import blackadderDeploymentUtility.data.BAGlobalConf;
import blackadderDeploymentUtility.data.BANode;
import blackadderDeploymentUtility.data.BAGlobalConf.BAGlobalConfOption;
import blackadderDeploymentUtility.data.BANode.BANodeOption;
import blackadderDeploymentUtility.data.BANode.BANodeRole;
import blackadderDeploymentUtility.data.BANetwork;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSeparator;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainUI {
	public static final String PROGRAM_NAME = "PURSUIT - Blackadder Deployment Config Generator b0321";
	public static final String CONFIG_EXT = ".conf";
	private BAGlobalConf baGlobalConf;
	private BANetwork baNet;
	
	private BANode curNode;
	private BAConnection curConn;
	private boolean initialized = false;
	private File curConfigFile;
	
	
	private JFileChooser chooser;
	private JFrame frmPursuitBlackadder;
	private JTextField clickHomeTextField;
	private JTextField writeConfTextField;
	private JTextField userTextField;
	private JList nodeList;
	private JTextField labelField;
	private JTextField testbedIpField;
	private JList connList;
	private JLabel lblSrc;
	private JLabel lblDst;
	private JComboBox toNodeCombo;
	private JTextField srcConnField;
	private JTextField dstConnField;
	private JTextField srcMacField;
	private JTextField dstMacField;
	private JComboBox runningModeCombo;
	private JCheckBox chckbxTm;
	private JCheckBox chckbxRv;
	private JSpinner baIdLenSpinner;
	private JSpinner lpsIdLenSpinner;
	private JComboBox overlayModeCombo;
	private JCheckBox sudoCheckBox;
	private JLabel lblMac;
	private JTextField commentField;
	private JCheckBoxMenuItem chckbxmntmAutoSave;
	private JMenuItem mntmSave;
	private JButton addNodeButton;
	private JButton delNodeButton;
	private JButton btnConnAdd;
	private JButton btnConnDel;
	private JButton btnGenerate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		final BAGlobalConf conf = new BAGlobalConf();
		//network.addNode(new BANode("0001"));
		//network.addNode(new BANode("0002"));
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI window = new MainUI(conf);
					window.frmPursuitBlackadder.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainUI(BAGlobalConf conf) {
		this.baGlobalConf = conf;
		this.baNet = conf.getBaNet();
		
		initialize();
		loadGlobalConf();
		this.setGlobalEditorEnabled(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPursuitBlackadder = new JFrame() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5577448307338211914L;

			@Override
			protected void processWindowEvent(WindowEvent e) {
				if (e.getID() == WindowEvent.WINDOW_CLOSING) {
					// check saving
					closeProgram();
				} else {
					// other window event
					super.processComponentEvent(e);
				}
			}
		};
		frmPursuitBlackadder.setTitle(PROGRAM_NAME);
		frmPursuitBlackadder.setBounds(100, 100, 590, 555);
		frmPursuitBlackadder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(new File(".").getAbsolutePath()));
		
		
		JPanel globalConfPanel = new JPanel();
		globalConfPanel.setBorder(new TitledBorder(null, "Global Config", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmPursuitBlackadder.getContentPane().add(globalConfPanel, BorderLayout.NORTH);
		globalConfPanel.setLayout(new GridLayout(0, 2, 10, 10));
		
		JPanel globalConfLeftPanel = new JPanel();
		globalConfPanel.add(globalConfLeftPanel);
		globalConfLeftPanel.setLayout(new BorderLayout(10, 10));
		
		JPanel gcLeftLabelPanel = new JPanel();
		globalConfLeftPanel.add(gcLeftLabelPanel, BorderLayout.WEST);
		gcLeftLabelPanel.setLayout(new GridLayout(4, 1, 5, 5));
		
		JLabel lblNewLabel = new JLabel("BLACKADDER_ID_LENGTH: ");
		gcLeftLabelPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("LIPSIN_ID_LENGTH: ");
		gcLeftLabelPanel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("OVERLAY_MODE: ");
		gcLeftLabelPanel.add(lblNewLabel_2);
		
		JPanel gcLeftValuePanel = new JPanel();
		globalConfLeftPanel.add(gcLeftValuePanel, BorderLayout.CENTER);
		gcLeftValuePanel.setLayout(new GridLayout(4, 1, 5, 5));
		
		baIdLenSpinner = new JSpinner();
		baIdLenSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				getGlobalConf().setOption(BAGlobalConfOption.BLACKADDER_ID_LENGTH, ((JSpinner)e.getSource()).getValue());
			}
		});
		baIdLenSpinner.setModel(new SpinnerNumberModel(0, 0, 128, 1));
		gcLeftValuePanel.add(baIdLenSpinner);
		
		lpsIdLenSpinner = new JSpinner();
		lpsIdLenSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				getGlobalConf().setOption(BAGlobalConfOption.LIPSIN_ID_LENGTH, ((JSpinner)e.getSource()).getValue());
			}
		});
		lpsIdLenSpinner.setModel(new SpinnerNumberModel(0, 0, 128, 1));
		gcLeftValuePanel.add(lpsIdLenSpinner);
		
		overlayModeCombo = new JComboBox();
		overlayModeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String overlayMode = ((JComboBox)arg0.getSource()).getSelectedItem().toString();
				getGlobalConf().setOption(BAGlobalConfOption.OVERLAY_MODE, overlayMode);
			}
		});
		overlayModeCombo.setModel(new DefaultComboBoxModel(new String[] {"ip", "mac"}));
		gcLeftValuePanel.add(overlayModeCombo);
		
		JPanel globalConfRightPanel = new JPanel();
		globalConfPanel.add(globalConfRightPanel);
		globalConfRightPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel gcRightLabelPanel = new JPanel();
		globalConfRightPanel.add(gcRightLabelPanel, BorderLayout.WEST);
		gcRightLabelPanel.setLayout(new GridLayout(4, 1, 5, 5));
		
		JLabel lblNewLabel_3 = new JLabel("CLICK_HOME: ");
		gcRightLabelPanel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("WRITE_CONF: ");
		gcRightLabelPanel.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("USER: ");
		gcRightLabelPanel.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("SUDO: ");
		gcRightLabelPanel.add(lblNewLabel_6);
		
		JPanel gcRightValuePanel = new JPanel();
		globalConfRightPanel.add(gcRightValuePanel, BorderLayout.CENTER);
		gcRightValuePanel.setLayout(new GridLayout(4, 1, 5, 5));
		
		clickHomeTextField = new JTextField();
		clickHomeTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				getGlobalConf().setOption(BAGlobalConfOption.CLICK_HOME, ((JTextField)e.getSource()).getText());
			}
		});
		gcRightValuePanel.add(clickHomeTextField);
		clickHomeTextField.setColumns(10);
		
		writeConfTextField = new JTextField();
		writeConfTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				getGlobalConf().setOption(BAGlobalConfOption.WRITE_CONF, ((JTextField)e.getSource()).getText());
			}
		});
		gcRightValuePanel.add(writeConfTextField);
		writeConfTextField.setColumns(10);
		
		userTextField = new JTextField();
		userTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				getGlobalConf().setOption(BAGlobalConfOption.USER, ((JTextField)e.getSource()).getText());
			}
		});
		gcRightValuePanel.add(userTextField);
		userTextField.setColumns(10);
		
		sudoCheckBox = new JCheckBox("");
		sudoCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getGlobalConf().setOption(BAGlobalConfOption.SUDO, ((JCheckBox)e.getSource()).isSelected());
			}
		});
		gcRightValuePanel.add(sudoCheckBox);
		
		final List<BANode> nodes = getNetworkConf().getNodeList();
		ListModel nodeData = new AbstractListModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6159465479638788025L;
			@Override
			public Object getElementAt(int arg0) {
				return nodes.get(arg0);
			}
			@Override
			public int getSize() {
				return nodes.size();
			}
		};
		
		JPanel nodeListPanel = new JPanel();
		nodeListPanel.setBorder(new TitledBorder(null, "Node List", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmPursuitBlackadder.getContentPane().add(nodeListPanel, BorderLayout.WEST);
		nodeListPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		nodeListPanel.add(scrollPane, BorderLayout.CENTER);
		nodeList = new JList();
		nodeList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if ((int)e.getKeyChar() == KeyEvent.VK_DELETE) {
					System.out.println("Delete key pressed, try to delete node " + curNode);
					if (curNode != null) {
						int delIndex = nodeList.getSelectedIndex();
						System.out.println("Deleting node " + curNode + " at index " + delIndex);
						baNet.removeNode(curNode);
						nodeList.updateUI();
						if(delIndex >= 0) {
							nodeList.setSelectedIndex(delIndex);
						}
					}
				}
			}
		});
		nodeList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (arg0.getValueIsAdjusting()) {
					return;
				}
				
				curNode = (BANode)((JList)arg0.getSource()).getSelectedValue();
				if (curNode != null) {
					setNodeEditorEnabled(true);
					// basic info
					labelField.setText((String)curNode.getOption(BANodeOption.LABEL));
					commentField.setText(curNode.getComment());
					runningModeCombo.setSelectedItem(curNode.getOption(BANodeOption.RUNNING_MODE));
					testbedIpField.setText((String)curNode.getOption(BANodeOption.TESTBED_IP));
					chckbxTm.setSelected(false);
					chckbxRv.setSelected(false);
					if (curNode.getRoles().size() > 0) {
						Iterator<BANodeRole> iter = curNode.getRoles().iterator();
						while (iter.hasNext()) {
							BANodeRole role = iter.next();
							if (role == BANodeRole.TM) {
								chckbxTm.setSelected(true);
							} else if (role == BANodeRole.RV) {
								chckbxRv.setSelected(true);
							}
						}
					}
					// connection list mode
					final List<BAConnection> conns = curNode.getConnections();
					ListModel connData = new AbstractListModel() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 6455117621580677234L;
						@Override
						public Object getElementAt(int arg0) {
							return conns.get(arg0);
						}
						@Override
						public int getSize() {
							return conns.size();
						}
					};
					connList.setModel(connData);
				} else {
					setNodeEditorEnabled(false);
				}
				
			}
		});
		scrollPane.setViewportView(nodeList);
		nodeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nodeList.setModel(nodeData);
		
		JPanel controlPanel = new JPanel();
		nodeListPanel.add(controlPanel, BorderLayout.SOUTH);
		controlPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel nodeControlPanel = new JPanel();
		controlPanel.add(nodeControlPanel);
		nodeControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		addNodeButton = new JButton("Add");
		addNodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getNetworkConf().addNode(new BANode());
				nodeList.updateUI();
				toNodeCombo.updateUI();
			}
		});
		nodeControlPanel.add(addNodeButton);
		
		delNodeButton = new JButton("Del");
		delNodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (curNode != null) {
					getNetworkConf().removeNode(curNode);
					nodeList.updateUI();
				}
			}
		});
		nodeControlPanel.add(delNodeButton);
		
		btnGenerate = new JButton("Generate!");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					// TODO: remove the .dat extension from the generated .conf file name
					PrintWriter out = new PrintWriter(curConfigFile.getName() + CONFIG_EXT);
					baGlobalConf.writeConfig(out, 0);
					out.flush();
					out.close();
					System.out.println("Conf: \"" + curConfigFile.getName() + CONFIG_EXT + "\" generated!");
					JOptionPane.showMessageDialog((Component)e.getSource(), "Config file \"" + curConfigFile.getName() + CONFIG_EXT + "\" successfully generated!", "PURSUIT", JOptionPane.INFORMATION_MESSAGE);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				/*
				File confFile;
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Blackadder Deployment Config File (.conf)", "conf");
				chooser.setFileFilter(filter);
				int rtnVal = chooser.showSaveDialog((Component)e.getSource());
				if (rtnVal == JFileChooser.APPROVE_OPTION) {
					confFile = chooser.getSelectedFile();
					try {
						PrintWriter out = new PrintWriter(confFile);
						new BAGlobalConf().writeConfig(out, 0);
						getNetworkConf().writeConfig(out, 0);
						out.flush();
						out.close();
						System.out.println("Conf: \"" + confFile.getName() + "\" generated!");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}*/
			}
		});
		controlPanel.add(btnGenerate);
		
		JPanel nodePanel = new JPanel();
		nodePanel.setBorder(new TitledBorder(null, "Node Config", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmPursuitBlackadder.getContentPane().add(nodePanel, BorderLayout.CENTER);
		nodePanel.setLayout(new BorderLayout(0, 0));
		
		JPanel basicNodeInfoPanel = new JPanel();
		nodePanel.add(basicNodeInfoPanel, BorderLayout.NORTH);
		basicNodeInfoPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel basicNodeInfoLabels = new JPanel();
		basicNodeInfoPanel.add(basicNodeInfoLabels, BorderLayout.WEST);
		basicNodeInfoLabels.setLayout(new GridLayout(0, 1, 5, 5));
		
		JLabel lblLabel = new JLabel("LABEL: ");
		basicNodeInfoLabels.add(lblLabel);
		
		JLabel lblComment = new JLabel("Comment: ");
		basicNodeInfoLabels.add(lblComment);
		
		JLabel lblRunningmode = new JLabel("RUNNING_MODE: ");
		basicNodeInfoLabels.add(lblRunningmode);
		
		JLabel lblTestbedip = new JLabel("TESTBED_IP: ");
		basicNodeInfoLabels.add(lblTestbedip);
		
		JLabel lblRole = new JLabel("ROLE: ");
		basicNodeInfoLabels.add(lblRole);
		
		JPanel basicNodeInfoValues = new JPanel();
		basicNodeInfoPanel.add(basicNodeInfoValues, BorderLayout.CENTER);
		basicNodeInfoValues.setLayout(new GridLayout(0, 1, 5, 5));
		
		labelField = new JTextField();
		labelField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (curNode != null) {
					curNode.setOption(BANodeOption.LABEL, ((JTextField)e.getSource()).getText());
					nodeList.updateUI();
				}
			}
		});
		basicNodeInfoValues.add(labelField);
		labelField.setColumns(10);
		
		runningModeCombo = new JComboBox();
		runningModeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (curNode != null) {
					curNode.setOption(BANodeOption.RUNNING_MODE, ((JComboBox)arg0.getSource()).getSelectedItem().toString());
				}
			}
		});
		
		commentField = new JTextField();
		commentField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (curNode != null) {
					curNode.setComment(((JTextField)e.getSource()).getText());
				}
			}
		});
		basicNodeInfoValues.add(commentField);
		commentField.setColumns(10);
		runningModeCombo.setModel(new DefaultComboBoxModel(new String[] {"user", "kernel"}));
		basicNodeInfoValues.add(runningModeCombo);
		
		testbedIpField = new JTextField();
		testbedIpField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (curNode != null) {
					curNode.setOption(BANodeOption.TESTBED_IP, ((JTextField)e.getSource()).getText());
				}
			}
		});
		basicNodeInfoValues.add(testbedIpField);
		testbedIpField.setColumns(10);
		
		JPanel rolePanel = new JPanel();
		basicNodeInfoValues.add(rolePanel);
		rolePanel.setLayout(new GridLayout(0, 2, 5, 0));
		
		chckbxTm = new JCheckBox("TM");
		chckbxTm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (curNode != null) {
					if (((JCheckBox)e.getSource()).isSelected()) {
						curNode.addRole(BANodeRole.TM);
					} else {
						curNode.removeRole(BANodeRole.TM);
					}
				}
			}
		});
		rolePanel.add(chckbxTm);
		
		chckbxRv = new JCheckBox("RV");
		chckbxRv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (curNode != null) {
					if (((JCheckBox)e.getSource()).isSelected()) {
						curNode.addRole(BANodeRole.RV);
					} else {
						curNode.removeRole(BANodeRole.RV);
					}
				}
			}
		});
		rolePanel.add(chckbxRv);
		
		JPanel nodeConnPanel = new JPanel();
		nodeConnPanel.setBorder(new TitledBorder(null, "Connections", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		nodePanel.add(nodeConnPanel, BorderLayout.CENTER);
		nodeConnPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel connInfoPanel = new JPanel();
		nodeConnPanel.add(connInfoPanel, BorderLayout.SOUTH);
		connInfoPanel.setLayout(new GridLayout(0, 2, 5, 5));
		
		JPanel connLeftPanel = new JPanel();
		connInfoPanel.add(connLeftPanel);
		connLeftPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel connLeftLabels = new JPanel();
		connLeftPanel.add(connLeftLabels, BorderLayout.WEST);
		connLeftLabels.setLayout(new GridLayout(3, 1, 5, 5));
		
		JLabel lblTo = new JLabel("TO");
		connLeftLabels.add(lblTo);
		
		lblSrc = new JLabel("SRC_IF");
		connLeftLabels.add(lblSrc);
		
		lblDst = new JLabel("DST_IF");
		connLeftLabels.add(lblDst);
		
		JPanel connLeftValues = new JPanel();
		connLeftPanel.add(connLeftValues);
		connLeftValues.setLayout(new GridLayout(3, 1, 5, 5));
		
		toNodeCombo = new JComboBox();
		toNodeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (curConn != null) {
					curConn.setTo((BANode)((JComboBox)e.getSource()).getSelectedItem());
					connList.updateUI();
				}
			}
		});
		connLeftValues.add(toNodeCombo);
		ComboBoxModel connComboModel = new ComboBoxModel() {
			private Vector<ListDataListener> listeners = new Vector<ListDataListener>();
			private Object selectedNode;
			
			@Override
			public void addListDataListener(ListDataListener arg0) {
				listeners.add(arg0);
			}

			@Override
			public Object getElementAt(int arg0) {
				return nodes.get(arg0);
			}

			@Override
			public int getSize() {
				return nodes.size();
			}

			@Override
			public void removeListDataListener(ListDataListener arg0) {
				listeners.remove(arg0);
			}

			@Override
			public Object getSelectedItem() {
				return selectedNode;
			}

			@Override
			public void setSelectedItem(Object arg0) {
				selectedNode = arg0;
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).contentsChanged(
							new ListDataEvent(toNodeCombo, 
												ListDataEvent.CONTENTS_CHANGED, 
												nodes.indexOf(arg0), 
												nodes.indexOf(arg0)));
					
				}
			}
			
		};
		toNodeCombo.setModel(connComboModel);
		
		srcConnField = new JTextField();
		srcConnField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (curConn != null) {
					if (getOverlayMode().equals("ip")) {
						curConn.setOptionIP(BAConnOptionIP.SRC_IP, ((JTextField)e.getSource()).getText());
					} else if (getOverlayMode().equals("mac")) {
						curConn.setOptionMAC(BAConnOptionMAC.SRC_IF, ((JTextField)e.getSource()).getText());
					} else {
						throw new IllegalStateException("Invalid overlay mode! " + getOverlayMode());
					}
					connList.updateUI();
				}	
			}
		});
		connLeftValues.add(srcConnField);
		srcConnField.setColumns(10);
		
		dstConnField = new JTextField();
		dstConnField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (curConn != null) {
					if (getOverlayMode().equals("ip")) {
						curConn.setOptionIP(BAConnOptionIP.DST_IP, ((JTextField)e.getSource()).getText());
					} else if (getOverlayMode().equals("mac")) {
						curConn.setOptionMAC(BAConnOptionMAC.DST_IF, ((JTextField)e.getSource()).getText());
						//currentConn.setOptionMAC(BAConnOptionMAC.DST_MAC, dstMacField.getText());
					} else {
						throw new IllegalStateException("Invalid overlay mode! " + getOverlayMode());
					}
					connList.updateUI();
				}
			}
		});
		connLeftValues.add(dstConnField);
		dstConnField.setColumns(10);
		
		JPanel connRightPanel = new JPanel();
		connInfoPanel.add(connRightPanel);
		connRightPanel.setLayout(new GridLayout(3, 1, 5, 5));
		
		lblMac = new JLabel("MAC (Optional)");
		connRightPanel.add(lblMac);
		
		srcMacField = new JTextField();
		srcMacField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (curConn != null) {
					curConn.setOptionMAC(BAConnOptionMAC.SRC_MAC, ((JTextField)e.getSource()).getText());
					connList.updateUI();
				}
			}
		});
		connRightPanel.add(srcMacField);
		srcMacField.setColumns(10);
		
		dstMacField = new JTextField();
		dstMacField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (curConn != null) {
					curConn.setOptionMAC(BAConnOptionMAC.DST_MAC, ((JTextField)e.getSource()).getText());
					connList.updateUI();
				}	
			}
		});
		connRightPanel.add(dstMacField);
		dstMacField.setColumns(10);
		
		JScrollPane connScrollPane = new JScrollPane();
		connList = new JList();
		connList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (arg0.getValueIsAdjusting()) {
					return;
				}
				
				curConn = (BAConnection)((JList)arg0.getSource()).getSelectedValue();
				if (curConn != null) {
					setConnectionEditorEnabled(true);
					toNodeCombo.setSelectedItem(curConn.getTo());
					if (getOverlayMode().equals("ip")) {
						System.out.println("Loading connection info (ip)");
						srcConnField.setText((String)curConn.getOptionIP(BAConnOptionIP.SRC_IP));
						dstConnField.setText((String)curConn.getOptionIP(BAConnOptionIP.DST_IP));
						srcMacField.setText("");
						dstMacField.setText("");
						setMacEditorEnabled(false);
					} else if (getOverlayMode().equals("mac")) {
						System.out.println("Loading connection info (mac) " + curConn.hashCode());
						System.out.println("SRC_IF = " + (String)curConn.getOptionMAC(BAConnOptionMAC.SRC_IF));
						srcConnField.setText((String)curConn.getOptionMAC(BAConnOptionMAC.SRC_IF));
						dstConnField.setText((String)curConn.getOptionMAC(BAConnOptionMAC.DST_IF));
						srcMacField.setText((String)curConn.getOptionMAC(BAConnOptionMAC.SRC_MAC));
						dstMacField.setText((String)curConn.getOptionMAC(BAConnOptionMAC.DST_MAC));
						setMacEditorEnabled(true);
					}
				} else {
					setConnectionEditorEnabled(false);
				}
			}
		});
		connScrollPane.setViewportView(connList);
		nodeConnPanel.add(connScrollPane, BorderLayout.CENTER);
		connList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel connEditPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) connEditPanel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		flowLayout.setAlignment(FlowLayout.RIGHT);
		nodeConnPanel.add(connEditPanel, BorderLayout.NORTH);
		
		btnConnAdd = new JButton("Add");
		btnConnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (curNode != null) {
					curNode.addConnection(new BAConnection());
					connList.updateUI();
				}
			}
		});
		connEditPanel.add(btnConnAdd);
		
		btnConnDel = new JButton("Del");
		btnConnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (curNode != null && curConn != null) {
					curNode.removeConnection(curConn);
					connList.updateUI();
				}
			}
		});
		connEditPanel.add(btnConnDel);
		
		JMenuBar menuBar = new JMenuBar();
		frmPursuitBlackadder.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLoad = new JMenuItem("Load...");
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					// TODO: add a file chooser here
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Blackadder Deployment Config Data (.dat)", "dat");
					chooser.setFileFilter(filter);
					int rtnVal = chooser.showOpenDialog((Component)arg0.getSource());
					if (rtnVal == JFileChooser.APPROVE_OPTION) {
						try {
							readConfigObjects(chooser.getSelectedFile());
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					//JOptionPane.showMessageDialog((Component)arg0.getSource(), "Config data loaded successfully", "PURSUIT", JOptionPane.INFORMATION_MESSAGE);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newConfig = JOptionPane.showInputDialog("Please enter the name of the new config");
				if (newConfig != null) {
					newConfig = newConfig.trim();
					if (!newConfig.isEmpty()) {
						curConfigFile = new File(newConfig + ".dat");
						System.out.println("New config file = " + newConfig);
						fireConfigFileChanged();
						return;
					} else {
						JOptionPane.showMessageDialog(frmPursuitBlackadder, "Config name cannot be empty!", "Invalid config name", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		mnFile.add(mntmNew);
		mnFile.add(mntmLoad);
		
		mntmSave = new JMenuItem("Save");
		mntmSave.setEnabled(false);
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					// TODO: add another save as button
					saveConfigObjects(curConfigFile);
					JOptionPane.showMessageDialog((Component)arg0.getSource(), "Config data saved successfully", "PURSUIT", JOptionPane.INFORMATION_MESSAGE);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog((Component)arg0.getSource(), "Cannot write config files, do you have permission to write?", "PURSUIT", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog((Component)arg0.getSource(), "Critical error while writing config files!", "PURSUIT", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		});
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save as...");
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Blackadder Deployment Config Data (.dat)", "dat");
				chooser.setFileFilter(filter);
				int rtnVal = chooser.showSaveDialog((Component)arg0.getSource());
				if (rtnVal == JFileChooser.APPROVE_OPTION) {
					try {
						curConfigFile = chooser.getSelectedFile();
						saveConfigObjects(curConfigFile);
						fireConfigFileChanged();
						
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		mnFile.add(mntmSaveAs);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				closeProgram();
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnOption = new JMenu("Option");
		menuBar.add(mnOption);
		
		chckbxmntmAutoSave = new JCheckBoxMenuItem("Auto save");
		chckbxmntmAutoSave.setEnabled(false);
		mnOption.add(chckbxmntmAutoSave);
	}

	protected JList getNodeList() {
		return nodeList;
	}
	protected JList getConnList() {
		return connList;
	}
	protected JLabel getSrc() {
		return lblSrc;
	}
	protected JLabel getLblDst() {
		return lblDst;
	}
	protected JComboBox getToNodeCombo() {
		return toNodeCombo;
	}
	protected JTextField getLabelField() {
		return labelField;
	}
	protected JComboBox getRunningModeCombo() {
		return runningModeCombo;
	}
	public JTextField getTestbedIpField() {
		return testbedIpField;
	}
	public JCheckBox getChckbxTm() {
		return chckbxTm;
	}
	public JCheckBox getChckbxRv() {
		return chckbxRv;
	}
	
	/* read the overlay mode from the global conf */
	private String getOverlayMode() {
		return (String)getGlobalConf().getOption(BAGlobalConfOption.OVERLAY_MODE);
	}
	
	/* load global config at initial start */
	private void loadGlobalConf() {
		this.baIdLenSpinner.setValue(getGlobalConf().getOption(BAGlobalConfOption.BLACKADDER_ID_LENGTH));
		this.lpsIdLenSpinner.setValue(getGlobalConf().getOption(BAGlobalConfOption.LIPSIN_ID_LENGTH));
		this.overlayModeCombo.setSelectedItem(getGlobalConf().getOption(BAGlobalConfOption.OVERLAY_MODE));
		this.clickHomeTextField.setText((String)getGlobalConf().getOption(BAGlobalConfOption.CLICK_HOME));
		this.writeConfTextField.setText((String)getGlobalConf().getOption(BAGlobalConfOption.WRITE_CONF));
		this.userTextField.setText((String)getGlobalConf().getOption(BAGlobalConfOption.USER));
		this.sudoCheckBox.setSelected((Boolean)getGlobalConf().getOption(BAGlobalConfOption.SUDO));
	}
	
	
	protected JSpinner getBaIdLenSpinner() {
		return baIdLenSpinner;
	}
	protected JSpinner getLpsIdLenSpinner() {
		return lpsIdLenSpinner;
	}
	protected JComboBox getOverlayModeCombo() {
		return overlayModeCombo;
	}
	protected JTextField getClickHomeTextField() {
		return clickHomeTextField;
	}
	protected JTextField getWriteConfTextField() {
		return writeConfTextField;
	}
	protected JTextField getUserTextField() {
		return userTextField;
	}
	protected JCheckBox getSudoCheckBox() {
		return sudoCheckBox;
	}
	
	/* enable/disable all fields for connection editing */
	private void setConnectionEditorEnabled(boolean enabled) {
		this.toNodeCombo.setEnabled(enabled);
		this.srcConnField.setEnabled(enabled);
		this.dstConnField.setEnabled(enabled);
		this.srcMacField.setEnabled(enabled);
		this.dstMacField.setEnabled(enabled);
		
		
		if (!enabled) {
			setMacEditorEnabled(false);
		}
	}
	
	private void setNodeEditorEnabled(boolean enabled) {
		this.labelField.setEnabled(enabled);
		this.runningModeCombo.setEnabled(enabled);
		this.testbedIpField.setEnabled(enabled);
		this.chckbxRv.setEnabled(enabled);
		this.chckbxTm.setEnabled(enabled);
		this.btnConnAdd.setEnabled(enabled);
		this.btnConnDel.setEnabled(enabled);
		if (!enabled) {
			setConnectionEditorEnabled(false);
		}
	}
	
	private void setMacEditorEnabled(boolean enabled) {
		this.srcMacField.setEnabled(enabled);
		this.dstMacField.setEnabled(enabled);
		this.lblMac.setEnabled(enabled);
		
		// change labels
		if (enabled) {
			this.lblSrc.setText("SRC_IF");
			this.lblDst.setText("DST_IF");
			this.lblMac.setText("MAC (Optional)");
		} else {
			this.lblSrc.setText("SRC_IP");
			this.lblDst.setText("DST_IP");
			this.lblMac.setText("MAC (N/A for IP mode)");
			
		}
	}
	
	private void setGlobalEditorEnabled(boolean enabled) {
		this.baIdLenSpinner.setEnabled(enabled);
		this.lpsIdLenSpinner.setEnabled(enabled);
		this.overlayModeCombo.setEnabled(enabled);
		this.clickHomeTextField.setEnabled(enabled);
		this.writeConfTextField.setEnabled(enabled);
		this.userTextField.setEnabled(enabled);
		this.sudoCheckBox.setEnabled(enabled);
		this.addNodeButton.setEnabled(enabled);
		this.delNodeButton.setEnabled(enabled);
		this.btnGenerate.setEnabled(enabled);
		
		if (!enabled) {
			this.setNodeEditorEnabled(false);
		}
		
	}
	
	protected JLabel getLblMac() {
		return lblMac;
	}
	
	private void saveConfigObjects(File config) throws FileNotFoundException, IOException {
		if (!config.exists()) {
			config.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(config);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(baGlobalConf);
		out.flush();
		out.close();
		System.out.println("Config file \"" + config.getName() + "\" serialized (saved)!");
	}
	
	private void readConfigObjects(File config) throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream fis;
		ObjectInputStream in;
		
		fis = new FileInputStream(config);
		in = new ObjectInputStream(fis);
		this.baGlobalConf = (BAGlobalConf)in.readObject();
		in.close();
		
		this.curConfigFile = config;
		fireConfigFileChanged();
		System.out.println("Config file \"" + config.getName() + "\" unserialized (loaded)!");
	}
	public JCheckBoxMenuItem getChckbxmntmAutoSave() {
		return chckbxmntmAutoSave;
	}
	
	public boolean isAutoSave() {
		return chckbxmntmAutoSave.isSelected();
	}
	
	private BANetwork getNetworkConf() {
		return baNet;
	}
	
	private BAGlobalConf getGlobalConf() {
		return baGlobalConf;
	}
	
	private void fireConfigFileChanged() {
		// enable autosave only after loaded
		this.baNet = this.baGlobalConf.getBaNet();
		frmPursuitBlackadder.setTitle(PROGRAM_NAME + "(" + curConfigFile.getName() + ")");
		chckbxmntmAutoSave.setEnabled(true);
		chckbxmntmAutoSave.setSelected(true);
		mntmSave.setEnabled(true);
		setGlobalEditorEnabled(true);
		initialized = true;
		
		loadGlobalConf();
		final List<BANode> nodes = getNetworkConf().getNodeList();
		ListModel nodeData = new AbstractListModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6159465479638788025L;
			@Override
			public Object getElementAt(int arg0) {
				return nodes.get(arg0);
			}
			@Override
			public int getSize() {
				return nodes.size();
			}
		};
		nodeList.setModel(nodeData);
		// select the first node if there are any
		if (nodeData.getSize() > 0) {
			nodeList.setSelectedIndex(0);
		}
		
		ComboBoxModel connComboModel = new ComboBoxModel() {
			private Vector<ListDataListener> listeners = new Vector<ListDataListener>();
			private Object selectedNode;
			
			@Override
			public void addListDataListener(ListDataListener arg0) {
				listeners.add(arg0);
			}

			@Override
			public Object getElementAt(int arg0) {
				return nodes.get(arg0);
			}

			@Override
			public int getSize() {
				return nodes.size();
			}

			@Override
			public void removeListDataListener(ListDataListener arg0) {
				listeners.remove(arg0);
			}

			@Override
			public Object getSelectedItem() {
				return selectedNode;
			}

			@Override
			public void setSelectedItem(Object arg0) {
				selectedNode = arg0;
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).contentsChanged(
							new ListDataEvent(toNodeCombo, 
												ListDataEvent.CONTENTS_CHANGED, 
												nodes.indexOf(arg0), 
												nodes.indexOf(arg0)));
					
				}
			}
			
		};
		
		this.toNodeCombo.setModel(connComboModel);
	}
	protected JMenuItem getMntmSave() {
		return mntmSave;
	}
	protected JButton getAddNodeButton() {
		return addNodeButton;
	}
	protected JButton getDelNodeButton() {
		return delNodeButton;
	}
	protected JButton getBtnConnAdd() {
		return btnConnAdd;
	}
	protected JButton getBtnConnDel() {
		return btnConnDel;
	}
	protected JButton getBtnGenerate() {
		return btnGenerate;
	}
	
	private void closeProgram() {
		if (!initialized) {
			System.exit(0);
		}
		boolean save = isAutoSave();
		boolean quit = false;
		if (!isAutoSave()) {
			int rtnVal = JOptionPane.showConfirmDialog(null, "Config not saved, unsaved changes will be lost! Would you like to save now?", "Save Config", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			switch (rtnVal) {
			case JOptionPane.YES_OPTION:
				save = true;
				quit = true;
				break;
			case JOptionPane.NO_OPTION:
				System.out.println("Global_conf and net_conf unsaved, remain the state of last change!");
				save = false;
				quit = true;
				break;
			case JOptionPane.CANCEL_OPTION:
				save = false;
				quit = false;
				break;
			}
		} else {
			save = true;
			quit = true;
		}
		
		if (save) {
			try {
				saveConfigObjects(curConfigFile);
			} catch (FileNotFoundException fileNotFoundE) {
				// TODO Auto-generated catch block
				fileNotFoundE.printStackTrace();
			} catch (IOException ioE) {
				// TODO Auto-generated catch block
				ioE.printStackTrace();
			}
		}
		
		if (quit) {
			System.out.println("Attempting to close the window");
			System.exit(0);
		}
	}
}

package server_gui;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;

import utility.ServerInfo;
import utility.infoProvider.ServerConnectionInfo;
import utility.infoProvider.ServerInfoRecover;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Connection extends JFrame {

	private JPanel contentPane;
	private JTextField textField_port;
	private JTextField textField_address;
	private JTextField textField_name;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Connection frame = new Connection("", System.out);
				frame.setTitle("Connection Manager");
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	final private String myName;
	private JButton btnUpdate;
	private JPanel ServerList_panel;
	final private OutputStream executor;
	
	public Connection(String myName, OutputStream executor) {
		Objects.requireNonNull(this.myName = myName);
		this.executor = Objects.requireNonNull(executor);

		setMinimumSize(new Dimension(390, 340));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 444, 446);
		contentPane = new JPanel();
		contentPane.setMinimumSize(new Dimension(340, 10));
		contentPane.setBorder(null);
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2, true), "Server Info:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2, true), "Server List:", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		
		JButton btnNewButton_1 = new JButton("CONNECT");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String name, address, port;
				
				name = textField_name.getText();
				address = textField_address.getText();
				port = textField_port.getText();
				JFrame frame = (JFrame) SwingUtilities.windowForComponent(e.getComponent());
				try {
					ServerConnectionInfo.validateServerName(name);
					ServerConnectionInfo.validateIPAddress(address);
					ServerConnectionInfo.validatePort(port);
					
					executor.write("add broker\n".getBytes());
					executor.flush();
					executor.write("N\n".getBytes());
					executor.flush();
					executor.write((name+"\n").getBytes());
					executor.flush();
					executor.write((address+"\n").getBytes());
					executor.flush();
					executor.write((port+"\n").getBytes());
					executor.flush();

					frame.setVisible(false);
					frame.dispose();
				}catch(IllegalArgumentException e1) {
					JOptionPane.showMessageDialog(frame,
						    e1.getMessage(),
						    "Inane error",
						    JOptionPane.ERROR_MESSAGE);
				}catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(12)
					.addComponent(btnNewButton_1, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 378, Short.MAX_VALUE))
					.addGap(4))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGap(22)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		btnUpdate = new JButton("UPDATE");
		btnUpdate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				updateList();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(217, Short.MAX_VALUE)
					.addComponent(btnUpdate)
					.addContainerGap())
				.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(btnUpdate)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))
		);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		scrollPane.setViewportView(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		ServerList_panel = new JPanel();
		ServerList_panel.setBackground(Color.WHITE);
		panel_2.add(ServerList_panel, BorderLayout.NORTH);
		ServerList_panel.setLayout(new GridLayout(0, 1, 0, 0));
		panel_1.setLayout(gl_panel_1);
		
		JLabel lblServerIp = new JLabel("Registry Addres:");
		
		JLabel lblServerPort = new JLabel("Registry Port:");
		
		textField_address = new JTextField();
		textField_address.setColumns(10);
		
		textField_port = new JTextField();
		textField_port.setColumns(10);
		
		JLabel lblServerName = new JLabel("Server Name:");
		
		textField_name = new JTextField();
		textField_name.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(6)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblServerName, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblServerPort, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblServerIp))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(textField_name, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(textField_address, GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
								.addComponent(textField_port, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(0))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(8)
							.addComponent(lblServerName))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(6)
							.addComponent(textField_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblServerIp)
						.addComponent(textField_address, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblServerPort)
						.addComponent(textField_port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}
	
	private void updateList() {
		ServerSearchWorker worker = new ServerSearchWorker();
		worker.execute();
	}
	
	private class ServerInfoPanel extends JPanel {

		/**
		 * Create the panel.
		 */
		final private String name;
		final private ServerInfo info;
		private JLabel lblServername;
		
		
		ServerInfoPanel(String name, ServerInfo info) {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						textField_address.setText(info.getRegHost());
						textField_port.setText(String.valueOf(info.getRegPort()));
						textField_name.setText(name);
						return;
					}
					
					java.awt.Component[] components = ServerList_panel.getComponents();
					for(java.awt.Component c: components) {
						if(c instanceof ServerInfoPanel) {
							ServerInfoPanel pnl = (ServerInfoPanel)c;
							pnl.setBackground(Color.WHITE);
							pnl.lblServername.setBackground(Color.BLACK);
						}							
					}
					lblServername.setBackground(Color.white);
					e.getComponent().setBackground(Color.CYAN);
					
				}
			});
			setPreferredSize(new Dimension(10, 40));
			setBorder(new CompoundBorder(new EmptyBorder(1, 1, 1, 1), new LineBorder(new Color(0, 0, 0), 1, true)));
			setBackground(Color.WHITE);
			setMinimumSize(new Dimension(10, 40));
			setMaximumSize(new Dimension(32767, 40));
			setLayout(new BorderLayout(0, 0));

			this.name = name;
			this.info = info;
			
			lblServername = new JLabel(this.name);
			add(lblServername);
		}


		public ServerInfo getInfo() {
			return info;
		}	
	}
	
	private class ServerSearchWorker extends SwingWorker<HashMap<String, String[]>, Void>{
		
		@Override
		protected HashMap<String, String[]> doInBackground() {
			btnUpdate.setEnabled(false);
			btnUpdate.setText("Searching");
			
			//RIMOZIONE DEI VECCHI SERVER
	        ServerList_panel.removeAll();
	        ServerList_panel.revalidate();
	        ServerList_panel.repaint();		

	        //RICERCA DEI SERVER DISPONIBILI NELLA RETE LOCALE
	        try {
	        	ServerInfoRecover infoServer = new ServerInfoRecover(false);
	        	return infoServer.findAllServerOnLan();
	        } catch (IOException e1) {
	            return null;
	        }
		}
		
		@Override
		protected void done() {
			btnUpdate.setText("Update");
			btnUpdate.setEnabled(true);		
	        
	        //VISUALIZZAZIONE DEI SERVER TROVATI
	        try {
				HashMap<String, String[]> servers = get();
				if(servers==null)
					return;
				String[] serverFound = servers.keySet().toArray(new String[0]);
		        for(String server: serverFound) {
		        	if(server.equals(myName))
		        		continue;
		        	String[] curServer = servers.get(server);
		        	ServerList_panel.add(new ServerInfoPanel(server, new ServerInfo(curServer[0], Integer.parseInt(curServer[1]))));
		        }
			} catch (InterruptedException | ExecutionException ignored) {
			}
		}
		
		
	}
}

package server_gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;

import com.sun.xml.internal.ws.api.Component;

import utility.ServerInfo;
import utility.infoProvider.ServerInfoRecover;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.awt.GridLayout;
import java.awt.TextField;

public class Connection extends JFrame {

	private JPanel contentPane;
	private JPanel ServerList_panel;
	private JTextField textField_port;
	private JTextField textField_address;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Connection frame = new Connection();
					frame.setTitle("Connection Manager");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Connection() {
		setMinimumSize(new Dimension(340, 300));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 341, 492);
		contentPane = new JPanel();
		contentPane.setMinimumSize(new Dimension(340, 10));
		contentPane.setBorder(null);
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2, true), "Server Info:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2, true), "Server List:", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		
		JButton btnNewButton_1 = new JButton("CONNECT");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 329, Short.MAX_VALUE))
					.addGap(4))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(12)
					.addComponent(btnNewButton_1, GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(22)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JButton btnNewButton = new JButton("UPDATE");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ServerInfoRecover infoServer;
		        HashMap<String, String[]> servers;

		        //RICERCA DEI SERVER DISPONIBILI NELLA RETE LOCALE
		        try {
		            infoServer = new ServerInfoRecover(false);
		        } catch (IOException e1) {
		            return;
		        }
		        servers = infoServer.findAllServerOnLan();

		        //RIMOZIONE DEI VECCHI SERVER
		        ServerList_panel.removeAll();
		        ServerList_panel.revalidate();
		        ServerList_panel.repaint();
		        
		        //VISUALIZAZIONE DEI SERVER TROVATI
		        String[] serverFound = servers.keySet().toArray(new String[0]);
		        for(String server: serverFound) {
		        	String[] curServer = servers.get(server);
		        	ServerList_panel.add(new ServerInfoPanel(server, new ServerInfo(curServer[2], Integer.parseInt(curServer[1]))));
		        }		        
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(217, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addContainerGap())
				.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))
		);
		
		ServerList_panel = new JPanel();
		ServerList_panel.setBackground(Color.WHITE);
		scrollPane.setViewportView(ServerList_panel);
		ServerList_panel.setLayout(new GridLayout(0, 1, 5, 0));
		panel_1.setLayout(gl_panel_1);
		
		JLabel lblServerIp = new JLabel("Server Addres:");
		
		JLabel lblServerPort = new JLabel("Server port:");
		
		textField_address = new JTextField();
		textField_address.setColumns(10);
		
		textField_port = new JTextField();
		textField_port.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(6)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblServerIp, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField_address, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addComponent(lblServerPort, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
							.addGap(25)
							.addComponent(textField_port, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)))
					.addGap(0))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(21)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblServerIp)
						.addComponent(textField_address, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblServerPort)
						.addComponent(textField_port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(31))
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}
	
	private class ServerInfoPanel extends JPanel {

		/**
		 * Create the panel.
		 */
		final private String name;
		final private ServerInfo info;
		private JLabel lblServername;
		
		
		public ServerInfoPanel(String name, ServerInfo info) {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						textField_address.setText(info.getRegHost());
						textField_port.setText(String.valueOf(info.getRegPort()));
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
			setPreferredSize(new Dimension(10, 30));
			setBorder(new CompoundBorder(new EmptyBorder(1, 1, 1, 1), new LineBorder(new Color(0, 0, 0), 1, true)));
			setBackground(Color.WHITE);
			setMinimumSize(new Dimension(10, 30));
			setMaximumSize(new Dimension(32767, 30));

			this.name = name;
			this.info = info;
			setLayout(new GridLayout(0, 2, 0, 0));
			
			lblServername = new JLabel(this.name);
			add(lblServername);
		}


		public ServerInfo getInfo() {
			return info;
		}	
	}
}

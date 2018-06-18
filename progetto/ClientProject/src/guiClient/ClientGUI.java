package guiClient;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gui.ServerGUI;
import keeptoo.KGradientPanel;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.Font;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.MatteBorder;

public class ClientGUI extends JFrame {

	private JPanel contentPane;
	private static final Color BLACKBLUE = new Color(23, 35, 51);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI frame = new ClientGUI();
					frame.setVisible(true);
					Dimension objDimension = Toolkit.getDefaultToolkit().getScreenSize();
			        int iCoordX = (objDimension.width - frame.getWidth()) / 2;
			        int iCoordY = (objDimension.height - frame.getHeight()) / 2;
			        frame.setLocation(iCoordX, iCoordY); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientGUI() {
		
		initComponents();	
	}
	
	private void initComponents() {

        pnl_bg = new javax.swing.JPanel();
        pnl_rg = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        login = new javax.swing.JPanel();
        login.setForeground(Color.BLACK);
        jLabel6 = new javax.swing.JLabel();
        jLabel6.setBounds(69, 50, 101, 37);
        jLabel6.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/dot.png")));
        loader = new javax.swing.JPanel();
        registration = new javax.swing.JPanel();
        img_loader = new javax.swing.JLabel();
        img_loader.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/logogif.gif")));
        

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setUndecorated(true);

        pnl_bg.setBackground(new java.awt.Color(23, 35, 51));
        pnl_rg.setBackground(new java.awt.Color(23, 35, 51));

        jPanel1.setBackground(new java.awt.Color(23, 35, 51));
        jPanel1.setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(23, 35, 51));
        login.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                loginMouseDragged(evt);
            }
        });
        login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                loginMousePressed(evt);
            }
        });

        jLabel6.setFont(new Font("Tahoma", Font.PLAIN, 30)); // NOI18N
        jLabel6.setForeground(Color.WHITE);
        jLabel6.setText("Login");
        
        jPanel1.add(login, "card2");
        login.setLayout(null);
        login.add(jLabel6);
        
        JLabel lblNewLabel_3 = new JLabel("");
        lblNewLabel_3.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/close1.png")));
        lblNewLabel_3.setBounds(769, 6, 25, 25);
        login.add(lblNewLabel_3);
        lblNewLabel_3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_closeMousePressed(evt);
            };
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lblNewLabel_3.setIcon(new ImageIcon(getClass().getResource("/risorse/close2.png")));
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		lblNewLabel_3.setIcon(new ImageIcon(getClass().getResource("/risorse/close1.png")));
        	}
        });
        
        JLabel lblNewLabel_4 = new JLabel("");
        lblNewLabel_4.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/minimize1.png")));
        lblNewLabel_4.setBounds(745, 6, 25, 25);
        login.add(lblNewLabel_4);
        lblNewLabel_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ClientGUI.this.setState(Frame.ICONIFIED);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblNewLabel_4.setIcon(new ImageIcon(getClass().getResource("/risorse/minimize2.png")));
				
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				lblNewLabel_4.setIcon(new ImageIcon(getClass().getResource("/risorse/minimize1.png")));
			}
		});
        
        JLabel lblNewLabel_6 = new JLabel("Sign up");
        lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel_6.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/register_user.png")));
        lblNewLabel_6.setForeground(Color.WHITE);
        lblNewLabel_6.setBounds(69, 516, 85, 25);
        login.add(lblNewLabel_6);
        lblNewLabel_6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
            		login.hide();
            		loader.hide();
            		registration.show();
            }
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lblNewLabel_6.setForeground(Color.CYAN);
        		lblNewLabel_6.setIcon(new ImageIcon(getClass().getResource("/risorse/register_user2.png")));
        	}
        @Override
        	public void mouseExited(MouseEvent e) {
        		lblNewLabel_6.setForeground(Color.WHITE);
        		lblNewLabel_6.setIcon(new ImageIcon(getClass().getResource("/risorse/register_user.png")));
        	}
        });
        
        JLabel lblNewLabel_7 = new JLabel("");
        lblNewLabel_7.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/logo1.png")));
        lblNewLabel_7.setBounds(716, 516, 61, 64);
        login.add(lblNewLabel_7);
        
        JPanel panel = new JPanel();
        //panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(64, 64, 64)));
        panel.setBackground(BLACKBLUE);
        panel.setBounds(207, 113, 369, 390);
        login.add(panel);
        panel.setLayout(null);
        btn_login = new javax.swing.JLabel();
        btn_login.setBounds(301, 335, 32, 32);
        panel.add(btn_login);
        
        btn_login.setIcon(new ImageIcon(getClass().getResource("/risorse/login.png")));       
        
        JLabel lblNewLabel_5 = new JLabel("Forgot password?");
        lblNewLabel_5.setBounds(56, 335, 117, 16);
        panel.add(lblNewLabel_5);
        lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel_5.setForeground(Color.WHITE);
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator1.setBounds(56, 321, 277, 12);
        panel.add(jSeparator1);
        
                jSeparator1.setBackground(Color.CYAN);
                jSeparator1.setForeground(Color.CYAN);
                
                textField = new JTextField();
                textField.setBounds(56, 287, 277, 36);
                panel.add(textField);
                textField.setFont(new Font("Tahoma", Font.PLAIN, 13));
                textField.setText("********");
                textField.setHorizontalAlignment(SwingConstants.LEFT);
                textField.setForeground(Color.BLACK);
                textField.setBackground(Color.WHITE);
                jLabel3=new javax.swing.JLabel();
                jLabel3.setBounds(19, 286, 25, 37);
                panel.add(jLabel3);
                jLabel3.setIcon(new ImageIcon(getClass().getResource("/risorse/lock.png")));
                
                lblPassword = new JLabel("Password");
                lblPassword.setBounds(56, 270, 61, 16);
                panel.add(lblPassword);
                lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
                lblPassword.setForeground(Color.WHITE);
                
                separator_3 = new JSeparator();
                separator_3.setBounds(56, 246, 277, 12);
                panel.add(separator_3);
                separator_3.setForeground(Color.CYAN);
                separator_3.setBackground(Color.CYAN);
                
        txtEmail = new JTextField();
        txtEmail.setBounds(56, 212, 277, 36);
        panel.add(txtEmail);
        txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtEmail.setText("Email or username");
        txtEmail.setHorizontalAlignment(SwingConstants.LEFT);
        txtEmail.setForeground(Color.BLACK);
        txtEmail.setBackground(Color.WHITE);
        jLabel2= new javax.swing.JLabel();
        jLabel2.setBounds(19, 212, 25, 36);
        panel.add(jLabel2);
        
        jLabel2.setIcon(new ImageIcon(getClass().getResource("/risorse/user.png")));
        
        lblEmail_1 = new JLabel("Email or username");
        lblEmail_1.setBounds(56, 195, 117, 16);
        panel.add(lblEmail_1);
        lblEmail_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblEmail_1.setForeground(Color.WHITE);
        
        JSeparator separator_6 = new JSeparator();
        separator_6.setForeground(Color.CYAN);
        separator_6.setBackground(Color.CYAN);
        separator_6.setBounds(56, 160, 277, 12);
        panel.add(separator_6);
        
        txtServerPortAddress = new JTextField();
        txtServerPortAddress.setText("Server port");
        txtServerPortAddress.setHorizontalAlignment(SwingConstants.LEFT);
        txtServerPortAddress.setForeground(Color.BLACK);
        txtServerPortAddress.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtServerPortAddress.setBackground(Color.WHITE);
        txtServerPortAddress.setBounds(56, 124, 277, 36);
        panel.add(txtServerPortAddress);
        
        JLabel lblServerPortAddress = new JLabel("Server port");
        lblServerPortAddress.setForeground(Color.WHITE);
        lblServerPortAddress.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblServerPortAddress.setBounds(56, 108, 70, 16);
        panel.add(lblServerPortAddress);
        lblServerPortAddress.hide();
        
        txtServerPortAddress.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		if(txtServerPortAddress.getText().equals("Server port"))
        			txtServerPortAddress.setText(null);
        		 lblServerPortAddress.show();
        	}
        });
        
        JSeparator separator_7 = new JSeparator();
        separator_7.setForeground(Color.CYAN);
        separator_7.setBackground(Color.CYAN);
        separator_7.setBounds(56, 84, 277, 12);
        panel.add(separator_7);
        
        txtServerIpAdress = new JTextField();
        txtServerIpAdress.setText("Server IP address");
        txtServerIpAdress.setHorizontalAlignment(SwingConstants.LEFT);
        txtServerIpAdress.setForeground(Color.BLACK);
        txtServerIpAdress.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtServerIpAdress.setBackground(Color.WHITE);
        txtServerIpAdress.setBounds(56, 48, 277, 36);
        panel.add(txtServerIpAdress);
        
        lblServerIpAddress = new JLabel("Server IP address");
        lblServerIpAddress.setForeground(Color.WHITE);
        lblServerIpAddress.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblServerIpAddress.setBounds(56, 31, 102, 16);
        panel.add(lblServerIpAddress);
        lblServerIpAddress.hide();
        txtServerIpAdress.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		if(txtServerIpAdress.getText().equals("Server IP address"))
        			txtServerIpAdress.setText(null);
        		lblServerIpAddress.show();
        	}
        });
                lblPassword.hide();
                textField.addMouseListener(new MouseAdapter() {
                	@Override
                	public void mouseEntered(MouseEvent e) {
                		if(textField.getText().equals("********"))
                		textField.setText(null);
                		lblPassword.show();
                	}
                });
        
        label_4 = new JLabel();
        label_4.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/icons8-wired_network.png")));
        label_4.setBounds(19, 124, 25, 36);
        panel.add(label_4);
        
        label_6 = new JLabel();
        label_6.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/icons8-server.png")));
        label_6.setBounds(19, 48, 25, 36);
        panel.add(label_6);
        
        //panel_3 = new JPanel();
        KGradientPanel panel_3 = new KGradientPanel();
        panel_3.setkEndColor(Color.BLACK);
        panel_3.setkStartColor(BLACKBLUE);
        panel_3.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(0, 0, 0)));
        panel_3.setBackground(Color.BLACK);
        panel_3.setBounds(0, 0, 369, 286);
        panel.add(panel_3);
        panel_3.setLayout(null);
        panel_3.hide();
        
        label_7 = new JLabel();
        label_7.setBounds(301, 185, 32, 32);
        panel_3.add(label_7);
        label_7.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/login.png")));
        label_7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_loginMousePressed(evt);
            }
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		label_7.setIcon(new ImageIcon(getClass().getResource("/risorse/login2.png")));
        	}
        @Override
        	public void mouseExited(MouseEvent e) {
        		label_7.setIcon(new ImageIcon(getClass().getResource("/risorse/login.png")));
        	}
        });
        
        lblServerIpAddress_2 = new JLabel("Server IP address or port do not correspond");
        lblServerIpAddress_2.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/notok.png")));
        lblServerIpAddress_2.setForeground(Color.RED);
        lblServerIpAddress_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblServerIpAddress_2.setBounds(55, 255, 308, 25);
        panel_3.add(lblServerIpAddress_2);
        lblEmail_1.hide();
        txtEmail.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		if(txtEmail.getText().equals("Email or username"))
        		txtEmail.setText(null);
        		lblEmail_1.show();
        	}
        });
                lblPassword.hide();
                textField.addMouseListener(new MouseAdapter() {
                	@Override
                	public void mouseEntered(MouseEvent e) {
                		if(textField.getText().equals("********"))
                		textField.setText(null);
                		lblPassword.show();
                	}
                });
        lblNewLabel_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblNewLabel_5.setForeground(Color.cyan);
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				lblNewLabel_5.setForeground(Color.white);
			}
		});
        btn_login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_loginMousePressed(evt);
            }
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		btn_login.setIcon(new ImageIcon(getClass().getResource("/risorse/login2.png")));
        	}
        @Override
        	public void mouseExited(MouseEvent e) {
        	btn_login.setIcon(new ImageIcon(getClass().getResource("/risorse/login.png")));
        	}
        });
        
        lbldont = new JLabel("Email, username or password do not correspond");
        lbldont.setBounds(230, 516, 308, 25);
        login.add(lbldont);
        lbldont.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lbldont.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/notok.png")));
        lbldont.setForeground(Color.RED);
        
        lblServerIpAddress_1 = new JLabel("Server IP address or port do not correspond");
        lblServerIpAddress_1.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/notok.png")));
        lblServerIpAddress_1.setForeground(Color.RED);
        lblServerIpAddress_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblServerIpAddress_1.setBounds(230, 542, 308, 25);
        login.add(lblServerIpAddress_1);
       
        KGradientPanel panel_1 = new KGradientPanel();
        panel_1.setkEndColor(Color.BLACK);
        panel_1.setkStartColor(BLACKBLUE);
        panel_1.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(64, 64, 64)));
        panel_1.setBackground(BLACKBLUE);
        panel_1.setBounds(391, 80, 185, 32);
        login.add(panel_1);
        panel_1.addMouseListener(new MouseAdapter() {
        		public void mouseClicked(MouseEvent e) 
        		{
        			//panel_1.setBorder(new MatteBorder(1, 1, 0, 1, (Color) new Color(64, 64, 64)));
        			panel_3.show();
        			//panel_3.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(64, 64, 64)));
        			panel.setBorder(null);
        			lblEmail_1.hide();
        			jLabel2.hide();
        			txtEmail.hide();
        			separator_3.hide();
        			lblPassword.hide();
        			jLabel3.hide();
        			textField.hide();
        			jSeparator1.hide();
        			lblNewLabel_5.hide();
        			btn_login.hide();
        			lbldont.hide();
        			lblServerIpAddress_1.hide();
        		}
        });
        
        login.addMouseListener(new MouseAdapter() {
    		public void mouseClicked(MouseEvent e) 
    		{
    			panel_3.hide();
    			panel_1.setBackground(BLACKBLUE);
    			//panel_1.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(64, 64, 64)));
    			//panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(64, 64, 64)));
    			lblEmail_1.show();
    			jLabel2.show();
    			txtEmail.show();
    			separator_3.show();
    			lblPassword.show();
    			jLabel3.show();
    			textField.show();
    			jSeparator1.show();
    			lblNewLabel_5.show();
    			btn_login.show();
    			lbldont.show();
    			lblServerIpAddress_1.show();
    		}
        });
        
        JLabel lblAnonymous = new JLabel("Anonymous");
        panel_1.add(lblAnonymous);
        lblAnonymous.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/icons8-anonymous_mask.png")));
        lblAnonymous.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblAnonymous.setForeground(Color.WHITE);
       
        loader.setBackground(BLACKBLUE);

        javax.swing.GroupLayout loaderLayout = new javax.swing.GroupLayout(loader);
        loaderLayout.setHorizontalGroup(
        	loaderLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(Alignment.TRAILING, loaderLayout.createSequentialGroup()
        			.addContainerGap(104, Short.MAX_VALUE)
        			.addComponent(img_loader, GroupLayout.PREFERRED_SIZE, 649, GroupLayout.PREFERRED_SIZE)
        			.addGap(47))
        );
        loaderLayout.setVerticalGroup(
        	loaderLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(Alignment.TRAILING, loaderLayout.createSequentialGroup()
        			.addGap(12)
        			.addComponent(img_loader, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
        );
        loader.setLayout(loaderLayout);

        jPanel1.add(loader, "card3");

        javax.swing.GroupLayout pnl_bgLayout = new javax.swing.GroupLayout(pnl_bg);
        pnl_bg.setLayout(pnl_bgLayout);
        pnl_bgLayout.setHorizontalGroup(
            pnl_bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnl_bgLayout.setVerticalGroup(
            pnl_bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(pnl_bg, GroupLayout.PREFERRED_SIZE, 968, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(pnl_bg, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        getContentPane().setLayout(layout);
 
        registration.setBackground(new java.awt.Color(23, 35, 51));
        
        lblReg = new JLabel("New Account");
        lblReg.setBounds(69, 50, 202, 37);
        lblReg.setForeground(Color.WHITE);
        lblReg.setFont(new Font("Tahoma", Font.PLAIN, 30));
        lblReg.setIcon(new ImageIcon(getClass().getResource("/risorse/dot.png")));
        
        
        registration.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                loginMouseDragged(evt);
            }
        });
        registration.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                loginMousePressed(evt);
            }
        });
        jPanel1.add(registration, "card4");
        
        lblNewLabel = new JLabel("");
        lblNewLabel.setBounds(716, 516, 64, 64);
        lblNewLabel.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/logo1.png")));
        
        lblAccedi = new JLabel("Or access");
        lblAccedi.setBounds(69, 516, 97, 32);
        lblAccedi.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/login.png")));
        lblAccedi.setForeground(Color.WHITE);
        lblAccedi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
            		lblAccedi_loginMousePressed(evt);
            }
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lblAccedi.setForeground(Color.CYAN);
        		lblAccedi.setIcon(new ImageIcon(getClass().getResource("/risorse/login2.png")));
        	}
        @Override
        	public void mouseExited(MouseEvent e) {
        	lblAccedi.setForeground(Color.WHITE);
        	lblAccedi.setIcon(new ImageIcon(getClass().getResource("/risorse/login.png")));
        	}
        });
        
        JSeparator separator = new JSeparator();
        separator.setBounds(282, 257, 0, 12);
        separator.setForeground(Color.CYAN);
        
        label_3 = new JLabel("");
        label_3.setBounds(769, 6, 25, 25);
        label_3.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/close1.png")));
        
        label_3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_closeMousePressed(evt);
            };
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		label_3.setIcon(new ImageIcon(getClass().getResource("/risorse/close2.png")));
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		label_3.setIcon(new ImageIcon(getClass().getResource("/risorse/close1.png")));
        	}
        }); 
        
        
        registration.setLayout(null);
        registration.add(separator);
        registration.add(lblReg);
        registration.add(lblAccedi);
        registration.add(lblNewLabel);
        registration.add(label_3);
        
        label_5 = new JLabel("");
        label_5.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/minimize1.png")));
        label_5.setBounds(745, 6, 25, 25);
        registration.add(label_5);
        
        panel_2 = new JPanel();
        panel_2.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(64, 64, 64)));
        panel_2.setBounds(205, 111, 369, 390);
        panel_2.setBackground(BLACKBLUE);
        registration.add(panel_2);
        panel_2.setLayout(null);
        
                
                lblNewLabel_1 = new JLabel("Sign up");
                lblNewLabel_1.setBounds(252, 330, 76, 25);
                panel_2.add(lblNewLabel_1);
                lblNewLabel_1.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/register_user.png")));
                lblNewLabel_1.setForeground(Color.WHITE);
                
                JSeparator separator_4 = new JSeparator();
                separator_4.setBounds(54, 295, 274, 12);
                panel_2.add(separator_4);
                separator_4.setForeground(Color.CYAN);
                
                textField_5 = new JTextField();
                textField_5.setBounds(51, 259, 277, 34);
                panel_2.add(textField_5);
                textField_5.setText("Email");
                textField_5.setColumns(10);
                
                JLabel label_2 = new JLabel("\n");
                label_2.setBounds(20, 261, 33, 30);
                panel_2.add(label_2);
                label_2.setForeground(Color.WHITE);
                label_2.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/email.png")));
                
                 JLabel lblEmail = new JLabel("Email");
                 lblEmail.setBounds(54, 238, 78, 22);
                 panel_2.add(lblEmail);
                 lblEmail.setForeground(Color.WHITE);
                 lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 13));
                 
                 JSeparator separator_2 = new JSeparator();
                 separator_2.setBounds(51, 223, 277, 12);
                 panel_2.add(separator_2);
                 separator_2.setForeground(Color.CYAN);
                 
                 textField_4 = new JTextField();
                 textField_4.setBounds(51, 186, 277, 34);
                 panel_2.add(textField_4);
                 textField_4.setText("********");
                 textField_4.setColumns(10);
                 
                 label_1 = new JLabel("\n");
                 label_1.setBounds(20, 186, 29, 35);
                 panel_2.add(label_1);
                 label_1.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/lock.png")));
                 
                 JLabel lblRepeatPassword = new JLabel("Repeat Password");
                 lblRepeatPassword.setBounds(54, 166, 142, 22);
                 panel_2.add(lblRepeatPassword);
                 lblRepeatPassword.setForeground(Color.WHITE);
                 lblRepeatPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
                 
                 JSeparator separator_1 = new JSeparator();
                 separator_1.setBounds(54, 151, 274, 12);
                 panel_2.add(separator_1);
                 separator_1.setForeground(Color.CYAN);
                 
                 textField_3 = new JTextField();
                 textField_3.setBounds(51, 116, 277, 34);
                 panel_2.add(textField_3);
                 textField_3.setText("********");
                 textField_3.setColumns(10);
                 
                 JLabel lblPassord = new JLabel("Password");
                 lblPassord.setBounds(54, 96, 61, 22);
                 panel_2.add(lblPassord);
                 lblPassord.setForeground(Color.WHITE);
                 lblPassord.setFont(new Font("Tahoma", Font.PLAIN, 13));
                 lblPassord.hide();
                 
                 label = new JLabel("\n");
                 label.setBounds(20, 116, 33, 34);
                 panel_2.add(label);
                 label.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/lock.png")));
                 
                 separator_5 = new JSeparator();
                 separator_5.setBounds(51, 80, 277, 12);
                 panel_2.add(separator_5);
                 separator_5.setForeground(Color.CYAN);
                 textField_2 = new JTextField();
                 textField_2.setBounds(51, 49, 277, 34);
                 panel_2.add(textField_2);
                 textField_2.setText("Username");
                 textField_2.setColumns(10);
                 
                 lblUsername = new JLabel("Username");
                 lblUsername.setBounds(54, 32, 64, 14);
                 panel_2.add(lblUsername);
                 lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 13));
                 lblUsername.setForeground(Color.WHITE);
                 
                 lblNewLabel_2 = new JLabel("\n");
                 lblNewLabel_2.setBounds(20, 54, 33, 25);
                 panel_2.add(lblNewLabel_2);
                 lblNewLabel_2.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/user.png")));
                 
                 lblNewLabel_9 = new JLabel("");
                 lblNewLabel_9.setBounds(340, 49, 29, 34);
                 panel_2.add(lblNewLabel_9);
                 lblNewLabel_9.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/ok.png")));
                 
                 lblEmailDoesnExists = new JLabel("Email does not exists or match");
                 lblEmailDoesnExists.setBounds(340, 259, 228, 35);
                 panel_2.add(lblEmailDoesnExists);
                 lblEmailDoesnExists.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/notok.png")));
                 lblEmailDoesnExists.setForeground(Color.RED);
                 
                 JLabel label_8 = new JLabel("");
                 label_8.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/ok.png")));
                 label_8.setBounds(340, 186, 29, 34);
                 panel_2.add(label_8);
                 
                 lblUsernameDoesntAvaiable = new JLabel("Username not avaiable");
                 lblUsernameDoesntAvaiable.setBounds(545, 165, 171, 25);
                 registration.add(lblUsernameDoesntAvaiable);
                 lblUsernameDoesntAvaiable.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/notok.png")));
                 lblUsernameDoesntAvaiable.setForeground(Color.RED);
                 lblUsernameDoesntAvaiable.setFont(new Font("Tahoma", Font.PLAIN, 13));
                 lblPasswordDoesntCorrespond = new JLabel("Password does not correspond");
                 lblPasswordDoesntCorrespond.setBounds(545, 298, 228, 35);
                 registration.add(lblPasswordDoesntCorrespond);
                 lblPasswordDoesntCorrespond.setIcon(new ImageIcon(ClientGUI.class.getResource("/risorse/notok.png")));
                 lblPasswordDoesntCorrespond.setForeground(Color.RED);
                 lblPasswordDoesntCorrespond.hide();
                 lblUsername.hide();
                 textField_2.addMouseListener(new MouseAdapter() {
                 		public void mouseEntered(MouseEvent e) {
                 			if(textField_2.getText().equals("Username"))
                 				textField_2.setText(null);
                 			lblUsername.show();
                 		}
                 });
                 
                 textField_3.addMouseListener(new MouseAdapter() {
                	 	public void mouseEntered(MouseEvent e) {
                	 		if(textField_3.getText().equals("********"))
                	 			textField_3.setText(null);
                	 		lblPassord.show();
                	 	}
                 });
                 lblRepeatPassword.hide();
                 textField_4.addMouseListener(new MouseAdapter() {
                	 	public void mouseEntered(MouseEvent e) {
                	 		if(textField_4.getText().equals("********"))
                	 			textField_4.setText(null);
                	 		lblRepeatPassword.show();
                	 	}
                	 	/*public void mouseClicked(MouseEvent e) {
                	 		if(textField_4.getText().equals("********"))
                	 			textField_4.setText(null);
                	 		lblRepeatPassword.show();
                	 	}*/
                	 	public void mouseExited(MouseEvent e) {
                	 		if(!textField_3.getText().equals(textField_4.getText())) {
                	 			lblPasswordDoesntCorrespond.show();
                	 			label_8.hide();
                	 		}
                	 		else {
                	 			lblPasswordDoesntCorrespond.hide();
                	 			label_8.show();
                	 		}
                	 	}
                 });
                 lblEmail.hide();
                textField_5.addMouseListener(new MouseAdapter() {
                	public void mouseEntered(MouseEvent e) {
                		if(textField_5.getText().equals("Email"))
                			textField_5.setText(null);
                		lblEmail.show();
                	}
                });
                lblNewLabel_1.addMouseListener(new MouseAdapter() {
                	@Override
                	public void mouseEntered(MouseEvent e) {
                		lblNewLabel_1.setForeground(Color.CYAN);
                		lblNewLabel_1.setIcon(new ImageIcon(getClass().getResource("/risorse/register_user2.png")));
                	}
                	public void mouseExited(MouseEvent e) {
                		lblNewLabel_1.setForeground(Color.WHITE);
                		lblNewLabel_1.setIcon(new ImageIcon(getClass().getResource("/risorse/register_user.png")));
                	}
                /**	@Override
                	public void mouseClicked(MouseEvent e) {
                		jLabelMouseClicked(e);
                	}**/
                });
       
        label_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ClientGUI.this.setState(Frame.ICONIFIED);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				label_5.setIcon(new ImageIcon(getClass().getResource("/risorse/minimize2.png")));
				
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				label_5.setIcon(new ImageIcon(getClass().getResource("/risorse/minimize1.png")));
			}
		});

        javax.swing.GroupLayout pnl_rgLayout = new javax.swing.GroupLayout(pnl_rg);
        pnl_rg.setLayout(pnl_rgLayout);
        pnl_rgLayout.setHorizontalGroup(
            pnl_rgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnl_rgLayout.setVerticalGroup(
            pnl_rgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout2 = new javax.swing.GroupLayout(getContentPane());
        layout2.setHorizontalGroup(
        	layout2.createParallelGroup(Alignment.LEADING)
        		.addComponent(pnl_bg, GroupLayout.PREFERRED_SIZE, 800, Short.MAX_VALUE)
        );
        layout2.setVerticalGroup(
        	layout2.createParallelGroup(Alignment.LEADING)
        		.addComponent(pnl_bg, GroupLayout.PREFERRED_SIZE, 600, Short.MAX_VALUE)
        );
        getContentPane().setLayout(layout2);

        pack(); 
    }


    private void btn_loginMousePressed(MouseEvent evt) {
        // TODO add your handling code here:
        //put your sql/your statements here to check for password and email if correct
        //then
        //also validate -
        loader.show();
        login.hide();
        btn_login.hide();
        
        //add new form       
       /** new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //after validating let's show the main Jframe
               Main m = new Main();
               m.setExtendedState(MAXIMIZED_BOTH);
               m.show();
              // after successfull login let's close the login window
              //call:
              
              dispose();
              
              //
              
              //cool stuff.....
              
              
              //Added some Jfree chart so let's see how it goes.
              //it will include on Github
              
               }
        },1000*5);**/
    }
    
    private void lblAccedi_loginMousePressed(MouseEvent evt) {
    	loader.hide();
    	registration.hide();
    	login.show();
    }
    
    private void lbl_closeMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        System.exit(0);
    }

    int xy, xx;
    private void loginMouseDragged(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);

    }

    private void loginMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:

        xx = evt.getX();
        xy = evt.getY();
    }

    // Variables declaration
    private javax.swing.JLabel btn_login;
    private javax.swing.JLabel img_loader;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel loader;
    private javax.swing.JPanel registration;
    private javax.swing.JPanel login;
    private javax.swing.JPanel pnl_bg;
    private javax.swing.JPanel pnl_rg;
    private JTextField txtEmail;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private JTextField textField;
    private JLabel lblReg;
    private JSeparator separator_3;
    private JLabel lblUsername;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    private JTextField textField_5;
    private JLabel lblNewLabel;
    private JLabel lblAccedi;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_2;
    private JLabel label;
    private JLabel label_1;
    private JLabel label_3;
    private JSeparator separator_5;
    private JLabel label_5;
    private JLabel lblEmail_1;
    private JLabel lblPassword;
    private JLabel lblPasswordDoesntCorrespond;
    private JLabel lbldont;
    private JLabel lblUsernameDoesntAvaiable;
    private JLabel lblNewLabel_9;
    private JLabel lblEmailDoesnExists;
    private JTextField txtServerPortAddress;
    private JTextField txtServerIpAdress;
    private JLabel lblServerIpAddress;
    private JLabel label_4;
    private JLabel label_6;
    private JPanel panel_2;
    private JLabel lblServerIpAddress_1;
    private JPanel panel_3;
    private JLabel label_7;
    private JLabel lblServerIpAddress_2;
}

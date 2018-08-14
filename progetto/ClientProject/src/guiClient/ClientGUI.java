package guiClient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import keeptoo.KGradientPanel;
import utility.ServerInfoRecover;

import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Objects;
import java.util.Timer;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import Events.*;
import client.AnonymousClient;
import client.Client;

import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import java.awt.event.MouseAdapter;
import java.awt.Font;
import javax.swing.border.MatteBorder;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JScrollBar;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.CardLayout;


public class ClientGUI extends JFrame implements Runnable{
	
	// Variables declaration
    private javax.swing.JLabel btn_login;
    private javax.swing.JLabel img_loader;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel loader;
    private javax.swing.JPanel registration;
    private javax.swing.JPanel forgot;
    private javax.swing.JPanel login;
    private javax.swing.JPanel forum;
    private javax.swing.JPanel Anonymousforum;
    private javax.swing.JPanel RecoveryCode;
    private javax.swing.JPanel error;
    private javax.swing.JPanel pnl_bg;
    private javax.swing.JPanel pnl_rg;
    private javax.swing.JPanel pnl_fp;
    private javax.swing.JPanel pnl_fo;
    private javax.swing.JPanel pnl_afo;
    private javax.swing.JPanel pnl_rc;
    private javax.swing.JPanel pnl_error;
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
    private JTextField txtusername;
    private JTextField txtServerIpAdress;
    private JLabel lblServerIpAddress;
    private JLabel label_4;
    private JLabel label_6;
    private JPanel panel_2;
    private JLabel label_7;
    private JLabel lblServerIpAddress_2;
    private JTextField txtEmail_1;
    private JTextField txtRecoveryCode;
    private JTextField txtPassword;
    private JTextField txtRepeatPassword;
    private JLabel label_16;
    private JLabel label_17;
    private JLabel label_18;
    private JLabel label_19;
    private JLabel label_20;
    private JLabel lblSubmit;
    private JLabel label_21;
    private JLabel label_22;
    private JPanel panel_5;
    private JLabel lblNewLabel_11;
    private JPanel panel_4;
    private JPanel panel_10;
    private JPanel panel_11;
    private JPanel panel_13;
    private JPanel panel_13_1;
    private JPanel panel_14;
    private JPanel panel_23;
    private JPanel panel_17;
    private JPanel panel_15;
    private JLabel lblDate;
    private JLabel lblNewLabel_16;
    private JLabel label_24;
    private JLabel label_25;
    private JLabel label_26;
    private JLabel lblNewLabel_17;
    private JPanel panel_16;
    private JPanel panel_24;
    private JLabel lblNewLabel_18;
    private JPanel panel_21;
    private JPanel panel_25;
    private ImageIcon dot;
    private ImageIcon logoGif;
    private ImageIcon closeW;
    private ImageIcon closeR;
    private ImageIcon minW;
    private ImageIcon minC;
    private ImageIcon addL;
    private ImageIcon addD;
    private ImageIcon emailW;
    private ImageIcon anonymousS;
    private ImageIcon anonymousL;
    private ImageIcon logoutW;
    private ImageIcon logoutR;
    private ImageIcon code;
    private ImageIcon sent;
    private ImageIcon server;
    private ImageIcon canc;
    private ImageIcon port;
    private ImageIcon lock;
    private ImageIcon loginW;
    private ImageIcon loginC;
    private ImageIcon logo128;
    private ImageIcon logo32;
    private ImageIcon ok;
    private ImageIcon notOk;
    private ImageIcon registrateW;
    private ImageIcon registrateC;
    private ImageIcon user;
    private ImageIcon err;
    
    private ConcurrentLinkedQueue<Event> clientEngineToGui;
    private ConcurrentLinkedQueue<Event> guiToClient;
	private JTextField textField_1;
	private JLabel lblSubmit_1;
	private JLabel lblNewLabel_23;
    
	private static final Color BLACKBLUE = new Color(23, 35, 51);

	/**
	 * Launch the application.
	 */
	/**public static void main(String[] args) {
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
	}**/
	
	public void run() {
		try {
			Event current;
	        boolean uscita=false;
	        do{

	            current=clientEngineToGui.poll();
	            if(current instanceof ClientEvent){
	                switch(((ClientEvent) current).getType()){
	                    case SHUTDOWN:
	                        uscita=true;
	                        break;
	                }
	            }
	            if(current instanceof Window){
	                switch (((Window) current).getWindowType()){
	                    case FORUM:

	                        break;
	                        
	                    case LOGIN:
	                        	login.setVisible(true);
	                        	if(((AccountLoginWindow)current).isErr())
							{
								lbldont.setVisible(true);
							}	
	                        break;
	                        
	                    case NEWACCOUNT:
	                      
	                        break;
	                        
	                    case ANONYMOUSLOGIN:
	                        
	                        break;
	                            
	                    case FORGOTPASSWORD:
	                        
	                        break;
	                }
	            }
	        }while(!uscita);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Create the frame.
	 */
	public ClientGUI(ConcurrentLinkedQueue<Event> clientEngineToGui, ConcurrentLinkedQueue<Event> guiToClientEngine) {
		this.clientEngineToGui=clientEngineToGui;
		this.guiToClient=guiToClientEngine;
		this.setVisible(true);
		Dimension objDimension = Toolkit.getDefaultToolkit().getScreenSize();
        int iCoordX = (objDimension.width - this.getWidth()) / 2;
        int iCoordY = (objDimension.height - this.getHeight()) / 2;
        this.setLocation(iCoordX, iCoordY); 
		initComponents();	
	}
	
	private void initComponents() {

        pnl_bg = new javax.swing.JPanel();
        pnl_rg = new javax.swing.JPanel();
        pnl_fp = new javax.swing.JPanel();
        pnl_fo = new javax.swing.JPanel();
        pnl_afo = new javax.swing.JPanel();
        pnl_rc = new javax.swing.JPanel();
        pnl_error = new javax.swing.JPanel();
        
        jPanel1 = new javax.swing.JPanel();
        
        login = new javax.swing.JPanel();
        login.setForeground(Color.BLACK);
        jLabel6 = new javax.swing.JLabel();
        jLabel6.setBounds(69, 50, 101, 37);
       
        loader = new javax.swing.JPanel();
        registration = new javax.swing.JPanel();
        forgot = new javax.swing.JPanel();
        forum = new javax.swing.JPanel();
        Anonymousforum = new javax.swing.JPanel();
        img_loader = new javax.swing.JLabel();
        RecoveryCode = new javax.swing.JPanel();
        error = new javax.swing.JPanel();
        
        ClassLoader classLoader= ClassLoader.getSystemClassLoader();
        logoGif=new ImageIcon(Objects.requireNonNull(classLoader.getResource("logogif.gif")));
        img_loader.setIcon(logoGif);
        dot= new ImageIcon(Objects.requireNonNull(classLoader.getResource("dot.png")));
        closeW = new ImageIcon(Objects.requireNonNull(classLoader.getResource("close1.png")));
        closeR = new ImageIcon(Objects.requireNonNull(classLoader.getResource("close2.png")));
        minW = new ImageIcon(Objects.requireNonNull(classLoader.getResource("minimize1.png")));
        minC = new ImageIcon(Objects.requireNonNull(classLoader.getResource("minimize2.png")));
        addL = new ImageIcon(Objects.requireNonNull(classLoader.getResource("add.png")));
        addD = new ImageIcon(Objects.requireNonNull(classLoader.getResource("icons8-add.png")));
        emailW = new ImageIcon(Objects.requireNonNull(classLoader.getResource("email.png")));
        anonymousS = new ImageIcon(Objects.requireNonNull(classLoader.getResource("anonymous_mask.png")));
        anonymousL = new ImageIcon(Objects.requireNonNull(classLoader.getResource("icons8-anonymous_mask.png")));
        logoutW = new ImageIcon(Objects.requireNonNull(classLoader.getResource("export.png")));
        logoutR = new ImageIcon(Objects.requireNonNull(classLoader.getResource("icons8-export.png")));
        code = new ImageIcon(Objects.requireNonNull(classLoader.getResource("icons8-qr_code.png")));
        sent = new ImageIcon(Objects.requireNonNull(classLoader.getResource("icons8-sent.png")));
        server = new ImageIcon(Objects.requireNonNull(classLoader.getResource("icons8-server.png")));
        canc = new ImageIcon(Objects.requireNonNull(classLoader.getResource("icons8-trash.png")));
        port = new ImageIcon(Objects.requireNonNull(classLoader.getResource("icons8-wired_network.png")));
        lock = new ImageIcon(Objects.requireNonNull(classLoader.getResource("lock.png")));
        loginW = new ImageIcon(Objects.requireNonNull(classLoader.getResource("login.png")));
        loginC = new ImageIcon(Objects.requireNonNull(classLoader.getResource("login2.png")));
        logo128 = new ImageIcon(Objects.requireNonNull(classLoader.getResource("logo.png")));
        logo32 = new ImageIcon(Objects.requireNonNull(classLoader.getResource("logo1.png")));
        ok = new ImageIcon(Objects.requireNonNull(classLoader.getResource("ok.png")));
        notOk = new ImageIcon(Objects.requireNonNull(classLoader.getResource("notok.png")));
        registrateW = new ImageIcon(Objects.requireNonNull(classLoader.getResource("register_user.png")));
        registrateC = new ImageIcon(Objects.requireNonNull(classLoader.getResource("register_user2.png")));
        user = new ImageIcon(Objects.requireNonNull(classLoader.getResource("user.png")));
        err = new ImageIcon(Objects.requireNonNull(classLoader.getResource("icons8-error.png")));

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
        
//--------------------- Login form --------------------------------------------        
        
        jPanel1.add(login, "card2");
        login.setLayout(null);
        login.add(jLabel6);
        
        JLabel lblNewLabel_3 = new JLabel("");
        lblNewLabel_3.setIcon(closeW);
        lblNewLabel_3.setBounds(769, 6, 25, 25);
        login.add(lblNewLabel_3);
        lblNewLabel_3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_closeMousePressed(evt);
            };
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lblNewLabel_3.setIcon(closeR);
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		lblNewLabel_3.setIcon(closeW);
        	}
        });
        
        JLabel lblNewLabel_4 = new JLabel("");
        lblNewLabel_4.setIcon(minW);
        lblNewLabel_4.setBounds(745, 6, 25, 25);
        login.add(lblNewLabel_4);
        lblNewLabel_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ClientGUI.this.setState(Frame.ICONIFIED);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblNewLabel_4.setIcon(minC);
				
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				lblNewLabel_4.setIcon(minW);
			}
		});
        
        JLabel lblNewLabel_6 = new JLabel("Sign up");
        lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel_6.setIcon(registrateW);
        lblNewLabel_6.setForeground(Color.WHITE);
        lblNewLabel_6.setBounds(69, 516, 85, 25);
        login.add(lblNewLabel_6);
        lblNewLabel_6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
            		login.setVisible(false);
            		loader.setVisible(false);
            		registration.setVisible(true);
            }
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lblNewLabel_6.setForeground(Color.CYAN);
        		lblNewLabel_6.setIcon(registrateC);
        	}
        @Override
        	public void mouseExited(MouseEvent e) {
        		lblNewLabel_6.setForeground(Color.WHITE);
        		lblNewLabel_6.setIcon(registrateW);
        	}
        });
        
        JLabel lblNewLabel_7 = new JLabel("");
        lblNewLabel_7.setIcon(logo32);
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
        
        btn_login.setIcon(loginW);       
        
        JLabel lblNewLabel_5 = new JLabel("Forgot password?");
        lblNewLabel_5.setBounds(56, 335, 102, 16);
        panel.add(lblNewLabel_5);
        lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel_5.setForeground(Color.WHITE);
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator1.setBounds(56, 321, 277, 12);
        panel.add(jSeparator1);
        
                jSeparator1.setBackground(Color.CYAN);
                jSeparator1.setForeground(Color.CYAN);
                
                textField = new JPasswordField();
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
                jLabel3.setIcon(lock);
                
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
        txtEmail.setText("Email");
        txtEmail.setHorizontalAlignment(SwingConstants.LEFT);
        txtEmail.setForeground(Color.BLACK);
        txtEmail.setBackground(Color.WHITE);
        jLabel2= new javax.swing.JLabel();
        jLabel2.setBounds(19, 212, 25, 36);
        panel.add(jLabel2);
        
        jLabel2.setIcon(user);
        
        lblEmail_1 = new JLabel("Email");
        lblEmail_1.setBounds(56, 195, 117, 16);
        panel.add(lblEmail_1);
        lblEmail_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblEmail_1.setForeground(Color.WHITE);
        
        JSeparator separator_6 = new JSeparator();
        separator_6.setForeground(Color.CYAN);
        separator_6.setBackground(Color.CYAN);
        separator_6.setBounds(56, 160, 277, 12);
        panel.add(separator_6);
        
        txtusername = new JTextField();
        txtusername.setText("Username");
        txtusername.setHorizontalAlignment(SwingConstants.LEFT);
        txtusername.setForeground(Color.BLACK);
        txtusername.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtusername.setBackground(Color.WHITE);
        txtusername.setBounds(56, 124, 277, 36);
        panel.add(txtusername);
        
        JLabel lblusername = new JLabel("Username");
        lblusername.setForeground(Color.WHITE);
        lblusername.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblusername.setBounds(56, 108, 70, 16);
        panel.add(lblusername);
        lblusername.setVisible(false);
        
        txtusername.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		if(txtusername.getText().equals("Server port"))
        			txtusername.setText(null);
        		 lblusername.setVisible(true);
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
        lblServerIpAddress.setVisible(false);
        txtServerIpAdress.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		if(txtServerIpAdress.getText().equals("Server IP address"))
        			txtServerIpAdress.setText(null);
        		lblServerIpAddress.setVisible(true);
        	}
        });
                lblPassword.setVisible(false);
                textField.addMouseListener(new MouseAdapter() {
                	@Override
                	public void mouseEntered(MouseEvent e) {
                		if(textField.getText().equals("********"))
                		textField.setText(null);
                		lblPassword.setVisible(true);
                	}
                });
        
        label_4 = new JLabel();
        label_4.setIcon(port);
        label_4.setBounds(19, 124, 25, 36);
        panel.add(label_4);
        
        label_6 = new JLabel();
        label_6.setIcon(server);
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
        panel_3.setVisible(false);
        
        label_7 = new JLabel();
        label_7.setBounds(301, 185, 32, 32);
        panel_3.add(label_7);
        label_7.setIcon(loginW);
        label_7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_loginMousePressed3(evt);
            }
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		label_7.setIcon(loginC);
        	}
        @Override
        	public void mouseExited(MouseEvent e) {
        		label_7.setIcon(loginW);
        	}
        });
        
        lblServerIpAddress_2 = new JLabel("Server IP address does not correspond");
        lblServerIpAddress_2.setIcon(notOk);
        lblServerIpAddress_2.setForeground(Color.RED);
        lblServerIpAddress_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblServerIpAddress_2.setBounds(55, 255, 308, 25);
        panel_3.add(lblServerIpAddress_2);
        lblEmail_1.setVisible(false);
        txtEmail.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		if(txtEmail.getText().equals("Email or username"))
        		txtEmail.setText(null);
        		lblEmail_1.setVisible(true);
        	}
        });
                lblPassword.setVisible(false);
                textField.addMouseListener(new MouseAdapter() {
                	@Override
                	public void mouseEntered(MouseEvent e) {
                		if(textField.getText().equals("********"))
                		textField.setText(null);
                		lblPassword.setVisible(true);
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
        		@Override
        		public void mouseClicked(MouseEvent e) {
        			forgot_loginMousePressed(e);
        		}
		});
        btn_login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
            			btn_loginMousePressed(evt);
            			
            }
        		@Override
        		public void mouseEntered(MouseEvent e) {
        			btn_login.setIcon(loginC);
        		}
        		@Override
        		public void mouseExited(MouseEvent e) {
        			btn_login.setIcon(loginW);
        		}
        	});
        
        lbldont = new JLabel("Username, Server IP address or password does not correspond");
        lbldont.setBounds(207, 516, 369, 25);
        login.add(lbldont);
        lbldont.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lbldont.setIcon(notOk);
        lbldont.setForeground(Color.RED);
        lbldont.setVisible(false);
       
        KGradientPanel panel_1 = new KGradientPanel();
        panel_1.setkEndColor(Color.BLACK);
        panel_1.setkStartColor(BLACKBLUE);
        panel_1.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(64, 64, 64)));
        panel_1.setBackground(BLACKBLUE);
        panel_1.setBounds(391, 81, 185, 32);
        login.add(panel_1);
        panel_1.addMouseListener(new MouseAdapter() {
        		public void mouseClicked(MouseEvent e) 
        		{
        			//panel_1.setBorder(new MatteBorder(1, 1, 0, 1, (Color) new Color(64, 64, 64)));
        			panel_3.setVisible(true);
        			//panel_3.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(64, 64, 64)));
        			panel.setBorder(null);
        			lblEmail_1.setVisible(false);
        			jLabel2.setVisible(false);
        			txtEmail.setVisible(false);
        			separator_3.setVisible(false);
        			lblPassword.setVisible(false);
        			jLabel3.setVisible(false);
        			textField.setVisible(false);
        			jSeparator1.setVisible(false);
        			lblNewLabel_5.setVisible(false);
        			btn_login.setVisible(false);
        			lbldont.setVisible(false);
        			lblServerIpAddress_1.setVisible(false);
        		}
        });
        
        login.addMouseListener(new MouseAdapter() {
    		public void mouseClicked(MouseEvent e) 
    		{
    			panel_3.setVisible(false);
    			panel_1.setBackground(BLACKBLUE);
    			//panel_1.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(64, 64, 64)));
    			//panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(64, 64, 64)));
    			lblEmail_1.setVisible(true);
    			jLabel2.setVisible(true);
    			txtEmail.setVisible(true);
    			separator_3.setVisible(true);
    			lblPassword.setVisible(true);
    			jLabel3.setVisible(true);
    			textField.setVisible(true);
    			jSeparator1.setVisible(true);
    			lblNewLabel_5.setVisible(true);
    			btn_login.setVisible(true);
    			lbldont.setVisible(true);
    			lblServerIpAddress_1.setVisible(true);
    		}
        });
        
        JLabel lblAnonymous = new JLabel("Anonymous");
        panel_1.add(lblAnonymous);
        lblAnonymous.setIcon(anonymousS);
        lblAnonymous.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblAnonymous.setForeground(Color.WHITE);
       
//------------------------------ Loader Form -------------------------------------------------------------        
        
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
        
//------------------------- Registration Form -------------------------------------------------  
 
        registration.setBackground(new java.awt.Color(23, 35, 51));
        
        lblReg = new JLabel("New Account");
        lblReg.setBounds(69, 50, 202, 37);
        lblReg.setForeground(Color.WHITE);
        lblReg.setFont(new Font("Tahoma", Font.PLAIN, 30));
        lblReg.setIcon(dot);
        
        
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
        lblNewLabel.setIcon(logo32);
        
        lblAccedi = new JLabel("Or access");
        lblAccedi.setBounds(69, 516, 97, 32);
        lblAccedi.setIcon(loginW);
        lblAccedi.setForeground(Color.WHITE);
        lblAccedi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
            		lblAccedi_loginMousePressed(evt);
            }
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lblAccedi.setForeground(Color.CYAN);
        		lblAccedi.setIcon(loginC);
        	}
        @Override
        	public void mouseExited(MouseEvent e) {
        	lblAccedi.setForeground(Color.WHITE);
        	lblAccedi.setIcon(loginW);
        	}
        });
        
        JSeparator separator = new JSeparator();
        separator.setBounds(282, 257, 0, 12);
        separator.setForeground(Color.CYAN);
        
        label_3 = new JLabel("");
        label_3.setBounds(769, 6, 25, 25);
        label_3.setIcon(closeW);
        
        label_3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_closeMousePressed(evt);
            };
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		label_3.setIcon(closeR);
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		label_3.setIcon(closeW);
        	}
        }); 
        
        registration.setLayout(null);
        registration.add(separator);
        registration.add(lblReg);
        registration.add(lblAccedi);
        registration.add(lblNewLabel);
        registration.add(label_3);
        
        label_5 = new JLabel("");
        label_5.setIcon(minW);
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
                lblNewLabel_1.setIcon(registrateW);
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
                label_2.setIcon(emailW);
                
                 JLabel lblEmail = new JLabel("Email");
                 lblEmail.setBounds(54, 238, 78, 22);
                 panel_2.add(lblEmail);
                 lblEmail.setForeground(Color.WHITE);
                 lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 13));
                 
                 JSeparator separator_2 = new JSeparator();
                 separator_2.setBounds(51, 223, 277, 12);
                 panel_2.add(separator_2);
                 separator_2.setForeground(Color.CYAN);
                 
                 textField_4 = new JPasswordField();
                 textField_4.setBounds(51, 186, 277, 34);
                 panel_2.add(textField_4);
                 textField_4.setText("********");
                 textField_4.setColumns(10);
                 
                 label_1 = new JLabel("\n");
                 label_1.setBounds(20, 186, 29, 35);
                 panel_2.add(label_1);
                 label_1.setIcon(lock);
                 
                 JLabel lblRepeatPassword = new JLabel("Repeat Password");
                 lblRepeatPassword.setBounds(54, 166, 142, 22);
                 panel_2.add(lblRepeatPassword);
                 lblRepeatPassword.setForeground(Color.WHITE);
                 lblRepeatPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
                 
                 JSeparator separator_1 = new JSeparator();
                 separator_1.setBounds(54, 151, 274, 12);
                 panel_2.add(separator_1);
                 separator_1.setForeground(Color.CYAN);
                 
                 textField_3 = new JPasswordField();
                 textField_3.setBounds(51, 116, 277, 34);
                 panel_2.add(textField_3);
                 textField_3.setText("********");
                 textField_3.setColumns(10);
                 
                 JLabel lblPassord = new JLabel("Password");
                 lblPassord.setBounds(54, 96, 61, 22);
                 panel_2.add(lblPassord);
                 lblPassord.setForeground(Color.WHITE);
                 lblPassord.setFont(new Font("Tahoma", Font.PLAIN, 13));
                 lblPassord.setVisible(false);
                 
                 label = new JLabel("\n");
                 label.setBounds(20, 116, 33, 34);
                 panel_2.add(label);
                 label.setIcon(lock);
                 
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
                 lblNewLabel_2.setIcon(user);
                 
                 lblNewLabel_9 = new JLabel("");
                 lblNewLabel_9.setBounds(340, 49, 29, 34);
                 panel_2.add(lblNewLabel_9);
                 lblNewLabel_9.setIcon(ok);
                 
                 JLabel label_8 = new JLabel("");
                 label_8.setIcon(ok);
                 label_8.setBounds(340, 186, 29, 34);
                 panel_2.add(label_8);
                 
                 label_16 = new JLabel("");
                 label_16.setBounds(340, 259, 29, 34);
                 panel_2.add(label_16);
                 
                 lblUsernameDoesntAvaiable = new JLabel("Username not avaiable");
                 lblUsernameDoesntAvaiable.setBounds(545, 165, 171, 25);
                 registration.add(lblUsernameDoesntAvaiable);
                 lblUsernameDoesntAvaiable.setIcon(notOk);
                 lblUsernameDoesntAvaiable.setForeground(Color.RED);
                 lblUsernameDoesntAvaiable.setFont(new Font("Tahoma", Font.PLAIN, 13));
                 lblPasswordDoesntCorrespond = new JLabel("Password does not correspond");
                 lblPasswordDoesntCorrespond.setBounds(545, 298, 228, 35);
                 registration.add(lblPasswordDoesntCorrespond);
                 lblPasswordDoesntCorrespond.setIcon(notOk);
                 lblPasswordDoesntCorrespond.setForeground(Color.RED);
                 
                 lblEmailDoesnExists = new JLabel("Email does not exists or match");
                 lblEmailDoesnExists.setBounds(545, 369, 228, 35);
                 registration.add(lblEmailDoesnExists);
                 lblEmailDoesnExists.setFont(new Font("Tahoma", Font.PLAIN, 13));
                 lblEmailDoesnExists.setIcon(notOk);
                 lblEmailDoesnExists.setForeground(Color.RED);
                 lblPasswordDoesntCorrespond.setVisible(false);
                 lblUsername.setVisible(false);
                 textField_2.addMouseListener(new MouseAdapter() {
                 		public void mouseEntered(MouseEvent e) {
                 			if(textField_2.getText().equals("Username"))
                 				textField_2.setText(null);
                 			lblUsername.setVisible(true);
                 		}
                 });
                 
                 textField_3.addMouseListener(new MouseAdapter() {
                	 	public void mouseEntered(MouseEvent e) {
                	 		if(textField_3.getText().equals("********"))
                	 			textField_3.setText(null);
                	 		lblPassord.setVisible(true);
                	 	}
                 });
                 lblRepeatPassword.setVisible(false);
                 textField_4.addMouseListener(new MouseAdapter() {
                	 	public void mouseEntered(MouseEvent e) {
                	 		if(textField_4.getText().equals("********"))
                	 			textField_4.setText(null);
                	 		lblRepeatPassword.setVisible(true);
                	 	}
                	 	/*public void mouseClicked(MouseEvent e) {
                	 		if(textField_4.getText().equals("********"))
                	 			textField_4.setText(null);
                	 		lblRepeatPassword.show();
                	 	}*/
                	 	public void mouseExited(MouseEvent e) {
                	 		if(!textField_3.getText().equals(textField_4.getText())) {
                	 			lblPasswordDoesntCorrespond.setVisible(true);
                	 			label_8.setVisible(false);
                	 		}
                	 		else {
                	 			lblPasswordDoesntCorrespond.setVisible(false);
                	 			label_8.setVisible(true);
                	 		}
                	 	}
                 });
                 lblEmail.setVisible(false);
                textField_5.addMouseListener(new MouseAdapter() {
                	public void mouseEntered(MouseEvent e) {
                		if(textField_5.getText().equals("Email"))
                			textField_5.setText(null);
                		lblEmail.setVisible(true);
                	}
                });
                lblNewLabel_1.addMouseListener(new MouseAdapter() {
                	@Override
                	public void mouseEntered(MouseEvent e) {
                		lblNewLabel_1.setForeground(Color.CYAN);
                		lblNewLabel_1.setIcon(registrateC);
                	}
                	public void mouseExited(MouseEvent e) {
                		lblNewLabel_1.setForeground(Color.WHITE);
                		lblNewLabel_1.setIcon(registrateW);
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
				label_5.setIcon(minC);
				
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				label_5.setIcon(minW);
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
        
//-------------------------- Forgot password Form -------------------------------------------------------        
        
        forgot.setBackground(new java.awt.Color(23, 35, 51));
        
        forgot.setLayout(null);
        
        forgot.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                loginMouseDragged(evt);
            }
        });
        
        jPanel1.add(forgot, "card5");
        
        JLabel lblRecoverPassword = new JLabel("Recover Password");
        lblRecoverPassword.setIcon(dot);
        lblRecoverPassword.setForeground(Color.WHITE);
        lblRecoverPassword.setFont(new Font("Tahoma", Font.PLAIN, 30));
        lblRecoverPassword.setBounds(69, 50, 270, 37);
        forgot.add(lblRecoverPassword);
        
        JLabel label_9 = new JLabel("");
        label_9.setIcon(closeW);
        label_9.setBounds(769, 6, 25, 25);
        forgot.add(label_9);
        
        JLabel label_10 = new JLabel("");
        label_10.setIcon(minW);
        label_10.setBounds(745, 6, 25, 25);
        forgot.add(label_10);
        
        txtEmail_1 = new JTextField();
        txtEmail_1.setText("Email");
        txtEmail_1.setHorizontalAlignment(SwingConstants.LEFT);
        txtEmail_1.setForeground(Color.BLACK);
        txtEmail_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtEmail_1.setBackground(Color.WHITE);
        txtEmail_1.setBounds(215, 180, 277, 36);
        forgot.add(txtEmail_1);
        
        JLabel lblNewLabel_8 = new JLabel("Insert the email of your account, then we will send to your email a message containing the recovery code.");
        lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel_8.setForeground(Color.WHITE);
        lblNewLabel_8.setBounds(69, 132, 666, 16);
        forgot.add(lblNewLabel_8);
        
        txtRecoveryCode = new JTextField();
        txtRecoveryCode.setText("Recovery code");
        txtRecoveryCode.setHorizontalAlignment(SwingConstants.LEFT);
        txtRecoveryCode.setForeground(Color.BLACK);
        txtRecoveryCode.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtRecoveryCode.setBackground(Color.WHITE);
        txtRecoveryCode.setBounds(215, 296, 277, 36);
        forgot.add(txtRecoveryCode);
        
        JLabel lblInsertTheRecovery = new JLabel("Insert the recovery code, then you can set the new password.");
        lblInsertTheRecovery.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblInsertTheRecovery.setForeground(Color.WHITE);
        lblInsertTheRecovery.setBounds(69, 247, 417, 16);
        forgot.add(lblInsertTheRecovery);
        
        txtPassword = new JPasswordField();
        txtPassword.setText("********");
        txtPassword.setHorizontalAlignment(SwingConstants.LEFT);
        txtPassword.setForeground(Color.BLACK);
        txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setBounds(215, 369, 277, 36);
        forgot.add(txtPassword);
        
        txtRepeatPassword = new JPasswordField();
        txtRepeatPassword.setText("********");
        txtRepeatPassword.setHorizontalAlignment(SwingConstants.LEFT);
        txtRepeatPassword.setForeground(Color.BLACK);
        txtRepeatPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtRepeatPassword.setBackground(Color.WHITE);
        txtRepeatPassword.setBounds(215, 438, 277, 36);
        forgot.add(txtRepeatPassword);
        
        JLabel lblEmail_2 = new JLabel("Email");
        lblEmail_2.setForeground(Color.WHITE);
        lblEmail_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblEmail_2.setBounds(215, 168, 43, 16);
        forgot.add(lblEmail_2);
        lblEmail_2.setVisible(false);
        txtEmail_1.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		if(txtEmail_1.getText().equals("Email"))
        			txtEmail_1.setText(null);
        		lblEmail_2.setVisible(true);
        	}
        });
        
        JLabel lblRecoveryCode = new JLabel("Recovery code");
        lblRecoveryCode.setForeground(Color.WHITE);
        lblRecoveryCode.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblRecoveryCode.setBounds(215, 284, 88, 16);
        forgot.add(lblRecoveryCode);
        lblRecoveryCode.setVisible(false);
        txtRecoveryCode.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		if(txtRecoveryCode.getText().equals("Recovery code"))
        			txtRecoveryCode.setText(null);
        		lblRecoveryCode.setVisible(true);
        	}
        });
        
        JLabel lblPassword_1 = new JLabel("New password");
        lblPassword_1.setForeground(Color.WHITE);
        lblPassword_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblPassword_1.setBounds(215, 357, 88, 16);
        forgot.add(lblPassword_1);
        lblPassword_1.setVisible(false);
        txtPassword.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		if(txtPassword.getText().equals("********"))
        			txtPassword.setText(null);
        		lblPassword_1.setVisible(true);
        	}
        });
        
        JLabel lblRepeatPassword_1 = new JLabel("Repeat password");
        lblRepeatPassword_1.setForeground(Color.WHITE);
        lblRepeatPassword_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblRepeatPassword_1.setBounds(215, 425, 106, 16);
        forgot.add(lblRepeatPassword_1);
        lblRepeatPassword_1.setVisible(false);
        txtRepeatPassword.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		if(txtRepeatPassword.getText().equals("********"))
        			txtRepeatPassword.setText(null);
        		lblRepeatPassword_1.setVisible(true);
        	}
        });
        
        JLabel label_11 = new JLabel("");
        label_11.setIcon(logo32);
        label_11.setBounds(716, 516, 64, 64);
        forgot.add(label_11);
        
        JLabel label_12 = new JLabel("Or access");
        label_12.setFont(new Font("Tahoma", Font.PLAIN, 13));
        label_12.addMouseListener(new MouseAdapter() {
        	public void mousePressed(java.awt.event.MouseEvent evt) {
        		lblAccedi_loginMousePressed(evt);
        }
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		label_12.setForeground(Color.CYAN);
        		label_12.setIcon(loginC);
        	}
        @Override
        	public void mouseExited(MouseEvent e) {
        		label_12.setForeground(Color.WHITE);
        		label_12.setIcon(loginW);
        	}
        });
        label_12.setIcon(loginW);
        label_12.setForeground(Color.WHITE);
        label_12.setBounds(69, 516, 97, 32);
        forgot.add(label_12);
        
        JSeparator separator_8 = new JSeparator();
        separator_8.setForeground(Color.CYAN);
        separator_8.setBounds(215, 212, 277, 12);
        forgot.add(separator_8);
        
        JSeparator separator_9 = new JSeparator();
        separator_9.setForeground(Color.CYAN);
        separator_9.setBounds(215, 327, 277, 12);
        forgot.add(separator_9);
        
        JSeparator separator_10 = new JSeparator();
        separator_10.setForeground(Color.CYAN);
        separator_10.setBounds(215, 400, 277, 12);
        forgot.add(separator_10);
        
        JSeparator separator_11 = new JSeparator();
        separator_11.setForeground(Color.CYAN);
        separator_11.setBounds(215, 470, 277, 12);
        forgot.add(separator_11);
        
        JLabel label_13 = new JLabel("");
        label_13.setIcon(ok);
        label_13.setBounds(504, 180, 29, 34);
        forgot.add(label_13);
        
        JLabel lblEmailDoesNot = new JLabel("Email does not exist");
        lblEmailDoesNot.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblEmailDoesNot.setForeground(Color.RED);
        lblEmailDoesNot.setIcon(notOk);
        lblEmailDoesNot.setBounds(504, 181, 188, 34);
        forgot.add(lblEmailDoesNot);
        
        JLabel label_14 = new JLabel("");
        label_14.setIcon(ok);
        label_14.setBounds(504, 296, 29, 34);
        forgot.add(label_14);
        
        JLabel lblTheRecoveryDoes = new JLabel("The recovery code does not correspond");
        lblTheRecoveryDoes.setIcon(notOk);
        lblTheRecoveryDoes.setForeground(Color.RED);
        lblTheRecoveryDoes.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblTheRecoveryDoes.setBounds(504, 297, 266, 34);
        forgot.add(lblTheRecoveryDoes);
        
        JLabel label_15 = new JLabel("");
        label_15.setIcon(ok);
        label_15.setBounds(504, 438, 29, 34);
        forgot.add(label_15);
        
        JLabel lblPasswordsDoNot = new JLabel("Passwords do not correspond");
        lblPasswordsDoNot.setIcon(notOk);
        lblPasswordsDoNot.setForeground(Color.RED);
        lblPasswordsDoNot.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblPasswordsDoNot.setBounds(504, 439, 198, 34);
        forgot.add(lblPasswordsDoNot);
        
        label_17 = new JLabel("\n");
        label_17.setIcon(emailW);
        label_17.setForeground(Color.WHITE);
        label_17.setBounds(184, 180, 33, 30);
        forgot.add(label_17);
        
        label_18 = new JLabel("\n");
        label_18.setIcon(code);
        label_18.setForeground(Color.WHITE);
        label_18.setBounds(184, 296, 33, 30);
        forgot.add(label_18);
        
        label_19 = new JLabel("\n");
        label_19.setIcon(lock);
        label_19.setForeground(Color.WHITE);
        label_19.setBounds(184, 369, 33, 30);
        forgot.add(label_19);
        
        label_20 = new JLabel("\n");
        label_20.setIcon(lock);
        label_20.setForeground(Color.WHITE);
        label_20.setBounds(184, 438, 33, 30);
        forgot.add(label_20);
        
        lblSubmit = new JLabel("Submit");
        lblSubmit.setIcon(registrateW);
        lblSubmit.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblSubmit.setForeground(Color.WHITE);
        lblSubmit.setBounds(419, 486, 73, 32);
        forgot.add(lblSubmit);
        lblSubmit.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lblSubmit.setIcon(registrateC);
        		lblSubmit.setForeground(Color.CYAN);
        	}
        	public void mouseExited(MouseEvent e) {
        		lblSubmit.setIcon(registrateW);
        		lblSubmit.setForeground(Color.WHITE);
        	}
        });
        
        label_9.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mousePressed(java.awt.event.MouseEvent evt) {
    			lbl_closeMousePressed(evt);
        };
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		label_9.setIcon(closeR);
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		label_9.setIcon(closeW);
        	}
        });
        label_10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ClientGUI.this.setState(Frame.ICONIFIED);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				label_10.setIcon(minC);
				
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				label_10.setIcon(minW);
			}
        });
        
//---------------------- Forum Form --------------------------------------------------------------        
        
        jPanel1.add(forum, "card6");
        forum.setBackground(BLACKBLUE);
        forum.setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        forum.setSize(screenSize.width, screenSize.height);
        forum.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                loginMouseDragged(evt);
            }
        });
        forum.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                loginMousePressed(evt);
            }
        });
        
        label_21 = new JLabel("");
        label_21.setIcon(closeW);
        label_21.setBounds(769, 6, 25, 25);
        forum.add(label_21);
        label_21.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mousePressed(java.awt.event.MouseEvent evt) {
    			lbl_closeMousePressed(evt);
        };
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		label_21.setIcon(closeR);
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		label_21.setIcon(closeW);
        	}
        });
        
        label_22 = new JLabel("");
        label_22.setIcon(minW);
        label_22.setBounds(745, 6, 25, 25);
        forum.add(label_22);
        
        JLabel lblNewLabel_10 = new JLabel("");
        lblNewLabel_10.setIcon(logo32);
        lblNewLabel_10.setBounds(20, 523, 69, 70);
        forum.add(lblNewLabel_10);
        
        JPanel Panel1 = new JPanel();
        Panel1.setBackground(new Color(238,238,238));
        Panel1.setBounds(107, 46, 687, 501);
        forum.add(Panel1);
        Panel1.setLayout(null);
        
        KGradientPanel panel_7 = new KGradientPanel();
        panel_7.setBounds(0, 0, 687, 42);
        panel_7.kGradientFocus = 800;
        panel_7.kStartColor = Color.BLUE;
        panel_7.setkEndColor(Color.white);
        panel_7.setkStartColor(BLACKBLUE);
        Panel1.add(panel_7);
        panel_7.setLayout(null);
        
        lblNewLabel_11 = new JLabel("New post");
        lblNewLabel_11.setIcon(addL);
        lblNewLabel_11.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_11.setForeground(BLACKBLUE);
        lblNewLabel_11.setBounds(581, 6, 100, 31);
        panel_7.add(lblNewLabel_11);
        
        lblNewLabel_16 = new JLabel("Hi username!");
        lblNewLabel_16.setBounds(6, 6, 100, 16);
        panel_7.add(lblNewLabel_16);
        lblNewLabel_16.setForeground(Color.WHITE);
        lblNewLabel_16.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        panel_10 = new JPanel();
        panel_10.setBackground(new Color(255, 255, 255));
        panel_10.setBounds(345, 42, 342, 459);
        Panel1.add(panel_10);
        panel_10.setVisible(false);
        panel_10.setLayout(new CardLayout(0, 0));
        
        panel_13 = new JPanel();
        panel_10.add(panel_13, "card1");
        panel_13.setLayout(null);
        
        JEditorPane dtrpnAddATopic = new JEditorPane();
        dtrpnAddATopic.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		if(dtrpnAddATopic.getText().equals(" Add a topic"))
        			dtrpnAddATopic.setText(null);
        	}
        	public void mouseExited(MouseEvent e) {
        		if(dtrpnAddATopic.getText().equals(""))
        			dtrpnAddATopic.setText(" Add a topic");
        	}
        });
        
        dtrpnAddATopic.setBounds(16, 16, 297, 21);
        panel_13.add(dtrpnAddATopic);
        dtrpnAddATopic.setToolTipText("");
        dtrpnAddATopic.setFont(new Font("Tahoma", Font.PLAIN, 15));
        dtrpnAddATopic.setText(" Add a topic");
        
        JEditorPane editorPane_1 = new JEditorPane();
        //editorPane_1.setBounds(16, 49, 297, 390);
        
        editorPane_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        JScrollPane scrollPane = new JScrollPane(editorPane_1);
        scrollPane.setComponentZOrder(scrollPane.getVerticalScrollBar(), 0);
        scrollPane.setComponentZOrder(scrollPane.getViewport(), 1);
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.setBounds(16, 49, 297, 339);
        panel_13.add(scrollPane);
        
        JPanel panel_12 = new JPanel();
        panel_12.setBounds(109, 400, 70, 35);
        panel_13.add(panel_12);
        panel_12.addMouseListener(new MouseAdapter() {
        		@Override
        		public void mouseEntered(MouseEvent e) {
        			panel_12.setBackground(Color.LIGHT_GRAY);
        		}
        		@Override
        		public void mouseExited(MouseEvent e) 
        		{
        			panel_12.setBackground(Color.white);
        		}
        		@Override
        		public void mouseClicked(MouseEvent e) {
        			panel_10.setVisible(false);
        			editorPane_1.setText(null);
        			dtrpnAddATopic.setText(" Add a Topic");
        		}
        	});
        panel_12.setLayout(null);
        panel_12.setBackground(new Color(255, 255, 255));
        
        JLabel label_23 = new JLabel("");
        label_23.setBounds(22, 0, 24, 35);
        panel_12.add(label_23);
        label_23.setIcon(canc);
        
        panel_11 = new JPanel();
        panel_11.setBounds(26, 400, 70, 35);
        panel_13.add(panel_11);
        panel_11.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		panel_11.setBackground(new Color(23,28,208));
        	}
        	@Override
        	public void mouseExited(MouseEvent e) 
        	{
        		panel_11.setBackground(BLACKBLUE);
        	}
        });
        panel_11.setBackground(BLACKBLUE);
        panel_11.setLayout(null);
        panel_10.add(panel_13);
        
        JLabel lblNewLabel_12 = new JLabel("");
        lblNewLabel_12.setIcon(sent);
        lblNewLabel_12.setBounds(24, 6, 24, 23);
        panel_11.add(lblNewLabel_12);
        
        panel_13_1 = new JPanel();
        panel_13_1.setBackground(new Color(238,238,238));
        panel_10.add(panel_13_1, "card1");
        panel_13_1.setLayout(null);
        
        JTextArea txt=new JTextArea();
        txt.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txt.setText("Content...");
        
        JScrollPane jsp=new JScrollPane(txt);
        jsp.setComponentZOrder(jsp.getVerticalScrollBar(), 0);
        jsp.setComponentZOrder(jsp.getViewport(), 1);
        jsp.getVerticalScrollBar().setOpaque(false);
        jsp.setBounds(0, 82, 342, 342);
        panel_13_1.add(jsp);
        
        panel_15 = new JPanel();
        panel_15.setBounds(0, 0, 342, 81);
        panel_13_1.add(panel_15);
        panel_15.setBackground(new Color(238,238,238));
        panel_15.setLayout(null);
        
        JLabel lblNewLabel_14 = new JLabel("Topic");
        lblNewLabel_14.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_14.setBounds(6, 6, 330, 32);
        panel_15.add(lblNewLabel_14);
        
        JLabel lblNewLabel_15 = new JLabel("Date");
        lblNewLabel_15.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel_15.setBounds(254, 50, 82, 16);
        panel_15.add(lblNewLabel_15);
        
        panel_14 = new JPanel();
        panel_14.setBackground(Color.WHITE);
        //panel_14.setBounds(0, 42, 347, 506);
        //Panel1.add(panel_14);
        panel_14.setLayout(null);
        
       /** scrollBar_1 = new JScrollBar();
        scrollBar_1.setBounds(332, 0, 15, 506);
        panel_14.add(scrollBar_1);**/
        
        panel_16 = new JPanel();
        panel_16.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		panel_16.setBackground(new Color(238,238,238));
        		panel_17.setBackground(Color.WHITE);
        	}
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		panel_10.setVisible(true);
        		panel_13.setVisible(false);
        		panel_13_1.setVisible(true);
        	}
        });
        panel_16.setBackground(Color.WHITE);
        panel_16.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(238,238,238)));
        panel_16.setBounds(0, 0, 337, 81);
        panel_14.add(panel_16);
        panel_16.setLayout(null);
        
        JLabel lblUsername_1 = new JLabel("Username");
        lblUsername_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblUsername_1.setBounds(6, 6, 69, 16);
        panel_16.add(lblUsername_1);
        
        JLabel lblNewLabel_13 = new JLabel("Topic");
        lblNewLabel_13.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel_13.setBounds(6, 27, 320, 16);
        panel_16.add(lblNewLabel_13);
        
        JLabel lblFirstRowOf = new JLabel("First row of post…");
        lblFirstRowOf.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblFirstRowOf.setBounds(6, 55, 320, 16);
        panel_16.add(lblFirstRowOf);
        
        lblDate = new JLabel("Date");
        lblDate.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblDate.setBounds(265, 6, 61, 16);
        panel_16.add(lblDate);
        
        panel_17 = new JPanel();
        panel_17.setLayout(null);
        panel_17.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(238,238,238)));
        panel_17.setBackground(Color.WHITE);
        panel_17.setBounds(0, 82, 337, 81);
        panel_14.add(panel_17);
        panel_17.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		panel_17.setBackground(new Color(238,238,238));
        		panel_16.setBackground(Color.WHITE);
        	}
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		panel_10.setVisible(true);
        		panel_13.setVisible(false);
        		panel_13_1.setVisible(true);
        	}
        });
       
        lblNewLabel_11.addMouseListener(new java.awt.event.MouseAdapter() {
    			public void mouseClicked(java.awt.event.MouseEvent evt) {
    				panel_10.setVisible(true);
    				panel_13.setVisible(true);
    				panel_13_1.setVisible(false);
    			};
        		@Override
        		public void mouseEntered(MouseEvent e) {
        			lblNewLabel_11.setIcon(addD);
        			lblNewLabel_11.setForeground(new Color(23,28,208));
        		}
        		@Override
        		public void mouseExited(MouseEvent e) {
        			lblNewLabel_11.setIcon(addL);
        			lblNewLabel_11.setForeground(BLACKBLUE);
        		}
        	});
        
        JScrollPane jsp2=new JScrollPane(panel_14);
        jsp2.setComponentZOrder(jsp2.getVerticalScrollBar(), 0);
        jsp2.setComponentZOrder(jsp2.getViewport(), 1);
        jsp2.getVerticalScrollBar().setOpaque(false);
        jsp2.setBounds(0, 41, 347, 460);
        Panel1.add(jsp2);
        
        panel_5 = new JPanel();
        panel_5.setForeground(Color.WHITE);
        panel_5.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(0, 0, 0)));
        panel_5.setBackground(new Color(23, 35, 51));
        panel_5.setBounds(0, 114, 102, 28);
        forum.add(panel_5);
        panel_5.setLayout(null);
        panel_5.addMouseListener(new java.awt.event.MouseAdapter() {
    			@Override
    			public void mouseClicked(MouseEvent e) {
    				panel_5.setBorder(new MatteBorder(0, 3, 0, 0, Color.white));
    				panel_5.setBackground(new Color(23, 35, 90));
    				panel_4.setBorder(new MatteBorder(0, 3, 0, 0, BLACKBLUE));
    				panel_4.setBackground(new Color(23, 35, 51));
    			}
    			@Override
    			public void mouseEntered(MouseEvent e) 
    			{
    				panel_5.setBackground(new Color(23, 35, 90));
    			}
    			@Override
    			public void mouseExited(MouseEvent e) 
    			{
    				panel_5.setBackground(BLACKBLUE);
    			}
        });
        
        panel_4 = new JPanel();
        panel_4.setLayout(null);
        panel_4.setForeground(Color.WHITE);
        panel_4.setBorder(new MatteBorder(0, 5, 0, 0, (Color) new Color(0, 0, 0)));
        panel_4.setBackground(new Color(23, 35, 51));
        panel_4.setBounds(0, 154, 102, 28);
        forum.add(panel_4);
        panel_4.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				panel_4.setBorder(new MatteBorder(0, 3, 0, 0, Color.white));
				panel_4.setBackground(new Color(23, 35, 90));
				panel_5.setBorder(new MatteBorder(0, 3, 0, 0, BLACKBLUE));
				panel_5.setBackground(new Color(23, 35, 51));
			}
			@Override
			public void mouseEntered(MouseEvent e) 
			{
				panel_4.setBackground(new Color(23, 35, 90));
			}
			@Override
			public void mouseExited(MouseEvent e) 
			{
				panel_4.setBackground(BLACKBLUE);
			}
        });
        
        JLabel label_27 = new JLabel("Logout");
        label_27.addMouseListener(new MouseAdapter() {
        		@Override
        		public void mouseClicked(MouseEvent e) {
        			login.setVisible(true);
        			forum.setVisible(false);
        			btn_login.setVisible(true);
        		}
        		@Override
        		public void mouseEntered(MouseEvent e) 
        		{
        			label_27.setIcon(logoutR);
        			label_27.setForeground(Color.RED);
        		}
        		@Override
        		public void mouseExited(MouseEvent e) 
        		{
        			label_27.setIcon(logoutW);
        			label_27.setForeground(Color.WHITE);
        		}
        });
        label_27.setIcon(logoutW);
        label_27.setForeground(Color.WHITE);
        label_27.setFont(new Font("Tahoma", Font.PLAIN, 13));
        label_27.setBounds(720, 559, 74, 31);
        forum.add(label_27);
        
        panel_4.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				panel_13_1.setVisible(true);
				panel_13.setVisible(false);
				};
    			@Override
    			public void mouseClicked(MouseEvent e) {
    				panel_5.setBorder(new MatteBorder(0, 3, 0, 0, BLACKBLUE));
    				panel_5.setBackground(new Color(23, 35, 51));
    				panel_4.setBorder(new MatteBorder(0, 3, 0, 0, Color.white));
    				panel_4.setBackground(new Color(23, 35, 90));
    			}
    			@Override
    			public void mouseEntered(MouseEvent e) 
    			{
    				panel_4.setBackground(new Color(23, 35, 90));
    			}
    			@Override
    			public void mouseExited(MouseEvent e) 
    			{
    				panel_4.setBackground(BLACKBLUE);
    			}
        });
        
        label_22.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ClientGUI.this.setState(Frame.ICONIFIED);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				label_22.setIcon(minC);
				
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				label_22.setIcon(minW);
			}
        });
        
//------------------------ AnonymousForum Form ----------------------------------------------------
        
        jPanel1.add(Anonymousforum, "card7");
        Anonymousforum.setBackground(Color.BLACK);
        Anonymousforum.setLayout(null);
        Dimension screenSize1 = Toolkit.getDefaultToolkit().getScreenSize();
        Anonymousforum.setSize(screenSize1.width, screenSize1.height);
        Anonymousforum.setLayout(null);
        
        KGradientPanel panel_18 = new KGradientPanel();
        panel_18.kGradientFocus = 2000;
        panel_18.kStartColor = Color.WHITE;
        panel_18.kEndColor = Color.GRAY;
        panel_18.setBounds(106, 46, 687, 501);
        Anonymousforum.add(panel_18);
        panel_18.setLayout(null);
        
        KGradientPanel panel_19 = new KGradientPanel();
        panel_19.kEndColor = Color.GRAY;
        panel_19.kStartColor = Color.BLACK;
        panel_19.setBounds(0, 0, 687, 42);
        panel_18.add(panel_19);
        panel_19.setLayout(null);
        
        lblNewLabel_17 = new JLabel("All posts view");
        lblNewLabel_17.setForeground(Color.WHITE);
        lblNewLabel_17.setBounds(6, 12, 95, 16);
        lblNewLabel_17.setFont(new Font("Tahoma", Font.PLAIN, 15));
        panel_19.add(lblNewLabel_17);
        
        label_24 = new JLabel("");
        label_24.setIcon(logo32);
        label_24.setBounds(19, 524, 69, 70);
        Anonymousforum.add(label_24);
        
        label_25 = new JLabel("");
        label_25.addMouseListener(new MouseAdapter() {
        		@Override
        		public void mouseEntered(MouseEvent e) {
        			label_25.setIcon(closeR);
        		}
        		@Override
        		public void mouseExited(MouseEvent e) 
        		{
        			label_25.setIcon(closeW);
        		}
        		@Override
        		public void mouseClicked(MouseEvent e) 
        		{
        			lbl_closeMousePressed(e);
        		}
        });
        label_25.setIcon(closeW);
        label_25.setBounds(769, 6, 25, 25);
        Anonymousforum.add(label_25);
        
        label_26 = new JLabel("");
        label_26.addMouseListener(new MouseAdapter() {
        		@Override
        		public void mouseEntered(MouseEvent e) {
        			label_26.setIcon(minC);
        		}
        		@Override
        		public void mouseExited(MouseEvent e) {
        			label_26.setIcon(minW);
        		}
        		@Override
    			public void mouseClicked(MouseEvent arg0) {
    				ClientGUI.this.setState(Frame.ICONIFIED);
    			}
        });
        label_26.setIcon(minW);
        label_26.setBounds(745, 6, 25, 25);
        Anonymousforum.add(label_26);
        
        Anonymousforum.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                loginMouseDragged(evt);
            }
        });
        Anonymousforum.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                loginMousePressed(evt);
            }
        });
        
        panel_24 = new JPanel();
        panel_24.setBackground(Color.WHITE);
        //panel_14.setBounds(0, 42, 347, 506);
        //Panel1.add(panel_14);
        panel_24.setLayout(null);
        
       	//scrollBar_1 = new JScrollBar();
        //scrollBar_1.setBounds(332, 0, 15, 506);
        //panel_14.add(scrollBar_1);
        
        panel_23 = new JPanel();
        panel_23.setBackground(Color.WHITE);
        panel_23.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(238,238,238)));
        panel_23.setBounds(0, 0, 337, 81);
        panel_24.add(panel_23);
        panel_23.setLayout(null);
        
        JLabel lblUsername_2 = new JLabel("Username");
        lblUsername_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblUsername_2.setBounds(6, 6, 69, 16);
        panel_23.add(lblUsername_2);
        
        JLabel lblNewLabel_16 = new JLabel("Topic");
        lblNewLabel_16.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel_16.setBounds(6, 27, 320, 16);
        panel_23.add(lblNewLabel_16);
        
        JLabel lblFirstRowOf2 = new JLabel("First row of post…");
        lblFirstRowOf2.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblFirstRowOf2.setBounds(6, 55, 320, 16);
        panel_23.add(lblFirstRowOf2);
        
        lblDate = new JLabel("Date");
        lblDate.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblDate.setBounds(265, 6, 61, 16);
        panel_23.add(lblDate);
        
        panel_25 = new JPanel();
        panel_25.setLayout(null);
        panel_25.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(238,238,238)));
        panel_25.setBackground(Color.WHITE);
        panel_25.setBounds(0, 82, 337, 81);
        panel_24.add(panel_25);
        
        JScrollPane jsp3=new JScrollPane(panel_24);
        jsp3.setComponentZOrder(jsp3.getVerticalScrollBar(), 0);
        jsp3.setComponentZOrder(jsp3.getViewport(), 1);
        jsp3.getVerticalScrollBar().setOpaque(false);
        jsp3.setBounds(0, 41, 347, 460);
        panel_18.add(jsp3);
        
        lblNewLabel_18 = new JLabel("New label");
        lblNewLabel_18.setIcon(anonymousL);
        lblNewLabel_18.setBounds(19, 19, 69, 50);
        Anonymousforum.add(lblNewLabel_18); 
        
        JPanel panel_20 = new JPanel();
        panel_20.setBounds(359, 42, 328, 459);
        panel_18.add(panel_20);
        panel_20.setLayout(null);
        
        JTextArea txt2=new JTextArea();
        //txt2.setBounds(25, 81, 280, 400);
        //panel_20.add(txt2);
        txt2.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txt2.setText("Content...");
        
        JScrollPane jsp4=new JScrollPane(txt2);
        jsp4.setComponentZOrder(jsp4.getVerticalScrollBar(), 0);
        jsp4.setComponentZOrder(jsp4.getViewport(), 1);
        jsp4.getVerticalScrollBar().setOpaque(false);
        jsp4.setBounds(6, 81, 316, 316);
        panel_20.add(jsp4);
        
        panel_21 = new JPanel();
        panel_21.setBounds(6, 0, 316, 81);
        panel_20.add(panel_21);
        panel_21.setLayout(null);
        
        JLabel lblNewLabel_19 = new JLabel("Topic");
        lblNewLabel_19.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_19.setBounds(6, 6, 304, 16);
        panel_21.add(lblNewLabel_19);
        
        JLabel lblDate_1 = new JLabel("Date");
        lblDate_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblDate_1.setBounds(237, 59, 73, 16);
        panel_21.add(lblDate_1);
        
        JLabel lblNewLabel_20 = new JLabel("Logout");
        lblNewLabel_20.addMouseListener(new MouseAdapter() {
        		@Override
        		public void mouseClicked(MouseEvent e) {
        			login.setVisible(true);
        			Anonymousforum.setVisible(false);
        		}
        		@Override
        		public void mouseEntered(MouseEvent e) 
        		{
        			lblNewLabel_20.setIcon(logoutR);
        			lblNewLabel_20.setForeground(Color.RED);
        		}
        		@Override
        		public void mouseExited(MouseEvent e) 
        		{
        			lblNewLabel_20.setIcon(logoutW);
        			lblNewLabel_20.setForeground(Color.WHITE);
        		}
        });
        lblNewLabel_20.setIcon(logoutW);
        lblNewLabel_20.setForeground(Color.WHITE);
        lblNewLabel_20.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel_20.setBounds(719, 559, 74, 31);
        Anonymousforum.add(lblNewLabel_20);
        panel_20.setVisible(false);
        
        panel_25.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		panel_25.setBackground(new Color(238,238,238));
        		panel_23.setBackground(Color.WHITE);
        	}
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		panel_20.setVisible(true);
        	}
        });
        panel_23.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		panel_23.setBackground(new Color(238,238,238));
        		panel_25.setBackground(Color.WHITE);
        	}
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		panel_20.setVisible(true);
        	}
        });
        
      //--------------------------RecoveryCode form-----------------
        jPanel1.add(RecoveryCode, "card8");
        RecoveryCode.setLayout(null);
        RecoveryCode.setBackground(BLACKBLUE);
        
        JPanel panel_6 = new JPanel();
        panel_6.setForeground(Color.WHITE);
        panel_6.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.GRAY));
        panel_6.setBounds(200, 200, 400, 200);
        RecoveryCode.add(panel_6);
        panel_6.setLayout(null);
        panel_6.setBackground(new Color(20,35,45));
        
        lblRecoveryCode = new JLabel("Recovery code");
        lblRecoveryCode.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblRecoveryCode.setForeground(Color.WHITE);
        lblRecoveryCode.setBounds(100, 23, 186, 30);
        lblRecoveryCode.setIcon(dot);
        panel_6.add(lblRecoveryCode);
        
        textField_1 = new JTextField();
        textField_1.setBounds(100, 79, 200, 36);
        panel_6.add(textField_1);
        textField_1.setColumns(10);
        
        lblSubmit_1 = new JLabel("Submit");
        lblSubmit_1.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lblSubmit_1.setForeground(Color.CYAN);
        		lblSubmit_1.setIcon(loginC);
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		lblSubmit_1.setForeground(Color.WHITE);
        		lblSubmit_1.setIcon(loginW);
        	}
        });
        lblSubmit_1.setForeground(Color.WHITE);
        lblSubmit_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblSubmit_1.setBounds(100, 127, 105, 47);
        lblSubmit_1.setIcon(loginW);
        panel_6.add(lblSubmit_1);
        
//-------------------------Error form-------------------------------------------
        jPanel1.add(error, "card9");
        error.setLayout(null);
        error.setBackground(BLACKBLUE);
        JPanel panel_8 = new JPanel();
        panel_8.setForeground(Color.WHITE);
        panel_8.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.GRAY));
        panel_8.setBounds(200, 200, 400, 200);
        error.add(panel_8);
        panel_8.setLayout(null);
        panel_8.setBackground(new Color(255,255,255));
        
        lblNewLabel_23 = new JLabel("ERROR");
        lblNewLabel_23.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblNewLabel_23.setBounds(137, 77, 146, 40);
        lblNewLabel_23.setIcon(err);
        panel_8.add(lblNewLabel_23);
              
        pack(); 
    }


    private void btn_loginMousePressed(MouseEvent evt) {
		login.setVisible(false);
		btn_login.setVisible(false);
		loader.setVisible(true);
		
		AccountLoginWindow event=new AccountLoginWindow();
		event.setEmail(txtEmail.getText());
		event.setPassword(textField.getText());
		event.setServerAddress(txtServerIpAdress.getText());
		event.setUsername(txtusername.getText());
		this.guiToClient.offer(event);
		
		Timer t=new Timer();
		TimerTask task=new MyTask();
		t.schedule(task,5000);
    }
    
    private void btn_loginMousePressed3(MouseEvent evt) {
		login.setVisible(false);
		btn_login.setVisible(false);
		forum.setVisible(false);
		loader.setVisible(true);
		
		Timer t=new Timer();
		TimerTask task=new MyTask2();
		t.schedule(task,5000);
    }
    
    private void btn_loginMousePressed2(MouseEvent evt) {
		loader.setVisible(false);
		login.setVisible(false);
		btn_login.setVisible(false);
		forum.setVisible(true);
    }
    
    private void lblAccedi_loginMousePressed(MouseEvent evt) {
    		loader.setVisible(false);
    		registration.setVisible(false);
    		forgot.setVisible(false);
    		login.setVisible(true);
    }
    
    private void forgot_loginMousePressed(MouseEvent evt) {
    		loader.setVisible(false);
    		registration.setVisible(false);
    		login.setVisible(false);
    		forgot.setVisible(true);
    }
    
    private void lbl_closeMousePressed(java.awt.event.MouseEvent evt) {
        ShutDown event=new ShutDown();
        event.setErrExit(false);
        this.guiToClient.offer(event);
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
    /**public void updatePanelSize() { 

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        float monitorWidth = gd.getDisplayMode().getWidth();
        float monitorHeight = gd.getDisplayMode().getHeight();

        // Aspect ratio of the monitor in decimal form.
        float monitorRatio = monitorWidth / monitorHeight;

        JComponent parent = (JComponent) getParent();
        float width = parent.getWidth();
        float height = parent.getHeight();

        width = Math.min(width, height * monitorRatio);
        height = width / monitorRatio;
        setPreferredSize(new Dimension((int)width - (16 * 10), (int)height - (9 * 10)));
    }**/
    public class MyTask extends TimerTask
    {
    		public void run() {
    			loader.setVisible(false);
            forum.setVisible(true);
    		}
    }
    
    public class MyTask2 extends TimerTask
    {
    		public void run() {
    			loader.setVisible(false);
            Anonymousforum.setVisible(true);
    		}
    }
}



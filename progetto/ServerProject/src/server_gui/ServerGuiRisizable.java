package server_gui;

import java.util.Objects;
import keeptoo.KGradientPanel;
import utility.AddressIp;
import utility.MyScrollBar;
import utility.MyScrollPaneLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.MouseMotionAdapter;
import java.awt.CardLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class ServerGuiRisizable extends JFrame {

	private static final Color BLACKBLUE = new Color(23, 35, 51);

	private JPanel contentPane;
	private int xx, xy;
	private int Px, Py;
	private int section = 1;
	private int size_sidePnl = 244;
	final int resizeBorder_size = 5;
	private int titleSize = 37;
	private int textSize = 18;
	final private ImageIcon minB;
	final private ImageIcon minL;
	final private ImageIcon clsB;
	final private ImageIcon clsL;
	final private ImageIcon shdwnL;
	final private ImageIcon shdwnR;
	final private ImageIcon dotBlack;
	final private ImageIcon dotWhite;
	final private ImageIcon connected;
	final private ImageIcon disconnected;
	final private ImageIcon reduceWhite;
	final private ImageIcon reduceGray;
	final private ImageIcon growWhite;
	final private ImageIcon growGray;

	final private Cursor N_RESIZE_CURSOR = new Cursor(Cursor.N_RESIZE_CURSOR);
	final private Cursor NW_RESIZE_CURSOR = new Cursor(Cursor.NW_RESIZE_CURSOR);
	final private Cursor E_RESIZE_CURSOR = new Cursor(Cursor.E_RESIZE_CURSOR);
	private JPanel ind_3;
	private JPanel ind_2;
	private JPanel ind_1;
	private JPanel sideBtn_3;
	private JPanel sideBtn_2;
	private JPanel sideBtn_1;
	private JPanel main_panel;
	private CardLayout cl;
	private JPanel side_panel;
	private JLabel label_10;
	private JLabel label_11;
	private JPanel panel_16;
	private JPanel panel_19;
	private JPanel panel_18;
	private JPanel panel_20;
	private JLabel label;
	private JLabel lblI;
	private JLabel label_1;
	private JPanel conteiner_btn;
	private JLabel lblC;
	private JLabel lblS;
	private JLabel lblProgettoPcad;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				ServerGuiRisizable frame = new ServerGuiRisizable();
				frame.setMinimumSize(new Dimension(780, 420));
				frame.setUndecorated(true);
				frame.update();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void update() {
		setIpAddress();
	}

	public void serverIsActive(boolean active) {
		if (active) {

		} else {

		}
	}

	/**
	 * Create the frame.
	 */
	public ServerGuiRisizable() {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		clsL = new ImageIcon(Objects.requireNonNull(classLoader.getResource("CloseLight_28px.png")));
		clsB = new ImageIcon(Objects.requireNonNull(classLoader.getResource("CloseDark_28px.png")));

		minL = new ImageIcon(Objects.requireNonNull(classLoader.getResource("MinimizeLight_28px.png")));
		minB = new ImageIcon(Objects.requireNonNull(classLoader.getResource("MinimizeDark_28px.png")));

		shdwnL = new ImageIcon(Objects.requireNonNull(classLoader.getResource("ShutdownLight_28px.png")));
		shdwnR = new ImageIcon(Objects.requireNonNull(classLoader.getResource("ShutdownRed_28px.png")));

		dotBlack = new ImageIcon(Objects.requireNonNull(classLoader.getResource("DotDark_26px.png")));
		dotWhite = new ImageIcon(Objects.requireNonNull(classLoader.getResource("DotLight_15px.png")));

		connected = new ImageIcon(Objects.requireNonNull(classLoader.getResource("Connected_20px.png")));
		disconnected = new ImageIcon(Objects.requireNonNull(classLoader.getResource("Disconnected_20px.png")));

		reduceWhite = new ImageIcon(Objects.requireNonNull(classLoader.getResource("Double_Left_White_32px.png")));
		reduceGray = new ImageIcon(Objects.requireNonNull(classLoader.getResource("Double_Left_Gray_32px.png")));

		// growGray = new
		// ImageIcon(Objects.requireNonNull(classLoader.getResource("Double_Right_Gray_32px.png")));
		growGray = new ImageIcon(Objects.requireNonNull(classLoader.getResource("icons8_Menu_30px.png")));
		growWhite = new ImageIcon(Objects.requireNonNull(classLoader.getResource("Double_Right_White_32px.png")));

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 1270, 705);
		contentPane = new JPanel();
		contentPane.setMinimumSize(new Dimension(5000, 10));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel top_panel = new JPanel();
		top_panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				EventQueue.invokeLater(() -> {
					Point p = ServerGuiRisizable.this.getLocationOnScreen();
					p.x = p.x + arg0.getX() - xx;
					p.y = p.y + arg0.getY() - xy;
					ServerGuiRisizable.this.setLocation(p);
				});
			}
		});
		top_panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				EventQueue.invokeLater(() -> {
					xx = arg0.getX();
					xy = arg0.getY();
				});

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					Point p = ServerGuiRisizable.this.getLocationOnScreen();
					if (Math.abs(p.x) < 12)
						p.x = 0;
					if (Math.abs(p.y) < 12)
						p.y = 0;
					ServerGuiRisizable.this.setLocation(p);
				});
			}
		});
		top_panel.setAlignmentY(Component.TOP_ALIGNMENT);
		top_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		top_panel.setBackground(BLACKBLUE);
		top_panel.setPreferredSize(new Dimension(10, 40));
		contentPane.add(top_panel, BorderLayout.NORTH);
		top_panel.setLayout(new BorderLayout(0, 0));

		KGradientPanel gradientPanel = new KGradientPanel();
		gradientPanel.setPreferredSize(new Dimension(520, 40));
		gradientPanel.setkEndColor(Color.BLACK);
		gradientPanel.setkStartColor(BLACKBLUE);
		top_panel.add(gradientPanel, BorderLayout.EAST);
		gradientPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel_2.setPreferredSize(new Dimension(150, 10));
		panel_2.setOpaque(false);
		gradientPanel.add(panel_2, BorderLayout.EAST);
		panel_2.setLayout(null);

		JLabel btn_shdw = new JLabel("");
		btn_shdw.setVerticalAlignment(SwingConstants.BOTTOM);
		btn_shdw.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				EventQueue.invokeLater(() -> {
					System.exit(0);
				});
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					btn_shdw.setIcon(shdwnR);
				});
			}

			@Override
			public void mouseExited(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					btn_shdw.setIcon(shdwnL);
				});
			}
		});
		btn_shdw.setIcon(shdwnL);
		btn_shdw.setHorizontalAlignment(SwingConstants.CENTER);
		btn_shdw.setBackground(new Color(231, 17, 35));
		btn_shdw.setBounds(118, 0, 32, 32);
		panel_2.add(btn_shdw);

		JLabel btn_exit = new JLabel("");
		btn_exit.setHorizontalAlignment(SwingConstants.CENTER);
		btn_exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					System.exit(0);
				});
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					btn_exit.setOpaque(true);
					btn_exit.setIcon(clsB);
				});
			}

			@Override
			public void mouseExited(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					btn_exit.setOpaque(false);
					btn_exit.setIcon(clsL);
				});
			}
		});
		btn_exit.setIcon(clsL);
		btn_exit.setVerticalAlignment(SwingConstants.BOTTOM);
		btn_exit.setBackground(Color.WHITE);
		btn_exit.setBounds(91, 0, 26, 32);
		panel_2.add(btn_exit);

		JLabel btn_minze = new JLabel("");
		btn_minze.setHorizontalAlignment(SwingConstants.CENTER);
		btn_minze.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				EventQueue.invokeLater(() -> {
					ServerGuiRisizable.this.setState(Frame.ICONIFIED);
				});
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					btn_minze.setOpaque(true);
					btn_minze.setIcon(minB);
				});
			}

			@Override
			public void mouseExited(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					btn_minze.setOpaque(false);
					btn_minze.setIcon(minL);
				});
			}
		});
		btn_minze.setIcon(minL);
		btn_minze.setVerticalAlignment(SwingConstants.BOTTOM);
		btn_minze.setBackground(Color.WHITE);
		btn_minze.setBounds(62, 0, 26, 32);
		panel_2.add(btn_minze);

		JPanel panel_12 = new JPanel();
		panel_12.setPreferredSize(new Dimension(300, 10));
		panel_12.setOpaque(false);
		top_panel.add(panel_12, BorderLayout.WEST);
		panel_12.setLayout(null);

		lblI = new JLabel("");
		lblI.setVisible(false);
		lblI.setIcon(new ImageIcon(Objects.requireNonNull(classLoader.getResource("logo_32.png"))));
		lblI.setHorizontalAlignment(SwingConstants.CENTER);
		lblI.setBounds(0, 0, 56, 40);
		panel_12.add(lblI);
		
		lblProgettoPcad = new JLabel("PCAD Project - Server");
		lblProgettoPcad.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblProgettoPcad.setForeground(Color.WHITE);
		lblProgettoPcad.setBounds(12, 0, 150, 40);
		panel_12.add(lblProgettoPcad);

		side_panel = new JPanel();
		side_panel.setBorder(null);
		side_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		side_panel.setPreferredSize(new Dimension(size_sidePnl, 10));
		side_panel.setBackground(BLACKBLUE);
		contentPane.add(side_panel, BorderLayout.WEST);
		side_panel.setLayout(null);

		label = new JLabel("");
		label.setIcon(new ImageIcon(Objects.requireNonNull(classLoader.getResource("logo_128.png"))));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.LIGHT_GRAY);
		label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 32));
		label.setBackground(Color.WHITE);
		label.setBounds(0, 0, 195, 129);
		side_panel.add(label);

		conteiner_btn = new JPanel();
		conteiner_btn.setBounds(0, 159, 244, 175);
		conteiner_btn.setOpaque(false);
		side_panel.add(conteiner_btn);
		conteiner_btn.setLayout(null);

		sideBtn_1 = new JPanel();
		sideBtn_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					if (section == 1)
						return;
					section = 1;
					setColor(sideBtn_1);
					ind_1.setOpaque(true);
					resetColor(new JPanel[] { sideBtn_2, sideBtn_3 }, new JPanel[] { ind_2, ind_3 });
					cl.show(main_panel, "serverInfo");
				});
			}
		});
		sideBtn_1.setLayout(null);
		sideBtn_1.setBackground(new Color(41, 57, 80));
		sideBtn_1.setBounds(0, 0, 244, 44);
		conteiner_btn.add(sideBtn_1);

		JLabel sideImg_1 = new JLabel("");
		sideImg_1.setIcon(new ImageIcon(Objects.requireNonNull(classLoader.getResource("InfoLight_28px.png"))));
		sideImg_1.setHorizontalAlignment(SwingConstants.CENTER);
		sideImg_1.setBounds(10, 0, 44, 42);
		sideBtn_1.add(sideImg_1);

		JLabel lblFstsection = new JLabel("Server Info");
		lblFstsection.setForeground(Color.LIGHT_GRAY);
		lblFstsection.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFstsection.setBounds(70, 0, 203, 42);
		sideBtn_1.add(lblFstsection);

		ind_1 = new JPanel();
		ind_1.setBounds(0, 0, 5, 44);
		sideBtn_1.add(ind_1);

		sideBtn_2 = new JPanel();
		sideBtn_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					if (section == 2)
						return;
					section = 2;
					setColor(sideBtn_2);
					ind_2.setOpaque(true);
					resetColor(new JPanel[] { sideBtn_1, sideBtn_3 }, new JPanel[] { ind_1, ind_3 });
					cl.show(main_panel, "clientInfo");
				});
			}
		});
		sideBtn_2.setLayout(null);
		sideBtn_2.setBackground(new Color(23, 35, 51));
		sideBtn_2.setBounds(0, 57, 244, 44);
		conteiner_btn.add(sideBtn_2);

		JLabel sideImg_2 = new JLabel("");
		sideImg_2.setIcon(new ImageIcon(Objects.requireNonNull(classLoader.getResource("PeopleLight_28px.png"))));
		sideImg_2.setHorizontalAlignment(SwingConstants.CENTER);
		sideImg_2.setBounds(10, 0, 44, 42);
		sideBtn_2.add(sideImg_2);

		JLabel lblScndsection = new JLabel("Client Info");
		lblScndsection.setForeground(Color.LIGHT_GRAY);
		lblScndsection.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblScndsection.setBounds(70, 0, 203, 42);
		sideBtn_2.add(lblScndsection);

		ind_2 = new JPanel();
		ind_2.setOpaque(false);
		ind_2.setBounds(0, 0, 5, 44);
		sideBtn_2.add(ind_2);

		sideBtn_3 = new JPanel();
		sideBtn_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					if (section == 3)
						return;
					section = 3;
					setColor(sideBtn_3);
					ind_3.setOpaque(true);
					resetColor(new JPanel[] { sideBtn_1, sideBtn_2 }, new JPanel[] { ind_1, ind_2 });
					cl.show(main_panel, "topicInfo");
				});
			}
		});
		sideBtn_3.setLayout(null);
		sideBtn_3.setBackground(new Color(23, 35, 51));
		sideBtn_3.setBounds(0, 114, 244, 44);
		conteiner_btn.add(sideBtn_3);

		JLabel sideImg_3 = new JLabel("");
		sideImg_3.setIcon(new ImageIcon(Objects.requireNonNull(classLoader.getResource("QuestionLight_28px.png"))));
		sideImg_3.setHorizontalAlignment(SwingConstants.CENTER);
		sideImg_3.setBounds(10, 0, 44, 42);
		sideBtn_3.add(sideImg_3);

		JLabel lblThrdsection = new JLabel("Topic");
		lblThrdsection.setForeground(Color.LIGHT_GRAY);
		lblThrdsection.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblThrdsection.setBounds(70, 0, 203, 42);
		sideBtn_3.add(lblThrdsection);

		ind_3 = new JPanel();
		ind_3.setOpaque(false);
		ind_3.setBounds(0, 0, 5, 44);
		sideBtn_3.add(ind_3);

		JPanel panel_9 = new JPanel();
		panel_9.setBackground(Color.LIGHT_GRAY);
		panel_9.setBounds(8, 50, 224, 1);
		conteiner_btn.add(panel_9);

		JPanel panel_10 = new JPanel();
		panel_10.setBackground(Color.LIGHT_GRAY);
		panel_10.setBounds(8, 107, 224, 1);
		conteiner_btn.add(panel_10);

		JLabel label_30 = new JLabel("");
		label_30.setVerticalAlignment(SwingConstants.TOP);
		label_30.setIcon(reduceGray);
		label_30.setHorizontalAlignment(SwingConstants.RIGHT);
		label_30.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				EventQueue.invokeLater(() -> {
					Dimension d = side_panel.getSize();
					d.width = 60;
					side_panel.setPreferredSize(d);
					side_panel.setSize(d);
					d = panel_19.getSize();
					d.width = 60;
					panel_19.setPreferredSize(d);
					panel_19.setSize(d);
					d = panel_20.getSize();
					d.width = 60;
					panel_20.setPreferredSize(d);
					panel_20.setSize(d);
					label.setVisible(false);
					lblI.setVisible(true);
					label_1.setVisible(true);
					conteiner_btn.setLocation(0, 62);
					lblProgettoPcad.setLocation(60, 0);
				});
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					label_30.setIcon(reduceWhite);
				});
			}

			@Override
			public void mouseExited(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					label_30.setIcon(reduceGray);
				});
			}
		});
		label_30.setFont(new Font("High Tower Text", Font.BOLD, 26));
		label_30.setForeground(Color.WHITE);
		label_30.setBounds(195, 0, 49, 46);
		side_panel.add(label_30);

		label_1 = new JLabel("");
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					Dimension d = side_panel.getSize();
					d.width = size_sidePnl;
					side_panel.setPreferredSize(d);
					side_panel.setSize(d);
					d = panel_19.getSize();
					d.width = size_sidePnl;
					panel_19.setPreferredSize(d);
					panel_19.setSize(d);
					d = panel_20.getSize();
					d.width = size_sidePnl;
					panel_20.setPreferredSize(d);
					panel_20.setSize(d);
					label.setVisible(false);
					label_1.setVisible(false);
					lblI.setVisible(false);
					label.setVisible(true);
					conteiner_btn.setLocation(0, 159);
					lblProgettoPcad.setLocation(12, 0);
				});
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					label_1.setIcon(growWhite);
				});
			}

			@Override
			public void mouseExited(MouseEvent e) {
				EventQueue.invokeLater(() -> {
					label_1.setIcon(growGray);
				});
			}
		});
		label_1.setVisible(false);
		label_1.setIcon(growGray);
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setForeground(Color.WHITE);
		label_1.setFont(new Font("High Tower Text", Font.BOLD, 26));
		label_1.setBounds(0, 20, 57, 32);
		side_panel.add(label_1);

		JPanel border_right = new JPanel();
		border_right.setCursor(E_RESIZE_CURSOR);
		border_right.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				EventQueue.invokeLater(() -> {
					int x = arg0.getX() - Px;
					Dimension d = ServerGuiRisizable.this.getSize();
					ServerGuiRisizable.this.setSize(d.width + x, d.height);
				});
			}
		});
		border_right.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				EventQueue.invokeLater(() -> {
					Px = arg0.getX();
				});
			}
		});
		border_right.setPreferredSize(new Dimension(3, 10));
		border_right.setAlignmentX(Component.RIGHT_ALIGNMENT);
		contentPane.add(border_right, BorderLayout.EAST);

		main_panel = new JPanel();
		main_panel.setBackground(Color.WHITE);
		main_panel.setAlignmentY(Component.TOP_ALIGNMENT);
		main_panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		contentPane.add(main_panel, BorderLayout.CENTER);
		main_panel.setLayout(new CardLayout(0, 0));

		JScrollPane scrollPanel_serverInfo = new JScrollPane();
		scrollPanel_serverInfo.setComponentZOrder(scrollPanel_serverInfo.getVerticalScrollBar(), 0);
		scrollPanel_serverInfo.setComponentZOrder(scrollPanel_serverInfo.getViewport(), 1);
		scrollPanel_serverInfo.getVerticalScrollBar().setOpaque(false);
		scrollPanel_serverInfo.setBorder(null);
		scrollPanel_serverInfo.setOpaque(false);
		scrollPanel_serverInfo.setLayout(new MyScrollPaneLayout());
		scrollPanel_serverInfo.getVerticalScrollBar().setUI(new MyScrollBar());
		main_panel.add(scrollPanel_serverInfo, "serverInfo");

		JPanel panel_6 = new JPanel();
		panel_6.setPreferredSize(new Dimension(830, 460));
		panel_6.setBackground(Color.WHITE);
		scrollPanel_serverInfo.setViewportView(panel_6);

		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setOpaque(false);

		JLabel label_2 = new JLabel("");
		label_2.setIcon(dotBlack);
		label_2.setBounds(0, 24, 26, 26);
		panel_7.add(label_2);

		JLabel label_3 = new JLabel("General information:");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 37));
		label_3.setBounds(30, 0, 345, 70);
		panel_7.add(label_3);

		KGradientPanel gradientPanel_1 = new KGradientPanel();
		gradientPanel_1.kStartColor = Color.LIGHT_GRAY;
		gradientPanel_1.setkStartColor(Color.LIGHT_GRAY);
		gradientPanel_1.kGradientFocus = 550;
		gradientPanel_1.setkGradientFocus(550);
		gradientPanel_1.kEndColor = Color.WHITE;
		gradientPanel_1.setkEndColor(Color.WHITE);
		gradientPanel_1.setBounds(2, 63, 800, 3);
		panel_7.add(gradientPanel_1);

		JLabel label_4 = new JLabel("Server name:");
		label_4.setVerticalAlignment(SwingConstants.TOP);
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_4.setBounds(64, 76, 123, 29);
		panel_7.add(label_4);

		JLabel label_5 = new JLabel("Tempo di attivit\u00E0:");
		label_5.setVerticalAlignment(SwingConstants.TOP);
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_5.setBounds(64, 110, 257, 29);
		panel_7.add(label_5);

		JLabel label_6 = new JLabel("");
		label_6.setIcon(dotWhite);
		label_6.setBounds(46, 114, 16, 16);
		panel_7.add(label_6);

		JLabel label_7 = new JLabel("");
		label_7.setIcon(dotWhite);
		label_7.setBounds(46, 80, 16, 16);
		panel_7.add(label_7);

		JPanel panel_8 = new JPanel();
		panel_8.setLayout(null);
		panel_8.setOpaque(false);

		JLabel label_8 = new JLabel("");
		label_8.setIcon(dotBlack);
		label_8.setBounds(0, 24, 26, 26);
		panel_8.add(label_8);

		JLabel label_9 = new JLabel("Network informatons:");
		label_9.setFont(new Font("Tahoma", Font.PLAIN, 37));
		label_9.setBounds(30, 0, 361, 70);
		panel_8.add(label_9);

		JPanel panel_11 = new JPanel();
		panel_11.setBackground(Color.LIGHT_GRAY);
		panel_11.setBounds(2, 60, 480, 3);
		panel_8.add(panel_11);

		label_10 = new JLabel();
		label_10.setVerticalAlignment(SwingConstants.TOP);
		label_10.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_10.setBounds(201, 76, 260, 29);
		panel_8.add(label_10);

		label_11 = new JLabel();
		label_11.setVerticalAlignment(SwingConstants.TOP);
		label_11.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_11.setBounds(201, 109, 227, 29);
		panel_8.add(label_11);

		JLabel label_12 = new JLabel("Pubblic IPv4:");
		label_12.setVerticalAlignment(SwingConstants.TOP);
		label_12.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_12.setBounds(64, 109, 113, 29);
		panel_8.add(label_12);

		JLabel label_13 = new JLabel("Local IPv4:");
		label_13.setVerticalAlignment(SwingConstants.TOP);
		label_13.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_13.setBounds(64, 76, 113, 29);
		panel_8.add(label_13);

		JLabel label_14 = new JLabel("");
		label_14.setIcon(dotWhite);
		label_14.setBounds(46, 79, 16, 16);
		panel_8.add(label_14);

		JLabel label_15 = new JLabel("");
		label_15.setIcon(dotWhite);
		label_15.setBounds(46, 113, 16, 16);
		panel_8.add(label_15);

		JPanel panel_13 = new JPanel();
		panel_13.setLayout(null);
		panel_13.setOpaque(false);

		JPanel panel_14 = new JPanel();
		panel_14.setBackground(Color.LIGHT_GRAY);
		panel_14.setBounds(2, 60, 330, 3);
		panel_13.add(panel_14);

		JLabel label_16 = new JLabel("Number of topics:");
		label_16.setVerticalAlignment(SwingConstants.TOP);
		label_16.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_16.setBounds(64, 76, 150, 29);
		panel_13.add(label_16);

		JLabel label_17 = new JLabel("number of posts:");
		label_17.setVerticalAlignment(SwingConstants.TOP);
		label_17.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_17.setBounds(64, 109, 150, 29);
		panel_13.add(label_17);

		JLabel label_18 = new JLabel("");
		label_18.setIcon(dotWhite);
		label_18.setBounds(46, 112, 16, 16);
		panel_13.add(label_18);

		JLabel label_19 = new JLabel("");
		label_19.setIcon(dotWhite);
		label_19.setBounds(46, 79, 16, 16);
		panel_13.add(label_19);

		JPanel panel_15 = new JPanel();
		panel_15.setBackground(Color.LIGHT_GRAY);
		panel_15.setBounds(437, 60, 330, 3);
		panel_13.add(panel_15);

		JLabel label_20 = new JLabel("");
		label_20.setIcon(dotBlack);
		label_20.setBounds(0, 24, 26, 26);
		panel_13.add(label_20);

		JLabel label_21 = new JLabel("");
		label_21.setIcon(dotBlack);
		label_21.setBounds(435, 24, 26, 26);
		panel_13.add(label_21);

		JLabel label_22 = new JLabel("0");
		label_22.setVerticalAlignment(SwingConstants.TOP);
		label_22.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_22.setBounds(216, 76, 227, 29);
		panel_13.add(label_22);

		JLabel label_23 = new JLabel("0");
		label_23.setVerticalAlignment(SwingConstants.TOP);
		label_23.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_23.setBounds(216, 109, 227, 29);
		panel_13.add(label_23);

		JLabel label_24 = new JLabel("Client online:");
		label_24.setVerticalAlignment(SwingConstants.TOP);
		label_24.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_24.setBounds(475, 81, 150, 24);
		panel_13.add(label_24);

		JLabel label_25 = new JLabel("Registered:");
		label_25.setVerticalAlignment(SwingConstants.TOP);
		label_25.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_25.setBounds(502, 113, 84, 19);
		panel_13.add(label_25);

		JLabel label_26 = new JLabel("Anonymous:");
		label_26.setVerticalAlignment(SwingConstants.TOP);
		label_26.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_26.setBounds(502, 135, 99, 19);
		panel_13.add(label_26);

		JLabel label_27 = new JLabel("Topics:");
		label_27.setFont(new Font("Tahoma", Font.PLAIN, 37));
		label_27.setBounds(30, 0, 123, 70);
		panel_13.add(label_27);

		JLabel label_28 = new JLabel("Clients:");
		label_28.setFont(new Font("Tahoma", Font.PLAIN, 37));
		label_28.setBounds(465, 0, 132, 70);
		panel_13.add(label_28);
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_6
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_13, GroupLayout.PREFERRED_SIZE, 789, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_6.createSequentialGroup()
								.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING, false)
										.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
										.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 728,
												GroupLayout.PREFERRED_SIZE))
								.addContainerGap(168, Short.MAX_VALUE)))));
		gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_6.createSequentialGroup().addContainerGap()
						.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_13, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(49, Short.MAX_VALUE)));
		panel_6.setLayout(gl_panel_6);

		JScrollPane scrollPane_clientInfo = new JScrollPane();
		scrollPane_clientInfo.setLayout(new MyScrollPaneLayout());
		scrollPane_clientInfo.getVerticalScrollBar().setUI(new MyScrollBar());
		scrollPane_clientInfo.getVerticalScrollBar().setOpaque(false);

		/*
		 * scrollPanel_serverInfo.setComponentZOrder(scrollPanel_serverInfo.
		 * getVerticalScrollBar(), 0);
		 * scrollPanel_serverInfo.setComponentZOrder(scrollPanel_serverInfo.getViewport( * ), 1); 
		 * scrollPanel_serverInfo.setBorder(null);
		 * scrollPanel_serverInfo.setOpaque(false);
		 */
		main_panel.add(scrollPane_clientInfo, "clientInfo");

		JPanel panel_21 = new JPanel();
		panel_21.setBackground(Color.WHITE);
		scrollPane_clientInfo.setViewportView(panel_21);

		JScrollPane scrollPane_topicInfo = new JScrollPane();
		/*
		 * scrollPanel_serverInfo.setComponentZOrder(scrollPanel_serverInfo.
		 * getVerticalScrollBar(), 0);
		 * scrollPanel_serverInfo.setComponentZOrder(scrollPanel_serverInfo.getViewport(
		 * ), 1); scrollPanel_serverInfo.setBorder(null);
		 * scrollPanel_serverInfo.setOpaque(false);
		 */
		scrollPane_topicInfo.getVerticalScrollBar().setOpaque(false);
		scrollPane_topicInfo.setLayout(new MyScrollPaneLayout());
		scrollPane_topicInfo.getVerticalScrollBar().setUI(new MyScrollBar());
		main_panel.add(scrollPane_topicInfo, "topicInfo");

		JPanel panel_22 = new JPanel();
		panel_22.setBackground(Color.WHITE);
		scrollPane_topicInfo.setViewportView(panel_22);

		JPanel border_down = new JPanel();
		border_down.setPreferredSize(new Dimension(10, 25));
		border_down.setBorder(null);
		border_down.setAlignmentX(Component.LEFT_ALIGNMENT);
		border_down.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		contentPane.add(border_down, BorderLayout.SOUTH);
		border_down.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setCursor(N_RESIZE_CURSOR);
		panel.setPreferredSize(new Dimension(10, 3));
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				EventQueue.invokeLater(() -> {
					int y = arg0.getY() - Py;
					Dimension d = ServerGuiRisizable.this.getSize();
					ServerGuiRisizable.this.setSize(d.width, d.height + y);
				});
			}
		});
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				EventQueue.invokeLater(() -> {
					Py = arg0.getY();
				});
			}
		});
		border_down.add(panel, BorderLayout.SOUTH);

		JPanel panel_4 = new JPanel();
		panel_4.setOpaque(false);
		panel_4.setCursor(NW_RESIZE_CURSOR);
		panel_4.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				EventQueue.invokeLater(() -> {
					int x = arg0.getX() - Px;
					int y = arg0.getY() - Py;
					Dimension d = ServerGuiRisizable.this.getSize();
					ServerGuiRisizable.this.setSize(d.width + x, d.height + y);
				});
			}
		});
		panel_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				EventQueue.invokeLater(() -> {
					Px = arg0.getX();
					Py = arg0.getY();
				});
			}
		});

		panel_18 = new JPanel();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
						.addComponent(panel_18, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 1005, Short.MAX_VALUE)
						.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 4, GroupLayout.PREFERRED_SIZE).addGap(0)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(panel_18, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE));
		panel_18.setLayout(null);

		panel_20 = new JPanel();
		panel_20.setBackground(BLACKBLUE);
		panel_20.setBounds(0, 0, size_sidePnl, 3);
		panel_18.add(panel_20);
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		panel_1.setOpaque(true);
		panel_1.setCursor(E_RESIZE_CURSOR);
		panel_1.setPreferredSize(new Dimension(3, 10));
		panel_1.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				EventQueue.invokeLater(() -> {
					int x = arg0.getX() - Px;
					Dimension d = ServerGuiRisizable.this.getSize();
					ServerGuiRisizable.this.setSize(d.width + x, d.height);
				});
			}
		});
		panel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				EventQueue.invokeLater(() -> {
					Px = arg0.getX();
				});
			}
		});
		border_down.add(panel_1, BorderLayout.EAST);

		JPanel panel_3 = new JPanel();
		border_down.add(panel_3, BorderLayout.CENTER);

		JPanel panel_5 = new JPanel();

		panel_16 = new JPanel();
		panel_16.setOpaque(false);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
						.addComponent(panel_16, GroupLayout.PREFERRED_SIZE, size_sidePnl, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 676, Short.MAX_VALUE)
						.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addComponent(panel_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
				.addComponent(panel_16, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE));
		panel_16.setLayout(null);

		panel_19 = new JPanel();
		panel_19.setBounds(0, 0, size_sidePnl, 22);
		panel_19.setBackground(BLACKBLUE);
		panel_16.add(panel_19);
		panel_5.setLayout(null);

		JLabel label_29 = new JLabel("");
		label_29.setIcon(new ImageIcon(Objects.requireNonNull(classLoader.getResource("Settings_22px.png"))));
		label_29.setBounds(167, 1, 22, 22);
		panel_5.add(label_29);

		JPanel panel_17 = new JPanel();
		panel_17.setBackground(Color.DARK_GRAY);
		panel_17.setBounds(162, 2, 1, 22);
		panel_5.add(panel_17);

		lblC = new JLabel("");
		lblC.setVerticalAlignment(SwingConstants.BOTTOM);
		lblC.setHorizontalAlignment(SwingConstants.CENTER);
		lblC.setBounds(133, 0, 22, 22);
		panel_5.add(lblC);

		JLabel lblStatus = new JLabel("Server state:");
		lblStatus.setFont(new Font("Consolas", Font.BOLD, 14));
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(0, 1, 109, 22);
		panel_5.add(lblStatus);

		lblS = new JLabel("");
		lblS.setHorizontalAlignment(SwingConstants.CENTER);
		lblS.setBounds(103, 0, 22, 22);
		panel_5.add(lblS);
		panel_3.setLayout(gl_panel_3);

		cl = (CardLayout) (main_panel.getLayout());
	}

	// UTILITY FUNCTIONS
	private void setColor(JPanel pane) {
		pane.setBackground(new Color(41, 57, 80));
	}

	private void resetColor(JPanel[] pane, JPanel[] indicators) {
		for (int i = 0; i < pane.length; i++)
			pane[i].setBackground(BLACKBLUE);
		for (int i = 0; i < indicators.length; i++)
			indicators[i].setOpaque(false);
	}

	private void setIpAddress() {
		AddressIp.updateIp();
		label_10.setText(AddressIp.getLocalIp());
		label_11.setText(AddressIp.getPublicIp());
		if (AddressIp.getLocalIp() != "Unkown") {
			lblC.setIcon(connected);
			lblC.setToolTipText("");
		} else {
			lblC.setIcon(disconnected);
			lblC.setToolTipText("No connection are available");
		}
	}
}

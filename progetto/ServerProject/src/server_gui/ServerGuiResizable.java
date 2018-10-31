package server_gui;

import keeptoo.KGradientPanel;
import server.utility.StreamRedirector;
import utility.AddressIp;
import utility.gui.MyScrollBar;
import utility.gui.MyScrollPaneLayout;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerGuiResizable extends JFrame implements ActionListener, Runnable {

    private static final Color BLACKBLUE = new Color(23, 35, 51);

    private JPanel contentPane;
    private int xx, xy;
    private int Px, Py;
    private int section = 1;
    private int size_sidePnl = 230;
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

    private ServerStatistic serverStat;
    private Timer timer = new Timer(1000, this);
    private JLabel lblTopicNumber;
    private JLabel lblPostNumber;
    private JLabel lblServerName;
    private JLabel label_22;
    private JLabel lblClientOnline;
    private JPanel sideBtn_4;
    private JPanel ind_4;
    private JTextField textField;
    private final MutableAttributeSet attributeInput;
    private final MutableAttributeSet attributeOutput;
    private final MutableAttributeSet attributeError;

    final private OutputStream executor;
    final private InputStream stdOut;
    final private InputStream stdErr;
    private Thread readerStdOut;
    private Thread readerStdErr;
    private boolean quit;
    private JTextPane textPane;
    private JPopupMenu popupMenu;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ServerStatistic serverStat = new ServerStatistic();
                serverStat.setServerInfo("test", new ConcurrentLinkedQueue<>(), "test", 0);
                ServerGuiResizable frame = new ServerGuiResizable(serverStat, System.out, null, null);
                frame.setMinimumSize(new Dimension(780, 420));
                frame.setUndecorated(true);
                frame.setTitle("TEST");
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

    /**
     * Create the frame.
     */
    public ServerGuiResizable(ServerStatistic serverStat, OutputStream executor, InputStream out, InputStream err) {
        this.serverStat = Objects.requireNonNull(serverStat);
        this.executor = Objects.requireNonNull(executor);
        this.stdOut = out;
        this.stdErr = err;

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try {
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

            growGray = new ImageIcon(Objects.requireNonNull(classLoader.getResource("icons8_Menu_30px.png")));
            growWhite = new ImageIcon(Objects.requireNonNull(classLoader.getResource("Double_Right_White_32px.png")));
        } catch (NullPointerException e) {
            throw new MissingResourceException("Impossible to load some necessary resources.", "ImageFile", "");
        }


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
                SwingUtilities.invokeLater(() -> {
                    Point p = ServerGuiResizable.this.getLocationOnScreen();
                    p.x = p.x + arg0.getX() - xx;
                    p.y = p.y + arg0.getY() - xy;
                    ServerGuiResizable.this.setLocation(p);
                });
            }
        });
        top_panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                SwingUtilities.invokeLater(() -> {
                    xx = arg0.getX();
                    xy = arg0.getY();
                });

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    Point p = ServerGuiResizable.this.getLocationOnScreen();
                    if (Math.abs(p.x) < 12)
                        p.x = 0;
                    if (Math.abs(p.y) < 12)
                        p.y = 0;
                    ServerGuiResizable.this.setLocation(p);
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
                SwingUtilities.invokeLater(() -> {
                    try {
                        executor.write("shutdown\n".getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    btn_shdw.setIcon(shdwnR);
                });
            }

            @Override
            public void mouseExited(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
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
                SwingUtilities.invokeLater(() -> {
                    System.exit(0);
                });
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    btn_exit.setOpaque(true);
                    btn_exit.setIcon(clsB);
                });
            }

            @Override
            public void mouseExited(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
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
                SwingUtilities.invokeLater(() -> {
                    ServerGuiResizable.this.setState(Frame.ICONIFIED);
                });
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    btn_minze.setOpaque(true);
                    btn_minze.setIcon(minB);
                });
            }

            @Override
            public void mouseExited(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
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

        lblProgettoPcad = new JLabel("PCAD Project - Server alpha 0.4.2");
        lblProgettoPcad.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblProgettoPcad.setForeground(Color.WHITE);
        lblProgettoPcad.setBounds(12, 0, 288, 32);
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
        conteiner_btn.setBounds(0, 159, 244, 248);
        conteiner_btn.setOpaque(false);
        side_panel.add(conteiner_btn);
        conteiner_btn.setLayout(null);

        sideBtn_1 = new JPanel();
        sideBtn_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (section == 1)
                        return;
                    section = 1;
                    setColor(sideBtn_1);
                    ind_1.setOpaque(true);
                    resetColor(new JPanel[]{sideBtn_2, sideBtn_3, sideBtn_4}, new JPanel[]{ind_2, ind_3, ind_4});
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
                SwingUtilities.invokeLater(() -> {
                    if (section == 2)
                        return;
                    section = 2;
                    setColor(sideBtn_2);
                    ind_2.setOpaque(true);
                    resetColor(new JPanel[]{sideBtn_1, sideBtn_3, sideBtn_4}, new JPanel[]{ind_1, ind_3, ind_4});
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
                SwingUtilities.invokeLater(() -> {
                    if (section == 3)
                        return;
                    section = 3;
                    setColor(sideBtn_3);
                    ind_3.setOpaque(true);
                    resetColor(new JPanel[]{sideBtn_1, sideBtn_2, sideBtn_4}, new JPanel[]{ind_1, ind_2, ind_4});
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

        sideBtn_4 = new JPanel();
        sideBtn_4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

                SwingUtilities.invokeLater(() -> {
                    if (section == 4)
                        return;
                    section = 4;
                    setColor(sideBtn_4);
                    ind_4.setOpaque(true);
                    resetColor(new JPanel[]{sideBtn_1, sideBtn_2, sideBtn_3}, new JPanel[]{ind_1, ind_2, ind_3});
                    cl.show(main_panel, "console");
                });
            }
        });
        sideBtn_4.setLayout(null);
        sideBtn_4.setBackground(new Color(23, 35, 51));
        sideBtn_4.setBounds(0, 171, 244, 44);
        conteiner_btn.add(sideBtn_4);

        JLabel label_32 = new JLabel("");
        label_32.setHorizontalAlignment(SwingConstants.CENTER);
        label_32.setIcon(new ImageIcon(Objects.requireNonNull(classLoader.getResource("Console_28px.png"))));
        label_32.setBounds(10, 0, 44, 42);
        sideBtn_4.add(label_32);

        JLabel lblConsole = new JLabel("Console");
        lblConsole.setForeground(Color.LIGHT_GRAY);
        lblConsole.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblConsole.setBounds(70, 0, 203, 42);
        sideBtn_4.add(lblConsole);

        ind_4 = new JPanel();
        ind_4.setOpaque(false);
        ind_4.setBounds(0, 0, 5, 44);
        sideBtn_4.add(ind_4);

        JPanel panel_30 = new JPanel();
        panel_30.setBackground(Color.LIGHT_GRAY);
        panel_30.setBounds(8, 164, 224, 1);
        conteiner_btn.add(panel_30);

        JLabel label_30 = new JLabel("");
        label_30.setVerticalAlignment(SwingConstants.TOP);
        label_30.setIcon(reduceGray);
        label_30.setHorizontalAlignment(SwingConstants.RIGHT);
        label_30.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                SwingUtilities.invokeLater(() -> {
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
                SwingUtilities.invokeLater(() -> {
                    label_30.setIcon(reduceWhite);
                });
            }

            @Override
            public void mouseExited(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    label_30.setIcon(reduceGray);
                });
            }
        });
        label_30.setFont(new Font("High Tower Text", Font.BOLD, 26));
        label_30.setForeground(Color.WHITE);
        label_30.setBounds(181, 0, 49, 46);
        side_panel.add(label_30);

        label_1 = new JLabel("");
        label_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
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
                SwingUtilities.invokeLater(() -> {
                    label_1.setIcon(growWhite);
                });
            }

            @Override
            public void mouseExited(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
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
                SwingUtilities.invokeLater(() -> {
                    int x = arg0.getX() - Px;
                    Dimension d = ServerGuiResizable.this.getSize();
                    ServerGuiResizable.this.setSize(d.width + x, d.height);
                });
            }
        });
        border_right.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                SwingUtilities.invokeLater(() -> {
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
        label_3.setBounds(30, 0, 431, 70);
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

        JLabel lblTime = new JLabel("Tempo di attivit\u00E0:");
        lblTime.setVerticalAlignment(SwingConstants.TOP);
        lblTime.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblTime.setBounds(64, 110, 147, 29);
        panel_7.add(lblTime);

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
        label_9.setBounds(30, 0, 452, 70);
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

        JLabel label_20 = new JLabel("");
        label_20.setIcon(dotBlack);
        label_20.setBounds(0, 24, 26, 26);
        panel_13.add(label_20);

        lblTopicNumber = new JLabel("0");
        lblTopicNumber.setVerticalAlignment(SwingConstants.TOP);
        lblTopicNumber.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblTopicNumber.setBounds(216, 76, 146, 29);
        panel_13.add(lblTopicNumber);

        lblPostNumber = new JLabel("0");
        lblPostNumber.setVerticalAlignment(SwingConstants.TOP);
        lblPostNumber.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblPostNumber.setBounds(216, 109, 149, 29);
        panel_13.add(lblPostNumber);

        JLabel label_27 = new JLabel("Topics:");
        label_27.setFont(new Font("Tahoma", Font.PLAIN, 37));
        label_27.setBounds(30, 0, 302, 70);
        panel_13.add(label_27);

        JPanel panel_23 = new JPanel();
        panel_23.setBackground(Color.WHITE);
        GroupLayout gl_panel_6 = new GroupLayout(panel_6);
        gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_6
                .createSequentialGroup().addContainerGap()
                .addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_6.createSequentialGroup()
                        .addComponent(panel_13, GroupLayout.PREFERRED_SIZE, 378, GroupLayout.PREFERRED_SIZE).addGap(106)
                        .addComponent(panel_23, GroupLayout.PREFERRED_SIZE, 502, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING, false)
                                .addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
                                .addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 728, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE)));
        gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_panel_6.createSequentialGroup().addContainerGap()
                        .addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
                                .addComponent(panel_13, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
                                .addComponent(panel_23, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(49, Short.MAX_VALUE)));

        lblServerName = new JLabel("");
        lblServerName.setVerticalAlignment(SwingConstants.TOP);
        lblServerName.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblServerName.setBounds(238, 76, 484, 29);
        panel_7.add(lblServerName);

        label_22 = new JLabel("00:00:00");
        label_22.setVerticalAlignment(SwingConstants.TOP);
        label_22.setFont(new Font("Tahoma", Font.PLAIN, 18));
        label_22.setBounds(238, 110, 103, 29);
        panel_7.add(label_22);
        panel_23.setLayout(null);

        JLabel label_24 = new JLabel("Clients:");
        label_24.setFont(new Font("Tahoma", Font.PLAIN, 37));
        label_24.setBounds(30, 0, 302, 70);
        panel_23.add(label_24);

        JLabel label_25 = new JLabel("Client online:");
        label_25.setVerticalAlignment(SwingConstants.TOP);
        label_25.setFont(new Font("Tahoma", Font.PLAIN, 18));
        label_25.setBounds(64, 76, 106, 29);
        panel_23.add(label_25);

        JPanel panel_15 = new JPanel();
        panel_15.setBackground(Color.LIGHT_GRAY);
        panel_15.setBounds(2, 60, 330, 3);
        panel_23.add(panel_15);

        lblClientOnline = new JLabel("0");
        lblClientOnline.setVerticalAlignment(SwingConstants.TOP);
        lblClientOnline.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblClientOnline.setBounds(175, 76, 150, 24);
        panel_23.add(lblClientOnline);

        JLabel label_23 = new JLabel("");
        label_23.setBounds(0, 24, 26, 26);
        label_23.setIcon(dotBlack);
        panel_23.add(label_23);

        JLabel label_31 = new JLabel("");
        label_31.setBounds(46, 79, 16, 16);
        label_31.setIcon(dotWhite);
        panel_23.add(label_31);
        panel_6.setLayout(gl_panel_6);

        JScrollPane scrollPane_clientInfo = new JScrollPane();
        scrollPane_clientInfo.setLayout(new MyScrollPaneLayout());
        scrollPane_clientInfo.getVerticalScrollBar().setUI(new MyScrollBar());
        scrollPane_clientInfo.getVerticalScrollBar().setOpaque(false);
        main_panel.add(scrollPane_clientInfo, "clientInfo");

        JPanel panel_21 = new JPanel();
        panel_21.setBorder(null);
        panel_21.setBackground(Color.WHITE);
        scrollPane_clientInfo.setViewportView(panel_21);

        JPanel panel_24 = new JPanel();
        panel_24.setBackground(Color.WHITE);
        panel_24.setBorder(new LineBorder(new Color(0, 0, 0), 3, true));

        JLabel lblWip = new JLabel("W.I.P.");
        lblWip.setHorizontalAlignment(SwingConstants.CENTER);
        lblWip.setFont(new Font("Bodoni MT", Font.BOLD, 80));
        GroupLayout gl_panel_21 = new GroupLayout(panel_21);
        gl_panel_21.setHorizontalGroup(gl_panel_21.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_21.createSequentialGroup().addGap(74)
                        .addComponent(panel_24, GroupLayout.PREFERRED_SIZE, 367, GroupLayout.PREFERRED_SIZE).addGap(49)
                        .addComponent(lblWip, GroupLayout.PREFERRED_SIZE, 301, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(352, Short.MAX_VALUE)));
        gl_panel_21.setVerticalGroup(gl_panel_21.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_21.createSequentialGroup().addGap(116)
                        .addComponent(panel_24, GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE).addGap(78))
                .addGroup(gl_panel_21.createSequentialGroup().addGap(146)
                        .addComponent(lblWip, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(311, Short.MAX_VALUE)));
        panel_24.setLayout(new BorderLayout(0, 0));

        JPanel panel_25 = new JPanel();
        panel_25.setPreferredSize(new Dimension(10, 32));
        panel_24.add(panel_25, BorderLayout.NORTH);
        panel_25.setLayout(null);

        JLabel lblNewLabel = new JLabel("Client Online:");
        lblNewLabel.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 15));
        lblNewLabel.setBounds(12, 0, 175, 32);
        panel_25.add(lblNewLabel);

        JLabel label_21 = new JLabel("0");
        label_21.setFont(new Font("Consolas", Font.PLAIN, 21));
        label_21.setHorizontalAlignment(SwingConstants.RIGHT);
        label_21.setBounds(293, 0, 56, 32);
        panel_25.add(label_21);

        JPanel panel_26 = new JPanel();
        panel_26.setBorder(new MatteBorder(1, 0, 0, 0, new Color(0, 0, 0)));
        panel_24.add(panel_26, BorderLayout.CENTER);
        panel_21.setLayout(gl_panel_21);

        JScrollPane scrollPane_topicInfo = new JScrollPane();
        scrollPane_topicInfo.getVerticalScrollBar().setOpaque(false);
        scrollPane_topicInfo.setLayout(new MyScrollPaneLayout());
        scrollPane_topicInfo.getVerticalScrollBar().setUI(new MyScrollBar());
        main_panel.add(scrollPane_topicInfo, "topicInfo");

        JPanel panel_22 = new JPanel();
        panel_22.setBorder(null);
        panel_22.setBackground(Color.WHITE);
        scrollPane_topicInfo.setViewportView(panel_22);
        panel_22.setLayout(null);

        JPanel panel_27 = new JPanel();
        panel_27.setBounds(37, 78, 501, 464);
        panel_22.add(panel_27);
        panel_27.setLayout(new BorderLayout(0, 0));

        JLabel label_5 = new JLabel("W.I.P.");
        label_5.setHorizontalAlignment(SwingConstants.CENTER);
        label_5.setFont(new Font("Bodoni MT", Font.BOLD, 80));
        label_5.setBounds(510, 112, 301, 198);
        panel_22.add(label_5);

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
                SwingUtilities.invokeLater(() -> {
                    int y = arg0.getY() - Py;
                    Dimension d = ServerGuiResizable.this.getSize();
                    ServerGuiResizable.this.setSize(d.width, d.height + y);
                });
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                SwingUtilities.invokeLater(() -> {
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
                SwingUtilities.invokeLater(() -> {
                    int x = arg0.getX() - Px;
                    int y = arg0.getY() - Py;
                    Dimension d = ServerGuiResizable.this.getSize();
                    ServerGuiResizable.this.setSize(d.width + x, d.height + y);
                });
            }
        });
        panel_4.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                SwingUtilities.invokeLater(() -> {
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
                SwingUtilities.invokeLater(() -> {
                    int x = arg0.getX() - Px;
                    Dimension d = ServerGuiResizable.this.getSize();
                    ServerGuiResizable.this.setSize(d.width + x, d.height);
                });
            }
        });
        panel_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                SwingUtilities.invokeLater(() -> {
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
        label_29.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		popupMenu.pack();
        		Point pos = new Point();
        		Dimension size = popupMenu.getPreferredSize();
        		pos.x = (label_29.getWidth() / 4) - size.width + 2;
        		pos.y = (label_29.getHeight() / 4) - size.height + 2;
        		popupMenu.show(label_29, pos.x, pos.y);
        	}
        });
        label_29.setIcon(new ImageIcon(Objects.requireNonNull(classLoader.getResource("Settings_22px.png"))));
        label_29.setBounds(167, 1, 22, 22);
        panel_5.add(label_29);
        
        popupMenu = new JPopupMenu();
        addPopup(label_29, popupMenu);
        
        JMenuItem mntmConnectTo = new JMenuItem("Connect to...");
        mntmConnectTo.addActionListener(e -> {
            try {
                Connection frame = new Connection(serverStat.getServerName(), executor);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setTitle("Connection Manager");
                frame.setVisible(true);
            }catch(Exception e1) {
                System.err.println("[GUI-ERROR]: error duiring the creation of COnnectionTo form.");
                System.err.println("\tException type: " + e1.getClass().getSimpleName());
                System.err.println("\tException message: " + e1.getMessage());
            }
        });
        popupMenu.add(mntmConnectTo);

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

        JPanel panel_console = new JPanel();
        panel_console.setOpaque(false);
        panel_console.setBackground(Color.WHITE);
        main_panel.add(panel_console, "console");

        JPanel panel_28 = new JPanel();
        panel_28.setBorder(new LineBorder(Color.WHITE, 8, true));
        panel_28.setBackground(Color.DARK_GRAY);
        GroupLayout gl_panel_console = new GroupLayout(panel_console);
        gl_panel_console.setHorizontalGroup(gl_panel_console.createParallelGroup(Alignment.LEADING)
                .addComponent(panel_28, GroupLayout.DEFAULT_SIZE, 1027, Short.MAX_VALUE));
        gl_panel_console.setVerticalGroup(gl_panel_console.createParallelGroup(Alignment.LEADING).addComponent(panel_28,
                GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE));

        JPanel panel_29 = new JPanel();
        panel_29.setBackground(new Color(20, 20, 20));

        JScrollPane scrollPane = new JScrollPane();

        scrollPane.setComponentZOrder(scrollPane.getVerticalScrollBar(), 0);
        scrollPane.setComponentZOrder(scrollPane.getViewport(), 1);
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setLayout(new MyScrollPaneLayout());
        scrollPane.getVerticalScrollBar().setUI(new MyScrollBar(new Color(250, 250, 250)));

        scrollPane.setBackground(Color.DARK_GRAY);
        scrollPane.setBorder(null);
        GroupLayout gl_panel_28 = new GroupLayout(panel_28);
        gl_panel_28.setHorizontalGroup(gl_panel_28.createParallelGroup(Alignment.LEADING)
                .addComponent(panel_29, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(gl_panel_28.createSequentialGroup().addGap(5)
                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 994, Short.MAX_VALUE).addGap(1)));
        gl_panel_28.setVerticalGroup(gl_panel_28.createParallelGroup(Alignment.TRAILING)
                .addGroup(gl_panel_28.createSequentialGroup().addGap(5)
                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE).addGap(1)
                        .addComponent(panel_29, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)));

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setBorder(null);
        textPane.setForeground(Color.WHITE);
        textPane.setFont(new Font("Consolas", Font.PLAIN, 16));
        textPane.setBackground(Color.DARK_GRAY);
        scrollPane.setViewportView(textPane);

        attributeInput = new SimpleAttributeSet(textPane.getInputAttributes());
        StyleConstants.setForeground(attributeInput, Color.GREEN);
        attributeOutput = new SimpleAttributeSet(textPane.getInputAttributes());
        StyleConstants.setForeground(attributeOutput, Color.WHITE);
        attributeError = new SimpleAttributeSet(textPane.getInputAttributes());
        StyleConstants.setForeground(attributeError, new Color(255, 0, 0));

        JLabel label_26 = new JLabel(">:");
        label_26.setBorder(null);
        label_26.setAlignmentY(0.0f);
        label_26.setFont(new Font("Consolas", Font.BOLD, 18));
        label_26.setForeground(Color.WHITE);
        label_26.setHorizontalAlignment(SwingConstants.RIGHT);

        textField = new JTextField();
        textField.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        textField.setAlignmentX(0.1f);
        textField.addActionListener(arg0 -> {
            String command = textField.getText() + "\n";
            if (!command.isEmpty()) {
                SwingUtilities.invokeLater(() -> {
                    appendToPane(">: " + command, attributeInput);
                    textField.setText("");
                    try {
                        executor.write(command.getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        appendToPane("\n[GUI-ERROR] Console reports an Internal error on stdIn The error is: " + e + ". Resetting it to the initial stream\n",attributeError);
                        StreamRedirector.resetStdIn();
                    }
                });
            }
        });
        textField.setMargin(new Insets(2, 0, 2, 2));
        textField.setHorizontalAlignment(SwingConstants.LEFT);
        textField.setForeground(Color.WHITE);
        textField.setBorder(null);
        textField.setFont(new Font("Consolas", Font.PLAIN, 18));
        textField.setOpaque(false);
        textField.setColumns(10);
        GroupLayout gl_panel_29 = new GroupLayout(panel_29);
        gl_panel_29.setHorizontalGroup(gl_panel_29.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_29.createSequentialGroup()
                        .addComponent(label_26, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(textField, GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)));
        gl_panel_29.setVerticalGroup(gl_panel_29.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel_29.createParallelGroup(Alignment.BASELINE)
                        .addComponent(label_26, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                        .addComponent(textField, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)));
        panel_29.setLayout(gl_panel_29);
        panel_28.setLayout(gl_panel_28);
        panel_console.setLayout(gl_panel_console);

        timer.start();
        startReaders();

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
        if (!AddressIp.getLocalIp().equals("Unknown")) {
            lblC.setIcon(connected);
            lblC.setToolTipText("Internet access");
        } else {
            lblC.setIcon(disconnected);
            lblC.setToolTipText("No connection are available");
        }
    }

    private void updateServerStat() {
        SwingUtilities.invokeLater(() -> {
            lblClientOnline.setText(String.valueOf(serverStat.getClientNumber()));
            lblPostNumber.setText(String.valueOf(serverStat.getPostNumber()));
            lblTopicNumber.setText(String.valueOf(serverStat.getTopicNumber()));
        });
    }

    public void setServerName() {
        SwingUtilities.invokeLater(() -> {
            lblServerName.setText(serverStat.getServerName());
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (serverStat.getServerReady())
            updateServerStat();
    }

    @Override
    public void run() {
        Scanner sc;
        final MutableAttributeSet myAttribute;
        try {
            if (Thread.currentThread() == readerStdOut) {
                if (stdOut == null)
                    return;
                sc = new Scanner(stdOut);
                myAttribute = attributeOutput;
            } else if (Thread.currentThread() == readerStdErr) {
                if (stdErr == null)
                    return;
                sc = new Scanner(stdErr);
                myAttribute = attributeError;
            } else
                return;
            while (!quit) {
                final String s = sc.nextLine();
                SwingUtilities.invokeLater(() -> {
                    appendToPane(s + "\n", myAttribute);
                });
            }
            sc.close();
        } catch (Exception e) {
            String errorMsg = "\n[GUI-ERROR] Console reports an Internal error on" + (Thread.currentThread() == readerStdOut ? "StdOut" : Thread.currentThread() == readerStdErr ? "StdErr" : "???") + "The error is: " + e + ". Resetting it to the initial stream\n";

            if (Thread.currentThread() == readerStdOut) {
                StreamRedirector.resetStdOut();
            } else if (Thread.currentThread() == readerStdErr) {
                StreamRedirector.resetStdErr();
            }
            SwingUtilities.invokeLater(() -> {
                appendToPane(errorMsg, attributeError);
            });
        }
    }

    private void startReaders() {
        readerStdOut = new Thread(this);
        readerStdOut.setDaemon(true);

        readerStdErr = new Thread(this);
        readerStdErr.setDaemon(true);

        readerStdOut.start();
        readerStdErr.start();
    }

    private void appendToPane(String msg, MutableAttributeSet attr) {
        try {
            textPane.getStyledDocument().insertString(textPane.getDocument().getLength(), msg, attr);
        } catch (BadLocationException ignored) {
        }

    }
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}

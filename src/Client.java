import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.*;
import java.text.*;

public class Client implements ActionListener {

    JButton send;
    JTextField text;
    JPanel al;
    static DataOutputStream dout;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();

    Client() {
        f.setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5, 20, 25, 25);
        p1.add(back);

        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        ImageIcon pi = new ImageIcon(ClassLoader.getSystemResource("icons/2.png"));
        Image pi1 = pi.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon pi2 = new ImageIcon(pi1);
        JLabel image = new JLabel(pi2);
        image.setBounds(40, 10, 50, 50);
        p1.add(image);

        JLabel name = new JLabel("Friend");
        name.setBounds(110, 15, 200, 15);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SanSerif", Font.BOLD, 18));
        p1.add(name);

        JLabel status = new JLabel("Active now");
        status.setBounds(110, 35, 100, 8);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SanSerif", Font.BOLD, 10));
        p1.add(status);

        al = new JPanel();
        al.setBounds(5, 75, 440, 575);
        al.setLayout(new BoxLayout(al, BoxLayout.Y_AXIS));
        f.add(al);

        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN SERIF", Font.PLAIN, 16));
        f.add(text);

        send = new JButton("Send");
        send.setBounds(320, 655, 120, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.BLACK);
        send.setFont(new Font("SAN SERIF", Font.PLAIN, 16));
        send.addActionListener(this);
        f.add(send);

        f.setSize(450, 700);
        f.setLocation(800, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String out = text.getText();
            JPanel p2 = formatLabel(out);

            al.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            al.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(out);
            text.setText("");

            f.repaint();
            f.invalidate();
            f.validate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

        JLabel time = new JLabel();
        time.setFont(new Font("Tahoma", Font.PLAIN, 12));
        time.setForeground(Color.GRAY);
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }

    public static void main(String[] args) {
        new Client();

        try {
            Socket s = new Socket("localhost", 6001);
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while (true) {
                try {
                    String msg = din.readUTF();
                    JPanel panel = formatLabel(msg);

                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);

                    vertical.add(left);
                    f.validate();
                } catch (Exception e) {
                    System.out.println("Server disconnected.");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

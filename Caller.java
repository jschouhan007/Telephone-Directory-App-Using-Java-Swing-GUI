import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Caller extends JFrame {
    // ==================== CONTACT ====================
    private static class Contact {
        private String name;
        private String phone;
        private String email;
        private String address;

        Contact(String name, String phone, String email, String address) {
            this.name    = name;
            this.phone   = phone;
            this.email   = email;
            this.address = address;
        }

        Contact copy() {
            return new Contact(name, phone, email, address);
        }

        public String getName()                 { return name;    }
        public void   setName(String name)      { this.name = name; }
        public String getPhone()                { return phone;   }
        public void   setPhone(String phone)    { this.phone = phone; }
        public String getEmail()                { return email;   }
        public void   setEmail(String email)    { this.email = email; }
        public String getAddress()              { return address; }
        public void   setAddress(String address){ this.address = address; }
    }

    

    private enum ActionType { ADD, DELETE, UPDATE, BULK_DELETE }

    private static class UndoAction {
        ActionType type;
        int index;
        Contact before;
        Contact after;
        List<int[]> bulkIndices;
        List<Contact> bulkContacts;

        static UndoAction add(int index, Contact added) {
            UndoAction a = new UndoAction();
            a.type  = ActionType.ADD;
            a.index = index;
            a.after = added.copy();
            return a;
        }

        static UndoAction delete(int index, Contact removed) {
            UndoAction a = new UndoAction();
            a.type   = ActionType.DELETE;
            a.index  = index;
            a.before = removed.copy();
            return a;
        }

        static UndoAction update(int index, Contact before, Contact after) {
            UndoAction a = new UndoAction();
            a.type   = ActionType.UPDATE;
            a.index  = index;
            a.before = before.copy();
            a.after  = after.copy();
            return a;
        }

        static UndoAction bulkDelete(List<Integer> indices,
                                     List<Contact> removed) {
            UndoAction a = new UndoAction();
            a.type         = ActionType.BULK_DELETE;
            a.bulkIndices  = new ArrayList<>();
            a.bulkContacts = new ArrayList<>();
            for (int i = 0; i < indices.size(); i++) {
                a.bulkIndices.add(new int[]{ indices.get(i) });
                a.bulkContacts.add(removed.get(i).copy());
            }
            return a;
        }
    }

    //gradient 

    private static class GradientPanel extends JPanel {
        private final Color c1, c2;
        GradientPanel(Color c1, Color c2) { this.c1 = c1; this.c2 = c2; }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // ==================== ROUNDED CARD PANEL ====================

    private static class CardPanel extends JPanel {
        private final Color fill;
        private final int arc;
        private final boolean shadow;

        CardPanel(Color fill, int arc, boolean shadow) {
            this.fill = fill;
            this.arc = arc;
            this.shadow = shadow;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            if (shadow) {
                for (int i = 4; i > 0; i--) {
                    g2.setColor(new Color(0, 0, 0, 10));
                    g2.fill(new RoundRectangle2D.Float(i, i + 1, w - 2 * i, h - 2 * i, arc, arc));
                }
            }
            g2.setColor(fill);
            g2.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - (shadow ? 3 : 1), arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ==================== ROUNDED BUTTON ====================

    private static class RoundedButton extends JButton {
        private Color bg, hoverBg, pressBg;
        private boolean hovered = false, pressed = false;
        private final int arc = 10;

        RoundedButton(String text, Color bg, Color hoverBg) {
            super(text);
            this.bg = bg;
            this.hoverBg = hoverBg;
            this.pressBg = hoverBg.darker();
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(FONT_BUTTON);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                @Override public void mousePressed(MouseEvent e) { pressed = true; repaint(); }
                @Override public void mouseReleased(MouseEvent e) { pressed = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = pressed ? pressBg : (hovered ? hoverBg : bg);
            g2.setColor(c);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arc, arc));
            if (hovered && !pressed) {
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() / 2f, arc, arc));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ==================== ROUNDED FIELD BORDER ====================

    private static class RoundedFieldBorder extends AbstractBorder {
        private final JTextField field;
        private final Color normal, focusColor;
        private final int arc = 10;

        RoundedFieldBorder(JTextField field, Color normal, Color focusColor) {
            this.field = field;
            this.normal = normal;
            this.focusColor = focusColor;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            boolean focused = field.hasFocus();
            g2.setColor(focused ? focusColor : normal);
            g2.setStroke(new BasicStroke(focused ? 1.6f : 1f));
            g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, w - 3, h - 3, arc, arc));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(7, 12, 7, 12);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(7, 12, 7, 12);
            return insets;
        }
    }

    // ==================== PLACEHOLDER TEXT FIELD ====================

    private static class PlaceholderField extends JTextField {
        private final String placeholder;

        PlaceholderField(String placeholder, int cols) {
            super(cols);
            this.placeholder = placeholder;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(INPUT_BG);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
            g2.dispose();
            super.paintComponent(g);
            if (getText().isEmpty() && !placeholder.isEmpty()) {
                Graphics2D pg = (Graphics2D) g.create();
                pg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                pg.setColor(TEXT_SECONDARY);
                pg.setFont(getFont());
                FontMetrics fm = pg.getFontMetrics();
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                pg.drawString(placeholder, getInsets().left, textY);
                pg.dispose();
            }
        }
    }

    // ==================== STRIPED RENDERER (with hover row) ====================

    private static class StripedRenderer extends DefaultTableCellRenderer {
        private final Color r1, r2, sel, fg;
        private final Font f;
        private int hoverRow = -1;

        StripedRenderer(Color r1, Color r2, Color sel, Color fg, Font f) {
            this.r1 = r1; this.r2 = r2; this.sel = sel;
            this.fg = fg; this.f  = f;
        }

        void setHoverRow(int row) { hoverRow = row; }

        @Override
        public Component getTableCellRendererComponent(JTable t,
                Object val, boolean isSel, boolean hasFocus,
                int row, int col) {
            super.getTableCellRendererComponent(t, val, isSel, hasFocus, row, col);
            setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
            setFont(f);
            setForeground(fg);
            Color base = row % 2 == 0 ? r1 : r2;
            if (isSel) {
                setBackground(sel);
            } else if (row == hoverRow) {
                setBackground(lighten(base, 10));
            } else {
                setBackground(base);
            }
            return this;
        }

        private Color lighten(Color c, int amt) {
            return new Color(
                    Math.min(255, c.getRed() + amt),
                    Math.min(255, c.getGreen() + amt),
                    Math.min(255, c.getBlue() + amt));
        }
    }

    // ==================== ROW HOVER + CLICK LISTENER ====================

    private class RowInteractionListener extends MouseAdapter {
        private final StripedRenderer renderer;

        RowInteractionListener(StripedRenderer renderer) { this.renderer = renderer; }

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtName.setText(str(model.getValueAt(row, 1)));
                txtPhone.setText(str(model.getValueAt(row, 2)));
                txtEmail.setText(str(model.getValueAt(row, 3)));
                txtAddress.setText(str(model.getValueAt(row, 4)));
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int row = table.rowAtPoint(e.getPoint());
            renderer.setHoverRow(row);
            table.repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            renderer.setHoverRow(-1);
            table.repaint();
        }
    }

    // ==================== SLEEK SCROLLBAR ====================

    private static class SleekScrollBarUI extends BasicScrollBarUI {
        @Override protected void configureScrollBarColors() {
            thumbColor = PRIMARY_LIGHT;
            trackColor = SURFACE;
        }

        @Override protected JButton createDecreaseButton(int orientation) { return zeroButton(); }
        @Override protected JButton createIncreaseButton(int orientation) { return zeroButton(); }

        private JButton zeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            b.setMinimumSize(new Dimension(0, 0));
            b.setMaximumSize(new Dimension(0, 0));
            return b;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(ACCENT.darker());
            g2.fill(new RoundRectangle2D.Float(
                    thumbBounds.x + 3, thumbBounds.y + 2,
                    thumbBounds.width - 6, thumbBounds.height - 4, 8, 8));
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            g.setColor(SURFACE);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
    }

    // ==================== COUNTRY CODE RENDERER ====================

    private class CountryCodeRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list,
                Object value, int index, boolean isSelected, boolean cellHasFocus) {

            super.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);

            String entry = (value == null) ? "" : value.toString();
            String[] parts = entry.split("\\|", 2);
            String code = parts[0];
            String country = parts.length > 1 ? parts[1] : "";

            if (index == -1) {
                // Collapsed view — show only the code
                setText(code);
            } else {
                // Dropdown list — show code and country name
                setText(code + "  " + country);
            }

            setFont(FONT_INPUT);
            setBackground(isSelected ? ACCENT : INPUT_BG);
            setForeground(isSelected ? Color.WHITE : TEXT_PRIMARY);
            setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

            return this;
        }
    }

    // ======================== DATA ========================
    private final ArrayList<Contact> directory = new ArrayList<>();
    private final Stack<UndoAction> undoStack  = new Stack<>();
    private final Stack<UndoAction> redoStack  = new Stack<>();

    // ======================== GUI ========================
    private JTextField txtName, txtPhone, txtEmail, txtAddress, txtSearch;
    private JComboBox<String> cmbCountryCode;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblStatus, lblCountBadge;
    private JButton btnUndo, btnRedo;

    // Country codes: "code|displayName"
    private static final String[] COUNTRY_CODES = {
            "+91|India",
            "+1|USA",
            "+1|Canada",
            "+44|UK",
            "+61|Australia",
            "+81|Japan",
            "+86|China",
            "+49|Germany",
            "+33|France",
            "+39|Italy",
            "+7|Russia",
            "+55|Brazil",
            "+27|South Africa",
            "+82|South Korea",
            "+65|Singapore",
            "+971|UAE",
            "+966|Saudi Arabia",
            "+62|Indonesia",
            "+60|Malaysia",
            "+63|Philippines",
            "+66|Thailand",
            "+84|Vietnam",
            "+880|Bangladesh",
            "+92|Pakistan",
            "+94|Sri Lanka",
            "+977|Nepal",
            "+234|Nigeria",
            "+254|Kenya",
            "+20|Egypt",
            "+52|Mexico",
            "+54|Argentina",
            "+56|Chile",
            "+48|Poland",
            "+46|Sweden",
            "+47|Norway",
            "+31|Netherlands",
            "+34|Spain",
            "+351|Portugal",
            "+90|Turkey",
            "+353|Ireland",
            "+64|New Zealand"
    };

    // Sound
    private Clip clip;

    // The project root directory (where .java and .wav files live)
    private String projectDir;

    // ======================== COLORS ========================
    private static final Color PRIMARY       = new Color(13, 20, 38);
    private static final Color PRIMARY_LIGHT = new Color(51, 65, 85);
    private static final Color ACCENT        = new Color(99, 102, 241);
    private static final Color ACCENT_HOVER  = new Color(129, 140, 248);
    private static final Color SUCCESS       = new Color(16, 185, 129);
    private static final Color SUCCESS_HOVER = new Color(52, 211, 153);
    private static final Color DANGER        = new Color(239, 68, 68);
    private static final Color DANGER_HOVER  = new Color(248, 113, 113);
    private static final Color WARNING       = new Color(245, 158, 11);
    private static final Color WARNING_HOVER = new Color(251, 191, 36);
    private static final Color SURFACE       = new Color(26, 36, 56);
    private static final Color SURFACE_2     = new Color(32, 44, 66);
    private static final Color TEXT_PRIMARY   = new Color(241, 245, 249);
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184);
    private static final Color BORDER        = new Color(51, 65, 85);
    private static final Color TABLE_ROW_1   = new Color(28, 39, 60);
    private static final Color TABLE_ROW_2   = new Color(22, 32, 51);
    private static final Color TABLE_SELECT  = new Color(99, 102, 241, 90);
    private static final Color INPUT_BG      = new Color(15, 23, 42);
    private static final Color INPUT_BORDER  = new Color(71, 85, 105);
    private static final Color TEAL          = new Color(20, 184, 166);
    private static final Color TEAL_HOVER    = new Color(45, 212, 191);
    private static final Color PURPLE        = new Color(168, 85, 247);
    private static final Color PURPLE_HOVER  = new Color(192, 132, 252);
    private static final Color GRAY_BTN      = new Color(71, 85, 105);
    private static final Color GRAY_HOVER    = new Color(100, 116, 139);

    // ======================== FONTS ========================
    private static final Font FONT_TITLE     = new Font("Segoe UI", Font.BOLD, 23);
    private static final Font FONT_SUBTITLE  = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_LABEL     = new Font("Segoe UI", Font.BOLD, 11);
    private static final Font FONT_SECTION   = new Font("Segoe UI", Font.BOLD, 11);
    private static final Font FONT_INPUT     = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BUTTON    = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_TABLE     = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_TABLE_HDR = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_STATUS    = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_BADGE     = new Font("Segoe UI", Font.BOLD, 11);

    // ======================== FILES ========================
    private static final String SOUND_ADD    = "Add sound.wav";
    private static final String SOUND_DELETE = "fahhhhh.wav";
    private static final String SOUND_CLICK  = "soft click tap.wav";
    private static final String DATA_FILENAME = "contacts.csv";

    // ==========================================================
    //  CONSTRUCTOR
    // ==========================================================

    public Caller() {

        // Resolve the project directory once at startup
        projectDir = resolveProjectDir();

        setTitle("Telephone Directory Management System");
        setSize(1000, 660);
        setMinimumSize(new Dimension(880, 540));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set app icon
        try {
            File iconFile = findFile("phone_icon.jpg");
            if (iconFile != null)
                setIconImage(new ImageIcon(
                        iconFile.getAbsolutePath()).getImage());
        } catch (Exception ignored) {}

        getContentPane().setBackground(PRIMARY);
        setLayout(new BorderLayout(0, 0));

        // ---- HEADER ----

        GradientPanel header = new GradientPanel(new Color(79, 70, 229), new Color(168, 85, 247));
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(16, 26, 16, 26));
        header.setPreferredSize(new Dimension(0, 78));

        JPanel headerText = new JPanel();
        headerText.setOpaque(false);
        headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Telephone Directory");
        title.setFont(FONT_TITLE);
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerText.add(title);

        JLabel subtitle = new JLabel(
                "Merge Sort   \u00b7   Binary Search   \u00b7   Undo / Redo Stack");
        subtitle.setFont(FONT_SUBTITLE);
        subtitle.setForeground(new Color(255, 255, 255, 210));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(3, 2, 0, 0));
        headerText.add(subtitle);

        header.add(headerText, BorderLayout.WEST);

        lblCountBadge = new JLabel("0 contacts");
        lblCountBadge.setFont(FONT_BADGE);
        lblCountBadge.setForeground(Color.WHITE);
        lblCountBadge.setOpaque(false);
        lblCountBadge.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        JPanel badgeWrap = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 45));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 18, 18));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badgeWrap.setOpaque(false);
        badgeWrap.add(lblCountBadge);
        JPanel badgeHolder = new JPanel(new GridBagLayout());
        badgeHolder.setOpaque(false);
        badgeHolder.add(badgeWrap);
        header.add(badgeHolder, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ---- CENTER ----

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(PRIMARY);
        center.setBorder(BorderFactory.createEmptyBorder(14, 16, 0, 16));

        // ---- INPUT CARD ----

        CardPanel inputPanel = new CardPanel(SURFACE, 16, true);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        // Section 1 : Contact Details
        inputPanel.add(sectionLabel("CONTACT DETAILS"));
        inputPanel.add(Box.createVerticalStrut(8));

        JPanel r1 = flowRow();
        r1.add(fixedLabel("Name", 50));
        txtName = makeTextField("Full name", 11);
        r1.add(txtName);

        r1.add(fixedLabel("Phone", 44));

        // Country code dropdown — dark themed, matching text field height
        cmbCountryCode = new JComboBox<>(COUNTRY_CODES);
        cmbCountryCode.setSelectedIndex(0);
        cmbCountryCode.setFont(FONT_INPUT);
        cmbCountryCode.setRenderer(new CountryCodeRenderer());
        cmbCountryCode.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cmbCountryCode.setOpaque(false);
        cmbCountryCode.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton b = super.createArrowButton();
                b.setBackground(PRIMARY_LIGHT);
                b.setForeground(TEXT_SECONDARY);
                b.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, INPUT_BORDER));
                return b;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds,
                                                     boolean hasFocus) {
                g.setColor(INPUT_BG);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }

            @Override
            public void paintCurrentValue(Graphics g, Rectangle bounds,
                                           boolean hasFocus) {
                ListCellRenderer<Object> r = comboBox.getRenderer();
                Component c = r.getListCellRendererComponent(
                        listBox, comboBox.getSelectedItem(),
                        -1, false, false);
                c.setFont(FONT_INPUT);
                c.setForeground(TEXT_PRIMARY);
                c.setBackground(INPUT_BG);
                currentValuePane.paintComponent(g, c,
                        comboBox, bounds.x, bounds.y,
                        bounds.width, bounds.height, false);
            }
        });
        // Match height of text fields and apply border
        cmbCountryCode.setPreferredSize(new Dimension(78, 34));
        cmbCountryCode.setMaximumSize(new Dimension(78, 34));
        cmbCountryCode.setBorder(BorderFactory.createLineBorder(INPUT_BORDER, 1));
        r1.add(cmbCountryCode);

        txtPhone = makeTextField("e.g. 9876543210", 10);
        r1.add(txtPhone);

        r1.add(fixedLabel("Email", 40));
        txtEmail = makeTextField("name@example.com", 14);
        r1.add(txtEmail);
        inputPanel.add(r1);

        JPanel r2 = flowRow();
        r2.add(fixedLabel("Address", 50));
        txtAddress = makeTextField("Street, city", 20);
        r2.add(txtAddress);

        r2.add(Box.createHorizontalStrut(10));
        RoundedButton btnAdd     = new RoundedButton("+  Add",              SUCCESS, SUCCESS_HOVER);
        RoundedButton btnUpdate  = new RoundedButton("Update",              ACCENT,  ACCENT_HOVER);
        RoundedButton btnBulkDel = new RoundedButton("Delete Selected",     DANGER,  DANGER_HOVER);
        r2.add(btnAdd);
        r2.add(btnUpdate);
        r2.add(btnBulkDel);
        inputPanel.add(r2);

        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(divider());
        inputPanel.add(Box.createVerticalStrut(10));

        // Section 2 : Search & Tools
        inputPanel.add(sectionLabel("SEARCH & TOOLS"));
        inputPanel.add(Box.createVerticalStrut(8));

        JPanel r3 = flowRow();
        r3.add(fixedLabel("Search", 50));
        txtSearch = makeTextField("Search by name...", 12);
        r3.add(txtSearch);

        r3.add(Box.createHorizontalStrut(6));
        RoundedButton btnSearch = new RoundedButton("Search",       WARNING, WARNING_HOVER);
        RoundedButton btnSort   = new RoundedButton("Merge Sort",   ACCENT,  ACCENT_HOVER);
        btnUndo                 = new RoundedButton("Undo",         GRAY_BTN, GRAY_HOVER);
        btnRedo                 = new RoundedButton("Redo",         GRAY_BTN, GRAY_HOVER);
        RoundedButton btnImport = new RoundedButton("Import CSV",   TEAL,    TEAL_HOVER);
        RoundedButton btnExport = new RoundedButton("Export CSV",   PURPLE,  PURPLE_HOVER);

        r3.add(btnSearch);
        r3.add(btnSort);
        r3.add(btnUndo);
        r3.add(btnRedo);
        r3.add(btnImport);
        r3.add(btnExport);
        inputPanel.add(r3);

        center.add(inputPanel, BorderLayout.NORTH);

        // ---- TABLE CARD ----

        model = new DefaultTableModel(
                new String[]{"", "Name", "Phone", "Email", "Address"}, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 0 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0;
            }
        };

        table = new JTable(model);
        table.setFont(FONT_TABLE);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(SURFACE);
        table.setForeground(TEXT_PRIMARY);
        table.setSelectionBackground(TABLE_SELECT);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        table.getColumnModel().getColumn(0).setMaxWidth(42);
        table.getColumnModel().getColumn(0).setMinWidth(42);

        StripedRenderer renderer = new StripedRenderer(
                TABLE_ROW_1, TABLE_ROW_2, TABLE_SELECT,
                TEXT_PRIMARY, FONT_TABLE);
        for (int i = 1; i < 5; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);

        RowInteractionListener rowListener = new RowInteractionListener(renderer);
        table.addMouseListener(rowListener);
        table.addMouseMotionListener(rowListener);

        JTableHeader th = table.getTableHeader();
        th.setFont(FONT_TABLE_HDR);
        th.setBackground(SURFACE_2);
        th.setForeground(TEXT_SECONDARY);
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));
        th.setPreferredSize(new Dimension(0, 38));
        th.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) th.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        sp.getViewport().setBackground(SURFACE);
        sp.getVerticalScrollBar().setUI(new SleekScrollBarUI());
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        CardPanel tableCard = new CardPanel(SURFACE, 14, true);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(BorderFactory.createEmptyBorder(4, 4, 8, 4));
        tableCard.add(sp, BorderLayout.CENTER);

        center.add(tableCard, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        // ---- STATUS BAR ----

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(PRIMARY);
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)));
        lblStatus = new JLabel("Ready");
        lblStatus.setFont(FONT_STATUS);
        lblStatus.setForeground(SUCCESS);
        statusBar.add(lblStatus, BorderLayout.WEST);

        JLabel lblHint = new JLabel("Click a row to edit  \u00b7  Tick boxes to bulk delete");
        lblHint.setFont(FONT_STATUS);
        lblHint.setForeground(TEXT_SECONDARY);
        statusBar.add(lblHint, BorderLayout.EAST);

        add(statusBar, BorderLayout.SOUTH);

        // ============ LISTENERS ============

        btnAdd.addActionListener(e -> addContact());
        btnUpdate.addActionListener(e -> updateContact());
        btnBulkDel.addActionListener(e -> bulkDeleteContacts());
        btnSort.addActionListener(e -> sortContacts());
        btnSearch.addActionListener(e -> searchContact());
        btnUndo.addActionListener(e -> undo());
        btnRedo.addActionListener(e -> redo());
        btnImport.addActionListener(e -> importCSV());
        btnExport.addActionListener(e -> exportCSV());

        // Load saved data
        loadFromFile();
        refreshTable();

    } // END CONSTRUCTOR

    // ==========================================================
    //  ADD CONTACT
    // ==========================================================

    private void addContact() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String address = txtAddress.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            showMsg("Please enter at least Name and Phone.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Phone must have at least 10 digits
        if (countDigits(phone) < 10) {
            showMsg("Phone Number must be at least 10 digits.",
                    "Invalid Phone", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Contact c : directory) {
            if (c.getPhone().equals(phone)) {
                showMsg("Phone Number already exists!",
                        "Duplicate Phone", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        for (Contact c : directory) {
            if (c.getName().equalsIgnoreCase(name)) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "A contact named \"" + c.getName()
                                + "\" already exists.\nAdd anyway?",
                        "Duplicate Name",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (choice != JOptionPane.YES_OPTION) return;
                break;
            }
        }

        Contact newContact = new Contact(name, phone, email, address);
        directory.add(newContact);

        undoStack.push(UndoAction.add(directory.size() - 1, newContact));
        redoStack.clear();

        refreshTable();
        saveToFile();
        clearFields();
        playSound(SOUND_ADD);
        showMsg("Contact Added Successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==========================================================
    //  UPDATE CONTACT
    // ==========================================================

    private void updateContact() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showMsg("Select a contact first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String newName    = txtName.getText().trim();
        String newPhone   = txtPhone.getText().trim();
        String newEmail   = txtEmail.getText().trim();
        String newAddress = txtAddress.getText().trim();

        if (newName.isEmpty() || newPhone.isEmpty()) {
            showMsg("Name and Phone cannot be empty.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Phone must have at least 10 digits
        if (countDigits(newPhone) < 10) {
            showMsg("Phone Number must be at least 10 digits.",
                    "Invalid Phone", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < directory.size(); i++) {
            if (i != row && directory.get(i).getPhone().equals(newPhone)) {
                showMsg("Phone Number already exists for \""
                                + directory.get(i).getName() + "\"!",
                        "Duplicate Phone", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        for (int i = 0; i < directory.size(); i++) {
            if (i != row && directory.get(i).getName()
                    .equalsIgnoreCase(newName)) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "A contact named \"" + directory.get(i).getName()
                                + "\" already exists.\nUpdate anyway?",
                        "Duplicate Name",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (choice != JOptionPane.YES_OPTION) return;
                break;
            }
        }

        Contact old = directory.get(row);
        Contact before = old.copy();

        old.setName(newName);
        old.setPhone(newPhone);
        old.setEmail(newEmail);
        old.setAddress(newAddress);

        undoStack.push(UndoAction.update(row, before, old));
        redoStack.clear();

        refreshTable();
        saveToFile();
        playSound(SOUND_CLICK);
        showMsg("Contact Updated.", "Updated",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ==========================================================
    //  BULK DELETE
    // ==========================================================

    private void bulkDeleteContacts() {
        List<Integer> selected = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            Object val = model.getValueAt(i, 0);
            if (val instanceof Boolean && (Boolean) val) {
                selected.add(i);
            }
        }

        if (selected.isEmpty()) {
            showMsg("Tick the checkboxes of contacts to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete " + selected.size() + " selected contact(s)?",
                "Confirm Bulk Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        List<Contact> removed = new ArrayList<>();
        for (int idx : selected) removed.add(directory.get(idx).copy());

        for (int i = selected.size() - 1; i >= 0; i--) {
            directory.remove((int) selected.get(i));
        }

        undoStack.push(UndoAction.bulkDelete(selected, removed));
        redoStack.clear();

        refreshTable();
        saveToFile();
        clearFields();
        playSound(SOUND_DELETE);
        showMsg(selected.size() + " contact(s) deleted.",
                "Deleted", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==========================================================
    //  UNDO
    // ==========================================================

    private void undo() {
        if (undoStack.isEmpty()) {
            showMsg("Nothing to undo.", "Undo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        UndoAction action = undoStack.pop();

        switch (action.type) {
            case ADD:
                directory.remove(action.index);
                break;
            case DELETE:
                directory.add(action.index, action.before.copy());
                break;
            case UPDATE:
                Contact c = directory.get(action.index);
                c.setName(action.before.getName());
                c.setPhone(action.before.getPhone());
                c.setEmail(action.before.getEmail());
                c.setAddress(action.before.getAddress());
                break;
            case BULK_DELETE:
                for (int i = 0; i < action.bulkIndices.size(); i++) {
                    int idx = action.bulkIndices.get(i)[0];
                    directory.add(idx, action.bulkContacts.get(i).copy());
                }
                break;
        }

        redoStack.push(action);
        refreshTable();
        saveToFile();
        playSound(SOUND_CLICK);
        updateStatus();
    }

    // ==========================================================
    //  REDO
    // ==========================================================

    private void redo() {
        if (redoStack.isEmpty()) {
            showMsg("Nothing to redo.", "Redo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        UndoAction action = redoStack.pop();

        switch (action.type) {
            case ADD:
                directory.add(action.index, action.after.copy());
                break;
            case DELETE:
                directory.remove(action.index);
                break;
            case UPDATE:
                Contact c = directory.get(action.index);
                c.setName(action.after.getName());
                c.setPhone(action.after.getPhone());
                c.setEmail(action.after.getEmail());
                c.setAddress(action.after.getAddress());
                break;
            case BULK_DELETE:
                for (int i = action.bulkIndices.size() - 1; i >= 0; i--) {
                    directory.remove(action.bulkIndices.get(i)[0]);
                }
                break;
        }

        undoStack.push(action);
        refreshTable();
        saveToFile();
        playSound(SOUND_CLICK);
        updateStatus();
    }

    // ==========================================================
    //  SORT
    // ==========================================================

    private void sortContacts() {
        if (directory.size() > 1) {
            mergeSort(directory, 0, directory.size() - 1);
            refreshTable();
            saveToFile();
            playSound(SOUND_CLICK);
            showMsg("Directory Sorted.",
                    "Sorted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ==========================================================
    //  SEARCH
    // ==========================================================

    private void searchContact() {
        String target = txtSearch.getText().trim();
        if (target.isEmpty()) {
            showMsg("Enter a name to search.",
                    "Search", JOptionPane.WARNING_MESSAGE);
            return;
        }

        mergeSort(directory, 0, directory.size() - 1);
        refreshTable();

        int index = binarySearch(directory, target);

        if (index != -1) {
            table.setRowSelectionInterval(index, index);
            table.scrollRectToVisible(table.getCellRect(index, 0, true));
            Contact c = directory.get(index);
            playSound(SOUND_CLICK);
            showMsg("Contact Found!\n\n"
                            + "Name     :  " + c.getName()
                            + "\nPhone   :  " + c.getPhone()
                            + "\nEmail    :  " + c.getEmail()
                            + "\nAddress :  " + c.getAddress(),
                    "Found", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showMsg("Contact Not Found.",
                    "Search", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ==========================================================
    //  IMPORT CSV
    // ==========================================================

    private void importCSV() {
        JFileChooser fc = new JFileChooser(projectDir);
        fc.setDialogTitle("Import Contacts from CSV");
        fc.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        int imported = 0, skipped = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = parseCSVLine(line);
                if (p.length < 2) continue;

                String name    = p[0];
                String phone   = p.length > 1 ? p[1] : "";
                String email   = p.length > 2 ? p[2] : "";
                String address = p.length > 3 ? p[3] : "";

                boolean dup = false;
                for (Contact c : directory) {
                    if (c.getPhone().equals(phone)) { dup = true; break; }
                }
                if (dup) { skipped++; continue; }

                directory.add(new Contact(name, phone, email, address));
                imported++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            showMsg("Import failed.\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        undoStack.clear();
        redoStack.clear();

        refreshTable();
        saveToFile();
        playSound(SOUND_CLICK);
        showMsg("Imported " + imported + " contact(s).\n"
                        + "Skipped " + skipped + " duplicate(s).",
                "Import Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==========================================================
    //  EXPORT CSV
    // ==========================================================

    private void exportCSV() {
        if (directory.isEmpty()) {
            showMsg("No contacts to export.",
                    "Export", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fc = new JFileChooser(projectDir);
        fc.setDialogTitle("Export Contacts to CSV");
        fc.setSelectedFile(new File("contacts_export.csv"));
        fc.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("Name,Phone,Email,Address");
            bw.newLine();
            for (Contact c : directory) {
                bw.write(escapeCSV(c.getName())
                        + "," + escapeCSV(c.getPhone())
                        + "," + escapeCSV(c.getEmail())
                        + "," + escapeCSV(c.getAddress()));
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            showMsg("Export failed.\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        playSound(SOUND_CLICK);
        showMsg("Exported " + directory.size()
                        + " contact(s) to:\n" + file.getAbsolutePath(),
                "Export Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==========================================================
    //  UI HELPERS
    // ==========================================================

    private JPanel flowRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        p.setOpaque(false);
        return p;
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_SECONDARY);
        return l;
    }

    private JLabel fixedLabel(String text, int width) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_SECONDARY);
        l.setPreferredSize(new Dimension(width, 20));
        l.setMinimumSize(new Dimension(width, 20));
        l.setMaximumSize(new Dimension(width, 20));
        return l;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(FONT_SECTION);
        l.setForeground(ACCENT_HOVER);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        return l;
    }

    private JSeparator divider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setBackground(BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private JTextField makeTextField(String placeholder, int cols) {
        PlaceholderField tf = new PlaceholderField(placeholder, cols);
        tf.setFont(FONT_INPUT);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(ACCENT_HOVER);
        tf.setBorder(new RoundedFieldBorder(tf, INPUT_BORDER, ACCENT_HOVER));
        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { tf.repaint(); }
            @Override public void focusLost(FocusEvent e)   { tf.repaint(); }
        });
        return tf;
    }

    private void clearFields() {
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
    }

    private void showMsg(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }

    private String str(Object o) {
        return o == null ? "" : o.toString();
    }

    // Counts only digit characters (ignores dashes, spaces, brackets etc.)
    private int countDigits(String s) {
        int count = 0;
        for (char ch : s.toCharArray()) {
            if (Character.isDigit(ch)) count++;
        }
        return count;
    }

    // ==========================================================
    //  PLAY SOUND  (fixed path resolution)
    // ==========================================================

    private void playSound(String soundFile) {
        try {
            if (clip != null) { clip.stop(); clip.close(); }

            File f = findFile(soundFile);
            if (f == null) {
                System.err.println("Sound not found: " + soundFile);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    // ==========================================================
    //  FILE FINDER  (checks multiple locations)
    // ==========================================================

    private File findFile(String name) {
        // 1. Project directory (where .java and .wav files are)
        File f = new File(projectDir + File.separator + name);
        if (f.exists()) return f;

        // 2. Current working directory
        f = new File(System.getProperty("user.dir") + File.separator + name);
        if (f.exists()) return f;

        // 3. Plain relative path
        f = new File(name);
        if (f.exists()) return f;

        return null;
    }

    // ==========================================================
    //  RESOLVE PROJECT DIRECTORY
    //  (where the .java source and resource files live)
    // ==========================================================

    private String resolveProjectDir() {
        // Strategy 1: Check where the .class file is; if in bin/,
        // the project root is one level up
        try {
            File classLoc = new File(Caller.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath());
            if (classLoc.isDirectory()) {
                // Check if sound files are here
                if (new File(classLoc, SOUND_ADD).exists()) {
                    return classLoc.getAbsolutePath();
                }
                // Try parent (for bin/ compiled scenario)
                File parent = classLoc.getParentFile();
                if (parent != null && new File(parent, SOUND_ADD).exists()) {
                    return parent.getAbsolutePath();
                }
            }
        } catch (Exception ignored) {}

        // Strategy 2: Current working directory
        String cwd = System.getProperty("user.dir");
        if (new File(cwd, SOUND_ADD).exists()) {
            return cwd;
        }

        // Fallback
        return cwd;
    }

    // ==========================================================
    //  REFRESH TABLE  &  STATUS
    // ==========================================================

    private void refreshTable() {
        model.setRowCount(0);
        for (Contact c : directory) {
            model.addRow(new Object[]{
                    Boolean.FALSE,
                    c.getName(), c.getPhone(),
                    c.getEmail(), c.getAddress()
            });
        }
        updateStatus();
    }

    private void updateStatus() {
        int n = directory.size();
        int u = undoStack.size();
        int r = redoStack.size();
        lblCountBadge.setText(n + (n == 1 ? " contact" : " contacts"));
        lblStatus.setText("Ready  |  Undo: " + u + "  |  Redo: " + r);
    }

    // ==========================================================
    //  AUTO-SAVE / AUTO-LOAD
    // ==========================================================

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(projectDir + File.separator + DATA_FILENAME))) {
            for (Contact c : directory) {
                bw.write(escapeCSV(c.getName())
                        + "," + escapeCSV(c.getPhone())
                        + "," + escapeCSV(c.getEmail())
                        + "," + escapeCSV(c.getAddress()));
                bw.newLine();
            }
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    private void loadFromFile() {
        File file = new File(projectDir + File.separator + DATA_FILENAME);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            directory.clear();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = parseCSVLine(line);
                String name    = p.length > 0 ? p[0] : "";
                String phone   = p.length > 1 ? p[1] : "";
                String email   = p.length > 2 ? p[2] : "";
                String address = p.length > 3 ? p[3] : "";
                if (!name.isEmpty())
                    directory.add(new Contact(name, phone, email, address));
            }
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    // ==========================================================
    //  CSV HELPERS
    // ==========================================================

    private String escapeCSV(String v) {
        if (v == null) return "";
        if (v.contains(",") || v.contains("\"") || v.contains("\n"))
            return "\"" + v.replace("\"", "\"\"") + "\"";
        return v;
    }

    private String[] parseCSVLine(String line) {
        ArrayList<String> f = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean q = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (q) {
                if (ch == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"')
                    { sb.append('"'); i++; }
                    else q = false;
                } else sb.append(ch);
            } else {
                if (ch == '"')      q = true;
                else if (ch == ',') { f.add(sb.toString()); sb.setLength(0); }
                else                sb.append(ch);
            }
        }
        f.add(sb.toString());
        return f.toArray(new String[0]);
    }

    // ==========================================================
    //  MERGE SORT  –  O(n log n)
    // ==========================================================

    private void mergeSort(List<Contact> list, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    private void merge(List<Contact> list, int l, int m, int r) {
        int n1 = m - l + 1, n2 = r - m;
        ArrayList<Contact> L = new ArrayList<>(), R = new ArrayList<>();
        for (int i = 0; i < n1; i++) L.add(list.get(l + i));
        for (int j = 0; j < n2; j++) R.add(list.get(m + 1 + j));

        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L.get(i).getName()
                    .compareToIgnoreCase(R.get(j).getName()) <= 0)
                { list.set(k, L.get(i)); i++; }
            else
                { list.set(k, R.get(j)); j++; }
            k++;
        }
        while (i < n1) { list.set(k, L.get(i)); i++; k++; }
        while (j < n2) { list.set(k, R.get(j)); j++; k++; }
    }

    // ==========================================================
    //  BINARY SEARCH  –  O(log n)
    // ==========================================================

    private int binarySearch(List<Contact> list, String target) {
        int left = 0, right = list.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = list.get(mid).getName().compareToIgnoreCase(target);
            if (cmp == 0)     return mid;
            else if (cmp < 0) left  = mid + 1;
            else              right = mid - 1;
        }
        return -1;
    }

    // ==========================================================
    //  MAIN
    // ==========================================================

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new Caller().setVisible(true));
    }
}

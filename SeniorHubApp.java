import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.text.SimpleDateFormat;

// === DOMAIN LAYER ===

class Student {
    String studentId, name, email, password;
    double cgpa;
    String groupId;
    List<String> appliedJobs = new ArrayList<>();
    boolean transcriptRequested = false;

    Student(String id, String name, String email, String pass, double cgpa) {
        this.studentId=id; this.name=name; this.email=email; this.password=pass; this.cgpa=cgpa;
    }
    boolean login(String e, String p) { return email.equalsIgnoreCase(e) && password.equals(p); }
    void register() {}
    void joinGroup() {}
    String getName() { return name; }
    String getStudentId() { return studentId; }
    double getCgpa() { return cgpa; }
}

class Faculty {
    String facultyId, name, email, researchDomain;
    int groupLimit;

    Faculty(String id, String name, String email, String domain, int limit) {
        this.facultyId=id; this.name=name; this.email=email; this.researchDomain=domain; this.groupLimit=limit;
    }
    boolean login(String e, String p) { return email.equalsIgnoreCase(e) && p.equals("admin123"); }
    void reviewSupervisionRequest() {}
    String getName() { return name; }
    String getResearchDomain() { return researchDomain; }
}

class FYP_Group {
    String groupId, projectTitle, inviteCode;
    List<Student> members = new ArrayList<>();
    Faculty supervisor;

    FYP_Group(String id, String title, String code) {
        this.groupId=id; this.projectTitle=title; this.inviteCode=code;
    }
    void createGroup() {}
    void addMember(Student s) { members.add(s); s.groupId=groupId; }
    String getInviteCode() { return inviteCode; }
}

class Supervision_Request {
    String requestId, proposalDoc, status;
    Faculty target;

    Supervision_Request(String id, String doc) {
        this.requestId=id; this.proposalDoc=doc; this.status="Pending";
    }
    void sendRequest(Faculty f) { this.target=f; }
    void updateStatus(String s) { this.status=s; }
}

class Deliverable {
    String id, title, milestone, studentId;
    String content;
    String grade, feedback;
    Date timestamp;

    Deliverable(String id, String title, String milestone, String studentId, String content) {
        this.id=id; this.title=title; this.milestone=milestone; this.studentId=studentId;
        this.content=content; this.timestamp=new Date();
    }
}

class Clearance_Record {
    String studentId;
    Map<String,String> statuses = new LinkedHashMap<>();

    Clearance_Record(String sid) {
        this.studentId=sid;
        statuses.put("Finance","Pending");
        statuses.put("Library","Pending");
        statuses.put("Labs","Pending");
        statuses.put("Hostel","Pending");
        statuses.put("IT Department","Pending");
    }
}

class Job_Posting {
    String jobId, title, company, description, deadline;
    List<String> applicants = new ArrayList<>();

    Job_Posting(String id, String title, String company, String desc, String deadline) {
        this.jobId=id; this.title=title; this.company=company; this.description=desc; this.deadline=deadline;
    }
}

class Alumni {
    String alumniId, name, company, jobTitle, email;
    List<String> messages = new ArrayList<>();

    Alumni(String id, String name, String company, String title, String email) {
        this.alumniId=id; this.name=name; this.company=company; this.jobTitle=title; this.email=email;
    }
}

class SystemAdmin {
    String adminId, name, email, password;

    SystemAdmin(String id, String name, String email, String pass) {
        this.adminId=id; this.name=name; this.email=email; this.password=pass;
    }
    boolean login(String e, String p) { return email.equalsIgnoreCase(e) && password.equals(p); }
}

// === DATA STORAGE (SINGLETON) ===

class AppData {
    private static AppData instance;
    List<Student> students = new ArrayList<>();
    List<Faculty> faculties = new ArrayList<>();
    List<FYP_Group> groups = new ArrayList<>();
    List<Supervision_Request> requests = new ArrayList<>();
    List<Deliverable> deliverables = new ArrayList<>();
    List<Clearance_Record> clearances = new ArrayList<>();
    List<Job_Posting> jobs = new ArrayList<>();
    List<Alumni> alumni = new ArrayList<>();
    List<SystemAdmin> admins = new ArrayList<>();
    String[][] milestones = {
        {"M1","FYP Proposal Submission","2026-06-15","High"},
        {"M2","Literature Review","2026-07-20","Medium"},
        {"M3","Mid-Term Presentation","2026-09-01","High"},
        {"M4","Implementation Phase 1","2026-10-15","High"},
        {"M5","Final Report Submission","2026-11-30","High"},
        {"M6","Final Viva/Defense","2026-12-15","High"}
    };

    private AppData() {
        students.add(new Student("2024-STUD-001","Hammad","student@uni.edu","pass123",3.85));
        students.add(new Student("2024-STUD-002","Muqeet","muqeet@uni.edu","pass123",3.72));
        students.add(new Student("2024-STUD-003","Ali","ali@uni.edu","pass123",3.60));
        faculties.add(new Faculty("FAC-201","Dr. Smith","smith@uni.edu","Artificial Intelligence",3));
        faculties.add(new Faculty("FAC-202","Dr. Jane","jane@uni.edu","Software Engineering",2));
        faculties.add(new Faculty("FAC-203","Dr. Ahmed","ahmed@uni.edu","Data Science",3));
        admins.add(new SystemAdmin("ADM-01","Admin","admin@uni.edu","admin123"));
        clearances.add(new Clearance_Record("2024-STUD-001"));
        clearances.add(new Clearance_Record("2024-STUD-002"));
        clearances.add(new Clearance_Record("2024-STUD-003"));
        jobs.add(new Job_Posting("JOB-01","Junior Developer","TechCorp","Java/Spring Boot developer role","2026-07-01"));
        jobs.add(new Job_Posting("JOB-02","Data Analyst Intern","DataFlow Inc","Python/SQL analytics intern","2026-06-15"));
        jobs.add(new Job_Posting("JOB-03","UI/UX Designer","DesignHub","Figma-based design role","2026-08-01"));
        alumni.add(new Alumni("ALM-01","Sarah Khan","Google","Software Engineer","sarah@alumni.edu"));
        alumni.add(new Alumni("ALM-02","Omar Farooq","Microsoft","PM Lead","omar@alumni.edu"));
        alumni.add(new Alumni("ALM-03","Ayesha Malik","Careem","Data Scientist","ayesha@alumni.edu"));
    }

    static AppData getInstance() {
        if (instance==null) instance=new AppData();
        return instance;
    }

    Clearance_Record getClearance(String sid) {
        for (Clearance_Record c : clearances) if (c.studentId.equals(sid)) return c;
        return null;
    }
}
// === CUSTOM UI ===

class RoundedBorder extends AbstractBorder {
    int r; Color c;
    RoundedBorder(int r, Color c){this.r=r;this.c=c;}
    public void paintBorder(Component comp,Graphics g,int x,int y,int w,int h){
        Graphics2D g2=(Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c); g2.drawRoundRect(x,y,w-1,h-1,r,r); g2.dispose();
    }
    public Insets getBorderInsets(Component c){return new Insets(8,12,8,12);}
}

class RBtn extends JButton {
    Color bg,hv,pr; int r=12;
    RBtn(String t,Color bg){
        super(t);this.bg=bg;hv=bg.brighter();pr=bg.darker();
        setFont(new Font("SansSerif",Font.BOLD,13));setForeground(Color.WHITE);
        setFocusPainted(false);setBorderPainted(false);setContentAreaFilled(false);setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    protected void paintComponent(Graphics g){
        Graphics2D g2=(Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getModel().isPressed()?pr:getModel().isRollover()?hv:bg);
        g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r);g2.dispose();
        super.paintComponent(g);
    }
}

class SideBtn extends JButton {
    boolean active=false;
    static final Color ACTIVE=new Color(99,102,241), HOVER=new Color(51,65,85), NORMAL=new Color(30,41,59);
    SideBtn(String t){
        super(t);setFont(new Font("SansSerif",Font.PLAIN,13));setForeground(new Color(200,210,230));
        setHorizontalAlignment(LEFT);setBorderPainted(false);setFocusPainted(false);
        setContentAreaFilled(false);setOpaque(false);setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(10,20,10,20));setMaximumSize(new Dimension(220,42));
    }
    void setActive(boolean a){this.active=a;repaint();}
    protected void paintComponent(Graphics g){
        Graphics2D g2=(Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        if(active)g2.setColor(ACTIVE);else if(getModel().isRollover())g2.setColor(HOVER);else g2.setColor(NORMAL);
        g2.fillRoundRect(4,2,getWidth()-8,getHeight()-4,10,10);g2.dispose();
        super.paintComponent(g);
    }
}
// === MAIN APP CLASS (Part 3: Login, Register, Reset) ===

public class SeniorHubApp extends JFrame {
    CardLayout cardLayout = new CardLayout();
    JPanel mainContainer = new JPanel(cardLayout);
    Object currentUser;

    static final Color BG=new Color(15,23,42),CARD=new Color(30,41,59),ACC=new Color(99,102,241);
    static final Color GRN=new Color(16,185,129),TXT=new Color(241,245,249),TXT2=new Color(148,163,184);
    static final Color BRD=new Color(51,65,85),INP=new Color(30,41,59);

    public SeniorHubApp(){
        setTitle("SeniorHub | FYP Management Platform");setSize(900,650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);setLocationRelativeTo(null);
        mainContainer.setBackground(BG);
        initLogin();initRegister();initReset();
        add(mainContainer);setVisible(true);
    }

    JPanel gradBg(){
        return new JPanel(new GridBagLayout()){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);Graphics2D g2=(Graphics2D)g;
                g2.setPaint(new GradientPaint(0,0,BG,getWidth(),getHeight(),new Color(30,27,75)));
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
    }

    JTextField field(int cols){
        JTextField f=new JTextField(cols);f.setFont(new Font("SansSerif",Font.PLAIN,14));
        f.setBackground(INP);f.setForeground(TXT);f.setCaretColor(TXT);
        f.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10,BRD),BorderFactory.createEmptyBorder(4,6,4,6)));
        return f;
    }

    JPasswordField passField(){
        JPasswordField f=new JPasswordField(22);f.setFont(new Font("SansSerif",Font.PLAIN,14));
        f.setBackground(INP);f.setForeground(TXT);f.setCaretColor(TXT);
        f.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10,BRD),BorderFactory.createEmptyBorder(4,6,4,6)));
        return f;
    }

    JLabel lbl(String t,int sz,boolean b){
        JLabel l=new JLabel(t);l.setFont(new Font("SansSerif",b?Font.BOLD:Font.PLAIN,sz));
        l.setForeground(b?TXT:TXT2);return l;
    }

    JPanel card(int pad){
        JPanel c=new JPanel();c.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));c.setBackground(CARD);
        c.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(16,BRD),new EmptyBorder(pad,45,pad,45)));
        return c;
    }

    void addRow(JPanel p,String label,JComponent comp, float alignX){
        JPanel row=new JPanel();row.setLayout(new BoxLayout(row,BoxLayout.Y_AXIS));row.setOpaque(false);
        JLabel l=lbl(label,12,false);l.setAlignmentX(0);row.add(l);row.add(Box.createVerticalStrut(6));
        comp.setMaximumSize(new Dimension(300,42));comp.setAlignmentX(0);row.add(comp);
        row.setMaximumSize(new Dimension(300, 100));
        row.setAlignmentX(alignX);p.add(row);p.add(Box.createVerticalStrut(14));
    }

    void addRow(JPanel p,String label,JComponent comp){
        addRow(p,label,comp,0f); // default left
    }

    void initLogin(){
        JPanel bg=gradBg();JPanel c=card(35);
        JLabel t=lbl("SeniorHub",28,true);t.setAlignmentX(0.5f);c.add(t);
        JLabel s=lbl("Final Year Project Portal",13,false);s.setAlignmentX(0.5f);c.add(s);
        c.add(Box.createVerticalStrut(25));
        JTextField ef=field(22);addRow(c,"University Email",ef, 0.5f);
        JPasswordField pf=passField();addRow(c,"Password",pf, 0.5f);
        RBtn btn=new RBtn("Sign In",ACC);btn.setMaximumSize(new Dimension(300,44));
        btn.setAlignmentX(0.5f);c.add(btn);c.add(Box.createVerticalStrut(12));

        JPanel links=new JPanel(new FlowLayout(FlowLayout.CENTER));links.setOpaque(false);
        JButton reg=linkBtn("Create Account");reg.addActionListener(e->cardLayout.show(mainContainer,"REGISTER"));
        JButton rst=linkBtn("Forgot Password?");rst.addActionListener(e->cardLayout.show(mainContainer,"RESET"));
        links.add(reg);links.add(new JLabel("  |  "){{setForeground(TXT2);}});links.add(rst);
        c.add(links);

        btn.addActionListener(e->{
            String em=ef.getText(),ps=new String(pf.getPassword());
            if(em.isEmpty()||ps.isEmpty()){msg("Please enter all credentials.");return;}
            for(Student st:AppData.getInstance().students)if(st.login(em,ps)){currentUser=st;showStudentDash();return;}
            for(Faculty f:AppData.getInstance().faculties)if(f.login(em,ps)){currentUser=f;showFacultyDash();return;}
            for(SystemAdmin a:AppData.getInstance().admins)if(a.login(em,ps)){currentUser=a;showAdminDash();return;}
            msg("Access Denied: Invalid Credentials.");
        });
        bg.add(c);mainContainer.add(bg,"LOGIN");
    }

    void initRegister(){
        JPanel bg=gradBg();JPanel c=card(30);
        JLabel t=lbl("Create Account",24,true);t.setAlignmentX(0.5f);c.add(t);c.add(Box.createVerticalStrut(20));
        JTextField nf=field(22),idf=field(22),ef=field(22);
        JPasswordField pf=passField();
        addRow(c,"Full Name",nf,0.5f);addRow(c,"Student ID",idf,0.5f);addRow(c,"University Email",ef,0.5f);addRow(c,"Password",pf,0.5f);
        RBtn btn=new RBtn("Register",GRN);btn.setMaximumSize(new Dimension(300,44));btn.setAlignmentX(0.5f);c.add(btn);
        c.add(Box.createVerticalStrut(10));
        JButton back=linkBtn("Back to Login");back.addActionListener(e->cardLayout.show(mainContainer,"LOGIN"));
        back.setAlignmentX(0.5f);c.add(back);
        btn.addActionListener(e->{
            if(nf.getText().isEmpty()||idf.getText().isEmpty()||ef.getText().isEmpty()||pf.getPassword().length==0){msg("Fill all fields.");return;}
            AppData.getInstance().students.add(new Student(idf.getText(),nf.getText(),ef.getText(),new String(pf.getPassword()),0.0));
            AppData.getInstance().clearances.add(new Clearance_Record(idf.getText()));
            JOptionPane.showMessageDialog(this,"Registration successful! Please login."); cardLayout.show(mainContainer,"LOGIN");
        });
        bg.add(c);mainContainer.add(bg,"REGISTER");
    }

    void initReset(){
        JPanel bg=gradBg();JPanel c=card(35);
        JLabel t=lbl("Reset Password",24,true);t.setAlignmentX(0.5f);c.add(t);c.add(Box.createVerticalStrut(20));
        JTextField ef=field(22);addRow(c,"University Email",ef,0.5f);
        RBtn btn=new RBtn("Send Reset Link",ACC);btn.setMaximumSize(new Dimension(300,44));btn.setAlignmentX(0.5f);c.add(btn);
        c.add(Box.createVerticalStrut(10));
        JButton back=linkBtn("Back to Login");back.addActionListener(e->cardLayout.show(mainContainer,"LOGIN"));
        back.setAlignmentX(0.5f);c.add(back);
        btn.addActionListener(e->{
            if(ef.getText().isEmpty()){msg("Enter your email.");return;}
            JOptionPane.showMessageDialog(this,"Password reset link sent to "+ef.getText());
            cardLayout.show(mainContainer,"LOGIN");
        });
        bg.add(c);mainContainer.add(bg,"RESET");
    }

    JButton linkBtn(String t){
        JButton b=new JButton(t);b.setFont(new Font("SansSerif",Font.PLAIN,12));
        b.setForeground(ACC);b.setBorderPainted(false);b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;
    }
    void msg(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
// === STUDENT DASHBOARD ===

    void showStudentDash(){
        Student s=(Student)currentUser;
        JPanel dash=new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){super.paintComponent(g);Graphics2D g2=(Graphics2D)g;
                g2.setPaint(new GradientPaint(0,0,BG,getWidth(),getHeight(),new Color(30,27,75)));
                g2.fillRect(0,0,getWidth(),getHeight());}
        };

        // Sidebar
        JPanel side=new JPanel();side.setLayout(new BoxLayout(side,BoxLayout.Y_AXIS));
        side.setBackground(new Color(15,23,42));side.setPreferredSize(new Dimension(200,0));
        side.setBorder(new EmptyBorder(20,0,20,0));
        JLabel logo=lbl("  SeniorHub",18,true);logo.setAlignmentX(0.5f);side.add(logo);side.add(Box.createVerticalStrut(8));
        JLabel sub=lbl("  "+s.getName(),11,false);sub.setAlignmentX(0.5f);side.add(sub);side.add(Box.createVerticalStrut(20));

        CardLayout cl=new CardLayout();JPanel content=new JPanel(cl);content.setOpaque(false);
        String[] labels={"Profile","FYP Group","Faculty Dir","Supervision Req","Milestones","Deliverables","Job Board","Alumni","Clearance","Transcript"};
        String[] keys={"PROF","GRP","FAC","SUP","MILE","DEL","JOB","ALM","CLR","TRN"};
        List<SideBtn> btns=new ArrayList<>();

        for(int i=0;i<labels.length;i++){
            SideBtn b=new SideBtn(labels[i]);btns.add(b);final int idx=i;
            b.addActionListener(e->{for(SideBtn x:btns)x.setActive(false);b.setActive(true);cl.show(content,keys[idx]);});
            side.add(b);side.add(Box.createVerticalStrut(2));
        }
        side.add(Box.createVerticalGlue());
        SideBtn logout=new SideBtn("Logout");logout.setForeground(new Color(239,68,68));
        logout.addActionListener(e->cardLayout.show(mainContainer,"LOGIN"));side.add(logout);
        btns.get(0).setActive(true);

        // Profile (FR-03)
        JPanel prof=sectionPanel("My Profile");
        JTextField pName=field(20);pName.setText(s.name);JTextField pEmail=field(20);pEmail.setText(s.email);
        JTextField pId=field(20);pId.setText(s.studentId);pId.setEditable(false);
        addRow(prof,"Full Name",pName);addRow(prof,"Email",pEmail);addRow(prof,"Student ID",pId);
        JLabel cgLbl=lbl("CGPA: "+s.cgpa,14,true);cgLbl.setAlignmentX(0);prof.add(cgLbl);prof.add(Box.createVerticalStrut(15));
        RBtn saveProf=new RBtn("Save Changes",GRN);saveProf.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));saveProf.setAlignmentX(0);
        saveProf.addActionListener(e->{s.name=pName.getText();s.email=pEmail.getText();JOptionPane.showMessageDialog(this,"Profile updated!");});
        prof.add(saveProf);
        content.add(wrap(prof),"PROF");

        // FYP Group (FR-05,FR-06)
        JPanel grp=sectionPanel("FYP Group Management");
        JTextField projTitle=field(20);addRow(grp,"Project Title",projTitle);
        RBtn createGrp=new RBtn("Create Group",ACC);createGrp.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));createGrp.setAlignmentX(0);
        grp.add(createGrp);grp.add(Box.createVerticalStrut(20));
        JLabel orLbl=lbl("— OR Join Existing Group —",13,false);orLbl.setAlignmentX(0.5f);grp.add(orLbl);grp.add(Box.createVerticalStrut(10));
        JTextField codeF=field(20);addRow(grp,"Invite Code",codeF);
        RBtn joinGrp=new RBtn("Join Group",GRN);joinGrp.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));joinGrp.setAlignmentX(0);
        grp.add(joinGrp);
        JLabel grpStatus=lbl("",12,false);grpStatus.setAlignmentX(0);grp.add(Box.createVerticalStrut(10));grp.add(grpStatus);

        createGrp.addActionListener(e->{
            if(projTitle.getText().trim().isEmpty()){msg("Enter project title.");return;}
            if(s.groupId!=null){msg("Already in a group.");return;}
            String code="GRP-"+(int)(Math.random()*9000+1000);
            FYP_Group g=new FYP_Group("G-"+System.currentTimeMillis(),projTitle.getText(),code);
            g.addMember(s);AppData.getInstance().groups.add(g);
            grpStatus.setText("Group created! Invite code: "+code);grpStatus.setForeground(GRN);
        });
        joinGrp.addActionListener(e->{
            if(codeF.getText().trim().isEmpty()){msg("Enter invite code.");return;}
            if(s.groupId!=null){msg("Already in a group.");return;}
            for(FYP_Group g:AppData.getInstance().groups){
                if(g.getInviteCode().equals(codeF.getText().trim())){
                    if(g.members.size()>=3){msg("Group is full (max 3).");return;}
                    g.addMember(s);grpStatus.setText("Joined group: "+g.projectTitle);grpStatus.setForeground(GRN);return;
                }
            }
            msg("Invalid invite code.");
        });
        content.add(wrap(grp),"GRP");

        // Faculty Directory (FR-07)
        JPanel fac=sectionPanel("Faculty Directory");
        for(Faculty f:AppData.getInstance().faculties){
            JPanel fc=new JPanel(new BorderLayout());fc.setBackground(new Color(51,65,85));
            fc.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10,BRD),new EmptyBorder(12,16,12,16)));
            fc.setMaximumSize(new Dimension(Integer.MAX_VALUE,70));fc.setAlignmentX(0);
            JLabel fn=lbl(f.getName(),15,true);JLabel fd=lbl(f.getResearchDomain(),12,false);
            JPanel info=new JPanel();info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS));info.setOpaque(false);
            info.add(fn);info.add(fd);fc.add(info,BorderLayout.CENTER);
            fac.add(fc);fac.add(Box.createVerticalStrut(8));
        }
        content.add(wrap(fac),"FAC");

        // Supervision Request (FR-08,FR-09)
        JPanel sup=sectionPanel("Submit Supervision Request");
        JComboBox<String> facBox=new JComboBox<>();facBox.setBackground(INP);facBox.setForeground(TXT);
        facBox.setFont(new Font("SansSerif",Font.PLAIN,13));
        for(Faculty f:AppData.getInstance().faculties)facBox.addItem(f.getName()+" — "+f.getResearchDomain());
        facBox.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));facBox.setAlignmentX(0);
        JLabel sl=lbl("Supervisor",12,false);sl.setAlignmentX(0);sup.add(sl);sup.add(Box.createVerticalStrut(6));sup.add(facBox);sup.add(Box.createVerticalStrut(14));
        JTextArea propTxt=new JTextArea(5,30);propTxt.setBackground(INP);propTxt.setForeground(TXT);propTxt.setCaretColor(TXT);
        propTxt.setFont(new Font("SansSerif",Font.PLAIN,13));propTxt.setLineWrap(true);propTxt.setWrapStyleWord(true);
        propTxt.setBorder(new EmptyBorder(8,8,8,8));
        JScrollPane sp=new JScrollPane(propTxt);sp.setBorder(new RoundedBorder(10,BRD));sp.setAlignmentX(0);
        JLabel pl=lbl("Proposal Document",12,false);pl.setAlignmentX(0);sup.add(pl);sup.add(Box.createVerticalStrut(6));sup.add(sp);sup.add(Box.createVerticalStrut(14));
        RBtn subBtn=new RBtn("Submit Request",GRN);subBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));subBtn.setAlignmentX(0);
        subBtn.addActionListener(e->{
            if(propTxt.getText().trim().isEmpty()){msg("Proposal cannot be blank.");return;}
            Supervision_Request r=new Supervision_Request("REQ-"+System.currentTimeMillis(),propTxt.getText());
            Faculty tf=AppData.getInstance().faculties.get(facBox.getSelectedIndex());r.sendRequest(tf);
            AppData.getInstance().requests.add(r);
            JOptionPane.showMessageDialog(this,"Request sent to "+tf.getName()+"!");propTxt.setText("");
        });
        sup.add(subBtn);
        content.add(wrap(sup),"SUP");

        // Milestones (FR-10)
        JPanel mile=sectionPanel("FYP Milestones & Deadlines");
        String[][] ms=AppData.getInstance().milestones;
        for(String[] m:ms){
            JPanel mc=new JPanel(new BorderLayout());mc.setBackground(new Color(51,65,85));
            mc.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10,BRD),new EmptyBorder(10,16,10,16)));
            mc.setMaximumSize(new Dimension(Integer.MAX_VALUE,60));mc.setAlignmentX(0);
            JPanel mi=new JPanel();mi.setLayout(new BoxLayout(mi,BoxLayout.Y_AXIS));mi.setOpaque(false);
            mi.add(lbl(m[0]+": "+m[1],14,true));mi.add(lbl("Deadline: "+m[2]+" | Priority: "+m[3],11,false));
            mc.add(mi);mile.add(mc);mile.add(Box.createVerticalStrut(6));
        }
        content.add(wrap(mile),"MILE");

        // Deliverables (FR-11)
        JPanel del=sectionPanel("Submit Deliverable");
        JTextField delTitle=field(20);addRow(del,"Deliverable Title",delTitle);
        JComboBox<String> mileBox=new JComboBox<>();mileBox.setBackground(INP);mileBox.setForeground(TXT);
        for(String[] m:ms)mileBox.addItem(m[0]+": "+m[1]);
        mileBox.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));mileBox.setAlignmentX(0);
        JLabel ml2=lbl("Milestone",12,false);ml2.setAlignmentX(0);del.add(ml2);del.add(Box.createVerticalStrut(6));del.add(mileBox);del.add(Box.createVerticalStrut(14));
        JTextArea delContent=new JTextArea(4,30);delContent.setBackground(INP);delContent.setForeground(TXT);delContent.setCaretColor(TXT);
        delContent.setFont(new Font("SansSerif",Font.PLAIN,13));delContent.setLineWrap(true);delContent.setBorder(new EmptyBorder(8,8,8,8));
        JScrollPane dsp=new JScrollPane(delContent);dsp.setBorder(new RoundedBorder(10,BRD));dsp.setAlignmentX(0);
        JLabel dl2=lbl("Content / Report",12,false);dl2.setAlignmentX(0);del.add(dl2);del.add(Box.createVerticalStrut(6));del.add(dsp);del.add(Box.createVerticalStrut(14));
        RBtn delBtn=new RBtn("Submit Deliverable",GRN);delBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));delBtn.setAlignmentX(0);
        delBtn.addActionListener(e->{
            if(delTitle.getText().trim().isEmpty()||delContent.getText().trim().isEmpty()){msg("Fill all fields.");return;}
            AppData.getInstance().deliverables.add(new Deliverable("DEL-"+System.currentTimeMillis(),delTitle.getText(),(String)mileBox.getSelectedItem(),s.studentId,delContent.getText()));
            JOptionPane.showMessageDialog(this,"Deliverable submitted!");delTitle.setText("");delContent.setText("");
        });
        del.add(delBtn);
        content.add(wrap(del),"DEL");

        // Job Board (FR-13,FR-14)
        JPanel job=sectionPanel("Job & Internship Board");
        for(Job_Posting j:AppData.getInstance().jobs){
            JPanel jc=new JPanel(new BorderLayout());jc.setBackground(new Color(51,65,85));
            jc.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10,BRD),new EmptyBorder(12,16,12,16)));
            jc.setMaximumSize(new Dimension(Integer.MAX_VALUE,90));jc.setAlignmentX(0);
            JPanel ji=new JPanel();ji.setLayout(new BoxLayout(ji,BoxLayout.Y_AXIS));ji.setOpaque(false);
            ji.add(lbl(j.title,14,true));ji.add(lbl(j.company+" | Deadline: "+j.deadline,11,false));
            ji.add(lbl(j.description,11,false));jc.add(ji,BorderLayout.CENTER);
            RBtn applyBtn=new RBtn("Apply",ACC);applyBtn.setPreferredSize(new Dimension(80,32));
            applyBtn.addActionListener(e->{
                if(s.appliedJobs.contains(j.jobId)){msg("Already applied.");return;}
                s.appliedJobs.add(j.jobId);j.applicants.add(s.studentId);
                JOptionPane.showMessageDialog(this,"Applied to "+j.title+"!");
            });
            jc.add(applyBtn,BorderLayout.EAST);job.add(jc);job.add(Box.createVerticalStrut(8));
        }
        content.add(wrap(job),"JOB");

        // Alumni (FR-15,FR-16)
        JPanel alm=sectionPanel("Alumni Directory");
        for(Alumni a:AppData.getInstance().alumni){
            JPanel ac=new JPanel(new BorderLayout());ac.setBackground(new Color(51,65,85));
            ac.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10,BRD),new EmptyBorder(12,16,12,16)));
            ac.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));ac.setAlignmentX(0);
            JPanel ai=new JPanel();ai.setLayout(new BoxLayout(ai,BoxLayout.Y_AXIS));ai.setOpaque(false);
            ai.add(lbl(a.name,14,true));ai.add(lbl(a.jobTitle+" at "+a.company,11,false));
            ac.add(ai,BorderLayout.CENTER);
            RBtn msgBtn=new RBtn("Message",ACC);msgBtn.setPreferredSize(new Dimension(90,32));
            msgBtn.addActionListener(e->{
                String m=JOptionPane.showInputDialog(this,"Message to "+a.name+":");
                if(m!=null&&!m.trim().isEmpty()){a.messages.add(s.name+": "+m);JOptionPane.showMessageDialog(this,"Message sent!");}
            });
            ac.add(msgBtn,BorderLayout.EAST);alm.add(ac);alm.add(Box.createVerticalStrut(8));
        }
        content.add(wrap(alm),"ALM");

        // Clearance (FR-17)
        JPanel clr=sectionPanel("Graduation Clearance Checklist");
        Clearance_Record cr=AppData.getInstance().getClearance(s.studentId);
        if(cr!=null){
            for(Map.Entry<String,String> en:cr.statuses.entrySet()){
                JPanel row=new JPanel(new BorderLayout());row.setBackground(new Color(51,65,85));
                row.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10,BRD),new EmptyBorder(10,16,10,16)));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE,50));row.setAlignmentX(0);
                row.add(lbl(en.getKey(),14,true),BorderLayout.WEST);
                JLabel st=lbl(en.getValue(),13,true);
                st.setForeground(en.getValue().equals("Cleared")?GRN:new Color(251,191,36));
                row.add(st,BorderLayout.EAST);clr.add(row);clr.add(Box.createVerticalStrut(6));
            }
        }
        content.add(wrap(clr),"CLR");

        // Transcript (FR-19)
        JPanel trn=sectionPanel("Request Provisional Transcript");
        JLabel tInfo=lbl("Student: "+s.name+" ("+s.studentId+")",14,true);tInfo.setAlignmentX(0);trn.add(tInfo);trn.add(Box.createVerticalStrut(6));
        JLabel tCgpa=lbl("Current CGPA: "+s.cgpa,13,false);tCgpa.setAlignmentX(0);trn.add(tCgpa);trn.add(Box.createVerticalStrut(20));
        JLabel tStatus=lbl(s.transcriptRequested?"Status: Request Submitted":"Status: Not Requested",13,false);tStatus.setAlignmentX(0);
        RBtn tBtn=new RBtn("Request Transcript",ACC);tBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));tBtn.setAlignmentX(0);
        tBtn.addActionListener(e->{
            if(s.transcriptRequested){msg("Already requested.");return;}
            s.transcriptRequested=true;tStatus.setText("Status: Request Submitted");tStatus.setForeground(GRN);
            JOptionPane.showMessageDialog(this,"Transcript request submitted!");
        });
        trn.add(tBtn);trn.add(Box.createVerticalStrut(10));trn.add(tStatus);
        content.add(wrap(trn),"TRN");

        dash.add(side,BorderLayout.WEST);dash.add(content,BorderLayout.CENTER);
        mainContainer.add(dash,"STUDENT_DASH");cardLayout.show(mainContainer,"STUDENT_DASH");
    }

    JPanel sectionPanel(String title){
        JPanel p=new JPanel();p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(14,BRD),new EmptyBorder(25,30,25,30)));
        JLabel t=lbl(title,20,true);t.setAlignmentX(0);p.add(t);p.add(Box.createVerticalStrut(18));return p;
    }

    JScrollPane wrap(JPanel p){
        JPanel w=new JPanel();w.setLayout(new BoxLayout(w,BoxLayout.Y_AXIS));w.setOpaque(false);
        w.setBorder(new EmptyBorder(25,25,25,25));p.setAlignmentX(0);w.add(p);
        JScrollPane sp=new JScrollPane(w);sp.setOpaque(false);sp.getViewport().setOpaque(false);sp.setBorder(null);return sp;
    }
// === FACULTY DASHBOARD (FR-12) ===

    void showFacultyDash(){
        Faculty f=(Faculty)currentUser;
        JPanel dash=new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){super.paintComponent(g);Graphics2D g2=(Graphics2D)g;
                g2.setPaint(new GradientPaint(0,0,BG,getWidth(),getHeight(),new Color(30,27,75)));
                g2.fillRect(0,0,getWidth(),getHeight());}
        };
        JPanel side=new JPanel();side.setLayout(new BoxLayout(side,BoxLayout.Y_AXIS));
        side.setBackground(new Color(15,23,42));side.setPreferredSize(new Dimension(200,0));
        side.setBorder(new EmptyBorder(20,0,20,0));
        JLabel logo=lbl("  SeniorHub",18,true);logo.setAlignmentX(0.5f);side.add(logo);
        JLabel sub=lbl("  Faculty Portal",11,false);sub.setAlignmentX(0.5f);side.add(sub);side.add(Box.createVerticalStrut(20));
        JLabel wl=lbl("  "+f.getName(),13,true);wl.setAlignmentX(0.5f);side.add(wl);
        JLabel dl=lbl("  "+f.getResearchDomain(),11,false);dl.setAlignmentX(0.5f);side.add(dl);side.add(Box.createVerticalStrut(20));

        CardLayout cl=new CardLayout();JPanel content=new JPanel(cl);content.setOpaque(false);
        SideBtn revBtn=new SideBtn("Review Deliverables");revBtn.setActive(true);
        SideBtn reqBtn=new SideBtn("View Requests");
        revBtn.addActionListener(e->{revBtn.setActive(true);reqBtn.setActive(false);cl.show(content,"REV");});
        reqBtn.addActionListener(e->{reqBtn.setActive(true);revBtn.setActive(false);cl.show(content,"REQ");});
        side.add(revBtn);side.add(Box.createVerticalStrut(2));side.add(reqBtn);
        side.add(Box.createVerticalGlue());
        SideBtn logout=new SideBtn("Logout");logout.setForeground(new Color(239,68,68));
        logout.addActionListener(e->cardLayout.show(mainContainer,"LOGIN"));side.add(logout);

        // Review Deliverables
        JPanel rev=sectionPanel("Review & Grade Deliverables");
        List<Deliverable> dels=AppData.getInstance().deliverables;
        if(dels.isEmpty()){rev.add(lbl("No deliverables submitted yet.",13,false));}
        else{
            for(Deliverable d:dels){
                JPanel dc=new JPanel(new BorderLayout(10,0));dc.setBackground(new Color(51,65,85));
                dc.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10,BRD),new EmptyBorder(12,16,12,16)));
                dc.setMaximumSize(new Dimension(Integer.MAX_VALUE,100));dc.setAlignmentX(0);
                JPanel di=new JPanel();di.setLayout(new BoxLayout(di,BoxLayout.Y_AXIS));di.setOpaque(false);
                di.add(lbl(d.title,14,true));di.add(lbl("Student: "+d.studentId+" | "+d.milestone,11,false));
                di.add(lbl("Grade: "+(d.grade!=null?d.grade:"Not graded"),11,false));
                dc.add(di,BorderLayout.CENTER);
                RBtn gradeBtn=new RBtn("Grade",ACC);gradeBtn.setPreferredSize(new Dimension(80,32));
                gradeBtn.addActionListener(e->{
                    String g=JOptionPane.showInputDialog(this,"Enter grade for '"+d.title+"':");
                    if(g!=null&&!g.trim().isEmpty()){
                        d.grade=g;d.feedback="Graded by "+f.getName();
                        JOptionPane.showMessageDialog(this,"Graded: "+g);showFacultyDash();
                    }
                });
                dc.add(gradeBtn,BorderLayout.EAST);rev.add(dc);rev.add(Box.createVerticalStrut(8));
            }
        }
        content.add(wrap(rev),"REV");

        // View Requests
        JPanel req=sectionPanel("Supervision Requests");
        List<Supervision_Request> reqs=AppData.getInstance().requests;
        if(reqs.isEmpty()){req.add(lbl("No requests yet.",13,false));}
        else{
            for(Supervision_Request r:reqs){
                JPanel rc=new JPanel(new BorderLayout(10,0));rc.setBackground(new Color(51,65,85));
                rc.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10,BRD),new EmptyBorder(12,16,12,16)));
                rc.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));rc.setAlignmentX(0);
                JPanel ri=new JPanel();ri.setLayout(new BoxLayout(ri,BoxLayout.Y_AXIS));ri.setOpaque(false);
                ri.add(lbl("Request: "+r.requestId,13,true));
                ri.add(lbl("Status: "+r.status,11,false));
                rc.add(ri,BorderLayout.CENTER);
                JPanel btns=new JPanel(new FlowLayout(FlowLayout.RIGHT,4,0));btns.setOpaque(false);
                RBtn acc=new RBtn("Accept",GRN);acc.setPreferredSize(new Dimension(75,30));
                RBtn rej=new RBtn("Reject",new Color(239,68,68));rej.setPreferredSize(new Dimension(75,30));
                acc.addActionListener(e->{r.updateStatus("Accepted");JOptionPane.showMessageDialog(this,"Accepted!");showFacultyDash();});
                rej.addActionListener(e->{r.updateStatus("Rejected");JOptionPane.showMessageDialog(this,"Rejected.");showFacultyDash();});
                btns.add(acc);btns.add(rej);rc.add(btns,BorderLayout.EAST);
                req.add(rc);req.add(Box.createVerticalStrut(8));
            }
        }
        content.add(wrap(req),"REQ");

        dash.add(side,BorderLayout.WEST);dash.add(content,BorderLayout.CENTER);
        mainContainer.add(dash,"FACULTY_DASH");cardLayout.show(mainContainer,"FACULTY_DASH");
    }

// === ADMIN DASHBOARD (FR-18) ===

    void showAdminDash(){
        SystemAdmin a=(SystemAdmin)currentUser;
        JPanel dash=new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){super.paintComponent(g);Graphics2D g2=(Graphics2D)g;
                g2.setPaint(new GradientPaint(0,0,BG,getWidth(),getHeight(),new Color(30,27,75)));
                g2.fillRect(0,0,getWidth(),getHeight());}
        };
        JPanel side=new JPanel();side.setLayout(new BoxLayout(side,BoxLayout.Y_AXIS));
        side.setBackground(new Color(15,23,42));side.setPreferredSize(new Dimension(200,0));
        side.setBorder(new EmptyBorder(20,0,20,0));
        JLabel logo=lbl("  SeniorHub",18,true);logo.setAlignmentX(0.5f);side.add(logo);
        JLabel sub=lbl("  Admin Portal",11,false);sub.setAlignmentX(0.5f);side.add(sub);side.add(Box.createVerticalStrut(20));

        CardLayout cl=new CardLayout();JPanel content=new JPanel(cl);content.setOpaque(false);
        SideBtn clrBtn=new SideBtn("Update Clearance");clrBtn.setActive(true);
        SideBtn jobBtn=new SideBtn("Manage Jobs");
        clrBtn.addActionListener(e->{clrBtn.setActive(true);jobBtn.setActive(false);cl.show(content,"CLR");});
        jobBtn.addActionListener(e->{jobBtn.setActive(true);clrBtn.setActive(false);cl.show(content,"JOB");});
        side.add(clrBtn);side.add(Box.createVerticalStrut(2));side.add(jobBtn);
        side.add(Box.createVerticalGlue());
        SideBtn logout=new SideBtn("Logout");logout.setForeground(new Color(239,68,68));
        logout.addActionListener(e->cardLayout.show(mainContainer,"LOGIN"));side.add(logout);

        // Update Clearance
        JPanel clrP=sectionPanel("Update Student Clearance");
        JComboBox<String> stuBox=new JComboBox<>();stuBox.setBackground(INP);stuBox.setForeground(TXT);
        for(Student s:AppData.getInstance().students)stuBox.addItem(s.getStudentId()+" — "+s.getName());
        stuBox.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));stuBox.setAlignmentX(0);
        JLabel sl=lbl("Select Student",12,false);sl.setAlignmentX(0);clrP.add(sl);clrP.add(Box.createVerticalStrut(6));clrP.add(stuBox);clrP.add(Box.createVerticalStrut(14));
        String[] depts={"Finance","Library","Labs","Hostel","IT Department"};
        JComboBox<String> deptBox=new JComboBox<>(depts);deptBox.setBackground(INP);deptBox.setForeground(TXT);
        deptBox.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));deptBox.setAlignmentX(0);
        JLabel dl=lbl("Department",12,false);dl.setAlignmentX(0);clrP.add(dl);clrP.add(Box.createVerticalStrut(6));clrP.add(deptBox);clrP.add(Box.createVerticalStrut(14));
        String[] statuses={"Pending","Cleared"};
        JComboBox<String> statBox=new JComboBox<>(statuses);statBox.setBackground(INP);statBox.setForeground(TXT);
        statBox.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));statBox.setAlignmentX(0);
        JLabel stl=lbl("Status",12,false);stl.setAlignmentX(0);clrP.add(stl);clrP.add(Box.createVerticalStrut(6));clrP.add(statBox);clrP.add(Box.createVerticalStrut(14));
        RBtn updBtn=new RBtn("Update Status",GRN);updBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));updBtn.setAlignmentX(0);
        updBtn.addActionListener(e->{
            Student sel=AppData.getInstance().students.get(stuBox.getSelectedIndex());
            Clearance_Record cr=AppData.getInstance().getClearance(sel.getStudentId());
            if(cr!=null){cr.statuses.put((String)deptBox.getSelectedItem(),(String)statBox.getSelectedItem());
                JOptionPane.showMessageDialog(this,"Clearance updated for "+sel.getName());}
        });
        clrP.add(updBtn);
        content.add(wrap(clrP),"CLR");

        // Manage Jobs
        JPanel jobP=sectionPanel("Manage Job Postings");
        JTextField jTitle=field(20),jComp=field(20),jDesc=field(20),jDead=field(20);
        addRow(jobP,"Job Title",jTitle);addRow(jobP,"Company",jComp);addRow(jobP,"Description",jDesc);addRow(jobP,"Deadline (YYYY-MM-DD)",jDead);
        RBtn addJob=new RBtn("Add Job Posting",ACC);addJob.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));addJob.setAlignmentX(0);
        addJob.addActionListener(e->{
            if(jTitle.getText().trim().isEmpty()||jComp.getText().trim().isEmpty()){msg("Fill required fields.");return;}
            AppData.getInstance().jobs.add(new Job_Posting("JOB-"+System.currentTimeMillis(),jTitle.getText(),jComp.getText(),jDesc.getText(),jDead.getText()));
            JOptionPane.showMessageDialog(this,"Job posting added!");jTitle.setText("");jComp.setText("");jDesc.setText("");jDead.setText("");
        });
        jobP.add(addJob);
        content.add(wrap(jobP),"JOB");

        dash.add(side,BorderLayout.WEST);dash.add(content,BorderLayout.CENTER);
        mainContainer.add(dash,"ADMIN_DASH");cardLayout.show(mainContainer,"ADMIN_DASH");
    }

    public static void main(String[] args){SwingUtilities.invokeLater(SeniorHubApp::new);}
}

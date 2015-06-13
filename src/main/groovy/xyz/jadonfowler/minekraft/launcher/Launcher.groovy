package xyz.jadonfowler.minekraft.launcher;

//import xyz.jadonfowler.minekraft.Minekraft
import javax.swing.*
import java.awt.Dimension

class Launcher {
    static void main(String[] args) {
        //TODO Create launcher with tumbler page
        JEditorPane jep = new JEditorPane();
        jep.setEditable(false);
        try{
            jep.setPage("https://github.com/phase/minekraft");
        } catch (IOException e){
            jep.setContentType("text/html");
            jep.setText("<html><h1>Minekraft</h1><p>There was an error loading the page!</p></html>");
        }
        JScrollPane scrollPane = new JScrollPane(jep);     
        JFrame f = new JFrame("Minekraft Launcher");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(scrollPane);
        f.setPreferredSize(new Dimension(800,600));
        f.setVisable(true);
    }
}

package projectkos;

import javax.swing.SwingUtilities;
import view.MainFrame;

public class Main {

    public static void main(String[] args) {
        // Jalankan GUI di Event Dispatch Thread (EDT) - cara yang benar untuk Swing
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
    
}

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import view.loginform;

public class main {

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                    new FlatLightLaf()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(
                new Runnable() {

            @Override
            public void run() {

                new loginform().setVisible(true);

            }

        });

    }

}
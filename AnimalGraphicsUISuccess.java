/*  Successful Animal Graphics Display
    Name: Ethan Chen
    Date Started: November 18, 2019
*/

import javax.swing.SwingUtilities;

public class AnimalGraphicsUISuccess { // code sent to me by Mr. Bakker for generic java swing runner file


    /**

     * Launch the application.

     */

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    createAndShowGUI();

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        });

    }




    private static void createAndShowGUI() throws Exception{

        new AnimalForm();

    }


}


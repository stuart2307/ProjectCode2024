import java.awt.Dimension;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUIManager 
{
    private static JFrame frame = new JFrame("Crocodeal");  //Creates the frame to hold all the panels
    private static JPanel lastScreen;
    //INSTANTIATES AND INITIALISES ALL SCREENS
    private static MarketPlaceGUI marketplace;
    private static Login login;
    private static SignUp signup;
    private static AdPanel createAd;
    private static ViewAccount viewAccount;
    private static EditAccount editAccount;
    private static ViewAd viewAd;
    private static NoConnection errorPanel;

    private GUIManager()
    {}

    public static void prepareManager()
        {
            errorPanel = new NoConnection();
            if (DatabaseManager.connection == null)
                {
                    
                    frame.add(errorPanel);
                }
            else
                {
                    marketplace = new MarketPlaceGUI();
                    login = new Login();
                    signup = new SignUp();
                    createAd = new AdPanel();
                    viewAccount = new ViewAccount();
                    editAccount = new EditAccount();
                    viewAd = new ViewAd();
                    frame.add(marketplace);   //Adds the marketplace as the default panel
                }     
            frame.setMinimumSize(new Dimension(640, 480));          //Sets a minimum size for the JFrame
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //Makes it so that the program will terminate upon closing the frame
            frame.setTitle("Crocodeal");                            //Sets the frame's title to Crocodeal
            frame.pack();                                           //Sets the frame to adjust to the size of its components
            frame.setVisible(true);                                 //Makes the frame visible
        }
        

    public static void changeMarketplace(JPanel switchFrom, String search)
        {
            switchFrom.setVisible(false);                           //Sets the current screen's panel to be invisible
            frame.remove(switchFrom);                               //Removes the current screen's panel from the frame
            lastScreen = switchFrom;
            if (checkConnection())                                      //Make sure connection is still valid
            {
                marketplace.generateAds(search);
                marketplace.setVisible(true);                           //Sets the marketplace's panel to be visible
                frame.add(marketplace);                                 //Adds the marketplace's panel to the Frame
            }
        }
    public static void changeLogin(JPanel switchFrom)
        {
            switchFrom.setVisible(false);                           //Sets the current screen's panel to be invisible
            frame.remove(switchFrom);                               //Removes the current screen's panel from the frame
            lastScreen = switchFrom;
            if (checkConnection())                                      //Make sure connection is still valid
            {
                login.setVisible(true);                                 //Sets the login screen's panel to be visible
                frame.add(login);                                       //Adds the login screen's panel to the Frame
            }
        }
    public static void changeSignup(JPanel switchFrom)
        {
            switchFrom.setVisible(false);                           //Sets the current screen's panel to be invisible
            frame.remove(switchFrom);                               //Removes the current screen's panel from the frame
            lastScreen = switchFrom;
            if (checkConnection())                                      //Make sure connection is still valid
            {
                signup.setVisible(true);                                //Sets the sign up screen's panel to be visible
                frame.add(signup);                                      //Adds the sign up screen's panel to the Frame  
            }
        }
    public static void changeCreateAd(JPanel switchFrom)
        {
            switchFrom.setVisible(false);                           //Sets the current screen's panel to be invisible
            frame.remove(switchFrom);                               //Removes the current screen's panel from the frame
            lastScreen = switchFrom;
            if (checkConnection())                                      //Make sure connection is still valid
            {
                createAd.setVisible(true);                              //Sets the ad creation panel to be visible
                frame.add(createAd);                                    //Adds the ad creation panel to the Frame
            }   
        }
    public static void changeAccount(JPanel switchFrom, int id)
        {

            switchFrom.setVisible(false);                           //Sets the current screen's panel to be invisible
            frame.remove(switchFrom);                               //Removes the current screen's panel from the frame
            lastScreen = switchFrom;
            if (checkConnection())                                      //Make sure connection is still valid
            {
                viewAccount.populatePage(id);                           //Populates the view account page using the given account id
                viewAccount.setVisible(true);                           //Sets the view account panel to be visible
                frame.add(viewAccount);                                 //Adds the view account panel to the Frame
            } 
        }
    public static void changeEditAccount(JPanel switchFrom)
        {
            switchFrom.setVisible(false);
            frame.remove(switchFrom);
            lastScreen = switchFrom;
            if (checkConnection())                                      //Make sure connection is still valid
            {
                editAccount.populateEditPage();                         //Populates the edit account page using the given account id
                editAccount.setVisible(true);                     //Sets the edit creation panel to be visible
                frame.add(editAccount);                                 //Adds the edit creation panel to the Frame
            }
        }
    public static void changeViewAd(JPanel switchFrom, int adId)
        {
            switchFrom.setVisible(false);                         //Sets the current screen's panel to be invisible
            frame.remove(switchFrom);                                   //Removes the current screen's panel from the frame
            lastScreen = switchFrom;
            if (checkConnection())                                      //Make sure connection is still valid
            {
                viewAd.populateScreen(adId);                            //Populate the view ad screen with the given ad id
                if(viewAd.getAdAccountId()==CurrentSession.getUserID()) //If ad is owned by current user
                {
                    viewAd.deleteButton.setVisible(true);         //Make delete ad button visible
                    viewAd.footerButtonPanel.add(viewAd.deleteButton);  //
                    viewAd.likeDislikePanel.setVisible(false);
                }
                else
                {
                    viewAd.deleteButton.setVisible(false);
                    viewAd.footerButtonPanel.remove(viewAd.deleteButton);
                }
                viewAd.setVisible(true);
                frame.add(viewAd);
            }
        }
    public static void backButton(JPanel switchFrom)
        {
            switchFrom.setVisible(false);
            frame.remove(switchFrom);
            if (checkConnection())                                      //Make sure connection is still valid
            {
                marketplace.generateAds("");
                lastScreen.setVisible(true);
                frame.add(lastScreen);
            }
        }
    public static void loggedIn()
        {
            marketplace.preLoginButtonPanel.setVisible(false);
            marketplace.topPanel.remove(marketplace.preLoginButtonPanel);
            marketplace.postLoginButtonPanel.setVisible(true);
            marketplace.topPanel.add(marketplace.postLoginButtonPanel);

            viewAd.preLoginButtonPanel.setVisible(false);
            viewAd.topPanel.remove(viewAd.preLoginButtonPanel);
            viewAd.postLoginButtonPanel.setVisible(true);
            viewAd.topPanel.add(viewAd.postLoginButtonPanel);
            viewAd.likeDislikePanel.setVisible(true);
            
            viewAccount.preLoginButtonPanel.setVisible(false);
            viewAccount.topPanel.remove(viewAccount.preLoginButtonPanel);
            viewAccount.postLoginButtonPanel.setVisible(true);
            viewAccount.topPanel.add(viewAccount.postLoginButtonPanel);
        }
    public static void loggedOut()
        {
            marketplace.postLoginButtonPanel.setVisible(false);
            marketplace.topPanel.remove(marketplace.postLoginButtonPanel);
            marketplace.preLoginButtonPanel.setVisible(true);
            marketplace.topPanel.add(marketplace.preLoginButtonPanel);

            viewAd.postLoginButtonPanel.setVisible(false);
            viewAd.topPanel.remove(viewAd.postLoginButtonPanel);
            viewAd.preLoginButtonPanel.setVisible(true);
            viewAd.topPanel.add(viewAd.preLoginButtonPanel);
            viewAd.deleteButton.setVisible(false);
            viewAd.footerButtonPanel.remove(viewAd.deleteButton);
            viewAd.likeDislikePanel.setVisible(false);

            viewAccount.postLoginButtonPanel.setVisible(false);
            viewAccount.topPanel.remove(viewAccount.postLoginButtonPanel);
            viewAccount.preLoginButtonPanel.setVisible(true);
            viewAccount.topPanel.add(viewAccount.preLoginButtonPanel);
        }

    public static Boolean checkConnection()
        {
            try{
            if (DatabaseManager.connection.isValid(1) == false)
                {
                    errorPanel.setVisible(true);
                    frame.add(errorPanel);
                    return false;
                }
            else
                {
                    return true;
                }
            }
            catch (SQLException e)
                {
                    return false;
                }
        }
}

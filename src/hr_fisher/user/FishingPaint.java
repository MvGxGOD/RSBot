package hr_fisher.user;/*
    Name:
    Version:
    Author(s):
    
    Description:
    
*/

import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class FishingPaint {

    public static final String IMAGE_LOCATION = "http://i50.tinypic.com/qz3dok.jpg";
    public static final int NUM_MOUSE_TO_SAVE = 100;

    public static Image img = null;

    public static void setupImage() {
        try {
            URL url = new URL(IMAGE_LOCATION);
            img = ImageIO.read(url);
        } catch (IOException e) {
            return;
        }
    }
    public static void onRepaint(Graphics g) {
        g.drawImage(img, 3, 390, null);

        g.setColor(new Color(0, 252, 255));
        g.setFont(new Font("Serif", Font.PLAIN, 22));

        long totalTime = System.currentTimeMillis() - Variables.startTime;

        long seconds = totalTime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;

        String formattedTime =
                ((days == 0) ? "" : (days + ":" )) + (hours < 10 ? "0" : "") + hours + ":" +
                        (minutes < 10 ? "0" : "") + minutes + ":" +
                        (seconds < 10 ? "0" : "") + seconds;

        g.drawString(formattedTime, 162, 461);

        int XPGained = Skills.getExperience(Skills.FISHING) - Variables.startXP;
        int fishCaught = 0;
        for(int i : Variables.fishCaught) {
            fishCaught += i;
        }

        double fishPerMillisecond = fishCaught * 1.0 / totalTime;
        int fishPerHour = (int)(fishPerMillisecond * 3600 * 1000);

        int curLevel = Skills.getLevel(Skills.FISHING);
        int levelsGained = curLevel - Variables.startLevel;

        int XPTillLevel = Skills.getExperienceToLevel(Skills.FISHING, (levelsGained + Variables.startLevel + 1));

        double XPPerMillisecond = XPGained * 1.0 / totalTime;
        long timeTillLevel = XPPerMillisecond == 0 ? 0 : (long) (XPTillLevel / XPPerMillisecond);
        int xpPerHour = (int)(XPPerMillisecond * 3600 * 1000);

        seconds = timeTillLevel / 1000;
        minutes = seconds / 60;
        hours = minutes / 60;
        days = hours / 24;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;

        formattedTime =
                ((days == 0) ? "" : (days + ":" )) + (hours < 10 ? "0" : "") + hours + ":" +
                        (minutes < 10 ? "0" : "") + minutes + ":" +
                        (seconds < 10 ? "0" : "") + seconds;

        int profit = 0;

        int price = 0;

        for(int i = 0; i < Variables.fishCaught.length; i++) {
            if(Variables.dropTuna && Variables.chosenFishingType.getPossibleFish()[i] == Variables.TUNA_ID) {
                continue;
            }
            profit += Variables.fishCaught[i] * Variables.fishPrice[i];
        }

        double profitPerMillisecond = profit * 1.0 / totalTime;
        int profitPerHour = (int)(profitPerMillisecond * 3600 * 1000);

        g.drawString("" + XPGained+ " (" + xpPerHour + "/h)", 143, 500);
        g.drawString("" + fishCaught + " (" + fishPerHour + "/h)", 149, 540);
        g.drawString("" + curLevel + " (+" + levelsGained + ")", 436, 458);
        g.drawString(formattedTime, 340, 498);
        String profitDisplay = "N/A";
        if(Variables.bankingType != Variables.TYPE_POWERFISH && Variables.bankingType != Variables.TYPE_F1D1) {
            profitDisplay = profit + " (" + profitPerHour + "/h)";
        }
        g.drawString(profitDisplay, 360, 540);

        int mouseX = Mouse.getX();
        int mouseY = Mouse.getY();

        g.setColor(Color.red);
        g.drawLine(mouseX - 5, mouseY, mouseX + 5, mouseY);
        g.drawLine(mouseX, mouseY - 5, mouseX, mouseY + 5);

    }

}
package com.mycompany.lotto.action.euro;

import com.mycompany.lotto.action.Action;
import com.mycompany.lotto.context.Lotto5aus50;
import com.mycompany.lotto.context.Lotto6Aus49;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author saj
 */
public class LottoSaveWinnerNumbers6Aus49Action implements Action {

    private WebDriver driver;
    private Lotto6Aus49 lotto;

    public LottoSaveWinnerNumbers6Aus49Action(Lotto6Aus49 lotto, WebDriver driver) {
        this.driver = driver;
        this.lotto = lotto;
    }

    @Override
    public void perform() {
        lotto.getLocalDates().stream().forEach((LocalDate localDate) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String selectText = driver.findElement(By.xpath("//div[@data-groups='lotto, game']/div/div/select/option[1]")).getText();

            String day;
            if(localDate.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
                day = "Mittwoch";
            } else {
                day = "Samstag";
            }

            if (selectText.contains(",")) {
                day = day + ", " + localDate.format(formatter);
            } else {
                day = day + " " + localDate.format(formatter);
            }

            new Select(driver.findElement(By.xpath("//div[@data-groups='lotto, game']/div/div/select"))).selectByVisibleText(day);

            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(LottoSaveWinnerNumbers6Aus49Action.class.getName()).log(Level.SEVERE, null, ex);
            }
            new WebDriverWait(driver, 10).
                    until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-groups='lotto, game']/div/div/select")));

            for (int i = 1; i < 7; i++) {
                String text = driver.findElement(By.xpath("//div[@data-bind='with: lotto6aus49()']/div[@data-bind='text: zahl(" + i + ")']")).getText();
                lotto.getWinnerNumbers(localDate).add(Integer.parseInt(text));
            }

            String text = driver.findElement(By.xpath("//div[@data-bind='with: lotto6aus49()']/div[@class='sz']")).getText();
            lotto.getAdditionalWinnerNumbers(localDate).add(Integer.parseInt(text));
        });
    }
}

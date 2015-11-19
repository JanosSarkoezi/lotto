package com.mycompany.lotto.action.euro;

import com.mycompany.lotto.action.Action;
import com.mycompany.lotto.context.Lotto5aus50;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author saj
 */
public class LottoSaveWinnerNumbersAction implements Action {

    private WebDriver driver;
    private Lotto5aus50 lotto;

    public LottoSaveWinnerNumbersAction(Lotto5aus50 lotto, WebDriver driver) {
        this.driver = driver;
        this.lotto = lotto;
    }

    @Override
    public void perform() {
        lotto.getLocalDates().stream().forEach((LocalDate localDate) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String selectText = driver.findElement(By.xpath("//div[@data-groups='ej, game']/div/div/select/option[1]")).getText();

            String friday = "Freitag";
            if (selectText.contains(",")) {
                friday = friday + ", " + localDate.format(formatter);
            } else {
                friday = friday + " " + localDate.format(formatter);
            }
            
            new Select(driver.findElement(By.xpath("//div[@data-groups='ej, game']/div/div/select"))).selectByVisibleText(friday);
            //driver.navigate().refresh();
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(LottoSaveWinnerNumbersAction.class.getName()).log(Level.SEVERE, null, ex);
            }
            new WebDriverWait(driver, 10).
                    until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-groups='ej, game']/div/div/select")));

            for (int i = 1; i < 6; i++) {
                String text = driver.findElement(By.xpath("//div[@data-bind='with: ej()']/div[@data-bind='text: zahl(" + i + ")']")).getText();
                lotto.getWinnerNumbers(localDate).add(Integer.parseInt(text));
            }

            for (int i = 1; i < 3; i++) {
                String text = driver.findElement(By.xpath("//div[@data-bind='with: ej()']/div[@data-bind='text: eurozahl(" + i + ")']")).getText();
                lotto.getAdditionalWinnerNumbers(localDate).add(Integer.parseInt(text));
            }
        });
    }
}

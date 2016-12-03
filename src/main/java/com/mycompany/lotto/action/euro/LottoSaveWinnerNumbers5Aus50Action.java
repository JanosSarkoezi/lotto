package com.mycompany.lotto.action.euro;

import com.mycompany.lotto.action.Action;
import com.mycompany.lotto.context.Lotto5aus50;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author saj
 */
public class LottoSaveWinnerNumbers5Aus50Action implements Action {

    private WebDriver driver;
    private Lotto5aus50 lotto;

    public LottoSaveWinnerNumbers5Aus50Action(Lotto5aus50 lotto, WebDriver driver) {
        this.driver = driver;
        this.lotto = lotto;
    }

    @Override
    public void perform() {
        lotto.getLocalDates().stream().forEach(new FillLotto());
    }

    private class FillLotto implements Consumer<LocalDate> {
        private final By FIRST_OPTION_IN_SELECTION = By.xpath("//div[@data-groups='ej, game']/div/div/select/option[1]");
        private final By SELECTION = By.xpath("//div[@data-groups='ej, game']/div/div/select");

        @Override
        public void accept(LocalDate localDate) {
            new Select(driver.findElement(SELECTION)).selectByVisibleText(calculateVisibleText(localDate));

            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(LottoSaveWinnerNumbers5Aus50Action.class.getName()).log(Level.SEVERE, null, ex);
            }

            new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(SELECTION));

            for (int i = 1; i < 6; i++) {
                String text = driver.findElement(By.xpath("//div[@data-bind='with: ej()']/div[@data-bind='text: zahl(" + i + ")']")).getText();
                lotto.getWinnerNumbers(localDate).add(Integer.parseInt(text));
            }

            for (int i = 1; i < 3; i++) {
                String text = driver.findElement(By.xpath("//div[@data-bind='with: ej()']/div[@data-bind='text: eurozahl(" + i + ")']")).getText();
                lotto.getAdditionalWinnerNumbers(localDate).add(Integer.parseInt(text));
            }
        }

        /**
         * Ermittelt ein Text, um aus der Auswahlbox 'Gewinnzahlen' eine Auswahl treffen zu können. Dieser Text besteht
         * aus dem Wochentag Freeitag gefolgt von einer Datumsangabe. Dieser Text kann durch eine Komma getrennt werden.
         * Ist das der Fall, so wird der Text aus dem Tag getrennt mit Komman und dann dem Datum zurückgegeben
         * (Wochentag + ', ' + Datum). Manchmal wird die Trennung mit dem Komma weggelassen. In diesem Falle wird nur
         * (Wochentag + ' ' + Datum) zurückgegeben.
         *
         * @param localDate Datumsangabe. Ist ein Freitag.
         * @return Der ermittelter Text.
         */
        private String calculateVisibleText(LocalDate localDate) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String selectText = driver.findElement(FIRST_OPTION_IN_SELECTION).getText();

            String friday = "Freitag";
            if (selectText.contains(",")) {
                friday = friday + ", " + localDate.format(formatter);
            } else {
                friday = friday + " " + localDate.format(formatter);
            }

            return friday;
        }
    }
}

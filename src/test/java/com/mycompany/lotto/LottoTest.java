package com.mycompany.lotto;

import com.mycompany.lotto.action.Macro;
import com.mycompany.lotto.action.euro.LottoClickEuroAction;
import com.mycompany.lotto.action.euro.LottoSaveWinnerNumbers5Aus50Action;
import com.mycompany.lotto.action.euro.LottoSaveWinnerNumbers6Aus49Action;
import com.mycompany.lotto.context.Lotto5aus50;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.mycompany.lotto.context.Lotto6Aus49;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author saj
 */
public class LottoTest {

    WebDriver driver;

    public LottoTest() {
    }

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
        driver.get("https://www.lotto.de");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Ignore
    public void testLotto5Aus50() {
        Lotto5aus50 lotto = new Lotto5aus50().
                from(LocalDate.of(2015, 10, 02)).
                to(LocalDate.of(2015, 11, 20)).generate();

        Macro macro = new Macro().name("5 aus 50");

        macro.addAction(new LottoClickEuroAction(driver));
        macro.addAction(new LottoSaveWinnerNumbers5Aus50Action(lotto, driver));

        macro.perform();

        evaluate5(lotto.add5(16, 21, 28, 44, 49).add2(1, 2));
        evaluate5(lotto.add5(2, 11, 20, 26, 33).add2(1, 2));
    }

    @Test
    public void testLotto6Aus49() {
        Lotto6Aus49 lotto = new Lotto6Aus49().
                from(LocalDate.of(2016, 3, 26)).
                to(LocalDate.of(2016, 6, 1)).generate();

        Macro macro = new Macro().name("6 aus 49");

        macro.addAction(new LottoSaveWinnerNumbers6Aus49Action(lotto, driver));

        macro.perform();

        evaluate6(lotto.add6(9, 11, 15, 34, 35, 49).addSuperNumber(0));
        evaluate6(lotto.add6(13, 23, 25, 32, 34, 48).addSuperNumber(0));
    }

    private void evaluate5(Lotto5aus50 lotto) {
        System.out.println("Zahlen: " + lotto.getNumbers() + " " + lotto.getAdditionalNumbers());
        
        lotto.getLocalDates().stream().forEach((localDate) -> {
            long count1 = 0;
            count1 = lotto.getNumbers().stream().
                    map((number) -> lotto.getWinnerNumbers(localDate).stream().filter(n -> n.equals(number)).count()).
                    reduce(count1, (a, b) -> a + b);

            long count2 = 0;
            count2 = lotto.getAdditionalNumbers().stream().
                    map((number) -> lotto.getAdditionalWinnerNumbers(localDate).stream().filter(n -> n.equals(number)).count()).
                    reduce(count2, (a, b) -> a + b);

            System.out.println("Treffer [" + localDate + "]: "
                    + lotto.getWinnerNumbers(localDate) + "(" + count1 + ") "
                    + lotto.getAdditionalWinnerNumbers(localDate) + "(" + count2 + ")");
        });
    }

    private void evaluate6(Lotto6Aus49 lotto) {
        System.out.println("Zahlen: " + lotto.getNumbers() + " " + lotto.getAdditionalNumbers());

        lotto.getLocalDates().stream().forEach((localDate) -> {
            long count1 = 0;
            count1 = lotto.getNumbers().stream().
                    map((number) -> lotto.getWinnerNumbers(localDate).stream().filter(n -> n.equals(number)).count()).
                    reduce(count1, (a, b) -> a + b);

            long count2 = 0;
            count2 = lotto.getAdditionalNumbers().stream().
                    map((number) -> lotto.getAdditionalWinnerNumbers(localDate).stream().filter(n -> n.equals(number)).count()).
                    reduce(count2, (a, b) -> a + b);

            System.out.println("Treffer [" + localDate + "]: "
                    + lotto.getWinnerNumbers(localDate) + "(" + count1 + ") "
                    + lotto.getAdditionalWinnerNumbers(localDate) + "(" + count2 + ")");
        });
    }

    private String join(List<Integer> values) {
        return values.stream().
                map(Object::toString).
                collect(Collectors.joining(", "));
    }
}

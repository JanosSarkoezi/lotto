package com.mycompany.lotto;

import com.mycompany.lotto.action.Macro;
import com.mycompany.lotto.action.euro.LottoClickEuroAction;
import com.mycompany.lotto.action.euro.LottoSaveWinnerNumbers5Aus50Action;
import com.mycompany.lotto.action.euro.LottoSaveWinnerNumbers6Aus49Action;
import com.mycompany.lotto.context.Lotto5aus50;
import com.mycompany.lotto.context.Lotto6Aus49;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        System.setProperty("webdriver.chrome.driver", "/home/saj/tmp/chromedriver");
        driver = new ChromeDriver();

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
    public void testLotto6Aus49() {
        Lotto6Aus49 lotto = new Lotto6Aus49().
                from(LocalDate.of(2017, 5, 6)).
                to(LocalDate.of(2017, 5, 24)).generate();

        Macro macro = new Macro().name("6 aus 49");

        macro.addAction(new LottoSaveWinnerNumbers6Aus49Action(lotto, driver));

        macro.perform();

        evaluate6(lotto.add6(2, 11, 15, 19, 37, 40).addSuperNumber(1));
        evaluate6(lotto.add6(4, 12, 15, 29, 38, 47).addSuperNumber(1));
        evaluate6(lotto.add6(6, 23, 26, 30, 38, 48).addSuperNumber(1));
    }

    @Test
    @Ignore
    public void testLotto5Aus50() {
        Lotto5aus50 lotto = new Lotto5aus50().
                from(LocalDate.of(2016, 8, 12)).
                to(LocalDate.of(2016, 10, 14)).generate();

        Macro macro = new Macro().name("5 aus 50");

        macro.addAction(new LottoClickEuroAction(driver));
        macro.addAction(new LottoSaveWinnerNumbers5Aus50Action(lotto, driver));

        macro.perform();

        evaluate5(lotto.add5(2, 6, 13, 24, 42).add2(2, 10));
        evaluate5(lotto.add5(2, 30, 38, 46, 48).add2(4, 7));
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

            System.out.println("Treffer [" + localDate + "]: ["
                    + join(lotto.getWinnerNumbers(localDate)) + "] (" + count1 + ") "
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

            System.out.println("Treffer [" + localDate + "]: ["
                    + join(lotto.getWinnerNumbers(localDate)) + "] (" + count1 + ") "
                    + lotto.getAdditionalWinnerNumbers(localDate) + "(" + count2 + ")");
        });
    }

    private String join(List<Integer> values) {
        return values.stream().
                map(new NumberMapper()).
                collect(Collectors.joining(", "));
    }

    private class NumberMapper implements Function<Integer, String> {

        @Override
        public String apply(Integer value) {
            if(value > 9) {
                return value.toString();
            }
            return "0" + value.toString();
        }
    }
}

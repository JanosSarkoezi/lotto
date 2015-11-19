package com.mycompany.lotto;

import com.mycompany.lotto.action.Macro;
import com.mycompany.lotto.action.euro.LottoClickEuroAction;
import com.mycompany.lotto.action.euro.LottoSaveWinnerNumbersAction;
import com.mycompany.lotto.context.Lotto5aus50;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import static java.time.temporal.TemporalAdjusters.next;
import static java.time.DayOfWeek.FRIDAY;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author saj
 */
public class Lotto {

    WebDriver driver;

    public Lotto() {
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
    // @Ignore
    public void testLotto5Aus50() {
        Lotto5aus50 lotto = new Lotto5aus50().
                from(LocalDate.of(2015, 10, 02)).
                to(LocalDate.of(2015, 11, 20)).generate();

        Macro macro = new Macro().name("5 aus 50");

        macro.addAction(new LottoClickEuroAction(driver));
        macro.addAction(new LottoSaveWinnerNumbersAction(lotto, driver));

        macro.perform();

        evaluate(lotto.add5(16, 21, 28, 44, 49).add2(1, 2));
        evaluate(lotto.add5(2, 11, 20, 26, 33).add2(1, 2));

        Assert.assertTrue(false);
    }

    @Test
    @Ignore
    public void testbla() {
        bla();
        Assert.assertTrue(false);
    }

    private void evaluate(Lotto5aus50 lotto) {
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

    private void bla() {
        LocalDate startDate = LocalDate.of(2015, 5, 15);
        LocalDate endDate = LocalDate.of(2015, 6, 5);

        List<LocalDate> localDates = new ArrayList<>();
        LocalDate temp = startDate;
        while (temp.isBefore(endDate) || temp.isEqual(endDate)) {
            localDates.add(temp);
            temp = temp.with(next(FRIDAY));
        }

        localDates.stream().forEach(System.out::println);
    }
}

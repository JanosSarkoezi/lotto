package com.mycompany.lotto.action.euro;

import com.mycompany.lotto.action.Action;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author saj
 */
public class LottoClickEuroAction implements Action {
    
    private WebDriver driver;

    public LottoClickEuroAction(WebDriver driver) {
        this.driver = driver;
    }
    
    @Override
    public void perform() {
        driver.findElement(By.xpath("//li[@class='ej']/a[@class='ko-jackpot']")).click();
    }
}

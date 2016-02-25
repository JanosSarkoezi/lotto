/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lotto.context;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.time.temporal.TemporalAdjusters.next;
import static java.time.DayOfWeek.FRIDAY;

/**
 *
 * @author saj
 */
public class Lotto6Aus49 {

    private LocalDate to;
    private LocalDate from;

    private final List<LocalDate> localDates = new ArrayList<>();
    private final List<Integer> numbers = new ArrayList<>();
    private final List<Integer> additionalNumbers = new ArrayList<>();

    private final Map<LocalDate, List<Integer>> winnerMap = new HashMap<>();
    private final Map<LocalDate, List<Integer>> additionalWinnerMap = new HashMap<>();

    public LocalDate getFrom() {
        return from;
    }

    public Lotto6Aus49 generate() {
        LocalDate temp = from;
        LocalDate wednesday = temp.with(next(WEDNESDAY));
        LocalDate saturday = temp.with(next(SATURDAY));
        boolean nextDayIsWednesday = wednesday.isBefore(saturday);
        if(nextDayIsWednesday) {
            temp = wednesday;
        } else {
            temp = saturday;
        }

        while (temp.isBefore(to) || temp.isEqual(to)) {
            if (temp.isAfter(LocalDate.now()) || temp.isEqual(LocalDate.now())) {
                break;
            }

            winnerMap.put(temp, new ArrayList<>());
            additionalWinnerMap.put(temp, new ArrayList<>());
            localDates.add(temp);

            if(nextDayIsWednesday) {
                temp = temp.with(next(SATURDAY));
                nextDayIsWednesday = false;
            } else {
                temp = temp.with(next(WEDNESDAY));
                nextDayIsWednesday = true;
            }
        }

        return this;
    }

    public Lotto6Aus49 add6(Integer... numbers) {
        if (numbers.length != 6) {
            throw new IllegalStateException("6 aus 49");
        }

        this.numbers.clear();
        this.numbers.addAll(Arrays.asList(numbers));
        return this;
    }

    public Lotto6Aus49 addSuperNumber(Integer number) {
        this.additionalNumbers.clear();
        this.additionalNumbers.addAll(Arrays.asList(number));
        return this;
    }

    public Lotto6Aus49 from(LocalDate from) {
        this.from = from;
        return this;
    }

    public LocalDate getTo() {
        return to;
    }

    public Lotto6Aus49 to(LocalDate to) {
        this.to = to;
        return this;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public List<Integer> getAdditionalNumbers() {
        return additionalNumbers;
    }

    public List<LocalDate> getLocalDates() {
        return localDates;
    }

    public List<Integer> getWinnerNumbers(LocalDate localDate) {
        return winnerMap.get(localDate);
    }

    public List<Integer> getAdditionalWinnerNumbers(LocalDate localDate) {
        return additionalWinnerMap.get(localDate);
    }
}

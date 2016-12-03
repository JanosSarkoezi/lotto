/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lotto.context;

import java.time.LocalDate;
import java.util.*;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.time.temporal.TemporalAdjusters.next;

/**
 * @author saj
 */
public class Lotto6Aus49 {

    private LocalDate to;
    private LocalDate from;

    private final List<LocalDate> localDates = new ArrayList<>();
    private final List<Integer> numbers = new ArrayList<>();
    private final List<Integer> additionalNumbers = new ArrayList<>();
    private final List<Integer> game77 = new ArrayList<>();
    private final List<Integer> gluecksspirale = new ArrayList<>();

    private final Map<LocalDate, List<Integer>> date2WinnerNumbers = new HashMap<>();
    private final Map<LocalDate, List<Integer>> date2AdditionalWinnerNumbers = new HashMap<>();
    private final Map<LocalDate, List<Integer>> date2Game77 = new HashMap<>();
    private final Map<LocalDate, List<Integer>> date2Gluecksspirale = new HashMap<>();

    public LocalDate getFrom() {
        return from;
    }

    public Lotto6Aus49 generate() {
        LocalDate temp = from;
        LocalDate wednesday = temp.with(next(WEDNESDAY));
        LocalDate saturday = temp.with(next(SATURDAY));
        boolean nextDayIsWednesday = wednesday.isBefore(saturday);
        if (nextDayIsWednesday) {
            temp = wednesday;
        } else {
            temp = saturday;
        }

        while (temp.isBefore(to) || temp.isEqual(to)) {
            if (temp.isAfter(LocalDate.now()) || temp.isEqual(LocalDate.now())) {
                break;
            }

            date2WinnerNumbers.put(temp, new ArrayList<>());
            date2AdditionalWinnerNumbers.put(temp, new ArrayList<>());
            date2Game77.put(temp, new ArrayList<>());
            date2Gluecksspirale.put(temp, new ArrayList<>());
            localDates.add(temp);

            if (nextDayIsWednesday) {
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

    public Lotto6Aus49 addGame77(Integer number) {
        this.game77.clear();
        this.game77.addAll(Arrays.asList(number));
        return this;
    }

    public Lotto6Aus49 addGluecksspirale(Integer number) {
        this.gluecksspirale.clear();
        this.gluecksspirale.addAll(Arrays.asList(number));
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

    public List<Integer> getGame77() {
        return game77;
    }

    public List<Integer> getGluecksspirale() {
        return gluecksspirale;
    }

    public List<Integer> getAdditionalNumbers() {
        return additionalNumbers;
    }

    public List<LocalDate> getLocalDates() {
        return localDates;
    }

    public List<Integer> getWinnerNumbers(LocalDate localDate) {
        return date2WinnerNumbers.get(localDate);
    }

    public List<Integer> getAdditionalWinnerNumbers(LocalDate localDate) {
        return date2AdditionalWinnerNumbers.get(localDate);
    }
}

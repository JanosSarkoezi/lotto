package com.mycompany.lotto.context;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.temporal.TemporalAdjusters.next;

/**
 *
 * @author saj
 */
public class Lotto5aus50 {

    private LocalDate to;
    private LocalDate from;

    private final List<LocalDate> localDates = new ArrayList<>();
    private final List<Integer> numbers = new ArrayList<>();
    private final List<Integer> additionalNumbers = new ArrayList<>();

    private final Map<LocalDate, List<Integer>> winnerMap = new HashMap<>();
    private final Map<LocalDate, List<Integer>> additionalWinnerMap = new HashMap<>();

    public Lotto5aus50 generate() {
        LocalDate temp = from;
        while (temp.isBefore(to) || temp.isEqual(to)) {
            if (temp.getDayOfWeek() != DayOfWeek.FRIDAY) {
                temp = temp.with(next(FRIDAY));
                continue;
            }

            if (temp.isAfter(LocalDate.now()) || temp.isEqual(LocalDate.now())) {
                break;
            }

            winnerMap.put(temp, new ArrayList<>());
            additionalWinnerMap.put(temp, new ArrayList<>());
            localDates.add(temp);

            temp = temp.with(next(FRIDAY));
        }

        return this;
    }

    public List<LocalDate> getLocalDates() {
        return localDates;
    }

    public LocalDate getFrom() {
        return from;
    }

    public Lotto5aus50 from(LocalDate from) {
        this.from = from;
        return this;
    }

    public LocalDate getTo() {
        return to;
    }

    public Lotto5aus50 to(LocalDate to) {
        this.to = to;
        return this;
    }

    public Lotto5aus50 add5(Integer... numbers) {
        if (numbers.length != 5) {
            throw new IllegalStateException("5 aus 50");
        }

        this.numbers.clear();
        this.numbers.addAll(Arrays.asList(numbers));
        return this;
    }

    public Lotto5aus50 add2(Integer... numbers) {
        if (numbers.length != 2) {
            throw new IllegalStateException("2 aus 10");
        }

        this.additionalNumbers.clear();
        this.additionalNumbers.addAll(Arrays.asList(numbers));
        return this;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public List<Integer> getAdditionalNumbers() {
        return additionalNumbers;
    }

    public List<Integer> getWinnerNumbers(LocalDate localDate) {
        return winnerMap.get(localDate);
    }

    public List<Integer> getAdditionalWinnerNumbers(LocalDate localDate) {
        return additionalWinnerMap.get(localDate);
    }
}

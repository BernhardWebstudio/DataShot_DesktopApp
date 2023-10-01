package edu.harvard.mcz.imagecapture.query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringToDateQueryParser {
    Date lowerBound = null;
    Date upperBound = null;

    public StringToDateQueryParser(String code) {
        int wildcardCount = code.length() - code.replace("%", "").length();
        if (wildcardCount > 1) {
            throw new IllegalArgumentException("Too many wildcards in date search.");
        }
        int dotCount = code.length() - code.replace(".", "").length();
        int minusCount = code.length() - code.replace("-", "").length();
        int slashCount = code.length() - code.replace("/", "").length();
        if (minusCount > 1) {
            if (slashCount > 0) {
                throw new IllegalArgumentException("Unknown date format");
            }
            // else, just one format of type kind
        }

        HashMap<String, MatchInterpreter> regexers = new HashMap<String, MatchInterpreter>() {{
            put("(\\d{4})[/,\\.\\-]?%?", m -> {
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return new DateTuple(
                            ft.parse(m.group(1) + "-01-01"),
                            ft.parse(m.group(1) + "-12-31")
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            });
            put("(\\d{1,2})[/,\\.\\-]{1}(\\d{4})[/,\\.\\-]?%?", m -> {
                YearMonth ym = YearMonth.of(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
                return new DateTuple(
                        ym.atDay(1),
                        ym.atEndOfMonth()
                );
            });
            put("(\\d{4})[/,\\.\\-]{1}(\\d{1,2})[/,\\.\\-]?%?", m -> {
                YearMonth ym = YearMonth.of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
                return new DateTuple(
                        ym.atDay(1),
                        ym.atEndOfMonth()
                );
            });
            put("(\\d{4})[/,\\.\\-]{1}(\\d{1,2})[/,\\.\\-]{1}(\\d{1,2})", m -> {
                LocalDate date = LocalDate.of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
                return new DateTuple(date);
            });
            put("(\\d{1,2})[/,\\.\\-]{1}(\\d{1,2})[/,\\.\\-]{1}(\\d{4})", m -> {
                LocalDate date = LocalDate.of(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
                return new DateTuple(date);
            });
        }};

        for (Map.Entry<String, MatchInterpreter> entry : regexers.entrySet()) {
            Pattern pattern = Pattern.compile(entry.getKey(), Pattern.CASE_INSENSITIVE);
            Matcher m = pattern.matcher(code.trim());
            if (m.matches()) {
                DateTuple result = entry.getValue().interpretMatch(m);
                this.lowerBound = result.lowerBound;
                this.upperBound = result.upperBound;
                return;
            }
        }

        throw new IllegalArgumentException("Date string could not be interpreted to a tuple.");
    }

    public Date getDateLowerBound() {
        return this.lowerBound;
    }

    public Date getDateUpperBound() {
        return this.upperBound;
    }

    protected interface MatchInterpreter {
        DateTuple interpretMatch(Matcher m);
    }

    protected class DateTuple {
        public Date lowerBound;
        public Date upperBound;

        public DateTuple(Date lowerBound, Date upperBound) {
            assert lowerBound.before(upperBound);
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        public DateTuple(LocalDate startDay, LocalDate endDay) {
            this(
                    Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).plusHours(24).toInstant())
            );
        }

        public DateTuple(LocalDate date) {
            this(
                    date, date
            );
        }
    }
}

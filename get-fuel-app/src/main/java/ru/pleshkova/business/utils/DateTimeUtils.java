package ru.pleshkova.business.utils;

import com.google.protobuf.Timestamp;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;


@UtilityClass
public class DateTimeUtils {

    private final DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Преобразование {@link Timestamp} в {@link LocalDateTime} без учёта часового пояса
     *
     * @param timestamp дата/время в gRPC-формате
     * @return дата/время в часовой зоне {@link ZoneOffset#UTC}
     */
    public static LocalDateTime localDateTime(final Timestamp timestamp) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()), ZoneOffset.UTC);
    }

    /**
     * Преобразование {@link Timestamp} в {@link LocalDate} без учёта часового пояса
     *
     * @param timestamp дата/время в gRPC-формате
     * @return дата в часовой зоне {@link ZoneOffset#UTC}
     */
    public LocalDate localDate(final Timestamp timestamp) {
        return localDateTime(timestamp).toLocalDate();
    }

    /**
     * Преобразование {@link LocalDateTime} в {@link Timestamp} без учёта часового пояса
     *
     * @param localDateTimeUtc дата/время в часовой зоне {@link ZoneOffset#UTC}
     * @return дата/время в gRPC-формате
     */
    public static Timestamp timestamp(final LocalDateTime localDateTimeUtc) {
        final Instant instant = localDateTimeUtc.toInstant(ZoneOffset.UTC);
        return Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).setNanos(instant.getNano())
                .build();
    }

    /**
     * Преобразование {@link LocalDate} в {@link Timestamp} без учёта часового пояса
     *
     * @param localDateUtc дата в часовой зоне {@link ZoneOffset#UTC}
     * @return дата/время в gRPC-формате (время обрезается до начала дня
     * {@link java.time.LocalTime#MIDNIGHT})
     */
    public Timestamp timestamp(final LocalDate localDateUtc) {
        return timestamp(localDateUtc.atStartOfDay());
    }

    /**
     * @return дата/время в часовой зоне {@link ZoneOffset#UTC}
     */
    public LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    /**
     * @return дата в часовой зоне {@link ZoneOffset#UTC}
     */
    public LocalDate today() {
        return LocalDate.now(ZoneOffset.UTC);
    }

    /**
     * @param stringDate в формате "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss" или "yyyy-MM-dd'T'HH:mm:ssX"
     * @return дата в часовой зоне {@link ZoneOffset#UTC}
     */
    public LocalDate dateFromString(final String stringDate) {
        return Stream.of(
                        customFormatter,
                        DateTimeFormatter.ISO_LOCAL_DATE,
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                )
                .flatMap(formatter -> {
                    try {
                        return Stream.of(LocalDate.parse(stringDate, formatter));
                    } catch (DateTimeParseException e) {
                        return Stream.empty();
                    }
                })
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot parse date: " + stringDate));
    }

    /**
     * @param date дата в часовой зоне {@link ZoneOffset#UTC}
     * @return stringDate в формате "yyyy-MM-dd"
     */
    public String stringFromDate(final LocalDate date) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * @param stringDateTime в формате "yyyy-MM-dd'T'HH:mm:ss"
     * @return дата и время в часовой зоне {@link ZoneOffset#UTC}
     */
    public LocalDateTime dateTimeFromString(final String stringDateTime) {
        return LocalDateTime.parse(stringDateTime).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }


}
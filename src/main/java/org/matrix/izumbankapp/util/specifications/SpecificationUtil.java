package org.matrix.izumbankapp.util.specifications;

import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@UtilityClass
public class SpecificationUtil {
    public static <T> Specification<T> likeIgnoreCase(String attribute, String value) {
        return (root, query, criteriaBuilder) ->
                value == null || value.isBlank() ?
                        null : criteriaBuilder.like(criteriaBuilder.lower(root.get(attribute)),
                        "%" + value.toLowerCase() + "%");
    }

    public static <T> Specification<T> isEqual(String attribute, Object value) {
        return (root, query, criteriaBuilder) ->
                value == null ?
                        null : criteriaBuilder.equal(root.get(attribute), value);
    }

    public static <T> Specification<T> filterByDates(LocalDate createdAt, LocalDate endDate, String dateField) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (createdAt != null) {
                LocalDateTime startOfDay = createdAt.atStartOfDay();
                LocalDateTime endOfDay = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
                if (endOfDay != null) {
                    predicate = criteriaBuilder.between(root.get(dateField), startOfDay, endOfDay);
                } else {
                    predicate = criteriaBuilder.greaterThanOrEqualTo(root.get(dateField), startOfDay);
                }
            }
            return predicate;
        };
    }

    public static <T, Y extends Comparable<? super Y>> Specification<T> greaterThanOrEqualTo(String attribute, Y value) {
        return (root, query, criteriaBuilder) ->
                value == null ?
                        null : criteriaBuilder.greaterThanOrEqualTo(root.get(attribute), value);
    }

    public static <T, Y extends Comparable<? super Y>> Specification<T> lessThanOrEqualTo(String attribute, Y value) {
        return (root, query, criteriaBuilder) ->
                value == null ?
                        null : criteriaBuilder.lessThanOrEqualTo(root.get(attribute), value);
    }
}

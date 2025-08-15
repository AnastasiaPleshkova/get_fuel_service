package ru.pleshkova.business.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "vstamp")
    private long version;

    @Column(name = "internal_id")
    @UuidGenerator
    private UUID internalId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @EqualsAndHashCode.Exclude
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @EqualsAndHashCode.Exclude
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public String toString() {
        return "%s (id = %d)".formatted(getClass().getSimpleName(), id);
    }
}

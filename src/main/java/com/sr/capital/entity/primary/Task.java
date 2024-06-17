package com.sr.capital.entity.primary;

import com.sr.capital.helpers.enums.TaskStatus;
import com.sr.capital.helpers.enums.TaskType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static com.sr.capital.helpers.constants.Constants.EntityNames.TASK;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = TASK)
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class Task extends LongBaseEntity{

    @Column(name = "request_id")
    private String requestId;

    @Column(name="group_id")
    private Long groupId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Builder.Default
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PROCESSING;

    @Column(name = "remarks")
    private String remarks;

    @Builder.Default
    @Column(name = "retries")
    private Integer retries = 0;

    @Builder.Default
    @Column(name = "last_try_at")
    private LocalDateTime lastTryAt = LocalDateTime.now();
}

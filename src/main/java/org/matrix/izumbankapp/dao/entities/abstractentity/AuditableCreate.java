package org.matrix.izumbankapp.dao.entities.abstractentity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditableCreate implements Serializable {
    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;
}

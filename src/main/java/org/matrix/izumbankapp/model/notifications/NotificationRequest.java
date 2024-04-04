package org.matrix.izumbankapp.model.notifications;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.matrix.izumbankapp.enumeration.NotificationType;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    @NotBlank(message = "Message must not be null")
    private String message;
    @NotNull(message = "Type must not be null")
    private NotificationType type;
}

package ua.vspelykh.salon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.model.entity.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class FeedbackDto {

    private int id;
    private String client;
    private int mark;
    private String comment;
    private LocalDateTime date;

    public static class FeedbackDtoBuilder {
        private final User client;
        private final Feedback feedback;

        public FeedbackDtoBuilder(User client, Feedback feedback) {
            this.client = client;
            this.feedback = feedback;
        }

        public FeedbackDto build() {
            return new FeedbackDto(feedback.getId(), client.getName() + " " + client.getSurname(), feedback.getMark(),
                    feedback.getComment(), feedback.getDate());
        }
    }
}

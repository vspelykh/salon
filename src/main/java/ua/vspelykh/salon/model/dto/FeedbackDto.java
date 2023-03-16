package ua.vspelykh.salon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.model.entity.User;

import java.time.LocalDateTime;

/**
 * This class represents a feedback object in the application. It contains information such as the unique identifier
 * of the feedback, the name of the client who left the feedback, the mark given by the client, the comment provided
 * by the client, and the date and time when the feedback was left.
 * <p>
 * The FeedbackDtoBuilder class is used to create instances of FeedbackDto.
 *
 * @version 1.0
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class FeedbackDto {

    private int id;
    private String client;
    private int mark;
    private String comment;
    private LocalDateTime date;

    /**
     * A builder class to create instances of FeedbackDto.
     */
    public static class FeedbackDtoBuilder {
        private final User client;
        private final Feedback feedback;

        /**
         * Constructs a new FeedbackDtoBuilder with the specified client and feedback.
         *
         * @param client   the client who left the feedback.
         * @param feedback the feedback entity from which to create the FeedbackDto instance.
         */
        public FeedbackDtoBuilder(User client, Feedback feedback) {
            this.client = client;
            this.feedback = feedback;
        }

        /**
         * Builds and returns an instance of FeedbackDto using the provided client and feedback entities.
         *
         * @return an instance of FeedbackDto with the specified client and feedback entities.
         */
        public FeedbackDto build() {
            return new FeedbackDto(feedback.getId(), client.getName() + " " + client.getSurname(), feedback.getMark(),
                    feedback.getComment(), feedback.getDate());
        }
    }
}

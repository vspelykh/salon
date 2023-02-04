package ua.vspelykh.salon.dto;

import ua.vspelykh.salon.model.Mark;
import ua.vspelykh.salon.model.User;

import java.time.LocalDateTime;

public class MarkDto {

    private int id;
    private String client;
    private int mark;
    private String comment;
    private LocalDateTime date;

    public MarkDto(int id, String client, int mark, String comment, LocalDateTime date) {
        this.id = id;
        this.client = client;
        this.mark = mark;
        this.comment = comment;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public static class MarkDtoBuilder {
        private User client;
        private Mark mark;

        public MarkDtoBuilder(User client, Mark mark) {
            this.client = client;
            this.mark = mark;
        }

        public MarkDto build() {
            return new MarkDto(mark.getId(), client.getName() + " " + client.getSurname(), mark.getMark(),
                    mark.getComment(), mark.getDate());
        }
    }
}

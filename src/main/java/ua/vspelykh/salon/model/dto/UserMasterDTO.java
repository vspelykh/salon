package ua.vspelykh.salon.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.model.entity.UserLevel;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;

@Data
@EqualsAndHashCode(of = "id")
public class UserMasterDTO {

    private int id;
    private String name;
    private String surname;
    private String email;
    private String number;
    private String level;
    private String about;
    private String rating;

    private static DecimalFormat df = new DecimalFormat("0.00");

    public static UserMasterDTO build(User user, UserLevel userLevel, double rating, String locale) {
        UserMasterDTO dto = new UserMasterDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        dto.setNumber(user.getNumber());
        dto.setLevel(String.valueOf(userLevel.getLevel()));
        if (Objects.equals(locale, UA_LOCALE)) {
            dto.setAbout(userLevel.getAboutUa());
        } else {
            dto.setAbout(userLevel.getAbout());
        }
        if (Double.isNaN(rating)){
            dto.setRating("0.0");
        } else {
            df.setRoundingMode(RoundingMode.UP);
            dto.setRating(df.format(rating));
        }
        return dto;
    }
}

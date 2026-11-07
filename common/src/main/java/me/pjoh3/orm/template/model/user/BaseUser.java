package me.pjoh3.orm.template.model.user;

import com.j256.ormlite.field.DatabaseField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pjoh3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public abstract class BaseUser {
    @DatabaseField(generatedId = true, unique = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private String username;
}

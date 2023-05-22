package edu.jcourse.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    private Long id;
    private Movie movie;
    private User user;
    private String text;
    private Short rate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (!Objects.equals(id, review.id)) return false;
        if (!Objects.equals(Optional.ofNullable(movie).map(Movie::getId).orElse(null), Optional.ofNullable(review.movie).map(Movie::getId).orElse(null)))
            return false;
        if (!Objects.equals(user, review.user)) return false;
        if (!Objects.equals(text, review.text)) return false;
        return Objects.equals(rate, review.rate);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (Optional.ofNullable(movie).map(Movie::getId).map(Object::hashCode).orElse(0));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (rate != null ? rate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("{id=").append(id);
        sb.append(", movieId=").append(Optional.ofNullable(movie).map(Movie::getId).orElse(null));
        sb.append(", user=").append(user);
        sb.append(", text='").append(text).append('\'');
        sb.append(", rate=").append(rate);
        sb.append('}');
        return sb.toString();
    }
}
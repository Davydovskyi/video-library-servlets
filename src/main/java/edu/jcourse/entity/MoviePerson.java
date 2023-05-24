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
public class MoviePerson {
    private Long id;
    private Movie movie;
    private Person person;
    private PersonRole personRole;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoviePerson that = (MoviePerson) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(Optional.ofNullable(movie).map(Movie::getId).orElse(null), Optional.ofNullable(that.movie).map(Movie::getId).orElse(null)))
            return false;
        if (!Objects.equals(person, that.person)) return false;
        return personRole == that.personRole;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (Optional.ofNullable(movie).map(Movie::getId).map(Object::hashCode).orElse(0));
        result = 31 * result + (person != null ? person.hashCode() : 0);
        result = 31 * result + (personRole != null ? personRole.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("{id=").append(id);
        sb.append(", movieId=").append(Optional.ofNullable(movie).map(Movie::getId).orElse(null));
        sb.append(", person=").append(person);
        sb.append(", personRole=").append(personRole);
        sb.append('}');
        return sb.toString();
    }
}
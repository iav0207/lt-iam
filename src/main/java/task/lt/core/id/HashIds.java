package task.lt.core.id;

import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import org.hashids.Hashids;

/**
 * Encapsulation of DB id to/from public 'hash id' translation.
 * <p>
 * Wraps {@link Hashids} instance.
 */
@ParametersAreNonnullByDefault
abstract class HashIds {

    private final Hashids ids = new Hashids(salt(), minLength());

    /**
     * Translate into public id.
     *
     * @param id db identifier of an entity
     * @return Public hash id, {@link String}.
     */
    public String encode(long id) {
        return ids.encode(id);
    }

    /**
     * Translate public hash id into db identifier.
     *
     * @param hash public string id of an entity
     * @return Db identifier of the entity with the given public id as {@link Optional},
     * which is empty if the given public id cannot be decoded,
     * i.e. is most possibly invalid.
     */
    public Optional<Long> decode(String hash) {
        return Optional.of(ids.decode(hash))
                .filter(arr -> arr.length == 1)
                .map(arr -> arr[0]);
    }

    abstract String salt();

    abstract int minLength();

}

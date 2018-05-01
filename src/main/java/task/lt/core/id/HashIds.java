package task.lt.core.id;

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
     * @return Db identifier of the entity with the given public id.
     * @throws NoSuchIdException if the attempt to decode given hash failed.
     */
    public long decode(String hash) {
        try {
            return ids.decode(hash)[0];
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new NoSuchIdException();
        }
    }

    abstract String salt();

    abstract int minLength();

}

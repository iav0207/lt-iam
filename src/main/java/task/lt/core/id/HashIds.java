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
     * @param id db identifier of a potato bag
     * @return Public hash id, {@link String}.
     */
    public String encode(long id) {
        return ids.encode(id);
    }

    /**
     * Translate potato bag public hash id into db identifier.
     *
     * @param hash public string id of a potato bag
     * @return Db identifier of the potato bag with the given public id.
     */
    public long decode(String hash) {
        return ids.decode(hash)[0];
    }

    abstract String salt();

    abstract int minLength();

}

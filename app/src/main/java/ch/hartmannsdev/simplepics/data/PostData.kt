package ch.hartmannsdev.simplepics.data

import android.os.Parcel
import android.os.Parcelable

/**
 * Data class representing post information.
 *
 * @property postId The unique identifier of the post.
 * @property userId The unique identifier of the user who created the post.
 * @property username The username of the user who created the post.
 * @property userImage The URL of the user's profile image.
 * @property postImage The URL of the post image.
 * @property postDescription The description of the post.
 * @property time The timestamp of when the post was created.
 * @property likes The list of user IDs who liked the post.
 * @property searchTerms The list of search terms associated with the post.
 */
data class PostData(
    val postId: String? = null,
    val userId: String? = null,
    val username: String? = null,
    val userImage: String? = null,
    val postImage: String? = null,
    val postDescription: String? = null,
    val time: Long? = null,
    var likes: List<String>? = null,
    val searchTerms: List<String>? = null,
) : Parcelable {

    /**
     * Secondary constructor for creating a PostData object from a Parcel.
     *
     * @param parcel The Parcel to read the object's data from.
     */
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.createStringArrayList(),
        parcel.createStringArrayList()
    )

    /**
     * Writes the PostData object to a Parcel.
     *
     * @param parcel The Parcel to write the object's data into.
     * @param flags Additional flags about how the object should be written.
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(postId)
        parcel.writeString(userId)
        parcel.writeString(username)
        parcel.writeString(userImage)
        parcel.writeString(postImage)
        parcel.writeString(postDescription)
        parcel.writeValue(time)
        parcel.writeStringList(likes)
        parcel.writeStringList(searchTerms)
    }

    /**
     * Describes the contents of the Parcel.
     *
     * @return An integer bitmask indicating the set of special object types marshaled by the Parcelable.
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * Companion object to generate instances of the Parcelable class from a Parcel.
     */
    companion object CREATOR : Parcelable.Creator<PostData> {
        /**
         * Creates a PostData object from a Parcel.
         *
         * @param parcel The Parcel to read the object's data from.
         * @return A new instance of PostData.
         */
        override fun createFromParcel(parcel: Parcel): PostData {
            return PostData(parcel)
        }

        /**
         * Creates a new array of PostData.
         *
         * @param size The size of the array.
         * @return An array of PostData, with every entry initialized to null.
         */
        override fun newArray(size: Int): Array<PostData?> {
            return arrayOfNulls(size)
        }
    }
}

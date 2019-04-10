package roomdatabase.mykotlin.com.roomdatabase.Local

import android.arch.persistence.room.*
import io.reactivex.Flowable
import roomdatabase.mykotlin.com.roomdatabase.Model.User

@Dao
interface UserDAO {

    @get:Query( "SELECT * FROM users")
    val allusers: Flowable<List<User>>

    @Query("SELECT * FROM users WHERE id=:userId")
    fun getUserById(userId:Int): Flowable<User>

    @Insert
    fun insertUser(vararg users:User)

    @Update
    fun updateUser(vararg users: User)

    @Delete
    fun deleteUser(vararg user:User)

    @Query("DELETE FROM users")
    fun deleteAllUsers()


}
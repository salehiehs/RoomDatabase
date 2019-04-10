package roomdatabase.mykotlin.com.roomdatabase.Database

import io.reactivex.Flowable
import roomdatabase.mykotlin.com.roomdatabase.Model.User

interface IUserDatasource {

    val allUsers:Flowable<List<User>>
    fun getUserById(userId:Int):Flowable<User>
    fun InsertUser(vararg users:User)
    fun updateUser(vararg users:User)
    fun deleteUser(user:User)
    fun deleteAllUsers()

}
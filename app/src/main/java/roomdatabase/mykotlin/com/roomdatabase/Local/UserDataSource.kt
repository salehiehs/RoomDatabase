package roomdatabase.mykotlin.com.roomdatabase.Local

import io.reactivex.Flowable
import roomdatabase.mykotlin.com.roomdatabase.Database.IUserDatasource
import roomdatabase.mykotlin.com.roomdatabase.Model.User

class UserDataSource(private val userDAO:UserDAO):IUserDatasource {
    override val allUsers: Flowable<List<User>>
        get() = userDAO.allusers

    override fun getUserById(userId: Int): Flowable<User> {
        return userDAO.getUserById(userId)
        }

    override fun InsertUser(vararg users: User) {
        return userDAO.insertUser(*users)
    }

    override fun updateUser(vararg users: User) {
         return updateUser(*users)
            }

    override fun deleteUser(user: User) {
         return deleteUser(user)
        }

    override fun deleteAllUsers() {
        return userDAO.deleteAllUsers()
    }

    companion object {
        private var mInstance:UserDataSource?=null
        fun getInstance(userDAO: UserDAO):UserDataSource{
            if(mInstance==null)
                mInstance = UserDataSource(userDAO)
            return  mInstance!!
        }
    }
}
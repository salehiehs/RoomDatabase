package roomdatabase.mykotlin.com.roomdatabase.Database

import io.reactivex.Flowable
import roomdatabase.mykotlin.com.roomdatabase.Model.User

class UserRepository(private val mLocationDatasource: IUserDatasource):IUserDatasource {
    override val allUsers: Flowable<List<User>>
        get() = mLocationDatasource.allUsers

    override fun getUserById(userId: Int): Flowable<User> {
    return mLocationDatasource.getUserById(userId)
    }

    override fun InsertUser(vararg users: User) {
        return mLocationDatasource.InsertUser(*users)
    }

    override fun updateUser(vararg users: User) {
       return updateUser(*users)
    }

    override fun deleteUser(user: User) {
        return mLocationDatasource.deleteUser(user)
    }

    override fun deleteAllUsers() {
        mLocationDatasource.deleteAllUsers()
    }

    companion object {
        private var mInstance:UserRepository?=null
        fun getInstance(mLocationDatasource: IUserDatasource):UserRepository{
            if(mInstance==null)
                mInstance = UserRepository(mLocationDatasource)
            return  mInstance!!
        }
    }
}